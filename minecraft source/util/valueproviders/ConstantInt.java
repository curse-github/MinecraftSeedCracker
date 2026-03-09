/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ConstantInt extends IntProvider {
/*  8 */   public static final ConstantInt ZERO = new ConstantInt(0);
/*    */   
/* 10 */   public static final MapCodec<ConstantInt> CODEC = Codec.INT.fieldOf("value").xmap(ConstantInt::of, ConstantInt::getValue);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   public static ConstantInt of(int value) {
/* 15 */     if (value == 0) {
/* 16 */       return ZERO;
/*    */     }
/* 18 */     return new ConstantInt(value);
/*    */   }
/*    */ 
/*    */   
/* 22 */   private ConstantInt(int value) { this.value = value; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public int getValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public int sample(RandomSource random) { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getMinValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public int getMaxValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public IntProviderType<?> getType() { return IntProviderType.CONSTANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public String toString() { return Integer.toString(this.value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\ConstantInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */