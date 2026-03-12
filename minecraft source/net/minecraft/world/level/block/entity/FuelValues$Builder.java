/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final HolderLookup<Item> items;
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   private final Object2IntSortedMap<Item> values;
/*     */   
/*     */   public Builder(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) {
/* 123 */     this.values = new Object2IntLinkedOpenHashMap();
/*     */ 
/*     */     
/* 126 */     this.items = registries.lookupOrThrow(Registries.ITEM);
/* 127 */     this.enabledFeatures = enabledFeatures;
/*     */   }
/*     */ 
/*     */   
/* 131 */   public FuelValues build() { return new FuelValues(this.values); }
/*     */ 
/*     */   
/*     */   public Builder remove(TagKey<Item> tag) {
/* 135 */     this.values.keySet().removeIf(item -> item.builtInRegistryHolder().is(tag));
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(TagKey<Item> tag, int time) {
/* 140 */     this.items.get(tag).ifPresent(items -> {
/* 141 */           for (Holder<Item> item : items) {
/* 142 */             putInternal(time, (Item)item.value());
/*     */           }
/*     */         });
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(ItemLike itemLike, int time) {
/* 149 */     Item item = itemLike.asItem();
/* 150 */     putInternal(time, item);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   private void putInternal(int time, Item item) {
/* 155 */     if (item.isEnabled(this.enabledFeatures))
/* 156 */       this.values.put(item, time); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\FuelValues$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */