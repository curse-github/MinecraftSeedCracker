/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
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
/*    */   private Optional<Long> period;
/*    */   private final IntRange value;
/*    */   
/*    */   public Builder(IntRange value) {
/* 47 */     this.period = Optional.empty();
/*    */ 
/*    */ 
/*    */     
/* 51 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Builder setPeriod(long period) {
/* 55 */     this.period = Optional.of(Long.valueOf(period));
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public TimeCheck build() { return new TimeCheck(this.period, this.value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */