/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface ConditionUserBuilder<T extends ConditionUserBuilder<T>> {
/*    */   T when(LootItemCondition.Builder paramBuilder);
/*    */   
/*    */   default <E> T when(Iterable<E> collection, Function<E, LootItemCondition.Builder> conditionProvider) {
/*  9 */     T result = (T)unwrap();
/* 10 */     for (E value : collection) {
/* 11 */       result = (T)result.when((LootItemCondition.Builder)conditionProvider.apply(value));
/*    */     }
/* 13 */     return result;
/*    */   }
/*    */   
/*    */   T unwrap();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionUserBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */