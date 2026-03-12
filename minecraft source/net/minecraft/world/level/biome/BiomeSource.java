/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ public abstract class BiomeSource
/*     */   implements BiomeResolver
/*     */ {
/*  29 */   public static final Codec<BiomeSource> CODEC = BuiltInRegistries.BIOME_SOURCE.byNameCodec().dispatchStable(BiomeSource::codec, Function.identity());
/*     */ 
/*     */   
/*  32 */   private final Supplier<Set<Holder<Biome>>> possibleBiomes = Suppliers.memoize(() -> (Set)collectPossibleBiomes().distinct().collect(ImmutableSet.toImmutableSet()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public Set<Holder<Biome>> possibleBiomes() { return (Set)this.possibleBiomes.get(); }
/*     */ 
/*     */   
/*     */   public Set<Holder<Biome>> getBiomesWithin(int x, int y, int z, int r, Climate.Sampler sampler) {
/*  46 */     int x0 = QuartPos.fromBlock(x - r);
/*  47 */     int y0 = QuartPos.fromBlock(y - r);
/*  48 */     int z0 = QuartPos.fromBlock(z - r);
/*  49 */     int x1 = QuartPos.fromBlock(x + r);
/*  50 */     int y1 = QuartPos.fromBlock(y + r);
/*  51 */     int z1 = QuartPos.fromBlock(z + r);
/*     */     
/*  53 */     int w = x1 - x0 + 1;
/*  54 */     int d = y1 - y0 + 1;
/*  55 */     int h = z1 - z0 + 1;
/*     */     
/*  57 */     Set<Holder<Biome>> biomeSet = Sets.newHashSet();
/*     */     
/*  59 */     for (int row = 0; row < h; row++) {
/*  60 */       for (int column = 0; column < w; column++) {
/*  61 */         for (int depth = 0; depth < d; depth++) {
/*  62 */           int noiseX = x0 + column;
/*  63 */           int noiseY = y0 + depth;
/*  64 */           int noiseZ = z0 + row;
/*  65 */           biomeSet.add(getNoiseBiome(noiseX, noiseY, noiseZ, sampler));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  70 */     return biomeSet;
/*     */   }
/*     */ 
/*     */   
/*  74 */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int x, int y, int z, int searchRadius, Predicate<Holder<Biome>> allowed, RandomSource random, Climate.Sampler sampler) { return findBiomeHorizontal(x, y, z, searchRadius, 1, allowed, random, false, sampler); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos origin, int searchRadius, int sampleResolutionHorizontal, int sampleResolutionVertical, Predicate<Holder<Biome>> allowed, Climate.Sampler sampler, LevelReader level) {
/*  80 */     Set<Holder<Biome>> candidateBiomes = (Set)possibleBiomes().stream().filter(allowed).collect(Collectors.toUnmodifiableSet());
/*     */     
/*  82 */     if (candidateBiomes.isEmpty()) {
/*  83 */       return null;
/*     */     }
/*     */     
/*  86 */     int sampleRadius = Math.floorDiv(searchRadius, sampleResolutionHorizontal);
/*  87 */     int[] sampleYs = Mth.outFromOrigin(origin.getY(), level.getMinY() + 1, level.getMaxY() + 1, sampleResolutionVertical).toArray();
/*     */     
/*  89 */     for (BlockPos.MutableBlockPos sampleColumn : BlockPos.spiralAround(BlockPos.ZERO, sampleRadius, Direction.EAST, Direction.SOUTH)) {
/*  90 */       int blockX = origin.getX() + sampleColumn.getX() * sampleResolutionHorizontal;
/*  91 */       int blockZ = origin.getZ() + sampleColumn.getZ() * sampleResolutionHorizontal;
/*  92 */       int noiseX = QuartPos.fromBlock(blockX);
/*  93 */       int noiseZ = QuartPos.fromBlock(blockZ);
/*  94 */       for (int blockY : sampleYs) {
/*  95 */         int noiseY = QuartPos.fromBlock(blockY);
/*  96 */         Holder<Biome> biome = getNoiseBiome(noiseX, noiseY, noiseZ, sampler);
/*  97 */         if (candidateBiomes.contains(biome)) {
/*  98 */           return Pair.of(new BlockPos(blockX, blockY, blockZ), biome);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int originX, int originY, int originZ, int searchRadius, int skipSteps, Predicate<Holder<Biome>> allowed, RandomSource random, boolean findClosest, Climate.Sampler sampler) {
/* 114 */     int noiseCenterX = QuartPos.fromBlock(originX);
/* 115 */     int noiseCenterZ = QuartPos.fromBlock(originZ);
/* 116 */     int noiseRadius = QuartPos.fromBlock(searchRadius);
/*     */     
/* 118 */     int noiseY = QuartPos.fromBlock(originY);
/*     */     
/* 120 */     Pair<BlockPos, Holder<Biome>> result = null;
/* 121 */     int found = 0;
/*     */     
/* 123 */     int startRadius = findClosest ? 0 : noiseRadius; int currentRadius;
/* 124 */     for (currentRadius = startRadius; currentRadius <= noiseRadius; currentRadius += skipSteps) {
/* 125 */       int z; for (z = (SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD || SharedConstants.debugGenerateSquareTerrainWithoutNoise) ? 0 : -currentRadius; z <= currentRadius; z += skipSteps) {
/* 126 */         boolean zEdge = (Math.abs(z) == currentRadius); int x;
/* 127 */         for (x = -currentRadius; x <= currentRadius; x += skipSteps) {
/* 128 */           if (findClosest) {
/*     */             
/* 130 */             boolean xEdge = (Math.abs(x) == currentRadius);
/* 131 */             if (!xEdge && !zEdge) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */           
/* 136 */           int noiseX = noiseCenterX + x;
/* 137 */           int noiseZ = noiseCenterZ + z;
/* 138 */           Holder<Biome> biome = getNoiseBiome(noiseX, noiseY, noiseZ, sampler);
/* 139 */           if (allowed.test(biome)) {
/* 140 */             if (result == null || random.nextInt(found + 1) == 0) {
/* 141 */               BlockPos resultPos = new BlockPos(QuartPos.toBlock(noiseX), originY, QuartPos.toBlock(noiseZ));
/* 142 */               if (findClosest) {
/* 143 */                 return Pair.of(resultPos, biome);
/*     */               }
/* 145 */               result = Pair.of(resultPos, biome);
/*     */             } 
/* 147 */             found++;
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/* 153 */     return result;
/*     */   }
/*     */   
/*     */   public void addDebugInfo(List<String> result, BlockPos feetPos, Climate.Sampler sampler) {}
/*     */   
/*     */   protected abstract MapCodec<? extends BiomeSource> codec();
/*     */   
/*     */   protected abstract Stream<Holder<Biome>> collectPossibleBiomes();
/*     */   
/*     */   public abstract Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3, Climate.Sampler paramSampler);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */