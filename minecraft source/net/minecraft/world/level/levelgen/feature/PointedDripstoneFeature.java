/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
/*    */ 
/*    */ public class PointedDripstoneFeature
/*    */   extends Feature<PointedDripstoneConfiguration>
/*    */ {
/* 15 */   public PointedDripstoneFeature(Codec<PointedDripstoneConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> context) {
/* 20 */     WorldGenLevel worldGenLevel = context.level();
/* 21 */     BlockPos pos = context.origin();
/* 22 */     RandomSource random = context.random();
/* 23 */     PointedDripstoneConfiguration config = (PointedDripstoneConfiguration)context.config();
/* 24 */     Optional<Direction> tipDirection = getTipDirection(worldGenLevel, pos, random);
/*    */     
/* 26 */     if (tipDirection.isEmpty()) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     BlockPos rootPos = pos.relative(((Direction)tipDirection.get()).getOpposite());
/*    */     
/* 32 */     createPatchOfDripstoneBlocks(worldGenLevel, random, rootPos, config);
/*    */     
/* 34 */     int height = (random.nextFloat() < config.chanceOfTallerDripstone && DripstoneUtils.isEmptyOrWater(worldGenLevel.getBlockState(pos.relative((Direction)tipDirection.get())))) ? 2 : 1;
/*    */     
/* 36 */     DripstoneUtils.growPointedDripstone(worldGenLevel, pos, (Direction)tipDirection.get(), height, false);
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   private static Optional<Direction> getTipDirection(LevelAccessor level, BlockPos pos, RandomSource random) {
/* 41 */     boolean canPlaceAbove = DripstoneUtils.isDripstoneBase(level.getBlockState(pos.above()));
/* 42 */     boolean canPlaceBelow = DripstoneUtils.isDripstoneBase(level.getBlockState(pos.below()));
/*    */     
/* 44 */     if (canPlaceAbove && canPlaceBelow) {
/* 45 */       return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
/*    */     }
/* 47 */     if (canPlaceAbove) {
/* 48 */       return Optional.of(Direction.DOWN);
/*    */     }
/* 50 */     if (canPlaceBelow) {
/* 51 */       return Optional.of(Direction.UP);
/*    */     }
/* 53 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   private static void createPatchOfDripstoneBlocks(LevelAccessor level, RandomSource random, BlockPos pos, PointedDripstoneConfiguration config) {
/* 57 */     DripstoneUtils.placeDripstoneBlockIfPossible(level, pos);
/*    */     
/* 59 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 60 */       if (random.nextFloat() > config.chanceOfDirectionalSpread) {
/*    */         continue;
/*    */       }
/*    */       
/* 64 */       BlockPos pos1 = pos.relative(direction);
/* 65 */       DripstoneUtils.placeDripstoneBlockIfPossible(level, pos1);
/* 66 */       if (random.nextFloat() > config.chanceOfSpreadRadius2) {
/*    */         continue;
/*    */       }
/* 69 */       BlockPos pos2 = pos1.relative(Direction.getRandom(random));
/* 70 */       DripstoneUtils.placeDripstoneBlockIfPossible(level, pos2);
/* 71 */       if (random.nextFloat() > config.chanceOfSpreadRadius3) {
/*    */         continue;
/*    */       }
/* 74 */       BlockPos pos3 = pos2.relative(Direction.getRandom(random));
/* 75 */       DripstoneUtils.placeDripstoneBlockIfPossible(level, pos3);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\PointedDripstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */