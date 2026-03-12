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
/*    */ public class GiantTrunkPlacer extends TrunkPlacer {
/* 17 */   public static final MapCodec<GiantTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, GiantTrunkPlacer::new));
/*    */ 
/*    */   
/* 20 */   public GiantTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) { super(baseHeight, heightRandA, heightRandB); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.GIANT_TRUNK_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 30 */     BlockPos below = origin.below();
/* 31 */     setDirtAt(level, trunkSetter, random, below, config);
/* 32 */     setDirtAt(level, trunkSetter, random, below.east(), config);
/* 33 */     setDirtAt(level, trunkSetter, random, below.south(), config);
/* 34 */     setDirtAt(level, trunkSetter, random, below.south().east(), config);
/*    */     
/* 36 */     BlockPos.MutableBlockPos trunkPos = new BlockPos.MutableBlockPos();
/*    */     
/* 38 */     for (int hh = 0; hh < treeHeight; hh++) {
/* 39 */       placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 0, hh, 0);
/*    */       
/* 41 */       if (hh < treeHeight - 1) {
/* 42 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 1, hh, 0);
/* 43 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 1, hh, 1);
/* 44 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 0, hh, 1);
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     return ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(treeHeight), 0, true));
/*    */   }
/*    */   
/*    */   private void placeLogIfFreeWithOffset(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos.MutableBlockPos trunkPos, TreeConfiguration config, BlockPos treePos, int x, int y, int z) {
/* 52 */     trunkPos.setWithOffset(treePos, x, y, z);
/* 53 */     placeLogIfFree(level, trunkSetter, random, trunkPos, config);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\GiantTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */