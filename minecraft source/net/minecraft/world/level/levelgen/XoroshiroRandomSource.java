/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ public class XoroshiroRandomSource
/*     */   implements RandomSource
/*     */ {
/*     */   private static final float FLOAT_UNIT = 5.9604645E-8F;
/*     */   private static final double DOUBLE_UNIT = 1.1102230246251565E-16D;
/*     */   private Xoroshiro128PlusPlus randomNumberGenerator;
/*  15 */   public static final Codec<XoroshiroRandomSource> CODEC = Xoroshiro128PlusPlus.CODEC.xmap(generator -> 
/*  16 */       new XoroshiroRandomSource(generator), source -> 
/*  17 */       source.randomNumberGenerator);
/*     */   private final MarsagliaPolarGaussian gaussianSource;
/*     */   
/*     */   public XoroshiroRandomSource(long seed) {
/*  21 */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*     */ 
/*     */     
/*  24 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*     */   }
/*     */   public XoroshiroRandomSource(RandomSupport.Seed128bit seed) {
/*     */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*  28 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seed);
/*     */   }
/*     */   public XoroshiroRandomSource(long seedLo, long seedHi) {
/*     */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*  32 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seedLo, seedHi);
/*     */   }
/*     */   private XoroshiroRandomSource(Xoroshiro128PlusPlus randomNumberGenerator) {
/*     */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*  36 */     this.randomNumberGenerator = randomNumberGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public RandomSource fork() { return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public PositionalRandomFactory forkPositional() { return new XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/*  51 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*  52 */     this.gaussianSource.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public int nextInt() { return (int)this.randomNumberGenerator.nextLong(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextInt(int bound) {
/*  62 */     if (bound <= 0) {
/*  63 */       throw new IllegalArgumentException("Bound must be positive");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     long randomBits = Integer.toUnsignedLong(nextInt());
/*     */ 
/*     */     
/*  72 */     long multipliedRandomBits = randomBits * bound;
/*     */     
/*  74 */     long fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*     */ 
/*     */     
/*  77 */     if (fractionalPart < bound) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       int unbiasedBucketsStartIndex = Integer.remainderUnsigned((bound ^ 0xFFFFFFFF) + 1, bound);
/*  83 */       while (fractionalPart < unbiasedBucketsStartIndex) {
/*     */         
/*  85 */         randomBits = Integer.toUnsignedLong(nextInt());
/*  86 */         multipliedRandomBits = randomBits * bound;
/*  87 */         fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     long integerPart = multipliedRandomBits >> 32;
/*     */     
/*  93 */     return (int)integerPart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public long nextLong() { return this.randomNumberGenerator.nextLong(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean nextBoolean() { return ((this.randomNumberGenerator.nextLong() & 0x1L) != 0L); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public float nextFloat() { return (float)nextBits(24) * 5.9604645E-8F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public double nextDouble() { return nextBits(53) * 1.1102230246251565E-16D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public double nextGaussian() { return this.gaussianSource.nextGaussian(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeCount(int rounds) {
/* 123 */     for (int i = 0; i < rounds; i++) {
/* 124 */       this.randomNumberGenerator.nextLong();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 129 */   private long nextBits(int bits) { return this.randomNumberGenerator.nextLong() >>> 64 - bits; }
/*     */   
/*     */   public static class XoroshiroPositionalRandomFactory
/*     */     implements PositionalRandomFactory {
/*     */     private final long seedLo;
/*     */     private final long seedHi;
/*     */     
/*     */     public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
/* 137 */       this.seedLo = seedLo;
/* 138 */       this.seedHi = seedHi;
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource at(int x, int y, int z) {
/* 143 */       long positionalSeed = Mth.getSeed(x, y, z);
/* 144 */       long randomSeed = positionalSeed ^ this.seedLo;
/* 145 */       return new XoroshiroRandomSource(randomSeed, this.seedHi);
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomSource fromHashOf(String name) {
/* 150 */       RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
/* 151 */       return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 156 */     public RandomSource fromSeed(long seed) { return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/* 162 */     public void parityConfigString(StringBuilder sb) { sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */