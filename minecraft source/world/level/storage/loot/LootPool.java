/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class LootPool {
/*  31 */   public static final Codec<LootPool> CODEC = RecordCodecBuilder.create(i -> i.group(LootPoolEntries.CODEC
/*  32 */         .listOf().fieldOf("entries").forGetter(()), LootItemCondition.DIRECT_CODEC
/*  33 */         .listOf().optionalFieldOf("conditions", List.of()).forGetter(()), LootItemFunctions.ROOT_CODEC
/*  34 */         .listOf().optionalFieldOf("functions", List.of()).forGetter(()), NumberProviders.CODEC
/*  35 */         .fieldOf("rolls").forGetter(()), NumberProviders.CODEC
/*  36 */         .fieldOf("bonus_rolls").orElse(ConstantValue.exactly(0.0F)).forGetter(()))
/*  37 */       .apply(i, LootPool::new));
/*     */   
/*     */   private final List<LootPoolEntryContainer> entries;
/*     */   private final List<LootItemCondition> conditions;
/*     */   private final Predicate<LootContext> compositeCondition;
/*     */   private final List<LootItemFunction> functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   private final NumberProvider rolls;
/*     */   private final NumberProvider bonusRolls;
/*     */   
/*     */   private LootPool(List<LootPoolEntryContainer> entries, List<LootItemCondition> conditions, List<LootItemFunction> functions, NumberProvider rolls, NumberProvider bonusRolls) {
/*  48 */     this.entries = entries;
/*  49 */     this.conditions = conditions;
/*  50 */     this.compositeCondition = Util.allOf(conditions);
/*  51 */     this.functions = functions;
/*  52 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*  53 */     this.rolls = rolls;
/*  54 */     this.bonusRolls = bonusRolls;
/*     */   }
/*     */   
/*     */   private void addRandomItem(Consumer<ItemStack> result, LootContext context) {
/*  58 */     RandomSource random = context.getRandom();
/*  59 */     List<LootPoolEntry> validEntries = Lists.newArrayList();
/*  60 */     MutableInt totalWeight = new MutableInt();
/*  61 */     for (LootPoolEntryContainer entry : this.entries) {
/*  62 */       entry.expand(context, e -> {
/*  63 */             int weight = e.getWeight(context.getLuck());
/*  64 */             if (weight > 0) {
/*  65 */               validEntries.add(e);
/*  66 */               totalWeight.add(weight);
/*     */             } 
/*     */           });
/*     */     } 
/*     */     
/*  71 */     int entryCount = validEntries.size();
/*  72 */     if (totalWeight.intValue() == 0 || entryCount == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     if (entryCount == 1) {
/*  77 */       ((LootPoolEntry)validEntries.get(0)).createItemStack(result, context);
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     int index = random.nextInt(totalWeight.intValue());
/*  82 */     for (LootPoolEntry entry : validEntries) {
/*  83 */       index -= entry.getWeight(context.getLuck());
/*  84 */       if (index < 0) {
/*  85 */         entry.createItemStack(result, context);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addRandomItems(Consumer<ItemStack> result, LootContext context) {
/*  92 */     if (!this.compositeCondition.test(context)) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     Consumer<ItemStack> decoratedConsumer = LootItemFunction.decorate(this.compositeFunction, result, context);
/*     */     
/*  98 */     int count = this.rolls.getInt(context) + Mth.floor(this.bonusRolls.getFloat(context) * context.getLuck());
/*  99 */     for (int i = 0; i < count; i++) {
/* 100 */       addRandomItem(decoratedConsumer, context);
/*     */     }
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext output) {
/* 105 */     for (int i = 0; i < this.conditions.size(); i++) {
/* 106 */       ((LootItemCondition)this.conditions.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("conditions", i)));
/*     */     }
/*     */     
/* 109 */     for (int i = 0; i < this.functions.size(); i++) {
/* 110 */       ((LootItemFunction)this.functions.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("functions", i)));
/*     */     }
/*     */     
/* 113 */     for (int i = 0; i < this.entries.size(); i++) {
/* 114 */       ((LootPoolEntryContainer)this.entries.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("entries", i)));
/*     */     }
/*     */     
/* 117 */     this.rolls.validate(output.forChild(new ProblemReporter.FieldPathElement("rolls")));
/* 118 */     this.bonusRolls.validate(output.forChild(new ProblemReporter.FieldPathElement("bonus_rolls")));
/*     */   }
/*     */   
/*     */   public static class Builder extends Object implements FunctionUserBuilder<Builder>, ConditionUserBuilder<Builder> {
/* 122 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/* 123 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/* 124 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/* 125 */     private NumberProvider rolls = ConstantValue.exactly(1.0F);
/* 126 */     private NumberProvider bonusRolls = ConstantValue.exactly(0.0F);
/*     */     
/*     */     public Builder setRolls(NumberProvider rolls) {
/* 129 */       this.rolls = rolls;
/* 130 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 135 */     public Builder unwrap() { return this; }
/*     */ 
/*     */     
/*     */     public Builder setBonusRolls(NumberProvider bonusRolls) {
/* 139 */       this.bonusRolls = bonusRolls;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(LootPoolEntryContainer.Builder<?> entry) {
/* 144 */       this.entries.add(entry.build());
/* 145 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder when(LootItemCondition.Builder condition) {
/* 150 */       this.conditions.add(condition.build());
/* 151 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder function) {
/* 156 */       this.functions.add(function.build());
/* 157 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 161 */     public LootPool build() { return new LootPool(this.entries.build(), this.conditions.build(), this.functions.build(), this.rolls, this.bonusRolls); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public static Builder lootPool() { return new Builder(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootPool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */