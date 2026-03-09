/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContextMap
/*    */ {
/*    */   private final Map<ContextKey<?>, Object> params;
/*    */   
/* 16 */   private ContextMap(Map<ContextKey<?>, Object> params) { this.params = params; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean has(ContextKey<?> key) { return this.params.containsKey(key); }
/*    */ 
/*    */   
/*    */   public <T> T getOrThrow(ContextKey<T> key) {
/* 24 */     T value = (T)this.params.get(key);
/* 25 */     if (value == null) {
/* 26 */       throw new NoSuchElementException(key.name().toString());
/*    */     }
/*    */     
/* 29 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public <T> T getOptional(ContextKey<T> key) { return (T)this.params.get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Contract("_,!null->!null; _,_->_")
/* 40 */   public <T> T getOrDefault(ContextKey<T> param, T _default) { return (T)this.params.getOrDefault(param, _default); }
/*    */   
/*    */   public static class Builder
/*    */   {
/* 44 */     private final Map<ContextKey<?>, Object> params = new IdentityHashMap();
/*    */     
/*    */     public <T> Builder withParameter(ContextKey<T> param, T value) {
/* 47 */       this.params.put(param, value);
/* 48 */       return this;
/*    */     }
/*    */     
/*    */     public <T> Builder withOptionalParameter(ContextKey<T> param, T value) {
/* 52 */       if (value == null) {
/* 53 */         this.params.remove(param);
/*    */       } else {
/* 55 */         this.params.put(param, value);
/*    */       } 
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public <T> T getParameter(ContextKey<T> param) {
/* 61 */       T value = (T)this.params.get(param);
/* 62 */       if (value == null) {
/* 63 */         throw new NoSuchElementException(param.name().toString());
/*    */       }
/*    */       
/* 66 */       return value;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 71 */     public <T> T getOptionalParameter(ContextKey<T> param) { return (T)this.params.get(param); }
/*    */ 
/*    */     
/*    */     public ContextMap create(ContextKeySet paramSet) {
/* 75 */       Sets.SetView setView1 = Sets.difference(this.params.keySet(), paramSet.allowed());
/* 76 */       if (!setView1.isEmpty()) {
/* 77 */         throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + String.valueOf(setView1));
/*    */       }
/*    */       
/* 80 */       Sets.SetView setView2 = Sets.difference(paramSet.required(), this.params.keySet());
/* 81 */       if (!setView2.isEmpty()) {
/* 82 */         throw new IllegalArgumentException("Missing required parameters: " + String.valueOf(setView2));
/*    */       }
/*    */       
/* 85 */       return new ContextMap(this.params);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\context\ContextMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */