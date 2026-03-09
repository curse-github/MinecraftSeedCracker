/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LlamaSpit
/*     */   extends Projectile {
/*  23 */   public LlamaSpit(EntityType<? extends LlamaSpit> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public LlamaSpit(Level level, Llama owner) {
/*  27 */     this(EntityType.LLAMA_SPIT, level);
/*  28 */     setOwner(owner);
/*  29 */     setPos(owner.getX() - (owner.getBbWidth() + 1.0F) * 0.5D * Mth.sin((owner.yBodyRot * 0.017453292F)), owner.getEyeY() - 0.10000000149011612D, owner.getZ() + (owner.getBbWidth() + 1.0F) * 0.5D * Mth.cos((owner.yBodyRot * 0.017453292F)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  34 */   protected double getDefaultGravity() { return 0.06D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  39 */     super.tick();
/*     */     
/*  41 */     Vec3 movement = getDeltaMovement();
/*  42 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*  43 */     hitTargetOrDeflectSelf(hitResult);
/*     */     
/*  45 */     double x = getX() + movement.x;
/*  46 */     double y = getY() + movement.y;
/*  47 */     double z = getZ() + movement.z;
/*     */     
/*  49 */     updateRotation();
/*     */     
/*  51 */     float inertia = 0.99F;
/*     */     
/*  53 */     if (level().getBlockStates(getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
/*  54 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  58 */     if (isInWater()) {
/*  59 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     setDeltaMovement(movement.scale(0.9900000095367432D));
/*  64 */     applyGravity();
/*     */     
/*  66 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*  71 */     super.onHitEntity(hitResult);
/*  72 */     Entity entity = getOwner(); if (entity instanceof LivingEntity) { LivingEntity livingOwner = (LivingEntity)entity;
/*  73 */       Entity target = hitResult.getEntity();
/*  74 */       DamageSource damageSource = damageSources().spit(this, livingOwner);
/*  75 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  76 */         if (target.hurtServer(serverLevel, damageSource, 1.0F)) {
/*  77 */           EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
/*     */         } }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/*  85 */     super.onHitBlock(hitResult);
/*     */     
/*  87 */     if (!level().isClientSide()) {
/*  88 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/*  98 */     super.recreateFromPacket(packet);
/*  99 */     Vec3 movement = packet.getMovement();
/*     */     
/* 101 */     for (int i = 0; i < 7; i++) {
/* 102 */       double k = 0.4D + 0.1D * i;
/* 103 */       level().addParticle(ParticleTypes.SPIT, getX(), getY(), getZ(), movement.x * k, movement.y, movement.z * k);
/*     */     } 
/*     */     
/* 106 */     setDeltaMovement(movement);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\LlamaSpit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */