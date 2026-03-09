/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class RangedAttribute extends Attribute {
/*    */   private final double minValue;
/*    */   private final double maxValue;
/*    */   
/*    */   public RangedAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
/* 10 */     super(descriptionId, defaultValue);
/* 11 */     this.minValue = minValue;
/* 12 */     this.maxValue = maxValue;
/*    */     
/* 14 */     if (minValue > maxValue) {
/* 15 */       throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
/*    */     }
/* 17 */     if (defaultValue < minValue) {
/* 18 */       throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
/*    */     }
/* 20 */     if (defaultValue > maxValue) {
/* 21 */       throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 26 */   public double getMinValue() { return this.minValue; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public double getMaxValue() { return this.maxValue; }
/*    */ 
/*    */ 
/*    */   
/*    */   public double sanitizeValue(double value) {
/* 35 */     if (Double.isNaN(value)) {
/* 36 */       return this.minValue;
/*    */     }
/* 38 */     return Mth.clamp(value, this.minValue, this.maxValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\RangedAttribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */