/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public static enum BooleanModifier implements AttributeModifier<Boolean, Boolean> {
/*  8 */   AND,
/*  9 */   NAND,
/* 10 */   OR,
/* 11 */   NOR,
/* 12 */   XOR,
/* 13 */   XNOR;
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean apply(Boolean subject, Boolean argument) {
/* 18 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: case 4: case 5: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 24 */       Boolean.valueOf((argument == subject));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public Codec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> type) { return Codec.BOOL; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public LerpFunction<Boolean> argumentKeyframeLerp(EnvironmentAttribute<Boolean> type) { return LerpFunction.ofConstant(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\modifier\BooleanModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */