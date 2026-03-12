/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapDecoder;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FuzzyCodec<T>
/*     */   extends MapCodec<T>
/*     */ {
/*     */   private final Collection<MapCodec<? extends T>> codecs;
/*     */   private final Function<T, ? extends MapEncoder<? extends T>> encoderGetter;
/*     */   
/*     */   public FuzzyCodec(Collection<MapCodec<? extends T>> codecs, Function<T, ? extends MapEncoder<? extends T>> encoderGetter) {
/* 127 */     this.codecs = codecs;
/* 128 */     this.encoderGetter = encoderGetter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> DataResult<T> decode(DynamicOps<S> ops, MapLike<S> input) {
/* 134 */     for (MapDecoder<? extends T> codec : this.codecs) {
/* 135 */       DataResult<? extends T> result = codec.decode(ops, input);
/* 136 */       if (result.result().isPresent()) {
/* 137 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return DataResult.error(() -> "No matching codec found");
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> RecordBuilder<S> encode(T input, DynamicOps<S> ops, RecordBuilder<S> prefix) {
/* 146 */     MapEncoder<T> encoder = (MapEncoder)this.encoderGetter.apply(input);
/* 147 */     return encoder.encode(input, ops, prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public <S> Stream<S> keys(DynamicOps<S> ops) { return this.codecs.stream().flatMap(c -> c.keys(ops)).distinct(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public String toString() { return "FuzzyCodec[" + String.valueOf(this.codecs) + "]"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentSerialization$FuzzyCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */