/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FishingHook
/*     */   extends Projectile
/*     */ {
/*  55 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private boolean biting;
/*  57 */   private final RandomSource syncronizedRandom = RandomSource.create();
/*     */   private int outOfWaterTime;
/*     */   private static final int MAX_OUT_OF_WATER_TIME = 10;
/*     */   
/*  61 */   private enum FishHookState { FLYING, HOOKED_IN_ENTITY, BOBBING; }
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.INT);
/*  66 */   private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int life;
/*     */   
/*     */   private int nibble;
/*     */   private int timeUntilLured;
/*     */   private int timeUntilHooked;
/*     */   private float fishAngle;
/*     */   private boolean openWater = true;
/*     */   private Entity hookedIn;
/*  76 */   private FishHookState currentState = FishHookState.FLYING;
/*     */   
/*     */   private final int luck;
/*     */   private final int lureSpeed;
/*  80 */   private final InterpolationHandler interpolationHandler = new InterpolationHandler(this);
/*     */   
/*     */   private FishingHook(EntityType<? extends FishingHook> type, Level level, int luck, int lureSpeed) {
/*  83 */     super(type, level);
/*  84 */     this.luck = Math.max(0, luck);
/*  85 */     this.lureSpeed = Math.max(0, lureSpeed);
/*     */   }
/*     */ 
/*     */   
/*  89 */   public FishingHook(EntityType<? extends FishingHook> type, Level level) { this(type, level, 0, 0); }
/*     */ 
/*     */   
/*     */   public FishingHook(Player player, Level level, int luck, int lureSpeed) {
/*  93 */     this(EntityType.FISHING_BOBBER, level, luck, lureSpeed);
/*  94 */     setOwner(player);
/*  95 */     float xRot1 = player.getXRot();
/*  96 */     float yRot1 = player.getYRot();
/*     */     
/*  98 */     float yCos = Mth.cos((-yRot1 * 0.017453292F - 3.1415927F));
/*  99 */     float ySin = Mth.sin((-yRot1 * 0.017453292F - 3.1415927F));
/* 100 */     float xCos = -Mth.cos((-xRot1 * 0.017453292F));
/* 101 */     float xSin = Mth.sin((-xRot1 * 0.017453292F));
/*     */     
/* 103 */     double x1 = player.getX() - ySin * 0.3D;
/* 104 */     double y1 = player.getEyeY();
/* 105 */     double z1 = player.getZ() - yCos * 0.3D;
/*     */     
/* 107 */     snapTo(x1, y1, z1, yRot1, xRot1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     Vec3 newMovement = new Vec3(-ySin, Mth.clamp(-(xSin / xCos), -5.0F, 5.0F), -yCos);
/*     */ 
/*     */ 
/*     */     
/* 116 */     double dist = newMovement.length();
/* 117 */     newMovement = newMovement.multiply(0.6D / dist + this.random
/* 118 */         .triangle(0.5D, 0.0103365D), 0.6D / dist + this.random
/* 119 */         .triangle(0.5D, 0.0103365D), 0.6D / dist + this.random
/* 120 */         .triangle(0.5D, 0.0103365D));
/*     */     
/* 122 */     setDeltaMovement(newMovement);
/*     */     
/* 124 */     setYRot((float)(Mth.atan2(newMovement.x, newMovement.z) * 57.2957763671875D));
/* 125 */     setXRot((float)(Mth.atan2(newMovement.y, newMovement.horizontalDistance()) * 57.2957763671875D));
/* 126 */     this.yRotO = getYRot();
/* 127 */     this.xRotO = getXRot();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public InterpolationHandler getInterpolation() { return this.interpolationHandler; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 137 */     entityData.define(DATA_HOOKED_ENTITY, Integer.valueOf(0));
/* 138 */     entityData.define(DATA_BITING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 143 */   protected boolean shouldBounceOnWorldBorder() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 148 */     if (DATA_HOOKED_ENTITY.equals(accessor)) {
/* 149 */       int id = ((Integer)getEntityData().get(DATA_HOOKED_ENTITY)).intValue();
/* 150 */       this.hookedIn = (id > 0) ? level().getEntity(id - 1) : null;
/*     */     } 
/*     */     
/* 153 */     if (DATA_BITING.equals(accessor)) {
/* 154 */       this.biting = ((Boolean)getEntityData().get(DATA_BITING)).booleanValue();
/* 155 */       if (this.biting) {
/* 156 */         setDeltaMovement((getDeltaMovement()).x, (-0.4F * Mth.nextFloat(this.syncronizedRandom, 0.6F, 1.0F)), (getDeltaMovement()).z);
/*     */       }
/*     */     } 
/* 159 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 164 */     double size = 64.0D;
/* 165 */     return (distance < 4096.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 171 */     this.syncronizedRandom.setSeed(getUUID().getLeastSignificantBits() ^ level().getGameTime());
/*     */     
/* 173 */     getInterpolation().interpolate();
/* 174 */     super.tick();
/*     */     
/* 176 */     Player owner = getPlayerOwner();
/* 177 */     if (owner == null) {
/* 178 */       discard();
/*     */       return;
/*     */     } 
/* 181 */     if (!level().isClientSide() && 
/* 182 */       shouldStopFishing(owner)) {
/*     */       return;
/*     */     }
/*     */     
/* 186 */     if (onGround()) {
/* 187 */       this.life++;
/* 188 */       if (this.life >= 1200) {
/* 189 */         discard();
/*     */         return;
/*     */       } 
/*     */     } else {
/* 193 */       this.life = 0;
/*     */     } 
/*     */     
/* 196 */     float liquidHeight = 0.0F;
/* 197 */     BlockPos blockPos = blockPosition();
/*     */     
/* 199 */     FluidState fluidState = level().getFluidState(blockPos);
/* 200 */     if (fluidState.is(FluidTags.WATER)) {
/* 201 */       liquidHeight = fluidState.getHeight(level(), blockPos);
/*     */     }
/*     */     
/* 204 */     boolean isInWater = (liquidHeight > 0.0F);
/* 205 */     if (this.currentState == FishHookState.FLYING)
/* 206 */     { if (this.hookedIn != null) {
/* 207 */         setDeltaMovement(Vec3.ZERO);
/*     */         
/* 209 */         this.currentState = FishHookState.HOOKED_IN_ENTITY;
/*     */         
/*     */         return;
/*     */       } 
/* 213 */       if (isInWater) {
/* 214 */         setDeltaMovement(getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
/*     */         
/* 216 */         this.currentState = FishHookState.BOBBING;
/*     */         
/*     */         return;
/*     */       } 
/* 220 */       checkCollision(); }
/* 221 */     else { if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
/* 222 */         if (this.hookedIn != null)
/* 223 */           if (this.hookedIn.isRemoved() || !this.hookedIn.canInteractWithLevel() || this.hookedIn.level().dimension() != level().dimension()) {
/* 224 */             setHookedEntity(null);
/* 225 */             this.currentState = FishHookState.FLYING;
/*     */           } else {
/* 227 */             setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
/*     */           }  
/*     */         return;
/*     */       } 
/* 231 */       if (this.currentState == FishHookState.BOBBING) {
/* 232 */         Vec3 movement = getDeltaMovement();
/* 233 */         double force = getY() + movement.y - blockPos.getY() - liquidHeight;
/* 234 */         if (Math.abs(force) < 0.01D) {
/* 235 */           force += Math.signum(force) * 0.1D;
/*     */         }
/*     */         
/* 238 */         setDeltaMovement(movement.x * 0.9D, movement.y - force * this.random
/*     */ 
/*     */             
/* 241 */             .nextFloat() * 0.2D, movement.z * 0.9D);
/*     */ 
/*     */         
/* 244 */         if (this.nibble > 0 || this.timeUntilHooked > 0) {
/* 245 */           this.openWater = (this.openWater && this.outOfWaterTime < 10 && calculateOpenWater(blockPos));
/*     */         } else {
/* 247 */           this.openWater = true;
/*     */         } 
/*     */         
/* 250 */         if (isInWater) {
/* 251 */           this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
/* 252 */           if (this.biting) {
/* 253 */             setDeltaMovement(getDeltaMovement().add(0.0D, -0.1D * this.syncronizedRandom.nextFloat() * this.syncronizedRandom.nextFloat(), 0.0D));
/*     */           }
/* 255 */           if (!level().isClientSide()) {
/* 256 */             catchingFish(blockPos);
/*     */           }
/*     */         } else {
/* 259 */           this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
/*     */         } 
/*     */       }  }
/*     */     
/* 263 */     if (!fluidState.is(FluidTags.WATER) && !onGround() && this.hookedIn == null) {
/* 264 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.03D, 0.0D));
/*     */     }
/*     */     
/* 267 */     move(MoverType.SELF, getDeltaMovement());
/* 268 */     applyEffectsFromBlocks();
/* 269 */     updateRotation();
/*     */     
/* 271 */     if (this.currentState == FishHookState.FLYING && (
/* 272 */       onGround() || this.horizontalCollision)) {
/* 273 */       setDeltaMovement(Vec3.ZERO);
/*     */     }
/*     */ 
/*     */     
/* 277 */     double inertia = 0.92D;
/* 278 */     setDeltaMovement(getDeltaMovement().scale(0.92D));
/*     */     
/* 280 */     reapplyPosition();
/*     */   }
/*     */   
/*     */   private boolean shouldStopFishing(Player owner) {
/* 284 */     if (owner.canInteractWithLevel()) {
/* 285 */       ItemStack selectedItem = owner.getMainHandItem();
/* 286 */       ItemStack selectedItemOffHand = owner.getOffhandItem();
/* 287 */       boolean mainHandIsFishing = selectedItem.is(Items.FISHING_ROD);
/* 288 */       boolean offHandIsFishing = selectedItemOffHand.is(Items.FISHING_ROD);
/*     */       
/* 290 */       if ((mainHandIsFishing || offHandIsFishing) && distanceToSqr(owner) <= 1024.0D) {
/* 291 */         return false;
/*     */       }
/*     */     } 
/* 294 */     discard();
/* 295 */     return true;
/*     */   }
/*     */   
/*     */   private void checkCollision() {
/* 299 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/* 300 */     hitTargetOrDeflectSelf(hitResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 305 */   protected boolean canHitEntity(Entity entity) { return (super.canHitEntity(entity) || (entity.isAlive() && entity instanceof ItemEntity)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 310 */     super.onHitEntity(hitResult);
/* 311 */     if (!level().isClientSide()) {
/* 312 */       setHookedEntity(hitResult.getEntity());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 318 */     super.onHitBlock(hitResult);
/* 319 */     setDeltaMovement(getDeltaMovement().normalize().scale(hitResult.distanceTo(this)));
/*     */   }
/*     */   
/*     */   private void setHookedEntity(Entity hookedIn) {
/* 323 */     this.hookedIn = hookedIn;
/* 324 */     getEntityData().set(DATA_HOOKED_ENTITY, Integer.valueOf((hookedIn == null) ? 0 : (hookedIn.getId() + 1)));
/*     */   }
/*     */   
/*     */   private void catchingFish(BlockPos blockPos) {
/* 328 */     ServerLevel serverLevel = (ServerLevel)level();
/*     */     
/* 330 */     int fishingSpeed = 1;
/* 331 */     BlockPos above = blockPos.above();
/* 332 */     if (this.random.nextFloat() < 0.25F && level().isRainingAt(above)) {
/* 333 */       fishingSpeed++;
/*     */     }
/* 335 */     if (this.random.nextFloat() < 0.5F && !level().canSeeSky(above)) {
/* 336 */       fishingSpeed--;
/*     */     }
/*     */     
/* 339 */     if (this.nibble > 0) {
/* 340 */       this.nibble--;
/*     */       
/* 342 */       if (this.nibble <= 0) {
/* 343 */         this.timeUntilLured = 0;
/* 344 */         this.timeUntilHooked = 0;
/* 345 */         getEntityData().set(DATA_BITING, Boolean.valueOf(false));
/*     */       } 
/* 347 */     } else if (this.timeUntilHooked > 0) {
/* 348 */       this.timeUntilHooked -= fishingSpeed;
/*     */       
/* 350 */       if (this.timeUntilHooked > 0) {
/* 351 */         this.fishAngle += (float)this.random.triangle(0.0D, 9.188D);
/*     */         
/* 353 */         float angle = this.fishAngle * 0.017453292F;
/* 354 */         float angleSin = Mth.sin(angle);
/* 355 */         float angleCos = Mth.cos(angle);
/* 356 */         double fishX = getX() + (angleSin * this.timeUntilHooked * 0.1F);
/* 357 */         double fishY = (Mth.floor(getY()) + 1.0F);
/* 358 */         double fishZ = getZ() + (angleCos * this.timeUntilHooked * 0.1F);
/*     */         
/* 360 */         BlockState splashBlockState = serverLevel.getBlockState(BlockPos.containing(fishX, fishY - 1.0D, fishZ));
/* 361 */         if (splashBlockState.is(Blocks.WATER)) {
/* 362 */           if (this.random.nextFloat() < 0.15F) {
/* 363 */             serverLevel.sendParticles(ParticleTypes.BUBBLE, fishX, fishY - 0.10000000149011612D, fishZ, 1, angleSin, 0.1D, angleCos, 0.0D);
/*     */           }
/*     */           
/* 366 */           float particleXMovement = angleSin * 0.04F;
/* 367 */           float particleZMovement = angleCos * 0.04F;
/*     */           
/* 369 */           serverLevel.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, particleZMovement, 0.01D, -particleXMovement, 1.0D);
/* 370 */           serverLevel.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, -particleZMovement, 0.01D, particleXMovement, 1.0D);
/*     */         } 
/*     */       } else {
/* 373 */         playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/* 374 */         double y = getY() + 0.5D;
/* 375 */         serverLevel.sendParticles(ParticleTypes.BUBBLE, getX(), y, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/* 376 */         serverLevel.sendParticles(ParticleTypes.FISHING, getX(), y, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/*     */         
/* 378 */         this.nibble = Mth.nextInt(this.random, 20, 40);
/* 379 */         getEntityData().set(DATA_BITING, Boolean.valueOf(true));
/*     */       } 
/* 381 */     } else if (this.timeUntilLured > 0) {
/* 382 */       this.timeUntilLured -= fishingSpeed;
/*     */       
/* 384 */       float teaseChance = 0.15F;
/* 385 */       if (this.timeUntilLured < 20) {
/* 386 */         teaseChance += (20 - this.timeUntilLured) * 0.05F;
/* 387 */       } else if (this.timeUntilLured < 40) {
/* 388 */         teaseChance += (40 - this.timeUntilLured) * 0.02F;
/* 389 */       } else if (this.timeUntilLured < 60) {
/* 390 */         teaseChance += (60 - this.timeUntilLured) * 0.01F;
/*     */       } 
/*     */       
/* 393 */       if (this.random.nextFloat() < teaseChance) {
/* 394 */         float angle = Mth.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
/* 395 */         float dist = Mth.nextFloat(this.random, 25.0F, 60.0F);
/* 396 */         double fishX = getX() + (Mth.sin(angle) * dist) * 0.1D;
/* 397 */         double fishY = (Mth.floor(getY()) + 1.0F);
/* 398 */         double fishZ = getZ() + (Mth.cos(angle) * dist) * 0.1D;
/* 399 */         BlockState splashBlockState = serverLevel.getBlockState(BlockPos.containing(fishX, fishY - 1.0D, fishZ));
/* 400 */         if (splashBlockState.is(Blocks.WATER)) {
/* 401 */           serverLevel.sendParticles(ParticleTypes.SPLASH, fishX, fishY, fishZ, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
/*     */         }
/*     */       } 
/*     */       
/* 405 */       if (this.timeUntilLured <= 0) {
/* 406 */         this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
/* 407 */         this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
/*     */       } 
/*     */     } else {
/* 410 */       this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
/* 411 */       this.timeUntilLured -= this.lureSpeed;
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum OpenWaterType {
/* 416 */     ABOVE_WATER, INSIDE_WATER, INVALID;
/*     */   }
/*     */   
/*     */   private boolean calculateOpenWater(BlockPos blockPos) {
/* 420 */     OpenWaterType previousLayer = OpenWaterType.INVALID;
/* 421 */     for (int y = -1; y <= 2; y++) {
/* 422 */       OpenWaterType layer = getOpenWaterTypeForArea(blockPos.offset(-2, y, -2), blockPos.offset(2, y, 2));
/* 423 */       switch (layer.ordinal()) {
/*     */         case 2:
/* 425 */           return false;
/*     */         case 0:
/* 427 */           if (previousLayer == OpenWaterType.INVALID) {
/* 428 */             return false;
/*     */           }
/*     */           break;
/*     */         case 1:
/* 432 */           if (previousLayer == OpenWaterType.ABOVE_WATER)
/* 433 */             return false; 
/*     */           break;
/*     */       } 
/* 436 */       previousLayer = layer;
/*     */     } 
/* 438 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 442 */   private OpenWaterType getOpenWaterTypeForArea(BlockPos from, BlockPos to) { return (OpenWaterType)BlockPos.betweenClosedStream(from, to).map(this::getOpenWaterTypeForBlock).reduce((a, b) -> (a == b) ? a : OpenWaterType.INVALID).orElse(OpenWaterType.INVALID); }
/*     */ 
/*     */   
/*     */   private OpenWaterType getOpenWaterTypeForBlock(BlockPos pos) {
/* 446 */     BlockState state = level().getBlockState(pos);
/* 447 */     if (state.isAir() || state.is(Blocks.LILY_PAD)) {
/* 448 */       return OpenWaterType.ABOVE_WATER;
/*     */     }
/* 450 */     FluidState fluidState = state.getFluidState();
/* 451 */     if (fluidState.is(FluidTags.WATER) && fluidState.isSource() && state.getCollisionShape(level(), pos).isEmpty()) {
/* 452 */       return OpenWaterType.INSIDE_WATER;
/*     */     }
/* 454 */     return OpenWaterType.INVALID;
/*     */   }
/*     */ 
/*     */   
/* 458 */   public boolean isOpenWaterFishing() { return this.openWater; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */ 
/*     */   
/*     */   public int retrieve(ItemStack rod) {
/* 470 */     Player owner = getPlayerOwner();
/* 471 */     if (level().isClientSide() || owner == null || shouldStopFishing(owner)) {
/* 472 */       return 0;
/*     */     }
/*     */     
/* 475 */     int dmg = 0;
/* 476 */     if (this.hookedIn != null) {
/* 477 */       pullEntity(this.hookedIn);
/* 478 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)owner, rod, this, Collections.emptyList());
/* 479 */       level().broadcastEntityEvent(this, (byte)31);
/* 480 */       dmg = (this.hookedIn instanceof ItemEntity) ? 3 : 5;
/* 481 */     } else if (this.nibble > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 487 */       LootParams params = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, rod).withParameter(LootContextParams.THIS_ENTITY, this).withLuck(this.luck + owner.getLuck()).create(LootContextParamSets.FISHING);
/* 488 */       LootTable lootTable = level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
/* 489 */       ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 490 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)owner, rod, this, objectArrayList);
/* 491 */       for (ItemStack itemStack : objectArrayList) {
/* 492 */         ItemEntity entity = new ItemEntity(level(), getX(), getY(), getZ(), itemStack);
/* 493 */         double xa = owner.getX() - getX();
/* 494 */         double ya = owner.getY() - getY();
/* 495 */         double za = owner.getZ() - getZ();
/*     */         
/* 497 */         double speed = 0.1D;
/* 498 */         entity.setDeltaMovement(xa * 0.1D, ya * 0.1D + 
/*     */             
/* 500 */             Math.sqrt(Math.sqrt(xa * xa + ya * ya + za * za)) * 0.08D, za * 0.1D);
/*     */ 
/*     */         
/* 503 */         level().addFreshEntity(entity);
/* 504 */         owner.level().addFreshEntity(new ExperienceOrb(owner.level(), owner.getX(), owner.getY() + 0.5D, owner.getZ() + 0.5D, this.random.nextInt(6) + 1));
/*     */         
/* 506 */         if (itemStack.is(ItemTags.FISHES)) {
/* 507 */           owner.awardStat(Stats.FISH_CAUGHT, 1);
/*     */         }
/*     */       } 
/* 510 */       dmg = 1;
/*     */     } 
/* 512 */     if (onGround()) {
/* 513 */       dmg = 2;
/*     */     }
/*     */     
/* 516 */     discard();
/* 517 */     return dmg;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 522 */     if (id == 31 && 
/* 523 */       level().isClientSide()) { Entity entity = this.hookedIn; if (entity instanceof Player) { Player player = (Player)entity; if (player.isLocalPlayer()) {
/* 524 */           pullEntity(this.hookedIn);
/*     */         } }
/*     */        }
/*     */     
/* 528 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   protected void pullEntity(Entity entity) {
/* 532 */     Entity owner = getOwner();
/* 533 */     if (owner == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     Vec3 delta = (new Vec3(owner.getX() - getX(), owner.getY() - getY(), owner.getZ() - getZ())).scale(0.1D);
/*     */     
/* 543 */     entity.setDeltaMovement(entity.getDeltaMovement().add(delta));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 548 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/* 553 */     updateOwnerInfo(null);
/* 554 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 559 */   public void onClientRemoval() { updateOwnerInfo(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(Entity owner) {
/* 564 */     super.setOwner(owner);
/* 565 */     updateOwnerInfo(this);
/*     */   }
/*     */   
/*     */   private void updateOwnerInfo(FishingHook hook) {
/* 569 */     Player owner = getPlayerOwner();
/* 570 */     if (owner != null) {
/* 571 */       owner.fishing = hook;
/*     */     }
/*     */   }
/*     */   
/*     */   public Player getPlayerOwner() {
/* 576 */     Entity owner = getOwner();
/* 577 */     Player player = (Player)owner; return (owner instanceof Player) ? player : null;
/*     */   }
/*     */ 
/*     */   
/* 581 */   public Entity getHookedIn() { return this.hookedIn; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 586 */   public boolean canUsePortal(boolean ignorePassenger) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 591 */     Entity owner = getOwner();
/* 592 */     return new ClientboundAddEntityPacket(this, serverEntity, (owner == null) ? getId() : owner.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 597 */     super.recreateFromPacket(packet);
/* 598 */     if (getPlayerOwner() == null) {
/* 599 */       int ownerId = packet.getData();
/* 600 */       LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", level().getEntity(ownerId), Integer.valueOf(ownerId));
/* 601 */       discard();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\FishingHook.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */