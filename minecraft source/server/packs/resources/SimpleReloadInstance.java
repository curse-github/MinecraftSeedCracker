/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleReloadInstance<S>
/*     */   extends Object
/*     */   implements ReloadInstance
/*     */ {
/*     */   private static final int PREPARATION_PROGRESS_WEIGHT = 2;
/*     */   private static final int EXTRA_RELOAD_PROGRESS_WEIGHT = 2;
/*     */   private static final int LISTENER_PROGRESS_WEIGHT = 1;
/*     */   private final CompletableFuture<Unit> allPreparations;
/*     */   private CompletableFuture<List<S>> allDone;
/*     */   private final Set<PreparableReloadListener> preparingListeners;
/*     */   private final int listenerCount;
/*     */   private final AtomicInteger startedTasks;
/*     */   private final AtomicInteger finishedTasks;
/*     */   private final AtomicInteger startedReloads;
/*     */   private final AtomicInteger finishedReloads;
/*     */   
/*     */   public static ReloadInstance of(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor taskExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask) {
/*  33 */     SimpleReloadInstance<Void> result = new SimpleReloadInstance<Void>(listeners);
/*  34 */     result.startTasks(taskExecutor, mainThreadExecutor, resourceManager, listeners, StateFactory.SIMPLE, initialTask);
/*  35 */     return result; } protected SimpleReloadInstance(List<PreparableReloadListener> listeners) { this.allPreparations = new CompletableFuture(); this.startedTasks = new AtomicInteger();
/*     */     this.finishedTasks = new AtomicInteger();
/*     */     this.startedReloads = new AtomicInteger();
/*     */     this.finishedReloads = new AtomicInteger();
/*  39 */     this.listenerCount = listeners.size();
/*  40 */     this.preparingListeners = new HashSet(listeners); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   protected void startTasks(Executor taskExecutor, Executor mainThreadExecutor, ResourceManager resourceManager, List<PreparableReloadListener> listeners, StateFactory<S> stateFactory, CompletableFuture<?> initialTask) { this.allDone = prepareTasks(taskExecutor, mainThreadExecutor, resourceManager, listeners, stateFactory, initialTask); }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<List<S>> prepareTasks(Executor taskExecutor, Executor mainThreadExecutor, ResourceManager resourceManager, List<PreparableReloadListener> listeners, StateFactory<S> stateFactory, CompletableFuture<?> initialTask) {
/*  48 */     Executor countingTaskExecutor = r -> {
/*  49 */         this.startedTasks.incrementAndGet();
/*  50 */         taskExecutor.execute(());
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     Executor countingReloadExecutor = r -> {
/*  57 */         this.startedReloads.incrementAndGet();
/*  58 */         mainThreadExecutor.execute(());
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.startedTasks.incrementAndGet();
/*  65 */     Objects.requireNonNull(this.finishedTasks); initialTask.thenRun(this.finishedTasks::incrementAndGet);
/*     */     
/*  67 */     PreparableReloadListener.SharedState sharedState = new PreparableReloadListener.SharedState(resourceManager);
/*     */     
/*  69 */     listeners.forEach(listener -> listener.prepareSharedState(sharedState));
/*     */     
/*  71 */     CompletableFuture<?> barrier = initialTask;
/*  72 */     List<CompletableFuture<S>> allSteps = new ArrayList<CompletableFuture<S>>();
/*  73 */     for (PreparableReloadListener listener : listeners) {
/*  74 */       PreparableReloadListener.PreparationBarrier barrierForCurrentTask = createBarrierForListener(listener, barrier, mainThreadExecutor);
/*  75 */       CompletableFuture<S> state = stateFactory.create(sharedState, barrierForCurrentTask, listener, countingTaskExecutor, countingReloadExecutor);
/*  76 */       allSteps.add(state);
/*  77 */       barrier = state;
/*     */     } 
/*  79 */     return Util.sequenceFailFast(allSteps);
/*     */   }
/*     */   
/*     */   private PreparableReloadListener.PreparationBarrier createBarrierForListener(final PreparableReloadListener listener, final CompletableFuture<?> previousBarrier, final Executor mainThreadExecutor) {
/*  83 */     return new PreparableReloadListener.PreparationBarrier()
/*     */       {
/*     */         public <T> CompletableFuture<T> wait(T t) {
/*  86 */           mainThreadExecutor.execute(() -> {
/*  87 */                 SimpleReloadInstance.this.preparingListeners.remove(listener);
/*  88 */                 if (SimpleReloadInstance.this.preparingListeners.isEmpty()) {
/*  89 */                   SimpleReloadInstance.this.allPreparations.complete(Unit.INSTANCE);
/*     */                 }
/*     */               });
/*  92 */           return SimpleReloadInstance.this.allPreparations.thenCombine(previousBarrier, (v1, v2) -> t);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public CompletableFuture<?> done() { return (CompletableFuture)Objects.requireNonNull(this.allDone, "not started"); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getActualProgress() {
/* 104 */     int preparationsDone = this.listenerCount - this.preparingListeners.size();
/* 105 */     float doneCount = weightProgress(this.finishedTasks.get(), this.finishedReloads.get(), preparationsDone);
/* 106 */     float totalCount = weightProgress(this.startedTasks.get(), this.startedReloads.get(), this.listenerCount);
/* 107 */     return doneCount / totalCount;
/*     */   }
/*     */ 
/*     */   
/* 111 */   private static int weightProgress(int preparationTasks, int reloadTasks, int listeners) { return preparationTasks * 2 + reloadTasks * 2 + listeners * 1; }
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface StateFactory<S>
/*     */   {
/* 116 */     public static final StateFactory<Void> SIMPLE = (currentReload, previousStep, listener, taskExecutor, reloadExecutor) -> listener.reload(currentReload, taskExecutor, previousStep, reloadExecutor);
/*     */     
/*     */     CompletableFuture<S> create(PreparableReloadListener.SharedState param1SharedState, PreparableReloadListener.PreparationBarrier param1PreparationBarrier, PreparableReloadListener param1PreparableReloadListener, Executor param1Executor1, Executor param1Executor2);
/*     */   }
/*     */   
/*     */   public static ReloadInstance create(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask, boolean enableProfiling) {
/* 122 */     if (enableProfiling) {
/* 123 */       return ProfiledReloadInstance.of(resourceManager, listeners, backgroundExecutor, mainThreadExecutor, initialTask);
/*     */     }
/* 125 */     return of(resourceManager, listeners, backgroundExecutor, mainThreadExecutor, initialTask);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\SimpleReloadInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */