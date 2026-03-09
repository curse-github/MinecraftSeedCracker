/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public abstract class RedstoneWireEvaluator
/*    */ {
/*    */   protected final RedStoneWireBlock wireBlock;
/*    */   
/* 14 */   protected RedstoneWireEvaluator(RedStoneWireBlock wireBlock) { this.wireBlock = wireBlock; }
/*    */ 
/*    */   
/*    */   public abstract void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean);
/*    */ 
/*    */   
/* 20 */   protected int getBlockSignal(Level level, BlockPos pos) { return this.wireBlock.getBlockSignal(level, pos); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected int getWireSignal(BlockPos pos, BlockState state) { return state.is(this.wireBlock) ? ((Integer)state.getValue(RedStoneWireBlock.POWER)).intValue() : 0; }
/*    */ 
/*    */   
/*    */   protected int getIncomingWireSignal(Level level, BlockPos pos) {
/* 28 */     int wireSignal = 0;
/* 29 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 30 */       BlockPos neighborPos = pos.relative(direction);
/* 31 */       BlockState neighborState = level.getBlockState(neighborPos);
/*    */       
/* 33 */       wireSignal = Math.max(wireSignal, getWireSignal(neighborPos, neighborState));
/*    */       
/* 35 */       BlockPos abovePos = pos.above();
/* 36 */       if (neighborState.isRedstoneConductor(level, neighborPos) && !level.getBlockState(abovePos).isRedstoneConductor(level, abovePos)) {
/* 37 */         BlockPos aboveNeighborPos = neighborPos.above();
/* 38 */         wireSignal = Math.max(wireSignal, getWireSignal(aboveNeighborPos, level.getBlockState(aboveNeighborPos))); continue;
/* 39 */       }  if (!neighborState.isRedstoneConductor(level, neighborPos)) {
/* 40 */         BlockPos belowNeighborPos = neighborPos.below();
/* 41 */         wireSignal = Math.max(wireSignal, getWireSignal(belowNeighborPos, level.getBlockState(belowNeighborPos)));
/*    */       } 
/*    */     } 
/* 44 */     return Math.max(0, wireSignal - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\RedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */