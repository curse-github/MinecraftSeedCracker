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
/*    */ static enum null
/*    */ {
/*    */   private final VoxelShape CENTER_SUPPORT_SHAPE;
/*    */   
/* 19 */   null() { this.CENTER_SUPPORT_SHAPE = Block.column(2.0D, 0.0D, 10.0D); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SupportType$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */