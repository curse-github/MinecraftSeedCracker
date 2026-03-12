/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BiasedToBottomHeight extends HeightProvider {
/* 13 */   public static final MapCodec<BiasedToBottomHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(VerticalAnchor.CODEC
/* 14 */         .fieldOf("min_inclusive").forGetter(()), VerticalAnchor.CODEC
/* 15 */         .fieldOf("max_inclusive").forGetter(()), 
/* 16 */         Codec.intRange(1, 2147483647).optionalFieldOf("inner", Integer.valueOf(1)).forGetter(()))
/* 17 */       .apply(i, BiasedToBottomHeight::new));
/*    */   
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private BiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
/* 26 */     this.minInclusive = minInclusive;
/* 27 */     this.maxInclusive = maxInclusive;
/* 28 */     this.inner = inner;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public static BiasedToBottomHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int offset) { return new BiasedToBottomHeight(minInclusive, maxInclusive, offset); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 37 */     int min = this.minInclusive.resolveY(context);
/* 38 */     int max = this.maxInclusive.resolveY(context);
/* 39 */     if (max - min - this.inner + 1 <= 0) {
/* 40 */       LOGGER.warn("Empty height range: {}", this);
/* 41 */       return min;
/*    */     } 
/*    */     
/* 44 */     int limit = random.nextInt(max - min - this.inner + 1);
/* 45 */     return random.nextInt(limit + this.inner) + min;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public HeightProviderType<?> getType() { return HeightProviderType.BIASED_TO_BOTTOM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String toString() { return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\BiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */