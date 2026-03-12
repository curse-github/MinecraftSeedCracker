/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class DataComponentPredicates {
/*  8 */   public static final DataComponentPredicate.Type<DamagePredicate> DAMAGE = register("damage", DamagePredicate.CODEC);
/*  9 */   public static final DataComponentPredicate.Type<EnchantmentsPredicate.Enchantments> ENCHANTMENTS = register("enchantments", EnchantmentsPredicate.Enchantments.CODEC);
/* 10 */   public static final DataComponentPredicate.Type<EnchantmentsPredicate.StoredEnchantments> STORED_ENCHANTMENTS = register("stored_enchantments", EnchantmentsPredicate.StoredEnchantments.CODEC);
/* 11 */   public static final DataComponentPredicate.Type<PotionsPredicate> POTIONS = register("potion_contents", PotionsPredicate.CODEC);
/* 12 */   public static final DataComponentPredicate.Type<CustomDataPredicate> CUSTOM_DATA = register("custom_data", CustomDataPredicate.CODEC);
/* 13 */   public static final DataComponentPredicate.Type<ContainerPredicate> CONTAINER = register("container", ContainerPredicate.CODEC);
/* 14 */   public static final DataComponentPredicate.Type<BundlePredicate> BUNDLE_CONTENTS = register("bundle_contents", BundlePredicate.CODEC);
/* 15 */   public static final DataComponentPredicate.Type<FireworkExplosionPredicate> FIREWORK_EXPLOSION = register("firework_explosion", FireworkExplosionPredicate.CODEC);
/* 16 */   public static final DataComponentPredicate.Type<FireworksPredicate> FIREWORKS = register("fireworks", FireworksPredicate.CODEC);
/* 17 */   public static final DataComponentPredicate.Type<WritableBookPredicate> WRITABLE_BOOK = register("writable_book_content", WritableBookPredicate.CODEC);
/* 18 */   public static final DataComponentPredicate.Type<WrittenBookPredicate> WRITTEN_BOOK = register("written_book_content", WrittenBookPredicate.CODEC);
/* 19 */   public static final DataComponentPredicate.Type<AttributeModifiersPredicate> ATTRIBUTE_MODIFIERS = register("attribute_modifiers", AttributeModifiersPredicate.CODEC);
/* 20 */   public static final DataComponentPredicate.Type<TrimPredicate> ARMOR_TRIM = register("trim", TrimPredicate.CODEC);
/* 21 */   public static final DataComponentPredicate.Type<JukeboxPlayablePredicate> JUKEBOX_PLAYABLE = register("jukebox_playable", JukeboxPlayablePredicate.CODEC);
/*    */ 
/*    */   
/* 24 */   private static <T extends DataComponentPredicate> DataComponentPredicate.Type<T> register(String id, Codec<T> codec) { return (DataComponentPredicate.Type)Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, id, new DataComponentPredicate.ConcreteType(codec)); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static DataComponentPredicate.Type<?> bootstrap(Registry<DataComponentPredicate.Type<?>> registry) { return DAMAGE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\DataComponentPredicates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */