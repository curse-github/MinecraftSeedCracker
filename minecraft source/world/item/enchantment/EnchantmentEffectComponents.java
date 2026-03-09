/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*    */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public interface EnchantmentEffectComponents
/*    */ {
/* 23 */   public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.byNameCodec());
/* 24 */   public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);
/*    */   
/* 26 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DAMAGE_PROTECTION = register("damage_protection", b -> b
/* 27 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 28 */   public static final DataComponentType<List<ConditionalEffect<DamageImmunity>>> DAMAGE_IMMUNITY = register("damage_immunity", b -> b
/* 29 */       .persistent(ConditionalEffect.codec(DamageImmunity.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 30 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DAMAGE = register("damage", b -> b
/* 31 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 32 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> SMASH_DAMAGE_PER_FALLEN_BLOCK = register("smash_damage_per_fallen_block", b -> b
/* 33 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 34 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> KNOCKBACK = register("knockback", b -> b
/* 35 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 36 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> ARMOR_EFFECTIVENESS = register("armor_effectiveness", b -> b
/* 37 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 38 */   public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentEntityEffect>>> POST_ATTACK = register("post_attack", b -> b
/* 39 */       .persistent(TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 40 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> POST_PIERCING_ATTACK = register("post_piercing_attack", b -> b
/* 41 */       .persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 42 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> HIT_BLOCK = register("hit_block", b -> b
/* 43 */       .persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.HIT_BLOCK).listOf()));
/* 44 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> ITEM_DAMAGE = register("item_damage", b -> b
/* 45 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/* 46 */   public static final DataComponentType<List<EnchantmentAttributeEffect>> ATTRIBUTES = register("attributes", b -> b
/* 47 */       .persistent(EnchantmentAttributeEffect.CODEC.codec().listOf()));
/* 48 */   public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentValueEffect>>> EQUIPMENT_DROPS = register("equipment_drops", b -> b
/* 49 */       .persistent(TargetedConditionalEffect.equipmentDropsCodec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
/* 50 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentLocationBasedEffect>>> LOCATION_CHANGED = register("location_changed", b -> b
/* 51 */       .persistent(ConditionalEffect.codec(EnchantmentLocationBasedEffect.CODEC, LootContextParamSets.ENCHANTED_LOCATION).listOf()));
/* 52 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> TICK = register("tick", b -> b
/* 53 */       .persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 54 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> AMMO_USE = register("ammo_use", b -> b
/* 55 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/* 56 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_PIERCING = register("projectile_piercing", b -> b
/* 57 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/* 58 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> PROJECTILE_SPAWNED = register("projectile_spawned", b -> b
/* 59 */       .persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 60 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_SPREAD = register("projectile_spread", b -> b
/* 61 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 62 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> PROJECTILE_COUNT = register("projectile_count", b -> b
/* 63 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 64 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> TRIDENT_RETURN_ACCELERATION = register("trident_return_acceleration", b -> b
/* 65 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 66 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> FISHING_TIME_REDUCTION = register("fishing_time_reduction", b -> b
/* 67 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 68 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> FISHING_LUCK_BONUS = register("fishing_luck_bonus", b -> b
/* 69 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 70 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> BLOCK_EXPERIENCE = register("block_experience", b -> b
/* 71 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/* 72 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> MOB_EXPERIENCE = register("mob_experience", b -> b
/* 73 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
/* 74 */   public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> REPAIR_WITH_XP = register("repair_with_xp", b -> b
/* 75 */       .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf()));
/*    */ 
/*    */   
/* 78 */   public static final DataComponentType<EnchantmentValueEffect> CROSSBOW_CHARGE_TIME = register("crossbow_charge_time", b -> b
/* 79 */       .persistent(EnchantmentValueEffect.CODEC));
/* 80 */   public static final DataComponentType<List<CrossbowItem.ChargingSounds>> CROSSBOW_CHARGING_SOUNDS = register("crossbow_charging_sounds", b -> b
/* 81 */       .persistent(CrossbowItem.ChargingSounds.CODEC.listOf()));
/* 82 */   public static final DataComponentType<List<Holder<SoundEvent>>> TRIDENT_SOUND = register("trident_sound", b -> b
/* 83 */       .persistent(SoundEvent.CODEC.listOf()));
/* 84 */   public static final DataComponentType<Unit> PREVENT_EQUIPMENT_DROP = register("prevent_equipment_drop", b -> b
/* 85 */       .persistent(Unit.CODEC));
/* 86 */   public static final DataComponentType<Unit> PREVENT_ARMOR_CHANGE = register("prevent_armor_change", b -> b
/* 87 */       .persistent(Unit.CODEC));
/* 88 */   public static final DataComponentType<EnchantmentValueEffect> TRIDENT_SPIN_ATTACK_STRENGTH = register("trident_spin_attack_strength", b -> b
/* 89 */       .persistent(EnchantmentValueEffect.CODEC));
/*    */ 
/*    */   
/* 92 */   static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) { return DAMAGE_PROTECTION; }
/*    */ 
/*    */ 
/*    */   
/* 96 */   private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) { return (DataComponentType)Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, id, ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentEffectComponents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */