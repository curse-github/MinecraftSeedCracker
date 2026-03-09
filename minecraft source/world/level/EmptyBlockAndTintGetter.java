/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public static enum EmptyBlockAndTintGetter
/*    */   implements BlockAndTintGetter
/*    */ {
/* 15 */   INSTANCE;
/*    */ 
/*    */ 
/*    */   
/* 19 */   public float getShade(Direction direction, boolean shade) { return 1.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public LevelLightEngine getLightEngine() { return LevelLightEngine.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public int getBlockTint(BlockPos pos, ColorResolver color) { return -1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public BlockEntity getBlockEntity(BlockPos pos) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public BlockState getBlockState(BlockPos pos) { return Blocks.AIR.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public FluidState getFluidState(BlockPos pos) { return Fluids.EMPTY.defaultFluidState(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int getHeight() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public int getMinY() { return 0; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\EmptyBlockAndTintGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */