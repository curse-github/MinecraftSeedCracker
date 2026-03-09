/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements SculkBehaviour
/*    */ {
/*    */   public boolean attemptSpreadVein(LevelAccessor level, BlockPos pos, BlockState state, Collection<Direction> facings, boolean postProcess) {
/* 42 */     if (facings == null) {
/* 43 */       return (((SculkVeinBlock)Blocks.SCULK_VEIN).getSameSpaceSpreader().spreadAll(level.getBlockState(pos), level, pos, postProcess) > 0L);
/*    */     }
/* 45 */     if (!facings.isEmpty()) {
/* 46 */       if (state.isAir() || state.getFluidState().is(Fluids.WATER)) {
/* 47 */         return SculkVeinBlock.regrow(level, pos, state, facings);
/*    */       }
/* 49 */       return false;
/*    */     } 
/* 51 */     return super.attemptSpreadVein(level, pos, state, facings, postProcess);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) { return (cursor.getDecayDelay() > 0) ? cursor.getCharge() : 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public int updateDecayDelay(int age) { return Math.max(age - 1, 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SculkBehaviour$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */