/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class WorldGenSettingsFix extends DataFix {
/*     */   private static final String VILLAGE = "minecraft:village";
/*     */   private static final String DESERT_PYRAMID = "minecraft:desert_pyramid";
/*     */   private static final String IGLOO = "minecraft:igloo";
/*     */   private static final String JUNGLE_TEMPLE = "minecraft:jungle_pyramid";
/*     */   private static final String SWAMP_HUT = "minecraft:swamp_hut";
/*     */   private static final String PILLAGER_OUTPOST = "minecraft:pillager_outpost";
/*     */   private static final String END_CITY = "minecraft:endcity";
/*     */   private static final String WOODLAND_MANSION = "minecraft:mansion";
/*     */   private static final String OCEAN_MONUMENT = "minecraft:monument";
/*     */   
/*  28 */   public WorldGenSettingsFix(Schema parent) { super(parent, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   protected TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("WorldGenSettings building", getInputSchema().getType(References.WORLD_GEN_SETTINGS), settings -> settings.update(DSL.remainderFinder(), WorldGenSettingsFix::fix)); }
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> noise(long seed, DynamicLike<T> input, Dynamic<T> noiseGeneratorSettings, Dynamic<T> biomeSource) {
/*  37 */     return input.createMap(ImmutableMap.of(input
/*  38 */           .createString("type"), input.createString("minecraft:noise"), input
/*  39 */           .createString("biome_source"), biomeSource, input
/*  40 */           .createString("seed"), input.createLong(seed), input
/*  41 */           .createString("settings"), noiseGeneratorSettings));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> input, long seed, boolean legacyBiomeInitLayer, boolean largeBiomes) {
/*  49 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder().put(input.createString("type"), input.createString("minecraft:vanilla_layered")).put(input.createString("seed"), input.createLong(seed)).put(input.createString("large_biomes"), input.createBoolean(largeBiomes));
/*     */     
/*  51 */     if (legacyBiomeInitLayer) {
/*  52 */       builder.put(input.createString("legacy_biome_init_layer"), input.createBoolean(legacyBiomeInitLayer));
/*     */     }
/*     */     
/*  55 */     return input.createMap(builder.build());
/*     */   }
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
/*  68 */   private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/*  69 */     .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
/*  70 */     .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
/*  71 */     .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
/*  72 */     .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
/*  73 */     .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
/*  74 */     .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
/*  75 */     .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
/*  76 */     .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
/*  77 */     .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
/*  78 */     .build();
/*     */   
/*     */   private static final class StructureFeatureConfiguration {
/*  81 */     public static final Codec<StructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/*  82 */           .fieldOf("spacing").forGetter(()), Codec.INT
/*  83 */           .fieldOf("separation").forGetter(()), Codec.INT
/*  84 */           .fieldOf("salt").forGetter(()))
/*  85 */         .apply(i, StructureFeatureConfiguration::new));
/*     */     
/*     */     private final int spacing;
/*     */     private final int separation;
/*     */     private final int salt;
/*     */     
/*     */     public StructureFeatureConfiguration(int spacing, int separation, int salt) {
/*  92 */       this.spacing = spacing;
/*  93 */       this.separation = separation;
/*  94 */       this.salt = salt;
/*     */     }
/*     */ 
/*     */     
/*  98 */     public <T> Dynamic<T> serialize(DynamicOps<T> ops) { return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap())); }
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fix(Dynamic<T> input) {
/*     */     Dynamic<T> generator;
/* 103 */     DynamicOps<T> ops = input.getOps();
/*     */     
/* 105 */     long seed = input.get("RandomSeed").asLong(0L);
/*     */     
/* 107 */     Optional<String> name = input.get("generatorName").asString().map(n -> n.toLowerCase(Locale.ROOT)).result();
/*     */     
/* 109 */     Optional<String> legacyCustomOptions = (Optional)input.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> {
/* 110 */           if (name.equals(Optional.of("customized"))) {
/* 111 */             return input.get("generatorOptions").asString().result();
/*     */           }
/* 113 */           return Optional.empty();
/*     */         });
/*     */     
/* 116 */     boolean caves = false;
/* 117 */     if (name.equals(Optional.of("customized")))
/* 118 */     { generator = defaultOverworld(input, seed); }
/* 119 */     else if (name.isEmpty())
/* 120 */     { generator = defaultOverworld(input, seed); }
/*     */     else
/* 122 */     { Dynamic<T> fixedSource, biomeSource, noiseGeneratorSettings; Optional<String> type; OptionalDynamic<?> chunkGeneratorObject, settings; Map<Dynamic<T>, Dynamic<T>> structureBuilder; ImmutableMap.Builder<T, T> builder; OptionalDynamic<T> flatSettings; boolean generateBonusChest; byte b; boolean generateMapFeatures; String str; Dynamic<T> generator, generator, generator; switch ((String)name.get())
/*     */       { case "flat":
/* 124 */           flatSettings = input.get("generatorOptions");
/* 125 */           structureBuilder = fixFlatStructures(ops, flatSettings);
/*     */           
/* 127 */           generator = input.createMap(ImmutableMap.of(input
/* 128 */                 .createString("type"), input.createString("minecraft:flat"), input
/* 129 */                 .createString("settings"), input.createMap(ImmutableMap.of(input
/* 130 */                     .createString("structures"), input.createMap(structureBuilder), input
/* 131 */                     .createString("layers"), (Dynamic)flatSettings.get("layers").result().orElseGet(() -> input.createList(Stream.of(new Dynamic[] { input
/* 132 */                             .createMap(ImmutableMap.of(input
/* 133 */                                 .createString("height"), input.createInt(1), input
/* 134 */                                 .createString("block"), input.createString("minecraft:bedrock"))), input
/*     */                             
/* 136 */                             .createMap(ImmutableMap.of(input
/* 137 */                                 .createString("height"), input.createInt(2), input
/* 138 */                                 .createString("block"), input.createString("minecraft:dirt"))), input
/*     */                             
/* 140 */                             .createMap(ImmutableMap.of(input
/* 141 */                                 .createString("height"), input.createInt(1), input
/* 142 */                                 .createString("block"), input.createString("minecraft:grass_block")))
/*     */ 
/*     */                           
/* 145 */                           }))), input.createString("biome"), input.createString(flatSettings.get("biome").asString("minecraft:plains"))))));
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
/* 201 */           generateMapFeatures = input.get("MapFeatures").asBoolean(true);
/* 202 */           generateBonusChest = input.get("BonusChest").asBoolean(false);
/*     */           
/* 204 */           builder = ImmutableMap.builder();
/* 205 */           builder.put(ops.createString("seed"), ops.createLong(seed));
/* 206 */           builder.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures));
/* 207 */           builder.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest));
/* 208 */           builder.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/* 209 */           legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/*     */           
/* 211 */           return new Dynamic(ops, ops.createMap(builder.build()));case "debug_all_block_states": generator = input.createMap(ImmutableMap.of(input.createString("type"), input.createString("minecraft:debug"))); generateMapFeatures = input.get("MapFeatures").asBoolean(true); generateBonusChest = input.get("BonusChest").asBoolean(false); builder = ImmutableMap.builder(); builder.put(ops.createString("seed"), ops.createLong(seed)); builder.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures)); builder.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest)); builder.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves)); legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o))); return new Dynamic(ops, ops.createMap(builder.build()));case "buffet": settings = input.get("generatorOptions"); chunkGeneratorObject = settings.get("chunk_generator"); type = chunkGeneratorObject.get("type").asString().result(); if (Objects.equals(type, Optional.of("minecraft:caves"))) { noiseGeneratorSettings = input.createString("minecraft:caves"); caves = true; } else if (Objects.equals(type, Optional.of("minecraft:floating_islands"))) { noiseGeneratorSettings = input.createString("minecraft:floating_islands"); } else { noiseGeneratorSettings = input.createString("minecraft:overworld"); }  biomeSource = (Dynamic)settings.get("biome_source").result().orElseGet(() -> input.createMap(ImmutableMap.of(input.createString("type"), input.createString("minecraft:fixed")))); if (biomeSource.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) { String biome = (String)biomeSource.get("options").get("biomes").asStream().findFirst().flatMap(b -> b.asString().result()).orElse("minecraft:ocean"); fixedSource = biomeSource.remove("options").set("biome", input.createString(biome)); } else { fixedSource = biomeSource; }  generator = noise(seed, input, noiseGeneratorSettings, fixedSource); generateMapFeatures = input.get("MapFeatures").asBoolean(true); generateBonusChest = input.get("BonusChest").asBoolean(false); builder = ImmutableMap.builder(); builder.put(ops.createString("seed"), ops.createLong(seed)); builder.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures)); builder.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest)); builder.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves)); legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o))); return new Dynamic(ops, ops.createMap(builder.build())); }  boolean normal = ((String)name.get()).equals("default"); boolean legacyBiomeInitLayer = (((String)name.get()).equals("default_1_1") || (normal && input.get("generatorVersion").asInt(0) == 0)); boolean isAmplified = ((String)name.get()).equals("amplified"); boolean largeBiomes = ((String)name.get()).equals("largebiomes"); generator = noise(seed, input, input.createString(isAmplified ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(input, seed, legacyBiomeInitLayer, largeBiomes)); }  boolean bool1 = input.get("MapFeatures").asBoolean(true); boolean bool2 = input.get("BonusChest").asBoolean(false); ImmutableMap.Builder builder1 = ImmutableMap.builder(); builder1.put(ops.createString("seed"), ops.createLong(seed)); builder1.put(ops.createString("generate_features"), ops.createBoolean(bool1)); builder1.put(ops.createString("bonus_chest"), ops.createBoolean(bool2)); builder1.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves)); legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o))); return new Dynamic(ops, ops.createMap(builder1.build()));
/*     */   }
/*     */ 
/*     */   
/* 215 */   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> input, long seed) { return noise(seed, input, input.createString("minecraft:overworld"), vanillaBiomeSource(input, seed, false, false)); }
/*     */ 
/*     */   
/*     */   protected static <T> T vanillaLevels(Dynamic<T> input, long seed, Dynamic<T> overworldGenerator, boolean caves) {
/* 219 */     DynamicOps<T> ops = input.getOps();
/* 220 */     return (T)ops.createMap(ImmutableMap.of(ops
/* 221 */           .createString("minecraft:overworld"), ops.createMap(ImmutableMap.of(ops
/* 222 */               .createString("type"), ops.createString("minecraft:overworld" + (caves ? "_caves" : "")), ops
/* 223 */               .createString("generator"), overworldGenerator.getValue())), ops
/*     */           
/* 225 */           .createString("minecraft:the_nether"), ops.createMap(ImmutableMap.of(ops
/* 226 */               .createString("type"), ops.createString("minecraft:the_nether"), ops
/* 227 */               .createString("generator"), noise(seed, input, input.createString("minecraft:nether"), input.createMap(ImmutableMap.of(input
/* 228 */                     .createString("type"), input.createString("minecraft:multi_noise"), input
/* 229 */                     .createString("seed"), input.createLong(seed), input
/* 230 */                     .createString("preset"), input.createString("minecraft:nether"))))
/* 231 */               .getValue())), ops
/*     */           
/* 233 */           .createString("minecraft:the_end"), ops.createMap(ImmutableMap.of(ops
/* 234 */               .createString("type"), ops.createString("minecraft:the_end"), ops
/* 235 */               .createString("generator"), noise(seed, input, input.createString("minecraft:end"), input.createMap(ImmutableMap.of(input
/* 236 */                     .createString("type"), input.createString("minecraft:the_end"), input
/* 237 */                     .createString("seed"), input.createLong(seed))))
/* 238 */               .getValue()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> ops, OptionalDynamic<T> settings) {
/* 244 */     MutableInt strongholdDistance = new MutableInt(32);
/* 245 */     MutableInt strongholdSpread = new MutableInt(3);
/* 246 */     MutableInt strongholdCount = new MutableInt(128);
/* 247 */     MutableBoolean hasStronghold = new MutableBoolean(false);
/* 248 */     Map<String, StructureFeatureConfiguration> structureConfig = Maps.newHashMap();
/*     */     
/* 250 */     if (settings.result().isEmpty()) {
/* 251 */       hasStronghold.setTrue();
/* 252 */       structureConfig.put("minecraft:village", (StructureFeatureConfiguration)DEFAULTS.get("minecraft:village"));
/*     */     } 
/*     */     
/* 255 */     settings.get("structures").flatMap(Dynamic::getMapValues).ifSuccess(map -> map.forEach(()));
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
/*     */ 
/*     */     
/* 318 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> structureBuilder = ImmutableMap.builder();
/* 319 */     structureBuilder.put(settings.createString("structures"), settings.createMap((Map)structureConfig.entrySet().stream().collect(Collectors.toMap(e -> 
/* 320 */               settings.createString((String)e.getKey()), e -> (
/* 321 */               (StructureFeatureConfiguration)e.getValue()).serialize(ops)))));
/*     */     
/* 323 */     if (hasStronghold.isTrue()) {
/* 324 */       structureBuilder.put(settings.createString("stronghold"), settings.createMap(ImmutableMap.of(settings
/* 325 */               .createString("distance"), settings.createInt(strongholdDistance.intValue()), settings
/* 326 */               .createString("spread"), settings.createInt(strongholdSpread.intValue()), settings
/* 327 */               .createString("count"), settings.createInt(strongholdCount.intValue()))));
/*     */     }
/*     */     
/* 330 */     return structureBuilder.build();
/*     */   }
/*     */ 
/*     */   
/* 334 */   private static int getInt(String input, int def) { return NumberUtils.toInt(input, def); }
/*     */ 
/*     */ 
/*     */   
/* 338 */   private static int getInt(String input, int def, int min) { return Math.max(min, getInt(input, def)); }
/*     */ 
/*     */   
/*     */   private static void setSpacing(Map<String, StructureFeatureConfiguration> structureConfig, String structure, String optionValue, int min) {
/* 342 */     StructureFeatureConfiguration config = (StructureFeatureConfiguration)structureConfig.getOrDefault(structure, (StructureFeatureConfiguration)DEFAULTS.get(structure));
/* 343 */     int spacing = getInt(optionValue, config.spacing, min);
/* 344 */     structureConfig.put(structure, new StructureFeatureConfiguration(spacing, config.separation, config.salt));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */