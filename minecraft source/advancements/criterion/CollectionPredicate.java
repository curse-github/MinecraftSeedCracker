/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class CollectionPredicate<T, P extends Predicate<T>> extends Record implements Predicate<Iterable<T>> {
/*    */   private final Optional<CollectionContentsPredicate<T, P>> contains;
/*    */   private final Optional<CollectionCountsPredicate<T, P>> counts;
/*    */   private final Optional<MinMaxBounds.Ints> size;
/*    */   
/* 10 */   public CollectionPredicate(Optional<CollectionContentsPredicate<T, P>> contains, Optional<CollectionCountsPredicate<T, P>> counts, Optional<MinMaxBounds.Ints> size) { this.contains = contains; this.counts = counts; this.size = size; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/CollectionPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate<TT;TP;>; } public Optional<CollectionContentsPredicate<T, P>> contains() { return this.contains; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/CollectionPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate<TT;TP;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/CollectionPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 10 */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/CollectionPredicate<TT;TP;>; } public Optional<CollectionCountsPredicate<T, P>> counts() { return this.counts; } public Optional<MinMaxBounds.Ints> size() { return this.size; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static <T, P extends Predicate<T>> Codec<CollectionPredicate<T, P>> codec(Codec<P> elementCodec) { return RecordCodecBuilder.create(i -> i.group(
/* 17 */           CollectionContentsPredicate.codec(elementCodec).optionalFieldOf("contains").forGetter(CollectionPredicate::contains), 
/* 18 */           CollectionCountsPredicate.codec(elementCodec).optionalFieldOf("count").forGetter(CollectionPredicate::counts), MinMaxBounds.Ints.CODEC
/* 19 */           .optionalFieldOf("size").forGetter(CollectionPredicate::size))
/* 20 */         .apply(i, CollectionPredicate::new)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(Iterable<T> value) {
/* 25 */     if (this.contains.isPresent() && !((CollectionContentsPredicate)this.contains.get()).test(value)) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     if (this.counts.isPresent() && !((CollectionCountsPredicate)this.counts.get()).test(value)) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     if (this.size.isPresent() && !((MinMaxBounds.Ints)this.size.get()).matches(Iterables.size(value))) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CollectionPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */