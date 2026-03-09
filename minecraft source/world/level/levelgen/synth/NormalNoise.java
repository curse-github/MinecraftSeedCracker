/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NormalNoise
/*     */ {
/*     */   private static final double INPUT_FACTOR = 1.0181268882175227D;
/*     */   private static final double TARGET_DEVIATION = 0.3333333333333333D;
/*     */   private final double valueFactor;
/*     */   private final PerlinNoise first;
/*     */   private final PerlinNoise second;
/*     */   private final double maxValue;
/*     */   private final NoiseParameters parameters;
/*     */   
/*     */   @Deprecated
/*  40 */   public static NormalNoise createLegacyNetherBiome(RandomSource random, NoiseParameters parameters) { return new NormalNoise(random, parameters, false); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static NormalNoise create(RandomSource random, int firstOctave, double... amplitudes) { return create(random, new NoiseParameters(firstOctave, new DoubleArrayList(amplitudes))); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static NormalNoise create(RandomSource random, NoiseParameters parameters) { return new NormalNoise(random, parameters, true); }
/*     */ 
/*     */   
/*     */   private NormalNoise(RandomSource random, NoiseParameters parameters, boolean useNewInitialization) {
/*  52 */     int firstOctave = parameters.firstOctave;
/*  53 */     DoubleList amplitudes = parameters.amplitudes;
/*     */     
/*  55 */     this.parameters = parameters;
/*     */     
/*  57 */     if (useNewInitialization) {
/*  58 */       this.first = PerlinNoise.create(random, firstOctave, amplitudes);
/*  59 */       this.second = PerlinNoise.create(random, firstOctave, amplitudes);
/*     */     } else {
/*  61 */       this.first = PerlinNoise.createLegacyForLegacyNetherBiome(random, firstOctave, amplitudes);
/*  62 */       this.second = PerlinNoise.createLegacyForLegacyNetherBiome(random, firstOctave, amplitudes);
/*     */     } 
/*     */     
/*  65 */     int minOctave = Integer.MAX_VALUE;
/*  66 */     int maxOctave = Integer.MIN_VALUE;
/*     */     
/*  68 */     DoubleListIterator iterator = amplitudes.iterator();
/*  69 */     while (iterator.hasNext()) {
/*  70 */       int i = iterator.nextIndex();
/*  71 */       double amplitude = iterator.nextDouble();
/*  72 */       if (amplitude != 0.0D) {
/*  73 */         minOctave = Math.min(minOctave, i);
/*  74 */         maxOctave = Math.max(maxOctave, i);
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     this.valueFactor = 0.16666666666666666D / expectedDeviation(maxOctave - minOctave);
/*     */     
/*  80 */     this.maxValue = (this.first.maxValue() + this.second.maxValue()) * this.valueFactor;
/*     */   }
/*     */ 
/*     */   
/*  84 */   public double maxValue() { return this.maxValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   private static double expectedDeviation(int octaveSpan) { return 0.1D * (1.0D + 1.0D / (octaveSpan + 1)); }
/*     */ 
/*     */   
/*     */   public double getValue(double x, double y, double z) {
/*  95 */     double x2 = x * 1.0181268882175227D;
/*  96 */     double y2 = y * 1.0181268882175227D;
/*  97 */     double z2 = z * 1.0181268882175227D;
/*  98 */     return (this.first.getValue(x, y, z) + this.second.getValue(x2, y2, z2)) * this.valueFactor;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public NoiseParameters parameters() { return this.parameters; }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 107 */     sb.append("NormalNoise {");
/* 108 */     sb.append("first: ");
/* 109 */     this.first.parityConfigString(sb);
/* 110 */     sb.append(", second: ");
/* 111 */     this.second.parityConfigString(sb);
/* 112 */     sb.append("}");
/*     */   }
/*     */   public static final class NoiseParameters extends Record { private final int firstOctave; private final DoubleList amplitudes;
/* 115 */     public NoiseParameters(int firstOctave, DoubleList amplitudes) { this.firstOctave = firstOctave; this.amplitudes = amplitudes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 115 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters; } public int firstOctave() { return this.firstOctave; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/* 115 */       //   0	8	1	o	Ljava/lang/Object; } public DoubleList amplitudes() { return this.amplitudes; }
/* 116 */     public static final Codec<NoiseParameters> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 117 */           .fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave), Codec.DOUBLE
/* 118 */           .listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes))
/* 119 */         .apply(i, NoiseParameters::new));
/*     */     
/* 121 */     public static final Codec<Holder<NoiseParameters>> CODEC = RegistryFileCodec.create(Registries.NOISE, DIRECT_CODEC);
/*     */ 
/*     */     
/* 124 */     public NoiseParameters(int firstOctave, List<Double> amplitudes) { this(firstOctave, new DoubleArrayList(amplitudes)); }
/*     */ 
/*     */ 
/*     */     
/* 128 */     public NoiseParameters(int firstOctave, double firstAmplitude, double... amplitudes) { this(firstOctave, (DoubleList)Util.make(new DoubleArrayList(amplitudes), list -> list.add(0, firstAmplitude))); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\NormalNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */