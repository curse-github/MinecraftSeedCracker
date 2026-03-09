/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.loot.LootDataType;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ReloadableServerRegistries {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  40 */   private static final RegistrationInfo DEFAULT_REGISTRATION_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.experimental());
/*     */   public static final class LoadResult extends Record { private final LayeredRegistryAccess<RegistryLayer> layers; private final HolderLookup.Provider lookupWithUpdatedTags;
/*  42 */     public LoadResult(LayeredRegistryAccess<RegistryLayer> layers, HolderLookup.Provider lookupWithUpdatedTags) { this.layers = layers; this.lookupWithUpdatedTags = lookupWithUpdatedTags; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  42 */       //   0	7	0	this	Lnet/minecraft/server/ReloadableServerRegistries$LoadResult; } public LayeredRegistryAccess<RegistryLayer> layers() { return this.layers; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/ReloadableServerRegistries$LoadResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/ReloadableServerRegistries$LoadResult;
/*  42 */       //   0	8	1	o	Ljava/lang/Object; } public HolderLookup.Provider lookupWithUpdatedTags() { return this.lookupWithUpdatedTags; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<LoadResult> reload(LayeredRegistryAccess<RegistryLayer> context, List<Registry.PendingTags<?>> updatedContextTags, ResourceManager manager, Executor executor) {
/*  50 */     List<HolderLookup.RegistryLookup<?>> contextRegistriesWithTags = TagLoader.buildUpdatedLookups(context.getAccessForLoading(RegistryLayer.RELOADABLE), updatedContextTags);
/*  51 */     HolderLookup.Provider loadingContextWithTags = HolderLookup.Provider.create(contextRegistriesWithTags.stream());
/*  52 */     RegistryOps<JsonElement> ops = loadingContextWithTags.createSerializationContext(JsonOps.INSTANCE);
/*  53 */     List<CompletableFuture<WritableRegistry<?>>> registryLoads = LootDataType.values().map(type -> scheduleRegistryLoad(type, ops, manager, executor)).toList();
/*     */     
/*  55 */     CompletableFuture<List<WritableRegistry<?>>> sequence = Util.sequence(registryLoads);
/*  56 */     return sequence.thenApplyAsync(newlyLoadedRegistries -> createAndValidateFullContext(context, loadingContextWithTags, newlyLoadedRegistries), executor);
/*     */   }
/*     */ 
/*     */   
/*  60 */   private static <T> CompletableFuture<WritableRegistry<?>> scheduleRegistryLoad(LootDataType<T> type, RegistryOps<JsonElement> ops, ResourceManager manager, Executor taskExecutor) { return CompletableFuture.supplyAsync(() -> {
/*     */           
/*  62 */           MappedRegistry mappedRegistry = new MappedRegistry(type.registryKey(), Lifecycle.experimental());
/*  63 */           Map<Identifier, T> elements = new HashMap<Identifier, T>();
/*  64 */           SimpleJsonResourceReloadListener.scanDirectory(manager, type.registryKey(), ops, type.codec(), elements);
/*  65 */           elements.forEach(());
/*     */           
/*  67 */           TagLoader.loadTagsForRegistry(manager, mappedRegistry);
/*  68 */           return mappedRegistry;
/*     */         }taskExecutor); }
/*     */ 
/*     */   
/*     */   private static LoadResult createAndValidateFullContext(LayeredRegistryAccess<RegistryLayer> contextLayers, HolderLookup.Provider contextLookupWithUpdatedTags, List<WritableRegistry<?>> newRegistries) {
/*  73 */     LayeredRegistryAccess<RegistryLayer> fullLayers = createUpdatedRegistries(contextLayers, newRegistries);
/*     */ 
/*     */     
/*  76 */     HolderLookup.Provider fullLookupWithUpdatedTags = concatenateLookups(contextLookupWithUpdatedTags, fullLayers.getLayer(RegistryLayer.RELOADABLE));
/*  77 */     validateLootRegistries(fullLookupWithUpdatedTags);
/*  78 */     return new LoadResult(fullLayers, fullLookupWithUpdatedTags);
/*     */   }
/*     */   
/*     */   private static HolderLookup.Provider concatenateLookups(HolderLookup.Provider first, HolderLookup.Provider second) {
/*  82 */     return HolderLookup.Provider.create(
/*  83 */         Stream.concat(first
/*  84 */           .listRegistries(), second
/*  85 */           .listRegistries()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validateLootRegistries(HolderLookup.Provider fullContextWithNewTags) {
/*  91 */     ProblemReporter.Collector problems = new ProblemReporter.Collector();
/*  92 */     ValidationContext validationContext = new ValidationContext(problems, LootContextParamSets.ALL_PARAMS, fullContextWithNewTags);
/*  93 */     LootDataType.values().forEach(lootDataType -> validateRegistry(validationContext, lootDataType, fullContextWithNewTags));
/*  94 */     problems.forEach((id, problem) -> LOGGER.warn("Found loot table element validation problem in {}: {}", id, problem.description()));
/*     */   }
/*     */ 
/*     */   
/*  98 */   private static LayeredRegistryAccess<RegistryLayer> createUpdatedRegistries(LayeredRegistryAccess<RegistryLayer> context, List<WritableRegistry<?>> registries) { return context.replaceFrom(RegistryLayer.RELOADABLE, new RegistryAccess.Frozen[] { (new RegistryAccess.ImmutableRegistryAccess(registries)).freeze() }); }
/*     */ 
/*     */   
/*     */   private static <T> void validateRegistry(ValidationContext validationContext, LootDataType<T> type, HolderLookup.Provider registries) {
/* 102 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(type.registryKey());
/* 103 */     registryLookup.listElements().forEach(element -> type.runValidation(validationContext, element.key(), element.value()));
/*     */   }
/*     */   
/*     */   public static class Holder
/*     */   {
/*     */     private final HolderLookup.Provider registries;
/*     */     
/* 110 */     public Holder(HolderLookup.Provider registries) { this.registries = registries; }
/*     */ 
/*     */ 
/*     */     
/* 114 */     public HolderLookup.Provider lookup() { return this.registries; }
/*     */ 
/*     */ 
/*     */     
/* 118 */     public LootTable getLootTable(ResourceKey<LootTable> id) { return (LootTable)this.registries.lookup(Registries.LOOT_TABLE).flatMap(r -> r.get(id)).map(Holder::value).orElse(LootTable.EMPTY); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ReloadableServerRegistries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */