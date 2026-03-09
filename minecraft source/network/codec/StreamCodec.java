/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public interface StreamCodec<B, V>
/*     */   extends StreamEncoder<B, V>, StreamDecoder<B, V> {
/*     */   static <B, V> StreamCodec<B, V> of(final StreamEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
/*  23 */     return new StreamCodec<B, V>()
/*     */       {
/*     */         public V decode(B input) {
/*  26 */           return (V)decoder.decode(input);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  31 */         public void encode(B output, V value) { encoder.encode(output, value); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, V> StreamCodec<B, V> ofMember(final StreamMemberEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
/*  40 */     return new StreamCodec<B, V>()
/*     */       {
/*     */         public V decode(B input) {
/*  43 */           return (V)decoder.decode(input);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  48 */         public void encode(B output, V value) { encoder.encode(value, output); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static <B, V> StreamCodec<B, V> unit(final V instance) {
/*  54 */     return new StreamCodec<B, V>()
/*     */       {
/*     */         public V decode(B input) {
/*  57 */           return (V)instance;
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, V value) {
/*  62 */           if (!value.equals(instance)) {
/*  63 */             throw new IllegalStateException("Can't encode '" + String.valueOf(value) + "', expected '" + String.valueOf(instance) + "'");
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   default <O> StreamCodec<B, O> apply(CodecOperation<B, V, O> operation) { return operation.apply(this); }
/*     */ 
/*     */   
/*     */   default <O> StreamCodec<B, O> map(final Function<? super V, ? extends O> to, final Function<? super O, ? extends V> from) {
/*  79 */     return new StreamCodec<B, O>()
/*     */       {
/*     */         public O decode(B input) {
/*  82 */           return (O)to.apply(StreamCodec.this.decode(input));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  87 */         public void encode(B output, O value) { StreamCodec.this.encode(output, from.apply(value)); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   default <O extends ByteBuf> StreamCodec<O, V> mapStream(final Function<O, ? extends B> operation) {
/*  93 */     return new StreamCodec<O, V>()
/*     */       {
/*     */         public V decode(O input) {
/*  96 */           B wrappedStream = (B)operation.apply(input);
/*  97 */           return (V)StreamCodec.this.decode(wrappedStream);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(O output, V value) {
/* 102 */           B wrappedStream = (B)operation.apply(output);
/* 103 */           StreamCodec.this.encode(wrappedStream, value);
/*     */         }
/*     */       };
/*     */   } @FunctionalInterface
/*     */   public static interface CodecOperation<B, S, T> {
/*     */     StreamCodec<B, T> apply(StreamCodec<B, S> param1StreamCodec); } default <U> StreamCodec<B, U> dispatch(final Function<? super U, ? extends V> type, final Function<? super V, ? extends StreamCodec<? super B, ? extends U>> codec) {
/* 109 */     return new StreamCodec<B, U>()
/*     */       {
/*     */         public U decode(B input) {
/* 112 */           V key = (V)StreamCodec.this.decode(input);
/* 113 */           StreamCodec<? super B, ? extends U> valueCodec = (StreamCodec)codec.apply(key);
/* 114 */           return (U)valueCodec.decode(input);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, U value) {
/* 119 */           V key = (V)type.apply(value);
/*     */           
/* 121 */           StreamCodec<B, U> valueCodec = (StreamCodec)codec.apply(key);
/* 122 */           StreamCodec.this.encode(output, key);
/* 123 */           valueCodec.encode(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final Function<T1, C> constructor) {
/* 133 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 136 */           T1 v1 = (T1)codec1.decode(input);
/* 137 */           return (C)constructor.apply(v1);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 142 */         public void encode(B output, C value) { codec1.encode(output, getter1.apply(value)); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final BiFunction<T1, T2, C> constructor) {
/* 152 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 155 */           T1 v1 = (T1)codec1.decode(input);
/* 156 */           T2 v2 = (T2)codec2.decode(input);
/* 157 */           return (C)constructor.apply(v1, v2);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 162 */           codec1.encode(output, getter1.apply(value));
/* 163 */           codec2.encode(output, getter2.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final Function3<T1, T2, T3, C> constructor) {
/* 174 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 177 */           T1 v1 = (T1)codec1.decode(input);
/* 178 */           T2 v2 = (T2)codec2.decode(input);
/* 179 */           T3 v3 = (T3)codec3.decode(input);
/* 180 */           return (C)constructor.apply(v1, v2, v3);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 185 */           codec1.encode(output, getter1.apply(value));
/* 186 */           codec2.encode(output, getter2.apply(value));
/* 187 */           codec3.encode(output, getter3.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final Function4<T1, T2, T3, T4, C> constructor) {
/* 199 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 202 */           T1 v1 = (T1)codec1.decode(input);
/* 203 */           T2 v2 = (T2)codec2.decode(input);
/* 204 */           T3 v3 = (T3)codec3.decode(input);
/* 205 */           T4 v4 = (T4)codec4.decode(input);
/* 206 */           return (C)constructor.apply(v1, v2, v3, v4);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 211 */           codec1.encode(output, getter1.apply(value));
/* 212 */           codec2.encode(output, getter2.apply(value));
/* 213 */           codec3.encode(output, getter3.apply(value));
/* 214 */           codec4.encode(output, getter4.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final Function5<T1, T2, T3, T4, T5, C> constructor) {
/* 227 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 230 */           T1 v1 = (T1)codec1.decode(input);
/* 231 */           T2 v2 = (T2)codec2.decode(input);
/* 232 */           T3 v3 = (T3)codec3.decode(input);
/* 233 */           T4 v4 = (T4)codec4.decode(input);
/* 234 */           T5 v5 = (T5)codec5.decode(input);
/* 235 */           return (C)constructor.apply(v1, v2, v3, v4, v5);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 240 */           codec1.encode(output, getter1.apply(value));
/* 241 */           codec2.encode(output, getter2.apply(value));
/* 242 */           codec3.encode(output, getter3.apply(value));
/* 243 */           codec4.encode(output, getter4.apply(value));
/* 244 */           codec5.encode(output, getter5.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final Function6<T1, T2, T3, T4, T5, T6, C> constructor) {
/* 258 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 261 */           T1 v1 = (T1)codec1.decode(input);
/* 262 */           T2 v2 = (T2)codec2.decode(input);
/* 263 */           T3 v3 = (T3)codec3.decode(input);
/* 264 */           T4 v4 = (T4)codec4.decode(input);
/* 265 */           T5 v5 = (T5)codec5.decode(input);
/* 266 */           T6 v6 = (T6)codec6.decode(input);
/* 267 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 272 */           codec1.encode(output, getter1.apply(value));
/* 273 */           codec2.encode(output, getter2.apply(value));
/* 274 */           codec3.encode(output, getter3.apply(value));
/* 275 */           codec4.encode(output, getter4.apply(value));
/* 276 */           codec5.encode(output, getter5.apply(value));
/* 277 */           codec6.encode(output, getter6.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor) {
/* 292 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 295 */           T1 v1 = (T1)codec1.decode(input);
/* 296 */           T2 v2 = (T2)codec2.decode(input);
/* 297 */           T3 v3 = (T3)codec3.decode(input);
/* 298 */           T4 v4 = (T4)codec4.decode(input);
/* 299 */           T5 v5 = (T5)codec5.decode(input);
/* 300 */           T6 v6 = (T6)codec6.decode(input);
/* 301 */           T7 v7 = (T7)codec7.decode(input);
/* 302 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 307 */           codec1.encode(output, getter1.apply(value));
/* 308 */           codec2.encode(output, getter2.apply(value));
/* 309 */           codec3.encode(output, getter3.apply(value));
/* 310 */           codec4.encode(output, getter4.apply(value));
/* 311 */           codec5.encode(output, getter5.apply(value));
/* 312 */           codec6.encode(output, getter6.apply(value));
/* 313 */           codec7.encode(output, getter7.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> constructor) {
/* 329 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 332 */           T1 v1 = (T1)codec1.decode(input);
/* 333 */           T2 v2 = (T2)codec2.decode(input);
/* 334 */           T3 v3 = (T3)codec3.decode(input);
/* 335 */           T4 v4 = (T4)codec4.decode(input);
/* 336 */           T5 v5 = (T5)codec5.decode(input);
/* 337 */           T6 v6 = (T6)codec6.decode(input);
/* 338 */           T7 v7 = (T7)codec7.decode(input);
/* 339 */           T8 v8 = (T8)codec8.decode(input);
/* 340 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 345 */           codec1.encode(output, getter1.apply(value));
/* 346 */           codec2.encode(output, getter2.apply(value));
/* 347 */           codec3.encode(output, getter3.apply(value));
/* 348 */           codec4.encode(output, getter4.apply(value));
/* 349 */           codec5.encode(output, getter5.apply(value));
/* 350 */           codec6.encode(output, getter6.apply(value));
/* 351 */           codec7.encode(output, getter7.apply(value));
/* 352 */           codec8.encode(output, getter8.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> constructor) {
/* 369 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 372 */           T1 v1 = (T1)codec1.decode(input);
/* 373 */           T2 v2 = (T2)codec2.decode(input);
/* 374 */           T3 v3 = (T3)codec3.decode(input);
/* 375 */           T4 v4 = (T4)codec4.decode(input);
/* 376 */           T5 v5 = (T5)codec5.decode(input);
/* 377 */           T6 v6 = (T6)codec6.decode(input);
/* 378 */           T7 v7 = (T7)codec7.decode(input);
/* 379 */           T8 v8 = (T8)codec8.decode(input);
/* 380 */           T9 v9 = (T9)codec9.decode(input);
/* 381 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 386 */           codec1.encode(output, getter1.apply(value));
/* 387 */           codec2.encode(output, getter2.apply(value));
/* 388 */           codec3.encode(output, getter3.apply(value));
/* 389 */           codec4.encode(output, getter4.apply(value));
/* 390 */           codec5.encode(output, getter5.apply(value));
/* 391 */           codec6.encode(output, getter6.apply(value));
/* 392 */           codec7.encode(output, getter7.apply(value));
/* 393 */           codec8.encode(output, getter8.apply(value));
/* 394 */           codec9.encode(output, getter9.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> constructor) {
/* 412 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 415 */           T1 v1 = (T1)codec1.decode(input);
/* 416 */           T2 v2 = (T2)codec2.decode(input);
/* 417 */           T3 v3 = (T3)codec3.decode(input);
/* 418 */           T4 v4 = (T4)codec4.decode(input);
/* 419 */           T5 v5 = (T5)codec5.decode(input);
/* 420 */           T6 v6 = (T6)codec6.decode(input);
/* 421 */           T7 v7 = (T7)codec7.decode(input);
/* 422 */           T8 v8 = (T8)codec8.decode(input);
/* 423 */           T9 v9 = (T9)codec9.decode(input);
/* 424 */           T10 v10 = (T10)codec10.decode(input);
/* 425 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 430 */           codec1.encode(output, getter1.apply(value));
/* 431 */           codec2.encode(output, getter2.apply(value));
/* 432 */           codec3.encode(output, getter3.apply(value));
/* 433 */           codec4.encode(output, getter4.apply(value));
/* 434 */           codec5.encode(output, getter5.apply(value));
/* 435 */           codec6.encode(output, getter6.apply(value));
/* 436 */           codec7.encode(output, getter7.apply(value));
/* 437 */           codec8.encode(output, getter8.apply(value));
/* 438 */           codec9.encode(output, getter9.apply(value));
/* 439 */           codec10.encode(output, getter10.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11, final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> constructor) {
/* 458 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 461 */           T1 v1 = (T1)codec1.decode(input);
/* 462 */           T2 v2 = (T2)codec2.decode(input);
/* 463 */           T3 v3 = (T3)codec3.decode(input);
/* 464 */           T4 v4 = (T4)codec4.decode(input);
/* 465 */           T5 v5 = (T5)codec5.decode(input);
/* 466 */           T6 v6 = (T6)codec6.decode(input);
/* 467 */           T7 v7 = (T7)codec7.decode(input);
/* 468 */           T8 v8 = (T8)codec8.decode(input);
/* 469 */           T9 v9 = (T9)codec9.decode(input);
/* 470 */           T10 v10 = (T10)codec10.decode(input);
/* 471 */           T11 v11 = (T11)codec11.decode(input);
/* 472 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 477 */           codec1.encode(output, getter1.apply(value));
/* 478 */           codec2.encode(output, getter2.apply(value));
/* 479 */           codec3.encode(output, getter3.apply(value));
/* 480 */           codec4.encode(output, getter4.apply(value));
/* 481 */           codec5.encode(output, getter5.apply(value));
/* 482 */           codec6.encode(output, getter6.apply(value));
/* 483 */           codec7.encode(output, getter7.apply(value));
/* 484 */           codec8.encode(output, getter8.apply(value));
/* 485 */           codec9.encode(output, getter9.apply(value));
/* 486 */           codec10.encode(output, getter10.apply(value));
/* 487 */           codec11.encode(output, getter11.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11, final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12, final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> constructor) {
/* 507 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 510 */           T1 v1 = (T1)codec1.decode(input);
/* 511 */           T2 v2 = (T2)codec2.decode(input);
/* 512 */           T3 v3 = (T3)codec3.decode(input);
/* 513 */           T4 v4 = (T4)codec4.decode(input);
/* 514 */           T5 v5 = (T5)codec5.decode(input);
/* 515 */           T6 v6 = (T6)codec6.decode(input);
/* 516 */           T7 v7 = (T7)codec7.decode(input);
/* 517 */           T8 v8 = (T8)codec8.decode(input);
/* 518 */           T9 v9 = (T9)codec9.decode(input);
/* 519 */           T10 v10 = (T10)codec10.decode(input);
/* 520 */           T11 v11 = (T11)codec11.decode(input);
/* 521 */           T12 v12 = (T12)codec12.decode(input);
/* 522 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 527 */           codec1.encode(output, getter1.apply(value));
/* 528 */           codec2.encode(output, getter2.apply(value));
/* 529 */           codec3.encode(output, getter3.apply(value));
/* 530 */           codec4.encode(output, getter4.apply(value));
/* 531 */           codec5.encode(output, getter5.apply(value));
/* 532 */           codec6.encode(output, getter6.apply(value));
/* 533 */           codec7.encode(output, getter7.apply(value));
/* 534 */           codec8.encode(output, getter8.apply(value));
/* 535 */           codec9.encode(output, getter9.apply(value));
/* 536 */           codec10.encode(output, getter10.apply(value));
/* 537 */           codec11.encode(output, getter11.apply(value));
/* 538 */           codec12.encode(output, getter12.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, T> StreamCodec<B, T> recursive(final UnaryOperator<StreamCodec<B, T>> factory) {
/* 544 */     return new StreamCodec<B, T>()
/*     */       {
/*     */         private final Supplier<StreamCodec<B, T>> inner;
/*     */ 
/*     */         
/* 549 */         public T decode(B input) { return (T)((StreamCodec)this.inner.get()).decode(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 554 */         public void encode(B output, T value) { ((StreamCodec)this.inner.get()).encode(output, value); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 565 */   default <S extends B> StreamCodec<S, V> cast() { return this; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\codec\StreamCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */