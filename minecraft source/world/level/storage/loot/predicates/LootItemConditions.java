/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LootItemConditions {
/*  9 */   public static final LootItemConditionType INVERTED = register("inverted", InvertedLootItemCondition.CODEC);
/* 10 */   public static final LootItemConditionType ANY_OF = register("any_of", AnyOfCondition.CODEC);
/* 11 */   public static final LootItemConditionType ALL_OF = register("all_of", AllOfCondition.CODEC);
/* 12 */   public static final LootItemConditionType RANDOM_CHANCE = register("random_chance", LootItemRandomChanceCondition.CODEC);
/* 13 */   public static final LootItemConditionType RANDOM_CHANCE_WITH_ENCHANTED_BONUS = register("random_chance_with_enchanted_bonus", LootItemRandomChanceWithEnchantedBonusCondition.CODEC);
/* 14 */   public static final LootItemConditionType ENTITY_PROPERTIES = register("entity_properties", LootItemEntityPropertyCondition.CODEC);
/* 15 */   public static final LootItemConditionType KILLED_BY_PLAYER = register("killed_by_player", LootItemKilledByPlayerCondition.CODEC);
/* 16 */   public static final LootItemConditionType ENTITY_SCORES = register("entity_scores", EntityHasScoreCondition.CODEC);
/* 17 */   public static final LootItemConditionType BLOCK_STATE_PROPERTY = register("block_state_property", LootItemBlockStatePropertyCondition.CODEC);
/* 18 */   public static final LootItemConditionType MATCH_TOOL = register("match_tool", MatchTool.CODEC);
/* 19 */   public static final LootItemConditionType TABLE_BONUS = register("table_bonus", BonusLevelTableCondition.CODEC);
/* 20 */   public static final LootItemConditionType SURVIVES_EXPLOSION = register("survives_explosion", ExplosionCondition.CODEC);
/* 21 */   public static final LootItemConditionType DAMAGE_SOURCE_PROPERTIES = register("damage_source_properties", DamageSourceCondition.CODEC);
/* 22 */   public static final LootItemConditionType LOCATION_CHECK = register("location_check", LocationCheck.CODEC);
/* 23 */   public static final LootItemConditionType WEATHER_CHECK = register("weather_check", WeatherCheck.CODEC);
/* 24 */   public static final LootItemConditionType REFERENCE = register("reference", ConditionReference.CODEC);
/* 25 */   public static final LootItemConditionType TIME_CHECK = register("time_check", TimeCheck.CODEC);
/* 26 */   public static final LootItemConditionType VALUE_CHECK = register("value_check", ValueCheckCondition.CODEC);
/* 27 */   public static final LootItemConditionType ENCHANTMENT_ACTIVE_CHECK = register("enchantment_active_check", EnchantmentActiveCheck.CODEC);
/*    */ 
/*    */   
/* 30 */   private static LootItemConditionType register(String name, MapCodec<? extends LootItemCondition> codec) { return (LootItemConditionType)Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Identifier.withDefaultNamespace(name), new LootItemConditionType(codec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */