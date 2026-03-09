/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends RegistrySetBuilder.EmptyTagLookup<Object>
/*     */ {
/*  99 */   private final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap();
/*     */ 
/*     */   
/* 102 */   public UniversalLookup(HolderOwner<Object> owner) { super(owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private <T> Holder.Reference<T> getOrCreate(ResourceKey<T> id) { return (Holder.Reference)this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this.owner, k)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$UniversalLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */