/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements ColorModifier<Integer>
/*    */ {
/* 16 */   public Integer apply(Integer subject, Integer argument) { return Integer.valueOf(ARGB.alphaBlend(subject.intValue(), argument.intValue())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) { return ExtraCodecs.STRING_ARGB_COLOR; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\ColorModifier$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */