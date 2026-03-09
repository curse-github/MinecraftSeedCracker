/*    */ package net.minecraft.server.network;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class Filterable<T> extends Record {
/*    */   private final T raw;
/*    */   private final Optional<T> filtered;
/*    */   
/* 12 */   public Filterable(T raw, Optional<T> filtered) { this.raw = raw; this.filtered = filtered; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/Filterable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public T raw() { return (T)this.raw; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/Filterable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/Filterable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public Optional<T> filtered() { return this.filtered; }
/*    */   public static <T> Codec<Filterable<T>> codec(Codec<T> valueCodec) {
/* 14 */     Codec<Filterable<T>> fullCodec = RecordCodecBuilder.create(i -> i.group(valueCodec
/* 15 */           .fieldOf("raw").forGetter(Filterable::raw), valueCodec
/* 16 */           .optionalFieldOf("filtered").forGetter(Filterable::filtered))
/* 17 */         .apply(i, Filterable::new));
/* 18 */     Codec<Filterable<T>> simpleCodec = valueCodec.xmap(Filterable::passThrough, Filterable::raw);
/* 19 */     return Codec.withAlternative(fullCodec, simpleCodec);
/*    */   }
/*    */   
/*    */   public static <B extends io.netty.buffer.ByteBuf, T> StreamCodec<B, Filterable<T>> streamCodec(StreamCodec<B, T> valueCodec) {
/* 23 */     return StreamCodec.composite(valueCodec, Filterable::raw, valueCodec
/*    */         
/* 25 */         .apply(ByteBufCodecs::optional), Filterable::filtered, Filterable::new);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static <T> Filterable<T> passThrough(T value) { return new Filterable(value, Optional.empty()); }
/*    */ 
/*    */   
/*    */   public static Filterable<String> from(FilteredText text) {
/* 35 */     return new Filterable(text
/* 36 */         .raw(), 
/* 37 */         text.isFiltered() ? Optional.of(text.filteredOrEmpty()) : Optional.empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(boolean filterEnabled) {
/* 42 */     if (filterEnabled) {
/* 43 */       return (T)this.filtered.orElse(this.raw);
/*    */     }
/* 45 */     return (T)this.raw;
/*    */   }
/*    */   
/*    */   public <U> Filterable<U> map(Function<T, U> function) {
/* 49 */     return new Filterable(function
/* 50 */         .apply(this.raw), this.filtered
/* 51 */         .map(function));
/*    */   }
/*    */ 
/*    */   
/*    */   public <U> Optional<Filterable<U>> resolve(Function<T, Optional<U>> function) {
/* 56 */     Optional<U> newRaw = (Optional)function.apply(this.raw);
/* 57 */     if (newRaw.isEmpty()) {
/* 58 */       return Optional.empty();
/*    */     }
/* 60 */     if (this.filtered.isPresent()) {
/* 61 */       Optional<U> newFiltered = (Optional)function.apply(this.filtered.get());
/* 62 */       if (newFiltered.isEmpty()) {
/* 63 */         return Optional.empty();
/*    */       }
/* 65 */       return Optional.of(new Filterable(newRaw.get(), newFiltered));
/*    */     } 
/* 67 */     return Optional.of(new Filterable(newRaw.get(), Optional.empty()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\Filterable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */