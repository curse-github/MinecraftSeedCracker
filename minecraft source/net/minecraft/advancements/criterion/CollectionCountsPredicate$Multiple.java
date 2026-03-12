/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Multiple<T, P extends Predicate<T>>
/*    */   extends Record
/*    */   implements CollectionCountsPredicate<T, P>
/*    */ {
/*    */   private final List<CollectionCountsPredicate.Entry<T, P>> entries;
/*    */   
/* 56 */   public Multiple(List<CollectionCountsPredicate.Entry<T, P>> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 56 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; } public List<CollectionCountsPredicate.Entry<T, P>> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionCountsPredicate$Multiple<TT;TP;>; }
/*    */   public boolean test(Iterable<T> values) {
/* 59 */     for (CollectionCountsPredicate.Entry<T, P> entry : this.entries) {
/* 60 */       if (!entry.test(values)) {
/* 61 */         return false;
/*    */       }
/*    */     } 
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public List<CollectionCountsPredicate.Entry<T, P>> unpack() { return this.entries; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CollectionCountsPredicate$Multiple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */