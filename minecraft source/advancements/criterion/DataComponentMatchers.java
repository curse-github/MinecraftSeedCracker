/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class DataComponentMatchers extends Record implements Predicate<DataComponentGetter> {
/*    */   private final DataComponentExactPredicate exact;
/*    */   private final Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial;
/*    */   
/* 16 */   public DataComponentMatchers(DataComponentExactPredicate exact, Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial) { this.exact = exact; this.partial = partial; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DataComponentMatchers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DataComponentMatchers; } public DataComponentExactPredicate exact() { return this.exact; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DataComponentMatchers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DataComponentMatchers; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DataComponentMatchers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DataComponentMatchers;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial() { return this.partial; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final DataComponentMatchers ANY = new DataComponentMatchers(DataComponentExactPredicate.EMPTY, Map.of());
/*    */   
/* 22 */   public static final MapCodec<DataComponentMatchers> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DataComponentExactPredicate.CODEC
/* 23 */         .optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(DataComponentMatchers::exact), DataComponentPredicate.CODEC
/* 24 */         .optionalFieldOf("predicates", Map.of()).forGetter(DataComponentMatchers::partial))
/* 25 */       .apply(i, DataComponentMatchers::new));
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentMatchers> STREAM_CODEC = StreamCodec.composite(DataComponentExactPredicate.STREAM_CODEC, DataComponentMatchers::exact, DataComponentPredicate.STREAM_CODEC, DataComponentMatchers::partial, DataComponentMatchers::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(DataComponentGetter values) {
/* 35 */     if (!this.exact.test(values)) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     for (DataComponentPredicate predicate : this.partial.values()) {
/* 40 */       if (!predicate.matches(values)) {
/* 41 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public boolean isEmpty() { return (this.exact.isEmpty() && this.partial.isEmpty()); }
/*    */   
/*    */   public static class Builder
/*    */   {
/* 53 */     private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
/* 54 */     private final ImmutableMap.Builder<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     public static Builder components() { return new Builder(); }
/*    */ 
/*    */     
/*    */     public <T extends DataComponentType<?>> Builder any(DataComponentType<?> type) {
/* 64 */       DataComponentPredicate.AnyValueType predicateType = DataComponentPredicate.AnyValueType.create(type);
/* 65 */       this.partial.put(predicateType, predicateType.predicate());
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public <T extends DataComponentPredicate> Builder partial(DataComponentPredicate.Type<T> type, T predicate) {
/* 70 */       this.partial.put(type, predicate);
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public Builder exact(DataComponentExactPredicate exact) {
/* 75 */       this.exact = exact;
/* 76 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 80 */     public DataComponentMatchers build() { return new DataComponentMatchers(this.exact, this.partial.buildOrThrow()); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DataComponentMatchers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */