/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
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
/*     */ class StrictEither<T>
/*     */   extends MapCodec<T>
/*     */ {
/*     */   private final String typeFieldName;
/*     */   private final MapCodec<T> typed;
/*     */   private final MapCodec<T> fuzzy;
/*     */   
/*     */   public StrictEither(String typeFieldName, MapCodec<T> typed, MapCodec<T> fuzzy) {
/*  98 */     this.typeFieldName = typeFieldName;
/*  99 */     this.typed = typed;
/* 100 */     this.fuzzy = fuzzy;
/*     */   }
/*     */ 
/*     */   
/*     */   public <O> DataResult<T> decode(DynamicOps<O> ops, MapLike<O> input) {
/* 105 */     if (input.get(this.typeFieldName) != null) {
/* 106 */       return this.typed.decode(ops, input);
/*     */     }
/* 108 */     return this.fuzzy.decode(ops, input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public <O> RecordBuilder<O> encode(T input, DynamicOps<O> ops, RecordBuilder<O> prefix) { return this.fuzzy.encode(input, ops, prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public <T1> Stream<T1> keys(DynamicOps<T1> ops) { return Stream.concat(this.typed.keys(ops), this.fuzzy.keys(ops)).distinct(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentSerialization$StrictEither.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */