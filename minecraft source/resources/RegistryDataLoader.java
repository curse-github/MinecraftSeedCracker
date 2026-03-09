/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySynchronization;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.TestEnvironmentDefinition;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RegistryDataLoader
/*     */ {
/*  87 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  89 */   private static final Comparator<ResourceKey<?>> ERROR_KEY_COMPARATOR = Comparator.comparing(ResourceKey::registry).thenComparing(ResourceKey::identifier);
/*     */   
/*  91 */   private static final RegistrationInfo NETWORK_REGISTRATION_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.experimental());
/*  92 */   private static final Function<Optional<KnownPack>, RegistrationInfo> REGISTRATION_INFO_CACHE = Util.memoize(knownPack -> {
/*  93 */         Lifecycle lifecycle = (Lifecycle)knownPack.map(KnownPack::isVanilla).map(()).orElse(Lifecycle.experimental());
/*  94 */         return new RegistrationInfo(knownPack, lifecycle);
/*     */       });
/*     */   public static final class RegistryData<T> extends Record { private final ResourceKey<? extends Registry<T>> key; private final Codec<T> elementCodec; private final boolean requiredNonEmpty;
/*  97 */     public RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, boolean requiredNonEmpty) { this.key = key; this.elementCodec = elementCodec; this.requiredNonEmpty = requiredNonEmpty; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  97 */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public Codec<T> elementCodec() { return this.elementCodec; } public boolean requiredNonEmpty() { return this.requiredNonEmpty; }
/*     */ 
/*     */     
/* 100 */     private RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) { this(key, elementCodec, false); }
/*     */ 
/*     */     
/*     */     private RegistryDataLoader.Loader<T> create(Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 104 */       MappedRegistry mappedRegistry = new MappedRegistry(this.key, lifecycle);
/* 105 */       return new RegistryDataLoader.Loader(this, mappedRegistry, loadingErrors);
/*     */     }
/*     */ 
/*     */     
/* 109 */     public void runWithArguments(BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) { output.accept(this.key, this.elementCodec); } }
/*     */ 
/*     */   
/*     */   private static final class Loader<T>
/*     */     extends Record {
/*     */     private final RegistryDataLoader.RegistryData<T> data;
/*     */     private final WritableRegistry<T> registry;
/*     */     private final Map<ResourceKey<?>, Exception> loadingErrors;
/*     */     
/* 118 */     private Loader(RegistryDataLoader.RegistryData<T> data, WritableRegistry<T> registry, Map<ResourceKey<?>, Exception> loadingErrors) { this.data = data; this.registry = registry; this.loadingErrors = loadingErrors; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$Loader;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$Loader;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$Loader;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 118 */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; } public RegistryDataLoader.RegistryData<T> data() { return this.data; } public WritableRegistry<T> registry() { return this.registry; } public Map<ResourceKey<?>, Exception> loadingErrors() { return this.loadingErrors; }
/*     */ 
/*     */     
/* 121 */     public void loadFromResources(ResourceManager resourceManager, RegistryOps.RegistryInfoLookup context) { RegistryDataLoader.loadContentsFromManager(resourceManager, context, this.registry, this.data.elementCodec, this.loadingErrors); }
/*     */ 
/*     */ 
/*     */     
/* 125 */     public void loadFromNetwork(Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entries, ResourceProvider knownDataSource, RegistryOps.RegistryInfoLookup context) { RegistryDataLoader.loadContentsFromNetwork(entries, knownDataSource, context, this.registry, this.data.elementCodec, this.loadingErrors); }
/*     */   }
/*     */ 
/*     */   
/* 129 */   public static final List<RegistryData<?>> WORLDGEN_REGISTRIES = List.of(new RegistryData[] { new RegistryData(Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC), new RegistryData(Registries.BIOME, Biome.DIRECT_CODEC), new RegistryData(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_CARVER, ConfiguredWorldCarver.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC), new RegistryData(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC), new RegistryData(Registries.STRUCTURE, Structure.DIRECT_CODEC), new RegistryData(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC), new RegistryData(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC), new RegistryData(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC), new RegistryData(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC), new RegistryData(Registries.NOISE, NormalNoise.NoiseParameters.DIRECT_CODEC), new RegistryData(Registries.DENSITY_FUNCTION, DensityFunction.DIRECT_CODEC), new RegistryData(Registries.WORLD_PRESET, WorldPreset.DIRECT_CODEC), new RegistryData(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPreset.DIRECT_CODEC), new RegistryData(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfig.DIRECT_CODEC), new RegistryData(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC, true), new RegistryData(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.DIRECT_CODEC, true), new RegistryData(Registries.PIG_VARIANT, PigVariant.DIRECT_CODEC, true), new RegistryData(Registries.FROG_VARIANT, FrogVariant.DIRECT_CODEC, true), new RegistryData(Registries.CAT_VARIANT, CatVariant.DIRECT_CODEC, true), new RegistryData(Registries.COW_VARIANT, CowVariant.DIRECT_CODEC, true), new RegistryData(Registries.CHICKEN_VARIANT, ChickenVariant.DIRECT_CODEC, true), new RegistryData(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.DIRECT_CODEC, true), new RegistryData(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, true), new RegistryData(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), new RegistryData(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC), new RegistryData(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT_PROVIDER, EnchantmentProvider.DIRECT_CODEC), new RegistryData(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), new RegistryData(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData(Registries.TIMELINE, Timeline.DIRECT_CODEC) });
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
/* 170 */   public static final List<RegistryData<?>> DIMENSION_REGISTRIES = List.of(new RegistryData(Registries.LEVEL_STEM, LevelStem.CODEC));
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final List<RegistryData<?>> SYNCHRONIZED_REGISTRIES = List.of(new RegistryData[] { new RegistryData(Registries.BIOME, Biome.NETWORK_CODEC), new RegistryData(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData(Registries.WOLF_VARIANT, WolfVariant.NETWORK_CODEC, true), new RegistryData(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.NETWORK_CODEC, true), new RegistryData(Registries.PIG_VARIANT, PigVariant.NETWORK_CODEC, true), new RegistryData(Registries.FROG_VARIANT, FrogVariant.NETWORK_CODEC, true), new RegistryData(Registries.CAT_VARIANT, CatVariant.NETWORK_CODEC, true), new RegistryData(Registries.COW_VARIANT, CowVariant.NETWORK_CODEC, true), new RegistryData(Registries.CHICKEN_VARIANT, ChickenVariant.NETWORK_CODEC, true), new RegistryData(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.NETWORK_CODEC, true), new RegistryData(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, true), new RegistryData(Registries.DIMENSION_TYPE, DimensionType.NETWORK_CODEC), new RegistryData(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), new RegistryData(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), new RegistryData(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData(Registries.TIMELINE, Timeline.NETWORK_CODEC) });
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
/* 201 */   public static RegistryAccess.Frozen load(ResourceManager resourceManager, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad) { return load((loader, context) -> loader.loadFromResources(resourceManager, context), contextRegistries, registriesToLoad); }
/*     */   public static final class NetworkedRegistryData extends Record { private final List<RegistrySynchronization.PackedRegistryEntry> elements; private final TagNetworkSerialization.NetworkPayload tags;
/*     */     
/* 204 */     public NetworkedRegistryData(List<RegistrySynchronization.PackedRegistryEntry> elements, TagNetworkSerialization.NetworkPayload tags) { this.elements = elements; this.tags = tags; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #204	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/* 204 */       //   0	8	1	o	Ljava/lang/Object; } public List<RegistrySynchronization.PackedRegistryEntry> elements() { return this.elements; } public TagNetworkSerialization.NetworkPayload tags() { return this.tags; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public static RegistryAccess.Frozen load(Map<ResourceKey<? extends Registry<?>>, NetworkedRegistryData> entries, ResourceProvider knownDataSource, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad) { return load((loader, context) -> loader.loadFromNetwork(entries, knownDataSource, context), contextRegistries, registriesToLoad); }
/*     */ 
/*     */   
/*     */   private static RegistryAccess.Frozen load(LoadingFunction loadingFunction, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad) {
/* 215 */     Map<ResourceKey<?>, Exception> loadingErrors = new HashMap<ResourceKey<?>, Exception>();
/*     */     
/* 217 */     List<Loader<?>> newRegistriesAndLoaders = (List)registriesToLoad.stream().map(r -> r.create(Lifecycle.stable(), loadingErrors)).collect(Collectors.toUnmodifiableList());
/* 218 */     RegistryOps.RegistryInfoLookup contextAndNewRegistries = createContext(contextRegistries, newRegistriesAndLoaders);
/*     */     
/* 220 */     newRegistriesAndLoaders.forEach(loader -> loadingFunction.apply(loader, contextAndNewRegistries));
/*     */     
/* 222 */     newRegistriesAndLoaders.forEach(p -> {
/* 223 */           WritableRegistry writableRegistry = p.registry();
/*     */           try {
/* 225 */             writableRegistry.freeze();
/* 226 */           } catch (Exception e) {
/* 227 */             loadingErrors.put(writableRegistry.key(), e);
/*     */           } 
/*     */           
/* 230 */           if (p.data.requiredNonEmpty && writableRegistry.size() == 0) {
/* 231 */             loadingErrors.put(writableRegistry.key(), new IllegalStateException("Registry must be non-empty: " + String.valueOf(writableRegistry.key().identifier())));
/*     */           }
/*     */         });
/*     */     
/* 235 */     if (!loadingErrors.isEmpty()) {
/* 236 */       throw logErrors(loadingErrors);
/*     */     }
/*     */     
/* 239 */     return (new RegistryAccess.ImmutableRegistryAccess(newRegistriesAndLoaders.stream().map(Loader::registry).toList())).freeze();
/*     */   }
/*     */   
/*     */   private static RegistryOps.RegistryInfoLookup createContext(List<HolderLookup.RegistryLookup<?>> contextRegistries, List<Loader<?>> newRegistriesAndLoaders) {
/* 243 */     final Map<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> result = new HashMap<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>>();
/*     */     
/* 245 */     contextRegistries.forEach(e -> result.put(e.key(), createInfoForContextRegistry(e)));
/* 246 */     newRegistriesAndLoaders.forEach(e -> result.put(e.registry.key(), createInfoForNewRegistry(e.registry)));
/*     */ 
/*     */     
/* 249 */     return new RegistryOps.RegistryInfoLookup()
/*     */       {
/*     */         public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key)
/*     */         {
/* 253 */           return Optional.ofNullable((RegistryOps.RegistryInfo)result.get(key));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 259 */   private static <T> RegistryOps.RegistryInfo<T> createInfoForNewRegistry(WritableRegistry<T> e) { return new RegistryOps.RegistryInfo(e, e.createRegistrationLookup(), e.registryLifecycle()); }
/*     */ 
/*     */ 
/*     */   
/* 263 */   private static <T> RegistryOps.RegistryInfo<T> createInfoForContextRegistry(HolderLookup.RegistryLookup<T> lookup) { return new RegistryOps.RegistryInfo(lookup, lookup, lookup.registryLifecycle()); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ReportedException logErrors(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 268 */     printFullDetailsToLog(loadingErrors);
/* 269 */     return createReportWithBriefInfo(loadingErrors);
/*     */   }
/*     */   
/*     */   private static void printFullDetailsToLog(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 273 */     StringWriter collectedErrors = new StringWriter();
/* 274 */     PrintWriter errorPrinter = new PrintWriter(collectedErrors);
/* 275 */     Map<Identifier, Map<Identifier, Exception>> errorsByRegistry = (Map)loadingErrors.entrySet().stream().collect(Collectors.groupingBy(e -> ((ResourceKey)e.getKey()).registry(), Collectors.toMap(e -> ((ResourceKey)e.getKey()).identifier(), Map.Entry::getValue)));
/* 276 */     errorsByRegistry.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(registryEntry -> {
/* 277 */           errorPrinter.printf(Locale.ROOT, "> Errors in registry %s:%n", new Object[] { registryEntry.getKey() });
/* 278 */           ((Map)registryEntry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(());
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 283 */     errorPrinter.flush();
/* 284 */     LOGGER.error("Registry loading errors:\n{}", collectedErrors);
/*     */   }
/*     */   
/*     */   private static ReportedException createReportWithBriefInfo(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 288 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Failed to load registries due to errors"), "Registry Loading");
/* 289 */     CrashReportCategory errors = report.addCategory("Loading info");
/* 290 */     errors.setDetail("Errors", () -> {
/* 291 */           StringBuilder briefDetails = new StringBuilder();
/* 292 */           loadingErrors.entrySet().stream().sorted(Map.Entry.comparingByKey(ERROR_KEY_COMPARATOR)).forEach(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 301 */           return briefDetails.toString();
/*     */         });
/*     */     
/* 304 */     return new ReportedException(report);
/*     */   }
/*     */   
/*     */   private static <E> void loadElementFromResource(WritableRegistry<E> output, Decoder<E> elementDecoder, RegistryOps<JsonElement> ops, ResourceKey<E> elementKey, Resource thunk, RegistrationInfo registrationInfo) throws IOException {
/* 308 */     Reader reader = thunk.openAsReader(); 
/* 309 */     try { JsonElement json = StrictJsonParser.parse(reader);
/* 310 */       DataResult<E> parseResult = elementDecoder.parse(ops, json);
/* 311 */       E parsedElement = (E)parseResult.getOrThrow();
/* 312 */       output.register(elementKey, parsedElement, registrationInfo);
/* 313 */       if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null)
/*     */         try { reader.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 317 */      } private static <E> void loadContentsFromManager(ResourceManager resourceManager, RegistryOps.RegistryInfoLookup lookup, WritableRegistry<E> registry, Decoder<E> elementDecoder, Map<ResourceKey<?>, Exception> loadingErrors) { FileToIdConverter lister = FileToIdConverter.registry(registry.key());
/*     */     
/* 319 */     RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, lookup);
/* 320 */     for (Map.Entry<Identifier, Resource> resourceEntry : lister.listMatchingResources(resourceManager).entrySet()) {
/* 321 */       Identifier resourceId = (Identifier)resourceEntry.getKey();
/* 322 */       ResourceKey<E> elementKey = ResourceKey.create(registry.key(), lister.fileToId(resourceId));
/*     */       
/* 324 */       Resource thunk = (Resource)resourceEntry.getValue();
/* 325 */       RegistrationInfo registrationInfo = (RegistrationInfo)REGISTRATION_INFO_CACHE.apply(thunk.knownPackInfo());
/*     */       try {
/* 327 */         loadElementFromResource(registry, elementDecoder, ops, elementKey, thunk, registrationInfo);
/* 328 */       } catch (Exception e) {
/* 329 */         loadingErrors.put(elementKey, new IllegalStateException(String.format(Locale.ROOT, "Failed to parse %s from pack %s", new Object[] { resourceId, thunk.sourcePackId() }), e));
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     TagLoader.loadTagsForRegistry(resourceManager, registry); }
/*     */ 
/*     */   
/*     */   private static <E> void loadContentsFromNetwork(Map<ResourceKey<? extends Registry<?>>, NetworkedRegistryData> entries, ResourceProvider knownDataSource, RegistryOps.RegistryInfoLookup lookup, WritableRegistry<E> registry, Decoder<E> elementDecoder, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 337 */     NetworkedRegistryData registryEntries = (NetworkedRegistryData)entries.get(registry.key());
/* 338 */     if (registryEntries == null) {
/*     */       return;
/*     */     }
/*     */     
/* 342 */     RegistryOps<Tag> nbtOps = RegistryOps.create(NbtOps.INSTANCE, lookup);
/* 343 */     RegistryOps<JsonElement> jsonOps = RegistryOps.create(JsonOps.INSTANCE, lookup);
/* 344 */     FileToIdConverter knownDataPathConverter = FileToIdConverter.registry(registry.key());
/*     */ 
/*     */     
/* 347 */     for (RegistrySynchronization.PackedRegistryEntry entry : registryEntries.elements) {
/* 348 */       ResourceKey<E> elementKey = ResourceKey.create(registry.key(), entry.id());
/* 349 */       Optional<Tag> data = entry.data();
/* 350 */       if (data.isPresent())
/*     */         try {
/* 352 */           DataResult<E> parseResult = elementDecoder.parse(nbtOps, (Tag)data.get());
/* 353 */           E parsedElement = (E)parseResult.getOrThrow();
/* 354 */           registry.register(elementKey, parsedElement, NETWORK_REGISTRATION_INFO); continue;
/* 355 */         } catch (Exception e) {
/* 356 */           loadingErrors.put(elementKey, new IllegalStateException(String.format(Locale.ROOT, "Failed to parse value %s from server", new Object[] { data.get() }), e));
/*     */           continue;
/*     */         }  
/* 359 */       Identifier knownDataPath = knownDataPathConverter.idToFile(entry.id());
/*     */       try {
/* 361 */         Resource thunk = knownDataSource.getResourceOrThrow(knownDataPath);
/* 362 */         loadElementFromResource(registry, elementDecoder, jsonOps, elementKey, thunk, NETWORK_REGISTRATION_INFO);
/* 363 */       } catch (Exception e) {
/* 364 */         loadingErrors.put(elementKey, new IllegalStateException("Failed to parse local data", e));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 369 */     TagLoader.loadTagsFromNetwork(registryEntries.tags, registry);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface LoadingFunction {
/*     */     void apply(RegistryDataLoader.Loader<?> param1Loader, RegistryOps.RegistryInfoLookup param1RegistryInfoLookup);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryDataLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */