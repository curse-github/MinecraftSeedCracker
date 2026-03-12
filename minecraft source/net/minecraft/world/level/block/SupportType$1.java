/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ static enum null
/*    */ {
/* 15 */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SupportType$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */