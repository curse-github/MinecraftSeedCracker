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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends CompositeLootItemCondition.Builder
/*    */ {
/* 28 */   public Builder(Builder... terms) { super(terms); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Builder and(LootItemCondition.Builder term) {
/* 33 */     addTerm(term);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected LootItemCondition create(List<LootItemCondition> terms) { return new AllOfCondition(terms); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\AllOfCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */