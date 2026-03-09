/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractWindCharge
/*     */   extends AbstractHurtingProjectile
/*     */   implements ItemSupplier {
/*  30 */   public static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, 
/*     */ 
/*     */       
/*  33 */       Optional.empty(), BuiltInRegistries.BLOCK
/*  34 */       .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
/*     */   
/*     */   public static final double JUMP_SCALE = 0.25D;
/*     */ 
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
/*  40 */     super(type, level);
/*  41 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, Level level, Entity owner, double x, double y, double z) {
/*  45 */     super(type, x, y, z, level);
/*  46 */     setOwner(owner);
/*  47 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, double x, double y, double z, Vec3 direction, Level level) {
/*  51 */     super(type, x, y, z, direction, level);
/*  52 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 position) {
/*  57 */     float width = getType().getDimensions().width() / 2.0F;
/*  58 */     float height = getType().getDimensions().height();
/*  59 */     float offset = 0.15F;
/*     */     
/*  61 */     return new AABB(position.x - width, position.y - 0.15000000596046448D, position.z - width, position.x + width, position.y - 0.15000000596046448D + height, position.z + width);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideWith(Entity entity) {
/*  66 */     if (entity instanceof AbstractWindCharge) {
/*  67 */       return false;
/*     */     }
/*  69 */     return super.canCollideWith(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/*  74 */     if (entity instanceof AbstractWindCharge) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (entity.getType() == EntityType.END_CRYSTAL) {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     return super.canHitEntity(entity);
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*     */     ServerLevel serverLevel;
/*  87 */     super.onHitEntity(hitResult);
/*  88 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/*  92 */     Entity entity1 = getOwner(); LivingEntity entity = (LivingEntity)entity1, owner = (entity1 instanceof LivingEntity) ? entity : null;
/*  93 */     Entity entity = hitResult.getEntity();
/*     */     
/*  95 */     if (owner != null) {
/*  96 */       owner.setLastHurtMob(entity);
/*     */     }
/*     */     
/*  99 */     DamageSource source = damageSources().windCharge(this, owner);
/* 100 */     if (entity.hurtServer(serverLevel, source, 1.0F) && 
/* 101 */       entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/* 102 */       EnchantmentHelper.doPostAttackEffects(serverLevel, mob, source); }
/*     */ 
/*     */     
/* 105 */     explode(position());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(double xa, double ya, double za) {}
/*     */ 
/*     */   
/*     */   protected abstract void explode(Vec3 paramVec3);
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 117 */     super.onHitBlock(hitResult);
/*     */     
/* 119 */     if (!level().isClientSide()) {
/* 120 */       Vec3i collisionNormal = hitResult.getDirection().getUnitVec3i();
/* 121 */       Vec3 scaledNormal = Vec3.atLowerCornerOf(collisionNormal).multiply(0.25D, 0.25D, 0.25D);
/* 122 */       Vec3 explosionPos = hitResult.getLocation().add(scaledNormal);
/*     */       
/* 124 */       explode(explosionPos);
/* 125 */       discard();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/* 131 */     super.onHit(hitResult);
/* 132 */     if (!level().isClientSide()) {
/* 133 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 139 */   protected boolean shouldBurn() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public ItemStack getItem() { return ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   protected float getInertia() { return 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   protected float getLiquidInertia() { return getInertia(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   protected ParticleOptions getTrailParticle() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 166 */     if (!level().isClientSide() && getBlockY() > level().getMaxY() + 30) {
/* 167 */       explode(position());
/* 168 */       discard();
/*     */     } else {
/* 170 */       super.tick();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\AbstractWindCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */