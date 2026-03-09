/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.ListBuilder;
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
/*     */ public class DelegateListBuilder
/*     */   extends Object
/*     */   implements ListBuilder<T>
/*     */ {
/*     */   private final ListBuilder<T> original;
/*     */   
/* 235 */   protected DelegateListBuilder(ListBuilder<T> original) { this.original = original; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 240 */   public DynamicOps<T> ops() { return DelegatingOps.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   public DataResult<T> build(T prefix) { return this.original.build(prefix); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListBuilder<T> add(T value) {
/* 250 */     this.original.add(value);
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<T> add(DataResult<T> value) {
/* 256 */     this.original.add(value);
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
/* 263 */     this.original.add(encoder.encodeStart(ops(), value));
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
/* 270 */     values.forEach(v -> this.original.add(encoder.encode(v, ops(), ops().empty())));
/* 271 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 276 */     this.original.withErrorsFrom(result);
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 282 */     this.original.mapError(onError);
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 288 */   public DataResult<T> build(DataResult<T> prefix) { return this.original.build(prefix); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\DelegatingOps$DelegateListBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */