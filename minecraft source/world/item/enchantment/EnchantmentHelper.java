/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedRandom;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class EnchantmentHelper {
/*     */   public static int getItemEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack piece) {
/*  51 */     ItemEnchantments enchantments = (ItemEnchantments)piece.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/*  52 */     return enchantments.getLevel(enchantment);
/*     */   }
/*     */   
/*     */   public static ItemEnchantments updateEnchantments(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer) {
/*  56 */     DataComponentType<ItemEnchantments> componentType = getComponentType(itemStack);
/*  57 */     ItemEnchantments oldEnchantments = (ItemEnchantments)itemStack.get(componentType);
/*  58 */     if (oldEnchantments == null) {
/*  59 */       return ItemEnchantments.EMPTY;
/*     */     }
/*  61 */     ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(oldEnchantments);
/*  62 */     consumer.accept(mutableEnchantments);
/*  63 */     ItemEnchantments newEnchantments = mutableEnchantments.toImmutable();
/*  64 */     itemStack.set(componentType, newEnchantments);
/*  65 */     return newEnchantments;
/*     */   }
/*     */ 
/*     */   
/*  69 */   public static boolean canStoreEnchantments(ItemStack itemStack) { return itemStack.has(getComponentType(itemStack)); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static void setEnchantments(ItemStack itemStack, ItemEnchantments enchantments) { itemStack.set(getComponentType(itemStack), enchantments); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static ItemEnchantments getEnchantmentsForCrafting(ItemStack itemStack) { return (ItemEnchantments)itemStack.getOrDefault(getComponentType(itemStack), ItemEnchantments.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static DataComponentType<ItemEnchantments> getComponentType(ItemStack itemStack) { return itemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS; }
/*     */ 
/*     */   
/*     */   public static boolean hasAnyEnchantments(ItemStack itemStack) {
/*  85 */     return (!((ItemEnchantments)itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty() || 
/*  86 */       !((ItemEnchantments)itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty());
/*     */   }
/*     */   
/*     */   public static int processDurabilityChange(ServerLevel serverLevel, ItemStack itemStack, int amount) {
/*  90 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/*  91 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDurabilityChange(serverLevel, level, itemStack, modifiedAmount));
/*  92 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processAmmoUse(ServerLevel serverLevel, ItemStack weapon, ItemStack ammo, int amount) {
/*  96 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/*  97 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyAmmoCount(serverLevel, level, ammo, modifiedAmount));
/*  98 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processBlockExperience(ServerLevel serverLevel, ItemStack itemStack, int amount) {
/* 102 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/* 103 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyBlockExperience(serverLevel, level, itemStack, modifiedAmount));
/* 104 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processMobExperience(ServerLevel serverLevel, Entity killer, Entity killed, int amount) {
/* 108 */     if (killer instanceof LivingEntity) { LivingEntity livingKiller = (LivingEntity)killer;
/* 109 */       MutableFloat modifiedAmount = new MutableFloat(amount);
/* 110 */       runIterationOnEquipment(livingKiller, (enchantment, level, item) -> ((Enchantment)enchantment.value()).modifyMobExperience(serverLevel, level, item.itemStack(), killed, modifiedAmount));
/* 111 */       return modifiedAmount.intValue(); }
/*     */     
/* 113 */     return amount;
/*     */   }
/*     */   
/*     */   public static ItemStack createBook(EnchantmentInstance enchant) {
/* 117 */     ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
/* 118 */     itemStack.enchant(enchant.enchantment(), enchant.level());
/* 119 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIterationOnItem(ItemStack piece, EnchantmentVisitor method) {
/* 128 */     ItemEnchantments enchantments = (ItemEnchantments)piece.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 129 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 130 */       method.accept((Holder)entry.getKey(), entry.getIntValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIterationOnItem(ItemStack piece, EquipmentSlot slot, LivingEntity owner, EnchantmentInSlotVisitor method) {
/* 140 */     if (piece.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     ItemEnchantments itemEnchantments = (ItemEnchantments)piece.get(DataComponents.ENCHANTMENTS);
/* 145 */     if (itemEnchantments == null || itemEnchantments.isEmpty()) {
/*     */       return;
/*     */     }
/* 148 */     EnchantedItemInUse itemInUse = new EnchantedItemInUse(piece, slot, owner);
/* 149 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 150 */       Holder<Enchantment> enchantment = (Holder)entry.getKey();
/* 151 */       if (((Enchantment)enchantment.value()).matchingSlot(slot)) {
/* 152 */         method.accept(enchantment, entry.getIntValue(), itemInUse);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void runIterationOnEquipment(LivingEntity owner, EnchantmentInSlotVisitor method) {
/* 158 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 159 */       runIterationOnItem(owner.getItemBySlot(slot), slot, owner, method);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isImmuneToDamage(ServerLevel serverLevel, LivingEntity victim, DamageSource source) {
/* 164 */     MutableBoolean result = new MutableBoolean();
/* 165 */     runIterationOnEquipment(victim, (enchantment, level, item) -> result.setValue((result.isTrue() || ((Enchantment)enchantment.value()).isImmuneToDamage(serverLevel, level, victim, source))));
/* 166 */     return result.isTrue();
/*     */   }
/*     */   
/*     */   public static float getDamageProtection(ServerLevel serverLevel, LivingEntity victim, DamageSource source) {
/* 170 */     MutableFloat result = new MutableFloat(0.0F);
/* 171 */     runIterationOnEquipment(victim, (enchantment, level, item) -> ((Enchantment)enchantment.value()).modifyDamageProtection(serverLevel, level, item.itemStack(), victim, source, result));
/* 172 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyDamage(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float damage) {
/* 176 */     MutableFloat result = new MutableFloat(damage);
/* 177 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDamage(serverLevel, level, itemStack, victim, damageSource, result));
/* 178 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyFallBasedDamage(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float damage) {
/* 182 */     MutableFloat result = new MutableFloat(damage);
/* 183 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFallBasedDamage(serverLevel, level, itemStack, victim, damageSource, result));
/* 184 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyArmorEffectiveness(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float armorFraction) {
/* 188 */     MutableFloat result = new MutableFloat(armorFraction);
/* 189 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyArmorEffectivness(serverLevel, level, itemStack, victim, damageSource, result));
/* 190 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyKnockback(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float knockback) {
/* 194 */     MutableFloat result = new MutableFloat(knockback);
/* 195 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyKnockback(serverLevel, level, itemStack, victim, damageSource, result));
/* 196 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffects(ServerLevel serverLevel, Entity victim, DamageSource damageSource) {
/* 200 */     Entity entity = damageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity attacker = (LivingEntity)entity;
/* 201 */       doPostAttackEffectsWithItemSource(serverLevel, victim, damageSource, attacker.getWeaponItem()); }
/*     */     else
/* 203 */     { doPostAttackEffectsWithItemSource(serverLevel, victim, damageSource, null); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void doLungeEffects(ServerLevel serverLevel, Entity entity) {
/* 208 */     if (entity instanceof LivingEntity) { LivingEntity user = (LivingEntity)entity;
/* 209 */       runIterationOnItem(entity.getWeaponItem(), EquipmentSlot.MAINHAND, user, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doLunge(serverLevel, level, item, entity)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 214 */   public static void doPostAttackEffectsWithItemSource(ServerLevel serverLevel, Entity victim, DamageSource damageSource, ItemStack source) { doPostAttackEffectsWithItemSourceOnBreak(serverLevel, victim, damageSource, source, null); }
/*     */ 
/*     */   
/*     */   public static void doPostAttackEffectsWithItemSourceOnBreak(ServerLevel serverLevel, Entity victim, DamageSource damageSource, ItemStack source, Consumer<Item> attackerlessOnBreak) {
/* 218 */     if (victim instanceof LivingEntity) { LivingEntity livingVictim = (LivingEntity)victim;
/* 219 */       runIterationOnEquipment(livingVictim, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.VICTIM, victim, damageSource)); }
/*     */     
/* 221 */     if (source != null) {
/* 222 */       Entity entity = damageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity attacker = (LivingEntity)entity;
/* 223 */         runIterationOnItem(source, EquipmentSlot.MAINHAND, attacker, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.ATTACKER, victim, damageSource)); }
/* 224 */       else if (attackerlessOnBreak != null)
/* 225 */       { EnchantedItemInUse item = new EnchantedItemInUse(source, null, null, attackerlessOnBreak);
/* 226 */         runIterationOnItem(source, (enchantment, level) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.ATTACKER, victim, damageSource)); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 232 */   public static void runLocationChangedEffects(ServerLevel serverLevel, LivingEntity entity) { runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).runLocationChangedEffects(serverLevel, level, item, entity)); }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public static void runLocationChangedEffects(ServerLevel serverLevel, ItemStack stack, LivingEntity entity, EquipmentSlot slot) { runIterationOnItem(stack, slot, entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).runLocationChangedEffects(serverLevel, level, item, entity)); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public static void stopLocationBasedEffects(LivingEntity entity) { runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).stopLocationBasedEffects(level, item, entity)); }
/*     */ 
/*     */ 
/*     */   
/* 244 */   public static void stopLocationBasedEffects(ItemStack stack, LivingEntity entity, EquipmentSlot slot) { runIterationOnItem(stack, slot, entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).stopLocationBasedEffects(level, item, entity)); }
/*     */ 
/*     */ 
/*     */   
/* 248 */   public static void tickEffects(ServerLevel serverLevel, LivingEntity entity) { runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).tick(serverLevel, level, item, entity)); }
/*     */ 
/*     */   
/*     */   public static int getEnchantmentLevel(Holder<Enchantment> enchantment, LivingEntity entity) {
/* 252 */     Iterable<ItemStack> allowedSlots = ((Enchantment)enchantment.value()).getSlotItems(entity).values();
/* 253 */     int bestLevel = 0;
/* 254 */     for (ItemStack piece : allowedSlots) {
/* 255 */       int newLevel = getItemEnchantmentLevel(enchantment, piece);
/* 256 */       if (newLevel > bestLevel) {
/* 257 */         bestLevel = newLevel;
/*     */       }
/*     */     } 
/* 260 */     return bestLevel;
/*     */   }
/*     */   
/*     */   public static int processProjectileCount(ServerLevel serverLevel, ItemStack weapon, Entity shooter, int count) {
/* 264 */     MutableFloat modifiedCount = new MutableFloat(count);
/* 265 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyProjectileCount(serverLevel, level, weapon, shooter, modifiedCount));
/* 266 */     return Math.max(0, modifiedCount.intValue());
/*     */   }
/*     */   
/*     */   public static float processProjectileSpread(ServerLevel serverLevel, ItemStack weapon, Entity shooter, float angle) {
/* 270 */     MutableFloat modifiedAngle = new MutableFloat(angle);
/* 271 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyProjectileSpread(serverLevel, level, weapon, shooter, modifiedAngle));
/* 272 */     return Math.max(0.0F, modifiedAngle.floatValue());
/*     */   }
/*     */   
/*     */   public static int getPiercingCount(ServerLevel serverLevel, ItemStack weapon, ItemStack ammo) {
/* 276 */     MutableFloat modifiedAmount = new MutableFloat(0.0F);
/* 277 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyPiercingCount(serverLevel, level, ammo, modifiedAmount));
/* 278 */     return Math.max(0, modifiedAmount.intValue());
/*     */   }
/*     */   
/*     */   public static void onProjectileSpawned(ServerLevel serverLevel, ItemStack weapon, Projectile projectileEntity, Consumer<Item> onBreak) {
/* 282 */     Entity entity = projectileEntity.getOwner(); LivingEntity le = (LivingEntity)entity, owner = (entity instanceof LivingEntity) ? le : null;
/* 283 */     EnchantedItemInUse item = new EnchantedItemInUse(weapon, null, owner, onBreak);
/* 284 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).onProjectileSpawned(serverLevel, level, item, projectileEntity));
/*     */   }
/*     */   
/*     */   public static void onHitBlock(ServerLevel serverLevel, ItemStack weapon, LivingEntity owner, Entity entity, EquipmentSlot slot, Vec3 hitLocation, BlockState hitBlock, Consumer<Item> onBreak) {
/* 288 */     EnchantedItemInUse item = new EnchantedItemInUse(weapon, slot, owner, onBreak);
/* 289 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).onHitBlock(serverLevel, level, item, entity, hitLocation, hitBlock));
/*     */   }
/*     */   
/*     */   public static int modifyDurabilityToRepairFromXp(ServerLevel serverLevel, ItemStack item, int durability) {
/* 293 */     MutableFloat modifiedDurability = new MutableFloat(durability);
/* 294 */     runIterationOnItem(item, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDurabilityToRepairFromXp(serverLevel, level, item, modifiedDurability));
/* 295 */     return Math.max(0, modifiedDurability.intValue());
/*     */   }
/*     */   
/*     */   public static float processEquipmentDropChance(ServerLevel serverLevel, LivingEntity entity, DamageSource killingBlow, float chance) {
/* 299 */     MutableFloat modifiedChance = new MutableFloat(chance);
/* 300 */     RandomSource random = entity.getRandom();
/* 301 */     runIterationOnEquipment(entity, (enchantment, level, item) -> {
/* 302 */           LootContext context = Enchantment.damageContext(serverLevel, level, entity, killingBlow);
/* 303 */           ((Enchantment)enchantment.value()).getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     Entity attacker = killingBlow.getEntity();
/* 312 */     if (attacker instanceof LivingEntity) { LivingEntity livingAttacker = (LivingEntity)attacker;
/* 313 */       runIterationOnEquipment(livingAttacker, (enchantment, level, item) -> {
/* 314 */             LootContext context = Enchantment.damageContext(serverLevel, level, entity, killingBlow);
/* 315 */             ((Enchantment)enchantment.value()).getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */           }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     return modifiedChance.floatValue();
/*     */   }
/*     */   
/*     */   public static void forEachModifier(ItemStack itemStack, EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 329 */     runIterationOnItem(itemStack, (enchantment, level) -> (
/*     */         
/* 331 */         (Enchantment)enchantment.value()).getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void forEachModifier(ItemStack itemStack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 342 */     runIterationOnItem(itemStack, (enchantment, level) -> (
/*     */         
/* 344 */         (Enchantment)enchantment.value()).getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFishingLuckBonus(ServerLevel serverLevel, ItemStack rod, Entity fisher) {
/* 355 */     MutableFloat modifiedSpeed = new MutableFloat(0.0F);
/* 356 */     runIterationOnItem(rod, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFishingLuckBonus(serverLevel, level, rod, fisher, modifiedSpeed));
/* 357 */     return Math.max(0, modifiedSpeed.intValue());
/*     */   }
/*     */   
/*     */   public static float getFishingTimeReduction(ServerLevel serverLevel, ItemStack rod, Entity fisher) {
/* 361 */     MutableFloat modifiedSpeed = new MutableFloat(0.0F);
/* 362 */     runIterationOnItem(rod, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFishingTimeReduction(serverLevel, level, rod, fisher, modifiedSpeed));
/* 363 */     return Math.max(0.0F, modifiedSpeed.floatValue());
/*     */   }
/*     */   
/*     */   public static int getTridentReturnToOwnerAcceleration(ServerLevel serverLevel, ItemStack weapon, Entity trident) {
/* 367 */     MutableFloat modifiedAcceleration = new MutableFloat(0.0F);
/* 368 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyTridentReturnToOwnerAcceleration(serverLevel, level, weapon, trident, modifiedAcceleration));
/* 369 */     return Math.max(0, modifiedAcceleration.intValue());
/*     */   }
/*     */   
/*     */   public static float modifyCrossbowChargingTime(ItemStack crossbow, LivingEntity holder, float time) {
/* 373 */     MutableFloat modifiedTime = new MutableFloat(time);
/* 374 */     runIterationOnItem(crossbow, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyCrossbowChargeTime(holder.getRandom(), level, modifiedTime));
/* 375 */     return Math.max(0.0F, modifiedTime.floatValue());
/*     */   }
/*     */   
/*     */   public static float getTridentSpinAttackStrength(ItemStack trident, LivingEntity holder) {
/* 379 */     MutableFloat strength = new MutableFloat(0.0F);
/* 380 */     runIterationOnItem(trident, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyTridentSpinAttackStrength(holder.getRandom(), level, strength));
/* 381 */     return strength.floatValue();
/*     */   }
/*     */   
/*     */   public static boolean hasTag(ItemStack item, TagKey<Enchantment> tag) {
/* 385 */     ItemEnchantments enchantments = (ItemEnchantments)item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 386 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 387 */       Holder<Enchantment> enchantment = (Holder)entry.getKey();
/* 388 */       if (enchantment.is(tag)) {
/* 389 */         return true;
/*     */       }
/*     */     } 
/* 392 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean has(ItemStack item, DataComponentType<?> effectType) {
/* 396 */     MutableBoolean found = new MutableBoolean(false);
/* 397 */     runIterationOnItem(item, (enchantment, level) -> {
/* 398 */           if (((Enchantment)enchantment.value()).effects().has(effectType)) {
/* 399 */             found.setTrue();
/*     */           }
/*     */         });
/* 402 */     return found.booleanValue();
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> pickHighestLevel(ItemStack itemStack, DataComponentType<List<T>> componentType) {
/* 406 */     Pair<List<T>, Integer> picked = getHighestLevel(itemStack, componentType);
/* 407 */     if (picked != null) {
/* 408 */       List<T> list = (List)picked.getFirst();
/* 409 */       int enchantmentLevel = ((Integer)picked.getSecond()).intValue();
/* 410 */       return Optional.of(list.get(Math.min(enchantmentLevel, list.size()) - 1));
/*     */     } 
/* 412 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static <T> Pair<T, Integer> getHighestLevel(ItemStack item, DataComponentType<T> effectType) {
/* 416 */     MutableObject<Pair<T, Integer>> found = new MutableObject<Pair<T, Integer>>();
/* 417 */     runIterationOnItem(item, (enchantment, level) -> {
/* 418 */           if (found.get() == null || ((Integer)((Pair)found.get()).getSecond()).intValue() < level) {
/* 419 */             T effect = (T)((Enchantment)enchantment.value()).effects().get(effectType);
/* 420 */             if (effect != null) {
/* 421 */               found.setValue(Pair.of(effect, Integer.valueOf(level)));
/*     */             }
/*     */           } 
/*     */         });
/* 425 */     return (Pair)found.get();
/*     */   }
/*     */   
/*     */   public static Optional<EnchantedItemInUse> getRandomItemWith(DataComponentType<?> componentType, LivingEntity source, Predicate<ItemStack> predicate) {
/* 429 */     List<EnchantedItemInUse> items = new ArrayList<EnchantedItemInUse>();
/* 430 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 431 */       ItemStack item = source.getItemBySlot(slot);
/* 432 */       if (!predicate.test(item)) {
/*     */         continue;
/*     */       }
/* 435 */       ItemEnchantments enchantments = (ItemEnchantments)item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 436 */       for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 437 */         Holder<Enchantment> enchantment = (Holder)entry.getKey();
/* 438 */         if (((Enchantment)enchantment.value()).effects().has(componentType) && ((Enchantment)enchantment.value()).matchingSlot(slot)) {
/* 439 */           items.add(new EnchantedItemInUse(item, slot, source));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 444 */     return Util.getRandomSafe(items, source.getRandom());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getEnchantmentCost(RandomSource random, int slot, int bookcases, ItemStack itemStack) {
/* 455 */     Enchantable enchantable = (Enchantable)itemStack.get(DataComponents.ENCHANTABLE);
/* 456 */     if (enchantable == null)
/*     */     {
/* 458 */       return 0;
/*     */     }
/*     */     
/* 461 */     if (bookcases > 15) {
/* 462 */       bookcases = 15;
/*     */     }
/* 464 */     int selected = random.nextInt(8) + 1 + (bookcases >> 1) + random.nextInt(bookcases + 1);
/* 465 */     if (slot == 0) {
/* 466 */       return Math.max(selected / 3, 1);
/*     */     }
/* 468 */     if (slot == 1) {
/* 469 */       return selected * 2 / 3 + 1;
/*     */     }
/* 471 */     return Math.max(selected, bookcases * 2);
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource random, ItemStack itemStack, int enchantmentCost, RegistryAccess registryAccess, Optional<? extends HolderSet<Enchantment>> set) {
/* 475 */     return enchantItem(random, itemStack, enchantmentCost, (Stream)set.map(HolderSet::stream).orElseGet(() -> registryAccess.lookupOrThrow(Registries.ENCHANTMENT).listElements().map(())));
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource random, ItemStack itemStack, int enchantmentCost, Stream<Holder<Enchantment>> source) {
/* 479 */     List<EnchantmentInstance> enchants = selectEnchantment(random, itemStack, enchantmentCost, source);
/* 480 */     if (itemStack.is(Items.BOOK)) {
/* 481 */       itemStack = new ItemStack(Items.ENCHANTED_BOOK);
/*     */     }
/*     */     
/* 484 */     for (EnchantmentInstance enchant : enchants) {
/* 485 */       itemStack.enchant(enchant.enchantment(), enchant.level());
/*     */     }
/*     */     
/* 488 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> selectEnchantment(RandomSource random, ItemStack itemStack, int enchantmentCost, Stream<Holder<Enchantment>> source) {
/* 492 */     List<EnchantmentInstance> results = Lists.newArrayList();
/*     */ 
/*     */     
/* 495 */     Enchantable enchantable = (Enchantable)itemStack.get(DataComponents.ENCHANTABLE);
/* 496 */     if (enchantable == null) {
/* 497 */       return results;
/*     */     }
/*     */     
/* 500 */     enchantmentCost += 1 + random.nextInt(enchantable.value() / 4 + 1) + random.nextInt(enchantable.value() / 4 + 1);
/*     */ 
/*     */     
/* 503 */     float randomSpan = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
/* 504 */     enchantmentCost = Mth.clamp(Math.round(enchantmentCost + enchantmentCost * randomSpan), 1, 2147483647);
/*     */     
/* 506 */     List<EnchantmentInstance> enchantments = getAvailableEnchantmentResults(enchantmentCost, itemStack, source);
/* 507 */     if (!enchantments.isEmpty()) {
/* 508 */       Objects.requireNonNull(results); WeightedRandom.getRandomItem(random, enchantments, EnchantmentInstance::weight).ifPresent(results::add);
/*     */       
/* 510 */       while (random.nextInt(50) <= enchantmentCost) {
/* 511 */         if (!results.isEmpty()) {
/* 512 */           filterCompatibleEnchantments(enchantments, (EnchantmentInstance)results.getLast());
/*     */         }
/*     */         
/* 515 */         if (enchantments.isEmpty()) {
/*     */           break;
/*     */         }
/*     */         
/* 519 */         Objects.requireNonNull(results); WeightedRandom.getRandomItem(random, enchantments, EnchantmentInstance::weight).ifPresent(results::add);
/* 520 */         enchantmentCost /= 2;
/*     */       } 
/*     */     } 
/* 523 */     return results;
/*     */   }
/*     */ 
/*     */   
/* 527 */   public static void filterCompatibleEnchantments(List<EnchantmentInstance> enchants, EnchantmentInstance target) { enchants.removeIf(e -> !Enchantment.areCompatible(target.enchantment(), e.enchantment())); }
/*     */ 
/*     */   
/*     */   public static boolean isEnchantmentCompatible(Collection<Holder<Enchantment>> enchants, Holder<Enchantment> target) {
/* 531 */     for (Holder<Enchantment> existing : enchants) {
/* 532 */       if (!Enchantment.areCompatible(existing, target)) {
/* 533 */         return false;
/*     */       }
/*     */     } 
/* 536 */     return true;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> getAvailableEnchantmentResults(int value, ItemStack itemStack, Stream<Holder<Enchantment>> source) {
/* 540 */     List<EnchantmentInstance> results = Lists.newArrayList();
/*     */     
/* 542 */     boolean isBook = itemStack.is(Items.BOOK);
/* 543 */     source
/*     */       
/* 545 */       .filter(enchantment -> (((Enchantment)enchantment.value()).isPrimaryItem(itemStack) || isBook))
/* 546 */       .forEach(holder -> {
/* 547 */           Enchantment enchantment = (Enchantment)holder.value();
/* 548 */           for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
/* 549 */             if (value >= enchantment.getMinCost(level) && value <= enchantment.getMaxCost(level)) {
/* 550 */               results.add(new EnchantmentInstance(holder, level));
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/* 556 */     return results;
/*     */   }
/*     */   
/*     */   public static void enchantItemFromProvider(ItemStack itemStack, RegistryAccess registryAccess, ResourceKey<EnchantmentProvider> providerKey, DifficultyInstance difficulty, RandomSource random) {
/* 560 */     EnchantmentProvider provider = (EnchantmentProvider)registryAccess.lookupOrThrow(Registries.ENCHANTMENT_PROVIDER).getValue(providerKey);
/* 561 */     if (provider != null)
/* 562 */       updateEnchantments(itemStack, enchantments -> provider.enchant(itemStack, enchantments, random, difficulty)); 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentInSlotVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int, EnchantedItemInUse param1EnchantedItemInUse);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */