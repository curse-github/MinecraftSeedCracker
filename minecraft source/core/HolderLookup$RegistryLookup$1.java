/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
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
/*    */ class null
/*    */   extends Object
/*    */   implements HolderLookup.RegistryLookup.Delegate<T>
/*    */ {
/* 54 */   public HolderLookup.RegistryLookup<T> parent() { return HolderLookup.RegistryLookup.this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id).filter(holder -> filter.test(holder.value())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public Stream<Holder.Reference<T>> listElements() { return parent().listElements().filter(e -> filter.test(e.value())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderLookup$RegistryLookup$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */