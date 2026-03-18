 package net.minecraft.world.level.levelgen.synth;
 import com.google.common.annotations.VisibleForTesting;
 import com.google.common.collect.ImmutableList;
 import com.mojang.datafixers.util.Pair;
 import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
 import it.unimi.dsi.fastutil.doubles.DoubleList;
 import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
 import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
 import it.unimi.dsi.fastutil.ints.IntSortedSet;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.List;
 import java.util.Locale;
 import java.util.Objects;
 import java.util.stream.IntStream;
 import net.minecraft.util.Mth;
 import net.minecraft.util.RandomSource;
 import net.minecraft.world.level.levelgen.PositionalRandomFactory;
 public class PerlinNoise
 {
   private static final int ROUND_OFF = 33554432;
   private final ImprovedNoise[] noiseLevels;
   private final int firstOctave;
   private final DoubleList amplitudes;
   private final double lowestFreqValueFactor;
   private final double lowestFreqInputFactor;
   private final double maxValue;
   @Deprecated
   public static PerlinNoise createLegacyForBlendedNoise(RandomSource random, IntStream octaves) { return new PerlinNoise(random, makeAmplitudes(new IntRBTreeSet((Collection)octaves.boxed().collect(ImmutableList.toImmutableList()))), false); }
   @Deprecated
   public static PerlinNoise createLegacyForLegacyNetherBiome(RandomSource random, int firstOctave, DoubleList amplitudes) { return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudes), false); }
   public static PerlinNoise create(RandomSource random, IntStream octaves) { return create(random, (List)octaves.boxed().collect(ImmutableList.toImmutableList())); }
   public static PerlinNoise create(RandomSource random, List<Integer> octaveSet) { return new PerlinNoise(random, makeAmplitudes(new IntRBTreeSet(octaveSet)), true); }
   public static PerlinNoise create(RandomSource random, int firstOctave, double firstAmplitude, double... amplitudes) {
     DoubleArrayList amplitudeList = new DoubleArrayList(amplitudes);
     amplitudeList.add(0, firstAmplitude);
     return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudeList), true);
   }
   public static PerlinNoise create(RandomSource random, int firstOctave, DoubleList amplitudes) { return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudes), true); }
   private static Pair<Integer, DoubleList> makeAmplitudes(IntSortedSet octaveSet) {
     if (octaveSet.isEmpty()) {
       throw new IllegalArgumentException("Need some octaves!");
     }
     int lowFreqOctaves = -octaveSet.firstInt();
     int highFreqOctaves = octaveSet.lastInt();
     int octaves = lowFreqOctaves + highFreqOctaves + 1;
     if (octaves < 1) {
       throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
     }
     DoubleArrayList doubleArrayList = new DoubleArrayList(new double[octaves]);
     IntBidirectionalIterator iterator = octaveSet.iterator();
     while (iterator.hasNext()) {
       int octave = iterator.nextInt();
       doubleArrayList.set(octave + lowFreqOctaves, 1.0D);
     } 
     return Pair.of(Integer.valueOf(-lowFreqOctaves), doubleArrayList);
   }
   protected PerlinNoise(RandomSource random, Pair<Integer, DoubleList> pair, boolean useNewInitialization) {
     this.firstOctave = ((Integer)pair.getFirst()).intValue();
     this.amplitudes = (DoubleList)pair.getSecond();
     int octaves = this.amplitudes.size();
     int zeroOctaveIndex = -this.firstOctave;
     this.noiseLevels = new ImprovedNoise[octaves];
     if (useNewInitialization) {
       PositionalRandomFactory positional = random.forkPositional();
       for (int i = 0; i < octaves; i++) {
         if (this.amplitudes.getDouble(i) != 0.0D) {
           int octave = this.firstOctave + i;
           this.noiseLevels[i] = new ImprovedNoise(positional.fromHashOf("octave_" + octave));
         } 
       } 
     } else {
       ImprovedNoise zeroOctave = new ImprovedNoise(random);
       if (zeroOctaveIndex >= 0 && zeroOctaveIndex < octaves) {
         double zeroOctaveAmplitude = this.amplitudes.getDouble(zeroOctaveIndex);
         if (zeroOctaveAmplitude != 0.0D) {
           this.noiseLevels[zeroOctaveIndex] = zeroOctave;
         }
       } 
       for (int i = zeroOctaveIndex - 1; i >= 0; i--) {
         if (i < octaves) {
           double amplitude = this.amplitudes.getDouble(i);
           if (amplitude != 0.0D) {
             this.noiseLevels[i] = new ImprovedNoise(random);
           } else {
             skipOctave(random);
           } 
         } else {
           skipOctave(random);
         } 
       } 
       if (Arrays.stream(this.noiseLevels).filter(Objects::nonNull).count() != this.amplitudes.stream().filter(a -> (a.doubleValue() != 0.0D)).count()) {
         throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes");
       }
       if (zeroOctaveIndex < octaves - 1)
       {
         throw new IllegalArgumentException("Positive octaves are temporarily disabled");
       }
     } 
     this.lowestFreqInputFactor = Math.pow(2.0D, -zeroOctaveIndex);
     this.lowestFreqValueFactor = Math.pow(2.0D, (octaves - 1)) / (Math.pow(2.0D, octaves) - 1.0D);
     this.maxValue = edgeValue(2.0D);
   }
   protected double maxValue() { return this.maxValue; }
   private static void skipOctave(RandomSource random) { random.consumeCount(262); }
   public double getValue(double x, double y, double z) { return getValue(x, y, z, 0.0D, 0.0D, false); }
   @Deprecated
   public double getValue(double x, double y, double z, double yScale, double yFudge, boolean yFlatHack) {
     double value = 0.0D;
     double factor = this.lowestFreqInputFactor;
     double valueFactor = this.lowestFreqValueFactor;
     for (int i = 0; i < this.noiseLevels.length; i++) {
       ImprovedNoise noise = this.noiseLevels[i];
       if (noise != null) {
         double noiseVal = noise.noise(wrap(x * factor), yFlatHack ? -noise.yo : wrap(y * factor), wrap(z * factor), yScale * factor, yFudge * factor);
         value += this.amplitudes.getDouble(i) * noiseVal * valueFactor;
       } 
       factor *= 2.0D;
       valueFactor /= 2.0D;
     } 
     return value;
   }
   public double maxBrokenValue(double yScale) { return edgeValue(yScale + 2.0D); }
   private double edgeValue(double noiseValue) {
     double value = 0.0D;
     double valueFactor = this.lowestFreqValueFactor;
     for (int i = 0; i < this.noiseLevels.length; i++) {
       ImprovedNoise noise = this.noiseLevels[i];
       if (noise != null) {
         value += this.amplitudes.getDouble(i) * noiseValue * valueFactor;
       }
       valueFactor /= 2.0D;
     } 
     return value;
   }
   public ImprovedNoise getOctaveNoise(int i) { return this.noiseLevels[this.noiseLevels.length - 1 - i]; }
   public static double wrap(double x) { return x - Mth.lfloor(x / 3.3554432E7D + 0.5D) * 3.3554432E7D; }
   protected int firstOctave() { return this.firstOctave; }
   protected DoubleList amplitudes() { return this.amplitudes; }
   @VisibleForTesting
   public void parityConfigString(StringBuilder sb) {
     sb.append("PerlinNoise{");
     List<String> amplitudeStrings = this.amplitudes.stream().map(d -> String.format(Locale.ROOT, "%.2f", new Object[] { d })).toList();
     sb.append("first octave: ").append(this.firstOctave).append(", amplitudes: ").append(amplitudeStrings)
       .append(", noise levels: [");
     for (int i = 0; i < this.noiseLevels.length; i++) {
       sb.append(i).append(": ");
       ImprovedNoise noiseLevel = this.noiseLevels[i];
       if (noiseLevel == null) {
         sb.append("null");
       } else {
         noiseLevel.parityConfigString(sb);
       } 
       sb.append(", ");
     } 
     sb.append("]");
     sb.append("}");
   }
 }
