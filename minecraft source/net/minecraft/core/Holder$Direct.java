/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Direct<T>
/*     */   extends Record
/*     */   implements Holder<T>
/*     */ {
/*     */   private final T value;
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Holder$Direct;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; }
/*     */   
/*  66 */   public Direct(T value) { this.value = value; } public T value() { return (T)this.value; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/Holder$Direct;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; }
/*     */   
/*  69 */   public boolean isBound() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public boolean is(Identifier key) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean is(ResourceKey<T> key) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean is(TagKey<T> tag) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public boolean is(Holder<T> holder) { return this.value.equals(holder.value()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public boolean is(Predicate<ResourceKey<T>> predicate) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public Either<ResourceKey<T>, T> unwrap() { return Either.right(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public Optional<ResourceKey<T>> unwrapKey() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public Holder.Kind kind() { return Holder.Kind.DIRECT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public String toString() { return "Direct{" + String.valueOf(this.value) + "}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean canSerializeIn(HolderOwner<T> registry) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public Stream<TagKey<T>> tags() { return Stream.of(new TagKey[0]); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Holder$Direct.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */