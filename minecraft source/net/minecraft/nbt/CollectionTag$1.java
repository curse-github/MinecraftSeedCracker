/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */   implements Iterator<Tag>
/*    */ {
/*    */   private int index;
/*    */   
/* 32 */   public boolean hasNext() { return (this.index < CollectionTag.this.size()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Tag next() {
/* 37 */     if (!hasNext()) {
/* 38 */       throw new NoSuchElementException();
/*    */     }
/* 40 */     return CollectionTag.this.get(this.index++);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\CollectionTag$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */