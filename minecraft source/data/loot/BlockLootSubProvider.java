/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.FlowerPotBlock;
/*     */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.SegmentableBlock;
/*     */ import net.minecraft.world.level.block.SlabBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.storage.loot.IntRange;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public abstract class BlockLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   
/*     */   protected LootItemCondition.Builder hasSilkTouch() {
/*  85 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(
/*  86 */           DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()));
/*     */   }
/*     */   protected final Set<Item> explosionResistant; protected final FeatureFlagSet enabledFeatures;
/*     */   protected final Map<ResourceKey<LootTable>, LootTable.Builder> map;
/*     */   
/*  91 */   protected LootItemCondition.Builder doesNotHaveSilkTouch() { return hasSilkTouch().invert(); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected LootItemCondition.Builder hasShears() { return MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.registries.lookupOrThrow(Registries.ITEM), new ItemLike[] { Items.SHEARS })); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private LootItemCondition.Builder hasShearsOrSilkTouch() { return hasShears().or(hasSilkTouch()); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() { return hasShearsOrSilkTouch().invert(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = { 0.05F, 0.0625F, 0.083333336F, 0.1F };
/* 114 */   private static final float[] NORMAL_LEAVES_STICK_CHANCES = { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
/*     */ 
/*     */   
/* 117 */   protected BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) { this(explosionResistant, enabledFeatures, new HashMap(), registries); }
/*     */ 
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, Map<ResourceKey<LootTable>, LootTable.Builder> map, HolderLookup.Provider registries) {
/* 121 */     this.explosionResistant = explosionResistant;
/* 122 */     this.enabledFeatures = enabledFeatures;
/* 123 */     this.map = map;
/* 124 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike type, FunctionUserBuilder<T> builder) {
/* 128 */     if (!this.explosionResistant.contains(type.asItem())) {
/* 129 */       return (T)builder.apply(ApplyExplosionDecay.explosionDecay());
/*     */     }
/*     */     
/* 132 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder) {
/* 136 */     if (!this.explosionResistant.contains(type.asItem())) {
/* 137 */       return (T)builder.when(ExplosionCondition.survivesExplosion());
/*     */     }
/*     */     
/* 140 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   public LootTable.Builder createSingleItemTable(ItemLike drop) {
/* 144 */     return LootTable.lootTable()
/* 145 */       .withPool((LootPool.Builder)applyExplosionCondition(drop, LootPool.lootPool()
/* 146 */           .setRolls(ConstantValue.exactly(1.0F))
/* 147 */           .add(LootItem.lootTableItem(drop))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LootTable.Builder createSelfDropDispatchTable(Block original, LootItemCondition.Builder condition, LootPoolEntryContainer.Builder<?> entry) {
/* 152 */     return LootTable.lootTable()
/* 153 */       .withPool(LootPool.lootPool()
/* 154 */         .setRolls(ConstantValue.exactly(1.0F))
/* 155 */         .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(original)
/* 156 */           .when(condition))
/* 157 */           .otherwise(entry)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected LootTable.Builder createSilkTouchDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) { return createSelfDropDispatchTable(original, hasSilkTouch(), entry); }
/*     */ 
/*     */ 
/*     */   
/* 167 */   protected LootTable.Builder createShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) { return createSelfDropDispatchTable(original, hasShears(), entry); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) { return createSelfDropDispatchTable(original, hasShearsOrSilkTouch(), entry); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop) { return createSilkTouchDispatchTable(original, (LootPoolEntryContainer.Builder)applyExplosionCondition(original, LootItem.lootTableItem(drop))); }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSingleItemTable(ItemLike drop, NumberProvider count) {
/* 179 */     return LootTable.lootTable()
/* 180 */       .withPool(LootPool.lootPool()
/* 181 */         .setRolls(ConstantValue.exactly(1.0F))
/* 182 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(drop, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(count)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 187 */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop, NumberProvider count) { return createSilkTouchDispatchTable(original, (LootPoolEntryContainer.Builder)applyExplosionDecay(original, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(count)))); }
/*     */ 
/*     */   
/*     */   private LootTable.Builder createSilkTouchOnlyTable(ItemLike drop) {
/* 191 */     return LootTable.lootTable()
/* 192 */       .withPool(LootPool.lootPool()
/* 193 */         .when(hasSilkTouch())
/* 194 */         .setRolls(ConstantValue.exactly(1.0F))
/* 195 */         .add(LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   private LootTable.Builder createPotFlowerItemTable(ItemLike flower) {
/* 200 */     return LootTable.lootTable()
/* 201 */       .withPool((LootPool.Builder)applyExplosionCondition(Blocks.FLOWER_POT, LootPool.lootPool()
/* 202 */           .setRolls(ConstantValue.exactly(1.0F))
/* 203 */           .add(LootItem.lootTableItem(Blocks.FLOWER_POT))))
/*     */       
/* 205 */       .withPool((LootPool.Builder)applyExplosionCondition(flower, LootPool.lootPool()
/* 206 */           .setRolls(ConstantValue.exactly(1.0F))
/* 207 */           .add(LootItem.lootTableItem(flower))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSlabItemTable(Block slab) {
/* 212 */     return LootTable.lootTable()
/* 213 */       .withPool(LootPool.lootPool()
/* 214 */         .setRolls(ConstantValue.exactly(1.0F))
/* 215 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(slab, LootItem.lootTableItem(slab)
/* 216 */             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(
/* 217 */                 LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends Comparable<T> & net.minecraft.util.StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block drop, Property<T> property, T value) {
/* 224 */     return LootTable.lootTable()
/* 225 */       .withPool((LootPool.Builder)applyExplosionCondition(drop, LootPool.lootPool()
/* 226 */           .setRolls(ConstantValue.exactly(1.0F))
/* 227 */           .add(LootItem.lootTableItem(drop)
/* 228 */             .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createNameableBlockEntityTable(Block drop) {
/* 234 */     return LootTable.lootTable()
/* 235 */       .withPool((LootPool.Builder)applyExplosionCondition(drop, LootPool.lootPool()
/* 236 */           .setRolls(ConstantValue.exactly(1.0F))
/* 237 */           .add(LootItem.lootTableItem(drop)
/* 238 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 239 */               .include(DataComponents.CUSTOM_NAME)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShulkerBoxDrop(Block shulkerBox) {
/* 245 */     return LootTable.lootTable()
/* 246 */       .withPool((LootPool.Builder)applyExplosionCondition(shulkerBox, LootPool.lootPool()
/* 247 */           .setRolls(ConstantValue.exactly(1.0F))
/* 248 */           .add(LootItem.lootTableItem(shulkerBox)
/* 249 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 250 */               .include(DataComponents.CUSTOM_NAME)
/* 251 */               .include(DataComponents.CONTAINER)
/* 252 */               .include(DataComponents.LOCK)
/* 253 */               .include(DataComponents.CONTAINER_LOOT)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperOreDrops(Block block) {
/* 260 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 261 */     return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 262 */         applyExplosionDecay(block, LootItem.lootTableItem(Items.RAW_COPPER)
/* 263 */           .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
/* 264 */           .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLapisOreDrops(Block block) {
/* 270 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 271 */     return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 272 */         applyExplosionDecay(block, LootItem.lootTableItem(Items.LAPIS_LAZULI)
/* 273 */           .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))
/* 274 */           .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createRedstoneOreDrops(Block block) {
/* 280 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 281 */     return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 282 */         applyExplosionDecay(block, LootItem.lootTableItem(Items.REDSTONE)
/* 283 */           .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
/* 284 */           .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBannerDrop(Block original) {
/* 290 */     return LootTable.lootTable()
/* 291 */       .withPool((LootPool.Builder)applyExplosionCondition(original, LootPool.lootPool()
/* 292 */           .setRolls(ConstantValue.exactly(1.0F))
/* 293 */           .add(LootItem.lootTableItem(original)
/* 294 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 295 */               .include(DataComponents.CUSTOM_NAME)
/* 296 */               .include(DataComponents.ITEM_NAME)
/* 297 */               .include(DataComponents.TOOLTIP_DISPLAY)
/* 298 */               .include(DataComponents.BANNER_PATTERNS)
/* 299 */               .include(DataComponents.RARITY)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeNestDrop(Block original) {
/* 306 */     return LootTable.lootTable()
/* 307 */       .withPool(LootPool.lootPool()
/* 308 */         .when(hasSilkTouch())
/* 309 */         .setRolls(ConstantValue.exactly(1.0F))
/* 310 */         .add(LootItem.lootTableItem(original)
/* 311 */           .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 312 */             .include(DataComponents.BEES))
/*     */           
/* 314 */           .apply(CopyBlockState.copyState(original).copy(BeehiveBlock.HONEY_LEVEL))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeHiveDrop(Block original) {
/* 320 */     return LootTable.lootTable()
/* 321 */       .withPool(LootPool.lootPool()
/* 322 */         .setRolls(ConstantValue.exactly(1.0F))
/* 323 */         .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(original)
/* 324 */           .when(hasSilkTouch()))
/* 325 */           .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 326 */             .include(DataComponents.BEES))
/*     */           
/* 328 */           .apply(CopyBlockState.copyState(original).copy(BeehiveBlock.HONEY_LEVEL))
/* 329 */           .otherwise(LootItem.lootTableItem(original))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCaveVinesDrop(Block original) {
/* 335 */     return LootTable.lootTable()
/* 336 */       .withPool(LootPool.lootPool()
/* 337 */         .add(LootItem.lootTableItem(Items.GLOW_BERRIES))
/* 338 */         .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(original).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CaveVines.BERRIES, true))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperGolemStatueBlock(Block block) {
/* 343 */     return LootTable.lootTable()
/* 344 */       .withPool((LootPool.Builder)applyExplosionCondition(block, LootPool.lootPool()
/* 345 */           .setRolls(ConstantValue.exactly(1.0F))
/* 346 */           .add(LootItem.lootTableItem(block)
/* 347 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 348 */               .include(DataComponents.CUSTOM_NAME))
/* 349 */             .apply(CopyBlockState.copyState(block).copy(CopperGolemStatueBlock.POSE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOreDrop(Block original, Item drop) {
/* 355 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 356 */     return createSilkTouchDispatchTable(original, (LootPoolEntryContainer.Builder)
/* 357 */         applyExplosionDecay(original, LootItem.lootTableItem(drop)
/* 358 */           .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMushroomBlockDrop(Block original, ItemLike drop) {
/* 364 */     return createSilkTouchDispatchTable(original, (LootPoolEntryContainer.Builder)applyExplosionDecay(original, LootItem.lootTableItem(drop)
/* 365 */           .apply(SetItemCountFunction.setCount(UniformGenerator.between(-6.0F, 2.0F)))
/* 366 */           .apply(LimitCount.limitCount(IntRange.lowerBound(0)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createGrassDrops(Block original) {
/* 372 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 373 */     return createShearsDispatchTable(original, (LootPoolEntryContainer.Builder)applyExplosionDecay(original, ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.WHEAT_SEEDS)
/* 374 */           .when(LootItemRandomChanceCondition.randomChance(0.125F)))
/* 375 */           .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 2))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createStemDrops(Block block, Item drop) {
/* 381 */     return LootTable.lootTable()
/* 382 */       .withPool((LootPool.Builder)applyExplosionDecay(block, LootPool.lootPool()
/* 383 */           .setRolls(ConstantValue.exactly(1.0F))
/* 384 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)
/* 385 */             .apply(StemBlock.AGE.getPossibleValues(), age -> SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, (age.intValue() + 1) / 15.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, age.intValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createAttachedStemDrops(Block block, Item drop) {
/* 391 */     return LootTable.lootTable()
/* 392 */       .withPool((LootPool.Builder)applyExplosionDecay(block, LootPool.lootPool()
/* 393 */           .setRolls(ConstantValue.exactly(1.0F))
/* 394 */           .add(LootItem.lootTableItem(drop)
/* 395 */             .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOnlyDrop(ItemLike drop) {
/* 401 */     return LootTable.lootTable()
/* 402 */       .withPool(LootPool.lootPool()
/* 403 */         .setRolls(ConstantValue.exactly(1.0F))
/* 404 */         .when(hasShears())
/* 405 */         .add(LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike drop) {
/* 410 */     return LootTable.lootTable()
/* 411 */       .withPool(LootPool.lootPool()
/* 412 */         .setRolls(ConstantValue.exactly(1.0F))
/* 413 */         .when(hasShearsOrSilkTouch())
/* 414 */         .add(LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block, LootItemCondition.Builder condition) {
/* 419 */     return LootTable.lootTable()
/* 420 */       .withPool(LootPool.lootPool()
/* 421 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, (
/* 422 */             (LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(block)
/* 423 */             .when(condition))
/* 424 */             .apply(Direction.values(), dir -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(dir), true)))))
/* 425 */             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block) {
/* 431 */     return LootTable.lootTable()
/* 432 */       .withPool(LootPool.lootPool()
/* 433 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, (
/* 434 */             (LootPoolSingletonContainer.Builder)LootItem.lootTableItem(block)
/* 435 */             .apply(Direction.values(), dir -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(dir), true)))))
/* 436 */             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMossyCarpetBlockDrops(Block block) {
/* 442 */     return LootTable.lootTable()
/* 443 */       .withPool(LootPool.lootPool()
/* 444 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, 
/* 445 */             (FunctionUserBuilder)LootItem.lootTableItem(block)
/* 446 */             .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MossyCarpetBlock.BASE, true))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 452 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 453 */     return createSilkTouchOrShearsDispatchTable(original, ((LootPoolSingletonContainer.Builder)
/* 454 */         applyExplosionCondition(original, LootItem.lootTableItem(sapling)))
/* 455 */         .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), saplingChances)))
/*     */       
/* 457 */       .withPool(LootPool.lootPool()
/* 458 */         .setRolls(ConstantValue.exactly(1.0F))
/* 459 */         .when(doesNotHaveShearsOrSilkTouch())
/* 460 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionDecay(original, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/* 461 */           .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOakLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 467 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 468 */     return 
/* 469 */       createLeavesDrops(original, sapling, saplingChances)
/* 470 */       .withPool(LootPool.lootPool()
/* 471 */         .setRolls(ConstantValue.exactly(1.0F))
/* 472 */         .when(doesNotHaveShearsOrSilkTouch())
/* 473 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition(original, LootItem.lootTableItem(Items.APPLE)))
/* 474 */           .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F }))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMangroveLeavesDrops(Block block) {
/* 480 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 481 */     return createSilkTouchOrShearsDispatchTable(block, ((LootPoolSingletonContainer.Builder)
/* 482 */         applyExplosionDecay(Blocks.MANGROVE_LEAVES, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
/* 483 */         .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCropDrops(Block original, Item cropDrop, Item seedDrop, LootItemCondition.Builder isMaxAge) {
/* 488 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 489 */     return (LootTable.Builder)applyExplosionDecay(original, LootTable.lootTable()
/* 490 */         .withPool(LootPool.lootPool()
/* 491 */           .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(cropDrop)
/* 492 */             .when(isMaxAge))
/* 493 */             .otherwise(LootItem.lootTableItem(seedDrop))))
/*     */ 
/*     */         
/* 496 */         .withPool(LootPool.lootPool()
/* 497 */           .when(isMaxAge)
/* 498 */           .add(LootItem.lootTableItem(seedDrop).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createDoublePlantShearsDrop(Block block) {
/* 504 */     return LootTable.lootTable().withPool(LootPool.lootPool()
/* 505 */         .when(hasShears())
/* 506 */         .add(LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoublePlantWithSeedDrops(Block block, Block drop) {
/* 510 */     HolderLookup.RegistryLookup<Block> blocks = this.registries.lookupOrThrow(Registries.BLOCK);
/*     */ 
/*     */ 
/*     */     
/* 514 */     AlternativesEntry.Builder builder = ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).when(hasShears())).otherwise(((LootPoolSingletonContainer.Builder)applyExplosionCondition(block, LootItem.lootTableItem(Items.WHEAT_SEEDS)))
/* 515 */         .when(LootItemRandomChanceCondition.randomChance(0.125F)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     return LootTable.lootTable()
/* 521 */       .withPool(
/* 522 */         LootPool.lootPool()
/* 523 */         .add(builder)
/* 524 */         .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
/* 525 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0))))
/*     */       
/* 527 */       .withPool(
/* 528 */         LootPool.lootPool()
/* 529 */         .add(builder)
/* 530 */         .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))
/* 531 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCandleDrops(Block block) {
/* 536 */     return LootTable.lootTable()
/* 537 */       .withPool(LootPool.lootPool()
/* 538 */         .setRolls(ConstantValue.exactly(1.0F))
/* 539 */         .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, LootItem.lootTableItem(block)
/* 540 */             .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), count -> SetItemCountFunction.setCount(ConstantValue.exactly(count.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, count.intValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createSegmentedBlockDrops(Block block) {
/* 546 */     if (block instanceof SegmentableBlock) { SegmentableBlock segmentableBlock = (SegmentableBlock)block;
/* 547 */       return LootTable.lootTable()
/* 548 */         .withPool(LootPool.lootPool()
/* 549 */           .setRolls(ConstantValue.exactly(1.0F))
/* 550 */           .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, LootItem.lootTableItem(block)
/* 551 */               .apply(IntStream.rangeClosed(1, 4).boxed().toList(), count -> SetItemCountFunction.setCount(ConstantValue.exactly(count.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(segmentableBlock.getSegmentAmountProperty(), count.intValue()))))))); }
/*     */ 
/*     */ 
/*     */     
/* 555 */     return noDrop();
/*     */   }
/*     */   
/*     */   protected static LootTable.Builder createCandleCakeDrops(Block candle) {
/* 559 */     return LootTable.lootTable()
/* 560 */       .withPool(LootPool.lootPool()
/* 561 */         .setRolls(ConstantValue.exactly(1.0F))
/* 562 */         .add(LootItem.lootTableItem(candle)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 567 */   public static LootTable.Builder noDrop() { return LootTable.lootTable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 574 */     generate();
/*     */     
/* 576 */     Set<ResourceKey<LootTable>> seen = new HashSet<ResourceKey<LootTable>>();
/* 577 */     for (Iterator iterator = BuiltInRegistries.BLOCK.iterator(); iterator.hasNext(); ) { Block block = (Block)iterator.next();
/* 578 */       if (!block.isEnabled(this.enabledFeatures)) {
/*     */         continue;
/*     */       }
/* 581 */       block.getLootTable().ifPresent(lootTable -> {
/* 582 */             if (seen.add(lootTable)) {
/* 583 */               LootTable.Builder builder = (LootTable.Builder)this.map.remove(lootTable);
/* 584 */               if (builder == null) {
/* 585 */                 throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { lootTable.identifier(), BuiltInRegistries.BLOCK.getKey(block) }));
/*     */               }
/* 587 */               output.accept(lootTable, builder);
/*     */             } 
/*     */           }); }
/*     */ 
/*     */     
/* 592 */     if (!this.map.isEmpty()) {
/* 593 */       throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet()));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addNetherVinesDropTable(Block vineBlock, Block plantBlock) {
/* 598 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 599 */     LootTable.Builder builder = createSilkTouchOrShearsDispatchTable(vineBlock, 
/* 600 */         LootItem.lootTableItem(vineBlock).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.33F, 0.55F, 0.77F, 1.0F })));
/* 601 */     add(vineBlock, builder);
/* 602 */     add(plantBlock, builder);
/*     */   }
/*     */ 
/*     */   
/* 606 */   protected LootTable.Builder createDoorTable(Block block) { return createSinglePropConditionTable(block, DoorBlock.HALF, DoubleBlockHalf.LOWER); }
/*     */ 
/*     */ 
/*     */   
/* 610 */   protected void dropPottedContents(Block potted) { add(potted, block -> createPotFlowerItemTable(((FlowerPotBlock)block).getPotted())); }
/*     */ 
/*     */ 
/*     */   
/* 614 */   protected void otherWhenSilkTouch(Block block, Block other) { add(block, createSilkTouchOnlyTable(other)); }
/*     */ 
/*     */ 
/*     */   
/* 618 */   protected void dropOther(Block block, ItemLike drop) { add(block, createSingleItemTable(drop)); }
/*     */ 
/*     */ 
/*     */   
/* 622 */   protected void dropWhenSilkTouch(Block block) { otherWhenSilkTouch(block, block); }
/*     */ 
/*     */ 
/*     */   
/* 626 */   protected void dropSelf(Block block) { dropOther(block, block); }
/*     */ 
/*     */ 
/*     */   
/* 630 */   protected void add(Block block, Function<Block, LootTable.Builder> builder) { add(block, (LootTable.Builder)builder.apply(block)); }
/*     */ 
/*     */ 
/*     */   
/* 634 */   protected void add(Block block, LootTable.Builder builder) { this.map.put((ResourceKey)block.getLootTable().orElseThrow(() -> new IllegalStateException("Block " + String.valueOf(block) + " does not have loot table")), builder); }
/*     */   
/*     */   protected abstract void generate();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\BlockLootSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */