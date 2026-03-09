/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FlatLevelGeneratorSettings {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  38 */   public static final Codec<FlatLevelGeneratorSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  39 */         RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).lenientOptionalFieldOf("structure_overrides").forGetter(()), FlatLayerInfo.CODEC
/*  40 */         .listOf().fieldOf("layers").forGetter(FlatLevelGeneratorSettings::getLayersInfo), Codec.BOOL
/*  41 */         .fieldOf("lakes").orElse(Boolean.valueOf(false)).forGetter(()), Codec.BOOL
/*  42 */         .fieldOf("features").orElse(Boolean.valueOf(false)).forGetter(()), Biome.CODEC
/*  43 */         .lenientOptionalFieldOf("biome").orElseGet(Optional::empty).forGetter(()), 
/*  44 */         RegistryOps.retrieveElement(Biomes.PLAINS), 
/*  45 */         RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), 
/*  46 */         RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_SURFACE))
/*  47 */       .apply(i, FlatLevelGeneratorSettings::new)).comapFlatMap(FlatLevelGeneratorSettings::validateHeight, Function.identity()).stable(); private final Optional<HolderSet<StructureSet>> structureOverrides;
/*     */   
/*     */   private static DataResult<FlatLevelGeneratorSettings> validateHeight(FlatLevelGeneratorSettings settings) {
/*  50 */     int totalHeight = settings.layersInfo.stream().mapToInt(FlatLayerInfo::getHeight).sum();
/*     */     
/*  52 */     if (totalHeight > DimensionType.Y_SIZE) {
/*  53 */       return DataResult.error(() -> "Sum of layer heights is > " + DimensionType.Y_SIZE, settings);
/*     */     }
/*  55 */     return DataResult.success(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   private final List<FlatLayerInfo> layersInfo;
/*     */   private final Holder<Biome> biome;
/*     */   private final List<BlockState> layers;
/*     */   private boolean voidGen;
/*     */   private boolean decoration;
/*     */   private boolean addLakes;
/*     */   private final List<Holder<PlacedFeature>> lakes;
/*     */   
/*     */   private FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> structureOverrides, List<FlatLayerInfo> layers, boolean lakes, boolean features, Optional<Holder<Biome>> biome, Holder.Reference<Biome> fallbackBiome, Holder<PlacedFeature> lavaUnderground, Holder<PlacedFeature> lavaSurface) {
/*  68 */     this(structureOverrides, getBiome(biome, fallbackBiome), List.of(lavaUnderground, lavaSurface));
/*  69 */     if (lakes) {
/*  70 */       setAddLakes();
/*     */     }
/*  72 */     if (features) {
/*  73 */       setDecoration();
/*     */     }
/*  75 */     this.layersInfo.addAll(layers);
/*  76 */     updateLayers();
/*     */   }
/*     */   
/*     */   private static Holder<Biome> getBiome(Optional<? extends Holder<Biome>> biome, Holder<Biome> fallbackBiome) {
/*  80 */     if (biome.isEmpty()) {
/*  81 */       LOGGER.error("Unknown biome, defaulting to plains");
/*  82 */       return fallbackBiome;
/*     */     } 
/*  84 */     return (Holder)biome.get();
/*     */   }
/*     */   public FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> structureOverrides, Holder<Biome> biome, List<Holder<PlacedFeature>> lakes) {
/*     */     this.layersInfo = Lists.newArrayList();
/*  88 */     this.structureOverrides = structureOverrides;
/*  89 */     this.biome = biome;
/*  90 */     this.layers = Lists.newArrayList();
/*  91 */     this.lakes = lakes;
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings withBiomeAndLayers(List<FlatLayerInfo> layers, Optional<HolderSet<StructureSet>> structureOverrides, Holder<Biome> biome) {
/*  95 */     FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(structureOverrides, biome, this.lakes);
/*  96 */     for (FlatLayerInfo layerInfo : layers) {
/*  97 */       settings.layersInfo.add(new FlatLayerInfo(layerInfo.getHeight(), layerInfo.getBlockState().getBlock()));
/*  98 */       settings.updateLayers();
/*     */     } 
/* 100 */     if (this.decoration) {
/* 101 */       settings.setDecoration();
/*     */     }
/* 103 */     if (this.addLakes) {
/* 104 */       settings.setAddLakes();
/*     */     }
/* 106 */     return settings;
/*     */   }
/*     */ 
/*     */   
/* 110 */   public void setDecoration() { this.decoration = true; }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public void setAddLakes() { this.addLakes = true; }
/*     */ 
/*     */   
/*     */   public BiomeGenerationSettings adjustGenerationSettings(Holder<Biome> sourceBiome) {
/* 118 */     if (!sourceBiome.equals(this.biome)) {
/* 119 */       return ((Biome)sourceBiome.value()).getGenerationSettings();
/*     */     }
/* 121 */     BiomeGenerationSettings biomeGenerationSettings = ((Biome)getBiome().value()).getGenerationSettings();
/*     */     
/* 123 */     BiomeGenerationSettings.PlainBuilder newGenerationSettings = new BiomeGenerationSettings.PlainBuilder();
/*     */     
/* 125 */     if (this.addLakes) {
/* 126 */       for (Holder<PlacedFeature> lake : this.lakes) {
/* 127 */         newGenerationSettings.addFeature(GenerationStep.Decoration.LAKES, lake);
/*     */       }
/*     */     }
/*     */     
/* 131 */     boolean biomeDecoration = ((!this.voidGen || sourceBiome.is(Biomes.THE_VOID)) && this.decoration);
/*     */     
/* 133 */     if (biomeDecoration) {
/* 134 */       List<HolderSet<PlacedFeature>> features = biomeGenerationSettings.features();
/* 135 */       for (int stepIndex = 0; stepIndex < features.size(); stepIndex++) {
/* 136 */         if (stepIndex != GenerationStep.Decoration.UNDERGROUND_STRUCTURES.ordinal() && stepIndex != GenerationStep.Decoration.SURFACE_STRUCTURES
/* 137 */           .ordinal() && (!this.addLakes || stepIndex != GenerationStep.Decoration.LAKES
/* 138 */           .ordinal())) {
/*     */ 
/*     */ 
/*     */           
/* 142 */           HolderSet<PlacedFeature> featureList = (HolderSet)features.get(stepIndex);
/* 143 */           for (Holder<PlacedFeature> feature : featureList) {
/* 144 */             newGenerationSettings.addFeature(stepIndex, feature);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 149 */     List<BlockState> layers = getLayers();
/* 150 */     for (int i = 0; i < layers.size(); i++) {
/* 151 */       BlockState layer = (BlockState)layers.get(i);
/*     */ 
/*     */       
/* 154 */       if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(layer)) {
/* 155 */         layers.set(i, null);
/* 156 */         newGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(Feature.FILL_LAYER, new LayerConfiguration(i, layer), new net.minecraft.world.level.levelgen.placement.PlacementModifier[0]));
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     return newGenerationSettings.build();
/*     */   }
/*     */ 
/*     */   
/* 164 */   public Optional<HolderSet<StructureSet>> structureOverrides() { return this.structureOverrides; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public Holder<Biome> getBiome() { return this.biome; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public List<FlatLayerInfo> getLayersInfo() { return this.layersInfo; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public List<BlockState> getLayers() { return this.layers; }
/*     */ 
/*     */   
/*     */   public void updateLayers() {
/* 180 */     this.layers.clear();
/*     */     
/* 182 */     for (FlatLayerInfo layer : this.layersInfo) {
/* 183 */       for (int y = 0; y < layer.getHeight(); y++) {
/* 184 */         this.layers.add(layer.getBlockState());
/*     */       }
/*     */     } 
/*     */     
/* 188 */     this.voidGen = this.layers.stream().allMatch(s -> s.is(Blocks.AIR));
/*     */   }
/*     */   
/*     */   public static FlatLevelGeneratorSettings getDefault(HolderGetter<Biome> biomes, HolderGetter<StructureSet> structureSets, HolderGetter<PlacedFeature> placedFeatures) {
/* 192 */     HolderSet.Direct direct = HolderSet.direct(new Holder[] { structureSets
/* 193 */           .getOrThrow(BuiltinStructureSets.STRONGHOLDS), structureSets
/* 194 */           .getOrThrow(BuiltinStructureSets.VILLAGES) });
/*     */ 
/*     */     
/* 197 */     FlatLevelGeneratorSettings result = new FlatLevelGeneratorSettings(Optional.of(direct), getDefaultBiome(biomes), createLakesList(placedFeatures));
/* 198 */     result.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
/* 199 */     result.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
/* 200 */     result.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
/* 201 */     result.updateLayers();
/*     */     
/* 203 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 207 */   public static Holder<Biome> getDefaultBiome(HolderGetter<Biome> biomes) { return biomes.getOrThrow(Biomes.PLAINS); }
/*     */ 
/*     */   
/*     */   public static List<Holder<PlacedFeature>> createLakesList(HolderGetter<PlacedFeature> placedFeatures) {
/* 211 */     return List.of(placedFeatures
/* 212 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), placedFeatures
/* 213 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */