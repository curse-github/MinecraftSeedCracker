/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedNormalInt extends IntProvider {
/* 11 */   public static final MapCodec<ClampedNormalInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 12 */         .fieldOf("mean").forGetter(()), Codec.FLOAT
/* 13 */         .fieldOf("deviation").forGetter(()), Codec.INT
/* 14 */         .fieldOf("min_inclusive").forGetter(()), Codec.INT
/* 15 */         .fieldOf("max_inclusive").forGetter(()))
/* 16 */       .apply(i, ClampedNormalInt::new)).validate(c -> {
/* 17 */         if (c.maxInclusive < c.minInclusive) {
/* 18 */           return DataResult.error(());
/*    */         }
/* 20 */         return DataResult.success(c);
/*    */       });
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/* 29 */   public static ClampedNormalInt of(float mean, float deviation, int min_inclusive, int max_inclusive) { return new ClampedNormalInt(mean, deviation, min_inclusive, max_inclusive); }
/*    */ 
/*    */   
/*    */   private ClampedNormalInt(float mean, float deviation, int minInclusive, int maxInclusive) {
/* 33 */     this.mean = mean;
/* 34 */     this.deviation = deviation;
/* 35 */     this.minInclusive = minInclusive;
/* 36 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public int sample(RandomSource random) { return sample(random, this.mean, this.deviation, this.minInclusive, this.maxInclusive); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static int sample(RandomSource random, float mean, float deviation, float min_inclusive, float max_inclusive) { return (int)Mth.clamp(Mth.normal(random, mean, deviation), min_inclusive, max_inclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public int getMinValue() { return this.minInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int getMaxValue() { return this.maxInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public IntProviderType<?> getType() { return IntProviderType.CLAMPED_NORMAL; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public String toString() { return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\ClampedNormalInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */