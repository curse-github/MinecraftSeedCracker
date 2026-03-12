/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*    */ static enum null
/*    */ {
/*    */   private final VoxelShape RIGID_SUPPORT_SHAPE;
/*    */   
/*    */   null() {
/* 27 */     this.RIGID_SUPPORT_SHAPE = Shapes.join(
/* 28 */         Shapes.block(), 
/* 29 */         Block.column(12.0D, 0.0D, 16.0D), BooleanOp.ONLY_FIRST);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SupportType$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */