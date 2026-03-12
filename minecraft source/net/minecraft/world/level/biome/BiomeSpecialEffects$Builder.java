/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
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
/*    */ {
/* 27 */   private OptionalInt waterColor = OptionalInt.empty();
/* 28 */   private Optional<Integer> foliageColorOverride = Optional.empty();
/* 29 */   private Optional<Integer> dryFoliageColorOverride = Optional.empty();
/* 30 */   private Optional<Integer> grassColorOverride = Optional.empty();
/* 31 */   private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*    */   
/*    */   public Builder waterColor(int waterColor) {
/* 34 */     this.waterColor = OptionalInt.of(waterColor);
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public Builder foliageColorOverride(int foliageColor) {
/* 39 */     this.foliageColorOverride = Optional.of(Integer.valueOf(foliageColor));
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public Builder dryFoliageColorOverride(int dryFoliageColor) {
/* 44 */     this.dryFoliageColorOverride = Optional.of(Integer.valueOf(dryFoliageColor));
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorOverride(int grassColor) {
/* 49 */     this.grassColorOverride = Optional.of(Integer.valueOf(grassColor));
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassModifier) {
/* 54 */     this.grassColorModifier = grassModifier;
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public BiomeSpecialEffects build() {
/* 59 */     return new BiomeSpecialEffects(this.waterColor
/* 60 */         .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */