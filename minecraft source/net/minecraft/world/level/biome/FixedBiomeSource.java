/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class FixedBiomeSource
/*    */   extends BiomeSource implements BiomeManager.NoiseBiomeSource {
/* 18 */   public static final MapCodec<FixedBiomeSource> CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, s -> s.biome).stable();
/*    */   
/*    */   private final Holder<Biome> biome;
/*    */ 
/*    */   
/* 23 */   public FixedBiomeSource(Holder<Biome> biome) { this.biome = biome; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected Stream<Holder<Biome>> collectPossibleBiomes() { return Stream.of(this.biome); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected MapCodec<? extends BiomeSource> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) { return this.biome; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) { return this.biome; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int originX, int originY, int originZ, int r, int skipStep, Predicate<Holder<Biome>> allowed, RandomSource random, boolean findClosest, Climate.Sampler sampler) {
/* 48 */     if (allowed.test(this.biome)) {
/* 49 */       if (findClosest) {
/* 50 */         return Pair.of(new BlockPos(originX, originY, originZ), this.biome);
/*    */       }
/* 52 */       return Pair.of(new BlockPos(originX - r + random.nextInt(r * 2 + 1), originY, originZ - r + random.nextInt(r * 2 + 1)), this.biome);
/*    */     } 
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos origin, int searchRadius, int sampleResolutionHorizontal, int sampleResolutionVertical, Predicate<Holder<Biome>> allowed, Climate.Sampler sampler, LevelReader level) { return allowed.test(this.biome) ? Pair.of(origin.atY(Mth.clamp(origin.getY(), level.getMinY() + 1, level.getMaxY() + 1)), this.biome) : null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Set<Holder<Biome>> getBiomesWithin(int x, int y, int z, int r, Climate.Sampler sampler) { return Sets.newHashSet(Set.of(this.biome)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\FixedBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */