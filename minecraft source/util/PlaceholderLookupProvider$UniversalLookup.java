/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UniversalLookup
/*     */   extends Object
/*     */   implements HolderGetter<Object>, HolderOwner<Object>
/*     */ {
/*  67 */   public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public Holder.Reference<Object> getOrThrow(ResourceKey<Object> id) { return getOrCreate(id); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   private Holder.Reference<Object> getOrCreate(ResourceKey<Object> id) { return (Holder.Reference)PlaceholderLookupProvider.this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this, k)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public Optional<HolderSet.Named<Object>> get(TagKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public HolderSet.Named<Object> getOrThrow(TagKey<Object> id) { return getOrCreate(id); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   private HolderSet.Named<Object> getOrCreate(TagKey<Object> id) { return (HolderSet.Named)PlaceholderLookupProvider.this.holderSets.computeIfAbsent(id, k -> HolderSet.emptyNamed(this, k)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public <T> HolderGetter<T> castAsLookup() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public <T> HolderOwner<T> castAsOwner() { return this; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\PlaceholderLookupProvider$UniversalLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */