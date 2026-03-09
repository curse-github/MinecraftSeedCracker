/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class BonusLevelTableCondition extends Record implements LootItemCondition {
/*    */   private final Holder<Enchantment> enchantment;
/*    */   private final List<Float> values;
/*    */   
/* 19 */   public BonusLevelTableCondition(Holder<Enchantment> enchantment, List<Float> values) { this.enchantment = enchantment; this.values = values; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition; } public Holder<Enchantment> enchantment() { return this.enchantment; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public List<Float> values() { return this.values; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<BonusLevelTableCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Enchantment.CODEC
/* 24 */         .fieldOf("enchantment").forGetter(BonusLevelTableCondition::enchantment), 
/* 25 */         ExtraCodecs.nonEmptyList(Codec.FLOAT.listOf()).fieldOf("chances").forGetter(BonusLevelTableCondition::values))
/* 26 */       .apply(i, BonusLevelTableCondition::new));
/*    */ 
/*    */ 
/*    */   
/* 30 */   public LootItemConditionType getType() { return LootItemConditions.TABLE_BONUS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.TOOL); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 40 */     ItemStack tool = (ItemStack)context.getOptionalParameter(LootContextParams.TOOL);
/*    */     
/* 42 */     int level = (tool != null) ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool) : 0;
/* 43 */     float chance = ((Float)this.values.get(Math.min(level, this.values.size() - 1))).floatValue();
/* 44 */     return (context.getRandom().nextFloat() < chance);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder bonusLevelFlatChance(Holder<Enchantment> enchantment, float... chances) {
/* 48 */     List<Float> chancesList = new ArrayList<Float>(chances.length);
/* 49 */     for (float chance : chances) {
/* 50 */       chancesList.add(Float.valueOf(chance));
/*    */     }
/* 52 */     return () -> new BonusLevelTableCondition(enchantment, chancesList);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\BonusLevelTableCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */