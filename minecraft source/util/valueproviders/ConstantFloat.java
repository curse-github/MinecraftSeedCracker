/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ConstantFloat extends FloatProvider {
/*  8 */   public static final ConstantFloat ZERO = new ConstantFloat(0.0F);
/*    */   
/* 10 */   public static final MapCodec<ConstantFloat> CODEC = Codec.FLOAT.fieldOf("value").xmap(ConstantFloat::of, ConstantFloat::getValue);
/*    */   
/*    */   private final float value;
/*    */   
/*    */   public static ConstantFloat of(float value) {
/* 15 */     if (value == 0.0F) {
/* 16 */       return ZERO;
/*    */     }
/* 18 */     return new ConstantFloat(value);
/*    */   }
/*    */ 
/*    */   
/* 22 */   private ConstantFloat(float value) { this.value = value; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public float getValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public float sample(RandomSource random) { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public float getMinValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public float getMaxValue() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public FloatProviderType<?> getType() { return FloatProviderType.CONSTANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public String toString() { return Float.toString(this.value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\ConstantFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */