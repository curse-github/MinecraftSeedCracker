/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.ToDoubleFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LandRandomPos
/*    */ {
/* 13 */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist) { Objects.requireNonNull(mob); return getPos(mob, horizontalDist, verticalDist, mob::getWalkTargetValue); }
/*    */ 
/*    */   
/*    */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist, ToDoubleFunction<BlockPos> positionWeight) {
/* 17 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 19 */     return RandomPos.generateRandomPos(() -> {
/* 20 */           BlockPos direction = RandomPos.generateRandomDirection(mob.getRandom(), horizontalDist, verticalDist);
/*    */           
/* 22 */           BlockPos pos = generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/* 23 */           if (pos == null) {
/* 24 */             return null;
/*    */           }
/*    */           
/* 27 */           return movePosUpOutOfSolid(mob, pos);
/*    */         }positionWeight);
/*    */   }
/*    */   
/*    */   public static Vec3 getPosTowards(PathfinderMob mob, int horizontalDist, int verticalDist, Vec3 towardsPos) {
/* 32 */     Vec3 dir = towardsPos.subtract(mob.getX(), mob.getY(), mob.getZ());
/* 33 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 35 */     return getPosInDirection(mob, 0.0D, horizontalDist, verticalDist, dir, restrict);
/*    */   }
/*    */ 
/*    */   
/* 39 */   public static Vec3 getPosAway(PathfinderMob mob, int horizontalDist, int verticalDist, Vec3 avoidPos) { return getPosAway(mob, 0.0D, horizontalDist, verticalDist, avoidPos); }
/*    */   
/*    */   public static Vec3 getPosAway(PathfinderMob mob, double minHorizontalDist, double maxHorizontalDist, int verticalDist, Vec3 avoidPos) {
/* 42 */     Vec3 dirAway = mob.position().subtract(avoidPos);
/* 43 */     if (dirAway.length() == 0.0D) {
/* 44 */       dirAway = new Vec3(mob.getRandom().nextDouble() - 0.5D, 0.0D, mob.getRandom().nextDouble() - 0.5D);
/*    */     }
/* 46 */     boolean restrict = GoalUtils.mobRestricted(mob, maxHorizontalDist);
/*    */     
/* 48 */     return getPosInDirection(mob, minHorizontalDist, maxHorizontalDist, verticalDist, dirAway, restrict);
/*    */   }
/*    */   
/*    */   private static Vec3 getPosInDirection(PathfinderMob mob, double minHorizontalDist, double maxHorizontalDist, int verticalDist, Vec3 dir, boolean restrict) {
/* 52 */     return RandomPos.generateRandomPos(mob, () -> {
/* 53 */           BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), minHorizontalDist, maxHorizontalDist, verticalDist, 0, dir.x, dir.z, 1.5707963705062866D);
/* 54 */           if (direction == null) {
/* 55 */             return null;
/*    */           }
/*    */           
/* 58 */           BlockPos pos = generateRandomPosTowardDirection(mob, maxHorizontalDist, restrict, direction);
/* 59 */           if (pos == null) {
/* 60 */             return null;
/*    */           }
/*    */           
/* 63 */           return movePosUpOutOfSolid(mob, pos);
/*    */         });
/*    */   }
/*    */   
/*    */   public static BlockPos movePosUpOutOfSolid(PathfinderMob mob, BlockPos pos) {
/* 68 */     pos = RandomPos.moveUpOutOfSolid(pos, mob.level().getMaxY(), blockPos -> GoalUtils.isSolid(mob, blockPos));
/* 69 */     if (GoalUtils.isWater(mob, pos) || GoalUtils.hasMalus(mob, pos)) {
/* 70 */       return null;
/*    */     }
/* 72 */     return pos;
/*    */   }
/*    */   
/*    */   public static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, double horizontalDist, boolean restrict, BlockPos direction) {
/* 76 */     BlockPos pos = RandomPos.generateRandomPosTowardDirection(mob, horizontalDist, mob.getRandom(), direction);
/* 77 */     if (GoalUtils.isOutsideLimits(pos, mob) || GoalUtils.isRestricted(restrict, mob, pos) || GoalUtils.isNotStable(mob.getNavigation(), pos)) {
/* 78 */       return null;
/*    */     }
/*    */     
/* 81 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\LandRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */