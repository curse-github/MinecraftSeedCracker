/*      */ package net.minecraft.world.item;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentHolder;
/*      */ import net.minecraft.core.component.DataComponentMap;
/*      */ import net.minecraft.core.component.DataComponentPatch;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.PatchedDataComponentMap;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.ComponentUtils;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.codec.ByteBufCodecs;
/*      */ import net.minecraft.network.codec.StreamCodec;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.NullOps;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.decoration.ItemFrame;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*      */ import net.minecraft.world.item.component.Consumable;
/*      */ import net.minecraft.world.item.component.DamageResistant;
/*      */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*      */ import net.minecraft.world.item.component.ItemContainerContents;
/*      */ import net.minecraft.world.item.component.KineticWeapon;
/*      */ import net.minecraft.world.item.component.SwingAnimation;
/*      */ import net.minecraft.world.item.component.TooltipDisplay;
/*      */ import net.minecraft.world.item.component.TooltipProvider;
/*      */ import net.minecraft.world.item.component.TypedEntityData;
/*      */ import net.minecraft.world.item.component.UseCooldown;
/*      */ import net.minecraft.world.item.component.UseEffects;
/*      */ import net.minecraft.world.item.component.UseRemainder;
/*      */ import net.minecraft.world.item.component.Weapon;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.context.UseOnContext;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*      */ import net.minecraft.world.item.enchantment.Repairable;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.Spawner;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import org.apache.commons.lang3.function.TriConsumer;
/*      */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public final class ItemStack implements DataComponentHolder {
/*  107 */   private static final List<Component> OP_NBT_WARNING = List.of(
/*  108 */       Component.translatable("item.op_warning.line1").withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.BOLD
/*  109 */         }, ), Component.translatable("item.op_warning.line2").withStyle(ChatFormatting.RED), 
/*  110 */       Component.translatable("item.op_warning.line3").withStyle(ChatFormatting.RED));
/*      */   
/*  112 */   private static final Component UNBREAKABLE_TOOLTIP = Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE);
/*  113 */   private static final Component INTANGIBLE_TOOLTIP = Component.translatable("item.intangible").withStyle(ChatFormatting.GRAY);
/*      */   
/*  115 */   public static final MapCodec<ItemStack> MAP_CODEC = MapCodec.recursive("ItemStack", subCodec -> RecordCodecBuilder.mapCodec(())); public static final Codec<ItemStack> CODEC; public static final Codec<ItemStack> SINGLE_ITEM_CODEC; public static final Codec<ItemStack> STRICT_CODEC; public static final Codec<ItemStack> STRICT_SINGLE_ITEM_CODEC; public static final Codec<ItemStack> OPTIONAL_CODEC; public static final Codec<ItemStack> SIMPLE_ITEM_CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> OPTIONAL_STREAM_CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> OPTIONAL_UNTRUSTED_STREAM_CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> STREAM_CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> OPTIONAL_LIST_STREAM_CODEC; private static final Logger LOGGER; public static final ItemStack EMPTY; private static final Component DISABLED_ITEM_TOOLTIP; private int count;
/*      */   private int popTime;
/*      */   @Deprecated
/*      */   private final Item item;
/*      */   private final PatchedDataComponentMap components;
/*      */   private Entity entityRepresentation;
/*      */   
/*  122 */   static  { Objects.requireNonNull(MAP_CODEC); CODEC = Codec.lazyInitialized(MAP_CODEC::codec);
/*      */     
/*  124 */     SINGLE_ITEM_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  130 */     STRICT_CODEC = CODEC.validate(ItemStack::validateStrict);
/*  131 */     STRICT_SINGLE_ITEM_CODEC = SINGLE_ITEM_CODEC.validate(ItemStack::validateStrict);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  144 */     OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC).xmap(itemStack -> 
/*  145 */         (ItemStack)itemStack.orElse(EMPTY), itemStack -> 
/*  146 */         itemStack.isEmpty() ? Optional.empty() : Optional.of(itemStack));
/*      */ 
/*      */     
/*  149 */     SIMPLE_ITEM_CODEC = Item.CODEC.xmap(ItemStack::new, ItemStack::getItemHolder);
/*      */     
/*  151 */     OPTIONAL_STREAM_CODEC = createOptionalStreamCodec(DataComponentPatch.STREAM_CODEC);
/*      */ 
/*      */     
/*  154 */     OPTIONAL_UNTRUSTED_STREAM_CODEC = createOptionalStreamCodec(DataComponentPatch.DELIMITED_STREAM_CODEC);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  184 */     STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, ItemStack>()
/*      */       {
/*      */         public ItemStack decode(RegistryFriendlyByteBuf input) {
/*  187 */           ItemStack itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/*  188 */           if (itemStack.isEmpty()) {
/*  189 */             throw new DecoderException("Empty ItemStack not allowed");
/*      */           }
/*  191 */           return itemStack;
/*      */         }
/*      */ 
/*      */         
/*      */         public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/*  196 */           if (itemStack.isEmpty()) {
/*  197 */             throw new EncoderException("Empty ItemStack not allowed");
/*      */           }
/*  199 */           ItemStack.OPTIONAL_STREAM_CODEC.encode(output, itemStack);
/*      */         }
/*      */       };
/*      */     
/*  203 */     OPTIONAL_LIST_STREAM_CODEC = OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  228 */     LOGGER = LogUtils.getLogger();
/*  229 */     EMPTY = new ItemStack((Void)null);
/*      */     
/*  231 */     DISABLED_ITEM_TOOLTIP = Component.translatable("item.disabled").withStyle(ChatFormatting.RED); }
/*      */   public static DataResult<ItemStack> validateStrict(ItemStack itemStack) { DataResult<Unit> result = validateComponents(itemStack.getComponents()); if (result.isError()) return result.map(unit -> itemStack);  if (itemStack.getCount() > itemStack.getMaxStackSize()) return DataResult.error(() -> "Item stack with stack size of " + itemStack.getCount() + " was larger than maximum: " + itemStack.getMaxStackSize());  return DataResult.success(itemStack); } private static StreamCodec<RegistryFriendlyByteBuf, ItemStack> createOptionalStreamCodec(final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> patchCodec) { return new StreamCodec<RegistryFriendlyByteBuf, ItemStack>() {
/*      */         public ItemStack decode(RegistryFriendlyByteBuf input) { int count = input.readVarInt(); if (count <= 0) return ItemStack.EMPTY;  Holder<Item> item = (Holder)Item.STREAM_CODEC.decode(input); DataComponentPatch patch = (DataComponentPatch)patchCodec.decode(input); return new ItemStack(item, count, patch); } public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) { if (itemStack.isEmpty()) { output.writeVarInt(0); return; }  output.writeVarInt(itemStack.getCount()); Item.STREAM_CODEC.encode(output, itemStack.getItemHolder()); patchCodec.encode(output, itemStack.components.asPatch()); }
/*  234 */       }; } public Optional<TooltipComponent> getTooltipImage() { return getItem().getTooltipImage(this); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  239 */   public DataComponentMap getComponents() { return !isEmpty() ? this.components : DataComponentMap.EMPTY; } public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> validatedStreamCodec(final StreamCodec<RegistryFriendlyByteBuf, ItemStack> codec) { return new StreamCodec<RegistryFriendlyByteBuf, ItemStack>() { public ItemStack decode(RegistryFriendlyByteBuf input) { ItemStack itemStack = (ItemStack)codec.decode(input); if (!itemStack.isEmpty()) {
/*      */             RegistryOps<Unit> ops = input.registryAccess().createSerializationContext(NullOps.INSTANCE); ItemStack.CODEC.encodeStart(ops, itemStack).getOrThrow(DecoderException::new);
/*      */           }  return itemStack; } public void encode(RegistryFriendlyByteBuf output, ItemStack value) { codec.encode(output, value); } }
/*      */       ; }
/*  243 */   public DataComponentMap getPrototype() { return !isEmpty() ? getItem().components() : DataComponentMap.EMPTY; }
/*      */ 
/*      */ 
/*      */   
/*  247 */   public DataComponentPatch getComponentsPatch() { return !isEmpty() ? this.components.asPatch() : DataComponentPatch.EMPTY; }
/*      */ 
/*      */ 
/*      */   
/*  251 */   public DataComponentMap immutableComponents() { return !isEmpty() ? this.components.toImmutableMap() : DataComponentMap.EMPTY; }
/*      */ 
/*      */ 
/*      */   
/*  255 */   public boolean hasNonDefault(DataComponentType<?> type) { return (!isEmpty() && this.components.hasNonDefault(type)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  273 */   public ItemStack(ItemLike item) { this(item, 1); }
/*      */ 
/*      */ 
/*      */   
/*  277 */   public ItemStack(Holder<Item> item) { this((ItemLike)item.value(), 1); }
/*      */ 
/*      */ 
/*      */   
/*  281 */   public ItemStack(Holder<Item> item, int count, DataComponentPatch components) { this((ItemLike)item.value(), count, PatchedDataComponentMap.fromPatch(((Item)item.value()).components(), components)); }
/*      */ 
/*      */ 
/*      */   
/*  285 */   public ItemStack(Holder<Item> item, int count) { this((ItemLike)item.value(), count); }
/*      */ 
/*      */ 
/*      */   
/*  289 */   public ItemStack(ItemLike item, int count) { this(item, count, new PatchedDataComponentMap(item.asItem().components())); }
/*      */ 
/*      */   
/*      */   private ItemStack(ItemLike item, int count, PatchedDataComponentMap components) {
/*  293 */     this.item = item.asItem();
/*  294 */     this.count = count;
/*  295 */     this.components = components;
/*      */   }
/*      */   
/*      */   private ItemStack(Void nullMarker) {
/*  299 */     this.item = null;
/*  300 */     this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
/*      */   }
/*      */   
/*      */   public static DataResult<Unit> validateComponents(DataComponentMap components) {
/*  304 */     if (components.has(DataComponents.MAX_DAMAGE) && ((Integer)components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1) {
/*  305 */       return DataResult.error(() -> "Item cannot be both damageable and stackable");
/*      */     }
/*  307 */     ItemContainerContents container = (ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
/*  308 */     for (ItemStack item : container.nonEmptyItems()) {
/*  309 */       int itemCount = item.getCount();
/*  310 */       int maxStackSize = item.getMaxStackSize();
/*  311 */       if (itemCount > maxStackSize) {
/*  312 */         return DataResult.error(() -> "Item stack with count of " + itemCount + " was larger than maximum: " + maxStackSize);
/*      */       }
/*      */     } 
/*  315 */     return DataResult.success(Unit.INSTANCE);
/*      */   }
/*      */ 
/*      */   
/*  319 */   public boolean isEmpty() { return (this == EMPTY || this.item == Items.AIR || this.count <= 0); }
/*      */ 
/*      */ 
/*      */   
/*  323 */   public boolean isItemEnabled(FeatureFlagSet enabledFeatures) { return (isEmpty() || getItem().isEnabled(enabledFeatures)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack split(int amount) {
/*  331 */     int realAmount = Math.min(amount, getCount());
/*      */     
/*  333 */     ItemStack result = copyWithCount(realAmount);
/*  334 */     shrink(realAmount);
/*      */     
/*  336 */     return result;
/*      */   }
/*      */   
/*      */   public ItemStack copyAndClear() {
/*  340 */     if (isEmpty()) {
/*  341 */       return EMPTY;
/*      */     }
/*  343 */     ItemStack result = copy();
/*  344 */     setCount(0);
/*  345 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  350 */   public Item getItem() { return isEmpty() ? Items.AIR : this.item; }
/*      */ 
/*      */ 
/*      */   
/*  354 */   public Holder<Item> getItemHolder() { return getItem().builtInRegistryHolder(); }
/*      */ 
/*      */ 
/*      */   
/*  358 */   public boolean is(TagKey<Item> tag) { return getItem().builtInRegistryHolder().is(tag); }
/*      */ 
/*      */ 
/*      */   
/*  362 */   public boolean is(Item item) { return (getItem() == item); }
/*      */ 
/*      */ 
/*      */   
/*  366 */   public boolean is(Predicate<Holder<Item>> item) { return item.test(getItem().builtInRegistryHolder()); }
/*      */ 
/*      */ 
/*      */   
/*  370 */   public boolean is(Holder<Item> item) { return (getItem().builtInRegistryHolder() == item); }
/*      */ 
/*      */ 
/*      */   
/*  374 */   public boolean is(HolderSet<Item> set) { return set.contains(getItemHolder()); }
/*      */ 
/*      */ 
/*      */   
/*  378 */   public Stream<TagKey<Item>> getTags() { return getItem().builtInRegistryHolder().tags(); }
/*      */ 
/*      */   
/*      */   public InteractionResult useOn(UseOnContext context) {
/*  382 */     Player player = context.getPlayer();
/*  383 */     BlockPos pos = context.getClickedPos();
/*  384 */     if (player != null && !(player.getAbilities()).mayBuild && !canPlaceOnBlockInAdventureMode(new BlockInWorld(context.getLevel(), pos, false))) {
/*  385 */       return InteractionResult.PASS;
/*      */     }
/*      */     
/*  388 */     Item usedItem = getItem();
/*  389 */     InteractionResult result = usedItem.useOn(context);
/*      */     
/*  391 */     if (player != null && result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result; if (success.wasItemInteraction())
/*  392 */         player.awardStat(Stats.ITEM_USED.get(usedItem));  }
/*      */     
/*  394 */     return result;
/*      */   }
/*      */ 
/*      */   
/*  398 */   public float getDestroySpeed(BlockState state) { return getItem().getDestroySpeed(this, state); }
/*      */ 
/*      */   
/*      */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*  402 */     ItemStack stackBeforeUse = copy();
/*  403 */     boolean isInstantlyUsed = (getUseDuration(player) <= 0);
/*  404 */     InteractionResult result = getItem().use(level, player, hand);
/*      */     
/*  406 */     if (isInstantlyUsed && result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result;
/*  407 */       return success.heldItemTransformedTo((success.heldItemTransformedTo() == null) ? 
/*  408 */           applyAfterUseComponentSideEffects(player, stackBeforeUse) : 
/*  409 */           success.heldItemTransformedTo().applyAfterUseComponentSideEffects(player, stackBeforeUse)); }
/*      */ 
/*      */     
/*  412 */     return result;
/*      */   }
/*      */   
/*      */   public ItemStack finishUsingItem(Level level, LivingEntity livingEntity) {
/*  416 */     ItemStack stackBeforeUse = copy();
/*  417 */     ItemStack result = getItem().finishUsingItem(this, level, livingEntity);
/*  418 */     return result.applyAfterUseComponentSideEffects(livingEntity, stackBeforeUse);
/*      */   }
/*      */ 
/*      */   
/*      */   private ItemStack applyAfterUseComponentSideEffects(LivingEntity user, ItemStack stackBeforeUsing) {
/*  423 */     UseRemainder useRemainder = (UseRemainder)stackBeforeUsing.get(DataComponents.USE_REMAINDER);
/*  424 */     UseCooldown useCooldown = (UseCooldown)stackBeforeUsing.get(DataComponents.USE_COOLDOWN);
/*  425 */     int stackCountBeforeUsing = stackBeforeUsing.getCount();
/*      */     
/*  427 */     ItemStack result = this;
/*  428 */     if (useRemainder != null) {
/*  429 */       Objects.requireNonNull(user); result = useRemainder.convertIntoRemainder(result, stackCountBeforeUsing, user.hasInfiniteMaterials(), user::handleExtraItemsCreatedOnUse);
/*      */     } 
/*      */     
/*  432 */     if (useCooldown != null) {
/*  433 */       useCooldown.apply(stackBeforeUsing, user);
/*      */     }
/*      */     
/*  436 */     return result;
/*      */   }
/*      */ 
/*      */   
/*  440 */   public int getMaxStackSize() { return ((Integer)getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue(); }
/*      */ 
/*      */ 
/*      */   
/*  444 */   public boolean isStackable() { return (getMaxStackSize() > 1 && (!isDamageableItem() || !isDamaged())); }
/*      */ 
/*      */ 
/*      */   
/*  448 */   public boolean isDamageableItem() { return (has(DataComponents.MAX_DAMAGE) && !has(DataComponents.UNBREAKABLE) && has(DataComponents.DAMAGE)); }
/*      */ 
/*      */ 
/*      */   
/*  452 */   public boolean isDamaged() { return (isDamageableItem() && getDamageValue() > 0); }
/*      */ 
/*      */ 
/*      */   
/*  456 */   public int getDamageValue() { return Mth.clamp(((Integer)getOrDefault(DataComponents.DAMAGE, Integer.valueOf(0))).intValue(), 0, getMaxDamage()); }
/*      */ 
/*      */ 
/*      */   
/*  460 */   public void setDamageValue(int value) { set(DataComponents.DAMAGE, Integer.valueOf(Mth.clamp(value, 0, getMaxDamage()))); }
/*      */ 
/*      */ 
/*      */   
/*  464 */   public int getMaxDamage() { return ((Integer)getOrDefault(DataComponents.MAX_DAMAGE, Integer.valueOf(0))).intValue(); }
/*      */ 
/*      */ 
/*      */   
/*  468 */   public boolean isBroken() { return (isDamageableItem() && getDamageValue() >= getMaxDamage()); }
/*      */ 
/*      */ 
/*      */   
/*  472 */   public boolean nextDamageWillBreak() { return (isDamageableItem() && getDamageValue() >= getMaxDamage() - 1); }
/*      */ 
/*      */   
/*      */   public void hurtAndBreak(int amount, ServerLevel level, ServerPlayer player, Consumer<Item> onBreak) {
/*  476 */     int newAmount = processDurabilityChange(amount, level, player);
/*  477 */     if (newAmount != 0) {
/*  478 */       applyDamage(getDamageValue() + newAmount, player, onBreak);
/*      */     }
/*      */   }
/*      */   
/*      */   private int processDurabilityChange(int amount, ServerLevel level, ServerPlayer player) {
/*  483 */     if (!isDamageableItem()) {
/*  484 */       return 0;
/*      */     }
/*  486 */     if (player != null && player.hasInfiniteMaterials()) {
/*  487 */       return 0;
/*      */     }
/*  489 */     if (amount > 0) {
/*  490 */       return EnchantmentHelper.processDurabilityChange(level, this, amount);
/*      */     }
/*  492 */     return amount;
/*      */   }
/*      */   
/*      */   private void applyDamage(int newDamage, ServerPlayer player, Consumer<Item> onBreak) {
/*  496 */     if (player != null) {
/*  497 */       CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, this, newDamage);
/*      */     }
/*      */     
/*  500 */     setDamageValue(newDamage);
/*      */     
/*  502 */     if (isBroken()) {
/*  503 */       Item item = getItem();
/*  504 */       shrink(1);
/*  505 */       onBreak.accept(item);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void hurtWithoutBreaking(int amount, Player player) {
/*  510 */     if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/*  511 */       int newAmount = processDurabilityChange(amount, serverPlayer.level(), serverPlayer);
/*  512 */       if (newAmount == 0) {
/*      */         return;
/*      */       }
/*  515 */       int newDamage = Math.min(getDamageValue() + newAmount, getMaxDamage() - 1);
/*  516 */       applyDamage(newDamage, serverPlayer, i -> {
/*      */           
/*      */           }); }
/*      */   
/*      */   }
/*  521 */   public void hurtAndBreak(int amount, LivingEntity owner, InteractionHand hand) { hurtAndBreak(amount, owner, hand.asEquipmentSlot()); }
/*      */ 
/*      */   
/*      */   public void hurtAndBreak(int amount, LivingEntity owner, EquipmentSlot slot) {
/*  525 */     Level level = owner.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  526 */       ServerPlayer player = (ServerPlayer)owner; hurtAndBreak(amount, serverLevel, (owner instanceof ServerPlayer) ? player : null, brokenItem -> owner.onEquippedItemBroken(brokenItem, slot)); }
/*      */   
/*      */   }
/*      */   
/*      */   public ItemStack hurtAndConvertOnBreak(int amount, ItemLike newItem, LivingEntity owner, EquipmentSlot slot) {
/*  531 */     hurtAndBreak(amount, owner, slot);
/*  532 */     if (isEmpty()) {
/*  533 */       ItemStack replacement = transmuteCopyIgnoreEmpty(newItem, 1);
/*  534 */       if (replacement.isDamageableItem()) {
/*  535 */         replacement.setDamageValue(0);
/*      */       }
/*  537 */       return replacement;
/*      */     } 
/*  539 */     return this;
/*      */   }
/*      */ 
/*      */   
/*  543 */   public boolean isBarVisible() { return getItem().isBarVisible(this); }
/*      */ 
/*      */ 
/*      */   
/*  547 */   public int getBarWidth() { return getItem().getBarWidth(this); }
/*      */ 
/*      */ 
/*      */   
/*  551 */   public int getBarColor() { return getItem().getBarColor(this); }
/*      */ 
/*      */ 
/*      */   
/*  555 */   public boolean overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player) { return getItem().overrideStackedOnOther(this, slot, clickAction, player); }
/*      */ 
/*      */ 
/*      */   
/*  559 */   public boolean overrideOtherStackedOnMe(ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) { return getItem().overrideOtherStackedOnMe(this, other, slot, clickAction, player, carriedItem); }
/*      */ 
/*      */   
/*      */   public boolean hurtEnemy(LivingEntity mob, LivingEntity attacker) {
/*  563 */     Item usedItem = getItem();
/*  564 */     usedItem.hurtEnemy(this, mob, attacker);
/*  565 */     if (has(DataComponents.WEAPON)) {
/*  566 */       if (attacker instanceof Player) { Player player = (Player)attacker;
/*  567 */         player.awardStat(Stats.ITEM_USED.get(usedItem)); }
/*      */       
/*  569 */       return true;
/*      */     } 
/*  571 */     return false;
/*      */   }
/*      */   
/*      */   public void postHurtEnemy(LivingEntity mob, LivingEntity attacker) {
/*  575 */     getItem().postHurtEnemy(this, mob, attacker);
/*  576 */     Weapon weapon = (Weapon)get(DataComponents.WEAPON);
/*  577 */     if (weapon != null) {
/*  578 */       hurtAndBreak(weapon.itemDamagePerAttack(), attacker, EquipmentSlot.MAINHAND);
/*      */     }
/*      */   }
/*      */   
/*      */   public void mineBlock(Level level, BlockState state, BlockPos pos, Player owner) {
/*  583 */     Item usedItem = getItem();
/*  584 */     if (usedItem.mineBlock(this, level, state, pos, owner)) {
/*  585 */       owner.awardStat(Stats.ITEM_USED.get(usedItem));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  590 */   public boolean isCorrectToolForDrops(BlockState state) { return getItem().isCorrectToolForDrops(this, state); }
/*      */ 
/*      */   
/*      */   public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
/*  594 */     Equippable equippable = (Equippable)get(DataComponents.EQUIPPABLE);
/*  595 */     if (equippable != null && equippable.equipOnInteract()) {
/*  596 */       InteractionResult result = equippable.equipOnTarget(player, target, this);
/*  597 */       if (result != InteractionResult.PASS) {
/*  598 */         return result;
/*      */       }
/*      */     } 
/*  601 */     return getItem().interactLivingEntity(this, player, target, hand);
/*      */   }
/*      */   
/*      */   public ItemStack copy() {
/*  605 */     if (isEmpty()) {
/*  606 */       return EMPTY;
/*      */     }
/*  608 */     ItemStack copy = new ItemStack(getItem(), this.count, this.components.copy());
/*  609 */     copy.setPopTime(getPopTime());
/*  610 */     return copy;
/*      */   }
/*      */   
/*      */   public ItemStack copyWithCount(int count) {
/*  614 */     if (isEmpty()) {
/*  615 */       return EMPTY;
/*      */     }
/*  617 */     ItemStack copy = copy();
/*  618 */     copy.setCount(count);
/*  619 */     return copy;
/*      */   }
/*      */ 
/*      */   
/*  623 */   public ItemStack transmuteCopy(ItemLike newItem) { return transmuteCopy(newItem, getCount()); }
/*      */ 
/*      */   
/*      */   public ItemStack transmuteCopy(ItemLike newItem, int newCount) {
/*  627 */     if (isEmpty()) {
/*  628 */       return EMPTY;
/*      */     }
/*  630 */     return transmuteCopyIgnoreEmpty(newItem, newCount);
/*      */   }
/*      */ 
/*      */   
/*  634 */   private ItemStack transmuteCopyIgnoreEmpty(ItemLike newItem, int newCount) { return new ItemStack(newItem.asItem().builtInRegistryHolder(), newCount, this.components.asPatch()); }
/*      */ 
/*      */   
/*      */   public static boolean matches(ItemStack a, ItemStack b) {
/*  638 */     if (a == b) {
/*  639 */       return true;
/*      */     }
/*  641 */     if (a.getCount() != b.getCount()) {
/*  642 */       return false;
/*      */     }
/*  644 */     return isSameItemSameComponents(a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean listMatches(List<ItemStack> left, List<ItemStack> right) {
/*  652 */     if (left.size() != right.size()) {
/*  653 */       return false;
/*      */     }
/*  655 */     for (int i = 0; i < left.size(); i++) {
/*  656 */       if (!matches((ItemStack)left.get(i), (ItemStack)right.get(i))) {
/*  657 */         return false;
/*      */       }
/*      */     } 
/*  660 */     return true;
/*      */   }
/*      */ 
/*      */   
/*  664 */   public static boolean isSameItem(ItemStack a, ItemStack b) { return a.is(b.getItem()); }
/*      */ 
/*      */   
/*      */   public static boolean isSameItemSameComponents(ItemStack a, ItemStack b) {
/*  668 */     if (!a.is(b.getItem())) {
/*  669 */       return false;
/*      */     }
/*  671 */     if (a.isEmpty() && b.isEmpty()) {
/*  672 */       return true;
/*      */     }
/*  674 */     return Objects.equals(a.components, b.components);
/*      */   }
/*      */   
/*      */   public static boolean matchesIgnoringComponents(ItemStack a, ItemStack b, Predicate<DataComponentType<?>> ignoredPredicate) {
/*  678 */     if (a == b) {
/*  679 */       return true;
/*      */     }
/*  681 */     if (a.getCount() != b.getCount()) {
/*  682 */       return false;
/*      */     }
/*  684 */     if (!a.is(b.getItem())) {
/*  685 */       return false;
/*      */     }
/*  687 */     if (a.isEmpty() && b.isEmpty()) {
/*  688 */       return true;
/*      */     }
/*  690 */     if (a.components.size() != b.components.size()) {
/*  691 */       return false;
/*      */     }
/*  693 */     for (DataComponentType<?> type : a.components.keySet()) {
/*  694 */       Object componentA = a.components.get(type);
/*  695 */       Object componentB = b.components.get(type);
/*  696 */       if (componentA == null || componentB == null) {
/*  697 */         return false;
/*      */       }
/*  699 */       if (!Objects.equals(componentA, componentB) && !ignoredPredicate.test(type)) {
/*  700 */         return false;
/*      */       }
/*      */     } 
/*  703 */     return true;
/*      */   }
/*      */   
/*      */   public static MapCodec<ItemStack> lenientOptionalFieldOf(String name) {
/*  707 */     return CODEC.lenientOptionalFieldOf(name).xmap(itemStack -> 
/*  708 */         (ItemStack)itemStack.orElse(EMPTY), itemStack -> 
/*  709 */         itemStack.isEmpty() ? Optional.empty() : Optional.of(itemStack));
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashItemAndComponents(ItemStack item) {
/*  714 */     if (item != null) {
/*  715 */       int result = 31 + item.getItem().hashCode();
/*  716 */       return 31 * result + item.getComponents().hashCode();
/*      */     } 
/*  718 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int hashStackList(List<ItemStack> items) {
/*  726 */     int result = 0;
/*  727 */     for (ItemStack item : items) {
/*  728 */       result = result * 31 + hashItemAndComponents(item);
/*      */     }
/*  730 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  735 */   public String toString() { return "" + getCount() + " " + getCount(); }
/*      */ 
/*      */   
/*      */   public void inventoryTick(Level level, Entity owner, EquipmentSlot slot) {
/*  739 */     if (this.popTime > 0) {
/*  740 */       this.popTime--;
/*      */     }
/*  742 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  743 */       getItem().inventoryTick(this, serverLevel, owner, slot); }
/*      */   
/*      */   }
/*      */   
/*      */   public void onCraftedBy(Player player, int craftCount) {
/*  748 */     player.awardStat(Stats.ITEM_CRAFTED.get(getItem()), craftCount);
/*  749 */     getItem().onCraftedBy(this, player);
/*      */   }
/*      */ 
/*      */   
/*  753 */   public void onCraftedBySystem(Level level) { getItem().onCraftedPostProcess(this, level); }
/*      */ 
/*      */ 
/*      */   
/*  757 */   public int getUseDuration(LivingEntity user) { return getItem().getUseDuration(this, user); }
/*      */ 
/*      */ 
/*      */   
/*  761 */   public ItemUseAnimation getUseAnimation() { return getItem().getUseAnimation(this); }
/*      */ 
/*      */   
/*      */   public void releaseUsing(Level level, LivingEntity entity, int remainingTime) {
/*  765 */     ItemStack stackBeforeUsing = copy();
/*  766 */     if (getItem().releaseUsing(this, level, entity, remainingTime)) {
/*  767 */       ItemStack withSideEffects = applyAfterUseComponentSideEffects(entity, stackBeforeUsing);
/*  768 */       if (withSideEffects != this) {
/*  769 */         entity.setItemInHand(entity.getUsedItemHand(), withSideEffects);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void causeUseVibration(Entity causer, Holder.Reference<GameEvent> event) {
/*  775 */     UseEffects useEffects = (UseEffects)get(DataComponents.USE_EFFECTS);
/*  776 */     if (useEffects != null && useEffects.interactVibrations()) {
/*  777 */       causer.gameEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  782 */   public boolean useOnRelease() { return getItem().useOnRelease(this); }
/*      */ 
/*      */ 
/*      */   
/*  786 */   public <T> T set(DataComponentType<T> type, T value) { return (T)this.components.set(type, value); }
/*      */ 
/*      */ 
/*      */   
/*  790 */   public <T> T set(TypedDataComponent<T> value) { return (T)this.components.set(value); }
/*      */ 
/*      */ 
/*      */   
/*  794 */   public <T> void copyFrom(DataComponentType<T> type, DataComponentGetter source) { set(type, source.get(type)); }
/*      */ 
/*      */ 
/*      */   
/*  798 */   public <T, U> T update(DataComponentType<T> type, T defaultValue, U value, BiFunction<T, U, T> combiner) { return (T)set(type, combiner.apply(getOrDefault(type, defaultValue), value)); }
/*      */ 
/*      */   
/*      */   public <T> T update(DataComponentType<T> type, T defaultValue, UnaryOperator<T> function) {
/*  802 */     T value = (T)getOrDefault(type, defaultValue);
/*  803 */     return (T)set(type, function.apply(value));
/*      */   }
/*      */ 
/*      */   
/*  807 */   public <T> T remove(DataComponentType<? extends T> type) { return (T)this.components.remove(type); }
/*      */ 
/*      */   
/*      */   public void applyComponentsAndValidate(DataComponentPatch patch) {
/*  811 */     DataComponentPatch oldPatch = this.components.asPatch();
/*  812 */     this.components.applyPatch(patch);
/*  813 */     Optional<DataResult.Error<ItemStack>> validationError = validateStrict(this).error();
/*  814 */     if (validationError.isPresent()) {
/*  815 */       LOGGER.error("Failed to apply component patch '{}' to item: '{}'", patch, ((DataResult.Error)validationError.get()).message());
/*  816 */       this.components.restorePatch(oldPatch);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  821 */   public void applyComponents(DataComponentPatch patch) { this.components.applyPatch(patch); }
/*      */ 
/*      */ 
/*      */   
/*  825 */   public void applyComponents(DataComponentMap components) { this.components.setAll(components); }
/*      */ 
/*      */   
/*      */   public Component getHoverName() {
/*  829 */     Component customName = getCustomName();
/*  830 */     if (customName != null) {
/*  831 */       return customName;
/*      */     }
/*  833 */     return getItemName();
/*      */   }
/*      */   
/*      */   public Component getCustomName() {
/*  837 */     Component customName = (Component)get(DataComponents.CUSTOM_NAME);
/*  838 */     if (customName != null) {
/*  839 */       return customName;
/*      */     }
/*  841 */     WrittenBookContent content = (WrittenBookContent)get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  842 */     if (content != null) {
/*  843 */       String title = (String)content.title().raw();
/*  844 */       if (!StringUtil.isBlank(title)) {
/*  845 */         return Component.literal(title);
/*      */       }
/*      */     } 
/*  848 */     return null;
/*      */   }
/*      */ 
/*      */   
/*  852 */   public Component getItemName() { return getItem().getName(this); }
/*      */ 
/*      */   
/*      */   public Component getStyledHoverName() {
/*  856 */     MutableComponent hoverName = Component.empty().append(getHoverName()).withStyle(getRarity().color());
/*  857 */     if (has(DataComponents.CUSTOM_NAME)) {
/*  858 */       hoverName.withStyle(ChatFormatting.ITALIC);
/*      */     }
/*  860 */     return hoverName;
/*      */   }
/*      */   
/*      */   public <T extends TooltipProvider> void addToTooltip(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
/*  864 */     T component = (T)(TooltipProvider)get(type);
/*  865 */     if (component != null && display.shows(type)) {
/*  866 */       component.addToTooltip(context, consumer, flag, this.components);
/*      */     }
/*      */   }
/*      */   
/*      */   public List<Component> getTooltipLines(Item.TooltipContext context, Player player, TooltipFlag tooltipFlag) {
/*  871 */     TooltipDisplay display = (TooltipDisplay)getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/*  872 */     if (!tooltipFlag.isCreative() && display.hideTooltip()) {
/*  873 */       boolean shouldPrintOpWarning = getItem().shouldPrintOpWarning(this, player);
/*  874 */       return shouldPrintOpWarning ? OP_NBT_WARNING : List.of();
/*      */     } 
/*      */     
/*  877 */     List<Component> lines = Lists.newArrayList();
/*  878 */     lines.add(getStyledHoverName());
/*  879 */     Objects.requireNonNull(lines); addDetailsToTooltip(context, display, player, tooltipFlag, lines::add);
/*  880 */     return lines;
/*      */   }
/*      */   
/*      */   public void addDetailsToTooltip(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
/*  884 */     getItem().appendHoverText(this, context, display, builder, tooltipFlag);
/*      */     
/*  886 */     addToTooltip(DataComponents.TROPICAL_FISH_PATTERN, context, display, builder, tooltipFlag);
/*  887 */     addToTooltip(DataComponents.INSTRUMENT, context, display, builder, tooltipFlag);
/*  888 */     addToTooltip(DataComponents.MAP_ID, context, display, builder, tooltipFlag);
/*  889 */     addToTooltip(DataComponents.BEES, context, display, builder, tooltipFlag);
/*  890 */     addToTooltip(DataComponents.CONTAINER_LOOT, context, display, builder, tooltipFlag);
/*  891 */     addToTooltip(DataComponents.CONTAINER, context, display, builder, tooltipFlag);
/*  892 */     addToTooltip(DataComponents.BANNER_PATTERNS, context, display, builder, tooltipFlag);
/*  893 */     addToTooltip(DataComponents.POT_DECORATIONS, context, display, builder, tooltipFlag);
/*  894 */     addToTooltip(DataComponents.WRITTEN_BOOK_CONTENT, context, display, builder, tooltipFlag);
/*  895 */     addToTooltip(DataComponents.CHARGED_PROJECTILES, context, display, builder, tooltipFlag);
/*  896 */     addToTooltip(DataComponents.FIREWORKS, context, display, builder, tooltipFlag);
/*  897 */     addToTooltip(DataComponents.FIREWORK_EXPLOSION, context, display, builder, tooltipFlag);
/*  898 */     addToTooltip(DataComponents.POTION_CONTENTS, context, display, builder, tooltipFlag);
/*  899 */     addToTooltip(DataComponents.JUKEBOX_PLAYABLE, context, display, builder, tooltipFlag);
/*  900 */     addToTooltip(DataComponents.TRIM, context, display, builder, tooltipFlag);
/*  901 */     addToTooltip(DataComponents.STORED_ENCHANTMENTS, context, display, builder, tooltipFlag);
/*  902 */     addToTooltip(DataComponents.ENCHANTMENTS, context, display, builder, tooltipFlag);
/*  903 */     addToTooltip(DataComponents.DYED_COLOR, context, display, builder, tooltipFlag);
/*  904 */     addToTooltip(DataComponents.PROFILE, context, display, builder, tooltipFlag);
/*  905 */     addToTooltip(DataComponents.LORE, context, display, builder, tooltipFlag);
/*  906 */     addAttributeTooltips(builder, display, player);
/*      */     
/*  908 */     addUnitComponentToTooltip(DataComponents.INTANGIBLE_PROJECTILE, INTANGIBLE_TOOLTIP, display, builder);
/*  909 */     addUnitComponentToTooltip(DataComponents.UNBREAKABLE, UNBREAKABLE_TOOLTIP, display, builder);
/*      */     
/*  911 */     addToTooltip(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, context, display, builder, tooltipFlag);
/*  912 */     addToTooltip(DataComponents.SUSPICIOUS_STEW_EFFECTS, context, display, builder, tooltipFlag);
/*  913 */     addToTooltip(DataComponents.BLOCK_STATE, context, display, builder, tooltipFlag);
/*  914 */     addToTooltip(DataComponents.ENTITY_DATA, context, display, builder, tooltipFlag);
/*      */ 
/*      */     
/*  917 */     if ((is(Items.SPAWNER) || is(Items.TRIAL_SPAWNER)) && display.shows(DataComponents.BLOCK_ENTITY_DATA)) {
/*  918 */       TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData)get(DataComponents.BLOCK_ENTITY_DATA);
/*  919 */       Spawner.appendHoverText(blockEntityData, builder, "SpawnData");
/*      */     } 
/*      */     
/*  922 */     AdventureModePredicate canBreak = (AdventureModePredicate)get(DataComponents.CAN_BREAK);
/*  923 */     if (canBreak != null && display.shows(DataComponents.CAN_BREAK)) {
/*  924 */       builder.accept(CommonComponents.EMPTY);
/*  925 */       builder.accept(AdventureModePredicate.CAN_BREAK_HEADER);
/*  926 */       canBreak.addToTooltip(builder);
/*      */     } 
/*  928 */     AdventureModePredicate canPlaceOn = (AdventureModePredicate)get(DataComponents.CAN_PLACE_ON);
/*  929 */     if (canPlaceOn != null && display.shows(DataComponents.CAN_PLACE_ON)) {
/*  930 */       builder.accept(CommonComponents.EMPTY);
/*  931 */       builder.accept(AdventureModePredicate.CAN_PLACE_HEADER);
/*  932 */       canPlaceOn.addToTooltip(builder);
/*      */     } 
/*      */     
/*  935 */     if (tooltipFlag.isAdvanced()) {
/*  936 */       if (isDamaged() && display.shows(DataComponents.DAMAGE)) {
/*  937 */         builder.accept(Component.translatable("item.durability", new Object[] { Integer.valueOf(getMaxDamage() - getDamageValue()), Integer.valueOf(getMaxDamage()) }));
/*      */       }
/*  939 */       builder.accept(Component.literal(BuiltInRegistries.ITEM.getKey(getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
/*  940 */       int count = this.components.size();
/*  941 */       if (count > 0) {
/*  942 */         builder.accept(Component.translatable("item.components", new Object[] { Integer.valueOf(count) }).withStyle(ChatFormatting.DARK_GRAY));
/*      */       }
/*      */     } 
/*      */     
/*  946 */     if (player != null && !getItem().isEnabled(player.level().enabledFeatures())) {
/*  947 */       builder.accept(DISABLED_ITEM_TOOLTIP);
/*      */     }
/*      */     
/*  950 */     boolean shouldPrintOpWarning = getItem().shouldPrintOpWarning(this, player);
/*  951 */     if (shouldPrintOpWarning) {
/*  952 */       OP_NBT_WARNING.forEach(builder);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addUnitComponentToTooltip(DataComponentType<?> dataComponentType, Component component, TooltipDisplay display, Consumer<Component> builder) {
/*  957 */     if (has(dataComponentType) && display.shows(dataComponentType)) {
/*  958 */       builder.accept(component);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addAttributeTooltips(Consumer<Component> consumer, TooltipDisplay display, Player player) {
/*  963 */     if (!display.shows(DataComponents.ATTRIBUTE_MODIFIERS)) {
/*      */       return;
/*      */     }
/*  966 */     for (EquipmentSlotGroup slot : EquipmentSlotGroup.values()) {
/*  967 */       MutableBoolean first = new MutableBoolean(true);
/*  968 */       forEachModifier(slot, (attribute, modifier, tooltip) -> {
/*  969 */             if (tooltip == ItemAttributeModifiers.Display.hidden()) {
/*      */               return;
/*      */             }
/*      */             
/*  973 */             if (first.isTrue()) {
/*  974 */               consumer.accept(CommonComponents.EMPTY);
/*  975 */               consumer.accept(Component.translatable("item.modifiers." + slot.getSerializedName()).withStyle(ChatFormatting.GRAY));
/*  976 */               first.setFalse();
/*      */             } 
/*  978 */             tooltip.apply(consumer, player, attribute, modifier);
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasFoil() {
/*  984 */     Boolean enchantmentGlintOverride = (Boolean)get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
/*  985 */     if (enchantmentGlintOverride != null) {
/*  986 */       return enchantmentGlintOverride.booleanValue();
/*      */     }
/*  988 */     return getItem().isFoil(this);
/*      */   }
/*      */   
/*      */   public Rarity getRarity() {
/*  992 */     Rarity baseRarity = (Rarity)getOrDefault(DataComponents.RARITY, Rarity.COMMON);
/*  993 */     if (!isEnchanted()) {
/*  994 */       return baseRarity;
/*      */     }
/*  996 */     switch (baseRarity) { case COMMON: case UNCOMMON: case RARE:  }  return 
/*      */ 
/*      */       
/*  999 */       baseRarity;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnchantable() {
/* 1004 */     if (!has(DataComponents.ENCHANTABLE)) {
/* 1005 */       return false;
/*      */     }
/* 1007 */     ItemEnchantments enchantments = (ItemEnchantments)get(DataComponents.ENCHANTMENTS);
/* 1008 */     return (enchantments != null && enchantments.isEmpty());
/*      */   }
/*      */ 
/*      */   
/* 1012 */   public void enchant(Holder<Enchantment> enchantment, int level) { EnchantmentHelper.updateEnchantments(this, enchantments -> enchantments.upgrade(enchantment, level)); }
/*      */ 
/*      */ 
/*      */   
/* 1016 */   public boolean isEnchanted() { return !((ItemEnchantments)getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty(); }
/*      */ 
/*      */ 
/*      */   
/* 1020 */   public ItemEnchantments getEnchantments() { return (ItemEnchantments)getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY); }
/*      */ 
/*      */ 
/*      */   
/* 1024 */   public boolean isFramed() { return this.entityRepresentation instanceof ItemFrame; }
/*      */ 
/*      */   
/*      */   public void setEntityRepresentation(Entity entity) {
/* 1028 */     if (!isEmpty()) {
/* 1029 */       this.entityRepresentation = entity;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1034 */   public ItemFrame getFrame() { return (this.entityRepresentation instanceof ItemFrame) ? (ItemFrame)getEntityRepresentation() : null; }
/*      */ 
/*      */ 
/*      */   
/* 1038 */   public Entity getEntityRepresentation() { return !isEmpty() ? this.entityRepresentation : null; }
/*      */ 
/*      */   
/*      */   public void forEachModifier(EquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer) {
/* 1042 */     ItemAttributeModifiers modifiers = (ItemAttributeModifiers)getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/* 1043 */     modifiers.forEach(slot, consumer);
/* 1044 */     EnchantmentHelper.forEachModifier(this, slot, (a, b) -> consumer.accept(a, b, ItemAttributeModifiers.Display.attributeModifiers()));
/*      */   }
/*      */   
/*      */   public void forEachModifier(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 1048 */     ItemAttributeModifiers modifiers = (ItemAttributeModifiers)getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/* 1049 */     modifiers.forEach(slot, consumer);
/* 1050 */     EnchantmentHelper.forEachModifier(this, slot, consumer);
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/* 1054 */     MutableComponent hoverName = Component.empty().append(getHoverName());
/* 1055 */     if (has(DataComponents.CUSTOM_NAME)) {
/* 1056 */       hoverName.withStyle(ChatFormatting.ITALIC);
/*      */     }
/*      */     
/* 1059 */     MutableComponent result = ComponentUtils.wrapInSquareBrackets(hoverName);
/*      */     
/* 1061 */     if (!isEmpty()) {
/* 1062 */       result.withStyle(getRarity().color()).withStyle(s -> s.withHoverEvent(new HoverEvent.ShowItem(this)));
/*      */     }
/*      */     
/* 1065 */     return result;
/*      */   }
/*      */ 
/*      */   
/* 1069 */   public SwingAnimation getSwingAnimation() { return (SwingAnimation)getOrDefault(DataComponents.SWING_ANIMATION, SwingAnimation.DEFAULT); }
/*      */ 
/*      */   
/*      */   public boolean canPlaceOnBlockInAdventureMode(BlockInWorld blockInWorld) {
/* 1073 */     AdventureModePredicate canPlaceOn = (AdventureModePredicate)get(DataComponents.CAN_PLACE_ON);
/* 1074 */     return (canPlaceOn != null && canPlaceOn.test(blockInWorld));
/*      */   }
/*      */   
/*      */   public boolean canBreakBlockInAdventureMode(BlockInWorld blockInWorld) {
/* 1078 */     AdventureModePredicate canBreak = (AdventureModePredicate)get(DataComponents.CAN_BREAK);
/* 1079 */     return (canBreak != null && canBreak.test(blockInWorld));
/*      */   }
/*      */ 
/*      */   
/* 1083 */   public int getPopTime() { return this.popTime; }
/*      */ 
/*      */ 
/*      */   
/* 1087 */   public void setPopTime(int popTime) { this.popTime = popTime; }
/*      */ 
/*      */ 
/*      */   
/* 1091 */   public int getCount() { return isEmpty() ? 0 : this.count; }
/*      */ 
/*      */ 
/*      */   
/* 1095 */   public void setCount(int count) { this.count = count; }
/*      */ 
/*      */   
/*      */   public void limitSize(int maxStackSize) {
/* 1099 */     if (!isEmpty() && getCount() > maxStackSize) {
/* 1100 */       setCount(maxStackSize);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/* 1105 */   public void grow(int amount) { setCount(getCount() + amount); }
/*      */ 
/*      */ 
/*      */   
/* 1109 */   public void shrink(int amount) { grow(-amount); }
/*      */ 
/*      */   
/*      */   public void consume(int amount, LivingEntity owner) {
/* 1113 */     if (owner == null || !owner.hasInfiniteMaterials()) {
/* 1114 */       shrink(amount);
/*      */     }
/*      */   }
/*      */   
/*      */   public ItemStack consumeAndReturn(int amount, LivingEntity owner) {
/* 1119 */     ItemStack split = copyWithCount(amount);
/* 1120 */     consume(amount, owner);
/* 1121 */     return split;
/*      */   }
/*      */   
/*      */   public void onUseTick(Level level, LivingEntity livingEntity, int ticksRemaining) {
/* 1125 */     Consumable consumable = (Consumable)get(DataComponents.CONSUMABLE);
/* 1126 */     if (consumable != null && consumable.shouldEmitParticlesAndSounds(ticksRemaining)) {
/* 1127 */       consumable.emitParticlesAndSounds(livingEntity.getRandom(), livingEntity, this, 5);
/*      */     }
/* 1129 */     KineticWeapon kineticWeapon = (KineticWeapon)get(DataComponents.KINETIC_WEAPON);
/* 1130 */     if (kineticWeapon != null && !level.isClientSide()) {
/* 1131 */       kineticWeapon.damageEntities(this, ticksRemaining, livingEntity, livingEntity.getUsedItemHand().asEquipmentSlot());
/*      */       
/*      */       return;
/*      */     } 
/* 1135 */     getItem().onUseTick(level, livingEntity, this, ticksRemaining);
/*      */   }
/*      */ 
/*      */   
/* 1139 */   public void onDestroyed(ItemEntity itemEntity) { getItem().onDestroyed(itemEntity); }
/*      */ 
/*      */   
/*      */   public boolean canBeHurtBy(DamageSource source) {
/* 1143 */     DamageResistant damageResistant = (DamageResistant)get(DataComponents.DAMAGE_RESISTANT);
/* 1144 */     return (damageResistant == null || !damageResistant.isResistantTo(source));
/*      */   }
/*      */   
/*      */   public boolean isValidRepairItem(ItemStack repairItem) {
/* 1148 */     Repairable repairable = (Repairable)get(DataComponents.REPAIRABLE);
/* 1149 */     return (repairable != null && repairable.isValidRepairItem(repairItem));
/*      */   }
/*      */ 
/*      */   
/* 1153 */   public boolean canDestroyBlock(BlockState state, Level level, BlockPos pos, Player player) { return getItem().canDestroyBlock(this, state, level, pos, player); }
/*      */ 
/*      */   
/*      */   public DamageSource getDamageSource(LivingEntity attacker, Supplier<DamageSource> defaultSource) {
/* 1157 */     return (DamageSource)Optional.ofNullable((EitherHolder)get(DataComponents.DAMAGE_TYPE))
/* 1158 */       .flatMap(holder -> holder.unwrap(attacker.registryAccess()))
/* 1159 */       .map(type -> new DamageSource(type, attacker))
/* 1160 */       .or(() -> Optional.ofNullable(getItem().getItemDamageSource(attacker)))
/* 1161 */       .orElseGet(defaultSource);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemStack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */