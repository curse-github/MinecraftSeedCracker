/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootPoolEntryContainer
/*    */   implements ComposableEntryContainer {
/*    */   protected final List<LootItemCondition> conditions;
/*    */   private final Predicate<LootContext> compositeCondition;
/*    */   
/*    */   protected LootPoolEntryContainer(List<LootItemCondition> conditions) {
/* 21 */     this.conditions = conditions;
/* 22 */     this.compositeCondition = Util.allOf(conditions);
/*    */   }
/*    */   
/*    */   protected static <T extends LootPoolEntryContainer> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 26 */     return i.group(LootItemCondition.DIRECT_CODEC
/* 27 */         .listOf().optionalFieldOf("conditions", List.of()).forGetter(e -> e.conditions));
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 32 */     for (int i = 0; i < this.conditions.size(); i++) {
/* 33 */       ((LootItemCondition)this.conditions.get(i)).validate(output.forChild(new ProblemReporter.IndexedFieldPathElement("conditions", i)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 38 */   protected final boolean canRun(LootContext context) { return this.compositeCondition.test(context); }
/*    */   
/*    */   public abstract LootPoolEntryType getType();
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>>
/*    */     extends Object implements ConditionUserBuilder<T> {
/* 44 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 50 */       this.conditions.add(condition.build());
/* 51 */       return (T)getThis();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 56 */     public final T unwrap() { return (T)getThis(); }
/*    */ 
/*    */ 
/*    */     
/* 60 */     protected List<LootItemCondition> getConditions() { return this.conditions.build(); }
/*    */ 
/*    */ 
/*    */     
/* 64 */     public AlternativesEntry.Builder otherwise(Builder<?> other) { return new AlternativesEntry.Builder(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */     
/* 68 */     public EntryGroup.Builder append(Builder<?> other) { return new EntryGroup.Builder(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */     
/* 72 */     public SequentialEntry.Builder then(Builder<?> other) { return new SequentialEntry.Builder(new Builder[] { this, other }); }
/*    */     
/*    */     protected abstract T getThis();
/*    */     
/*    */     public abstract LootPoolEntryContainer build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntryContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */