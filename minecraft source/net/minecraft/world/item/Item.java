/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.Consumables;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.ArmorMaterial;
/*     */ import net.minecraft.world.item.equipment.ArmorType;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Item
/*     */   implements ItemLike, FeatureElement
/*     */ {
/* 100 */   public static final Codec<Holder<Item>> CODEC = BuiltInRegistries.ITEM.holderByNameCodec().validate(item -> 
/* 101 */       item.is(Items.AIR.builtInRegistryHolder()) ? DataResult.error(()) : DataResult.success(item));
/*     */   
/* 103 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);
/*     */   
/* 105 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 106 */   public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
/*     */   
/* 108 */   public static final Identifier BASE_ATTACK_DAMAGE_ID = Identifier.withDefaultNamespace("base_attack_damage");
/* 109 */   public static final Identifier BASE_ATTACK_SPEED_ID = Identifier.withDefaultNamespace("base_attack_speed");
/*     */   
/*     */   public static final int DEFAULT_MAX_STACK_SIZE = 64;
/*     */   
/*     */   public static final int ABSOLUTE_MAX_STACK_SIZE = 99;
/*     */   public static final int MAX_BAR_WIDTH = 13;
/*     */   protected static final int APPROXIMATELY_INFINITE_USE_DURATION = 72000;
/*     */   private final Holder.Reference<Item> builtInRegistryHolder;
/*     */   private final DataComponentMap components;
/*     */   private final Item craftingRemainingItem;
/*     */   protected final String descriptionId;
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/* 122 */   public static int getId(Item item) { return (item == null) ? 0 : BuiltInRegistries.ITEM.getId(item); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public static Item byId(int id) { return (Item)BuiltInRegistries.ITEM.byId(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 132 */   public static Item byBlock(Block block) { return (Item)BY_BLOCK.getOrDefault(block, Items.AIR); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item(Properties properties) {
/*     */     this.builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this);
/* 144 */     this.descriptionId = properties.effectiveDescriptionId();
/* 145 */     this.components = properties.buildAndValidateComponents(Component.translatable(this.descriptionId), properties.effectiveModel());
/* 146 */     this.craftingRemainingItem = properties.craftingRemainingItem;
/* 147 */     this.requiredFeatures = properties.requiredFeatures;
/*     */     
/* 149 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 150 */       String className = getClass().getSimpleName();
/* 151 */       if (!className.endsWith("Item")) {
/* 152 */         LOGGER.error("Item classes should end with Item and {} doesn't.", className);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 162 */   public Holder.Reference<Item> builtInRegistryHolder() { return this.builtInRegistryHolder; }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public DataComponentMap components() { return this.components; }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public int getDefaultMaxStackSize() { return ((Integer)this.components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue(); }
/*     */   
/*     */   public static class Properties
/*     */   {
/* 174 */     private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID = id -> Util.makeDescriptionId("block", id.identifier());
/* 175 */     private static final DependantName<Item, String> ITEM_DESCRIPTION_ID = id -> Util.makeDescriptionId("item", id.identifier());
/*     */     
/* 177 */     private final DataComponentMap.Builder components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS);
/*     */     private Item craftingRemainingItem;
/* 179 */     private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */     private ResourceKey<Item> id;
/* 181 */     private DependantName<Item, String> descriptionId = ITEM_DESCRIPTION_ID;
/* 182 */     private final DependantName<Item, Identifier> model = ResourceKey::identifier;
/*     */ 
/*     */     
/* 185 */     public Properties food(FoodProperties foodProperties) { return food(foodProperties, Consumables.DEFAULT_FOOD); }
/*     */ 
/*     */ 
/*     */     
/* 189 */     public Properties food(FoodProperties foodProperties, Consumable consumable) { return component(DataComponents.FOOD, foodProperties).component(DataComponents.CONSUMABLE, consumable); }
/*     */ 
/*     */ 
/*     */     
/* 193 */     public Properties usingConvertsTo(Item item) { return component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStack(item))); }
/*     */ 
/*     */ 
/*     */     
/* 197 */     public Properties useCooldown(float seconds) { return component(DataComponents.USE_COOLDOWN, new UseCooldown(seconds)); }
/*     */ 
/*     */ 
/*     */     
/* 201 */     public Properties stacksTo(int max) { return component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(max)); }
/*     */ 
/*     */     
/*     */     public Properties durability(int maxDamage) {
/* 205 */       component(DataComponents.MAX_DAMAGE, Integer.valueOf(maxDamage));
/* 206 */       component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1));
/* 207 */       component(DataComponents.DAMAGE, Integer.valueOf(0));
/* 208 */       return this;
/*     */     }
/*     */     
/*     */     public Properties craftRemainder(Item craftingRemainingItem) {
/* 212 */       this.craftingRemainingItem = craftingRemainingItem;
/* 213 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 217 */     public Properties rarity(Rarity rarity) { return component(DataComponents.RARITY, rarity); }
/*     */ 
/*     */ 
/*     */     
/* 221 */     public Properties fireResistant() { return component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE)); }
/*     */ 
/*     */ 
/*     */     
/* 225 */     public Properties jukeboxPlayable(ResourceKey<JukeboxSong> song) { return component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder(song))); }
/*     */ 
/*     */ 
/*     */     
/* 229 */     public Properties enchantable(int value) { return component(DataComponents.ENCHANTABLE, new Enchantable(value)); }
/*     */ 
/*     */ 
/*     */     
/* 233 */     public Properties repairable(Item repairItem) { return component(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(new Holder[] { repairItem.builtInRegistryHolder() }))); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Properties repairable(TagKey<Item> repairItems) {
/* 238 */       HolderGetter<Item> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
/* 239 */       return component(DataComponents.REPAIRABLE, new Repairable(registrationLookup.getOrThrow(repairItems)));
/*     */     }
/*     */ 
/*     */     
/* 243 */     public Properties equippable(EquipmentSlot slot) { return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).build()); }
/*     */ 
/*     */ 
/*     */     
/* 247 */     public Properties equippableUnswappable(EquipmentSlot slot) { return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).setSwappable(false).build()); }
/*     */ 
/*     */ 
/*     */     
/* 251 */     public Properties tool(ToolMaterial material, TagKey<Block> minesEfficiently, float attackDamageBaseline, float attackSpeedBaseline, float disableBlockingSeconds) { return material.applyToolProperties(this, minesEfficiently, attackDamageBaseline, attackSpeedBaseline, disableBlockingSeconds); }
/*     */ 
/*     */ 
/*     */     
/* 255 */     public Properties pickaxe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */     
/* 259 */     public Properties axe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_AXE, attackDamageBaseline, attackSpeedBaseline, 5.0F); }
/*     */ 
/*     */ 
/*     */     
/* 263 */     public Properties hoe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_HOE, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */     
/* 267 */     public Properties shovel(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return tool(material, BlockTags.MINEABLE_WITH_SHOVEL, attackDamageBaseline, attackSpeedBaseline, 0.0F); }
/*     */ 
/*     */ 
/*     */     
/* 271 */     public Properties sword(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) { return material.applySwordProperties(this, attackDamageBaseline, attackSpeedBaseline); }
/*     */ 
/*     */     
/*     */     public Properties spear(ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountThreshold, float knockbackTime, float knockbackThreshold, float damageTime, float damageThreshold) {
/* 275 */       return durability(material.durability())
/* 276 */         .repairable(material.repairItems())
/* 277 */         .enchantable(material.enchantmentValue())
/* 278 */         .component(DataComponents.DAMAGE_TYPE, new EitherHolder(DamageTypes.SPEAR))
/* 279 */         .component(DataComponents.KINETIC_WEAPON, new KineticWeapon(10, (int)(delay * 20.0F), 
/*     */ 
/*     */             
/* 282 */             KineticWeapon.Condition.ofAttackerSpeed((int)(dismountTime * 20.0F), dismountThreshold), 
/* 283 */             KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackTime * 20.0F), knockbackThreshold), 
/* 284 */             KineticWeapon.Condition.ofRelativeSpeed((int)(damageTime * 20.0F), damageThreshold), 0.38F, damageMultiplier, 
/*     */ 
/*     */             
/* 287 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE), 
/* 288 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 290 */         .component(DataComponents.PIERCING_WEAPON, new PiercingWeapon(true, false, 
/*     */ 
/*     */             
/* 293 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK), 
/* 294 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 296 */         .component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
/* 297 */         .component(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(1.0F))
/* 298 */         .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(attackDuration * 20.0F)))
/*     */ 
/*     */ 
/*     */         
/* 302 */         .attributes(ItemAttributeModifiers.builder()
/* 303 */           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (0.0F + material.attackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 304 */           .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (1.0F / attackDuration) - 4.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 305 */           .build())
/* 306 */         .component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
/* 307 */         .component(DataComponents.WEAPON, new Weapon(1));
/*     */     }
/*     */ 
/*     */     
/* 311 */     public Properties spawnEgg(EntityType<?> type) { return component(DataComponents.ENTITY_DATA, TypedEntityData.of(type, new CompoundTag())); }
/*     */ 
/*     */ 
/*     */     
/* 315 */     public Properties humanoidArmor(ArmorMaterial material, ArmorType type) { return durability(type.getDurability(material.durability()))
/* 316 */         .attributes(material.createAttributes(type))
/* 317 */         .enchantable(material.enchantmentValue())
/* 318 */         .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot()).setEquipSound(material.equipSound()).setAsset(material.assetId()).build())
/* 319 */         .repairable(material.repairIngredient()); }
/*     */ 
/*     */     
/*     */     public Properties wolfArmor(ArmorMaterial material) {
/* 323 */       return durability(ArmorType.BODY.getDurability(material.durability()))
/* 324 */         .attributes(material.createAttributes(ArmorType.BODY))
/* 325 */         .repairable(material.repairIngredient())
/* 326 */         .component(DataComponents.EQUIPPABLE, 
/* 327 */           Equippable.builder(EquipmentSlot.BODY)
/* 328 */           .setEquipSound(material.equipSound())
/* 329 */           .setAsset(material.assetId())
/* 330 */           .setAllowedEntities(HolderSet.direct(new Holder[] { EntityType.WOLF.builtInRegistryHolder()
/* 331 */               })).setCanBeSheared(true)
/* 332 */           .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
/* 333 */           .build())
/*     */         
/* 335 */         .component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
/* 336 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties horseArmor(ArmorMaterial material) {
/* 340 */       HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/* 341 */       return attributes(material.createAttributes(ArmorType.BODY))
/* 342 */         .component(DataComponents.EQUIPPABLE, 
/* 343 */           Equippable.builder(EquipmentSlot.BODY)
/* 344 */           .setEquipSound(SoundEvents.HORSE_ARMOR)
/* 345 */           .setAsset(material.assetId())
/* 346 */           .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
/* 347 */           .setDamageOnHurt(false)
/* 348 */           .setCanBeSheared(true)
/* 349 */           .setShearingSound(SoundEvents.HORSE_ARMOR_UNEQUIP)
/* 350 */           .build())
/*     */         
/* 352 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties nautilusArmor(ArmorMaterial material) {
/* 356 */       HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/* 357 */       return attributes(material.createAttributes(ArmorType.BODY))
/* 358 */         .component(DataComponents.EQUIPPABLE, 
/* 359 */           Equippable.builder(EquipmentSlot.BODY)
/* 360 */           .setEquipSound(SoundEvents.ARMOR_EQUIP_NAUTILUS)
/* 361 */           .setAsset(material.assetId())
/* 362 */           .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
/* 363 */           .setDamageOnHurt(false)
/* 364 */           .setEquipOnInteract(true)
/* 365 */           .setCanBeSheared(true)
/* 366 */           .setShearingSound(SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
/* 367 */           .build())
/*     */         
/* 369 */         .stacksTo(1);
/*     */     }
/*     */ 
/*     */     
/* 373 */     public Properties trimMaterial(ResourceKey<TrimMaterial> material) { return component(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(material)); }
/*     */ 
/*     */     
/*     */     public Properties requiredFeatures(FeatureFlag... flags) {
/* 377 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 378 */       return this;
/*     */     }
/*     */     
/*     */     public Properties setId(ResourceKey<Item> id) {
/* 382 */       this.id = id;
/* 383 */       return this;
/*     */     }
/*     */     
/*     */     public Properties overrideDescription(String descriptionId) {
/* 387 */       this.descriptionId = DependantName.fixed(descriptionId);
/* 388 */       return this;
/*     */     }
/*     */     
/*     */     public Properties useBlockDescriptionPrefix() {
/* 392 */       this.descriptionId = BLOCK_DESCRIPTION_ID;
/* 393 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Properties useItemDescriptionPrefix() {
/* 400 */       this.descriptionId = ITEM_DESCRIPTION_ID;
/* 401 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 405 */     protected String effectiveDescriptionId() { return (String)this.descriptionId.get((ResourceKey)Objects.requireNonNull(this.id, "Item id not set")); }
/*     */ 
/*     */ 
/*     */     
/* 409 */     public Identifier effectiveModel() { return (Identifier)this.model.get((ResourceKey)Objects.requireNonNull(this.id, "Item id not set")); }
/*     */ 
/*     */     
/*     */     public <T> Properties component(DataComponentType<T> type, T value) {
/* 413 */       this.components.set(type, value);
/* 414 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 418 */     public Properties attributes(ItemAttributeModifiers attributes) { return component(DataComponents.ATTRIBUTE_MODIFIERS, attributes); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private DataComponentMap buildAndValidateComponents(Component name, Identifier model) {
/* 425 */       DataComponentMap components = this.components.set(DataComponents.ITEM_NAME, name).set(DataComponents.ITEM_MODEL, model).build();
/*     */       
/* 427 */       if (components.has(DataComponents.DAMAGE) && ((Integer)components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1) {
/* 428 */         throw new IllegalStateException("Item cannot have both durability and be stackable");
/*     */       }
/* 430 */       return components;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {}
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity itemEntity) {}
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
/* 441 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 442 */     if (tool != null && !tool.canDestroyBlocksInCreative())
/* 443 */     { if (user instanceof Player) { Player player = (Player)user; if (!(player.getAbilities()).instabuild); return false; }
/*     */        }
/* 445 */     else { return true; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 450 */   public Item asItem() { return this; }
/*     */ 
/*     */ 
/*     */   
/* 454 */   public InteractionResult useOn(UseOnContext context) { return InteractionResult.PASS; }
/*     */ 
/*     */   
/*     */   public float getDestroySpeed(ItemStack itemStack, BlockState state) {
/* 458 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 459 */     return (tool != null) ? tool.getMiningSpeed(state) : 1.0F;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 463 */     ItemStack stack = player.getItemInHand(hand);
/*     */     
/* 465 */     Consumable consumable = (Consumable)stack.get(DataComponents.CONSUMABLE);
/* 466 */     if (consumable != null) {
/* 467 */       return consumable.startConsuming(player, stack, hand);
/*     */     }
/*     */     
/* 470 */     Equippable equippable = (Equippable)stack.get(DataComponents.EQUIPPABLE);
/* 471 */     if (equippable != null && equippable.swappable()) {
/* 472 */       return equippable.swapWithEquipmentSlot(stack, player);
/*     */     }
/*     */     
/* 475 */     if (stack.has(DataComponents.BLOCKS_ATTACKS)) {
/* 476 */       player.startUsingItem(hand);
/* 477 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 480 */     KineticWeapon kineticWeapon = (KineticWeapon)stack.get(DataComponents.KINETIC_WEAPON);
/* 481 */     if (kineticWeapon != null) {
/* 482 */       player.startUsingItem(hand);
/* 483 */       kineticWeapon.makeSound(player);
/* 484 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 487 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
/* 491 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 492 */     if (consumable != null) {
/* 493 */       return consumable.onConsume(level, entity, itemStack);
/*     */     }
/* 495 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/* 499 */   public boolean isBarVisible(ItemStack stack) { return stack.isDamaged(); }
/*     */ 
/*     */ 
/*     */   
/* 503 */   public int getBarWidth(ItemStack stack) { return Mth.clamp(Math.round(13.0F - stack.getDamageValue() * 13.0F / stack.getMaxDamage()), 0, 13); }
/*     */ 
/*     */   
/*     */   public int getBarColor(ItemStack stack) {
/* 507 */     int maxDamage = stack.getMaxDamage();
/* 508 */     float healthPercentage = Math.max(0.0F, (maxDamage - stack.getDamageValue()) / maxDamage);
/*     */ 
/*     */     
/* 511 */     return Mth.hsvToRgb(healthPercentage / 3.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 518 */   public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction clickAction, Player player) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 525 */   public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 529 */   public float getAttackDamageBonus(Entity victim, float damage, DamageSource damageSource) { return 0.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 537 */   public DamageSource getItemDamageSource(LivingEntity attacker) { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner) {
/* 550 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 551 */     if (tool == null) {
/* 552 */       return false;
/*     */     }
/*     */     
/* 555 */     if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F && 
/* 556 */       tool.damagePerBlock() > 0) {
/* 557 */       itemStack.hurtAndBreak(tool.damagePerBlock(), owner, EquipmentSlot.MAINHAND);
/*     */     }
/*     */     
/* 560 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCorrectToolForDrops(ItemStack itemStack, BlockState state) {
/* 564 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 565 */     return (tool != null && tool.isCorrectForDrops(state));
/*     */   }
/*     */ 
/*     */   
/* 569 */   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) { return InteractionResult.PASS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 574 */   public String toString() { return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName(); }
/*     */ 
/*     */ 
/*     */   
/* 578 */   public final ItemStack getCraftingRemainder() { return (this.craftingRemainingItem == null) ? ItemStack.EMPTY : new ItemStack(this.craftingRemainingItem); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 588 */   public void onCraftedBy(ItemStack itemStack, Player player) { onCraftedPostProcess(itemStack, player.level()); }
/*     */ 
/*     */   
/*     */   public void onCraftedPostProcess(ItemStack itemStack, Level level) {}
/*     */ 
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 595 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 596 */     if (consumable != null) {
/* 597 */       return consumable.animation();
/*     */     }
/* 599 */     if (itemStack.has(DataComponents.BLOCKS_ATTACKS)) {
/* 600 */       return ItemUseAnimation.BLOCK;
/*     */     }
/* 602 */     if (itemStack.has(DataComponents.KINETIC_WEAPON)) {
/* 603 */       return ItemUseAnimation.SPEAR;
/*     */     }
/* 605 */     return ItemUseAnimation.NONE;
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 609 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 610 */     if (consumable != null) {
/* 611 */       return consumable.consumeTicks();
/*     */     }
/* 613 */     if (itemStack.has(DataComponents.BLOCKS_ATTACKS) || itemStack.has(DataComponents.KINETIC_WEAPON)) {
/* 614 */       return 72000;
/*     */     }
/* 616 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 623 */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 634 */   public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 639 */   public final String getDescriptionId() { return this.descriptionId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 646 */   public final Component getName() { return (Component)this.components.getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 650 */   public Component getName(ItemStack itemStack) { return (Component)itemStack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 654 */   public boolean isFoil(ItemStack itemStack) { return itemStack.isEnchanted(); }
/*     */ 
/*     */   
/*     */   protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
/* 658 */     Vec3 from = player.getEyePosition();
/*     */     
/* 660 */     Vec3 to = from.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
/*     */     
/* 662 */     return level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, fluid, player));
/*     */   }
/*     */ 
/*     */   
/* 666 */   public boolean useOnRelease(ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 670 */   public ItemStack getDefaultInstance() { return new ItemStack(this); }
/*     */ 
/*     */ 
/*     */   
/* 674 */   public boolean canFitInsideContainerItems() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 679 */   public FeatureFlagSet requiredFeatures() { return this.requiredFeatures; }
/*     */ 
/*     */ 
/*     */   
/* 683 */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class null
/*     */     implements TooltipContext
/*     */   {
/* 695 */     public HolderLookup.Provider registries() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 700 */     public float tickRate() { return 20.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 705 */     public MapItemSavedData mapData(MapId id) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 710 */     public boolean isPeaceful() { return false; } } public static interface TooltipContext { public static final TooltipContext EMPTY = new TooltipContext() { public HolderLookup.Provider registries() { return null; } public float tickRate() { return 20.0F; } public MapItemSavedData mapData(MapId id) { return null; } public boolean isPeaceful() { return false; } }
/*     */     ;
/*     */ 
/*     */     
/*     */     HolderLookup.Provider registries();
/*     */     
/*     */     float tickRate();
/*     */     
/*     */     MapItemSavedData mapData(MapId param1MapId);
/*     */     
/*     */     boolean isPeaceful();
/*     */     
/*     */     static TooltipContext of(final Level level) {
/* 723 */       if (level == null) {
/* 724 */         return EMPTY;
/*     */       }
/*     */       
/* 727 */       return new TooltipContext()
/*     */         {
/*     */           public HolderLookup.Provider registries() {
/* 730 */             return level.registryAccess();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 735 */           public float tickRate() { return level.tickRateManager().tickrate(); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 740 */           public MapItemSavedData mapData(MapId id) { return level.getMapData(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 745 */           public boolean isPeaceful() { return (level.getDifficulty() == Difficulty.PEACEFUL); }
/*     */         };
/*     */     }
/*     */     
/*     */     static TooltipContext of(final HolderLookup.Provider registries)
/*     */     {
/* 751 */       return new TooltipContext()
/*     */         {
/*     */           public HolderLookup.Provider registries() {
/* 754 */             return registries;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 759 */           public float tickRate() { return 20.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 764 */           public MapItemSavedData mapData(MapId id) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 769 */           public boolean isPeaceful() { return false; } }; } } class null implements TooltipContext { public HolderLookup.Provider registries() { return level.registryAccess(); } public float tickRate() { return level.tickRateManager().tickrate(); } public MapItemSavedData mapData(MapId id) { return level.getMapData(id); } public boolean isPeaceful() { return (level.getDifficulty() == Difficulty.PEACEFUL); } } class null implements TooltipContext { public boolean isPeaceful() { return false; }
/*     */     
/*     */     public HolderLookup.Provider registries() { return registries; }
/*     */     
/*     */     public float tickRate() { return 20.0F; }
/*     */     
/*     */     public MapItemSavedData mapData(MapId id) { return null; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\Item.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */