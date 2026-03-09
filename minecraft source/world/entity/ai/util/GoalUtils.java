/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoalUtils
/*    */ {
/* 13 */   public static boolean hasGroundPathNavigation(Mob mob) { return mob.getNavigation().canNavigateGround(); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static boolean mobRestricted(PathfinderMob mob, double horizontalDist) { return (mob.hasHome() && mob.getHomePosition().closerToCenterThan(mob.position(), mob.getHomeRadius() + horizontalDist + 1.0D)); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static boolean isOutsideLimits(BlockPos pos, PathfinderMob mob) { return mob.level().isOutsideBuildHeight(pos.getY()); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static boolean isRestricted(boolean restrict, PathfinderMob mob, BlockPos pos) { return (restrict && !mob.isWithinHome(pos)); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static boolean isRestricted(boolean restrict, PathfinderMob mob, Vec3 pos) { return (restrict && !mob.isWithinHome(pos)); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static boolean isNotStable(PathNavigation navigation, BlockPos pos) { return !navigation.isStableDestination(pos); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static boolean isWater(PathfinderMob mob, BlockPos pos) { return mob.level().getFluidState(pos).is(FluidTags.WATER); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static boolean hasMalus(PathfinderMob mob, BlockPos pos) { return (mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(mob, pos)) != 0.0F); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static boolean isSolid(PathfinderMob mob, BlockPos pos) { return mob.level().getBlockState(pos).isSolid(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\GoalUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */