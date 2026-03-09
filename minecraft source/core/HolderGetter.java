/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HolderGetter<T>
/*    */ {
/* 16 */   default Holder.Reference<T> getOrThrow(ResourceKey<T> id) { return (Holder.Reference)get(id).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id))); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   default HolderSet.Named<T> getOrThrow(TagKey<T> id) { return (HolderSet.Named)get(id).orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(id))); }
/*    */   
/*    */   Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey);
/*    */   
/* 26 */   default Optional<Holder<T>> getRandomElementOf(TagKey<T> tag, RandomSource random) { return get(tag).flatMap(holderSet -> holderSet.getRandomElement(random)); }
/*    */   
/*    */   Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey);
/*    */   
/*    */   public static interface Provider
/*    */   {
/*    */     default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 33 */       return (HolderGetter)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*    */     }
/*    */ 
/*    */     
/* 37 */     default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return lookup(id.registryKey()).flatMap(l -> l.get(id)); }
/*    */ 
/*    */ 
/*    */     
/* 41 */     default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> id) { return (Holder.Reference)lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id))); }
/*    */     
/*    */     <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */