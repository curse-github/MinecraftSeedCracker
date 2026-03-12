/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.PowerParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class DragonFireball
/*    */   extends AbstractHurtingProjectile
/*    */ {
/*    */   public static final float SPLASH_RANGE = 4.0F;
/*    */   
/* 24 */   public DragonFireball(EntityType<? extends DragonFireball> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public DragonFireball(Level level, LivingEntity mob, Vec3 direction) { super(EntityType.DRAGON_FIREBALL, mob, direction, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 33 */     super.onHit(hitResult);
/* 34 */     if (hitResult.getType() == HitResult.Type.ENTITY && ownedBy(((EntityHitResult)hitResult).getEntity())) {
/*    */       return;
/*    */     }
/* 37 */     if (!level().isClientSide()) {
/* 38 */       List<LivingEntity> entitiesOfClass = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
/*    */       
/* 40 */       AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 41 */       Entity owner = getOwner();
/* 42 */       if (owner instanceof LivingEntity) {
/* 43 */         cloud.setOwner((LivingEntity)owner);
/*    */       }
/* 45 */       cloud.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
/* 46 */       cloud.setRadius(3.0F);
/* 47 */       cloud.setDuration(600);
/* 48 */       cloud.setRadiusPerTick((7.0F - cloud.getRadius()) / cloud.getDuration());
/* 49 */       cloud.setPotionDurationScale(0.25F);
/* 50 */       cloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
/*    */       
/* 52 */       if (!entitiesOfClass.isEmpty()) {
/* 53 */         for (LivingEntity entity : entitiesOfClass) {
/* 54 */           double dist = distanceToSqr(entity);
/* 55 */           if (dist < 16.0D) {
/* 56 */             cloud.setPos(entity.getX(), entity.getY(), entity.getZ());
/*    */             
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       }
/* 62 */       level().levelEvent(2006, blockPosition(), isSilent() ? -1 : 1);
/* 63 */       level().addFreshEntity(cloud);
/*    */       
/* 65 */       discard();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   protected ParticleOptions getTrailParticle() { return PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected boolean shouldBurn() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\DragonFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */