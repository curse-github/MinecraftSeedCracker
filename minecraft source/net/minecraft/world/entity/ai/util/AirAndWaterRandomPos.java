/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AirAndWaterRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist, int flyingHeight, double xDir, double zDir, double maxXzRadiansDifference) {
/* 10 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 12 */     return RandomPos.generateRandomPos(mob, () -> generateRandomPos(mob, horizontalDist, verticalDist, flyingHeight, xDir, zDir, maxXzRadiansDifference, restrict));
/*    */   }
/*    */   
/*    */   public static BlockPos generateRandomPos(PathfinderMob mob, int horizontalDist, int verticalDist, int flyingHeight, double xDir, double zDir, double maxXzRadiansDifference, boolean restrict) {
/* 16 */     BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), 0.0D, horizontalDist, verticalDist, flyingHeight, xDir, zDir, maxXzRadiansDifference);
/* 17 */     if (direction == null) {
/* 18 */       return null;
/*    */     }
/*    */     
/* 21 */     BlockPos pos = RandomPos.generateRandomPosTowardDirection(mob, horizontalDist, mob.getRandom(), direction);
/* 22 */     if (GoalUtils.isOutsideLimits(pos, mob) || GoalUtils.isRestricted(restrict, mob, pos)) {
/* 23 */       return null;
/*    */     }
/*    */     
/* 26 */     pos = RandomPos.moveUpOutOfSolid(pos, mob.level().getMaxY(), blockPos -> GoalUtils.isSolid(mob, blockPos));
/* 27 */     if (GoalUtils.hasMalus(mob, pos)) {
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\AirAndWaterRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */