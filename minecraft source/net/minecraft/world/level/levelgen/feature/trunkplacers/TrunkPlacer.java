/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public abstract class TrunkPlacer
/*    */ {
/* 23 */   public static final Codec<TrunkPlacer> CODEC = BuiltInRegistries.TRUNK_PLACER_TYPE.byNameCodec().dispatch(TrunkPlacer::type, TrunkPlacerType::codec);
/*    */   
/*    */   private static final int MAX_BASE_HEIGHT = 32;
/*    */   private static final int MAX_RAND = 24;
/*    */   public static final int MAX_HEIGHT = 80;
/*    */   
/*    */   protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(RecordCodecBuilder.Instance<P> instance) {
/* 30 */     return instance.group(
/* 31 */         Codec.intRange(0, 32).fieldOf("base_height").forGetter(p -> Integer.valueOf(p.baseHeight)), 
/* 32 */         Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(p -> Integer.valueOf(p.heightRandA)), 
/* 33 */         Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(p -> Integer.valueOf(p.heightRandB)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final int baseHeight;
/*    */   protected final int heightRandA;
/*    */   protected final int heightRandB;
/*    */   
/*    */   public TrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
/* 42 */     this.baseHeight = baseHeight;
/* 43 */     this.heightRandA = heightRandA;
/* 44 */     this.heightRandB = heightRandB;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getTreeHeight(RandomSource random) { return this.baseHeight + random.nextInt(this.heightRandA + 1) + random.nextInt(this.heightRandB + 1); }
/*    */ 
/*    */   
/*    */   private static boolean isDirt(LevelSimulatedReader level, BlockPos pos) {
/* 56 */     return level.isStateAtPosition(pos, state -> 
/* 57 */         (Feature.isDirt(state) && 
/* 58 */         !state.is(Blocks.GRASS_BLOCK) && 
/* 59 */         !state.is(Blocks.MYCELIUM)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static void setDirtAt(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 64 */     if (config.forceDirt || !isDirt(level, pos))
/*    */     {
/* 66 */       trunkSetter.accept(pos, config.dirtProvider.getState(random, pos));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 71 */   protected boolean placeLog(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config) { return placeLog(level, trunkSetter, random, pos, config, Function.identity()); }
/*    */ 
/*    */   
/*    */   protected boolean placeLog(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config, Function<BlockState, BlockState> stateModifier) {
/* 75 */     if (validTreePos(level, pos)) {
/* 76 */       trunkSetter.accept(pos, (BlockState)stateModifier.apply(config.trunkProvider.getState(random, pos)));
/*    */       
/* 78 */       return true;
/*    */     } 
/* 80 */     return false;
/*    */   }
/*    */   
/*    */   protected void placeLogIfFree(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos.MutableBlockPos pos, TreeConfiguration config) {
/* 84 */     if (isFree(level, pos)) {
/* 85 */       placeLog(level, trunkSetter, random, pos, config);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 90 */   protected boolean validTreePos(LevelSimulatedReader level, BlockPos pos) { return TreeFeature.validTreePos(level, pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 95 */   public boolean isFree(LevelSimulatedReader level, BlockPos pos) { return (validTreePos(level, pos) || level.isStateAtPosition(pos, state -> state.is(BlockTags.LOGS))); }
/*    */   
/*    */   protected abstract TrunkPlacerType<?> type();
/*    */   
/*    */   public abstract List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */