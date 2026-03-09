/*     */ package net.minecraft.world.level.levelgen.presets;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.biome.TheEndBiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Bootstrap
/*     */ {
/*     */   private final BootstrapContext<WorldPreset> context;
/*     */   private final HolderGetter<NoiseGeneratorSettings> noiseSettings;
/*     */   private final HolderGetter<Biome> biomes;
/*     */   private final HolderGetter<PlacedFeature> placedFeatures;
/*     */   private final HolderGetter<StructureSet> structureSets;
/*     */   private final HolderGetter<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists;
/*     */   private final Holder<DimensionType> overworldDimensionType;
/*     */   private final LevelStem netherStem;
/*     */   private final LevelStem endStem;
/*     */   
/*     */   private Bootstrap(BootstrapContext<WorldPreset> context) {
/*  56 */     this.context = context;
/*     */     
/*  58 */     HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
/*     */     
/*  60 */     this.noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
/*  61 */     this.biomes = context.lookup(Registries.BIOME);
/*  62 */     this.placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  63 */     this.structureSets = context.lookup(Registries.STRUCTURE_SET);
/*  64 */     this.multiNoiseBiomeSourceParameterLists = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
/*     */     
/*  66 */     this.overworldDimensionType = dimensionTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
/*     */     
/*  68 */     Holder.Reference reference1 = dimensionTypes.getOrThrow(BuiltinDimensionTypes.NETHER);
/*  69 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);
/*     */     
/*  71 */     Holder.Reference<MultiNoiseBiomeSourceParameterList> netherBiomePreset = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
/*     */     
/*  73 */     this.netherStem = new LevelStem(reference1, new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(netherBiomePreset), reference2));
/*     */     
/*  75 */     Holder.Reference reference3 = dimensionTypes.getOrThrow(BuiltinDimensionTypes.END);
/*  76 */     Holder.Reference reference4 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.END);
/*     */     
/*  78 */     this.endStem = new LevelStem(reference3, new NoiseBasedChunkGenerator(TheEndBiomeSource.create(this.biomes), reference4));
/*     */   }
/*     */ 
/*     */   
/*  82 */   private LevelStem makeOverworld(ChunkGenerator generator) { return new LevelStem(this.overworldDimensionType, generator); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   private LevelStem makeNoiseBasedOverworld(BiomeSource overworldBiomeSource, Holder<NoiseGeneratorSettings> noiseSettings) { return makeOverworld(new NoiseBasedChunkGenerator(overworldBiomeSource, noiseSettings)); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   private WorldPreset createPresetWithCustomOverworld(LevelStem overworldStem) { return new WorldPreset(
/*  91 */         Map.of(LevelStem.OVERWORLD, overworldStem, LevelStem.NETHER, this.netherStem, LevelStem.END, this.endStem)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private void registerCustomOverworldPreset(ResourceKey<WorldPreset> debug, LevelStem overworld) { this.context.register(debug, createPresetWithCustomOverworld(overworld)); }
/*     */ 
/*     */   
/*     */   private void registerOverworlds(BiomeSource biomeSource) {
/* 104 */     Holder.Reference reference1 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 105 */     registerCustomOverworldPreset(WorldPresets.NORMAL, makeNoiseBasedOverworld(biomeSource, reference1));
/*     */     
/* 107 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.LARGE_BIOMES);
/* 108 */     registerCustomOverworldPreset(WorldPresets.LARGE_BIOMES, makeNoiseBasedOverworld(biomeSource, reference2));
/*     */     
/* 110 */     Holder.Reference reference3 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED);
/* 111 */     registerCustomOverworldPreset(WorldPresets.AMPLIFIED, makeNoiseBasedOverworld(biomeSource, reference3));
/*     */   }
/*     */   
/*     */   public void bootstrap() {
/* 115 */     Holder.Reference<MultiNoiseBiomeSourceParameterList> overworldPreset = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
/* 116 */     registerOverworlds(MultiNoiseBiomeSource.createFromPreset(overworldPreset));
/*     */     
/* 118 */     Holder.Reference reference = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 119 */     Holder.Reference<Biome> plains = this.biomes.getOrThrow(Biomes.PLAINS);
/* 120 */     registerCustomOverworldPreset(WorldPresets.SINGLE_BIOME_SURFACE, makeNoiseBasedOverworld(new FixedBiomeSource(plains), reference));
/*     */     
/* 122 */     registerCustomOverworldPreset(WorldPresets.FLAT, makeOverworld(new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(this.biomes, this.structureSets, this.placedFeatures))));
/*     */     
/* 124 */     registerCustomOverworldPreset(WorldPresets.DEBUG, makeOverworld(new DebugLevelSource(plains)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\presets\WorldPresets$Bootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */