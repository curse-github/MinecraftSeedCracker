/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Named<T>
/*     */   extends HolderSet.ListBacked<T>
/*     */ {
/*     */   private final HolderOwner<T> owner;
/*     */   private final TagKey<T> key;
/*     */   private List<Holder<T>> contents;
/*     */   
/*     */   Named(HolderOwner<T> owner, TagKey<T> key) {
/* 143 */     this.owner = owner;
/* 144 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/* 148 */   void bind(List<Holder<T>> contents) { this.contents = List.copyOf(contents); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public TagKey<T> key() { return this.key; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Holder<T>> contents() {
/* 157 */     if (this.contents == null) {
/* 158 */       throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */     }
/* 160 */     return this.contents;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public boolean isBound() { return (this.contents != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public Either<TagKey<T>, List<Holder<T>>> unwrap() { return Either.left(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public Optional<TagKey<T>> unwrapKey() { return Optional.of(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   public boolean contains(Holder<T> value) { return value.is(this.key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   public String toString() { return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public boolean canSerializeIn(HolderOwner<T> context) { return this.owner.canSerializeIn(context); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\HolderSet$Named.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */