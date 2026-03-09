/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
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
/*    */ class null
/*    */   extends Object
/*    */   implements ColorModifier<ColorModifier.BlendToGray>
/*    */ {
/*    */   public Integer apply(Integer subject, ColorModifier.BlendToGray argument) {
/* 67 */     int multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject.intValue()), argument.brightness);
/* 68 */     return Integer.valueOf(ARGB.srgbLerp(argument.factor, subject.intValue(), multipliedGreyscale));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public Codec<ColorModifier.BlendToGray> argumentCodec(EnvironmentAttribute<Integer> type) { return ColorModifier.BlendToGray.CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public LerpFunction<ColorModifier.BlendToGray> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 78 */     return (alpha, from, to) -> new ColorModifier.BlendToGray(
/* 79 */         Mth.lerp(alpha, from.brightness, to.brightness), 
/* 80 */         Mth.lerp(alpha, from.factor, to.factor));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\ColorModifier$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */