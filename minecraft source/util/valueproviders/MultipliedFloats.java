/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MultipliedFloats
/*    */   implements SampledFloat
/*    */ {
/*    */   private final SampledFloat[] values;
/*    */   
/* 11 */   public MultipliedFloats(SampledFloat... values) { this.values = values; }
/*    */ 
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 16 */     float result = 1.0F;
/* 17 */     for (SampledFloat value : this.values) {
/* 18 */       result *= value.sample(random);
/*    */     }
/* 20 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public String toString() { return "MultipliedFloats" + Arrays.toString(this.values); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\MultipliedFloats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */