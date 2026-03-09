/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */ @FunctionalInterface
/*    */ public interface RgbModifier
/*    */   extends ColorModifier<Integer>
/*    */ {
/* 39 */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) { return ExtraCodecs.STRING_RGB_COLOR; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\ColorModifier$RgbModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */