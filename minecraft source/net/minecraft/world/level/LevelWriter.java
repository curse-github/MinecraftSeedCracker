/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelWriter
/*    */ {
/*    */   boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt1, int paramInt2);
/*    */   
/* 14 */   default boolean setBlock(BlockPos pos, BlockState blockState, @UpdateFlags int updateFlags) { return setBlock(pos, blockState, updateFlags, 512); }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean);
/*    */ 
/*    */   
/* 21 */   default boolean destroyBlock(BlockPos pos, boolean dropResources) { return destroyBlock(pos, dropResources, null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   default boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker) { return destroyBlock(pos, dropResources, breaker, 512); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity, int paramInt);
/*    */ 
/*    */ 
/*    */   
/* 35 */   default boolean addFreshEntity(Entity entity) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\LevelWriter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */