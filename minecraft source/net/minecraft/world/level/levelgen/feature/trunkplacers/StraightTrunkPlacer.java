/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class StraightTrunkPlacer extends TrunkPlacer {
/* 17 */   public static final MapCodec<StraightTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, StraightTrunkPlacer::new));
/*    */ 
/*    */   
/* 20 */   public StraightTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) { super(baseHeight, heightRandA, heightRandB); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.STRAIGHT_TRUNK_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 30 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*    */     
/* 32 */     for (int y = 0; y < treeHeight; y++) {
/* 33 */       placeLog(level, trunkSetter, random, origin.above(y), config);
/*    */     }
/* 35 */     return ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(treeHeight), 0, false));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\StraightTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */