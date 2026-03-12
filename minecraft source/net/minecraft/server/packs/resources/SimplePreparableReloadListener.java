/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public abstract class SimplePreparableReloadListener<T>
/*    */   extends Object implements PreparableReloadListener {
/*    */   public final CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 12 */     ResourceManager manager = currentReload.resourceManager();
/*    */     
/* 14 */     Objects.requireNonNull(preparationBarrier); return CompletableFuture.supplyAsync(() -> prepare(manager, Profiler.get()), taskExecutor).thenCompose(preparationBarrier::wait)
/* 15 */       .thenAcceptAsync(preparations -> apply(preparations, manager, Profiler.get()), reloadExecutor);
/*    */   }
/*    */   
/*    */   protected abstract T prepare(ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */   
/*    */   protected abstract void apply(T paramT, ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\SimplePreparableReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */