/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements Codec<ShufflingList.WeightedEntry<E>>
/*     */ {
/*     */   public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  94 */     Dynamic<T> map = new Dynamic<T>(ops, input);
/*     */     
/*  96 */     Objects.requireNonNull(elementCodec); return map.get("data").flatMap(elementCodec::parse)
/*  97 */       .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  98 */       .map(r -> Pair.of(r, ops.empty()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 103 */     return ops.mapBuilder()
/* 104 */       .add("weight", ops.createInt(input.weight))
/* 105 */       .add("data", elementCodec.encodeStart(ops, input.data))
/* 106 */       .build(prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ShufflingList$WeightedEntry$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */