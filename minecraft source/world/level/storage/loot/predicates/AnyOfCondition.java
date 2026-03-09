/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AnyOfCondition extends CompositeLootItemCondition {
/*  9 */   public static final MapCodec<AnyOfCondition> CODEC = createCodec(AnyOfCondition::new);
/*    */ 
/*    */   
/* 12 */   private AnyOfCondition(List<LootItemCondition> terms) { super(terms, Util.anyOf(terms)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public LootItemConditionType getType() { return LootItemConditions.ANY_OF; }
/*    */   
/*    */   public static class Builder
/*    */     extends CompositeLootItemCondition.Builder
/*    */   {
/* 22 */     public Builder(Builder... terms) { super(terms); }
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder or(LootItemCondition.Builder term) {
/* 27 */       addTerm(term);
/* 28 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 33 */     protected LootItemCondition create(List<LootItemCondition> terms) { return new AnyOfCondition(terms); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static Builder anyOf(Builder... terms) { return new Builder(terms); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */