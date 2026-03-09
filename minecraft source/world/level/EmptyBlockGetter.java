/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public static enum EmptyBlockGetter
/*    */   implements BlockGetter {
/* 12 */   INSTANCE;
/*    */ 
/*    */ 
/*    */   
/* 16 */   public BlockEntity getBlockEntity(BlockPos pos) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public BlockState getBlockState(BlockPos pos) { return Blocks.AIR.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public FluidState getFluidState(BlockPos pos) { return Fluids.EMPTY.defaultFluidState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public int getMinY() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getHeight() { return 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\EmptyBlockGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */