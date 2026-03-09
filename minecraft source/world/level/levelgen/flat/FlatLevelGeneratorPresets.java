/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public class FlatLevelGeneratorPresets {
/*  24 */   public static final ResourceKey<FlatLevelGeneratorPreset> CLASSIC_FLAT = register("classic_flat");
/*  25 */   public static final ResourceKey<FlatLevelGeneratorPreset> TUNNELERS_DREAM = register("tunnelers_dream");
/*  26 */   public static final ResourceKey<FlatLevelGeneratorPreset> WATER_WORLD = register("water_world");
/*  27 */   public static final ResourceKey<FlatLevelGeneratorPreset> OVERWORLD = register("overworld");
/*  28 */   public static final ResourceKey<FlatLevelGeneratorPreset> SNOWY_KINGDOM = register("snowy_kingdom");
/*  29 */   public static final ResourceKey<FlatLevelGeneratorPreset> BOTTOMLESS_PIT = register("bottomless_pit");
/*  30 */   public static final ResourceKey<FlatLevelGeneratorPreset> DESERT = register("desert");
/*  31 */   public static final ResourceKey<FlatLevelGeneratorPreset> REDSTONE_READY = register("redstone_ready");
/*  32 */   public static final ResourceKey<FlatLevelGeneratorPreset> THE_VOID = register("the_void");
/*     */   
/*     */   private static class Bootstrap
/*     */   {
/*     */     private final BootstrapContext<FlatLevelGeneratorPreset> context;
/*     */     
/*  38 */     private Bootstrap(BootstrapContext<FlatLevelGeneratorPreset> context) { this.context = context; }
/*     */ 
/*     */     
/*     */     private void register(ResourceKey<FlatLevelGeneratorPreset> key, ItemLike icon, ResourceKey<Biome> biome, Set<ResourceKey<StructureSet>> structures, boolean decoration, boolean addLakes, FlatLayerInfo... layers) {
/*  42 */       HolderGetter<StructureSet> structureSets = this.context.lookup(Registries.STRUCTURE_SET);
/*  43 */       HolderGetter<PlacedFeature> placedFeatures = this.context.lookup(Registries.PLACED_FEATURE);
/*  44 */       HolderGetter<Biome> biomes = this.context.lookup(Registries.BIOME);
/*     */       
/*  46 */       Objects.requireNonNull(structureSets); HolderSet.Direct<StructureSet> structuresHolder = HolderSet.direct((List)structures.stream().map(structureSets::getOrThrow).collect(Collectors.toList()));
/*  47 */       FlatLevelGeneratorSettings generator = new FlatLevelGeneratorSettings(Optional.of(structuresHolder), biomes.getOrThrow(biome), FlatLevelGeneratorSettings.createLakesList(placedFeatures));
/*  48 */       if (decoration) {
/*  49 */         generator.setDecoration();
/*     */       }
/*     */       
/*  52 */       if (addLakes) {
/*  53 */         generator.setAddLakes();
/*     */       }
/*     */       
/*  56 */       for (int i = layers.length - 1; i >= 0; i--) {
/*  57 */         generator.getLayersInfo().add(layers[i]);
/*     */       }
/*     */       
/*  60 */       this.context.register(key, new FlatLevelGeneratorPreset(icon
/*  61 */             .asItem().builtInRegistryHolder(), generator));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  67 */       register(FlatLevelGeneratorPresets.CLASSIC_FLAT, Blocks.GRASS_BLOCK, Biomes.PLAINS, 
/*     */ 
/*     */           
/*  70 */           ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       register(FlatLevelGeneratorPresets.TUNNELERS_DREAM, Blocks.STONE, Biomes.WINDSWEPT_HILLS, 
/*     */ 
/*     */           
/*  83 */           ImmutableSet.of(BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/*  95 */       register(FlatLevelGeneratorPresets.WATER_WORLD, Items.WATER_BUCKET, Biomes.DEEP_OCEAN, 
/*     */ 
/*     */           
/*  98 */           ImmutableSet.of(BuiltinStructureSets.OCEAN_RUINS, BuiltinStructureSets.SHIPWRECKS, BuiltinStructureSets.OCEAN_MONUMENTS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.GRAVEL), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(64, Blocks.DEEPSLATE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 113 */       register(FlatLevelGeneratorPresets.OVERWORLD, Blocks.SHORT_GRASS, Biomes.PLAINS, 
/*     */ 
/*     */           
/* 116 */           ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.PILLAGER_OUTPOSTS, BuiltinStructureSets.RUINED_PORTALS, BuiltinStructureSets.STRONGHOLDS), true, true, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 131 */       register(FlatLevelGeneratorPresets.SNOWY_KINGDOM, Blocks.SNOW, Biomes.SNOWY_PLAINS, 
/*     */ 
/*     */           
/* 134 */           ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.IGLOOS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.SNOW), new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 147 */       register(FlatLevelGeneratorPresets.BOTTOMLESS_PIT, Items.FEATHER, Biomes.PLAINS, 
/*     */ 
/*     */           
/* 150 */           ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 160 */       register(FlatLevelGeneratorPresets.DESERT, Blocks.SAND, Biomes.DESERT, 
/*     */ 
/*     */           
/* 163 */           ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.DESERT_PYRAMIDS, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
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
/* 177 */       register(FlatLevelGeneratorPresets.REDSTONE_READY, Items.REDSTONE, Biomes.DESERT, 
/*     */ 
/*     */           
/* 180 */           ImmutableSet.of(), false, false, new FlatLayerInfo[] { new FlatLayerInfo(116, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       register(FlatLevelGeneratorPresets.THE_VOID, Blocks.BARRIER, Biomes.THE_VOID, 
/*     */ 
/*     */           
/* 191 */           ImmutableSet.of(), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.AIR) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public static void bootstrap(BootstrapContext<FlatLevelGeneratorPreset> context) { (new Bootstrap(context)).run(); }
/*     */ 
/*     */ 
/*     */   
/* 204 */   private static ResourceKey<FlatLevelGeneratorPreset> register(String name) { return ResourceKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(name)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPresets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */