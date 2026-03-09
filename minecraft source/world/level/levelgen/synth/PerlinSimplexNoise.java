/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PerlinSimplexNoise
/*    */ {
/*    */   private final SimplexNoise[] noiseLevels;
/*    */   private final double highestFreqValueFactor;
/*    */   private final double highestFreqInputFactor;
/*    */   
/* 18 */   public PerlinSimplexNoise(RandomSource random, List<Integer> octaveSet) { this(random, new IntRBTreeSet(octaveSet)); }
/*    */ 
/*    */   
/*    */   private PerlinSimplexNoise(RandomSource random, IntSortedSet octaveSet) {
/* 22 */     if (octaveSet.isEmpty()) {
/* 23 */       throw new IllegalArgumentException("Need some octaves!");
/*    */     }
/*    */     
/* 26 */     int lowFreqOctaves = -octaveSet.firstInt();
/* 27 */     int highFreqOctaves = octaveSet.lastInt();
/*    */     
/* 29 */     int octaves = lowFreqOctaves + highFreqOctaves + 1;
/* 30 */     if (octaves < 1) {
/* 31 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
/*    */     }
/*    */     
/* 34 */     SimplexNoise zeroOctave = new SimplexNoise(random);
/* 35 */     int zeroOctaveIndex = highFreqOctaves;
/*    */     
/* 37 */     this.noiseLevels = new SimplexNoise[octaves];
/* 38 */     if (zeroOctaveIndex >= 0 && zeroOctaveIndex < octaves && octaveSet.contains(0)) {
/* 39 */       this.noiseLevels[zeroOctaveIndex] = zeroOctave;
/*    */     }
/*    */     
/* 42 */     for (int i = zeroOctaveIndex + 1; i < octaves; i++) {
/* 43 */       if (i >= 0 && octaveSet.contains(zeroOctaveIndex - i)) {
/* 44 */         this.noiseLevels[i] = new SimplexNoise(random);
/*    */       } else {
/* 46 */         random.consumeCount(262);
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     if (highFreqOctaves > 0) {
/*    */       
/* 52 */       long positiveOctaveSeed = (long)(zeroOctave.getValue(zeroOctave.xo, zeroOctave.yo, zeroOctave.zo) * 9.223372036854776E18D);
/* 53 */       WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(positiveOctaveSeed));
/* 54 */       for (int i = zeroOctaveIndex - 1; i >= 0; i--) {
/* 55 */         if (i < octaves && octaveSet.contains(zeroOctaveIndex - i)) {
/* 56 */           this.noiseLevels[i] = new SimplexNoise(worldgenRandom);
/*    */         } else {
/* 58 */           worldgenRandom.consumeCount(262);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 63 */     this.highestFreqInputFactor = Math.pow(2.0D, highFreqOctaves);
/* 64 */     this.highestFreqValueFactor = 1.0D / (Math.pow(2.0D, octaves) - 1.0D);
/*    */   }
/*    */   
/*    */   public double getValue(double x, double y, boolean useNoiseStart) {
/* 68 */     double value = 0.0D;
/* 69 */     double factor = this.highestFreqInputFactor;
/* 70 */     double valueFactor = this.highestFreqValueFactor;
/*    */     
/* 72 */     for (SimplexNoise noiseLevel : this.noiseLevels) {
/* 73 */       if (noiseLevel != null) {
/* 74 */         value += noiseLevel.getValue(x * factor + (useNoiseStart ? noiseLevel.xo : 0.0D), y * factor + (useNoiseStart ? noiseLevel.yo : 0.0D)) * valueFactor;
/*    */       }
/* 76 */       factor /= 2.0D;
/* 77 */       valueFactor *= 2.0D;
/*    */     } 
/*    */     
/* 80 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\PerlinSimplexNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */