/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public abstract class CoralFeature
/*    */   extends Feature<NoneFeatureConfiguration>
/*    */ {
/* 23 */   public CoralFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 28 */     RandomSource random = context.random();
/* 29 */     WorldGenLevel level = context.level();
/* 30 */     BlockPos origin = context.origin();
/* 31 */     Optional<Block> coral = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.CORAL_BLOCKS, random).map(Holder::value);
/* 32 */     if (coral.isEmpty()) {
/* 33 */       return false;
/*    */     }
/* 35 */     return placeFeature(level, random, origin, ((Block)coral.get()).defaultBlockState());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean placeCoralBlock(LevelAccessor level, RandomSource random, BlockPos pos, BlockState state) {
/* 41 */     BlockPos above = pos.above();
/* 42 */     BlockState targetBlockState = level.getBlockState(pos);
/*    */     
/* 44 */     if ((!targetBlockState.is(Blocks.WATER) && !targetBlockState.is(BlockTags.CORALS)) || !level.getBlockState(above).is(Blocks.WATER)) {
/* 45 */       return false;
/*    */     }
/*    */     
/* 48 */     level.setBlock(pos, state, 3);
/* 49 */     if (random.nextFloat() < 0.25F) {
/* 50 */       BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.CORALS, random).map(Holder::value).ifPresent(block -> 
/* 51 */           level.setBlock(above, block.defaultBlockState(), 2));
/*    */     }
/* 53 */     else if (random.nextFloat() < 0.05F) {
/* 54 */       level.setBlock(above, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(random.nextInt(4) + 1)), 2);
/*    */     } 
/*    */     
/* 57 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 58 */       if (random.nextFloat() < 0.2F) {
/* 59 */         BlockPos relativePos = pos.relative(direction);
/* 60 */         if (level.getBlockState(relativePos).is(Blocks.WATER)) {
/* 61 */           BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, random).map(Holder::value).ifPresent(coral -> {
/* 62 */                 BlockState coralFanState = coral.defaultBlockState();
/* 63 */                 if (coralFanState.hasProperty(BaseCoralWallFanBlock.FACING)) {
/* 64 */                   coralFanState = (BlockState)coralFanState.setValue(BaseCoralWallFanBlock.FACING, direction);
/*    */                 }
/* 66 */                 level.setBlock(relativePos, coralFanState, 2);
/*    */               });
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract boolean placeFeature(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */