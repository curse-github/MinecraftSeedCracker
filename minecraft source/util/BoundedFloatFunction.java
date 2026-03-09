/*    */ package net.minecraft.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BoundedFloatFunction<C>
/*    */ {
/*    */   static BoundedFloatFunction<Float> createUnlimited(final Float2FloatFunction function) {
/* 15 */     return new BoundedFloatFunction<Float>()
/*    */       {
/*    */         public float apply(Float aFloat) {
/* 18 */           return ((Float)function.apply(aFloat)).floatValue();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 23 */         public float minValue() { return Float.NEGATIVE_INFINITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 28 */         public float maxValue() { return Float.POSITIVE_INFINITY; }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static final BoundedFloatFunction<Float> IDENTITY = createUnlimited(input -> input);
/*    */   
/*    */   default <C2> BoundedFloatFunction<C2> comap(final Function<C2, C> function) {
/* 36 */     final BoundedFloatFunction<C> outer = this;
/* 37 */     return new BoundedFloatFunction<C2>(this)
/*    */       {
/*    */         public float apply(C2 c2) {
/* 40 */           return outer.apply(function.apply(c2));
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 45 */         public float minValue() { return outer.minValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 50 */         public float maxValue() { return outer.maxValue(); }
/*    */       };
/*    */   }
/*    */   
/*    */   float apply(C paramC);
/*    */   
/*    */   float minValue();
/*    */   
/*    */   float maxValue();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BoundedFloatFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */