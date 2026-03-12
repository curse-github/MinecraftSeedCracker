/*    */ package net.minecraft.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
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
/*    */   implements BoundedFloatFunction<Float>
/*    */ {
/* 18 */   public float apply(Float aFloat) { return ((Float)function.apply(aFloat)).floatValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public float minValue() { return Float.NEGATIVE_INFINITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public float maxValue() { return Float.POSITIVE_INFINITY; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BoundedFloatFunction$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */