/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.criterion.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.criterion.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DistancePredicate;
/*     */ import net.minecraft.advancements.criterion.DistanceTrigger;
/*     */ import net.minecraft.advancements.criterion.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.LootTableTrigger;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.criterion.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VanillaNetherAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/*  69 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*  70 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/*  71 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     AdvancementHolder root = Advancement.Builder.advancement().display(Blocks.RED_NETHER_BRICKS, Component.translatable("advancements.nether.root.title"), Component.translatable("advancements.nether.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/nether"), AdvancementType.TASK, false, false, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(output, "nether/root");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     AdvancementHolder returnToSender = Advancement.Builder.advancement().parent(root).display(Items.FIRE_CHARGE, Component.translatable("advancements.nether.return_to_sender.title"), Component.translatable("advancements.nether.return_to_sender.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.GHAST), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.FIREBALL)))).save(output, "nether/return_to_sender");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     AdvancementHolder findFortress = Advancement.Builder.advancement().parent(root).display(Blocks.NETHER_BRICKS, Component.translatable("advancements.nether.find_fortress.title"), Component.translatable("advancements.nether.find_fortress.description"), null, AdvancementType.TASK, true, true, false).addCriterion("fortress", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.FORTRESS)))).save(output, "nether/find_fortress");
/*     */     
/*  91 */     Advancement.Builder.advancement()
/*  92 */       .parent(root)
/*  93 */       .display(Items.MAP, Component.translatable("advancements.nether.fast_travel.title"), Component.translatable("advancements.nether.fast_travel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/*  94 */       .rewards(AdvancementRewards.Builder.experience(100))
/*  95 */       .addCriterion("travelled", DistanceTrigger.TriggerInstance.travelledThroughNether(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(7000.0D))))
/*  96 */       .save(output, "nether/fast_travel");
/*     */     
/*  98 */     Advancement.Builder.advancement()
/*  99 */       .parent(returnToSender)
/* 100 */       .display(Items.GHAST_TEAR, Component.translatable("advancements.nether.uneasy_alliance.title"), Component.translatable("advancements.nether.uneasy_alliance.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 101 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 102 */       .addCriterion("killed_ghast", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.GHAST).located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))))
/* 103 */       .save(output, "nether/uneasy_alliance");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     AdvancementHolder getWitherSkull = Advancement.Builder.advancement().parent(findFortress).display(Blocks.WITHER_SKELETON_SKULL, Component.translatable("advancements.nether.get_wither_skull.title"), Component.translatable("advancements.nether.get_wither_skull.description"), null, AdvancementType.TASK, true, true, false).addCriterion("wither_skull", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Blocks.WITHER_SKELETON_SKULL })).save(output, "nether/get_wither_skull");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     AdvancementHolder summonWither = Advancement.Builder.advancement().parent(getWitherSkull).display(Items.NETHER_STAR, Component.translatable("advancements.nether.summon_wither.title"), Component.translatable("advancements.nether.summon_wither.description"), null, AdvancementType.TASK, true, true, false).addCriterion("summoned", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.WITHER))).save(output, "nether/summon_wither");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     AdvancementHolder obtainBlazeRod = Advancement.Builder.advancement().parent(findFortress).display(Items.BLAZE_ROD, Component.translatable("advancements.nether.obtain_blaze_rod.title"), Component.translatable("advancements.nether.obtain_blaze_rod.description"), null, AdvancementType.TASK, true, true, false).addCriterion("blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.BLAZE_ROD })).save(output, "nether/obtain_blaze_rod");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     AdvancementHolder createBeacon = Advancement.Builder.advancement().parent(summonWither).display(Blocks.BEACON, Component.translatable("advancements.nether.create_beacon.title"), Component.translatable("advancements.nether.create_beacon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.atLeast(1))).save(output, "nether/create_beacon");
/*     */     
/* 129 */     Advancement.Builder.advancement()
/* 130 */       .parent(createBeacon)
/* 131 */       .display(Blocks.BEACON, Component.translatable("advancements.nether.create_full_beacon.title"), Component.translatable("advancements.nether.create_full_beacon.description"), null, AdvancementType.GOAL, true, true, false)
/* 132 */       .addCriterion("beacon", ConstructBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.exactly(4)))
/* 133 */       .save(output, "nether/create_full_beacon");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     AdvancementHolder brewPotion = Advancement.Builder.advancement().parent(obtainBlazeRod).display(Items.POTION, Component.translatable("advancements.nether.brew_potion.title"), Component.translatable("advancements.nether.brew_potion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("potion", BrewedPotionTrigger.TriggerInstance.brewedPotion()).save(output, "nether/brew_potion");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     AdvancementHolder allPotions = Advancement.Builder.advancement().parent(brewPotion).display(Items.MILK_BUCKET, Component.translatable("advancements.nether.all_potions.title"), Component.translatable("advancements.nether.all_potions.description"), null, AdvancementType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.SLOW_FALLING).and(MobEffects.RESISTANCE).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING))).save(output, "nether/all_potions");
/*     */     
/* 148 */     Advancement.Builder.advancement()
/* 149 */       .parent(allPotions)
/* 150 */       .display(Items.BUCKET, Component.translatable("advancements.nether.all_effects.title"), Component.translatable("advancements.nether.all_effects.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 151 */       .rewards(AdvancementRewards.Builder.experience(1000))
/* 152 */       .addCriterion("all_effects", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(MobEffects.SPEED).and(MobEffects.SLOWNESS).and(MobEffects.STRENGTH).and(MobEffects.JUMP_BOOST).and(MobEffects.REGENERATION).and(MobEffects.FIRE_RESISTANCE).and(MobEffects.WATER_BREATHING).and(MobEffects.INVISIBILITY).and(MobEffects.NIGHT_VISION).and(MobEffects.WEAKNESS).and(MobEffects.POISON).and(MobEffects.WITHER).and(MobEffects.HASTE).and(MobEffects.MINING_FATIGUE).and(MobEffects.LEVITATION).and(MobEffects.GLOWING).and(MobEffects.ABSORPTION).and(MobEffects.HUNGER).and(MobEffects.NAUSEA).and(MobEffects.RESISTANCE).and(MobEffects.SLOW_FALLING).and(MobEffects.CONDUIT_POWER).and(MobEffects.DOLPHINS_GRACE).and(MobEffects.BLINDNESS).and(MobEffects.BAD_OMEN).and(MobEffects.HERO_OF_THE_VILLAGE).and(MobEffects.DARKNESS).and(MobEffects.OOZING).and(MobEffects.INFESTED).and(MobEffects.WIND_CHARGED).and(MobEffects.WEAVING).and(MobEffects.TRIAL_OMEN).and(MobEffects.RAID_OMEN).and(MobEffects.BREATH_OF_THE_NAUTILUS)))
/* 153 */       .save(output, "nether/all_effects");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     AdvancementHolder obtainAncientDebris = Advancement.Builder.advancement().parent(root).display(Items.ANCIENT_DEBRIS, Component.translatable("advancements.nether.obtain_ancient_debris.title"), Component.translatable("advancements.nether.obtain_ancient_debris.description"), null, AdvancementType.TASK, true, true, false).addCriterion("ancient_debris", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.ANCIENT_DEBRIS })).save(output, "nether/obtain_ancient_debris");
/*     */     
/* 161 */     Advancement.Builder.advancement()
/* 162 */       .parent(obtainAncientDebris)
/* 163 */       .display(Items.NETHERITE_CHESTPLATE, Component.translatable("advancements.nether.netherite_armor.title"), Component.translatable("advancements.nether.netherite_armor.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 164 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 165 */       .addCriterion("netherite_armor", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
/* 166 */           })).save(output, "nether/netherite_armor");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     AdvancementHolder obtainCryingObsidian = Advancement.Builder.advancement().parent(root).display(Items.CRYING_OBSIDIAN, Component.translatable("advancements.nether.obtain_crying_obsidian.title"), Component.translatable("advancements.nether.obtain_crying_obsidian.description"), null, AdvancementType.TASK, true, true, false).addCriterion("crying_obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.CRYING_OBSIDIAN })).save(output, "nether/obtain_crying_obsidian");
/*     */     
/* 174 */     Advancement.Builder.advancement()
/* 175 */       .parent(obtainCryingObsidian)
/* 176 */       .display(Items.RESPAWN_ANCHOR, Component.translatable("advancements.nether.charge_respawn_anchor.title"), Component.translatable("advancements.nether.charge_respawn_anchor.description"), null, AdvancementType.TASK, true, true, false)
/* 177 */       .addCriterion("charge_respawn_anchor", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup3, new Block[] { Blocks.RESPAWN_ANCHOR }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RespawnAnchorBlock.CHARGE, 4))), ItemPredicate.Builder.item().of(registryLookup2, new ItemLike[] { Blocks.GLOWSTONE
/* 178 */             }))).save(output, "nether/charge_respawn_anchor");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     AdvancementHolder rideStrider = Advancement.Builder.advancement().parent(root).display(Items.WARPED_FUNGUS_ON_A_STICK, Component.translatable("advancements.nether.ride_strider.title"), Component.translatable("advancements.nether.ride_strider.description"), null, AdvancementType.TASK, true, true, false).addCriterion("used_warped_fungus_on_a_stick", ItemDurabilityTrigger.TriggerInstance.changedDurability(Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.STRIDER)))), Optional.of(ItemPredicate.Builder.item().of(registryLookup2, new ItemLike[] { Items.WARPED_FUNGUS_ON_A_STICK }).build()), MinMaxBounds.Ints.ANY)).save(output, "nether/ride_strider");
/*     */     
/* 191 */     Advancement.Builder.advancement()
/* 192 */       .parent(rideStrider)
/* 193 */       .display(Items.WARPED_FUNGUS_ON_A_STICK, Component.translatable("advancements.nether.ride_strider_in_overworld_lava.title"), Component.translatable("advancements.nether.ride_strider_in_overworld_lava.description"), null, AdvancementType.TASK, true, true, false)
/* 194 */       .addCriterion("ride_entity_distance", DistanceTrigger.TriggerInstance.rideEntityInLava(
/* 195 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.inDimension(Level.OVERWORLD)).vehicle(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.STRIDER)), 
/* 196 */           DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))))
/*     */       
/* 198 */       .save(output, "nether/ride_strider_in_overworld_lava");
/*     */     
/* 200 */     VanillaAdventureAdvancements.addBiomes(Advancement.Builder.advancement(), registries, MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().toList())
/* 201 */       .parent(rideStrider)
/* 202 */       .display(Items.NETHERITE_BOOTS, Component.translatable("advancements.nether.explore_nether.title"), Component.translatable("advancements.nether.explore_nether.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 203 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 204 */       .save(output, "nether/explore_nether");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     AdvancementHolder findBastion = Advancement.Builder.advancement().parent(root).display(Items.POLISHED_BLACKSTONE_BRICKS, Component.translatable("advancements.nether.find_bastion.title"), Component.translatable("advancements.nether.find_bastion.description"), null, AdvancementType.TASK, true, true, false).addCriterion("bastion", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.BASTION_REMNANT)))).save(output, "nether/find_bastion");
/*     */     
/* 212 */     Advancement.Builder.advancement()
/* 213 */       .parent(findBastion)
/* 214 */       .display(Blocks.CHEST, Component.translatable("advancements.nether.loot_bastion.title"), Component.translatable("advancements.nether.loot_bastion.description"), null, AdvancementType.TASK, true, true, false)
/* 215 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 216 */       .addCriterion("loot_bastion_other", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_OTHER))
/* 217 */       .addCriterion("loot_bastion_treasure", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_TREASURE))
/* 218 */       .addCriterion("loot_bastion_hoglin_stable", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_HOGLIN_STABLE))
/* 219 */       .addCriterion("loot_bastion_bridge", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.BASTION_BRIDGE))
/* 220 */       .save(output, "nether/loot_bastion");
/*     */     
/* 222 */     ContextAwarePredicate distractPiglinPlayerArmorPredicate = ContextAwarePredicate.create(new LootItemCondition[] {
/* 223 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of(registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 224 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of(registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 225 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of(registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build(), 
/* 226 */           LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(registryLookup2, ItemTags.PIGLIN_SAFE_ARMOR)))).invert().build()
/*     */         });
/*     */     
/* 229 */     Advancement.Builder.advancement()
/* 230 */       .parent(root)
/* 231 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 232 */       .display(Items.GOLD_INGOT, Component.translatable("advancements.nether.distract_piglin.title"), Component.translatable("advancements.nether.distract_piglin.description"), null, AdvancementType.TASK, true, true, false)
/* 233 */       .addCriterion("distract_piglin", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(distractPiglinPlayerArmorPredicate, 
/*     */           
/* 235 */           Optional.of(ItemPredicate.Builder.item().of(registryLookup2, ItemTags.PIGLIN_LOVED).build()), 
/* 236 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 238 */       .addCriterion("distract_piglin_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 239 */           Optional.of(distractPiglinPlayerArmorPredicate), 
/* 240 */           ItemPredicate.Builder.item().of(registryLookup2, new ItemLike[] { PiglinAi.BARTERING_ITEM
/* 241 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(registryLookup1, EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(false)))))))
/*     */       
/* 243 */       .save(output, "nether/distract_piglin");
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\advancements\packs\VanillaNetherAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */