/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*    */ 
/*    */ 
/*    */ public class WaterloggedVegetationPatchFeature
/*    */   extends VegetationPatchFeature
/*    */ {
/* 21 */   public WaterloggedVegetationPatchFeature(Codec<VegetationPatchConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Set<BlockPos> placeGroundPatch(WorldGenLevel level, VegetationPatchConfiguration config, RandomSource random, BlockPos origin, Predicate<BlockState> replaceable, int xRadius, int zRadius) {
/* 26 */     Set<BlockPos> surface = super.placeGroundPatch(level, config, random, origin, replaceable, xRadius, zRadius);
/* 27 */     Set<BlockPos> waterSurface = new HashSet<BlockPos>();
/* 28 */     BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
/* 29 */     for (BlockPos surfacePos : surface) {
/* 30 */       if (!isExposed(level, surface, surfacePos, testPos)) {
/* 31 */         waterSurface.add(surfacePos);
/*    */       }
/*    */     } 
/* 34 */     for (BlockPos surfacePos : waterSurface) {
/* 35 */       level.setBlock(surfacePos, Blocks.WATER.defaultBlockState(), 2);
/*    */     }
/* 37 */     return waterSurface;
/*    */   }
/*    */   
/*    */   private static boolean isExposed(WorldGenLevel level, Set<BlockPos> surface, BlockPos pos, BlockPos.MutableBlockPos testPos) {
/* 41 */     return (isExposedDirection(level, pos, testPos, Direction.NORTH) || 
/* 42 */       isExposedDirection(level, pos, testPos, Direction.EAST) || 
/* 43 */       isExposedDirection(level, pos, testPos, Direction.SOUTH) || 
/* 44 */       isExposedDirection(level, pos, testPos, Direction.WEST) || 
/* 45 */       isExposedDirection(level, pos, testPos, Direction.DOWN));
/*    */   }
/*    */   
/*    */   private static boolean isExposedDirection(WorldGenLevel level, BlockPos pos, BlockPos.MutableBlockPos testPos, Direction direction) {
/* 49 */     testPos.setWithOffset(pos, direction);
/* 50 */     return !level.getBlockState(testPos).isFaceSturdy(level, testPos, direction.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeVegetation(WorldGenLevel level, VegetationPatchConfiguration config, ChunkGenerator generator, RandomSource random, BlockPos placementPos) {
/* 55 */     if (super.placeVegetation(level, config, generator, random, placementPos.below())) {
/* 56 */       BlockState placed = level.getBlockState(placementPos);
/* 57 */       if (placed.hasProperty(BlockStateProperties.WATERLOGGED) && !((Boolean)placed.getValue(BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 58 */         level.setBlock(placementPos, (BlockState)placed.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 2);
/*    */       }
/* 60 */       return true;
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WaterloggedVegetationPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */