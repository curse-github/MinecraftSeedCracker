/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
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
/* 41 */   private Optional<Boolean> isRaining = Optional.empty();
/* 42 */   private Optional<Boolean> isThundering = Optional.empty();
/*    */   
/*    */   public Builder setRaining(boolean raining) {
/* 45 */     this.isRaining = Optional.of(Boolean.valueOf(raining));
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setThundering(boolean thundering) {
/* 50 */     this.isThundering = Optional.of(Boolean.valueOf(thundering));
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public WeatherCheck build() { return new WeatherCheck(this.isRaining, this.isThundering); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\WeatherCheck$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */