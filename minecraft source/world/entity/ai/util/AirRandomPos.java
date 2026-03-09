/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AirRandomPos
/*    */ {
/*    */   public static Vec3 getPosTowards(PathfinderMob mob, int horizontalDist, int verticalDist, int flyingHeight, Vec3 towardsPos, double maxXzRadiansFromDir) {
/* 10 */     Vec3 dir = towardsPos.subtract(mob.getX(), mob.getY(), mob.getZ());
/* 11 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 13 */     return RandomPos.generateRandomPos(mob, () -> {
/* 14 */           BlockPos pos = AirAndWaterRandomPos.generateRandomPos(mob, horizontalDist, verticalDist, flyingHeight, dir.x, dir.z, maxXzRadiansFromDir, restrict);
/* 15 */           if (pos == null || GoalUtils.isWater(mob, pos)) {
/* 16 */             return null;
/*    */           }
/*    */           
/* 19 */           return pos;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\AirRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */