/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class StrollThroughVillageGoal
/*    */   extends Goal
/*    */ {
/*    */   private static final int DISTANCE_THRESHOLD = 10;
/*    */   private final PathfinderMob mob;
/*    */   private final int interval;
/*    */   private BlockPos wantedPos;
/*    */   
/*    */   public StrollThroughVillageGoal(PathfinderMob mob, int interval) {
/* 24 */     this.mob = mob;
/* 25 */     this.interval = reducedTickDelay(interval);
/* 26 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 31 */     if (this.mob.hasControllingPassenger()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     if (this.mob.level().isBrightOutside()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     if (this.mob.getRandom().nextInt(this.interval) != 0) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     ServerLevel level = (ServerLevel)this.mob.level();
/*    */     
/* 45 */     BlockPos pos = this.mob.blockPosition();
/* 46 */     if (!level.isCloseToVillage(pos, 6)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     Vec3 landPos = LandRandomPos.getPos(this.mob, 15, 7, p -> -level.sectionsToVillage(SectionPos.of(p)));
/* 51 */     this.wantedPos = (landPos == null) ? null : BlockPos.containing(landPos);
/* 52 */     return (this.wantedPos != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean canContinueToUse() { return (this.wantedPos != null && !this.mob.getNavigation().isDone() && this.mob.getNavigation().getTargetPos().equals(this.wantedPos)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 62 */     if (this.wantedPos == null) {
/*    */       return;
/*    */     }
/* 65 */     PathNavigation navigation = this.mob.getNavigation();
/* 66 */     if (navigation.isDone() && 
/* 67 */       !this.wantedPos.closerToCenterThan(this.mob.position(), 10.0D)) {
/* 68 */       Vec3 longDistanceTarget = Vec3.atBottomCenterOf(this.wantedPos);
/*    */ 
/*    */       
/* 71 */       Vec3 selfVector = this.mob.position();
/* 72 */       Vec3 distance = selfVector.subtract(longDistanceTarget);
/*    */       
/* 74 */       longDistanceTarget = distance.scale(0.4D).add(longDistanceTarget);
/*    */       
/* 76 */       Vec3 moveTarget = longDistanceTarget.subtract(selfVector).normalize().scale(10.0D).add(selfVector);
/* 77 */       BlockPos pathTarget = BlockPos.containing(moveTarget);
/* 78 */       pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pathTarget);
/*    */       
/* 80 */       if (!navigation.moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), 1.0D))
/*    */       {
/* 82 */         moveRandomly();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void moveRandomly() {
/* 89 */     RandomSource random = this.mob.getRandom();
/* 90 */     BlockPos pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
/* 91 */     this.mob.getNavigation().moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\StrollThroughVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */