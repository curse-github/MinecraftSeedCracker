/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface FloatModifier<Argument> extends AttributeModifier<Float, Argument> {
/*  9 */   public static final FloatModifier<FloatWithAlpha> ALPHA_BLEND = new FloatModifier<FloatWithAlpha>()
/*    */     {
/*    */       public Float apply(Float subject, FloatWithAlpha argument) {
/* 12 */         return Float.valueOf(Mth.lerp(argument.alpha(), subject.floatValue(), argument.value()));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 17 */       public Codec<FloatWithAlpha> argumentCodec(EnvironmentAttribute<Float> type) { return FloatWithAlpha.CODEC; }
/*    */ 
/*    */ 
/*    */       
/*    */       public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 22 */         return (alpha, from, to) -> new FloatWithAlpha(
/* 23 */             Mth.lerp(alpha, from.value(), to.value()), 
/* 24 */             Mth.lerp(alpha, from.alpha(), to.alpha()));
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 29 */   public static final FloatModifier<Float> ADD = Float::sum;
/* 30 */   public static final FloatModifier<Float> SUBTRACT = (a, b) -> Float.valueOf(a.floatValue() - b.floatValue());
/* 31 */   public static final FloatModifier<Float> MULTIPLY = (a, b) -> Float.valueOf(a.floatValue() * b.floatValue());
/* 32 */   public static final FloatModifier<Float> MINIMUM = Math::min;
/* 33 */   public static final FloatModifier<Float> MAXIMUM = Math::max;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Simple
/*    */     extends FloatModifier<Float>
/*    */   {
/* 39 */     default Codec<Float> argumentCodec(EnvironmentAttribute<Float> type) { return Codec.FLOAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     default LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> type) { return LerpFunction.ofFloat(); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\FloatModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */