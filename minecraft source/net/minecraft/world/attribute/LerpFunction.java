/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface LerpFunction<T>
/*    */ {
/*  8 */   static LerpFunction<Float> ofFloat() { return Mth::lerp; }
/*    */ 
/*    */   
/*    */   static LerpFunction<Float> ofDegrees(float maxDelta) {
/* 12 */     return (alpha, from, to) -> {
/* 13 */         float delta = Mth.wrapDegrees(to.floatValue() - from.floatValue());
/* 14 */         if (Math.abs(delta) >= maxDelta) {
/* 15 */           return to;
/*    */         }
/* 17 */         return Float.valueOf(from.floatValue() + alpha * delta);
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 22 */   static <T> LerpFunction<T> ofConstant() { return (alpha, from, to) -> from; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   static <T> LerpFunction<T> ofStep(float threshold) { return (alpha, from, to) -> (alpha >= threshold) ? to : from; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   static LerpFunction<Integer> ofColor() { return ARGB::srgbLerp; }
/*    */   
/*    */   T apply(float paramFloat, T paramT1, T paramT2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\LerpFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */