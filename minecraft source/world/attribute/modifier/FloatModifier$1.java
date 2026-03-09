/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements FloatModifier<FloatWithAlpha>
/*    */ {
/* 12 */   public Float apply(Float subject, FloatWithAlpha argument) { return Float.valueOf(Mth.lerp(argument.alpha(), subject.floatValue(), argument.value())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public Codec<FloatWithAlpha> argumentCodec(EnvironmentAttribute<Float> type) { return FloatWithAlpha.CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Float> type) {
/* 22 */     return (alpha, from, to) -> new FloatWithAlpha(
/* 23 */         Mth.lerp(alpha, from.value(), to.value()), 
/* 24 */         Mth.lerp(alpha, from.alpha(), to.alpha()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\FloatModifier$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */