/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class HoverRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist, double xDir, double zDir, float maxXzRadiansDifference, int hoverMaxHeight, int hoverMinHeight) {
/* 10 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 12 */     return RandomPos.generateRandomPos(mob, () -> {
/* 13 */           BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), 0.0D, horizontalDist, verticalDist, 0, xDir, zDir, maxXzRadiansDifference);
/* 14 */           if (direction == null) {
/* 15 */             return null;
/*    */           }
/*    */           
/* 18 */           BlockPos pos = LandRandomPos.generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/* 19 */           if (pos == null) {
/* 20 */             return null;
/*    */           }
/*    */           
/* 23 */           pos = RandomPos.moveUpToAboveSolid(pos, mob.getRandom().nextInt(hoverMaxHeight - hoverMinHeight + 1) + hoverMinHeight, mob.level().getMaxY(), ());
/* 24 */           if (GoalUtils.isWater(mob, pos) || GoalUtils.hasMalus(mob, pos)) {
/* 25 */             return null;
/*    */           }
/*    */           
/* 28 */           return pos;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\HoverRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */