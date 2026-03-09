/*    */ package net.minecraft.util;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface AbortableIterationConsumer<T> {
/*    */   Continuation accept(T paramT);
/*    */   
/*    */   public enum Continuation {
/*  8 */     CONTINUE,
/*  9 */     ABORT;
/*    */ 
/*    */     
/* 12 */     public boolean shouldAbort() { return (this == ABORT); }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> AbortableIterationConsumer<T> forConsumer(Consumer<T> consumer) {
/* 24 */     return e -> {
/* 25 */         consumer.accept(e);
/* 26 */         return Continuation.CONTINUE;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\AbortableIterationConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */