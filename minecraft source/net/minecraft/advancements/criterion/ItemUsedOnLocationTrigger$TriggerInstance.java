/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
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
/*     */ public final class TriggerInstance
/*     */   extends Record
/*     */   implements SimpleCriterionTrigger.SimpleInstance
/*     */ {
/*     */   private final Optional<ContextAwarePredicate> player;
/*     */   private final Optional<ContextAwarePredicate> location;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #47	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #47	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #47	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger$TriggerInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  47 */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) { this.player = player; this.location = location; } public Optional<ContextAwarePredicate> player() { return this.player; } public Optional<ContextAwarePredicate> location() { return this.location; }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(EntityPredicate.ADVANCEMENT_CODEC
/*  52 */         .optionalFieldOf("player").forGetter(TriggerInstance::player), ContextAwarePredicate.CODEC
/*  53 */         .optionalFieldOf("location").forGetter(TriggerInstance::location))
/*  54 */       .apply(i, TriggerInstance::new));
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlock(Block block) {
/*  57 */     ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemCondition[] { LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build() });
/*  58 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlock(Builder... conditions) {
/*  62 */     ContextAwarePredicate location = ContextAwarePredicate.create((LootItemCondition[])Arrays.stream(conditions).map(LootItemCondition.Builder::build).toArray(x$0 -> new LootItemCondition[x$0]));
/*  63 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> property, String propertyValue) {
/*  67 */     StatePropertiesPredicate.Builder predicateBuilder = StatePropertiesPredicate.Builder.properties().hasProperty(property, propertyValue);
/*  68 */     ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemCondition[] { LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(predicateBuilder).build() });
/*  69 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */ 
/*     */   
/*  73 */   public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Boolean> property, boolean propertyValue) { return placedBlockWithProperties(block, property, String.valueOf(propertyValue)); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Integer> property, int propertyValue) { return placedBlockWithProperties(block, property, String.valueOf(propertyValue)); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static <T extends Comparable<T> & StringRepresentable> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> properties, T propertyValue) { return placedBlockWithProperties(block, properties, ((StringRepresentable)propertyValue).getSerializedName()); }
/*     */ 
/*     */   
/*     */   private static TriggerInstance itemUsedOnLocation(LocationPredicate.Builder location, ItemPredicate.Builder item) {
/*  85 */     ContextAwarePredicate predicate = ContextAwarePredicate.create(new LootItemCondition[] {
/*  86 */           LocationCheck.checkLocation(location).build(), 
/*  87 */           MatchTool.toolMatches(item).build()
/*     */         });
/*     */     
/*  90 */     return new TriggerInstance(Optional.empty(), Optional.of(predicate));
/*     */   }
/*     */ 
/*     */   
/*  94 */   public static Criterion<TriggerInstance> itemUsedOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) { return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item)); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static Criterion<TriggerInstance> allayDropItemOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) { return CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item)); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public boolean matches(LootContext locationContext) { return (this.location.isEmpty() || ((ContextAwarePredicate)this.location.get()).matches(locationContext)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(CriterionValidator validator) {
/* 107 */     super.validate(validator);
/* 108 */     this.location.ifPresent(predicate -> validator.validate(predicate, LootContextParamSets.ADVANCEMENT_LOCATION, "location"));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\ItemUsedOnLocationTrigger$TriggerInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */