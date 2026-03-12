/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.stream.Stream;
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
/*    */ abstract class EmptyTagRegistryLookup<T>
/*    */   extends RegistrySetBuilder.EmptyTagLookup<T>
/*    */   implements HolderLookup.RegistryLookup<T>
/*    */ {
/* 68 */   protected EmptyTagRegistryLookup(HolderOwner<T> owner) { super(owner); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 73 */   public Stream<HolderSet.Named<T>> listTags() { throw new UnsupportedOperationException("Tags are not available in datagen"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagRegistryLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */