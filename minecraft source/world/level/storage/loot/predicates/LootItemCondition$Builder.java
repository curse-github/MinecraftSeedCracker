/*    */ package net.minecraft.world.level.storage.loot.predicates;
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
/*    */ @FunctionalInterface
/*    */ public interface Builder
/*    */ {
/*    */   LootItemCondition build();
/*    */   
/* 26 */   default Builder invert() { return InvertedLootItemCondition.invert(this); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   default AnyOfCondition.Builder or(Builder other) { return AnyOfCondition.anyOf(new Builder[] { this, other }); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   default AllOfCondition.Builder and(Builder other) { return AllOfCondition.allOf(new Builder[] { this, other }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */