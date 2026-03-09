/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
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
/*    */ public abstract class Builder
/*    */   implements LootItemCondition.Builder
/*    */ {
/* 50 */   private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */   
/*    */   protected Builder(Builder... terms) {
/* 53 */     for (LootItemCondition.Builder term : terms) {
/* 54 */       this.terms.add(term.build());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 59 */   public void addTerm(LootItemCondition.Builder term) { this.terms.add(term.build()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public LootItemCondition build() { return create(this.terms.build()); }
/*    */   
/*    */   protected abstract LootItemCondition create(List<LootItemCondition> paramList);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\CompositeLootItemCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */