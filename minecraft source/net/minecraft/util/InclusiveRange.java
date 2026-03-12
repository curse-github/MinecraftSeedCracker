/*    */ package net.minecraft.util;
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ public final class InclusiveRange<T extends Comparable<T>> extends Record {
/*    */   private final T minInclusive;
/*    */   private final T maxInclusive;
/*    */   
/*  8 */   public T minInclusive() { return (T)this.minInclusive; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/InclusiveRange;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/InclusiveRange;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/InclusiveRange<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/InclusiveRange;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/InclusiveRange;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  8 */     //   0	8	0	this	Lnet/minecraft/util/InclusiveRange<TT;>; } public T maxInclusive() { return (T)this.maxInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static <T extends Comparable<T>> Codec<InclusiveRange<T>> codec(Codec<T> elementCodec) { return ExtraCodecs.intervalCodec(elementCodec, "min_inclusive", "max_inclusive", InclusiveRange::create, InclusiveRange::minInclusive, InclusiveRange::maxInclusive); }
/*    */ 
/*    */   
/*    */   public static <T extends Comparable<T>> Codec<InclusiveRange<T>> codec(Codec<T> elementCodec, T minAllowedInclusive, T maxAllowedInclusive) {
/* 17 */     return codec(elementCodec).validate(value -> {
/* 18 */           if (value.minInclusive().compareTo(minAllowedInclusive) < 0) {
/* 19 */             return DataResult.error(());
/*    */           }
/* 21 */           if (value.maxInclusive().compareTo(maxAllowedInclusive) > 0) {
/* 22 */             return DataResult.error(());
/*    */           }
/* 24 */           return DataResult.success(value);
/*    */         });
/*    */   }
/*    */   
/*    */   public static <T extends Comparable<T>> DataResult<InclusiveRange<T>> create(T minInclusive, T maxInclusive) {
/* 29 */     if (minInclusive.compareTo(maxInclusive) <= 0) {
/* 30 */       return DataResult.success(new InclusiveRange(minInclusive, maxInclusive));
/*    */     }
/* 32 */     return DataResult.error(() -> "min_inclusive must be less than or equal to max_inclusive");
/*    */   }
/*    */   
/*    */   public InclusiveRange(T minInclusive, T maxInclusive) {
/* 36 */     if (minInclusive.compareTo(maxInclusive) > 0)
/* 37 */       throw new IllegalArgumentException("min_inclusive must be less than or equal to max_inclusive"); 
/*    */     this.minInclusive = minInclusive;
/*    */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/* 42 */   public InclusiveRange(T value) { this(value, value); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <S extends Comparable<S>> InclusiveRange<S> map(Function<? super T, ? extends S> mapper) { return new InclusiveRange((Comparable)mapper.apply(this.minInclusive), (Comparable)mapper.apply(this.maxInclusive)); }
/*    */ 
/*    */   
/* 49 */   public static final Codec<InclusiveRange<Integer>> INT = codec(Codec.INT);
/*    */ 
/*    */   
/* 52 */   public boolean isValueInRange(T value) { return (value.compareTo(this.minInclusive) >= 0 && value.compareTo(this.maxInclusive) <= 0); }
/*    */ 
/*    */   
/*    */   public boolean contains(InclusiveRange<T> subRange) {
/* 56 */     return (subRange.minInclusive().compareTo(this.minInclusive) >= 0 && subRange.maxInclusive
/* 57 */       .compareTo(this.maxInclusive) <= 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public String toString() { return "[" + String.valueOf(this.minInclusive) + ", " + String.valueOf(this.maxInclusive) + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\InclusiveRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */