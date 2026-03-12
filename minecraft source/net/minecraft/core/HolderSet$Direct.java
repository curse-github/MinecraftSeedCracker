/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends HolderSet.ListBacked<T>
/*     */ {
/*  78 */   private static final Direct<?> EMPTY = new Direct(List.of());
/*     */ 
/*     */   
/*     */   private final List<Holder<T>> contents;
/*     */   
/*     */   private Set<Holder<T>> contentsSet;
/*     */ 
/*     */   
/*  86 */   private Direct(List<Holder<T>> contents) { this.contents = contents; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected List<Holder<T>> contents() { return this.contents; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public boolean isBound() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public Either<TagKey<T>, List<Holder<T>>> unwrap() { return Either.right(this.contents); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public Optional<TagKey<T>> unwrapKey() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Holder<T> value) {
/* 111 */     if (this.contentsSet == null) {
/* 112 */       this.contentsSet = Set.copyOf(this.contents);
/*     */     }
/* 114 */     return this.contentsSet.contains(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public String toString() { return "DirectSet[" + String.valueOf(this.contents) + "]"; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 124 */     if (this == obj) {
/* 125 */       return true;
/*     */     }
/* 127 */     if (obj instanceof Direct) { Direct<?> direct = (Direct)obj; if (this.contents.equals(direct.contents)); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public int hashCode() { return this.contents.hashCode(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderSet$Direct.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */