/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleReloadInstance;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableServerResources {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 24 */   private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*    */   
/*    */   private final ReloadableServerRegistries.Holder fullRegistryHolder;
/*    */   
/*    */   private final Commands commands;
/*    */   private final RecipeManager recipes;
/*    */   private final ServerAdvancementManager advancements;
/*    */   private final ServerFunctionLibrary functionLibrary;
/*    */   private final List<Registry.PendingTags<?>> postponedTags;
/*    */   
/*    */   private ReloadableServerResources(LayeredRegistryAccess<RegistryLayer> fullLayers, HolderLookup.Provider loadingContext, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> postponedTags, PermissionSet functionCompilationPermissions) {
/* 35 */     this.fullRegistryHolder = new ReloadableServerRegistries.Holder(fullLayers.compositeAccess());
/*    */     
/* 37 */     this.postponedTags = postponedTags;
/*    */     
/* 39 */     this.recipes = new RecipeManager(loadingContext);
/* 40 */     this.commands = new Commands(commandSelection, CommandBuildContext.simple(loadingContext, enabledFeatures));
/* 41 */     this.advancements = new ServerAdvancementManager(loadingContext);
/* 42 */     this.functionLibrary = new ServerFunctionLibrary(functionCompilationPermissions, this.commands.getDispatcher());
/*    */   }
/*    */ 
/*    */   
/* 46 */   public ServerFunctionLibrary getFunctionLibrary() { return this.functionLibrary; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public ReloadableServerRegistries.Holder fullRegistries() { return this.fullRegistryHolder; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public RecipeManager getRecipeManager() { return this.recipes; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public Commands getCommands() { return this.commands; }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public ServerAdvancementManager getAdvancements() { return this.advancements; }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public List<PreparableReloadListener> listeners() { return List.of(this.recipes, this.functionLibrary, this.advancements); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CompletableFuture<ReloadableServerResources> loadResources(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> contextLayers, List<Registry.PendingTags<?>> updatedContextTags, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions, Executor backgroundExecutor, Executor mainThreadExecutor) {
/* 79 */     return ReloadableServerRegistries.reload(contextLayers, updatedContextTags, resourceManager, backgroundExecutor).thenCompose(fullRegistries -> {
/* 80 */           ReloadableServerResources result = new ReloadableServerResources(fullRegistries.layers(), fullRegistries.lookupWithUpdatedTags(), enabledFeatures, commandSelection, updatedContextTags, functionCompilationPermissions);
/* 81 */           return SimpleReloadInstance.create(resourceManager, result.listeners(), backgroundExecutor, mainThreadExecutor, DATA_RELOAD_INITIAL_TASK, LOGGER.isDebugEnabled()).done()
/* 82 */             .thenApply(());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 87 */   public void updateStaticRegistryTags() { this.postponedTags.forEach(Registry.PendingTags::apply); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ReloadableServerResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */