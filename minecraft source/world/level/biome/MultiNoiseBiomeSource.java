/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ public class MultiNoiseBiomeSource extends BiomeSource {
/*  20 */   private static final MapCodec<Holder<Biome>> ENTRY_CODEC = Biome.CODEC.fieldOf("biome");
/*     */ 
/*     */   
/*  23 */   public static final MapCodec<Climate.ParameterList<Holder<Biome>>> DIRECT_CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes");
/*     */   
/*  25 */   private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
/*     */   
/*  27 */   public static final MapCodec<MultiNoiseBiomeSource> CODEC = Codec.mapEither(DIRECT_CODEC, PRESET_CODEC)
/*     */ 
/*     */     
/*  30 */     .xmap(MultiNoiseBiomeSource::new, o -> 
/*     */       
/*  32 */       o.parameters);
/*     */ 
/*     */   
/*     */   private final Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters;
/*     */ 
/*     */   
/*  38 */   private MultiNoiseBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters) { this.parameters = parameters; }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static MultiNoiseBiomeSource createFromList(Climate.ParameterList<Holder<Biome>> parameters) { return new MultiNoiseBiomeSource(Either.left(parameters)); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static MultiNoiseBiomeSource createFromPreset(Holder<MultiNoiseBiomeSourceParameterList> preset) { return new MultiNoiseBiomeSource(Either.right(preset)); }
/*     */ 
/*     */   
/*     */   private Climate.ParameterList<Holder<Biome>> parameters() {
/*  50 */     return (Climate.ParameterList)this.parameters.map(direct -> 
/*  51 */         direct, preset -> (
/*  52 */         (MultiNoiseBiomeSourceParameterList)preset.value()).parameters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected Stream<Holder<Biome>> collectPossibleBiomes() { return parameters().values().stream().map(Pair::getSecond); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected MapCodec<? extends BiomeSource> codec() { return CODEC; }
/*     */ 
/*     */   
/*     */   public boolean stable(ResourceKey<MultiNoiseBiomeSourceParameterList> expected) {
/*  67 */     Optional<Holder<MultiNoiseBiomeSourceParameterList>> preset = this.parameters.right();
/*  68 */     return (preset.isPresent() && ((Holder)preset.get()).is(expected));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) { return getNoiseBiome(sampler.sample(quartX, quartY, quartZ)); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*  78 */   public Holder<Biome> getNoiseBiome(Climate.TargetPoint target) { return (Holder)parameters().findValue(target); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDebugInfo(List<String> result, BlockPos feetPos, Climate.Sampler sampler) {
/*  83 */     int quartX = QuartPos.fromBlock(feetPos.getX());
/*  84 */     int quartY = QuartPos.fromBlock(feetPos.getY());
/*  85 */     int quartZ = QuartPos.fromBlock(feetPos.getZ());
/*  86 */     Climate.TargetPoint sampleQuantized = sampler.sample(quartX, quartY, quartZ);
/*     */     
/*  88 */     float continentalness = Climate.unquantizeCoord(sampleQuantized.continentalness());
/*  89 */     float erosion = Climate.unquantizeCoord(sampleQuantized.erosion());
/*  90 */     float temperature = Climate.unquantizeCoord(sampleQuantized.temperature());
/*  91 */     float humidity = Climate.unquantizeCoord(sampleQuantized.humidity());
/*  92 */     float weirdness = Climate.unquantizeCoord(sampleQuantized.weirdness());
/*     */     
/*  94 */     double peaksAndValleys = NoiseRouterData.peaksAndValleys(weirdness);
/*     */     
/*  96 */     OverworldBiomeBuilder biomeBuilder = new OverworldBiomeBuilder();
/*  97 */     result.add("Biome builder PV: " + 
/*  98 */         OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(peaksAndValleys) + " C: " + biomeBuilder
/*  99 */         .getDebugStringForContinentalness(continentalness) + " E: " + biomeBuilder
/* 100 */         .getDebugStringForErosion(erosion) + " T: " + biomeBuilder
/* 101 */         .getDebugStringForTemperature(temperature) + " H: " + biomeBuilder
/* 102 */         .getDebugStringForHumidity(humidity));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */