/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WeightedList<E>
/*     */   extends Object
/*     */ {
/*     */   private static final int FLAT_THRESHOLD = 64;
/*     */   private final int totalWeight;
/*     */   private final List<Weighted<E>> items;
/*     */   private final Selector<E> selector;
/*     */   
/*     */   private WeightedList(List<? extends Weighted<E>> items) {
/*  28 */     this.items = List.copyOf(items);
/*  29 */     this.totalWeight = WeightedRandom.getTotalWeight(items, Weighted::weight);
/*  30 */     if (this.totalWeight == 0) {
/*  31 */       this.selector = null;
/*  32 */     } else if (this.totalWeight < 64) {
/*  33 */       this.selector = new Flat(this.items, this.totalWeight);
/*     */     } else {
/*  35 */       this.selector = new Compact(this.items);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  40 */   public static <E> WeightedList<E> of() { return new WeightedList(List.of()); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static <E> WeightedList<E> of(E value) { return new WeightedList(List.of(new Weighted(value, 1))); }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*  49 */   public static <E> WeightedList<E> of(Weighted... items) { return new WeightedList(List.of(items)); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static <E> WeightedList<E> of(List<Weighted<E>> items) { return new WeightedList(items); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static <E> Builder<E> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public boolean isEmpty() { return this.items.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public <T> WeightedList<T> map(Function<E, T> mapper) { return new WeightedList(Lists.transform(this.items, e -> e.map(mapper))); }
/*     */ 
/*     */   
/*     */   public Optional<E> getRandom(RandomSource random) {
/*  69 */     if (this.selector == null) {
/*  70 */       return Optional.empty();
/*     */     }
/*  72 */     int selection = random.nextInt(this.totalWeight);
/*  73 */     return Optional.of(this.selector.get(selection));
/*     */   }
/*     */   
/*     */   public E getRandomOrThrow(RandomSource random) {
/*  77 */     if (this.selector == null) {
/*  78 */       throw new IllegalStateException("Weighted list has no elements");
/*     */     }
/*  80 */     int selection = random.nextInt(this.totalWeight);
/*  81 */     return (E)this.selector.get(selection);
/*     */   }
/*     */ 
/*     */   
/*  85 */   public List<Weighted<E>> unwrap() { return this.items; }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static <E> Codec<WeightedList<E>> codec(Codec<E> elementCodec) { return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static <E> Codec<WeightedList<E>> codec(MapCodec<E> elementCodec) { return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> elementCodec) { return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> elementCodec) { return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static <E, B extends io.netty.buffer.ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(StreamCodec<B, E> elementCodec) { return Weighted.streamCodec(elementCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap); }
/*     */ 
/*     */   
/*     */   public boolean contains(E value) {
/* 109 */     for (Weighted<E> item : this.items) {
/* 110 */       if (item.value().equals(value)) {
/* 111 */         return true;
/*     */       }
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 119 */     if (this == obj) {
/* 120 */       return true;
/*     */     }
/* 122 */     if (obj instanceof WeightedList) { WeightedList<?> list = (WeightedList)obj;
/* 123 */       return (this.totalWeight == list.totalWeight && Objects.equals(this.items, list.items)); }
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     result = this.totalWeight;
/* 131 */     return 31 * result + this.items.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends Object
/*     */   {
/* 143 */     private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();
/*     */ 
/*     */     
/* 146 */     public Builder<E> add(E item) { return add(item, 1); }
/*     */ 
/*     */     
/*     */     public Builder<E> add(E item, int weight) {
/* 150 */       this.result.add(new Weighted(item, weight));
/* 151 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 155 */     public WeightedList<E> build() { return new WeightedList(this.result.build()); }
/*     */   }
/*     */   
/*     */   private static class Flat<E>
/*     */     extends Object implements Selector<E> {
/*     */     private final Object[] entries;
/*     */     
/*     */     private Flat(List<Weighted<E>> entries, int totalWeight) {
/* 163 */       this.entries = new Object[totalWeight];
/* 164 */       int i = 0;
/* 165 */       for (Weighted<E> entry : entries) {
/* 166 */         int weight = entry.weight();
/* 167 */         Arrays.fill(this.entries, i, i + weight, entry.value());
/* 168 */         i += weight;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public E get(int selection) { return (E)this.entries[selection]; }
/*     */   }
/*     */   
/*     */   private static class Compact<E>
/*     */     extends Object
/*     */     implements Selector<E> {
/*     */     private final Weighted<?>[] entries;
/*     */     
/* 183 */     private Compact(List<Weighted<E>> entries) { this.entries = (Weighted[])entries.toArray(x$0 -> new Weighted[x$0]); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int selection) {
/* 189 */       for (Weighted<?> entry : this.entries) {
/* 190 */         selection -= entry.weight();
/* 191 */         if (selection < 0) {
/* 192 */           return (E)entry.value();
/*     */         }
/*     */       } 
/* 195 */       throw new IllegalStateException("" + selection + " exceeded total weight");
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface Selector<E> {
/*     */     E get(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\random\WeightedList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */