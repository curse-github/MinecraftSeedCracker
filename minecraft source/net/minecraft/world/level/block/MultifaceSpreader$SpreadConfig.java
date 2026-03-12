/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SpreadConfig
/*     */ {
/*     */   BlockState getStateForPlacement(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*     */   
/*     */   boolean canSpreadInto(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
/*     */   
/* 111 */   default MultifaceSpreader.SpreadType[] getSpreadTypes() { return MultifaceSpreader.DEFAULT_SPREAD_ORDER; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   default boolean hasFace(BlockState state, Direction face) { return MultifaceBlock.hasFace(state, face); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   default boolean isOtherBlockValidAsSource(BlockState state) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 123 */   default boolean canSpreadFrom(BlockState state, Direction face) { return (isOtherBlockValidAsSource(state) || hasFace(state, face)); }
/*     */ 
/*     */   
/*     */   default boolean placeBlock(LevelAccessor level, MultifaceSpreader.SpreadPos spreadPos, BlockState oldState, boolean postProcess) {
/* 127 */     BlockState spreadState = getStateForPlacement(oldState, level, spreadPos.pos(), spreadPos.face());
/* 128 */     if (spreadState != null) {
/*     */       
/* 130 */       if (postProcess) {
/* 131 */         level.getChunk(spreadPos.pos()).markPosForPostprocessing(spreadPos.pos());
/*     */       }
/* 133 */       return level.setBlock(spreadPos.pos(), spreadState, 2);
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */