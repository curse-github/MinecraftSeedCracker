/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Builder<T extends LootPoolEntryContainer.Builder<T>>
/*    */   extends Object
/*    */   implements ConditionUserBuilder<T>
/*    */ {
/* 44 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T when(LootItemCondition.Builder condition) {
/* 50 */     this.conditions.add(condition.build());
/* 51 */     return (T)getThis();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public final T unwrap() { return (T)getThis(); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected List<LootItemCondition> getConditions() { return this.conditions.build(); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public AlternativesEntry.Builder otherwise(Builder<?> other) { return new AlternativesEntry.Builder(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public EntryGroup.Builder append(Builder<?> other) { return new EntryGroup.Builder(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public SequentialEntry.Builder then(Builder<?> other) { return new SequentialEntry.Builder(new Builder[] { this, other }); }
/*    */   
/*    */   protected abstract T getThis();
/*    */   
/*    */   public abstract LootPoolEntryContainer build();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntryContainer$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */