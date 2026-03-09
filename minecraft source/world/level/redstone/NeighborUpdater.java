/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface NeighborUpdater
/*    */ {
/* 19 */   public static final Direction[] UPDATE_ORDER = { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };
/*    */   
/*    */   void shapeUpdate(Direction paramDirection, BlockState paramBlockState, BlockPos paramBlockPos1, BlockPos paramBlockPos2, @UpdateFlags int paramInt1, int paramInt2);
/*    */   
/*    */   void neighborChanged(BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation);
/*    */   
/*    */   void neighborChanged(BlockState paramBlockState, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean);
/*    */   
/*    */   default void updateNeighborsAtExceptFromFacing(BlockPos pos, Block block, Direction skipDirection, Orientation orientation) {
/* 28 */     for (Direction direction : UPDATE_ORDER) {
/* 29 */       if (direction != skipDirection) {
/* 30 */         neighborChanged(pos.relative(direction), block, null);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   static void executeShapeUpdate(LevelAccessor level, Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @UpdateFlags int updateFlags, int updateLimit) {
/* 36 */     BlockState currentState = level.getBlockState(pos);
/* 37 */     if ((updateFlags & 0x80) != 0 && currentState.is(Blocks.REDSTONE_WIRE)) {
/*    */       return;
/*    */     }
/* 40 */     BlockState newState = currentState.updateShape(level, level, pos, direction, neighborPos, neighborState, level.getRandom());
/* 41 */     Block.updateOrDestroy(currentState, newState, level, pos, updateFlags, updateLimit);
/*    */   }
/*    */   
/*    */   static void executeUpdate(Level level, BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {
/*    */     try {
/* 46 */       state.handleNeighborChanged(level, pos, changedBlock, orientation, movedByPiston);
/* 47 */     } catch (Throwable t) {
/* 48 */       CrashReport report = CrashReport.forThrowable(t, "Exception while updating neighbours");
/* 49 */       CrashReportCategory category = report.addCategory("Block being updated");
/*    */       
/* 51 */       category.setDetail("Source block type", () -> {
/*    */             try {
/* 53 */               return String.format(Locale.ROOT, "ID #%s (%s // %s)", new Object[] { BuiltInRegistries.BLOCK.getKey(changedBlock), changedBlock.getDescriptionId(), changedBlock.getClass().getCanonicalName() });
/* 54 */             } catch (Throwable ignored) {
/* 55 */               return "ID #" + String.valueOf(BuiltInRegistries.BLOCK.getKey(changedBlock));
/*    */             } 
/*    */           });
/*    */       
/* 59 */       CrashReportCategory.populateBlockDetails(category, level, pos, state);
/*    */       
/* 61 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\NeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */