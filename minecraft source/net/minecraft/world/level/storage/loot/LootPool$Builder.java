/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */   extends Object
/*     */   implements FunctionUserBuilder<LootPool.Builder>, ConditionUserBuilder<LootPool.Builder>
/*     */ {
/* 122 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/* 123 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/* 124 */   private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/* 125 */   private NumberProvider rolls = ConstantValue.exactly(1.0F);
/* 126 */   private NumberProvider bonusRolls = ConstantValue.exactly(0.0F);
/*     */   
/*     */   public Builder setRolls(NumberProvider rolls) {
/* 129 */     this.rolls = rolls;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public Builder unwrap() { return this; }
/*     */ 
/*     */   
/*     */   public Builder setBonusRolls(NumberProvider bonusRolls) {
/* 139 */     this.bonusRolls = bonusRolls;
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(LootPoolEntryContainer.Builder<?> entry) {
/* 144 */     this.entries.add(entry.build());
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder when(LootItemCondition.Builder condition) {
/* 150 */     this.conditions.add(condition.build());
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder apply(LootItemFunction.Builder function) {
/* 156 */     this.functions.add(function.build());
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 161 */   public LootPool build() { return new LootPool(this.entries.build(), this.conditions.build(), this.functions.build(), this.rolls, this.bonusRolls); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootPool$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */