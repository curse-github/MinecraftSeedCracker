/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListInt extends IntProvider {
/* 10 */   public static final MapCodec<WeightedListInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 11 */         WeightedList.nonEmptyCodec(IntProvider.CODEC).fieldOf("distribution").forGetter(()))
/* 12 */       .apply(i, WeightedListInt::new));
/*    */   
/*    */   private final WeightedList<IntProvider> distribution;
/*    */   private final int minValue;
/*    */   private final int maxValue;
/*    */   
/*    */   public WeightedListInt(WeightedList<IntProvider> distribution) {
/* 19 */     this.distribution = distribution;
/* 20 */     int min = Integer.MAX_VALUE;
/* 21 */     int max = Integer.MIN_VALUE;
/* 22 */     for (Weighted<IntProvider> value : distribution.unwrap()) {
/* 23 */       int entryMin = ((IntProvider)value.value()).getMinValue();
/* 24 */       int entryMax = ((IntProvider)value.value()).getMaxValue();
/* 25 */       min = Math.min(min, entryMin);
/* 26 */       max = Math.max(max, entryMax);
/*    */     } 
/* 28 */     this.minValue = min;
/* 29 */     this.maxValue = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public int sample(RandomSource random) { return ((IntProvider)this.distribution.getRandomOrThrow(random)).sample(random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public int getMinValue() { return this.minValue; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getMaxValue() { return this.maxValue; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public IntProviderType<?> getType() { return IntProviderType.WEIGHTED_LIST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\WeightedListInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */