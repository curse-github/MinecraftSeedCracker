/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
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
/*    */ public interface Delegate<T>
/*    */   extends HolderLookup.RegistryLookup<T>
/*    */ {
/*    */   HolderLookup.RegistryLookup<T> parent();
/*    */   
/* 74 */   default ResourceKey<? extends Registry<? extends T>> key() { return parent().key(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   default Lifecycle registryLifecycle() { return parent().registryLifecycle(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   default Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 89 */   default Stream<Holder.Reference<T>> listElements() { return parent().listElements(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 94 */   default Optional<HolderSet.Named<T>> get(TagKey<T> id) { return parent().get(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   default Stream<HolderSet.Named<T>> listTags() { return parent().listTags(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderLookup$RegistryLookup$Delegate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */