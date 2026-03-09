/*    */ package net.minecraft.world.entity.monster.breeze;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BreezeUtil
/*    */ {
/*    */   private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 50.0D;
/*    */   
/*    */   public static Vec3 randomPointBehindTarget(LivingEntity enemy, RandomSource random) {
/* 18 */     int spreadDegrees = 90;
/* 19 */     float viewAngle = enemy.yHeadRot + 180.0F + (float)random.nextGaussian() * 90.0F / 2.0F;
/* 20 */     float r = Mth.lerp(random.nextFloat(), 4.0F, 8.0F);
/*    */     
/* 22 */     Vec3 direction = Vec3.directionFromRotation(0.0F, viewAngle).scale(r);
/* 23 */     return enemy.position().add(direction);
/*    */   }
/*    */   
/*    */   public static boolean hasLineOfSight(Breeze breeze, Vec3 target) {
/* 27 */     Vec3 from = new Vec3(breeze.getX(), breeze.getY(), breeze.getZ());
/* 28 */     if (target.distanceTo(from) > getMaxLineOfSightTestRange(breeze)) {
/* 29 */       return false;
/*    */     }
/* 31 */     return (breeze.level().clip(new ClipContext(from, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, breeze)).getType() == HitResult.Type.MISS);
/*    */   }
/*    */ 
/*    */   
/* 35 */   private static double getMaxLineOfSightTestRange(Breeze breeze) { return Math.max(50.0D, breeze.getAttributeValue(Attributes.FOLLOW_RANGE)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\BreezeUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */