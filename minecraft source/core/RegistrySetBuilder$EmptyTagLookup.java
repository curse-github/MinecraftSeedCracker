/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ abstract class EmptyTagLookup<T>
/*    */   extends Object
/*    */   implements HolderGetter<T>
/*    */ {
/*    */   protected final HolderOwner<T> owner;
/*    */   
/* 57 */   protected EmptyTagLookup(HolderOwner<T> owner) { this.owner = owner; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.of(HolderSet.emptyNamed(this.owner, id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */