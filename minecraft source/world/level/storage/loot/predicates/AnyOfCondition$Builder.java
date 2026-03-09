/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class Builder
/*    */   extends CompositeLootItemCondition.Builder
/*    */ {
/* 22 */   public Builder(Builder... terms) { super(terms); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder or(LootItemCondition.Builder term) {
/* 27 */     addTerm(term);
/* 28 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected LootItemCondition create(List<LootItemCondition> terms) { return new AnyOfCondition(terms); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */