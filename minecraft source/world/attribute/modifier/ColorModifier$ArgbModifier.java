/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ARGB;
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
/*    */ public interface ArgbModifier
/*    */   extends ColorModifier<Integer>
/*    */ {
/*    */   default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 52 */     return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, color -> 
/*    */         
/* 54 */         (ARGB.alpha(color.intValue()) == 255) ? Either.right(color) : Either.left(color));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) { return LerpFunction.ofColor(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\ColorModifier$ArgbModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */