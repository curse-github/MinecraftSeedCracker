/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
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
/*    */ public class Builder
/*    */   implements LootItemCondition.Builder
/*    */ {
/*    */   private final ImmutableMap.Builder<String, IntRange> scores;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public Builder(LootContext.EntityTarget entityTarget) {
/* 69 */     this.scores = ImmutableMap.builder();
/*    */ 
/*    */ 
/*    */     
/* 73 */     this.entityTarget = entityTarget;
/*    */   }
/*    */   
/*    */   public Builder withScore(String score, IntRange bounds) {
/* 77 */     this.scores.put(score, bounds);
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public LootItemCondition build() { return new EntityHasScoreCondition(this.scores.build(), this.entityTarget); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */