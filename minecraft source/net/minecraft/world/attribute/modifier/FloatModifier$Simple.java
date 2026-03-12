/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
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
/*    */ @FunctionalInterface
/*    */ public interface Simple
/*    */   extends FloatModifier<Float>
/*    */ {
/* 39 */   default Codec<Float> argumentCodec(EnvironmentAttribute<Float> type) { return Codec.FLOAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   default LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> type) { return LerpFunction.ofFloat(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\FloatModifier$Simple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */