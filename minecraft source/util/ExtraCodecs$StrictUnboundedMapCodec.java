/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.codecs.BaseMapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
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
/*     */ public final class StrictUnboundedMapCodec<K, V>
/*     */   extends Record
/*     */   implements BaseMapCodec<K, V>, Codec<Map<K, V>>
/*     */ {
/*     */   private final Codec<K> keyCodec;
/*     */   private final Codec<V> elementCodec;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #389	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #389	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>; }
/*     */   
/* 389 */   public StrictUnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) { this.keyCodec = keyCodec; this.elementCodec = elementCodec; } public Codec<K> keyCodec() { return this.keyCodec; } public Codec<V> elementCodec() { return this.elementCodec; }
/*     */   
/*     */   public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 392 */     ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
/*     */     
/* 394 */     for (Pair<T, T> pair : input.entries().toList()) {
/* 395 */       DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
/* 396 */       DataResult<V> v = elementCodec().parse(ops, pair.getSecond());
/* 397 */       DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
/* 398 */       Optional<DataResult.Error<Pair<K, V>>> error = entry.error();
/* 399 */       if (error.isPresent()) {
/* 400 */         String errorMessage = ((DataResult.Error)error.get()).message();
/* 401 */         return DataResult.error(() -> {
/* 402 */               if (k.result().isPresent()) {
/* 403 */                 return "Map entry '" + String.valueOf(k.result().get()) + "' : " + errorMessage;
/*     */               }
/* 405 */               return errorMessage;
/*     */             });
/*     */       } 
/*     */       
/* 409 */       if (entry.result().isPresent()) {
/* 410 */         Pair<K, V> kvPair = (Pair)entry.result().get();
/* 411 */         read.put(kvPair.getFirst(), kvPair.getSecond()); continue;
/*     */       } 
/* 413 */       return DataResult.error(() -> "Empty or invalid map contents are not allowed");
/*     */     } 
/*     */     
/* 416 */     ImmutableMap immutableMap = read.build();
/* 417 */     return DataResult.success(immutableMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 422 */   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) { return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 427 */   public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) { return encode(input, ops, ops.mapBuilder()).build(prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 432 */   public String toString() { return "StrictUnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ExtraCodecs$StrictUnboundedMapCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */