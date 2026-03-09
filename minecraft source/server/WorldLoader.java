/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.resources.RegistryDataLoader;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldLoader
/*     */ {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static <D, R> CompletableFuture<R> load(InitConfig config, WorldDataSupplier<D> worldDataSupplier, ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor mainThreadExecutor) {
/*     */     try {
/*  32 */       Pair<WorldDataConfiguration, CloseableResourceManager> packsAndResourceManager = config.packConfig.createResourceManager();
/*  33 */       CloseableResourceManager resources = (CloseableResourceManager)packsAndResourceManager.getSecond();
/*  34 */       LayeredRegistryAccess<RegistryLayer> initialLayers = RegistryLayer.createRegistryAccess();
/*     */ 
/*     */       
/*  37 */       List<Registry.PendingTags<?>> staticLayerTags = TagLoader.loadTagsForExistingRegistries(resources, initialLayers.getLayer(RegistryLayer.STATIC));
/*     */ 
/*     */       
/*  40 */       RegistryAccess.Frozen worldgenLoadContext = initialLayers.getAccessForLoading(RegistryLayer.WORLDGEN);
/*  41 */       List<HolderLookup.RegistryLookup<?>> worldgenContextRegistries = TagLoader.buildUpdatedLookups(worldgenLoadContext, staticLayerTags);
/*  42 */       RegistryAccess.Frozen loadedWorldgenRegistries = RegistryDataLoader.load(resources, worldgenContextRegistries, RegistryDataLoader.WORLDGEN_REGISTRIES);
/*     */ 
/*     */       
/*  45 */       List<HolderLookup.RegistryLookup<?>> dimensionContextRegistries = Stream.concat(worldgenContextRegistries.stream(), loadedWorldgenRegistries.listRegistries()).toList();
/*  46 */       RegistryAccess.Frozen initialWorldgenDimensions = RegistryDataLoader.load(resources, dimensionContextRegistries, RegistryDataLoader.DIMENSION_REGISTRIES);
/*     */       
/*  48 */       WorldDataConfiguration worldDataConfiguration = (WorldDataConfiguration)packsAndResourceManager.getFirst();
/*     */ 
/*     */       
/*  51 */       HolderLookup.Provider dimensionContextProvider = HolderLookup.Provider.create(dimensionContextRegistries.stream());
/*  52 */       DataLoadOutput<D> worldDataAndRegistries = worldDataSupplier.get(new DataLoadContext(resources, worldDataConfiguration, dimensionContextProvider, initialWorldgenDimensions));
/*     */ 
/*     */       
/*  55 */       LayeredRegistryAccess<RegistryLayer> resourcesLoadContext = initialLayers.replaceFrom(RegistryLayer.WORLDGEN, new RegistryAccess.Frozen[] { loadedWorldgenRegistries, worldDataAndRegistries.finalDimensions });
/*     */       
/*  57 */       return ReloadableServerResources.loadResources(resources, resourcesLoadContext, staticLayerTags, worldDataConfiguration.enabledFeatures(), config.commandSelection(), config.functionCompilationPermissions(), backgroundExecutor, mainThreadExecutor)
/*  58 */         .whenComplete((managers, throwable) -> {
/*  59 */             if (throwable != null) {
/*  60 */               resources.close();
/*     */             }
/*     */           
/*  63 */           }).thenApplyAsync(managers -> {
/*  64 */             managers.updateStaticRegistryTags();
/*  65 */             return resultFactory.create(resources, managers, resourcesLoadContext, worldDataAndRegistries.cookie);
/*     */           }mainThreadExecutor);
/*  67 */     } catch (Exception e) {
/*  68 */       return CompletableFuture.failedFuture(e);
/*     */     } 
/*     */   }
/*     */   public static final class DataLoadContext extends Record { private final ResourceManager resources; private final WorldDataConfiguration dataConfiguration; private final HolderLookup.Provider datapackWorldgen; private final RegistryAccess.Frozen datapackDimensions;
/*  72 */     public DataLoadContext(ResourceManager resources, WorldDataConfiguration dataConfiguration, HolderLookup.Provider datapackWorldgen, RegistryAccess.Frozen datapackDimensions) { this.resources = resources; this.dataConfiguration = dataConfiguration; this.datapackWorldgen = datapackWorldgen; this.datapackDimensions = datapackDimensions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  72 */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext; } public ResourceManager resources() { return this.resources; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$DataLoadContext;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$DataLoadContext;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadContext;
/*  72 */       //   0	8	1	o	Ljava/lang/Object; } public WorldDataConfiguration dataConfiguration() { return this.dataConfiguration; } public HolderLookup.Provider datapackWorldgen() { return this.datapackWorldgen; } public RegistryAccess.Frozen datapackDimensions() { return this.datapackDimensions; } }
/*     */   public static final class DataLoadOutput<D> extends Record { private final D cookie; private final RegistryAccess.Frozen finalDimensions;
/*  74 */     public DataLoadOutput(D cookie, RegistryAccess.Frozen finalDimensions) { this.cookie = cookie; this.finalDimensions = finalDimensions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$DataLoadOutput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  74 */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$DataLoadOutput<TD;>; } public D cookie() { return (D)this.cookie; } public RegistryAccess.Frozen finalDimensions() { return this.finalDimensions; } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PackConfig
/*     */     extends Record
/*     */   {
/*     */     private final PackRepository packRepository;
/*     */     private final WorldDataConfiguration initialDataConfig;
/*     */     private final boolean safeMode;
/*     */     private final boolean initMode;
/*     */     
/*  86 */     public PackConfig(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean safeMode, boolean initMode) { this.packRepository = packRepository; this.initialDataConfig = initialDataConfig; this.safeMode = safeMode; this.initMode = initMode; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$PackConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$PackConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$PackConfig; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$PackConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$PackConfig;
/*  86 */       //   0	8	1	o	Ljava/lang/Object; } public PackRepository packRepository() { return this.packRepository; } public WorldDataConfiguration initialDataConfig() { return this.initialDataConfig; } public boolean safeMode() { return this.safeMode; } public boolean initMode() { return this.initMode; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
/*  93 */       WorldDataConfiguration newPackConfig = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig, this.initMode, this.safeMode);
/*     */       
/*  95 */       List<PackResources> openedPacks = this.packRepository.openAllSelected();
/*  96 */       MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
/*  97 */       return Pair.of(newPackConfig, multiPackResourceManager);
/*     */     } }
/*     */   public static final class InitConfig extends Record { private final WorldLoader.PackConfig packConfig; private final Commands.CommandSelection commandSelection; private final PermissionSet functionCompilationPermissions;
/*     */     
/* 101 */     public InitConfig(WorldLoader.PackConfig packConfig, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions) { this.packConfig = packConfig; this.commandSelection = commandSelection; this.functionCompilationPermissions = functionCompilationPermissions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldLoader$InitConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldLoader$InitConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/WorldLoader$InitConfig; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldLoader$InitConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/WorldLoader$InitConfig;
/* 101 */       //   0	8	1	o	Ljava/lang/Object; } public WorldLoader.PackConfig packConfig() { return this.packConfig; } public Commands.CommandSelection commandSelection() { return this.commandSelection; } public PermissionSet functionCompilationPermissions() { return this.functionCompilationPermissions; } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface WorldDataSupplier<D> {
/*     */     WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext param1DataLoadContext);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ResultFactory<D, R> {
/*     */     R create(CloseableResourceManager param1CloseableResourceManager, ReloadableServerResources param1ReloadableServerResources, LayeredRegistryAccess<RegistryLayer> param1LayeredRegistryAccess, D param1D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\WorldLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */