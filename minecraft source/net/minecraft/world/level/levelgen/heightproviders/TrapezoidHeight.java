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
/*    */ public class TrapezoidHeight extends HeightProvider {
/* 14 */   public static final MapCodec<TrapezoidHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(VerticalAnchor.CODEC
/* 15 */         .fieldOf("min_inclusive").forGetter(()), VerticalAnchor.CODEC
/* 16 */         .fieldOf("max_inclusive").forGetter(()), Codec.INT
/* 17 */         .optionalFieldOf("plateau", Integer.valueOf(0)).forGetter(()))
/* 18 */       .apply(i, TrapezoidHeight::new));
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int plateau;
/*    */   
/*    */   private TrapezoidHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int plateau) {
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/* 29 */     this.plateau = plateau;
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static TrapezoidHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int plateau) { return new TrapezoidHeight(minInclusive, maxInclusive, plateau); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static TrapezoidHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) { return of(minInclusive, maxInclusive, 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 42 */     int min = this.minInclusive.resolveY(context);
/* 43 */     int max = this.maxInclusive.resolveY(context);
/* 44 */     if (min > max) {
/* 45 */       LOGGER.warn("Empty height range: {}", this);
/* 46 */       return min;
/*    */     } 
/*    */     
/* 49 */     int range = max - min;
/* 50 */     if (this.plateau >= range) {
/* 51 */       return Mth.randomBetweenInclusive(random, min, max);
/*    */     }
/*    */     
/* 54 */     int plateauStart = (range - this.plateau) / 2;
/* 55 */     int plateauEnd = range - plateauStart;
/*    */     
/* 57 */     return min + Mth.randomBetweenInclusive(random, 0, plateauEnd) + Mth.randomBetweenInclusive(random, 0, plateauStart);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public HeightProviderType<?> getType() { return HeightProviderType.TRAPEZOID; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public String toString() { return (this.plateau == 0) ? ("triangle (" + 
/* 68 */       String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + ")") : ("trapezoid(" + 
/*    */       
/* 70 */       this.plateau + ") in [" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\TrapezoidHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */