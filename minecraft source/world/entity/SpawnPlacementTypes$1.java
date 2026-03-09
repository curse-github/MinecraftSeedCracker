/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
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
/*    */ 
/*    */ class null
/*    */   implements SpawnPlacementType
/*    */ {
/*    */   public boolean isSpawnPositionOk(LevelReader level, BlockPos blockPos, EntityType<?> type) {
/* 37 */     if (type == null || !level.getWorldBorder().isWithinBounds(blockPos)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     BlockPos above = blockPos.above();
/* 42 */     BlockPos below = blockPos.below();
/*    */     
/* 44 */     BlockState belowState = level.getBlockState(below);
/* 45 */     if (!belowState.isValidSpawn(level, below, type)) {
/* 46 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 50 */     return (isValidEmptySpawnBlock(level, blockPos, type) && 
/* 51 */       isValidEmptySpawnBlock(level, above, type));
/*    */   }
/*    */   
/*    */   private boolean isValidEmptySpawnBlock(LevelReader level, BlockPos blockPos, EntityType<?> type) {
/* 55 */     BlockState blockState = level.getBlockState(blockPos);
/* 56 */     return NaturalSpawner.isValidEmptySpawnBlock(level, blockPos, blockState, blockState.getFluidState(), type);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos adjustSpawnPosition(LevelReader level, BlockPos candidate) {
/* 61 */     BlockPos below = candidate.below();
/* 62 */     if (level.getBlockState(below).isPathfindable(PathComputationType.LAND)) {
/* 63 */       return below;
/*    */     }
/*    */     
/* 66 */     return candidate;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SpawnPlacementTypes$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */