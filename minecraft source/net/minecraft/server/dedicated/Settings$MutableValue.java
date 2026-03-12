/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.RegistryAccess;
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
/*    */ public class MutableValue<V>
/*    */   extends Object
/*    */   implements Supplier<V>
/*    */ {
/*    */   private final String key;
/*    */   private final V value;
/*    */   private final Function<V, String> serializer;
/*    */   
/*    */   private MutableValue(String key, V value, Function<V, String> serializer) {
/* 34 */     this.key = key;
/* 35 */     this.value = value;
/* 36 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public V get() { return (V)this.value; }
/*    */ 
/*    */   
/*    */   public T update(RegistryAccess registryAccess, V value) {
/* 45 */     Properties properties = Settings.this.cloneProperties();
/* 46 */     properties.put(this.key, this.serializer.apply(value));
/* 47 */     return (T)Settings.this.reload(registryAccess, properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\Settings$MutableValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */