/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ProfiledReloadInstance
/*    */   extends SimpleReloadInstance<ProfiledReloadInstance.State> {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 19 */   private final Stopwatch total = Stopwatch.createUnstarted();
/*    */   
/*    */   public static ReloadInstance of(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor taskExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask) {
/* 22 */     ProfiledReloadInstance result = new ProfiledReloadInstance(listeners);
/* 23 */     result.startTasks(taskExecutor, mainThreadExecutor, resourceManager, listeners, (currentReload, previousStep, listener, parentTaskExecutor, parentReloadExecutor) -> {
/* 24 */           AtomicLong preparationNanos = new AtomicLong();
/* 25 */           AtomicLong preparationCount = new AtomicLong();
/* 26 */           AtomicLong reloadNanos = new AtomicLong();
/* 27 */           AtomicLong reloadCount = new AtomicLong();
/* 28 */           CompletableFuture<Void> reload = listener.reload(currentReload, 
/*    */               
/* 30 */               profiledExecutor(parentTaskExecutor, preparationNanos, preparationCount, listener.getName()), previousStep, 
/*    */               
/* 32 */               profiledExecutor(parentReloadExecutor, reloadNanos, reloadCount, listener.getName()));
/*    */           
/* 34 */           return reload.thenApplyAsync((), mainThreadExecutor);
/*    */         }initialTask);
/*    */ 
/*    */ 
/*    */     
/* 39 */     return result;
/*    */   }
/*    */   
/*    */   private ProfiledReloadInstance(List<PreparableReloadListener> listeners) {
/* 43 */     super(listeners);
/* 44 */     this.total.start();
/*    */   }
/*    */ 
/*    */   
/*    */   protected CompletableFuture<List<State>> prepareTasks(Executor taskExecutor, Executor mainThreadExecutor, ResourceManager resourceManager, List<PreparableReloadListener> listeners, SimpleReloadInstance.StateFactory<State> stateFactory, CompletableFuture<?> initialTask) {
/* 49 */     return super.prepareTasks(taskExecutor, mainThreadExecutor, resourceManager, listeners, stateFactory, initialTask)
/* 50 */       .thenApplyAsync(this::finish, mainThreadExecutor);
/*    */   }
/*    */   
/*    */   private static Executor profiledExecutor(Executor executor, AtomicLong accumulatedNanos, AtomicLong taskCount, String name) {
/* 54 */     return r -> executor.execute(());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private List<State> finish(List<State> result) {
/* 66 */     this.total.stop();
/* 67 */     long blockingTime = 0L;
/* 68 */     LOGGER.info("Resource reload finished after {} ms", Long.valueOf(this.total.elapsed(TimeUnit.MILLISECONDS)));
/* 69 */     for (State state : result) {
/* 70 */       long prepTime = TimeUnit.NANOSECONDS.toMillis(state.preparationNanos.get());
/* 71 */       long prepCount = state.preparationCount.get();
/* 72 */       long reloadTime = TimeUnit.NANOSECONDS.toMillis(state.reloadNanos.get());
/* 73 */       long reloadCount = state.reloadCount.get();
/* 74 */       long totalTime = prepTime + reloadTime;
/* 75 */       long totalCount = prepCount + reloadCount;
/* 76 */       String name = state.name;
/* 77 */       LOGGER.info("{} took approximately {} tasks/{} ms ({} tasks/{} ms preparing, {} tasks/{} ms applying)", new Object[] { name, 
/*    */             
/* 79 */             Long.valueOf(totalCount), Long.valueOf(totalTime), 
/* 80 */             Long.valueOf(prepCount), Long.valueOf(prepTime), 
/* 81 */             Long.valueOf(reloadCount), Long.valueOf(reloadTime) });
/*    */ 
/*    */       
/* 84 */       blockingTime += reloadTime;
/*    */     } 
/*    */     
/* 87 */     LOGGER.info("Total blocking time: {} ms", Long.valueOf(blockingTime));
/* 88 */     return result;
/*    */   }
/*    */   public static final class State extends Record { private final String name; private final AtomicLong preparationNanos; private final AtomicLong preparationCount; private final AtomicLong reloadNanos; private final AtomicLong reloadCount;
/* 91 */     public State(String name, AtomicLong preparationNanos, AtomicLong preparationCount, AtomicLong reloadNanos, AtomicLong reloadCount) { this.name = name; this.preparationNanos = preparationNanos; this.preparationCount = preparationCount; this.reloadNanos = reloadNanos; this.reloadCount = reloadCount; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #91	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 91 */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #91	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #91	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;
/* 91 */       //   0	8	1	o	Ljava/lang/Object; } public AtomicLong preparationNanos() { return this.preparationNanos; } public AtomicLong preparationCount() { return this.preparationCount; } public AtomicLong reloadNanos() { return this.reloadNanos; } public AtomicLong reloadCount() { return this.reloadCount; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ProfiledReloadInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */