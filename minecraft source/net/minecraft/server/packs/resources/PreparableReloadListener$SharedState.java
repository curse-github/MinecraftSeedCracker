/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SharedState
/*    */ {
/*    */   private final ResourceManager manager;
/*    */   private final Map<PreparableReloadListener.StateKey<?>, Object> state;
/*    */   
/*    */   public SharedState(ResourceManager manager) {
/* 30 */     this.state = new IdentityHashMap();
/*    */ 
/*    */     
/* 33 */     this.manager = manager;
/*    */   }
/*    */ 
/*    */   
/* 37 */   public ResourceManager resourceManager() { return this.manager; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public <T> void set(PreparableReloadListener.StateKey<T> key, T value) { this.state.put(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <T> T get(PreparableReloadListener.StateKey<T> key) { return (T)Objects.requireNonNull(this.state.get(key)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\PreparableReloadListener$SharedState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */