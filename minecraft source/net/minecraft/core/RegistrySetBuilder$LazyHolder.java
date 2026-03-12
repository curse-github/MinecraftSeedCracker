/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.function.Supplier;
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
/*    */ class LazyHolder<T>
/*    */   extends Holder.Reference<T>
/*    */ {
/*    */   private Supplier<T> supplier;
/*    */   
/* 35 */   protected LazyHolder(HolderOwner<T> owner, ResourceKey<T> key) { super(Holder.Reference.Type.STAND_ALONE, owner, key, null); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void bindValue(T value) {
/* 40 */     super.bindValue(value);
/* 41 */     this.supplier = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public T value() {
/* 46 */     if (this.supplier != null) {
/* 47 */       bindValue(this.supplier.get());
/*    */     }
/* 49 */     return (T)super.value();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$LazyHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */