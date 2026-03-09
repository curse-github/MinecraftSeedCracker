/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeaturePlaceContext<FC extends FeatureConfiguration>
/*    */   extends Object
/*    */ {
/*    */   private final Optional<ConfiguredFeature<?, ?>> topFeature;
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   private final RandomSource random;
/*    */   private final BlockPos origin;
/*    */   private final FC config;
/*    */   
/*    */   public FeaturePlaceContext(Optional<ConfiguredFeature<?, ?>> topFeature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FC config) {
/* 23 */     this.topFeature = topFeature;
/* 24 */     this.level = level;
/* 25 */     this.chunkGenerator = chunkGenerator;
/* 26 */     this.random = random;
/* 27 */     this.origin = origin;
/* 28 */     this.config = config;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public Optional<ConfiguredFeature<?, ?>> topFeature() { return this.topFeature; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public WorldGenLevel level() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public ChunkGenerator chunkGenerator() { return this.chunkGenerator; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public RandomSource random() { return this.random; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public BlockPos origin() { return this.origin; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public FC config() { return (FC)this.config; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FeaturePlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */