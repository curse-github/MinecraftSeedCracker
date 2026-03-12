/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface HolderSet<T>
/*     */   extends Iterable<Holder<T>>
/*     */ {
/*     */   Stream<Holder<T>> stream();
/*     */   
/*     */   int size();
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   Either<TagKey<T>, List<Holder<T>>> unwrap();
/*     */   
/*     */   Optional<Holder<T>> getRandomElement(RandomSource paramRandomSource);
/*     */   
/*     */   Holder<T> get(int paramInt);
/*     */   
/*     */   boolean contains(Holder<T> paramHolder);
/*     */   
/*     */   boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   
/*     */   Optional<TagKey<T>> unwrapKey();
/*     */   
/*     */   public static abstract class ListBacked<T>
/*     */     extends Object
/*     */     implements HolderSet<T> {
/*     */     protected abstract List<Holder<T>> contents();
/*     */     
/*  43 */     public int size() { return contents().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     public Spliterator<Holder<T>> spliterator() { return contents().spliterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     public Iterator<Holder<T>> iterator() { return contents().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     public Stream<Holder<T>> stream() { return contents().stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     public Optional<Holder<T>> getRandomElement(RandomSource random) { return Util.getRandomSafe(contents(), random); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     public Holder<T> get(int index) { return (Holder)contents().get(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     public boolean canSerializeIn(HolderOwner<T> owner) { return true; }
/*     */   }
/*     */   
/*     */   public static final class Direct<T>
/*     */     extends ListBacked<T> {
/*  78 */     private static final Direct<?> EMPTY = new Direct(List.of());
/*     */ 
/*     */     
/*     */     private final List<Holder<T>> contents;
/*     */     
/*     */     private Set<Holder<T>> contentsSet;
/*     */ 
/*     */     
/*  86 */     private Direct(List<Holder<T>> contents) { this.contents = contents; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     protected List<Holder<T>> contents() { return this.contents; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     public boolean isBound() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     public Either<TagKey<T>, List<Holder<T>>> unwrap() { return Either.right(this.contents); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     public Optional<TagKey<T>> unwrapKey() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Holder<T> value) {
/* 111 */       if (this.contentsSet == null) {
/* 112 */         this.contentsSet = Set.copyOf(this.contents);
/*     */       }
/* 114 */       return this.contentsSet.contains(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 119 */     public String toString() { return "DirectSet[" + String.valueOf(this.contents) + "]"; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 124 */       if (this == obj) {
/* 125 */         return true;
/*     */       }
/* 127 */       if (obj instanceof Direct) { Direct<?> direct = (Direct)obj; if (this.contents.equals(direct.contents)); }  return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     public int hashCode() { return this.contents.hashCode(); }
/*     */   }
/*     */   
/*     */   public static class Named<T>
/*     */     extends ListBacked<T>
/*     */   {
/*     */     private final HolderOwner<T> owner;
/*     */     private final TagKey<T> key;
/*     */     private List<Holder<T>> contents;
/*     */     
/*     */     Named(HolderOwner<T> owner, TagKey<T> key) {
/* 143 */       this.owner = owner;
/* 144 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/* 148 */     void bind(List<Holder<T>> contents) { this.contents = List.copyOf(contents); }
/*     */ 
/*     */ 
/*     */     
/* 152 */     public TagKey<T> key() { return this.key; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected List<Holder<T>> contents() {
/* 157 */       if (this.contents == null) {
/* 158 */         throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 160 */       return this.contents;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     public boolean isBound() { return (this.contents != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     public Either<TagKey<T>, List<Holder<T>>> unwrap() { return Either.left(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public Optional<TagKey<T>> unwrapKey() { return Optional.of(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     public boolean contains(Holder<T> value) { return value.is(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     public String toString() { return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public boolean canSerializeIn(HolderOwner<T> context) { return this.owner.canSerializeIn(context); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   static <T> Named<T> emptyNamed(HolderOwner<T> owner, TagKey<T> key) {
/* 202 */     return new Named<T>(owner, key)
/*     */       {
/*     */         protected List<Holder<T>> contents() {
/* 205 */           throw new UnsupportedOperationException("Tag " + String.valueOf(key()) + " can't be dereferenced during construction");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 212 */   static <T> HolderSet<T> empty() { return Direct.EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/* 217 */   static <T> Direct<T> direct(Holder... values) { return new Direct(List.of(values)); }
/*     */ 
/*     */ 
/*     */   
/* 221 */   static <T> Direct<T> direct(List<? extends Holder<T>> values) { return new Direct(List.copyOf(values)); }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/* 226 */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, E... elements) { return direct(Stream.of(elements).map(holderGetter).toList()); }
/*     */ 
/*     */ 
/*     */   
/* 230 */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, Collection<E> elements) { return direct(elements.stream().map(holderGetter).toList()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */