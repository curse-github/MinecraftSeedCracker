/*     */ package net.minecraft.data.loot;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.SheepPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public abstract class EntityLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   private final FeatureFlagSet allowed;
/*     */   private final FeatureFlagSet required;
/*     */   private final Map<EntityType<?>, Map<ResourceKey<LootTable>, LootTable.Builder>> map;
/*     */   
/*     */   protected final AnyOfCondition.Builder shouldSmeltLoot() {
/*  54 */     HolderLookup.RegistryLookup<Enchantment> enchantmentsRegistry = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  55 */     return AnyOfCondition.anyOf(new LootItemCondition.Builder[] {
/*  56 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/*  57 */             EntityPredicate.Builder.entity()
/*  58 */             .flags(EntityFlagsPredicate.Builder.flags().setOnFire(Boolean.valueOf(true)))), 
/*     */           
/*  60 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_ATTACKER, 
/*     */             
/*  62 */             EntityPredicate.Builder.entity()
/*  63 */             .equipment(EntityEquipmentPredicate.Builder.equipment()
/*  64 */               .mainhand(ItemPredicate.Builder.item()
/*  65 */                 .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentsRegistry.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY)))).build()))))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected EntityLootSubProvider(FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) { this(enabledFeatures, enabledFeatures, registries); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityLootSubProvider(FeatureFlagSet allowed, FeatureFlagSet required, HolderLookup.Provider registries) {
/* 105 */     this.map = Maps.newHashMap();
/*     */     this.allowed = allowed;
/*     */     this.required = required;
/*     */     this.registries = registries;
/*     */   }
/*     */   
/* 111 */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) { generate();
/*     */     
/* 113 */     Set<ResourceKey<LootTable>> seen = new HashSet<ResourceKey<LootTable>>();
/* 114 */     BuiltInRegistries.ENTITY_TYPE.listElements().forEach(holder -> {
/* 115 */           EntityType<?> type = (EntityType)holder.value();
/*     */           
/* 117 */           if (!type.isEnabled(this.allowed)) {
/*     */             return;
/*     */           }
/*     */           
/* 121 */           Optional<ResourceKey<LootTable>> defaultLootTable = type.getDefaultLootTable();
/* 122 */           if (defaultLootTable.isPresent()) {
/* 123 */             Map<ResourceKey<LootTable>, LootTable.Builder> builders = (Map)this.map.remove(type);
/*     */             
/* 125 */             if (type.isEnabled(this.required) && (builders == null || !builders.containsKey(defaultLootTable.get()))) {
/* 126 */               throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { defaultLootTable.get(), holder.key().identifier() }));
/*     */             }
/*     */             
/* 129 */             if (builders != null) {
/* 130 */               builders.forEach(());
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 138 */             Map<ResourceKey<LootTable>, LootTable.Builder> builders = (Map)this.map.remove(type);
/* 139 */             if (builders != null) {
/* 140 */               throw new IllegalStateException(String.format(Locale.ROOT, "Weird loottables '%s' for '%s', not a LivingEntity so should not have loot", new Object[] { builders.keySet().stream().map(()).collect(Collectors.joining(",")), holder.key().identifier() }));
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 145 */     if (!this.map.isEmpty()) {
/* 146 */       throw new IllegalStateException("Created loot tables for entities not supported by datapack: " + String.valueOf(this.map.keySet()));
/*     */     } }
/*     */ 
/*     */   
/*     */   protected LootItemCondition.Builder killedByFrog(HolderGetter<EntityType<?>> entityTypes) {
/* 151 */     return DamageSourceCondition.hasDamageSource(
/* 152 */         DamageSourcePredicate.Builder.damageType().source(
/* 153 */           EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG))); } public static LootPool.Builder createSheepDispatchPool(Map<DyeColor, ResourceKey<LootTable>> tableNames) {
/*     */     AlternativesEntry.Builder variants = AlternativesEntry.alternatives(new net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder[0]);
/*     */     for (Map.Entry<DyeColor, ResourceKey<LootTable>> e : tableNames.entrySet())
/*     */       variants = variants.otherwise(NestedLootTable.lootTableReference((ResourceKey)e.getValue()).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.SHEEP_COLOR, (DyeColor)e.getKey())).build()).subPredicate(SheepPredicate.hasWool())))); 
/*     */     return LootPool.lootPool().add(variants);
/*     */   } protected LootItemCondition.Builder killedByFrogVariant(HolderGetter<EntityType<?>> entityTypes, HolderGetter<FrogVariant> frogVariants, ResourceKey<FrogVariant> variant) {
/* 159 */     return DamageSourceCondition.hasDamageSource(
/* 160 */         DamageSourcePredicate.Builder.damageType().source(
/* 161 */           EntityPredicate.Builder.entity()
/* 162 */           .of(entityTypes, EntityType.FROG)
/* 163 */           .components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariants.getOrThrow(variant))).build())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   protected void add(EntityType<?> type, LootTable.Builder builder) { add(type, (ResourceKey)type.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + String.valueOf(type) + " has no loot table")), builder); }
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, LootTable.Builder builder) { ((Map)this.map.computeIfAbsent(type, k -> new HashMap())).put(lootTable, builder); }
/*     */   
/*     */   public abstract void generate();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\EntityLootSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */