/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class MoveBackToVillageGoal
/*    */   extends RandomStrollGoal
/*    */ {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   private static final int MAX_Y_DIST = 7;
/*    */   
/* 18 */   public MoveBackToVillageGoal(PathfinderMob mob, double speedModifier, boolean checkNoActionTime) { super(mob, speedModifier, 10, checkNoActionTime); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     ServerLevel level = (ServerLevel)this.mob.level();
/* 24 */     BlockPos pos = this.mob.blockPosition();
/*    */     
/* 26 */     if (level.isVillage(pos)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     return super.canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 35 */     ServerLevel level = (ServerLevel)this.mob.level();
/* 36 */     BlockPos pos = this.mob.blockPosition();
/*    */     
/* 38 */     SectionPos sectionPos = SectionPos.of(pos);
/* 39 */     SectionPos optimalSectionPos = BehaviorUtils.findSectionClosestToVillage(level, sectionPos, 2);
/*    */     
/* 41 */     if (optimalSectionPos != sectionPos) {
/* 42 */       return DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(optimalSectionPos.center()), 1.5707963705062866D);
/*    */     }
/*    */     
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveBackToVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */