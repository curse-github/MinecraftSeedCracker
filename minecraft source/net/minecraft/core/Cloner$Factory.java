/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ 
/*    */ public class Factory
/*    */ {
/* 28 */   private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap();
/*    */   
/*    */   public <T> Factory addCodec(ResourceKey<? extends Registry<? extends T>> key, Codec<T> codec) {
/* 31 */     this.codecs.put(key, new Cloner(codec));
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public <T> Cloner<T> cloner(ResourceKey<? extends Registry<? extends T>> key) { return (Cloner)this.codecs.get(key); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Cloner$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */