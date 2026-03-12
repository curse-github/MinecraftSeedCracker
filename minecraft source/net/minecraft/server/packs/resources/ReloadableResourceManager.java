/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableResourceManager implements AutoCloseable, ResourceManager {
/*    */   private CloseableResourceManager resources;
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public ReloadableResourceManager(PackType type) {
/* 25 */     this.listeners = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */     
/* 29 */     this.type = type;
/* 30 */     this.resources = new MultiPackResourceManager(type, List.of());
/*    */   }
/*    */   
/*    */   private final List<PreparableReloadListener> listeners;
/*    */   
/* 35 */   public void close() { this.resources.close(); }
/*    */   
/*    */   private final PackType type;
/*    */   
/* 39 */   public void registerReloadListener(PreparableReloadListener listener) { this.listeners.add(listener); }
/*    */ 
/*    */   
/*    */   public ReloadInstance createReload(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask, List<PackResources> resourcePacks) {
/* 43 */     LOGGER.info("Reloading ResourceManager: {}", LogUtils.defer(() -> resourcePacks.stream().map(PackResources::packId).collect(Collectors.joining(", "))));
/*    */     
/* 45 */     this.resources.close();
/* 46 */     this.resources = new MultiPackResourceManager(this.type, resourcePacks);
/* 47 */     return SimpleReloadInstance.create(this.resources, this.listeners, backgroundExecutor, mainThreadExecutor, initialTask, LOGGER.isDebugEnabled());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public Optional<Resource> getResource(Identifier location) { return this.resources.getResource(location); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public Set<String> getNamespaces() { return this.resources.getNamespaces(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public List<Resource> getResourceStack(Identifier location) { return this.resources.getResourceStack(location); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filenameFilter) { return this.resources.listResources(directory, filenameFilter); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) { return this.resources.listResourceStacks(directory, filter); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public Stream<PackResources> listPacks() { return this.resources.listPacks(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ReloadableResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */