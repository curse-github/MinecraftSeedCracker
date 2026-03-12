/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
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
/*     */ class SilverfishWakeUpFriendsGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Silverfish silverfish;
/*     */   private int lookForFriends;
/*     */   
/* 142 */   public SilverfishWakeUpFriendsGoal(Silverfish silverfish) { this.silverfish = silverfish; }
/*     */ 
/*     */   
/*     */   public void notifyHurt() {
/* 146 */     if (this.lookForFriends == 0) {
/* 147 */       this.lookForFriends = adjustedTickDelay(20);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public boolean canUse() { return (this.lookForFriends > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 158 */     this.lookForFriends--;
/* 159 */     if (this.lookForFriends <= 0) {
/* 160 */       Level level = this.silverfish.level();
/* 161 */       RandomSource random = this.silverfish.getRandom();
/*     */ 
/*     */       
/* 164 */       BlockPos basePos = this.silverfish.blockPosition();
/*     */       
/*     */       int yOff;
/* 167 */       for (yOff = 0; yOff <= 5 && yOff >= -5; yOff = ((yOff <= 0) ? 1 : 0) - yOff) {
/* 168 */         int xOff; for (xOff = 0; xOff <= 10 && xOff >= -10; xOff = ((xOff <= 0) ? 1 : 0) - xOff) {
/* 169 */           int zOff; for (zOff = 0; zOff <= 10 && zOff >= -10; zOff = ((zOff <= 0) ? 1 : 0) - zOff) {
/* 170 */             BlockPos testPos = basePos.offset(xOff, yOff, zOff);
/* 171 */             BlockState blockState = level.getBlockState(testPos);
/*     */             
/* 173 */             Block block = blockState.getBlock();
/* 174 */             if (block instanceof InfestedBlock) {
/* 175 */               if (((Boolean)getServerLevel(level).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 176 */                 level.destroyBlock(testPos, true, this.silverfish);
/*     */               } else {
/* 178 */                 level.setBlock(testPos, ((InfestedBlock)block).hostStateByInfested(level.getBlockState(testPos)), 3);
/*     */               } 
/* 180 */               if (random.nextBoolean())
/*     */                 // Byte code: goto -> 251 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Silverfish$SilverfishWakeUpFriendsGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */