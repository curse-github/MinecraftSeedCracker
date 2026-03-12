/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegateRecordBuilder
/*     */   extends Object
/*     */   implements RecordBuilder<T>
/*     */ {
/*     */   private final RecordBuilder<T> original;
/*     */   
/* 307 */   protected DelegateRecordBuilder(RecordBuilder<T> original) { this.original = original; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 312 */   public DynamicOps<T> ops() { return DelegatingOps.this; }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T key, T value) {
/* 317 */     this.original.add(key, value);
/* 318 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 323 */     this.original.add(key, value);
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 329 */     this.original.add(key, value);
/* 330 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(String key, T value) {
/* 335 */     this.original.add(key, value);
/* 336 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(String key, DataResult<T> value) {
/* 341 */     this.original.add(key, value);
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   public <E> RecordBuilder<T> add(String key, E value, Encoder<E> encoder) { return this.original.add(key, encoder.encodeStart(ops(), value)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 353 */     this.original.withErrorsFrom(result);
/* 354 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
/* 359 */     this.original.setLifecycle(lifecycle);
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> mapError(UnaryOperator<String> onError) {
/* 365 */     this.original.mapError(onError);
/* 366 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 371 */   public DataResult<T> build(T prefix) { return this.original.build(prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public DataResult<T> build(DataResult<T> prefix) { return this.original.build(prefix); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\DelegatingOps$DelegateRecordBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */