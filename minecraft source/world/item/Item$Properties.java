/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.Consumables;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.ArmorMaterial;
/*     */ import net.minecraft.world.item.equipment.ArmorType;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Properties
/*     */ {
/* 174 */   private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID = id -> Util.makeDescriptionId("block", id.identifier());
/* 175 */   private static final DependantName<Item, String> ITEM_DESCRIPTION_ID = id -> Util.makeDescriptionId("item", id.identifier());
/*     */   
/* 177 */   private final DataComponentMap.Builder components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS);
/*     */   private Item craftingRemainingItem;
/* 179 */   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   private ResourceKey<Item> id;
/* 181 */   private DependantName<Item, String> descriptionId = ITEM_DESCRIPTION_ID;
/* 182 */   private final DependantName<Item, Identifier> model = ResourceKey::identifier;
/*     */ 
/*     */   
/* 185 */   public Properties food(FoodProperties foodProperties) { return food(foodProperties, Consumables.DEFAULT_FOOD); }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public Properties food(FoodProperties foodProperties, Consumable consumable) { return component(DataComponents.FOOD, foodProperties).component(DataComponents.CONSUMABLE, consumable); }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public Properties usingConvertsTo(Item item) { return component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStack(item))); }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public Properties useCooldown(float seconds) { return component(DataComponents.USE_COOLDOWN, new UseCooldown(seconds)); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public Properties stacksTo(int max) { return component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(max)); }
/*     */ 
/*     */   
/*     */   public Properties durability(int maxDamage) {
/* 205 */     component(DataComponents.MAX_DAMAGE, Integer.valueOf(maxDamage));
/* 206 */     component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1));
/* 207 */     component(DataComponents.DAMAGE, Integer.valueOf(0));
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   public Properties craftRemainder(Item craftingRemainingItem) {
/* 212 */     this.craftingRemainingItem = craftingRemainingItem;
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 217 */   public Properties rarity(Rarity rarity) { return component(DataComponents.RARITY, rarity); }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public Properties fireResistant() { return component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE)); }
/*     */ 
/*     */ 
/*     */   
/* 225 */   public Properties jukeboxPlayable(ResourceKey<JukeboxSong> song) { return component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder(song))); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public Properties enchantable(int value) { return component(DataComponents.ENCHANTABLE, new Enchantable(value)); }
/*     */ 
/*     */ 
/*     */   
/* 233 */   public Properties repairable(Item repairItem) { return component(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(new Holder[] { repairItem.builtInRegistryHolder() }))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties repairable(TagKey<Item> repairItems) {
/* 238 */     HolderGetter<Item> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
/* 239 */     return component(DataComponents.REPAIRABLE, new Repairable(registrationLookup.getOrThrow(repairItems)));
/*     */   }
/*     */ 
/*     */   
/* 243 */   public Properties equippable(EquipmentSlot slot) { return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).build()); }
/*     */ 
/*     */ 
/*     */   
/* 247 */   public Properties equippableUnswappable(EquipmentSlot slot) { return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).setSwappable(false).build()); }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public Properties tool(ToolMaterial material, TagKey<Block> minesEfficiently, float attackDamageBaseline, float attackSpeedBaseline, float disableBlockingSeconds) { return material.applyToolProperties(this, minesEfficiently, attackDamageBaseline, attackSpeedBaseline, disableBlockingSeconds); }
/*     */ 
/*     */ 
/*     */   
/* 255 */   public Properties pickaxe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 259 */   public Properties axe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_AXE, attackDamageBaseline, attackSpeedBaseline, 5.0F); }
/*     */ 
/*     */ 
/*     */   
/* 263 */   public Properties hoe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_HOE, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 267 */   public Properties shovel(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_SHOVEL, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 271 */   public Properties sword(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return material.applySwordProperties(this, attackDamageBaseline, attackSpeedBaseline); }
/*     */ 
/*     */   
/*     */   public Properties spear(ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountThreshold, float knockbackTime, float knockbackThreshold, float damageTime, float damageThreshold) {
/* 275 */     return durability(material.durability())
/* 276 */       .repairable(material.repairItems())
/* 277 */       .enchantable(material.enchantmentValue())
/* 278 */       .component(DataComponents.DAMAGE_TYPE, new EitherHolder(DamageTypes.SPEAR))
/* 279 */       .component(DataComponents.KINETIC_WEAPON, new KineticWeapon(10, (int)(delay * 20.0F), 
/*     */ 
/*     */           
/* 282 */           KineticWeapon.Condition.ofAttackerSpeed((int)(dismountTime * 20.0F), dismountThreshold), 
/* 283 */           KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackTime * 20.0F), knockbackThreshold), 
/* 284 */           KineticWeapon.Condition.ofRelativeSpeed((int)(damageTime * 20.0F), damageThreshold), 0.38F, damageMultiplier, 
/*     */ 
/*     */           
/* 287 */           Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE), 
/* 288 */           Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */       
/* 290 */       .component(DataComponents.PIERCING_WEAPON, new PiercingWeapon(true, false, 
/*     */ 
/*     */           
/* 293 */           Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK), 
/* 294 */           Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */       
/* 296 */       .component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
/* 297 */       .component(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(1.0F))
/* 298 */       .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(attackDuration * 20.0F)))
/*     */ 
/*     */ 
/*     */       
/* 302 */       .attributes(ItemAttributeModifiers.builder()
/* 303 */         .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (0.0F + material.attackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 304 */         .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (1.0F / attackDuration) - 4.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 305 */         .build())
/* 306 */       .component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
/* 307 */       .component(DataComponents.WEAPON, new Weapon(1));
/*     */   }
/*     */ 
/*     */   
/* 311 */   public Properties spawnEgg(EntityType<?> type) { return component(DataComponents.ENTITY_DATA, TypedEntityData.of(type, new CompoundTag())); }
/*     */ 
/*     */ 
/*     */   
/* 315 */   public Properties humanoidArmor(ArmorMaterial material, ArmorType type) { return durability(type.getDurability(material.durability()))
/* 316 */       .attributes(material.createAttributes(type))
/* 317 */       .enchantable(material.enchantmentValue())
/* 318 */       .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot()).setEquipSound(material.equipSound()).setAsset(material.assetId()).build())
/* 319 */       .repairable(material.repairIngredient()); }
/*     */ 
/*     */   
/*     */   public Properties wolfArmor(ArmorMaterial material) {
/* 323 */     return durability(ArmorType.BODY.getDurability(material.durability()))
/* 324 */       .attributes(material.createAttributes(ArmorType.BODY))
/* 325 */       .repairable(material.repairIngredient())
/* 326 */       .component(DataComponents.EQUIPPABLE, 
/* 327 */         Equippable.builder(EquipmentSlot.BODY)
/* 328 */         .setEquipSound(material.equipSound())
/* 329 */         .setAsset(material.assetId())
/* 330 */         .setAllowedEntities(HolderSet.direct(new Holder[] { EntityType.WOLF.builtInRegistryHolder()
/* 331 */             })).setCanBeSheared(true)
/* 332 */         .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
/* 333 */         .build())
/*     */       
/* 335 */       .component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
/* 336 */       .stacksTo(1);
/*     */   }
/*     */   
/*     */   public Properties horseArmor(ArmorMaterial material) {
/* 340 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/* 341 */     return attributes(material.createAttributes(ArmorType.BODY))
/* 342 */       .component(DataComponents.EQUIPPABLE, 
/* 343 */         Equippable.builder(EquipmentSlot.BODY)
/* 344 */         .setEquipSound(SoundEvents.HORSE_ARMOR)
/* 345 */         .setAsset(material.assetId())
/* 346 */         .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
/* 347 */         .setDamageOnHurt(false)
/* 348 */         .setCanBeSheared(true)
/* 349 */         .setShearingSound(SoundEvents.HORSE_ARMOR_UNEQUIP)
/* 350 */         .build())
/*     */       
/* 352 */       .stacksTo(1);
/*     */   }
/*     */   
/*     */   public Properties nautilusArmor(ArmorMaterial material) {
/* 356 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/* 357 */     return attributes(material.createAttributes(ArmorType.BODY))
/* 358 */       .component(DataComponents.EQUIPPABLE, 
/* 359 */         Equippable.builder(EquipmentSlot.BODY)
/* 360 */         .setEquipSound(SoundEvents.ARMOR_EQUIP_NAUTILUS)
/* 361 */         .setAsset(material.assetId())
/* 362 */         .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
/* 363 */         .setDamageOnHurt(false)
/* 364 */         .setEquipOnInteract(true)
/* 365 */         .setCanBeSheared(true)
/* 366 */         .setShearingSound(SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
/* 367 */         .build())
/*     */       
/* 369 */       .stacksTo(1);
/*     */   }
/*     */ 
/*     */   
/* 373 */   public Properties trimMaterial(ResourceKey<TrimMaterial> material) { return component(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(material)); }
/*     */ 
/*     */   
/*     */   public Properties requiredFeatures(FeatureFlag... flags) {
/* 377 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public Properties setId(ResourceKey<Item> id) {
/* 382 */     this.id = id;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideDescription(String descriptionId) {
/* 387 */     this.descriptionId = DependantName.fixed(descriptionId);
/* 388 */     return this;
/*     */   }
/*     */   
/*     */   public Properties useBlockDescriptionPrefix() {
/* 392 */     this.descriptionId = BLOCK_DESCRIPTION_ID;
/* 393 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties useItemDescriptionPrefix() {
/* 400 */     this.descriptionId = ITEM_DESCRIPTION_ID;
/* 401 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 405 */   protected String effectiveDescriptionId() { return (String)this.descriptionId.get((ResourceKey)Objects.requireNonNull(this.id, "Item id not set")); }
/*     */ 
/*     */ 
/*     */   
/* 409 */   public Identifier effectiveModel() { return (Identifier)this.model.get((ResourceKey)Objects.requireNonNull(this.id, "Item id not set")); }
/*     */ 
/*     */   
/*     */   public <T> Properties component(DataComponentType<T> type, T value) {
/* 413 */     this.components.set(type, value);
/* 414 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 418 */   public Properties attributes(ItemAttributeModifiers attributes) { return component(DataComponents.ATTRIBUTE_MODIFIERS, attributes); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DataComponentMap buildAndValidateComponents(Component name, Identifier model) {
/* 425 */     DataComponentMap components = this.components.set(DataComponents.ITEM_NAME, name).set(DataComponents.ITEM_MODEL, model).build();
/*     */     
/* 427 */     if (components.has(DataComponents.DAMAGE) && ((Integer)components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1) {
/* 428 */       throw new IllegalStateException("Item cannot have both durability and be stackable");
/*     */     }
/* 430 */     return components;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\Item$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */