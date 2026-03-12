/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EyeOfEnder
/*     */   extends Entity
/*     */   implements ItemSupplier
/*     */ {
/*     */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/*     */   private static final float TOO_FAR_SIGNAL_HEIGHT = 8.0F;
/*     */   private static final float TOO_FAR_DISTANCE = 12.0F;
/*  28 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private Vec3 target;
/*     */   
/*     */   private int life;
/*     */   private boolean surviveAfterDeath;
/*     */   
/*  35 */   public EyeOfEnder(EntityType<? extends EyeOfEnder> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public EyeOfEnder(Level level, double x, double y, double z) {
/*  39 */     this(EntityType.EYE_OF_ENDER, level);
/*     */     
/*  41 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack source) {
/*  45 */     if (source.isEmpty()) {
/*  46 */       getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
/*     */     } else {
/*  48 */       getEntityData().set(DATA_ITEM_STACK, source.copyWithCount(1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public ItemStack getItem() { return (ItemStack)getEntityData().get(DATA_ITEM_STACK); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_ITEM_STACK, getDefaultItem()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/*  64 */     if (this.tickCount < 2 && distance < 12.25D) {
/*  65 */       return false;
/*     */     }
/*  67 */     double size = getBoundingBox().getSize() * 4.0D;
/*  68 */     if (Double.isNaN(size)) {
/*  69 */       size = 4.0D;
/*     */     }
/*  71 */     size *= 64.0D;
/*  72 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   public void signalTo(Vec3 target) {
/*  76 */     Vec3 delta = target.subtract(position());
/*     */     
/*  78 */     double horizontalDistance = delta.horizontalDistance();
/*  79 */     if (horizontalDistance > 12.0D) {
/*  80 */       this.target = position().add(delta.x / horizontalDistance * 12.0D, 8.0D, delta.z / horizontalDistance * 12.0D);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  86 */       this.target = target;
/*     */     } 
/*     */     
/*  89 */     this.life = 0;
/*  90 */     this.surviveAfterDeath = (this.random.nextInt(5) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  95 */     super.tick();
/*     */     
/*  97 */     Vec3 newPosition = position().add(getDeltaMovement());
/*  98 */     if (!level().isClientSide() && this.target != null) {
/*  99 */       setDeltaMovement(updateDeltaMovement(getDeltaMovement(), newPosition, this.target));
/*     */     }
/*     */     
/* 102 */     if (level().isClientSide()) {
/* 103 */       Vec3 particleOrigin = newPosition.subtract(getDeltaMovement().scale(0.25D));
/* 104 */       spawnParticles(particleOrigin, getDeltaMovement());
/*     */     } 
/*     */     
/* 107 */     setPos(newPosition);
/*     */     
/* 109 */     if (!level().isClientSide()) {
/* 110 */       this.life++;
/* 111 */       if (this.life > 80 && !level().isClientSide()) {
/* 112 */         playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
/* 113 */         discard();
/* 114 */         if (this.surviveAfterDeath) {
/* 115 */           level().addFreshEntity(new ItemEntity(level(), getX(), getY(), getZ(), getItem()));
/*     */         } else {
/* 117 */           level().levelEvent(2003, blockPosition(), 0);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnParticles(Vec3 origin, Vec3 movement) {
/* 124 */     if (isInWater()) {
/* 125 */       for (int i = 0; i < 4; i++) {
/* 126 */         level().addParticle(ParticleTypes.BUBBLE, origin.x, origin.y, origin.z, movement.x, movement.y, movement.z);
/*     */       }
/*     */     } else {
/* 129 */       level().addParticle(ParticleTypes.PORTAL, origin.x + this.random
/* 130 */           .nextDouble() * 0.6D - 0.3D, origin.y - 0.5D, origin.z + this.random
/*     */           
/* 132 */           .nextDouble() * 0.6D - 0.3D, movement.x, movement.y, movement.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 updateDeltaMovement(Vec3 oldMovement, Vec3 position, Vec3 target) {
/* 139 */     Vec3 horizontalDelta = new Vec3(target.x - position.x, 0.0D, target.z - position.z);
/* 140 */     double horizontalLength = horizontalDelta.length();
/* 141 */     double wantedSpeed = Mth.lerp(0.0025D, oldMovement.horizontalDistance(), horizontalLength);
/* 142 */     double movementY = oldMovement.y;
/* 143 */     if (horizontalLength < 1.0D) {
/* 144 */       wantedSpeed *= 0.8D;
/* 145 */       movementY *= 0.8D;
/*     */     } 
/* 147 */     double wantedMovementY = (position.y - oldMovement.y < target.y) ? 1.0D : -1.0D;
/* 148 */     return horizontalDelta.scale(wantedSpeed / horizontalLength)
/* 149 */       .add(0.0D, movementY + (wantedMovementY - movementY) * 0.015D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected void addAdditionalSaveData(ValueOutput output) { output.store("Item", ItemStack.CODEC, getItem()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   protected void readAdditionalSaveData(ValueInput input) { setItem((ItemStack)input.read("Item", ItemStack.CODEC).orElse(getDefaultItem())); }
/*     */ 
/*     */ 
/*     */   
/* 163 */   private ItemStack getDefaultItem() { return new ItemStack(Items.ENDER_EYE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public float getLightLevelDependentMagicValue() { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public boolean isAttackable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\EyeOfEnder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */