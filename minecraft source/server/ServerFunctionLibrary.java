/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerFunctionLibrary implements PreparableReloadListener {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  36 */   public static final ResourceKey<Registry<CommandFunction<CommandSourceStack>>> TYPE_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("function"));
/*     */   
/*  38 */   private static final FileToIdConverter LISTER = new FileToIdConverter(Registries.elementsDirPath(TYPE_KEY), ".mcfunction");
/*     */ 
/*     */   
/*     */   private final TagLoader<CommandFunction<CommandSourceStack>> tagsLoader;
/*     */   
/*     */   private final PermissionSet functionCompilationPermissions;
/*     */   
/*     */   private final CommandDispatcher<CommandSourceStack> dispatcher;
/*     */ 
/*     */   
/*  48 */   public Optional<CommandFunction<CommandSourceStack>> getFunction(Identifier id) { return Optional.ofNullable((CommandFunction)this.functions.get(id)); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public Map<Identifier, CommandFunction<CommandSourceStack>> getFunctions() { return this.functions; }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public List<CommandFunction<CommandSourceStack>> getTag(Identifier tag) { return (List)this.tags.getOrDefault(tag, List.of()); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Iterable<Identifier> getAvailableTags() { return this.tags.keySet(); } public ServerFunctionLibrary(PermissionSet functionCompilationPermissions, CommandDispatcher<CommandSourceStack> dispatcher) {
/*     */     this.functions = ImmutableMap.of();
/*     */     this.tagsLoader = new TagLoader((id, required) -> getFunction(id), Registries.tagsDirPath(TYPE_KEY));
/*     */     this.tags = Map.of();
/*  64 */     this.functionCompilationPermissions = functionCompilationPermissions;
/*  65 */     this.dispatcher = dispatcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/*  70 */     ResourceManager manager = currentReload.resourceManager();
/*  71 */     CompletableFuture<Map<Identifier, List<TagLoader.EntryWithSource>>> tags = CompletableFuture.supplyAsync(() -> this.tagsLoader.load(manager), taskExecutor);
/*     */ 
/*     */ 
/*     */     
/*  75 */     CompletableFuture<Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>>> functions = CompletableFuture.supplyAsync(() -> LISTER.listMatchingResources(manager), taskExecutor).thenCompose(functionsToLoad -> {
/*  76 */           Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>> result = Maps.newHashMap();
/*  77 */           CommandSourceStack compilationContext = Commands.createCompilationContext(this.functionCompilationPermissions);
/*     */           
/*  79 */           for (Map.Entry<Identifier, Resource> entry : functionsToLoad.entrySet()) {
/*  80 */             Identifier resourceId = (Identifier)entry.getKey();
/*  81 */             Identifier id = LISTER.fileToId(resourceId);
/*     */             
/*  83 */             result.put(id, CompletableFuture.supplyAsync((), taskExecutor));
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  89 */           CompletableFuture[] futuresToCollect = (CompletableFuture[])result.values().toArray(new CompletableFuture[0]);
/*     */           
/*  91 */           return CompletableFuture.allOf(futuresToCollect).handle(());
/*     */         });
/*     */ 
/*     */     
/*  95 */     Objects.requireNonNull(preparationBarrier); return tags.thenCombine(functions, Pair::of).thenCompose(preparationBarrier::wait)
/*  96 */       .thenAcceptAsync(data -> {
/*  97 */           Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>> functionFutures = (Map)data.getSecond();
/*  98 */           ImmutableMap.Builder<Identifier, CommandFunction<CommandSourceStack>> newFunctions = ImmutableMap.builder();
/*  99 */           functionFutures.forEach(());
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
/* 110 */           this.functions = newFunctions.build();
/* 111 */           this.tags = this.tagsLoader.build((Map)data.getFirst());
/*     */         }reloadExecutor);
/*     */   }
/*     */   private static List<String> readLines(Resource resource) {
/*     */     
/* 116 */     try { BufferedReader reader = resource.openAsReader(); 
/* 117 */       try { List list = reader.lines().toList();
/* 118 */         if (reader != null) reader.close();  return list; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 119 */     { throw new CompletionException(ex); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerFunctionLibrary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */