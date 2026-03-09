/*     */ package net.minecraft.data.loot;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.RandomSequence;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LootTableProvider implements DataProvider {
/*     */   private final PackOutput.PathProvider pathProvider;
/*     */   private final Set<ResourceKey<LootTable>> requiredTables;
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger(); private final List<SubProviderEntry> subProviders; private final CompletableFuture<HolderLookup.Provider> registries;
/*     */   public static final class SubProviderEntry extends Record { private final Function<HolderLookup.Provider, LootTableSubProvider> provider; private final ContextKeySet paramSet;
/*  39 */     public SubProviderEntry(Function<HolderLookup.Provider, LootTableSubProvider> provider, ContextKeySet paramSet) { this.provider = provider; this.paramSet = paramSet; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  39 */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry; } public Function<HolderLookup.Provider, LootTableSubProvider> provider() { return this.provider; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$SubProviderEntry;
/*  39 */       //   0	8	1	o	Ljava/lang/Object; } public ContextKeySet paramSet() { return this.paramSet; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
/*  47 */     this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
/*  48 */     this.subProviders = subProviders;
/*  49 */     this.requiredTables = requiredTables;
/*  50 */     this.registries = registries;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public CompletableFuture<?> run(CachedOutput cache) { return this.registries.thenCompose(registries -> run(cache, registries)); }
/*     */ 
/*     */   
/*     */   private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
/*  59 */     MappedRegistry mappedRegistry = new MappedRegistry(Registries.LOOT_TABLE, Lifecycle.experimental());
/*     */     
/*  61 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/*     */     
/*  63 */     this.subProviders.forEach(subProvider -> (
/*  64 */         (LootTableSubProvider)subProvider.provider().apply(registries)).generate(()));
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
/*  76 */     mappedRegistry.freeze();
/*     */     
/*  78 */     ProblemReporter.Collector problems = new ProblemReporter.Collector();
/*  79 */     RegistryAccess.Frozen frozen = (new RegistryAccess.ImmutableRegistryAccess(List.of(mappedRegistry))).freeze();
/*     */     
/*  81 */     ValidationContext validationContext = new ValidationContext(problems, LootContextParamSets.ALL_PARAMS, frozen);
/*     */     
/*  83 */     Sets.SetView setView = Sets.difference(this.requiredTables, mappedRegistry.registryKeySet());
/*     */     
/*  85 */     for (ResourceKey<LootTable> missingTable : setView) {
/*  86 */       problems.report(new MissingTableProblem(missingTable));
/*     */     }
/*     */     
/*  89 */     mappedRegistry.listElements().forEach(tableHolder -> (
/*  90 */         (LootTable)tableHolder.value()).validate(validationContext.setContextKeySet(((LootTable)tableHolder.value()).getParamSet()).enterElement(new ProblemReporter.RootElementPathElement(tableHolder.key()), tableHolder.key())));
/*     */ 
/*     */     
/*  93 */     if (!problems.isEmpty()) {
/*  94 */       problems.forEach((id, problem) -> LOGGER.warn("Found validation problem in {}: {}", id, problem.description()));
/*  95 */       throw new IllegalStateException("Failed to validate loot tables, see logs");
/*     */     } 
/*     */     
/*  98 */     return CompletableFuture.allOf((CompletableFuture[])mappedRegistry.entrySet().stream()
/*  99 */         .map(entry -> {
/* 100 */             ResourceKey<LootTable> id = (ResourceKey)entry.getKey();
/* 101 */             LootTable table = (LootTable)entry.getValue();
/* 102 */             Path path = this.pathProvider.json(id.identifier());
/* 103 */             return DataProvider.saveStable(cache, registries, LootTable.DIRECT_CODEC, table, path);
/*     */           
/* 105 */           }).toArray(x$0 -> new CompletableFuture[x$0]));
/*     */   }
/*     */ 
/*     */   
/* 109 */   private static Identifier sequenceIdForLootTable(ResourceKey<LootTable> id) { return id.identifier(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public final String getName() { return "Loot Tables"; }
/*     */   public static final class MissingTableProblem extends Record implements ProblemReporter.Problem { private final ResourceKey<LootTable> id;
/*     */     
/* 117 */     public MissingTableProblem(ResourceKey<LootTable> id) { this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/* 117 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<LootTable> id() { return this.id; }
/*     */ 
/*     */     
/* 120 */     public String description() { return "Missing built-in table: " + String.valueOf(this.id.identifier()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\LootTableProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */