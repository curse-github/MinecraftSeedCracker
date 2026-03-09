/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import java.util.Collections;
/*     */ import java.util.SequencedSet;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class FuelValues
/*     */ {
/*     */   private final Object2IntSortedMap<Item> values;
/*     */   
/*  24 */   private FuelValues(Object2IntSortedMap<Item> values) { this.values = values; }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public boolean isFuel(ItemStack itemStack) { return this.values.containsKey(itemStack.getItem()); }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public SequencedSet<Item> fuelItems() { return Collections.unmodifiableSequencedSet(this.values.keySet()); }
/*     */ 
/*     */   
/*     */   public int burnDuration(ItemStack itemStack) {
/*  36 */     if (itemStack.isEmpty()) {
/*  37 */       return 0;
/*     */     }
/*     */     
/*  40 */     return this.values.getInt(itemStack.getItem());
/*     */   }
/*     */ 
/*     */   
/*  44 */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) { return vanillaBurnTimes(registries, enabledFeatures, 200); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, int baseUnit) { return (new Builder(registries, enabledFeatures))
/*  49 */       .add(Items.LAVA_BUCKET, baseUnit * 100)
/*  50 */       .add(Blocks.COAL_BLOCK, baseUnit * 8 * 10)
/*  51 */       .add(Items.BLAZE_ROD, baseUnit * 12)
/*  52 */       .add(Items.COAL, baseUnit * 8)
/*  53 */       .add(Items.CHARCOAL, baseUnit * 8)
/*  54 */       .add(ItemTags.LOGS, baseUnit * 3 / 2)
/*  55 */       .add(ItemTags.BAMBOO_BLOCKS, baseUnit * 3 / 2)
/*  56 */       .add(ItemTags.PLANKS, baseUnit * 3 / 2)
/*  57 */       .add(Blocks.BAMBOO_MOSAIC, baseUnit * 3 / 2)
/*  58 */       .add(ItemTags.WOODEN_STAIRS, baseUnit * 3 / 2)
/*  59 */       .add(Blocks.BAMBOO_MOSAIC_STAIRS, baseUnit * 3 / 2)
/*  60 */       .add(ItemTags.WOODEN_SLABS, baseUnit * 3 / 4)
/*  61 */       .add(Blocks.BAMBOO_MOSAIC_SLAB, baseUnit * 3 / 4)
/*  62 */       .add(ItemTags.WOODEN_TRAPDOORS, baseUnit * 3 / 2)
/*  63 */       .add(ItemTags.WOODEN_PRESSURE_PLATES, baseUnit * 3 / 2)
/*  64 */       .add(ItemTags.WOODEN_SHELVES, baseUnit * 3 / 2)
/*  65 */       .add(ItemTags.WOODEN_FENCES, baseUnit * 3 / 2)
/*  66 */       .add(ItemTags.FENCE_GATES, baseUnit * 3 / 2)
/*  67 */       .add(Blocks.NOTE_BLOCK, baseUnit * 3 / 2)
/*  68 */       .add(Blocks.BOOKSHELF, baseUnit * 3 / 2)
/*  69 */       .add(Blocks.CHISELED_BOOKSHELF, baseUnit * 3 / 2)
/*  70 */       .add(Blocks.LECTERN, baseUnit * 3 / 2)
/*  71 */       .add(Blocks.JUKEBOX, baseUnit * 3 / 2)
/*  72 */       .add(Blocks.CHEST, baseUnit * 3 / 2)
/*  73 */       .add(Blocks.TRAPPED_CHEST, baseUnit * 3 / 2)
/*  74 */       .add(Blocks.CRAFTING_TABLE, baseUnit * 3 / 2)
/*  75 */       .add(Blocks.DAYLIGHT_DETECTOR, baseUnit * 3 / 2)
/*  76 */       .add(ItemTags.BANNERS, baseUnit * 3 / 2)
/*  77 */       .add(Items.BOW, baseUnit * 3 / 2)
/*  78 */       .add(Items.FISHING_ROD, baseUnit * 3 / 2)
/*  79 */       .add(Blocks.LADDER, baseUnit * 3 / 2)
/*  80 */       .add(ItemTags.SIGNS, baseUnit)
/*  81 */       .add(ItemTags.HANGING_SIGNS, baseUnit * 4)
/*  82 */       .add(Items.WOODEN_SHOVEL, baseUnit)
/*  83 */       .add(Items.WOODEN_SWORD, baseUnit)
/*  84 */       .add(Items.WOODEN_SPEAR, baseUnit)
/*  85 */       .add(Items.WOODEN_HOE, baseUnit)
/*  86 */       .add(Items.WOODEN_AXE, baseUnit)
/*  87 */       .add(Items.WOODEN_PICKAXE, baseUnit)
/*  88 */       .add(ItemTags.WOODEN_DOORS, baseUnit)
/*  89 */       .add(ItemTags.BOATS, baseUnit * 6)
/*  90 */       .add(ItemTags.WOOL, baseUnit / 2)
/*  91 */       .add(ItemTags.WOODEN_BUTTONS, baseUnit / 2)
/*  92 */       .add(Items.STICK, baseUnit / 2)
/*  93 */       .add(ItemTags.SAPLINGS, baseUnit / 2)
/*  94 */       .add(Items.BOWL, baseUnit / 2)
/*  95 */       .add(ItemTags.WOOL_CARPETS, 1 + baseUnit / 3)
/*  96 */       .add(Blocks.DRIED_KELP_BLOCK, 1 + baseUnit * 20)
/*  97 */       .add(Items.CROSSBOW, baseUnit * 3 / 2)
/*  98 */       .add(Blocks.BAMBOO, baseUnit / 4)
/*  99 */       .add(Blocks.DEAD_BUSH, baseUnit / 2)
/* 100 */       .add(Blocks.SHORT_DRY_GRASS, baseUnit / 2)
/* 101 */       .add(Blocks.TALL_DRY_GRASS, baseUnit / 2)
/* 102 */       .add(Blocks.SCAFFOLDING, baseUnit / 4)
/* 103 */       .add(Blocks.LOOM, baseUnit * 3 / 2)
/* 104 */       .add(Blocks.BARREL, baseUnit * 3 / 2)
/* 105 */       .add(Blocks.CARTOGRAPHY_TABLE, baseUnit * 3 / 2)
/* 106 */       .add(Blocks.FLETCHING_TABLE, baseUnit * 3 / 2)
/* 107 */       .add(Blocks.SMITHING_TABLE, baseUnit * 3 / 2)
/* 108 */       .add(Blocks.COMPOSTER, baseUnit * 3 / 2)
/* 109 */       .add(Blocks.AZALEA, baseUnit / 2)
/* 110 */       .add(Blocks.FLOWERING_AZALEA, baseUnit / 2)
/* 111 */       .add(Blocks.MANGROVE_ROOTS, baseUnit * 3 / 2)
/* 112 */       .add(Blocks.LEAF_LITTER, baseUnit / 2)
/*     */       
/* 114 */       .remove(ItemTags.NON_FLAMMABLE_WOOD)
/* 115 */       .build(); }
/*     */   
/*     */   public static class Builder {
/*     */     private final HolderLookup<Item> items;
/*     */     private final FeatureFlagSet enabledFeatures;
/*     */     private final Object2IntSortedMap<Item> values;
/*     */     
/*     */     public Builder(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) {
/* 123 */       this.values = new Object2IntLinkedOpenHashMap();
/*     */ 
/*     */       
/* 126 */       this.items = registries.lookupOrThrow(Registries.ITEM);
/* 127 */       this.enabledFeatures = enabledFeatures;
/*     */     }
/*     */ 
/*     */     
/* 131 */     public FuelValues build() { return new FuelValues(this.values); }
/*     */ 
/*     */     
/*     */     public Builder remove(TagKey<Item> tag) {
/* 135 */       this.values.keySet().removeIf(item -> item.builtInRegistryHolder().is(tag));
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(TagKey<Item> tag, int time) {
/* 140 */       this.items.get(tag).ifPresent(items -> {
/* 141 */             for (Holder<Item> item : items) {
/* 142 */               putInternal(time, (Item)item.value());
/*     */             }
/*     */           });
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(ItemLike itemLike, int time) {
/* 149 */       Item item = itemLike.asItem();
/* 150 */       putInternal(time, item);
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     private void putInternal(int time, Item item) {
/* 155 */       if (item.isEnabled(this.enabledFeatures))
/* 156 */         this.values.put(item, time); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\FuelValues.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */