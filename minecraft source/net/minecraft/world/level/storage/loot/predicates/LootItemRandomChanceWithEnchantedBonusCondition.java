/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class LootItemRandomChanceWithEnchantedBonusCondition extends Record implements LootItemCondition {
/*    */   private final float unenchantedChance;
/*    */   private final LevelBasedValue enchantedChance;
/*    */   private final Holder<Enchantment> enchantment;
/*    */   
/* 21 */   public LootItemRandomChanceWithEnchantedBonusCondition(float unenchantedChance, LevelBasedValue enchantedChance, Holder<Enchantment> enchantment) { this.unenchantedChance = unenchantedChance; this.enchantedChance = enchantedChance; this.enchantment = enchantment; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 21 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition; } public float unenchantedChance() { return this.unenchantedChance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue enchantedChance() { return this.enchantedChance; } public Holder<Enchantment> enchantment() { return this.enchantment; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final MapCodec<LootItemRandomChanceWithEnchantedBonusCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 27 */         Codec.floatRange(0.0F, 1.0F).fieldOf("unenchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::unenchantedChance), LevelBasedValue.CODEC
/* 28 */         .fieldOf("enchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantedChance), Enchantment.CODEC
/* 29 */         .fieldOf("enchantment").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantment))
/* 30 */       .apply(i, LootItemRandomChanceWithEnchantedBonusCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 34 */   public LootItemConditionType getType() { return LootItemConditions.RANDOM_CHANCE_WITH_ENCHANTED_BONUS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.ATTACKING_ENTITY); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 44 */     Entity killerEntity = (Entity)context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
/*    */     
/* 46 */     LivingEntity livingKiller = (LivingEntity)killerEntity; int enchantmentLevel = (killerEntity instanceof LivingEntity) ? EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingKiller) : 0;
/* 47 */     float chance = (enchantmentLevel > 0) ? this.enchantedChance.calculate(enchantmentLevel) : this.unenchantedChance;
/* 48 */     return (context.getRandom().nextFloat() < chance);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChanceAndLootingBoost(HolderLookup.Provider registries, float chance, float perEnchantmentLevel) {
/* 52 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 53 */     return () -> new LootItemRandomChanceWithEnchantedBonusCondition(chance, new LevelBasedValue.Linear(chance + perEnchantmentLevel, perEnchantmentLevel), enchantments.getOrThrow(Enchantments.LOOTING));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceWithEnchantedBonusCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */