/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.goal.WrappedGoal;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class PathfinderMob
/*    */   extends Mob
/*    */ {
/*    */   protected static final float DEFAULT_WALK_TARGET_VALUE = 0.0F;
/*    */   
/* 17 */   protected PathfinderMob(EntityType<? extends PathfinderMob> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public float getWalkTargetValue(BlockPos pos) { return getWalkTargetValue(pos, level()); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason spawnReason) { return (getWalkTargetValue(blockPosition(), level) >= 0.0F); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isPathFinding() { return !getNavigation().isDone(); }
/*    */ 
/*    */   
/*    */   public boolean isPanicking() {
/* 38 */     if (this.brain.hasMemoryValue(MemoryModuleType.IS_PANICKING)) {
/* 39 */       return this.brain.getMemory(MemoryModuleType.IS_PANICKING).isPresent();
/*    */     }
/* 41 */     for (WrappedGoal wrappedGoal : this.goalSelector.getAvailableGoals()) {
/* 42 */       if (wrappedGoal.isRunning() && wrappedGoal.getGoal() instanceof net.minecraft.world.entity.ai.goal.PanicGoal) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected boolean shouldStayCloseToLeashHolder() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void closeRangeLeashBehaviour(Entity leashHolder) {
/* 56 */     super.closeRangeLeashBehaviour(leashHolder);
/* 57 */     if (shouldStayCloseToLeashHolder() && !isPanicking()) {
/* 58 */       this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
/* 59 */       float wantedDistance = 2.0F;
/* 60 */       float distanceTo = distanceTo(leashHolder);
/*    */       
/* 62 */       Vec3 delta = (new Vec3(leashHolder.getX() - getX(), leashHolder.getY() - getY(), leashHolder.getZ() - getZ())).normalize().scale(Math.max(distanceTo - 2.0F, 0.0F));
/* 63 */       getNavigation().moveTo(getX() + delta.x, getY() + delta.y, getZ() + delta.z, followLeashSpeed());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void whenLeashedTo(Entity leashHolder) {
/* 69 */     setHomeTo(leashHolder.blockPosition(), (int)leashElasticDistance() - 1);
/* 70 */     super.whenLeashedTo(leashHolder);
/*    */   }
/*    */ 
/*    */   
/* 74 */   protected double followLeashSpeed() { return 1.0D; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\PathfinderMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */