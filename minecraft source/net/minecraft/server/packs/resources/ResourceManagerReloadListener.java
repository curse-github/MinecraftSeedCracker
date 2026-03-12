/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public interface ResourceManagerReloadListener
/*    */   extends PreparableReloadListener
/*    */ {
/*    */   default CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 13 */     ResourceManager manager = currentReload.resourceManager();
/* 14 */     return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
/* 15 */           ProfilerFiller reloadProfiler = Profiler.get();
/* 16 */           reloadProfiler.push("listener");
/* 17 */           onResourceManagerReload(manager);
/* 18 */           reloadProfiler.pop();
/*    */         }reloadExecutor);
/*    */   }
/*    */   
/*    */   void onResourceManagerReload(ResourceManager paramResourceManager);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ResourceManagerReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */