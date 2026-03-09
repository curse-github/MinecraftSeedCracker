/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ 
/*    */ public interface CollectionTag
/*    */   extends Tag, Iterable<Tag> {
/*    */   void clear();
/*    */   
/*    */   boolean setTag(int paramInt, Tag paramTag);
/*    */   
/*    */   boolean addTag(int paramInt, Tag paramTag);
/*    */   
/*    */   Tag remove(int paramInt);
/*    */   
/*    */   Tag get(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/* 22 */   default boolean isEmpty() { return (size() == 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   default Iterator<Tag> iterator() {
/* 27 */     return new Iterator<Tag>()
/*    */       {
/*    */         private int index;
/*    */ 
/*    */         
/* 32 */         public boolean hasNext() { return (this.index < CollectionTag.this.size()); }
/*    */ 
/*    */ 
/*    */         
/*    */         public Tag next() {
/* 37 */           if (!hasNext()) {
/* 38 */             throw new NoSuchElementException();
/*    */           }
/* 40 */           return CollectionTag.this.get(this.index++);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 46 */   default Stream<Tag> stream() { return StreamSupport.stream(spliterator(), false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\CollectionTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */