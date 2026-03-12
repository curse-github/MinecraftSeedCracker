/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ 
/*    */ public interface SpawnPlacementTypes
/*    */ {
/*    */   public static final SpawnPlacementType NO_RESTRICTIONS = (level, blockPos, type) -> true;
/*    */   public static final SpawnPlacementType IN_WATER = (level, blockPos, type) -> {
/* 15 */       if (type == null || !level.getWorldBorder().isWithinBounds(blockPos)) {
/* 16 */         return false;
/*    */       }
/*    */       
/* 19 */       BlockPos above = blockPos.above();
/*    */ 
/*    */       
/* 22 */       return (level.getFluidState(blockPos).is(FluidTags.WATER) && 
/* 23 */         !level.getBlockState(above).isRedstoneConductor(level, above));
/*    */     };
/*    */   
/*    */   public static final SpawnPlacementType IN_LAVA = (level, blockPos, type) -> {
/* 27 */       if (type == null || !level.getWorldBorder().isWithinBounds(blockPos)) {
/* 28 */         return false;
/*    */       }
/*    */       
/* 31 */       return level.getFluidState(blockPos).is(FluidTags.LAVA);
/*    */     };
/*    */   
/* 34 */   public static final SpawnPlacementType ON_GROUND = new SpawnPlacementType()
/*    */     {
/*    */       public boolean isSpawnPositionOk(LevelReader level, BlockPos blockPos, EntityType<?> type) {
/* 37 */         if (type == null || !level.getWorldBorder().isWithinBounds(blockPos)) {
/* 38 */           return false;
/*    */         }
/*    */         
/* 41 */         BlockPos above = blockPos.above();
/* 42 */         BlockPos below = blockPos.below();
/*    */         
/* 44 */         BlockState belowState = level.getBlockState(below);
/* 45 */         if (!belowState.isValidSpawn(level, below, type)) {
/* 46 */           return false;
/*    */         }
/*    */ 
/*    */         
/* 50 */         return (isValidEmptySpawnBlock(level, blockPos, type) && 
/* 51 */           isValidEmptySpawnBlock(level, above, type));
/*    */       }
/*    */       
/*    */       private boolean isValidEmptySpawnBlock(LevelReader level, BlockPos blockPos, EntityType<?> type) {
/* 55 */         BlockState blockState = level.getBlockState(blockPos);
/* 56 */         return NaturalSpawner.isValidEmptySpawnBlock(level, blockPos, blockState, blockState.getFluidState(), type);
/*    */       }
/*    */ 
/*    */       
/*    */       public BlockPos adjustSpawnPosition(LevelReader level, BlockPos candidate) {
/* 61 */         BlockPos below = candidate.below();
/* 62 */         if (level.getBlockState(below).isPathfindable(PathComputationType.LAND)) {
/* 63 */           return below;
/*    */         }
/*    */         
/* 66 */         return candidate;
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SpawnPlacementTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */