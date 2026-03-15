 package net.minecraft.world.level.levelgen;
 import com.google.common.annotations.VisibleForTesting;
 import com.mojang.serialization.Codec;
 import net.minecraft.util.Mth;
 import net.minecraft.util.RandomSource;
 public class XoroshiroRandomSource
   implements RandomSource
 {
   private static final float FLOAT_UNIT = 5.9604645E-8F;
   private static final double DOUBLE_UNIT = 1.1102230246251565E-16D;
   private Xoroshiro128PlusPlus randomNumberGenerator;
   public static final Codec<XoroshiroRandomSource> CODEC = Xoroshiro128PlusPlus.CODEC.xmap(generator -> 
       new XoroshiroRandomSource(generator), source -> 
       source.randomNumberGenerator);
   private final MarsagliaPolarGaussian gaussianSource;
   public XoroshiroRandomSource(long seed) {
     this.gaussianSource = new MarsagliaPolarGaussian(this);
     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
   }
   public XoroshiroRandomSource(RandomSupport.Seed128bit seed) {
     this.gaussianSource = new MarsagliaPolarGaussian(this);
     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seed);
   }
   public XoroshiroRandomSource(long seedLo, long seedHi) {
     this.gaussianSource = new MarsagliaPolarGaussian(this);
     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seedLo, seedHi);
   }
   private XoroshiroRandomSource(Xoroshiro128PlusPlus randomNumberGenerator) {
     this.gaussianSource = new MarsagliaPolarGaussian(this);
     this.randomNumberGenerator = randomNumberGenerator;
   }
   public RandomSource fork() { return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong()); }
   public PositionalRandomFactory forkPositional() { return new XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong()); }
   public void setSeed(long seed) {
     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
     this.gaussianSource.reset();
   }
   public int nextInt() { return (int)this.randomNumberGenerator.nextLong(); }
   public int nextInt(int bound) {
     if (bound <= 0) {
       throw new IllegalArgumentException("Bound must be positive");
     }
     long randomBits = Integer.toUnsignedLong(nextInt());
     long multipliedRandomBits = randomBits * bound;
     long fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
     if (fractionalPart < bound) {
       int unbiasedBucketsStartIndex = Integer.remainderUnsigned((bound ^ 0xFFFFFFFF) + 1, bound);
       while (fractionalPart < unbiasedBucketsStartIndex) {
         randomBits = Integer.toUnsignedLong(nextInt());
         multipliedRandomBits = randomBits * bound;
         fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
       } 
     } 
     long integerPart = multipliedRandomBits >> 32;
     return (int)integerPart;
   }
   public long nextLong() { return this.randomNumberGenerator.nextLong(); }
   public boolean nextBoolean() { return ((this.randomNumberGenerator.nextLong() & 0x1L) != 0L); }
   public float nextFloat() { return (float)nextBits(24) * 5.9604645E-8F; }
   public double nextDouble() { return nextBits(53) * 1.1102230246251565E-16D; }
   public double nextGaussian() { return this.gaussianSource.nextGaussian(); }
   public void consumeCount(int rounds) {
     for (int i = 0; i < rounds; i++) {
       this.randomNumberGenerator.nextLong();
     }
   }
   private long nextBits(int bits) { return this.randomNumberGenerator.nextLong() >>> 64 - bits; }
   public static class XoroshiroPositionalRandomFactory
     implements PositionalRandomFactory {
     private final long seedLo;
     private final long seedHi;
     public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
       this.seedLo = seedLo;
       this.seedHi = seedHi;
     }
     public RandomSource at(int x, int y, int z) {
       long positionalSeed = Mth.getSeed(x, y, z);
       long randomSeed = positionalSeed ^ this.seedLo;
       return new XoroshiroRandomSource(randomSeed, this.seedHi);
     }
     public RandomSource fromHashOf(String name) {
       RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
       return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
     }
     public RandomSource fromSeed(long seed) { return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi); }
     @VisibleForTesting
     public void parityConfigString(StringBuilder sb) { sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi); }
   }
 }
