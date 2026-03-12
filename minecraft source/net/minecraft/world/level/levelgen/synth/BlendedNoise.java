/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlendedNoise
/*     */   implements DensityFunction.SimpleFunction
/*     */ {
/*  26 */   private static final Codec<Double> SCALE_RANGE = Codec.doubleRange(0.001D, 1000.0D);
/*     */   
/*  28 */   private static final MapCodec<BlendedNoise> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SCALE_RANGE
/*  29 */         .fieldOf("xz_scale").forGetter(()), SCALE_RANGE
/*  30 */         .fieldOf("y_scale").forGetter(()), SCALE_RANGE
/*  31 */         .fieldOf("xz_factor").forGetter(()), SCALE_RANGE
/*  32 */         .fieldOf("y_factor").forGetter(()), 
/*  33 */         Codec.doubleRange(1.0D, 8.0D).fieldOf("smear_scale_multiplier").forGetter(()))
/*  34 */       .apply(i, BlendedNoise::createUnseeded));
/*     */   
/*  36 */   public static final KeyDispatchDataCodec<BlendedNoise> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);
/*     */   
/*     */   private final PerlinNoise minLimitNoise;
/*     */   
/*     */   private final PerlinNoise maxLimitNoise;
/*     */   
/*     */   private final PerlinNoise mainNoise;
/*     */   
/*     */   private final double xzMultiplier;
/*     */   
/*     */   private final double yMultiplier;
/*     */   private final double xzFactor;
/*     */   private final double yFactor;
/*     */   private final double smearScaleMultiplier;
/*     */   private final double maxValue;
/*     */   private final double xzScale;
/*     */   private final double yScale;
/*     */   
/*  54 */   public static BlendedNoise createUnseeded(double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) { return new BlendedNoise(new XoroshiroRandomSource(0L), xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier); }
/*     */ 
/*     */   
/*     */   private BlendedNoise(PerlinNoise minLimitNoise, PerlinNoise maxLimitNoise, PerlinNoise mainNoise, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
/*  58 */     this.minLimitNoise = minLimitNoise;
/*  59 */     this.maxLimitNoise = maxLimitNoise;
/*  60 */     this.mainNoise = mainNoise;
/*     */     
/*  62 */     this.xzScale = xzScale;
/*  63 */     this.yScale = yScale;
/*  64 */     this.xzFactor = xzFactor;
/*  65 */     this.yFactor = yFactor;
/*  66 */     this.smearScaleMultiplier = smearScaleMultiplier;
/*     */     
/*  68 */     this.xzMultiplier = 684.412D * this.xzScale;
/*  69 */     this.yMultiplier = 684.412D * this.yScale;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.maxValue = minLimitNoise.maxBrokenValue(this.yMultiplier);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public BlendedNoise(RandomSource random, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
/*  79 */     this(
/*  80 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0)), 
/*  81 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0)), 
/*  82 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-7, 0)), xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public BlendedNoise withNewRandom(RandomSource terrainRandom) { return new BlendedNoise(terrainRandom, this.xzScale, this.yScale, this.xzFactor, this.yFactor, this.smearScaleMultiplier); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/*  99 */     double limitX = context.blockX() * this.xzMultiplier;
/* 100 */     double limitY = context.blockY() * this.yMultiplier;
/* 101 */     double limitZ = context.blockZ() * this.xzMultiplier;
/*     */     
/* 103 */     double mainX = limitX / this.xzFactor;
/* 104 */     double mainY = limitY / this.yFactor;
/* 105 */     double mainZ = limitZ / this.xzFactor;
/*     */     
/* 107 */     double limitSmear = this.yMultiplier * this.smearScaleMultiplier;
/* 108 */     double mainSmear = limitSmear / this.yFactor;
/*     */     
/* 110 */     double blendMin = 0.0D;
/* 111 */     double blendMax = 0.0D;
/* 112 */     double mainNoiseValue = 0.0D;
/*     */     
/* 114 */     boolean optimizeLoop = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     double pow = 1.0D;
/*     */ 
/*     */     
/* 125 */     for (int i = 0; i < 8; i++) {
/* 126 */       ImprovedNoise noise = this.mainNoise.getOctaveNoise(i);
/* 127 */       if (noise != null) {
/* 128 */         mainNoiseValue += noise.noise(PerlinNoise.wrap(mainX * pow), PerlinNoise.wrap(mainY * pow), PerlinNoise.wrap(mainZ * pow), mainSmear * pow, mainY * pow) / pow;
/*     */       }
/* 130 */       pow /= 2.0D;
/*     */     } 
/*     */     
/* 133 */     double factor = (mainNoiseValue / 10.0D + 1.0D) / 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 137 */     boolean isMax = (factor >= 1.0D);
/* 138 */     boolean isMin = (factor <= 0.0D);
/* 139 */     pow = 1.0D;
/* 140 */     for (int i = 0; i < 16; i++) {
/* 141 */       double wx = PerlinNoise.wrap(limitX * pow);
/* 142 */       double wy = PerlinNoise.wrap(limitY * pow);
/* 143 */       double wz = PerlinNoise.wrap(limitZ * pow);
/* 144 */       double yScalePow = limitSmear * pow;
/* 145 */       if (!isMax) {
/* 146 */         ImprovedNoise minNoise = this.minLimitNoise.getOctaveNoise(i);
/* 147 */         if (minNoise != null) {
/* 148 */           blendMin += minNoise.noise(wx, wy, wz, yScalePow, limitY * pow) / pow;
/*     */         }
/*     */       } 
/* 151 */       if (!isMin) {
/* 152 */         ImprovedNoise maxNoise = this.maxLimitNoise.getOctaveNoise(i);
/* 153 */         if (maxNoise != null) {
/* 154 */           blendMax += maxNoise.noise(wx, wy, wz, yScalePow, limitY * pow) / pow;
/*     */         }
/*     */       } 
/* 157 */       pow /= 2.0D;
/*     */     } 
/*     */ 
/*     */     
/* 161 */     return Mth.clampedLerp(factor, blendMin / 512.0D, blendMax / 512.0D) / 128.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public double minValue() { return -maxValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public double maxValue() { return this.maxValue; }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 176 */     sb.append("BlendedNoise{minLimitNoise=");
/* 177 */     this.minLimitNoise.parityConfigString(sb);
/* 178 */     sb.append(", maxLimitNoise=");
/* 179 */     this.maxLimitNoise.parityConfigString(sb);
/* 180 */     sb.append(", mainNoise=");
/* 181 */     this.mainNoise.parityConfigString(sb);
/*     */     
/* 183 */     sb.append(
/* 184 */         String.format(Locale.ROOT, ", xzScale=%.3f, yScale=%.3f, xzMainScale=%.3f, yMainScale=%.3f, cellWidth=4, cellHeight=8", new Object[] {
/* 185 */             Double.valueOf(684.412D), Double.valueOf(684.412D), Double.valueOf(8.555150000000001D), Double.valueOf(4.277575000000001D)
/* 186 */           })).append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public KeyDispatchDataCodec<? extends DensityFunction> codec() { return CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\BlendedNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */