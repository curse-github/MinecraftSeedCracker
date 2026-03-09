/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Holder<T>
/*     */ {
/*  55 */   default String getRegisteredName() { return (String)unwrapKey().map(key -> key.identifier().toString()).orElse("[unregistered]"); }
/*     */   
/*     */   public enum Kind
/*     */   {
/*  59 */     REFERENCE, DIRECT; } T value(); boolean isBound(); boolean is(Identifier paramIdentifier);
/*     */   boolean is(ResourceKey<T> paramResourceKey);
/*     */   boolean is(Predicate<ResourceKey<T>> paramPredicate);
/*     */   boolean is(TagKey<T> paramTagKey);
/*  63 */   static <T> Holder<T> direct(T value) { return new Direct(value); } @Deprecated
/*     */   boolean is(Holder<T> paramHolder); Stream<TagKey<T>> tags(); Either<ResourceKey<T>, T> unwrap(); Optional<ResourceKey<T>> unwrapKey(); Kind kind(); boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   public static final class Direct<T> extends Record implements Holder<T> { private final T value;
/*  66 */     public Direct(T value) { this.value = value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Holder$Direct;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/Holder$Direct;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  66 */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; } public T value() { return (T)this.value; }
/*     */ 
/*     */     
/*  69 */     public boolean isBound() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     public boolean is(Identifier key) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     public boolean is(ResourceKey<T> key) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     public boolean is(TagKey<T> tag) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     public boolean is(Holder<T> holder) { return this.value.equals(holder.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     public boolean is(Predicate<ResourceKey<T>> predicate) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     public Either<ResourceKey<T>, T> unwrap() { return Either.right(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     public Optional<ResourceKey<T>> unwrapKey() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     public Holder.Kind kind() { return Holder.Kind.DIRECT; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     public String toString() { return "Direct{" + String.valueOf(this.value) + "}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     public boolean canSerializeIn(HolderOwner<T> registry) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     public Stream<TagKey<T>> tags() { return Stream.of(new TagKey[0]); } }
/*     */   
/*     */   public static class Reference<T> extends Object implements Holder<T> { private final HolderOwner<T> owner;
/*     */     private Set<TagKey<T>> tags;
/*     */     private final Type type;
/*     */     private ResourceKey<T> key;
/*     */     private T value;
/*     */     
/*     */     protected enum Type {
/* 133 */       STAND_ALONE, INTRUSIVE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Reference(Type type, HolderOwner<T> owner, ResourceKey<T> key, T value) {
/* 146 */       this.owner = owner;
/* 147 */       this.type = type;
/* 148 */       this.key = key;
/* 149 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     public static <T> Reference<T> createStandAlone(HolderOwner<T> owner, ResourceKey<T> key) { return new Reference(Type.STAND_ALONE, owner, key, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/* 165 */     public static <T> Reference<T> createIntrusive(HolderOwner<T> owner, T value) { return new Reference(Type.INTRUSIVE, owner, null, value); }
/*     */ 
/*     */     
/*     */     public ResourceKey<T> key() {
/* 169 */       if (this.key == null) {
/* 170 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.value) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 172 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public T value() {
/* 177 */       if (this.value == null) {
/* 178 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 180 */       return (T)this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     public boolean is(Identifier key) { return key().identifier().equals(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public boolean is(ResourceKey<T> key) { return (key() == key); }
/*     */ 
/*     */     
/*     */     private Set<TagKey<T>> boundTags() {
/* 194 */       if (this.tags == null) {
/* 195 */         throw new IllegalStateException("Tags not bound");
/*     */       }
/* 197 */       return this.tags;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 202 */     public boolean is(TagKey<T> tag) { return boundTags().contains(tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     public boolean is(Holder<T> holder) { return holder.is(key()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     public boolean is(Predicate<ResourceKey<T>> predicate) { return predicate.test(key()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     public boolean canSerializeIn(HolderOwner<T> context) { return this.owner.canSerializeIn(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     public Either<ResourceKey<T>, T> unwrap() { return Either.left(key()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     public Optional<ResourceKey<T>> unwrapKey() { return Optional.of(key()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     public Holder.Kind kind() { return Holder.Kind.REFERENCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     public boolean isBound() { return (this.key != null && this.value != null); }
/*     */ 
/*     */     
/*     */     void bindKey(ResourceKey<T> key) {
/* 241 */       if (this.key != null && key != this.key) {
/* 242 */         throw new IllegalStateException("Can't change holder key: existing=" + String.valueOf(this.key) + ", new=" + String.valueOf(key));
/*     */       }
/* 244 */       this.key = key;
/*     */     }
/*     */     
/*     */     protected void bindValue(T value) {
/* 248 */       if (this.type == Type.INTRUSIVE && this.value != value) {
/* 249 */         throw new IllegalStateException("Can't change holder " + String.valueOf(this.key) + " value: existing=" + String.valueOf(this.value) + ", new=" + String.valueOf(value));
/*     */       }
/* 251 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/* 255 */     void bindTags(Collection<TagKey<T>> tags) { this.tags = Set.copyOf(tags); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     public Stream<TagKey<T>> tags() { return boundTags().stream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     public String toString() { return "Reference{" + String.valueOf(this.key) + "=" + String.valueOf(this.value) + "}"; } }
/*     */ 
/*     */   
/*     */   protected enum Type {
/*     */     STAND_ALONE, INTRUSIVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Holder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */