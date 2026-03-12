/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.flag.FeatureElement;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
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
/*    */ public interface RegistryLookup<T>
/*    */   extends HolderLookup<T>, HolderOwner<T>
/*    */ {
/*    */   default RegistryLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
/* 43 */     if (FeatureElement.FILTERED_REGISTRIES.contains(key())) {
/* 44 */       return filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures));
/*    */     }
/*    */     
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   default RegistryLookup<T> filterElements(final Predicate<T> filter) {
/* 51 */     return new Delegate<T>()
/*    */       {
/*    */         public HolderLookup.RegistryLookup<T> parent() {
/* 54 */           return HolderLookup.RegistryLookup.this;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 59 */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id).filter(holder -> filter.test(holder.value())); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 64 */         public Stream<Holder.Reference<T>> listElements() { return parent().listElements().filter(e -> filter.test(e.value())); }
/*    */       };
/*    */   }
/*    */   
/*    */   ResourceKey<? extends Registry<? extends T>> key();
/*    */   
/*    */   Lifecycle registryLifecycle();
/*    */   
/*    */   public static interface Delegate<T>
/*    */     extends RegistryLookup<T> {
/* 74 */     default ResourceKey<? extends Registry<? extends T>> key() { return parent().key(); }
/*    */ 
/*    */     
/*    */     HolderLookup.RegistryLookup<T> parent();
/*    */     
/* 79 */     default Lifecycle registryLifecycle() { return parent().registryLifecycle(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     default Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 89 */     default Stream<Holder.Reference<T>> listElements() { return parent().listElements(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 94 */     default Optional<HolderSet.Named<T>> get(TagKey<T> id) { return parent().get(id); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 99 */     default Stream<HolderSet.Named<T>> listTags() { return parent().listTags(); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderLookup$RegistryLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */