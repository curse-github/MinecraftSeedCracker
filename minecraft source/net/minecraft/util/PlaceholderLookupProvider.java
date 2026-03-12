/*     */ package net.minecraft.util;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class PlaceholderLookupProvider implements HolderGetter.Provider {
/*     */   private final HolderLookup.Provider context;
/*     */   
/*     */   public PlaceholderLookupProvider(HolderLookup.Provider context) {
/*  24 */     this.lookup = new UniversalLookup();
/*     */     
/*  26 */     this.holders = new HashMap();
/*  27 */     this.holderSets = new HashMap();
/*     */ 
/*     */     
/*  30 */     this.context = context;
/*     */   }
/*     */   private final UniversalLookup lookup; private final Map<ResourceKey<Object>, Holder.Reference<Object>> holders;
/*     */   private final Map<TagKey<Object>, HolderSet.Named<Object>> holderSets;
/*     */   
/*  35 */   public <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return Optional.of(this.lookup.castAsLookup()); }
/*     */ 
/*     */   
/*     */   public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/*  39 */     return RegistryOps.create(parent, new RegistryOps.RegistryInfoLookup()
/*     */         {
/*     */           public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/*  42 */             return PlaceholderLookupProvider.this.context.lookup(registryKey)
/*  43 */               .map(RegistryOps.RegistryInfo::fromRegistryLookup)
/*  44 */               .or(() -> Optional.of(new RegistryOps.RegistryInfo(PlaceholderLookupProvider.this.lookup.castAsOwner(), PlaceholderLookupProvider.this.lookup.castAsLookup(), Lifecycle.experimental())));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public RegistryContextSwapper createSwapper() {
/*  50 */     return new RegistryContextSwapper()
/*     */       {
/*     */         public <T> DataResult<T> swapTo(Codec<T> codec, T value, HolderLookup.Provider newContext) {
/*  53 */           return codec
/*  54 */             .encodeStart(PlaceholderLookupProvider.this.createSerializationContext(JavaOps.INSTANCE), value)
/*  55 */             .flatMap(v -> codec.parse(newContext.createSerializationContext(JavaOps.INSTANCE), v));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*  61 */   public boolean hasRegisteredPlaceholders() { return (!this.holders.isEmpty() || !this.holderSets.isEmpty()); }
/*     */   
/*     */   private class UniversalLookup
/*     */     extends Object
/*     */     implements HolderGetter<Object>, HolderOwner<Object>
/*     */   {
/*  67 */     public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     public Holder.Reference<Object> getOrThrow(ResourceKey<Object> id) { return getOrCreate(id); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     private Holder.Reference<Object> getOrCreate(ResourceKey<Object> id) { return (Holder.Reference)PlaceholderLookupProvider.this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this, k)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     public Optional<HolderSet.Named<Object>> get(TagKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     public HolderSet.Named<Object> getOrThrow(TagKey<Object> id) { return getOrCreate(id); }
/*     */ 
/*     */ 
/*     */     
/*  90 */     private HolderSet.Named<Object> getOrCreate(TagKey<Object> id) { return (HolderSet.Named)PlaceholderLookupProvider.this.holderSets.computeIfAbsent(id, k -> HolderSet.emptyNamed(this, k)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     public <T> HolderGetter<T> castAsLookup() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     public <T> HolderOwner<T> castAsOwner() { return this; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\PlaceholderLookupProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */