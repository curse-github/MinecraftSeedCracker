/*    */ package net.minecraft.world.attribute.modifier;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface ColorModifier<Argument> extends AttributeModifier<Integer, Argument> {
/* 13 */   public static final ColorModifier<Integer> ALPHA_BLEND = new ColorModifier<Integer>()
/*    */     {
/*    */       public Integer apply(Integer subject, Integer argument) {
/* 16 */         return Integer.valueOf(ARGB.alphaBlend(subject.intValue(), argument.intValue()));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 21 */       public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) { return ExtraCodecs.STRING_ARGB_COLOR; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 26 */       public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */     };
/*    */ 
/*    */   
/* 30 */   public static final ColorModifier<Integer> ADD = ARGB::addRgb;
/* 31 */   public static final ColorModifier<Integer> SUBTRACT = ARGB::subtractRgb;
/* 32 */   public static final ColorModifier<Integer> MULTIPLY_RGB = ARGB::multiply;
/* 33 */   public static final ColorModifier<Integer> MULTIPLY_ARGB = ARGB::multiply;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface RgbModifier
/*    */     extends ColorModifier<Integer>
/*    */   {
/* 39 */     default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) { return ExtraCodecs.STRING_RGB_COLOR; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ArgbModifier
/*    */     extends ColorModifier<Integer>
/*    */   {
/*    */     default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 52 */       return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, color -> 
/*    */           
/* 54 */           (ARGB.alpha(color.intValue()) == 255) ? Either.right(color) : Either.left(color));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */   }
/*    */ 
/*    */   
/* 64 */   public static final ColorModifier<BlendToGray> BLEND_TO_GRAY = new ColorModifier<BlendToGray>()
/*    */     {
/*    */       public Integer apply(Integer subject, BlendToGray argument) {
/* 67 */         int multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject.intValue()), argument.brightness);
/* 68 */         return Integer.valueOf(ARGB.srgbLerp(argument.factor, subject.intValue(), multipliedGreyscale));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 73 */       public Codec<BlendToGray> argumentCodec(EnvironmentAttribute<Integer> type) { return BlendToGray.CODEC; }
/*    */ 
/*    */ 
/*    */       
/*    */       public LerpFunction<BlendToGray> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 78 */         return (alpha, from, to) -> new BlendToGray(
/* 79 */             Mth.lerp(alpha, from.brightness, to.brightness), 
/* 80 */             Mth.lerp(alpha, from.factor, to.factor));
/*    */       }
/*    */     };
/*    */   public static final class BlendToGray extends Record { private final float brightness; private final float factor;
/*    */     
/* 85 */     public BlendToGray(float brightness, float factor) { this.brightness = brightness; this.factor = factor; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 85 */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray; } public float brightness() { return this.brightness; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/ColorModifier$BlendToGray;
/* 85 */       //   0	8	1	o	Ljava/lang/Object; } public float factor() { return this.factor; }
/*    */ 
/*    */ 
/*    */     
/* 89 */     public static final Codec<BlendToGray> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 90 */           Codec.floatRange(0.0F, 1.0F).fieldOf("brightness").forGetter(BlendToGray::brightness), 
/* 91 */           Codec.floatRange(0.0F, 1.0F).fieldOf("factor").forGetter(BlendToGray::factor))
/* 92 */         .apply(i, BlendToGray::new)); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\ColorModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */