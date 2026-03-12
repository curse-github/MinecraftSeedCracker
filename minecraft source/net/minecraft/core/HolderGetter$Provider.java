/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Provider
/*    */ {
/*    */   default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 33 */     return (HolderGetter)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*    */   }
/*    */ 
/*    */   
/* 37 */   default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return lookup(id.registryKey()).flatMap(l -> l.get(id)); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> id) { return (Holder.Reference)lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id))); }
/*    */   
/*    */   <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderGetter$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */