/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ 
/*     */ public final class Enchantment extends Record {
/*     */   private final Component description;
/*     */   private final EnchantmentDefinition definition;
/*     */   
/*  61 */   public Enchantment(Component description, EnchantmentDefinition definition, HolderSet<Enchantment> exclusiveSet, DataComponentMap effects) { this.description = description; this.definition = definition; this.exclusiveSet = exclusiveSet; this.effects = effects; } private final HolderSet<Enchantment> exclusiveSet; private final DataComponentMap effects; public static final int MAX_LEVEL = 255; public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment;
/*  61 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public EnchantmentDefinition definition() { return this.definition; } public HolderSet<Enchantment> exclusiveSet() { return this.exclusiveSet; } public DataComponentMap effects() { return this.effects; }
/*     */   
/*     */   public static final class Cost
/*     */     extends Record
/*     */   {
/*     */     private final int base;
/*     */     private final int perLevelAboveFirst;
/*     */     
/*  69 */     public Cost(int base, int perLevelAboveFirst) { this.base = base; this.perLevelAboveFirst = perLevelAboveFirst; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*  69 */       //   0	8	1	o	Ljava/lang/Object; } public int base() { return this.base; } public int perLevelAboveFirst() { return this.perLevelAboveFirst; }
/*  70 */     public static final Codec<Cost> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/*  71 */           .fieldOf("base").forGetter(Cost::base), Codec.INT
/*  72 */           .fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst))
/*  73 */         .apply(i, Cost::new));
/*     */ 
/*     */     
/*  76 */     public int calculate(int level) { return this.base + this.perLevelAboveFirst * (level - 1); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static Cost constantCost(int base) { return new Cost(base, 0); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static Cost dynamicCost(int base, int perLevel) { return new Cost(base, perLevel); }
/*     */   public static final class EnchantmentDefinition extends Record { private final HolderSet<Item> supportedItems; private final Optional<HolderSet<Item>> primaryItems; private final int weight; private final int maxLevel; private final Enchantment.Cost minCost; private final Enchantment.Cost maxCost; private final int anvilCost; private final List<EquipmentSlotGroup> slots;
/*     */     
/*  88 */     public EnchantmentDefinition(HolderSet<Item> supportedItems, Optional<HolderSet<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots) { this.supportedItems = supportedItems; this.primaryItems = primaryItems; this.weight = weight; this.maxLevel = maxLevel; this.minCost = minCost; this.maxCost = maxCost; this.anvilCost = anvilCost; this.slots = slots; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public HolderSet<Item> supportedItems() { return this.supportedItems; } public Optional<HolderSet<Item>> primaryItems() { return this.primaryItems; } public int weight() { return this.weight; } public int maxLevel() { return this.maxLevel; } public Enchantment.Cost minCost() { return this.minCost; } public Enchantment.Cost maxCost() { return this.maxCost; } public int anvilCost() { return this.anvilCost; } public List<EquipmentSlotGroup> slots() { return this.slots; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     public static final MapCodec<EnchantmentDefinition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  99 */           RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentDefinition::supportedItems), 
/* 100 */           RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("primary_items").forGetter(EnchantmentDefinition::primaryItems), 
/* 101 */           ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight), 
/* 102 */           ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel), Enchantment.Cost.CODEC
/* 103 */           .fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost), Enchantment.Cost.CODEC
/* 104 */           .fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost), ExtraCodecs.NON_NEGATIVE_INT
/* 105 */           .fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost), EquipmentSlotGroup.CODEC
/* 106 */           .listOf().fieldOf("slots").forGetter(EnchantmentDefinition::slots))
/* 107 */         .apply(i, EnchantmentDefinition::new)); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, HolderSet<Item> primaryItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) { return new EnchantmentDefinition(supportedItems, Optional.of(primaryItems), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots)); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) { return new EnchantmentDefinition(supportedItems, Optional.empty(), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots)); }
/*     */ 
/*     */   
/* 118 */   public static final Codec<Enchantment> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ComponentSerialization.CODEC
/* 119 */         .fieldOf("description").forGetter(Enchantment::description), EnchantmentDefinition.CODEC
/* 120 */         .forGetter(Enchantment::definition), 
/* 121 */         RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("exclusive_set", HolderSet.direct(new Holder[0])).forGetter(Enchantment::exclusiveSet), EnchantmentEffectComponents.CODEC
/* 122 */         .optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(Enchantment::effects))
/* 123 */       .apply(i, Enchantment::new));
/*     */   
/* 125 */   public static final Codec<Holder<Enchantment>> CODEC = RegistryFixedCodec.create(Registries.ENCHANTMENT);
/* 126 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Enchantment>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT);
/*     */   
/*     */   public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity entity) {
/* 129 */     Map<EquipmentSlot, ItemStack> itemStacks = Maps.newEnumMap(EquipmentSlot.class);
/* 130 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 131 */       if (matchingSlot(slot)) {
/* 132 */         ItemStack itemStack = entity.getItemBySlot(slot);
/* 133 */         if (!itemStack.isEmpty()) {
/* 134 */           itemStacks.put(slot, itemStack);
/*     */         }
/*     */       } 
/*     */     } 
/* 138 */     return itemStacks;
/*     */   }
/*     */ 
/*     */   
/* 142 */   public HolderSet<Item> getSupportedItems() { return this.definition.supportedItems(); }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public boolean matchingSlot(EquipmentSlot slot) { return this.definition.slots().stream().anyMatch(group -> group.test(slot)); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public boolean isPrimaryItem(ItemStack item) { return (isSupportedItem(item) && (this.definition.primaryItems.isEmpty() || item.is((HolderSet)this.definition.primaryItems.get()))); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public boolean isSupportedItem(ItemStack item) { return item.is(this.definition.supportedItems); }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public int getWeight() { return this.definition.weight(); }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public int getAnvilCost() { return this.definition.anvilCost(); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public int getMinLevel() { return 1; }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public int getMaxLevel() { return this.definition.maxLevel(); }
/*     */ 
/*     */ 
/*     */   
/* 174 */   public int getMinCost(int level) { return this.definition.minCost().calculate(level); }
/*     */ 
/*     */ 
/*     */   
/* 178 */   public int getMaxCost(int level) { return this.definition.maxCost().calculate(level); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public String toString() { return "Enchantment " + this.description.getString(); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public static boolean areCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other) { return (!enchantment.equals(other) && !((Enchantment)enchantment.value()).exclusiveSet.contains(other) && !((Enchantment)other.value()).exclusiveSet.contains(enchantment)); }
/*     */ 
/*     */   
/*     */   public static Component getFullname(Holder<Enchantment> enchantment, int level) {
/* 192 */     MutableComponent result = ((Enchantment)enchantment.value()).description.copy();
/* 193 */     if (enchantment.is(EnchantmentTags.CURSE)) {
/* 194 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(ChatFormatting.RED));
/*     */     } else {
/* 196 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(ChatFormatting.GRAY));
/*     */     } 
/* 198 */     if (level != 1 || ((Enchantment)enchantment.value()).getMaxLevel() != 1) {
/* 199 */       result.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));
/*     */     }
/* 201 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 205 */   public boolean canEnchant(ItemStack itemStack) { return this.definition.supportedItems().contains(itemStack.getItemHolder()); }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public <T> List<T> getEffects(DataComponentType<List<T>> type) { return (List)this.effects.getOrDefault(type, List.of()); }
/*     */ 
/*     */   
/*     */   public boolean isImmuneToDamage(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 213 */     LootContext context = damageContext(serverLevel, enchantmentLevel, victim, source);
/* 214 */     for (ConditionalEffect<DamageImmunity> filteredEffect : getEffects(EnchantmentEffectComponents.DAMAGE_IMMUNITY)) {
/* 215 */       if (filteredEffect.matches(context)) {
/* 216 */         return true;
/*     */       }
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   public void modifyDamageProtection(ServerLevel serverLevel, int enchantmentLevel, ItemStack item, Entity victim, DamageSource source, MutableFloat protection) {
/* 223 */     LootContext context = damageContext(serverLevel, enchantmentLevel, victim, source);
/* 224 */     for (ConditionalEffect<EnchantmentValueEffect> conditionalEffect : getEffects(EnchantmentEffectComponents.DAMAGE_PROTECTION)) {
/* 225 */       if (conditionalEffect.matches(context)) {
/* 226 */         protection.setValue(((EnchantmentValueEffect)conditionalEffect.effect()).process(enchantmentLevel, victim.getRandom(), protection.floatValue()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 232 */   public void modifyDurabilityChange(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) { modifyItemFilteredCount(EnchantmentEffectComponents.ITEM_DAMAGE, serverLevel, enchantmentLevel, itemStack, change); }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public void modifyAmmoCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) { modifyItemFilteredCount(EnchantmentEffectComponents.AMMO_USE, serverLevel, enchantmentLevel, itemStack, change); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public void modifyPiercingCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) { modifyItemFilteredCount(EnchantmentEffectComponents.PROJECTILE_PIERCING, serverLevel, enchantmentLevel, itemStack, count); }
/*     */ 
/*     */ 
/*     */   
/* 244 */   public void modifyBlockExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) { modifyItemFilteredCount(EnchantmentEffectComponents.BLOCK_EXPERIENCE, serverLevel, enchantmentLevel, itemStack, count); }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public void modifyMobExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity killer, MutableFloat experience) { modifyEntityFilteredValue(EnchantmentEffectComponents.MOB_EXPERIENCE, serverLevel, enchantmentLevel, itemStack, killer, experience); }
/*     */ 
/*     */ 
/*     */   
/* 252 */   public void modifyDurabilityToRepairFromXp(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) { modifyItemFilteredCount(EnchantmentEffectComponents.REPAIR_WITH_XP, serverLevel, enchantmentLevel, itemStack, change); }
/*     */ 
/*     */ 
/*     */   
/* 256 */   public void modifyTridentReturnToOwnerAcceleration(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity trident, MutableFloat count) { modifyEntityFilteredValue(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, serverLevel, enchantmentLevel, itemStack, trident, count); }
/*     */ 
/*     */ 
/*     */   
/* 260 */   public void modifyTridentSpinAttackStrength(RandomSource random, int enchantmentLevel, MutableFloat strength) { modifyUnfilteredValue(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, random, enchantmentLevel, strength); }
/*     */ 
/*     */ 
/*     */   
/* 264 */   public void modifyFishingTimeReduction(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat timeReduction) { modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, serverLevel, enchantmentLevel, itemStack, fisher, timeReduction); }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public void modifyFishingLuckBonus(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat luck) { modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_LUCK_BONUS, serverLevel, enchantmentLevel, itemStack, fisher, luck); }
/*     */ 
/*     */ 
/*     */   
/* 272 */   public void modifyDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) { modifyDamageFilteredValue(EnchantmentEffectComponents.DAMAGE, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount); }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public void modifyFallBasedDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) { modifyDamageFilteredValue(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount); }
/*     */ 
/*     */ 
/*     */   
/* 280 */   public void modifyKnockback(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) { modifyDamageFilteredValue(EnchantmentEffectComponents.KNOCKBACK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount); }
/*     */ 
/*     */ 
/*     */   
/* 284 */   public void modifyArmorEffectivness(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) { modifyDamageFilteredValue(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount); }
/*     */ 
/*     */   
/*     */   public void doPostAttack(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, EnchantmentTarget forTarget, Entity victim, DamageSource damageSource) {
/* 288 */     for (TargetedConditionalEffect<EnchantmentEntityEffect> effect : getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
/* 289 */       if (forTarget == effect.enchanted()) {
/* 290 */         doPostAttack(effect, serverLevel, enchantmentLevel, item, victim, damageSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void doPostAttack(TargetedConditionalEffect<EnchantmentEntityEffect> effect, ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity victim, DamageSource damageSource) {
/* 296 */     if (effect.matches(damageContext(serverLevel, enchantmentLevel, victim, damageSource))) {
/* 297 */       switch (effect.affected()) { default: throw new MatchException(null, null);
/*     */         case ATTACKER: 
/*     */         case DAMAGING_ENTITY: 
/* 300 */         case VICTIM: break; }  Entity target = victim;
/*     */       
/* 302 */       if (target != null) {
/* 303 */         ((EnchantmentEntityEffect)effect.effect()).apply(serverLevel, enchantmentLevel, item, target, target.position());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void doLunge(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity user) {
/* 309 */     applyEffects(
/* 310 */         getEffects(EnchantmentEffectComponents.POST_PIERCING_ATTACK), 
/* 311 */         entityContext(serverLevel, enchantmentLevel, user, user.position()), e -> 
/* 312 */         e.apply(serverLevel, enchantmentLevel, item, user, user.position()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public void modifyProjectileCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat count) { modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_COUNT, serverLevel, enchantmentLevel, weapon, shooter, count); }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public void modifyProjectileSpread(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat angle) { modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_SPREAD, serverLevel, enchantmentLevel, weapon, shooter, angle); }
/*     */ 
/*     */ 
/*     */   
/* 325 */   public void modifyCrossbowChargeTime(RandomSource random, int enchantmentLevel, MutableFloat time) { modifyUnfilteredValue(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, random, enchantmentLevel, time); }
/*     */ 
/*     */   
/*     */   public void modifyUnfilteredValue(DataComponentType<EnchantmentValueEffect> component, RandomSource random, int enchantmentLevel, MutableFloat value) {
/* 329 */     EnchantmentValueEffect effect = (EnchantmentValueEffect)this.effects.get(component);
/* 330 */     if (effect != null) {
/* 331 */       value.setValue(effect.process(enchantmentLevel, random, value.floatValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity) {
/* 336 */     applyEffects(
/* 337 */         getEffects(EnchantmentEffectComponents.TICK), 
/* 338 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), e -> 
/* 339 */         e.apply(serverLevel, enchantmentLevel, item, entity, entity.position()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileSpawned(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile) {
/* 344 */     applyEffects(
/* 345 */         getEffects(EnchantmentEffectComponents.PROJECTILE_SPAWNED), 
/* 346 */         entityContext(serverLevel, enchantmentLevel, projectile, projectile.position()), e -> 
/* 347 */         e.apply(serverLevel, enchantmentLevel, weapon, projectile, projectile.position()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onHitBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile, Vec3 position, BlockState hitBlock) {
/* 352 */     applyEffects(
/* 353 */         getEffects(EnchantmentEffectComponents.HIT_BLOCK), 
/* 354 */         blockHitContext(serverLevel, enchantmentLevel, projectile, position, hitBlock), e -> 
/* 355 */         e.apply(serverLevel, enchantmentLevel, weapon, projectile, position));
/*     */   }
/*     */ 
/*     */   
/*     */   private void modifyItemFilteredCount(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat value) {
/* 360 */     applyEffects(
/* 361 */         getEffects(effectType), 
/* 362 */         itemContext(serverLevel, enchantmentLevel, itemStack), e -> 
/* 363 */         value.setValue(e.process(enchantmentLevel, serverLevel.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   private void modifyEntityFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity entity, MutableFloat value) {
/* 368 */     applyEffects(
/* 369 */         getEffects(effectType), 
/* 370 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), e -> 
/* 371 */         value.setValue(e.process(enchantmentLevel, entity.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   private void modifyDamageFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat value) {
/* 376 */     applyEffects(
/* 377 */         getEffects(effectType), 
/* 378 */         damageContext(serverLevel, enchantmentLevel, victim, damageSource), e -> 
/* 379 */         value.setValue(e.process(enchantmentLevel, victim.getRandom(), value.floatValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootContext damageContext(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 391 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, victim).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, victim.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity()).create(LootContextParamSets.ENCHANTED_DAMAGE);
/* 392 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootContext itemContext(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack) {
/* 399 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.TOOL, itemStack).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).create(LootContextParamSets.ENCHANTED_ITEM);
/* 400 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootContext locationContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, boolean active) {
/* 409 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.ENCHANTMENT_ACTIVE, Boolean.valueOf(active)).create(LootContextParamSets.ENCHANTED_LOCATION);
/* 410 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootContext entityContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position) {
/* 418 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, position).create(LootContextParamSets.ENCHANTED_ENTITY);
/* 419 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootContext blockHitContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position, BlockState hitBlock) {
/* 428 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, position).withParameter(LootContextParams.BLOCK_STATE, hitBlock).create(LootContextParamSets.HIT_BLOCK);
/* 429 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext filterData, Consumer<T> action) {
/* 433 */     for (ConditionalEffect<T> conditionalEffect : effects) {
/* 434 */       if (conditionalEffect.matches(filterData)) {
/* 435 */         action.accept(conditionalEffect.effect());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runLocationChangedEffects(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/* 441 */     EquipmentSlot slot = item.inSlot();
/* 442 */     if (slot == null) {
/*     */       return;
/*     */     }
/* 445 */     Map<Enchantment, Set<EnchantmentLocationBasedEffect>> activeLocationDependentEffects = entity.activeLocationDependentEnchantments(slot);
/* 446 */     if (!matchingSlot(slot)) {
/* 447 */       Set<EnchantmentLocationBasedEffect> activeEffects = (Set)activeLocationDependentEffects.remove(this);
/* 448 */       if (activeEffects != null) {
/* 449 */         activeEffects.forEach(effect -> effect.onDeactivated(item, entity, entity.position(), enchantmentLevel));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 454 */     ObjectArraySet objectArraySet = (Set)activeLocationDependentEffects.get(this);
/* 455 */     for (ConditionalEffect<EnchantmentLocationBasedEffect> filteredEffect : getEffects(EnchantmentEffectComponents.LOCATION_CHANGED)) {
/* 456 */       EnchantmentLocationBasedEffect effect = (EnchantmentLocationBasedEffect)filteredEffect.effect();
/* 457 */       boolean wasActive = (objectArraySet != null && objectArraySet.contains(effect));
/* 458 */       if (filteredEffect.matches(locationContext(serverLevel, enchantmentLevel, entity, wasActive))) {
/* 459 */         if (!wasActive) {
/* 460 */           if (objectArraySet == null) {
/* 461 */             objectArraySet = new ObjectArraySet();
/* 462 */             activeLocationDependentEffects.put(this, objectArraySet);
/*     */           } 
/* 464 */           objectArraySet.add(effect);
/*     */         } 
/* 466 */         effect.onChangedBlock(serverLevel, enchantmentLevel, item, entity, entity.position(), !wasActive); continue;
/* 467 */       }  if (objectArraySet != null && objectArraySet.remove(effect)) {
/* 468 */         effect.onDeactivated(item, entity, entity.position(), enchantmentLevel);
/*     */       }
/*     */     } 
/* 471 */     if (objectArraySet != null && objectArraySet.isEmpty()) {
/* 472 */       activeLocationDependentEffects.remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopLocationBasedEffects(int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/* 477 */     EquipmentSlot slot = item.inSlot();
/* 478 */     if (slot == null) {
/*     */       return;
/*     */     }
/* 481 */     Set<EnchantmentLocationBasedEffect> activeEffects = (Set)entity.activeLocationDependentEnchantments(slot).remove(this);
/* 482 */     if (activeEffects == null) {
/*     */       return;
/*     */     }
/*     */     
/* 486 */     for (EnchantmentLocationBasedEffect effect : activeEffects)
/* 487 */       effect.onDeactivated(item, entity, entity.position(), enchantmentLevel); 
/*     */   }
/*     */   public static class Builder { private final Enchantment.EnchantmentDefinition definition; private HolderSet<Enchantment> exclusiveSet; private final Map<DataComponentType<?>, List<?>> effectLists;
/*     */     private final DataComponentMap.Builder effectMapBuilder;
/*     */     
/*     */     public Builder(Enchantment.EnchantmentDefinition definition) {
/* 493 */       this.exclusiveSet = HolderSet.direct(new Holder[0]);
/* 494 */       this.effectLists = new HashMap();
/* 495 */       this.effectMapBuilder = DataComponentMap.builder();
/*     */ 
/*     */       
/* 498 */       this.definition = definition;
/*     */     }
/*     */     
/*     */     public Builder exclusiveWith(HolderSet<Enchantment> set) {
/* 502 */       this.exclusiveSet = set;
/* 503 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
/* 507 */       getEffectsList(type).add(new ConditionalEffect(effect, Optional.of(condition.build())));
/* 508 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
/* 512 */       getEffectsList(type).add(new ConditionalEffect(effect, Optional.empty()));
/* 513 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect, LootItemCondition.Builder condition) {
/* 517 */       getEffectsList(type).add(new TargetedConditionalEffect(enchanted, affected, effect, Optional.of(condition.build())));
/* 518 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect) {
/* 522 */       getEffectsList(type).add(new TargetedConditionalEffect(enchanted, affected, effect, Optional.empty()));
/* 523 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> type, EnchantmentAttributeEffect effect) {
/* 527 */       getEffectsList(type).add(effect);
/* 528 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <E> Builder withSpecialEffect(DataComponentType<E> type, E effect) {
/* 533 */       this.effectMapBuilder.set(type, effect);
/* 534 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<Unit> type) {
/* 538 */       this.effectMapBuilder.set(type, Unit.INSTANCE);
/* 539 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private <E> List<E> getEffectsList(DataComponentType<List<E>> type) {
/* 544 */       return (List)this.effectLists.computeIfAbsent(type, k -> {
/* 545 */             ArrayList<E> newList = new ArrayList<E>();
/* 546 */             this.effectMapBuilder.set(type, newList);
/* 547 */             return newList;
/*     */           });
/*     */     }
/*     */ 
/*     */     
/* 552 */     public Enchantment build(Identifier descriptionKey) { return new Enchantment(Component.translatable(Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 557 */   public static Builder enchantment(EnchantmentDefinition definition) { return new Builder(definition); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */