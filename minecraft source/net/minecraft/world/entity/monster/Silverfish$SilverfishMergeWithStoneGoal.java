/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.InfestedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SilverfishMergeWithStoneGoal
/*     */   extends RandomStrollGoal
/*     */ {
/*     */   private Direction selectedDirection;
/*     */   private boolean doMerge;
/*     */   
/*     */   public SilverfishMergeWithStoneGoal(Silverfish silverfish) {
/* 196 */     super(silverfish, 1.0D, 10);
/*     */     
/* 198 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 203 */     if (this.mob.getTarget() != null) {
/* 204 */       return false;
/*     */     }
/* 206 */     if (!this.mob.getNavigation().isDone()) {
/* 207 */       return false;
/*     */     }
/*     */     
/* 210 */     RandomSource random = this.mob.getRandom();
/* 211 */     if (((Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && random.nextInt(reducedTickDelay(10)) == 0) {
/* 212 */       this.selectedDirection = Direction.getRandom(random);
/*     */       
/* 214 */       BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 215 */       BlockState blockState = this.mob.level().getBlockState(pos);
/* 216 */       if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 217 */         this.doMerge = true;
/* 218 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     this.doMerge = false;
/* 223 */     return super.canUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 228 */     if (this.doMerge) {
/* 229 */       return false;
/*     */     }
/* 231 */     return super.canContinueToUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 236 */     if (!this.doMerge) {
/* 237 */       super.start();
/*     */       
/*     */       return;
/*     */     } 
/* 241 */     Level level1 = this.mob.level();
/* 242 */     BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.selectedDirection);
/* 243 */     BlockState blockState = level1.getBlockState(pos);
/*     */     
/* 245 */     if (InfestedBlock.isCompatibleHostBlock(blockState)) {
/* 246 */       level1.setBlock(pos, InfestedBlock.infestedStateByHost(blockState), 3);
/* 247 */       this.mob.spawnAnim();
/* 248 */       this.mob.discard();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Silverfish$SilverfishMergeWithStoneGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */