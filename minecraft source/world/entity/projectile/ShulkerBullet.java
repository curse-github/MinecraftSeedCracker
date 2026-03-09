/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShulkerBullet
/*     */   extends Projectile
/*     */ {
/*     */   private static final double SPEED = 0.15D;
/*     */   private EntityReference<Entity> finalTarget;
/*     */   private Direction currentMoveDirection;
/*     */   private int flightSteps;
/*     */   private double targetDeltaX;
/*     */   private double targetDeltaY;
/*     */   private double targetDeltaZ;
/*     */   
/*     */   public ShulkerBullet(EntityType<? extends ShulkerBullet> type, Level level) {
/*  50 */     super(type, level);
/*     */     
/*  52 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public ShulkerBullet(Level level, LivingEntity owner, Entity target, Direction.Axis invalidStartAxis) {
/*  56 */     this(EntityType.SHULKER_BULLET, level);
/*  57 */     setOwner(owner);
/*     */     
/*  59 */     Vec3 position = owner.getBoundingBox().getCenter();
/*  60 */     snapTo(position.x, position.y, position.z, getYRot(), getXRot());
/*     */     
/*  62 */     this.finalTarget = EntityReference.of(target);
/*     */     
/*  64 */     this.currentMoveDirection = Direction.UP;
/*  65 */     selectNextMoveDirection(invalidStartAxis, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public SoundSource getSoundSource() { return SoundSource.HOSTILE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  75 */     super.addAdditionalSaveData(output);
/*  76 */     if (this.finalTarget != null) {
/*  77 */       output.store("Target", UUIDUtil.CODEC, this.finalTarget.getUUID());
/*     */     }
/*  79 */     output.storeNullable("Dir", Direction.LEGACY_ID_CODEC, this.currentMoveDirection);
/*  80 */     output.putInt("Steps", this.flightSteps);
/*  81 */     output.putDouble("TXD", this.targetDeltaX);
/*  82 */     output.putDouble("TYD", this.targetDeltaY);
/*  83 */     output.putDouble("TZD", this.targetDeltaZ);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  88 */     super.readAdditionalSaveData(input);
/*  89 */     this.flightSteps = input.getIntOr("Steps", 0);
/*  90 */     this.targetDeltaX = input.getDoubleOr("TXD", 0.0D);
/*  91 */     this.targetDeltaY = input.getDoubleOr("TYD", 0.0D);
/*  92 */     this.targetDeltaZ = input.getDoubleOr("TZD", 0.0D);
/*  93 */     this.currentMoveDirection = (Direction)input.read("Dir", Direction.LEGACY_ID_CODEC).orElse(null);
/*  94 */     this.finalTarget = EntityReference.read(input, "Target");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */   
/* 102 */   private Direction getMoveDirection() { return this.currentMoveDirection; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   private void setMoveDirection(Direction direction) { this.currentMoveDirection = direction; }
/*     */ 
/*     */   
/*     */   private void selectNextMoveDirection(Direction.Axis avoidAxis, Entity target) {
/*     */     BlockPos targetPos;
/* 111 */     double yOffset = 0.5D;
/* 112 */     if (target == null) {
/* 113 */       targetPos = blockPosition().below();
/*     */     } else {
/* 115 */       yOffset = target.getBbHeight() * 0.5D;
/* 116 */       targetPos = BlockPos.containing(target.getX(), target.getY() + yOffset, target.getZ());
/*     */     } 
/*     */     
/* 119 */     double targetX = targetPos.getX() + 0.5D;
/* 120 */     double targetY = targetPos.getY() + yOffset;
/* 121 */     double targetZ = targetPos.getZ() + 0.5D;
/*     */     
/* 123 */     Direction selection = null;
/* 124 */     if (!targetPos.closerToCenterThan(position(), 2.0D)) {
/* 125 */       BlockPos current = blockPosition();
/* 126 */       List<Direction> options = Lists.newArrayList();
/*     */       
/* 128 */       if (avoidAxis != Direction.Axis.X) {
/* 129 */         if (current.getX() < targetPos.getX() && level().isEmptyBlock(current.east())) {
/* 130 */           options.add(Direction.EAST);
/* 131 */         } else if (current.getX() > targetPos.getX() && level().isEmptyBlock(current.west())) {
/* 132 */           options.add(Direction.WEST);
/*     */         } 
/*     */       }
/* 135 */       if (avoidAxis != Direction.Axis.Y) {
/* 136 */         if (current.getY() < targetPos.getY() && level().isEmptyBlock(current.above())) {
/* 137 */           options.add(Direction.UP);
/* 138 */         } else if (current.getY() > targetPos.getY() && level().isEmptyBlock(current.below())) {
/* 139 */           options.add(Direction.DOWN);
/*     */         } 
/*     */       }
/* 142 */       if (avoidAxis != Direction.Axis.Z) {
/* 143 */         if (current.getZ() < targetPos.getZ() && level().isEmptyBlock(current.south())) {
/* 144 */           options.add(Direction.SOUTH);
/* 145 */         } else if (current.getZ() > targetPos.getZ() && level().isEmptyBlock(current.north())) {
/* 146 */           options.add(Direction.NORTH);
/*     */         } 
/*     */       }
/*     */       
/* 150 */       selection = Direction.getRandom(this.random);
/* 151 */       if (options.isEmpty()) {
/* 152 */         int attempts = 5;
/* 153 */         while (!level().isEmptyBlock(current.relative(selection)) && attempts > 0) {
/* 154 */           selection = Direction.getRandom(this.random);
/* 155 */           attempts--;
/*     */         } 
/*     */       } else {
/* 158 */         selection = (Direction)options.get(this.random.nextInt(options.size()));
/*     */       } 
/*     */       
/* 161 */       targetX = getX() + selection.getStepX();
/* 162 */       targetY = getY() + selection.getStepY();
/* 163 */       targetZ = getZ() + selection.getStepZ();
/*     */     } 
/*     */     
/* 166 */     setMoveDirection(selection);
/*     */     
/* 168 */     double xa = targetX - getX();
/* 169 */     double ya = targetY - getY();
/* 170 */     double za = targetZ - getZ();
/*     */     
/* 172 */     double distance = Math.sqrt(xa * xa + ya * ya + za * za);
/* 173 */     if (distance == 0.0D) {
/* 174 */       this.targetDeltaX = 0.0D;
/* 175 */       this.targetDeltaY = 0.0D;
/* 176 */       this.targetDeltaZ = 0.0D;
/*     */     } else {
/* 178 */       this.targetDeltaX = xa / distance * 0.15D;
/* 179 */       this.targetDeltaY = ya / distance * 0.15D;
/* 180 */       this.targetDeltaZ = za / distance * 0.15D;
/*     */     } 
/*     */     
/* 183 */     this.needsSync = true;
/* 184 */     this.flightSteps = 10 + this.random.nextInt(5) * 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {
/* 189 */     if (level().getDifficulty() == Difficulty.PEACEFUL) {
/* 190 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 196 */   protected double getDefaultGravity() { return 0.04D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 201 */     super.tick();
/*     */     
/* 203 */     Entity finalTarget = !level().isClientSide() ? EntityReference.getEntity(this.finalTarget, level()) : null;
/* 204 */     HitResult hitResult = null;
/* 205 */     if (!level().isClientSide()) {
/* 206 */       if (finalTarget == null) {
/* 207 */         this.finalTarget = null;
/*     */       }
/*     */       
/* 210 */       if (finalTarget != null && finalTarget.isAlive() && (!(finalTarget instanceof net.minecraft.world.entity.player.Player) || !finalTarget.isSpectator())) {
/* 211 */         this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
/* 212 */         this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
/* 213 */         this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
/*     */         
/* 215 */         Vec3 movement = getDeltaMovement();
/* 216 */         setDeltaMovement(movement.add((this.targetDeltaX - movement.x) * 0.2D, (this.targetDeltaY - movement.y) * 0.2D, (this.targetDeltaZ - movement.z) * 0.2D));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 222 */         applyGravity();
/*     */       } 
/*     */       
/* 225 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } 
/*     */     
/* 228 */     Vec3 movement = getDeltaMovement();
/* 229 */     setPos(position().add(movement));
/* 230 */     applyEffectsFromBlocks();
/* 231 */     if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
/* 232 */       handlePortal();
/*     */     }
/*     */     
/* 235 */     if (hitResult != null && isAlive() && hitResult.getType() != HitResult.Type.MISS) {
/* 236 */       hitTargetOrDeflectSelf(hitResult);
/*     */     }
/*     */     
/* 239 */     ProjectileUtil.rotateTowardsMovement(this, 0.5F);
/*     */     
/* 241 */     if (level().isClientSide()) {
/* 242 */       level().addParticle(ParticleTypes.END_ROD, getX() - movement.x, getY() - movement.y + 0.15D, getZ() - movement.z, 0.0D, 0.0D, 0.0D);
/* 243 */     } else if (finalTarget != null) {
/* 244 */       if (this.flightSteps > 0) {
/* 245 */         this.flightSteps--;
/* 246 */         if (this.flightSteps == 0) {
/* 247 */           selectNextMoveDirection((this.currentMoveDirection == null) ? null : this.currentMoveDirection.getAxis(), finalTarget);
/*     */         }
/*     */       } 
/*     */       
/* 251 */       if (this.currentMoveDirection != null) {
/*     */         
/* 253 */         BlockPos current = blockPosition();
/* 254 */         Direction.Axis axis = this.currentMoveDirection.getAxis();
/* 255 */         if (level().loadedAndEntityCanStandOn(current.relative(this.currentMoveDirection), this)) {
/* 256 */           selectNextMoveDirection(axis, finalTarget);
/*     */         } else {
/* 258 */           BlockPos targetPos = finalTarget.blockPosition();
/* 259 */           if ((axis == Direction.Axis.X && current.getX() == targetPos.getX()) || (axis == Direction.Axis.Z && current
/* 260 */             .getZ() == targetPos.getZ()) || (axis == Direction.Axis.Y && current
/* 261 */             .getY() == targetPos.getY()))
/*     */           {
/* 263 */             selectNextMoveDirection(axis, finalTarget);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   protected boolean isAffectedByBlocks() { return !isRemoved(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 278 */   protected boolean canHitEntity(Entity entity) { return (super.canHitEntity(entity) && !entity.noPhysics); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public boolean isOnFire() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 288 */   public boolean shouldRenderAtSqrDistance(double distance) { return (distance < 16384.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 293 */   public float getLightLevelDependentMagicValue() { return 1.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 298 */     super.onHitEntity(hitResult);
/* 299 */     Entity target = hitResult.getEntity();
/* 300 */     Entity owner = getOwner();
/* 301 */     LivingEntity livingOwner = (owner instanceof LivingEntity) ? (LivingEntity)owner : null;
/* 302 */     DamageSource damageSource = damageSources().mobProjectile(this, livingOwner);
/* 303 */     boolean wasHurt = target.hurtOrSimulate(damageSource, 4.0F);
/* 304 */     if (wasHurt) {
/* 305 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 306 */         EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource); }
/*     */       
/* 308 */       if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/* 309 */         livingTarget.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200), (Entity)MoreObjects.firstNonNull(owner, this)); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 316 */     super.onHitBlock(hitResult);
/* 317 */     ((ServerLevel)level()).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
/* 318 */     playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void destroy() {
/* 322 */     discard();
/* 323 */     level().gameEvent(GameEvent.ENTITY_DAMAGE, position(), GameEvent.Context.of(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/* 328 */     super.onHit(hitResult);
/* 329 */     destroy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 334 */   public boolean isPickable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 339 */   public boolean hurtClient(DamageSource source) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 344 */     playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
/* 345 */     level.sendParticles(ParticleTypes.CRIT, getX(), getY(), getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
/* 346 */     destroy();
/* 347 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 352 */     super.recreateFromPacket(packet);
/* 353 */     setDeltaMovement(packet.getMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\ShulkerBullet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */