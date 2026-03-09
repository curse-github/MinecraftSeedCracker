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
/*     */ public class WeightedEntry<T>
/*     */   extends Object
/*     */ {
/*     */   private final T data;
/*     */   private final int weight;
/*     */   private double randWeight;
/*     */   
/*     */   private WeightedEntry(T data, int weight) {
/*  65 */     this.weight = weight;
/*  66 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*  70 */   private double getRandWeight() { return this.randWeight; }
/*     */ 
/*     */ 
/*     */   
/*  74 */   private void setRandom(float random) { this.randWeight = -Math.pow(random, (1.0F / this.weight)); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public T getData() { return (T)this.data; }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public int getWeight() { return this.weight; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public String toString() { return "" + this.weight + ":" + this.weight; }
/*     */ 
/*     */   
/*     */   public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> elementCodec) {
/*  91 */     return new Codec<WeightedEntry<E>>()
/*     */       {
/*     */         public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  94 */           Dynamic<T> map = new Dynamic<T>(ops, input);
/*     */           
/*  96 */           Objects.requireNonNull(elementCodec); return map.get("data").flatMap(elementCodec::parse)
/*  97 */             .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  98 */             .map(r -> Pair.of(r, ops.empty()));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 103 */           return ops.mapBuilder()
/* 104 */             .add("weight", ops.createInt(input.weight))
/* 105 */             .add("data", elementCodec.encodeStart(ops, input.data))
/* 106 */             .build(prefix);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ShufflingList$WeightedEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */