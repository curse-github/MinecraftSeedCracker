/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ 
/*     */ public final class WorldDimensions extends Record {
/*     */   private final Map<ResourceKey<LevelStem>, LevelStem> dimensions;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldDimensions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions; }
/*     */   
/*  36 */   public Map<ResourceKey<LevelStem>, LevelStem> dimensions() { return this.dimensions; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldDimensions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldDimensions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  40 */   public static final MapCodec<WorldDimensions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  41 */         Codec.unboundedMap(ResourceKey.codec(Registries.LEVEL_STEM), LevelStem.CODEC)
/*  42 */         .fieldOf("dimensions").forGetter(WorldDimensions::dimensions))
/*  43 */       .apply(i, i.stable(WorldDimensions::new)));
/*     */ 
/*     */   
/*  46 */   private static final Set<ResourceKey<LevelStem>> BUILTIN_ORDER = ImmutableSet.of(LevelStem.OVERWORLD, LevelStem.NETHER, LevelStem.END);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final int VANILLA_DIMENSION_COUNT = BUILTIN_ORDER.size();
/*     */   
/*     */   public WorldDimensions(Map<ResourceKey<LevelStem>, LevelStem> dimensions) {
/*  55 */     LevelStem overworld = (LevelStem)dimensions.get(LevelStem.OVERWORLD);
/*  56 */     if (overworld == null)
/*  57 */       throw new IllegalStateException("Overworld settings missing"); 
/*     */     this.dimensions = dimensions;
/*     */   }
/*     */   
/*     */   public WorldDimensions(Registry<LevelStem> registry) {
/*  62 */     this((Map)registry
/*  63 */         .listElements().collect(Collectors.toMap(Holder.Reference::key, Holder.Reference::value)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Stream<ResourceKey<LevelStem>> keysInOrder(Stream<ResourceKey<LevelStem>> knownKeys) {
/*  68 */     return Stream.concat(BUILTIN_ORDER
/*  69 */         .stream(), knownKeys
/*  70 */         .filter(k -> !BUILTIN_ORDER.contains(k)));
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldDimensions replaceOverworldGenerator(HolderLookup.Provider registries, ChunkGenerator generator) {
/*  75 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.DIMENSION_TYPE);
/*  76 */     Map<ResourceKey<LevelStem>, LevelStem> newDimensions = withOverworld(registryLookup, this.dimensions, generator);
/*  77 */     return new WorldDimensions(newDimensions);
/*     */   }
/*     */   
/*     */   public static Map<ResourceKey<LevelStem>, LevelStem> withOverworld(HolderLookup<DimensionType> dimensionTypes, Map<ResourceKey<LevelStem>, LevelStem> dimensions, ChunkGenerator generator) {
/*  81 */     LevelStem stem = (LevelStem)dimensions.get(LevelStem.OVERWORLD);
/*  82 */     Holder.Reference reference = (stem == null) ? dimensionTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD) : stem.type();
/*     */     
/*  84 */     return withOverworld(dimensions, reference, generator);
/*     */   }
/*     */   
/*     */   public static Map<ResourceKey<LevelStem>, LevelStem> withOverworld(Map<ResourceKey<LevelStem>, LevelStem> dimensions, Holder<DimensionType> type, ChunkGenerator generator) {
/*  88 */     ImmutableMap.Builder<ResourceKey<LevelStem>, LevelStem> builder = ImmutableMap.builder();
/*  89 */     builder.putAll(dimensions);
/*  90 */     builder.put(LevelStem.OVERWORLD, new LevelStem(type, generator));
/*  91 */     return builder.buildKeepingLast();
/*     */   }
/*     */   
/*     */   public ChunkGenerator overworld() {
/*  95 */     LevelStem stem = (LevelStem)this.dimensions.get(LevelStem.OVERWORLD);
/*  96 */     if (stem == null) {
/*  97 */       throw new IllegalStateException("Overworld settings missing");
/*     */     }
/*  99 */     return stem.generator();
/*     */   }
/*     */ 
/*     */   
/* 103 */   public Optional<LevelStem> get(ResourceKey<LevelStem> key) { return Optional.ofNullable((LevelStem)this.dimensions.get(key)); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public ImmutableSet<ResourceKey<Level>> levels() { return (ImmutableSet)dimensions().keySet().stream().map(Registries::levelStemToLevel).collect(ImmutableSet.toImmutableSet()); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isDebug() { return overworld() instanceof DebugLevelSource; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static PrimaryLevelData.SpecialWorldProperty specialWorldProperty(Registry<LevelStem> registry) { return (PrimaryLevelData.SpecialWorldProperty)registry.getOptional(LevelStem.OVERWORLD).map(overworld -> {
/* 116 */           ChunkGenerator generator = overworld.generator();
/* 117 */           if (generator instanceof DebugLevelSource) {
/* 118 */             return PrimaryLevelData.SpecialWorldProperty.DEBUG;
/*     */           }
/* 120 */           if (generator instanceof FlatLevelSource) {
/* 121 */             return PrimaryLevelData.SpecialWorldProperty.FLAT;
/*     */           }
/* 123 */           return PrimaryLevelData.SpecialWorldProperty.NONE;
/* 124 */         }).orElse(PrimaryLevelData.SpecialWorldProperty.NONE); }
/*     */ 
/*     */ 
/*     */   
/* 128 */   private static Lifecycle checkStability(ResourceKey<LevelStem> key, LevelStem dimension) { return isVanillaLike(key, dimension) ? Lifecycle.stable() : Lifecycle.experimental(); }
/*     */ 
/*     */   
/*     */   private static boolean isVanillaLike(ResourceKey<LevelStem> key, LevelStem dimension) {
/* 132 */     if (key == LevelStem.OVERWORLD) {
/* 133 */       return isStableOverworld(dimension);
/*     */     }
/* 135 */     if (key == LevelStem.NETHER) {
/* 136 */       return isStableNether(dimension);
/*     */     }
/* 138 */     if (key == LevelStem.END) {
/* 139 */       return isStableEnd(dimension);
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isStableOverworld(LevelStem dimension) {
/* 145 */     Holder<DimensionType> dimensionType = dimension.type();
/* 146 */     if (!dimensionType.is(BuiltinDimensionTypes.OVERWORLD) && !dimensionType.is(BuiltinDimensionTypes.OVERWORLD_CAVES)) {
/* 147 */       return false;
/*     */     }
/* 149 */     BiomeSource biomeSource1 = dimension.generator().getBiomeSource(); if (biomeSource1 instanceof MultiNoiseBiomeSource) { MultiNoiseBiomeSource biomeSource = (MultiNoiseBiomeSource)biomeSource1;
/* 150 */       if (!biomeSource.stable(MultiNoiseBiomeSourceParameterLists.OVERWORLD)) {
/* 151 */         return false;
/*     */       } }
/*     */     
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isStableNether(LevelStem dimension) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual type : ()Lnet/minecraft/core/Holder;
/*     */     //   4: getstatic net/minecraft/world/level/dimension/BuiltinDimensionTypes.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   7: invokeinterface is : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   12: ifeq -> 73
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual generator : ()Lnet/minecraft/world/level/chunk/ChunkGenerator;
/*     */     //   19: astore_3
/*     */     //   20: aload_3
/*     */     //   21: instanceof net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   24: ifeq -> 73
/*     */     //   27: aload_3
/*     */     //   28: checkcast net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   31: astore_2
/*     */     //   32: aload_2
/*     */     //   33: getstatic net/minecraft/world/level/levelgen/NoiseGeneratorSettings.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   36: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   39: ifeq -> 73
/*     */     //   42: aload_2
/*     */     //   43: invokevirtual getBiomeSource : ()Lnet/minecraft/world/level/biome/BiomeSource;
/*     */     //   46: astore_3
/*     */     //   47: aload_3
/*     */     //   48: instanceof net/minecraft/world/level/biome/MultiNoiseBiomeSource
/*     */     //   51: ifeq -> 73
/*     */     //   54: aload_3
/*     */     //   55: checkcast net/minecraft/world/level/biome/MultiNoiseBiomeSource
/*     */     //   58: astore_1
/*     */     //   59: aload_1
/*     */     //   60: getstatic net/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterLists.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   63: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   66: ifeq -> 73
/*     */     //   69: iconst_1
/*     */     //   70: goto -> 74
/*     */     //   73: iconst_0
/*     */     //   74: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     //   #158	-> 1
/*     */     //   #159	-> 15
/*     */     //   #160	-> 36
/*     */     //   #161	-> 42
/*     */     //   #162	-> 63
/*     */     //   #158	-> 74
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   59	14	1	biomeSource	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSource;
/*     */     //   32	41	2	generator	Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;
/*     */     //   0	75	0	dimension	Lnet/minecraft/world/level/dimension/LevelStem; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isStableEnd(LevelStem dimension) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual type : ()Lnet/minecraft/core/Holder;
/*     */     //   4: getstatic net/minecraft/world/level/dimension/BuiltinDimensionTypes.END : Lnet/minecraft/resources/ResourceKey;
/*     */     //   7: invokeinterface is : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   12: ifeq -> 56
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual generator : ()Lnet/minecraft/world/level/chunk/ChunkGenerator;
/*     */     //   19: astore_2
/*     */     //   20: aload_2
/*     */     //   21: instanceof net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   24: ifeq -> 56
/*     */     //   27: aload_2
/*     */     //   28: checkcast net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   31: astore_1
/*     */     //   32: aload_1
/*     */     //   33: getstatic net/minecraft/world/level/levelgen/NoiseGeneratorSettings.END : Lnet/minecraft/resources/ResourceKey;
/*     */     //   36: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   39: ifeq -> 56
/*     */     //   42: aload_1
/*     */     //   43: invokevirtual getBiomeSource : ()Lnet/minecraft/world/level/biome/BiomeSource;
/*     */     //   46: instanceof net/minecraft/world/level/biome/TheEndBiomeSource
/*     */     //   49: ifeq -> 56
/*     */     //   52: iconst_1
/*     */     //   53: goto -> 57
/*     */     //   56: iconst_0
/*     */     //   57: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     //   #166	-> 1
/*     */     //   #167	-> 15
/*     */     //   #168	-> 36
/*     */     //   #169	-> 43
/*     */     //   #166	-> 57
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   32	24	1	generator	Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;
/*     */     //   0	58	0	dimension	Lnet/minecraft/world/level/dimension/LevelStem; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Complete bake(Registry<LevelStem> baseDimensions) {
/* 179 */     Stream<ResourceKey<LevelStem>> knownDimensions = Stream.concat(baseDimensions.registryKeySet().stream(), this.dimensions.keySet().stream()).distinct();
/*     */     
/* 181 */     List<Entry> results = new ArrayList<Entry>();
/* 182 */     keysInOrder(knownDimensions).forEach(key -> 
/* 183 */         baseDimensions.getOptional(key)
/* 184 */         .or(())
/* 185 */         .ifPresent(()));
/*     */ 
/*     */     
/* 188 */     Lifecycle initialStability = (results.size() == VANILLA_DIMENSION_COUNT) ? Lifecycle.stable() : Lifecycle.experimental();
/* 189 */     MappedRegistry mappedRegistry = new MappedRegistry(Registries.LEVEL_STEM, initialStability);
/* 190 */     results.forEach(entry -> writableDimensions.register(entry.key, entry.value, entry.registrationInfo()));
/* 191 */     Registry<LevelStem> newDimensions = mappedRegistry.freeze();
/*     */     
/* 193 */     PrimaryLevelData.SpecialWorldProperty specialWorldProperty = specialWorldProperty(newDimensions);
/* 194 */     return new Complete(newDimensions.freeze(), specialWorldProperty);
/*     */   }
/*     */   public static final class Complete extends Record { private final Registry<LevelStem> dimensions; private final PrimaryLevelData.SpecialWorldProperty specialWorldProperty;
/* 197 */     public Complete(Registry<LevelStem> dimensions, PrimaryLevelData.SpecialWorldProperty specialWorldProperty) { this.dimensions = dimensions; this.specialWorldProperty = specialWorldProperty; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 197 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete; } public Registry<LevelStem> dimensions() { return this.dimensions; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/* 197 */       //   0	8	1	o	Ljava/lang/Object; } public PrimaryLevelData.SpecialWorldProperty specialWorldProperty() { return this.specialWorldProperty; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     public Lifecycle lifecycle() { return this.dimensions.registryLifecycle(); }
/*     */ 
/*     */ 
/*     */     
/* 206 */     public RegistryAccess.Frozen dimensionsRegistryAccess() { return (new RegistryAccess.ImmutableRegistryAccess(List.of(this.dimensions))).freeze(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\WorldDimensions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */