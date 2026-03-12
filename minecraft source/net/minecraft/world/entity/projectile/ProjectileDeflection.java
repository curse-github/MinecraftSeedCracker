/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ProjectileDeflection
/*    */ {
/*    */   public static final ProjectileDeflection NONE = (projectile, entity, random) -> {
/*    */     
/*    */     };
/*    */   public static final ProjectileDeflection REVERSE = (projectile, entity, random) -> {
/* 16 */       float rotation = 170.0F + random.nextFloat() * 20.0F;
/* 17 */       projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.5D));
/* 18 */       projectile.setYRot(projectile.getYRot() + rotation);
/* 19 */       projectile.yRotO += rotation;
/* 20 */       projectile.needsSync = true;
/*    */     };
/*    */   
/*    */   public static final ProjectileDeflection AIM_DEFLECT = (projectile, entity, random) -> {
/* 24 */       if (entity != null) {
/* 25 */         Vec3 lookAngle = entity.getLookAngle();
/* 26 */         projectile.setDeltaMovement(lookAngle);
/* 27 */         projectile.needsSync = true;
/*    */       } 
/*    */     };
/*    */   
/*    */   public static final ProjectileDeflection MOMENTUM_DEFLECT = (projectile, entity, random) -> {
/* 32 */       if (entity != null) {
/* 33 */         Vec3 movement = entity.getDeltaMovement().normalize();
/* 34 */         projectile.setDeltaMovement(movement);
/* 35 */         projectile.needsSync = true;
/*    */       } 
/*    */     };
/*    */   
/*    */   void deflect(Projectile paramProjectile, Entity paramEntity, RandomSource paramRandomSource);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\ProjectileDeflection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */