/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface AttributeRange<Value> {
/*  7 */   public static final AttributeRange<Float> UNIT_FLOAT = ofFloat(0.0F, 1.0F);
/*  8 */   public static final AttributeRange<Float> NON_NEGATIVE_FLOAT = ofFloat(0.0F, Float.POSITIVE_INFINITY);
/*    */   
/*    */   static <Value> AttributeRange<Value> any() {
/* 11 */     return new AttributeRange<Value>()
/*    */       {
/*    */         public DataResult<Value> validate(Value value) {
/* 14 */           return DataResult.success(value);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 19 */         public Value sanitize(Value value) { return value; }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   static AttributeRange<Float> ofFloat(final float minValue, final float maxValue) {
/* 25 */     return new AttributeRange<Float>()
/*    */       {
/*    */         public DataResult<Float> validate(Float value) {
/* 28 */           if (value.floatValue() >= minValue && value.floatValue() <= maxValue) {
/* 29 */             return DataResult.success(value);
/*    */           }
/* 31 */           return DataResult.error(() -> "" + value + " is not in range [" + value + "; " + minValue + "]");
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public Float sanitize(Float value) {
/* 37 */           if (value.floatValue() >= minValue && value.floatValue() <= maxValue)
/*    */           {
/* 39 */             return value;
/*    */           }
/* 41 */           return Float.valueOf(Mth.clamp(value.floatValue(), minValue, maxValue));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   DataResult<Value> validate(Value paramValue);
/*    */   
/*    */   Value sanitize(Value paramValue);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AttributeRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */