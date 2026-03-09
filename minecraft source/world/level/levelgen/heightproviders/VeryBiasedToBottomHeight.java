/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class VeryBiasedToBottomHeight extends HeightProvider {
/* 14 */   public static final MapCodec<VeryBiasedToBottomHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(VerticalAnchor.CODEC
/* 15 */         .fieldOf("min_inclusive").forGetter(()), VerticalAnchor.CODEC
/* 16 */         .fieldOf("max_inclusive").forGetter(()), 
/* 17 */         Codec.intRange(1, 2147483647).optionalFieldOf("inner", Integer.valueOf(1)).forGetter(()))
/* 18 */       .apply(i, VeryBiasedToBottomHeight::new));
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private VeryBiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/* 29 */     this.inner = inner;
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static VeryBiasedToBottomHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int offset) { return new VeryBiasedToBottomHeight(minInclusive, maxInclusive, offset); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 38 */     int min = this.minInclusive.resolveY(context);
/* 39 */     int max = this.maxInclusive.resolveY(context);
/* 40 */     if (max - min - this.inner + 1 <= 0) {
/* 41 */       LOGGER.warn("Empty height range: {}", this);
/* 42 */       return min;
/*    */     } 
/*    */     
/* 45 */     int upperInclusive = Mth.nextInt(random, min + this.inner, max);
/* 46 */     int biasedUpperInclusive = Mth.nextInt(random, min, upperInclusive - 1);
/* 47 */     return Mth.nextInt(random, min, biasedUpperInclusive - 1 + this.inner);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public HeightProviderType<?> getType() { return HeightProviderType.VERY_BIASED_TO_BOTTOM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String toString() { return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\VeryBiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */