/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterList<T>
/*     */   extends Object
/*     */ {
/*     */   private final List<Pair<Climate.ParameterPoint, T>> values;
/*     */   private final Climate.RTree<T> index;
/*     */   
/*     */   public static <T> Codec<ParameterList<T>> codec(MapCodec<T> valueCodec) {
/* 282 */     return ExtraCodecs.nonEmptyList(RecordCodecBuilder.create(i -> i.group(Climate.ParameterPoint.CODEC
/* 283 */             .fieldOf("parameters").forGetter(Pair::getFirst), valueCodec
/* 284 */             .forGetter(Pair::getSecond)).apply(i, Pair::of))
/* 285 */         .listOf()).xmap(ParameterList::new, ParameterList::values);
/*     */   }
/*     */   
/*     */   public ParameterList(List<Pair<Climate.ParameterPoint, T>> values) {
/* 289 */     this.values = values;
/* 290 */     this.index = Climate.RTree.create(values);
/*     */   }
/*     */ 
/*     */   
/* 294 */   public List<Pair<Climate.ParameterPoint, T>> values() { return this.values; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   public T findValue(Climate.TargetPoint target) { return (T)findValueIndex(target); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public T findValueBruteForce(Climate.TargetPoint target) {
/* 306 */     Iterator<Pair<Climate.ParameterPoint, T>> iterator = values().iterator();
/*     */ 
/*     */     
/* 309 */     Pair<Climate.ParameterPoint, T> first = (Pair)iterator.next();
/* 310 */     long bestFitness = ((Climate.ParameterPoint)first.getFirst()).fitness(target);
/* 311 */     T best = (T)first.getSecond();
/*     */     
/* 313 */     while (iterator.hasNext()) {
/* 314 */       Pair<Climate.ParameterPoint, T> parameter = (Pair)iterator.next();
/* 315 */       long fitness = ((Climate.ParameterPoint)parameter.getFirst()).fitness(target);
/* 316 */       if (fitness < bestFitness) {
/* 317 */         bestFitness = fitness;
/* 318 */         best = (T)parameter.getSecond();
/*     */       } 
/*     */     } 
/* 321 */     return best;
/*     */   }
/*     */ 
/*     */   
/* 325 */   public T findValueIndex(Climate.TargetPoint target) { return (T)findValueIndex(target, Climate.RTree.Node::distance); }
/*     */ 
/*     */ 
/*     */   
/* 329 */   protected T findValueIndex(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) { return (T)this.index.search(target, distanceMetric); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate$ParameterList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */