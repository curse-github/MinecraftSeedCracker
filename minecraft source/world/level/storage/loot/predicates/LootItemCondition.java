/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface LootItemCondition
/*    */   extends LootContextUser, Predicate<LootContext> {
/* 14 */   public static final Codec<LootItemCondition> TYPED_CODEC = BuiltInRegistries.LOOT_CONDITION_TYPE.byNameCodec()
/* 15 */     .dispatch("condition", LootItemCondition::getType, LootItemConditionType::codec);
/* 16 */   public static final Codec<LootItemCondition> DIRECT_CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, AllOfCondition.INLINE_CODEC));
/* 17 */   public static final Codec<Holder<LootItemCondition>> CODEC = RegistryFileCodec.create(Registries.PREDICATE, DIRECT_CODEC);
/*    */   
/*    */   LootItemConditionType getType();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Builder
/*    */   {
/*    */     LootItemCondition build();
/*    */     
/* 26 */     default Builder invert() { return InvertedLootItemCondition.invert(this); }
/*    */ 
/*    */ 
/*    */     
/* 30 */     default AnyOfCondition.Builder or(Builder other) { return AnyOfCondition.anyOf(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */     
/* 34 */     default AllOfCondition.Builder and(Builder other) { return AllOfCondition.allOf(new Builder[] { this, other }); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */