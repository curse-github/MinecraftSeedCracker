/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
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
/*    */ final class DummyBuilder
/*    */   extends LootItemConditionalFunction.Builder<LootItemConditionalFunction.DummyBuilder>
/*    */ {
/*    */   private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */   
/* 77 */   public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) { this.constructor = constructor; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   protected DummyBuilder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public LootItemFunction build() { return (LootItemFunction)this.constructor.apply(getConditions()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction$DummyBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */