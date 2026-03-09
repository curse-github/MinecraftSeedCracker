/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSpreaderConfig
/*     */   implements MultifaceSpreader.SpreadConfig
/*     */ {
/*     */   protected MultifaceBlock block;
/*     */   
/* 143 */   public DefaultSpreaderConfig(MultifaceBlock block) { this.block = block; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) { return this.block.getStateForPlacement(oldState, level, placementPos, placementDirection); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   protected boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) { return (existingState.isAir() || existingState.is(this.block) || (existingState.is(Blocks.WATER) && existingState.getFluidState().isSource())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSpreadInto(BlockGetter level, BlockPos sourcePos, MultifaceSpreader.SpreadPos spreadPos) {
/* 157 */     BlockState existingState = level.getBlockState(spreadPos.pos());
/* 158 */     return (stateCanBeReplaced(level, sourcePos, spreadPos.pos(), spreadPos.face(), existingState) && this.block.isValidStateForPlacement(level, existingState, spreadPos.pos(), spreadPos.face()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreader$DefaultSpreaderConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */