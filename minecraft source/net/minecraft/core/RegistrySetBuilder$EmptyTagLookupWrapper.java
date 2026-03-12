/*    */ package net.minecraft.core;
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
/*    */ class EmptyTagLookupWrapper<T>
/*    */   extends RegistrySetBuilder.EmptyTagRegistryLookup<T>
/*    */   implements HolderLookup.RegistryLookup.Delegate<T>
/*    */ {
/*    */   private final HolderLookup.RegistryLookup<T> parent;
/*    */   
/*    */   private EmptyTagLookupWrapper(HolderOwner<T> owner, HolderLookup.RegistryLookup<T> parent) {
/* 81 */     super(owner);
/* 82 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 87 */   public HolderLookup.RegistryLookup<T> parent() { return this.parent; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookupWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */