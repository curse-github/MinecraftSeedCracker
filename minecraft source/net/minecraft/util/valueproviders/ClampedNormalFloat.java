/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedNormalFloat extends FloatProvider {
/* 11 */   public static final MapCodec<ClampedNormalFloat> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 12 */         .fieldOf("mean").forGetter(()), Codec.FLOAT
/* 13 */         .fieldOf("deviation").forGetter(()), Codec.FLOAT
/* 14 */         .fieldOf("min").forGetter(()), Codec.FLOAT
/* 15 */         .fieldOf("max").forGetter(()))
/* 16 */       .apply(i, ClampedNormalFloat::new)).validate(c -> {
/* 17 */         if (c.max < c.min) {
/* 18 */           return DataResult.error(());
/*    */         }
/* 20 */         return DataResult.success(c);
/*    */       });
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final float min;
/*    */   private final float max;
/*    */   
/* 29 */   public static ClampedNormalFloat of(float mean, float deviation, float min, float max) { return new ClampedNormalFloat(mean, deviation, min, max); }
/*    */ 
/*    */   
/*    */   private ClampedNormalFloat(float mean, float deviation, float min, float max) {
/* 33 */     this.mean = mean;
/* 34 */     this.deviation = deviation;
/* 35 */     this.min = min;
/* 36 */     this.max = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public float sample(RandomSource random) { return sample(random, this.mean, this.deviation, this.min, this.max); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static float sample(RandomSource random, float mean, float deviation, float min, float max) { return Mth.clamp(Mth.normal(random, mean, deviation), min, max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public float getMinValue() { return this.min; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public float getMaxValue() { return this.max; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public FloatProviderType<?> getType() { return FloatProviderType.CLAMPED_NORMAL; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public String toString() { return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\ClampedNormalFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */