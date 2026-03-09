/*    */ package net.minecraft.world.level.storage.loot.functions;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Builder<T extends LootItemConditionalFunction.Builder<T>>
/*    */   extends Object
/*    */   implements LootItemFunction.Builder, ConditionUserBuilder<T>
/*    */ {
/* 53 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */   
/*    */   public T when(LootItemCondition.Builder condition) {
/* 57 */     this.conditions.add(condition.build());
/* 58 */     return (T)getThis();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public final T unwrap() { return (T)getThis(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected List<LootItemCondition> getConditions() { return this.conditions.build(); }
/*    */   
/*    */   protected abstract T getThis();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */