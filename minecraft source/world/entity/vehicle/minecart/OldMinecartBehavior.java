/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OldMinecartBehavior
/*     */   extends MinecartBehavior
/*     */ {
/*     */   private static final double MINECART_RIDABLE_THRESHOLD = 0.01D;
/*     */   private static final double MAX_SPEED_IN_WATER = 0.2D;
/*     */   private static final double MAX_SPEED_ON_LAND = 0.4D;
/*     */   private static final double ABSOLUTE_MAX_SPEED = 0.4D;
/*     */   private final InterpolationHandler interpolation;
/*  38 */   private Vec3 targetDeltaMovement = Vec3.ZERO;
/*     */   
/*     */   public OldMinecartBehavior(AbstractMinecart minecart) {
/*  41 */     super(minecart);
/*  42 */     this.interpolation = new InterpolationHandler(minecart, this::onInterpolation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public InterpolationHandler getInterpolation() { return this.interpolation; }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public void onInterpolation(InterpolationHandler interpolation) { setDeltaMovement(this.targetDeltaMovement); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void lerpMotion(Vec3 movement) {
/*  56 */     this.targetDeltaMovement = movement;
/*  57 */     setDeltaMovement(this.targetDeltaMovement);
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     ServerLevel level;
/*  62 */     Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/*  63 */     else { if (this.interpolation.hasActiveInterpolation()) {
/*  64 */         this.interpolation.interpolate();
/*     */       } else {
/*  66 */         this.minecart.reapplyPosition();
/*  67 */         setXRot(getXRot() % 360.0F);
/*  68 */         setYRot(getYRot() % 360.0F);
/*     */       } 
/*     */       
/*     */       return; }
/*     */ 
/*     */     
/*  74 */     this.minecart.applyGravity();
/*     */     
/*  76 */     BlockPos pos = this.minecart.getCurrentBlockPosOrRailBelow();
/*  77 */     BlockState state = level().getBlockState(pos);
/*  78 */     boolean onRails = BaseRailBlock.isRail(state);
/*  79 */     this.minecart.setOnRails(onRails);
/*  80 */     if (onRails) {
/*  81 */       moveAlongTrack(level);
/*     */       
/*  83 */       if (state.is(Blocks.ACTIVATOR_RAIL)) {
/*  84 */         this.minecart.activateMinecart(level, pos.getX(), pos.getY(), pos.getZ(), ((Boolean)state.getValue(PoweredRailBlock.POWERED)).booleanValue());
/*     */       }
/*     */     } else {
/*  87 */       this.minecart.comeOffTrack(level);
/*     */     } 
/*     */     
/*  90 */     this.minecart.applyEffectsFromBlocks();
/*     */     
/*  92 */     setXRot(0.0F);
/*  93 */     double xDiff = this.minecart.xo - getX();
/*  94 */     double zDiff = this.minecart.zo - getZ();
/*  95 */     if (xDiff * xDiff + zDiff * zDiff > 0.001D) {
/*  96 */       setYRot((float)(Mth.atan2(zDiff, xDiff) * 180.0D / Math.PI));
/*  97 */       if (this.minecart.isFlipped()) {
/*  98 */         setYRot(getYRot() + 180.0F);
/*     */       }
/*     */     } 
/*     */     
/* 102 */     double rotDiff = Mth.wrapDegrees(getYRot() - this.minecart.yRotO);
/* 103 */     if (rotDiff < -170.0D || rotDiff >= 170.0D) {
/* 104 */       setYRot(getYRot() + 180.0F);
/* 105 */       this.minecart.setFlipped(!this.minecart.isFlipped());
/*     */     } 
/*     */     
/* 108 */     setXRot(getXRot() % 360.0F);
/* 109 */     setYRot(getYRot() % 360.0F);
/*     */     
/* 111 */     pushAndPickupEntities();
/*     */   }
/*     */   public void moveAlongTrack(ServerLevel level) {
/*     */     double progress;
/*     */     Vec3 moveIntent;
/* 116 */     BlockPos pos = this.minecart.getCurrentBlockPosOrRailBelow();
/* 117 */     BlockState state = level().getBlockState(pos);
/* 118 */     this.minecart.resetFallDistance();
/*     */     
/* 120 */     double x = this.minecart.getX();
/* 121 */     double y = this.minecart.getY();
/* 122 */     double z = this.minecart.getZ();
/* 123 */     Vec3 oldPos = getPos(x, y, z);
/* 124 */     y = pos.getY();
/*     */     
/* 126 */     boolean powerTrack = false;
/* 127 */     boolean haltTrack = false;
/*     */     
/* 129 */     if (state.is(Blocks.POWERED_RAIL)) {
/* 130 */       powerTrack = ((Boolean)state.getValue(PoweredRailBlock.POWERED)).booleanValue();
/* 131 */       haltTrack = !powerTrack;
/*     */     } 
/*     */     
/* 134 */     double slideSpeed = 0.0078125D;
/* 135 */     if (this.minecart.isInWater()) {
/* 136 */       slideSpeed *= 0.2D;
/*     */     }
/* 138 */     Vec3 movement = getDeltaMovement();
/* 139 */     RailShape shape = (RailShape)state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
/* 140 */     switch (shape) {
/*     */       case ASCENDING_EAST:
/* 142 */         setDeltaMovement(movement.add(-slideSpeed, 0.0D, 0.0D));
/* 143 */         y++;
/*     */         break;
/*     */       case ASCENDING_WEST:
/* 146 */         setDeltaMovement(movement.add(slideSpeed, 0.0D, 0.0D));
/* 147 */         y++;
/*     */         break;
/*     */       case ASCENDING_NORTH:
/* 150 */         setDeltaMovement(movement.add(0.0D, 0.0D, slideSpeed));
/* 151 */         y++;
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/* 154 */         setDeltaMovement(movement.add(0.0D, 0.0D, -slideSpeed));
/* 155 */         y++;
/*     */         break;
/*     */     } 
/*     */     
/* 159 */     movement = getDeltaMovement();
/*     */     
/* 161 */     Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
/* 162 */     Vec3i exit0 = (Vec3i)exits.getFirst();
/* 163 */     Vec3i exit1 = (Vec3i)exits.getSecond();
/*     */     
/* 165 */     double xD = (exit1.getX() - exit0.getX());
/* 166 */     double zD = (exit1.getZ() - exit0.getZ());
/* 167 */     double length = Math.sqrt(xD * xD + zD * zD);
/*     */     
/* 169 */     double flip = movement.x * xD + movement.z * zD;
/* 170 */     if (flip < 0.0D) {
/* 171 */       xD = -xD;
/* 172 */       zD = -zD;
/*     */     } 
/*     */     
/* 175 */     double pow = Math.min(2.0D, movement.horizontalDistance());
/*     */     
/* 177 */     movement = new Vec3(pow * xD / length, movement.y, pow * zD / length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     setDeltaMovement(movement);
/*     */     
/* 184 */     Entity controllingPassenger = this.minecart.getFirstPassenger();
/*     */     
/* 186 */     Entity entity = this.minecart.getFirstPassenger(); if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 187 */       moveIntent = player.getLastClientMoveIntent(); }
/*     */     else
/* 189 */     { moveIntent = Vec3.ZERO; }
/*     */ 
/*     */     
/* 192 */     if (controllingPassenger instanceof net.minecraft.world.entity.player.Player && moveIntent.lengthSqr() > 0.0D) {
/* 193 */       Vec3 riderMovement = moveIntent.normalize();
/* 194 */       double ownDist = getDeltaMovement().horizontalDistanceSqr();
/* 195 */       if (riderMovement.lengthSqr() > 0.0D && ownDist < 0.01D) {
/* 196 */         setDeltaMovement(getDeltaMovement().add(moveIntent.x * 0.001D, 0.0D, moveIntent.z * 0.001D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 202 */         haltTrack = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 207 */     if (haltTrack) {
/* 208 */       double speedLength = getDeltaMovement().horizontalDistance();
/* 209 */       if (speedLength < 0.03D) {
/* 210 */         setDeltaMovement(Vec3.ZERO);
/*     */       } else {
/* 212 */         setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.0D, 0.5D));
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     double x0 = pos.getX() + 0.5D + exit0.getX() * 0.5D;
/* 217 */     double z0 = pos.getZ() + 0.5D + exit0.getZ() * 0.5D;
/* 218 */     double x1 = pos.getX() + 0.5D + exit1.getX() * 0.5D;
/* 219 */     double z1 = pos.getZ() + 0.5D + exit1.getZ() * 0.5D;
/*     */     
/* 221 */     xD = x1 - x0;
/* 222 */     zD = z1 - z0;
/*     */ 
/*     */     
/* 225 */     if (xD == 0.0D) {
/* 226 */       progress = z - pos.getZ();
/* 227 */     } else if (zD == 0.0D) {
/* 228 */       progress = x - pos.getX();
/*     */     } else {
/* 230 */       double xx = x - x0;
/* 231 */       double zz = z - z0;
/*     */       
/* 233 */       progress = (xx * xD + zz * zD) * 2.0D;
/*     */     } 
/*     */     
/* 236 */     x = x0 + xD * progress;
/* 237 */     z = z0 + zD * progress;
/*     */     
/* 239 */     setPos(x, y, z);
/*     */     
/* 241 */     double scale = this.minecart.isVehicle() ? 0.75D : 1.0D;
/* 242 */     double maxSpeed = this.minecart.getMaxSpeed(level);
/*     */     
/* 244 */     movement = getDeltaMovement();
/* 245 */     this.minecart.move(MoverType.SELF, new Vec3(
/* 246 */           Mth.clamp(scale * movement.x, -maxSpeed, maxSpeed), 0.0D, 
/*     */           
/* 248 */           Mth.clamp(scale * movement.z, -maxSpeed, maxSpeed)));
/*     */ 
/*     */     
/* 251 */     if (exit0.getY() != 0 && Mth.floor(this.minecart.getX()) - pos.getX() == exit0.getX() && Mth.floor(this.minecart.getZ()) - pos.getZ() == exit0.getZ()) {
/* 252 */       setPos(this.minecart.getX(), this.minecart.getY() + exit0.getY(), this.minecart.getZ());
/* 253 */     } else if (exit1.getY() != 0 && Mth.floor(this.minecart.getX()) - pos.getX() == exit1.getX() && Mth.floor(this.minecart.getZ()) - pos.getZ() == exit1.getZ()) {
/* 254 */       setPos(this.minecart.getX(), this.minecart.getY() + exit1.getY(), this.minecart.getZ());
/*     */     } 
/*     */     
/* 257 */     setDeltaMovement(this.minecart.applyNaturalSlowdown(getDeltaMovement()));
/*     */     
/* 259 */     Vec3 newPos = getPos(this.minecart.getX(), this.minecart.getY(), this.minecart.getZ());
/* 260 */     if (newPos != null && oldPos != null) {
/* 261 */       double speed = (oldPos.y - newPos.y) * 0.05D;
/*     */       
/* 263 */       Vec3 vec3 = getDeltaMovement();
/* 264 */       double otherPow = vec3.horizontalDistance();
/* 265 */       if (otherPow > 0.0D) {
/* 266 */         setDeltaMovement(vec3.multiply((otherPow + speed) / otherPow, 1.0D, (otherPow + speed) / otherPow));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 272 */       setPos(this.minecart.getX(), newPos.y, this.minecart.getZ());
/*     */     } 
/*     */     
/* 275 */     int xn = Mth.floor(this.minecart.getX());
/* 276 */     int zn = Mth.floor(this.minecart.getZ());
/* 277 */     if (xn != pos.getX() || zn != pos.getZ()) {
/* 278 */       Vec3 vec3 = getDeltaMovement();
/* 279 */       double otherPow = vec3.horizontalDistance();
/* 280 */       setDeltaMovement(otherPow * (xn - pos
/* 281 */           .getX()), vec3.y, otherPow * (zn - pos
/*     */           
/* 283 */           .getZ()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (powerTrack) {
/* 289 */       Vec3 vec3 = getDeltaMovement();
/* 290 */       double speedLength = vec3.horizontalDistance();
/* 291 */       if (speedLength > 0.01D) {
/* 292 */         double speed = 0.06D;
/* 293 */         setDeltaMovement(vec3.add(vec3.x / speedLength * 0.06D, 0.0D, vec3.z / speedLength * 0.06D));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 300 */         Vec3 deltaMovement = getDeltaMovement();
/* 301 */         double dx = deltaMovement.x;
/* 302 */         double dz = deltaMovement.z;
/* 303 */         if (shape == RailShape.EAST_WEST) {
/* 304 */           if (this.minecart.isRedstoneConductor(pos.west())) {
/* 305 */             dx = 0.02D;
/* 306 */           } else if (this.minecart.isRedstoneConductor(pos.east())) {
/* 307 */             dx = -0.02D;
/*     */           } 
/* 309 */         } else if (shape == RailShape.NORTH_SOUTH) {
/* 310 */           if (this.minecart.isRedstoneConductor(pos.north())) {
/* 311 */             dz = 0.02D;
/* 312 */           } else if (this.minecart.isRedstoneConductor(pos.south())) {
/* 313 */             dz = -0.02D;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/* 318 */         setDeltaMovement(dx, deltaMovement.y, dz);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getPosOffs(double x, double y, double z, double offs) {
/* 324 */     int xt = Mth.floor(x);
/* 325 */     int yt = Mth.floor(y);
/* 326 */     int zt = Mth.floor(z);
/* 327 */     if (level().getBlockState(new BlockPos(xt, yt - 1, zt)).is(BlockTags.RAILS)) {
/* 328 */       yt--;
/*     */     }
/*     */     
/* 331 */     BlockState state = level().getBlockState(new BlockPos(xt, yt, zt));
/* 332 */     if (BaseRailBlock.isRail(state)) {
/* 333 */       RailShape shape = (RailShape)state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
/* 334 */       y = yt;
/* 335 */       if (shape.isSlope()) {
/* 336 */         y = (yt + 1);
/*     */       }
/*     */       
/* 339 */       Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
/* 340 */       Vec3i exit0 = (Vec3i)exits.getFirst();
/* 341 */       Vec3i exit1 = (Vec3i)exits.getSecond();
/*     */       
/* 343 */       double xD = (exit1.getX() - exit0.getX());
/* 344 */       double zD = (exit1.getZ() - exit0.getZ());
/* 345 */       double dd = Math.sqrt(xD * xD + zD * zD);
/* 346 */       xD /= dd;
/* 347 */       zD /= dd;
/*     */       
/* 349 */       x += xD * offs;
/* 350 */       z += zD * offs;
/*     */       
/* 352 */       if (exit0.getY() != 0 && Mth.floor(x) - xt == exit0.getX() && Mth.floor(z) - zt == exit0.getZ()) {
/* 353 */         y += exit0.getY();
/* 354 */       } else if (exit1.getY() != 0 && Mth.floor(x) - xt == exit1.getX() && Mth.floor(z) - zt == exit1.getZ()) {
/* 355 */         y += exit1.getY();
/*     */       } 
/*     */       
/* 358 */       return getPos(x, y, z);
/*     */     } 
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3 getPos(double x, double y, double z) {
/* 364 */     int xt = Mth.floor(x);
/* 365 */     int yt = Mth.floor(y);
/* 366 */     int zt = Mth.floor(z);
/* 367 */     if (level().getBlockState(new BlockPos(xt, yt - 1, zt)).is(BlockTags.RAILS)) {
/* 368 */       yt--;
/*     */     }
/*     */     
/* 371 */     BlockState state = level().getBlockState(new BlockPos(xt, yt, zt));
/* 372 */     if (BaseRailBlock.isRail(state)) {
/* 373 */       double progress; RailShape shape = (RailShape)state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
/*     */       
/* 375 */       Pair<Vec3i, Vec3i> exits = AbstractMinecart.exits(shape);
/* 376 */       Vec3i exit0 = (Vec3i)exits.getFirst();
/* 377 */       Vec3i exit1 = (Vec3i)exits.getSecond();
/*     */       
/* 379 */       double x0 = xt + 0.5D + exit0.getX() * 0.5D;
/* 380 */       double y0 = yt + 0.0625D + exit0.getY() * 0.5D;
/* 381 */       double z0 = zt + 0.5D + exit0.getZ() * 0.5D;
/* 382 */       double x1 = xt + 0.5D + exit1.getX() * 0.5D;
/* 383 */       double y1 = yt + 0.0625D + exit1.getY() * 0.5D;
/* 384 */       double z1 = zt + 0.5D + exit1.getZ() * 0.5D;
/*     */       
/* 386 */       double xD = x1 - x0;
/* 387 */       double yD = (y1 - y0) * 2.0D;
/* 388 */       double zD = z1 - z0;
/*     */ 
/*     */       
/* 391 */       if (xD == 0.0D) {
/* 392 */         progress = z - zt;
/* 393 */       } else if (zD == 0.0D) {
/* 394 */         progress = x - xt;
/*     */       } else {
/* 396 */         double xx = x - x0;
/* 397 */         double zz = z - z0;
/*     */         
/* 399 */         progress = (xx * xD + zz * zD) * 2.0D;
/*     */       } 
/*     */       
/* 402 */       x = x0 + xD * progress;
/* 403 */       y = y0 + yD * progress;
/* 404 */       z = z0 + zD * progress;
/* 405 */       if (yD < 0.0D) {
/* 406 */         y++;
/* 407 */       } else if (yD > 0.0D) {
/* 408 */         y += 0.5D;
/*     */       } 
/* 410 */       return new Vec3(x, y, z);
/*     */     } 
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 417 */   public double stepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) { return 0.0D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean pushAndPickupEntities() {
/* 422 */     AABB hitbox = this.minecart.getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D);
/* 423 */     if (this.minecart.isRideable() && getDeltaMovement().horizontalDistanceSqr() >= 0.01D) {
/* 424 */       List<Entity> entities = level().getEntities(this.minecart, hitbox, EntitySelector.pushableBy(this.minecart));
/* 425 */       if (!entities.isEmpty()) {
/* 426 */         for (Entity entity : entities) {
/* 427 */           if (entity instanceof net.minecraft.world.entity.player.Player || entity instanceof net.minecraft.world.entity.animal.golem.IronGolem || entity instanceof AbstractMinecart || this.minecart.isVehicle() || entity.isPassenger()) {
/* 428 */             entity.push(this.minecart); continue;
/*     */           } 
/* 430 */           entity.startRiding(this.minecart);
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 435 */       for (Entity entity : level().getEntities(this.minecart, hitbox)) {
/* 436 */         if (!this.minecart.hasPassenger(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
/* 437 */           entity.push(this.minecart);
/*     */         }
/*     */       } 
/*     */     } 
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 446 */   public Direction getMotionDirection() { return this.minecart.isFlipped() ? this.minecart.getDirection().getOpposite().getClockWise() : this.minecart.getDirection().getClockWise(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getKnownMovement(Vec3 knownMovement) {
/* 453 */     if (Double.isNaN(knownMovement.x) || Double.isNaN(knownMovement.y) || Double.isNaN(knownMovement.z)) {
/* 454 */       return Vec3.ZERO;
/*     */     }
/* 456 */     return new Vec3(
/* 457 */         Mth.clamp(knownMovement.x, -0.4D, 0.4D), knownMovement.y, 
/*     */         
/* 459 */         Mth.clamp(knownMovement.z, -0.4D, 0.4D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 465 */   public double getMaxSpeed(ServerLevel level) { return this.minecart.isInWater() ? 0.2D : 0.4D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 470 */   public double getSlowdownFactor() { return this.minecart.isVehicle() ? 0.997D : 0.96D; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\OldMinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */