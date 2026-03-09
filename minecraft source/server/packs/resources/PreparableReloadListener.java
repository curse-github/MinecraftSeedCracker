/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PreparableReloadListener
/*    */ {
/*    */   CompletableFuture<Void> reload(SharedState paramSharedState, Executor paramExecutor1, PreparationBarrier paramPreparationBarrier, Executor paramExecutor2);
/*    */   
/*    */   default void prepareSharedState(SharedState currentReload) {}
/*    */   
/* 17 */   default String getName() { return getClass().getSimpleName(); }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface PreparationBarrier {
/*    */     <T> CompletableFuture<T> wait(T param1T); }
/*    */   
/*    */   public static final class StateKey<T> extends Object {}
/*    */   
/*    */   public static final class SharedState {
/*    */     private final ResourceManager manager;
/*    */     private final Map<PreparableReloadListener.StateKey<?>, Object> state;
/*    */     
/*    */     public SharedState(ResourceManager manager) {
/* 30 */       this.state = new IdentityHashMap();
/*    */ 
/*    */       
/* 33 */       this.manager = manager;
/*    */     }
/*    */ 
/*    */     
/* 37 */     public ResourceManager resourceManager() { return this.manager; }
/*    */ 
/*    */ 
/*    */     
/* 41 */     public <T> void set(PreparableReloadListener.StateKey<T> key, T value) { this.state.put(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     public <T> T get(PreparableReloadListener.StateKey<T> key) { return (T)Objects.requireNonNull(this.state.get(key)); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\PreparableReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */