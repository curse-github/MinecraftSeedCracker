/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface FunctionUserBuilder<T extends FunctionUserBuilder<T>> {
/*    */   T apply(LootItemFunction.Builder paramBuilder);
/*    */   
/*    */   default <E> T apply(Iterable<E> collection, Function<E, LootItemFunction.Builder> functionProvider) {
/* 10 */     T result = (T)unwrap();
/* 11 */     for (E value : collection) {
/* 12 */       result = (T)result.apply((LootItemFunction.Builder)functionProvider.apply(value));
/*    */     }
/* 14 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 18 */   default <E> T apply(E[] collection, Function<E, LootItemFunction.Builder> functionProvider) { return (T)apply(Arrays.asList(collection), functionProvider); }
/*    */   
/*    */   T unwrap();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FunctionUserBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */