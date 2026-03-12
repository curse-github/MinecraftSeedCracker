/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LongJumpUtil
/*    */ {
/*    */   public static Optional<Vec3> calculateJumpVectorForAngle(Mob body, Vec3 targetPos, float maxJumpVelocity, int angle, boolean checkCollision) {
/* 20 */     Vec3 mobPos = body.position();
/*    */ 
/*    */     
/* 23 */     Vec3 directionVectorPlane = (new Vec3(targetPos.x - mobPos.x, 0.0D, targetPos.z - mobPos.z)).normalize().scale(0.5D);
/* 24 */     Vec3 targetPosition = targetPos.subtract(directionVectorPlane);
/*    */     
/* 26 */     Vec3 directionVector = targetPosition.subtract(mobPos);
/* 27 */     float angrad = angle * 3.1415927F / 180.0F;
/* 28 */     double xzAng = Math.atan2(directionVector.z, directionVector.x);
/* 29 */     double r2 = directionVector.subtract(0.0D, directionVector.y, 0.0D).lengthSqr();
/* 30 */     double r = Math.sqrt(r2);
/* 31 */     double y = directionVector.y;
/* 32 */     double g = body.getGravity();
/*    */     
/* 34 */     double sin2ang = Math.sin((2.0F * angrad));
/* 35 */     double cosangsqr = Math.pow(Math.cos(angrad), 2.0D);
/* 36 */     double sinangrad = Math.sin(angrad);
/* 37 */     double cosangrad = Math.cos(angrad);
/* 38 */     double sinxzAng = Math.sin(xzAng);
/* 39 */     double cosxzAng = Math.cos(xzAng);
/*    */     
/* 41 */     double v0sqr = r2 * g / (r * sin2ang - 2.0D * y * cosangsqr);
/* 42 */     if (v0sqr < 0.0D) {
/* 43 */       return Optional.empty();
/*    */     }
/*    */     
/* 46 */     double v0 = Math.sqrt(v0sqr);
/* 47 */     if (v0 > maxJumpVelocity) {
/* 48 */       return Optional.empty();
/*    */     }
/*    */     
/* 51 */     double v0r = v0 * cosangrad;
/* 52 */     double v0y = v0 * sinangrad;
/*    */     
/* 54 */     if (checkCollision) {
/*    */       
/* 56 */       int samples = Mth.ceil(r / v0r) * 2;
/* 57 */       double ri = 0.0D;
/* 58 */       Vec3 previousPos = null;
/*    */       
/* 60 */       EntityDimensions mobDimensions = body.getDimensions(Pose.LONG_JUMPING);
/* 61 */       for (int i = 0; i < samples - 1; i++) {
/* 62 */         ri += r / samples;
/* 63 */         double yi = sinangrad / cosangrad * ri - Math.pow(ri, 2.0D) * g / 2.0D * v0sqr * Math.pow(cosangrad, 2.0D);
/* 64 */         double xi = ri * cosxzAng;
/* 65 */         double zi = ri * sinxzAng;
/*    */         
/* 67 */         Vec3 samplePos = new Vec3(mobPos.x + xi, mobPos.y + yi, mobPos.z + zi);
/* 68 */         if (previousPos != null && !isClearTransition(body, mobDimensions, previousPos, samplePos)) {
/* 69 */           return Optional.empty();
/*    */         }
/*    */         
/* 72 */         previousPos = samplePos;
/*    */       } 
/*    */     } 
/*    */     
/* 76 */     return Optional.of((new Vec3(v0r * cosxzAng, v0y, v0r * sinxzAng)).scale(0.949999988079071D));
/*    */   }
/*    */   
/*    */   private static boolean isClearTransition(Mob body, EntityDimensions entityDimensions, Vec3 position1, Vec3 position2) {
/* 80 */     Vec3 direction = position2.subtract(position1);
/*    */     
/* 82 */     double minDimension = Math.min(entityDimensions.width(), entityDimensions.height());
/* 83 */     int checks = Mth.ceil(direction.length() / minDimension);
/*    */     
/* 85 */     Vec3 normalizedDirection = direction.normalize();
/* 86 */     Vec3 nextPointToCheck = position1;
/* 87 */     for (int i = 0; i < checks; i++) {
/* 88 */       nextPointToCheck = (i == checks - 1) ? position2 : nextPointToCheck.add(normalizedDirection.scale(minDimension * 0.8999999761581421D));
/* 89 */       if (!body.level().noCollision(body, entityDimensions.makeBoundingBox(nextPointToCheck))) {
/* 90 */         return false;
/*    */       }
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LongJumpUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */