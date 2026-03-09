/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AllOfCondition extends CompositeLootItemCondition {
/* 10 */   public static final MapCodec<AllOfCondition> CODEC = createCodec(AllOfCondition::new);
/* 11 */   public static final Codec<AllOfCondition> INLINE_CODEC = createInlineCodec(AllOfCondition::new);
/*    */ 
/*    */   
/* 14 */   private AllOfCondition(List<LootItemCondition> terms) { super(terms, Util.allOf(terms)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static AllOfCondition allOf(List<LootItemCondition> terms) { return new AllOfCondition(List.copyOf(terms)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LootItemConditionType getType() { return LootItemConditions.ALL_OF; }
/*    */   
/*    */   public static class Builder
/*    */     extends CompositeLootItemCondition.Builder
/*    */   {
/* 28 */     public Builder(Builder... terms) { super(terms); }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder and(LootItemCondition.Builder term) {
/* 33 */       addTerm(term);
/* 34 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 39 */     protected LootItemCondition create(List<LootItemCondition> terms) { return new AllOfCondition(terms); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static Builder allOf(Builder... terms) { return new Builder(terms); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\AllOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */