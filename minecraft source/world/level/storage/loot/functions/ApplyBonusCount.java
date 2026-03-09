/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ApplyBonusCount extends LootItemConditionalFunction {
/*     */   private static final class FormulaType extends Record {
/*     */     private final Identifier id;
/*     */     private final Codec<? extends ApplyBonusCount.Formula> codec;
/*     */     
/*  27 */     private FormulaType(Identifier id, Codec<? extends ApplyBonusCount.Formula> codec) { this.id = id; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  27 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;
/*  27 */       //   0	8	1	o	Ljava/lang/Object; } public Codec<? extends ApplyBonusCount.Formula> codec() { return this.codec; }
/*     */   }
/*     */   
/*     */   private static final class BinomialWithBonusCount
/*     */     extends Record
/*     */     implements Formula {
/*     */     private final int extraRounds;
/*     */     private final float probability;
/*     */     
/*  36 */     private BinomialWithBonusCount(int extraRounds, float probability) { this.extraRounds = extraRounds; this.probability = probability; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*  36 */       //   0	8	1	o	Ljava/lang/Object; } public int extraRounds() { return this.extraRounds; } public float probability() { return this.probability; }
/*  37 */     private static final Codec<BinomialWithBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/*  38 */           .fieldOf("extra").forGetter(BinomialWithBonusCount::extraRounds), Codec.FLOAT
/*  39 */           .fieldOf("probability").forGetter(BinomialWithBonusCount::probability))
/*  40 */         .apply(i, BinomialWithBonusCount::new));
/*     */     
/*  42 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), CODEC);
/*     */ 
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  46 */       for (int i = 0; i < level + this.extraRounds; i++) {
/*  47 */         if (random.nextFloat() < this.probability) {
/*  48 */           count++;
/*     */         }
/*     */       } 
/*  51 */       return count;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  56 */     public ApplyBonusCount.FormulaType getType() { return TYPE; } }
/*     */   
/*     */   private static final class UniformBonusCount extends Record implements Formula { private final int bonusMultiplier;
/*     */     
/*  60 */     private UniformBonusCount(int bonusMultiplier) { this.bonusMultiplier = bonusMultiplier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*  60 */       //   0	8	1	o	Ljava/lang/Object; } public int bonusMultiplier() { return this.bonusMultiplier; }
/*  61 */     public static final Codec<UniformBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/*  62 */           .fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier))
/*  63 */         .apply(i, UniformBonusCount::new));
/*     */     
/*  65 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), CODEC);
/*     */ 
/*     */ 
/*     */     
/*  69 */     public int calculateNewCount(RandomSource random, int count, int level) { return count + random.nextInt(this.bonusMultiplier * level + 1); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     public ApplyBonusCount.FormulaType getType() { return TYPE; } }
/*     */   private static final class OreDrops extends Record implements Formula { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/*  79 */     public static final OreDrops INSTANCE = new OreDrops();
/*     */     
/*  81 */     public static final Codec<OreDrops> CODEC = MapCodec.unitCodec(INSTANCE);
/*  82 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("ore_drops"), CODEC);
/*     */ 
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  86 */       if (level > 0) {
/*  87 */         int bonus = random.nextInt(level + 2) - 1;
/*  88 */         if (bonus < 0) {
/*  89 */           bonus = 0;
/*     */         }
/*  91 */         return count * (bonus + 1);
/*     */       } 
/*     */       
/*  94 */       return count;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  99 */     public ApplyBonusCount.FormulaType getType() { return TYPE; } }
/*     */ 
/*     */ 
/*     */   
/* 103 */   private static final Map<Identifier, FormulaType> FORMULAS = (Map)Stream.of(new FormulaType[] { BinomialWithBonusCount.TYPE, OreDrops.TYPE, UniformBonusCount.TYPE
/*     */ 
/*     */ 
/*     */       
/* 107 */       }).collect(Collectors.toMap(FormulaType::id, Function.identity()));
/*     */   
/* 109 */   private static final Codec<FormulaType> FORMULA_TYPE_CODEC = Identifier.CODEC.comapFlatMap(location -> {
/* 110 */         FormulaType type = (FormulaType)FORMULAS.get(location);
/* 111 */         if (type != null) {
/* 112 */           return DataResult.success(type);
/*     */         }
/* 114 */         return DataResult.error(());
/*     */       }FormulaType::id);
/*     */   
/* 117 */   private static final MapCodec<Formula> FORMULA_CODEC = ExtraCodecs.dispatchOptionalValue("formula", "parameters", FORMULA_TYPE_CODEC, Formula::getType, FormulaType::codec);
/*     */   
/* 119 */   public static final MapCodec<ApplyBonusCount> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(Enchantment.CODEC
/* 120 */           .fieldOf("enchantment").forGetter(()), FORMULA_CODEC
/* 121 */           .forGetter(())))
/* 122 */       .apply(i, ApplyBonusCount::new));
/*     */   
/*     */   private final Holder<Enchantment> enchantment;
/*     */   private final Formula formula;
/*     */   
/*     */   private ApplyBonusCount(List<LootItemCondition> predicates, Holder<Enchantment> enchantment, Formula formula) {
/* 128 */     super(predicates);
/* 129 */     this.enchantment = enchantment;
/* 130 */     this.formula = formula;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public LootItemFunctionType<ApplyBonusCount> getType() { return LootItemFunctions.APPLY_BONUS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.TOOL); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 145 */     ItemStack tool = (ItemStack)context.getOptionalParameter(LootContextParams.TOOL);
/*     */     
/* 147 */     if (tool != null) {
/* 148 */       int level = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool);
/* 149 */       int newCount = this.formula.calculateNewCount(context.getRandom(), itemStack.getCount(), level);
/* 150 */       itemStack.setCount(newCount);
/*     */     } 
/* 152 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/* 156 */   public static LootItemConditionalFunction.Builder<?> addBonusBinomialDistributionCount(Holder<Enchantment> enchantment, float probability, int extraRounds) { return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new BinomialWithBonusCount(extraRounds, probability))); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public static LootItemConditionalFunction.Builder<?> addOreBonusCount(Holder<Enchantment> enchantment) { return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, OreDrops.INSTANCE)); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment) { return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(1))); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment, int bonusMultiplier) { return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(bonusMultiplier))); }
/*     */   
/*     */   private static interface Formula {
/*     */     int calculateNewCount(RandomSource param1RandomSource, int param1Int1, int param1Int2);
/*     */     
/*     */     ApplyBonusCount.FormulaType getType();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */