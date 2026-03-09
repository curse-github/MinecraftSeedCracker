/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
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
/*    */ class null
/*    */   extends Object
/*    */   implements AttributeRange<Float>
/*    */ {
/*    */   public DataResult<Float> validate(Float value) {
/* 28 */     if (value.floatValue() >= minValue && value.floatValue() <= maxValue) {
/* 29 */       return DataResult.success(value);
/*    */     }
/* 31 */     return DataResult.error(() -> "" + value + " is not in range [" + value + "; " + minValue + "]");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Float sanitize(Float value) {
/* 37 */     if (value.floatValue() >= minValue && value.floatValue() <= maxValue)
/*    */     {
/* 39 */       return value;
/*    */     }
/* 41 */     return Float.valueOf(Mth.clamp(value.floatValue(), minValue, maxValue));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AttributeRange$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */