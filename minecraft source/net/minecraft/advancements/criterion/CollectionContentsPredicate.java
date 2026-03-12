/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public interface CollectionContentsPredicate<T, P extends Predicate<T>>
/*    */   extends Predicate<Iterable<T>>
/*    */ {
/*    */   List<P> unpack();
/*    */   
/* 13 */   static <T, P extends Predicate<T>> Codec<CollectionContentsPredicate<T, P>> codec(Codec<P> elementCodec) { return elementCodec.listOf().xmap(CollectionContentsPredicate::of, CollectionContentsPredicate::unpack); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SafeVarargs
/* 21 */   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(P... predicates) { return of(List.of(predicates)); }
/*    */ 
/*    */   
/*    */   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(List<P> predicates) {
/* 25 */     switch (predicates.size()) { case 0: case 1:  }  return 
/*    */ 
/*    */       
/* 28 */       new Multiple(predicates);
/*    */   }
/*    */   
/*    */   public static class Zero<T, P extends Predicate<T>>
/*    */     extends Object
/*    */     implements CollectionContentsPredicate<T, P>
/*    */   {
/* 35 */     public boolean test(Iterable<T> values) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     public List<P> unpack() { return List.of(); } }
/*    */   
/*    */   public static final class Single<T, P extends Predicate<T>> extends Record implements CollectionContentsPredicate<T, P> { private final P test;
/*    */     
/* 44 */     public Single(P test) { this.test = test; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 44 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single<TT;TP;>; } public P test() { return (P)this.test; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single<TT;TP;>; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Single<TT;TP;>; }
/*    */     public boolean test(Iterable<T> values) {
/* 47 */       for (T value : values) {
/* 48 */         if (this.test.test(value)) {
/* 49 */           return true;
/*    */         }
/*    */       } 
/* 52 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 57 */     public List<P> unpack() { return List.of(this.test); } }
/*    */   
/*    */   public static final class Multiple<T, P extends Predicate<T>> extends Record implements CollectionContentsPredicate<T, P> { private final List<P> tests;
/*    */     
/* 61 */     public Multiple(List<P> tests) { this.tests = tests; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple<TT;TP;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple<TT;TP;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 61 */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionContentsPredicate$Multiple<TT;TP;>; } public List<P> tests() { return this.tests; }
/*    */     
/*    */     public boolean test(Iterable<T> values) {
/* 64 */       List<Predicate<T>> testsToMatch = new ArrayList<Predicate<T>>(this.tests);
/* 65 */       for (T value : values) {
/* 66 */         testsToMatch.removeIf(p -> p.test(value));
/* 67 */         if (testsToMatch.isEmpty()) {
/* 68 */           return true;
/*    */         }
/*    */       } 
/*    */       
/* 72 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 77 */     public List<P> unpack() { return this.tests; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CollectionContentsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */