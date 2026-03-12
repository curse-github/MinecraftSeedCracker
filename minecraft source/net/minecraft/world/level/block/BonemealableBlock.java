/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
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
/*    */ public interface BonemealableBlock
/*    */ {
/*    */   boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/* 34 */   static boolean hasSpreadableNeighbourPos(LevelReader level, BlockPos pos, BlockState blockToPlace) { return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.stream().toList(), level, pos, blockToPlace).isPresent(); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   static Optional<BlockPos> findSpreadableNeighbourPos(Level level, BlockPos pos, BlockState blockToPlace) { return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.shuffledCopy(level.random), level, pos, blockToPlace); }
/*    */ 
/*    */   
/*    */   private static Optional<BlockPos> getSpreadableNeighbourPos(List<Direction> directions, LevelReader level, BlockPos pos, BlockState blockToPlace) {
/* 42 */     for (Direction direction : directions) {
/* 43 */       BlockPos neighbourPos = pos.relative(direction);
/* 44 */       if (level.isEmptyBlock(neighbourPos) && blockToPlace.canSurvive(level, neighbourPos)) {
/* 45 */         return Optional.of(neighbourPos);
/*    */       }
/*    */     } 
/* 48 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default BlockPos getParticlePos(BlockPos blockPos) {
/* 52 */     switch (getType().ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*    */       
/* 54 */       blockPos;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   default Type getType() { return Type.GROWER; }
/*    */   
/*    */   public enum Type
/*    */   {
/* 63 */     NEIGHBOR_SPREADER,
/* 64 */     GROWER;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BonemealableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */