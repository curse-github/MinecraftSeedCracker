/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class BendingTrunkPlacer extends TrunkPlacer {
/* 21 */   public static final MapCodec<BendingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i
/* 22 */         .group(ExtraCodecs.POSITIVE_INT
/* 23 */           .optionalFieldOf("min_height_for_leaves", Integer.valueOf(1)).forGetter(()), 
/* 24 */           IntProvider.codec(1, 64).fieldOf("bend_length").forGetter(())))
/*    */       
/* 26 */       .apply(i, BendingTrunkPlacer::new));
/*    */   
/*    */   private final int minHeightForLeaves;
/*    */   private final IntProvider bendLength;
/*    */   
/*    */   public BendingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves, IntProvider bendLength) {
/* 32 */     super(baseHeight, heightRandA, heightRandB);
/*    */     
/* 34 */     this.minHeightForLeaves = minHeightForLeaves;
/* 35 */     this.bendLength = bendLength;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.BENDING_TRUNK_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 45 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 46 */     int logHeight = treeHeight - 1;
/* 47 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 48 */     BlockPos belowPos = pos.below();
/*    */     
/* 50 */     setDirtAt(level, trunkSetter, random, belowPos, config);
/* 51 */     List<FoliagePlacer.FoliageAttachment> foliagePoints = Lists.newArrayList();
/*    */     
/* 53 */     for (int i = 0; i <= logHeight; i++) {
/*    */       
/* 55 */       if (i + 1 >= logHeight + random.nextInt(2)) {
/* 56 */         pos.move(direction);
/*    */       }
/*    */       
/* 59 */       if (TreeFeature.validTreePos(level, pos)) {
/* 60 */         placeLog(level, trunkSetter, random, pos, config);
/*    */       }
/*    */       
/* 63 */       if (i >= this.minHeightForLeaves) {
/* 64 */         foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
/*    */       }
/*    */       
/* 67 */       pos.move(Direction.UP);
/*    */     } 
/*    */ 
/*    */     
/* 71 */     int dirLength = this.bendLength.sample(random);
/* 72 */     for (int i = 0; i <= dirLength; i++) {
/* 73 */       if (TreeFeature.validTreePos(level, pos)) {
/* 74 */         placeLog(level, trunkSetter, random, pos, config);
/*    */       }
/*    */       
/* 77 */       foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
/* 78 */       pos.move(direction);
/*    */     } 
/*    */     
/* 81 */     return foliagePoints;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\BendingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */