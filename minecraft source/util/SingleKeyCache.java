/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public class SingleKeyCache<K, V>
/*    */   extends Object
/*    */ {
/*    */   private final Function<K, V> computeValue;
/*    */   private K cacheKey;
/*    */   private V cachedValue;
/*    */   
/*    */   public SingleKeyCache(Function<K, V> computeValue) {
/* 15 */     this.cacheKey = null;
/*    */ 
/*    */ 
/*    */     
/* 19 */     this.computeValue = computeValue;
/*    */   }
/*    */   
/*    */   public V getValue(K cacheKey) {
/* 23 */     if (this.cachedValue == null || !Objects.equals(this.cacheKey, cacheKey)) {
/* 24 */       this.cachedValue = this.computeValue.apply(cacheKey);
/* 25 */       this.cacheKey = cacheKey;
/*    */     } 
/* 27 */     return (V)this.cachedValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SingleKeyCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */