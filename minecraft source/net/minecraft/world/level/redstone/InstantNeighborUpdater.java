/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class InstantNeighborUpdater
/*    */   implements NeighborUpdater {
/*    */   private final Level level;
/*    */   
/* 14 */   public InstantNeighborUpdater(Level level) { this.level = level; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void shapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @UpdateFlags int updateFlags, int updateLimit) { NeighborUpdater.executeShapeUpdate(this.level, direction, pos, neighborPos, neighborState, updateFlags, updateLimit - 1); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {
/* 24 */     BlockState state = this.level.getBlockState(pos);
/* 25 */     neighborChanged(state, pos, changedBlock, orientation, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) { NeighborUpdater.executeUpdate(this.level, state, pos, changedBlock, orientation, movedByPiston); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\InstantNeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */