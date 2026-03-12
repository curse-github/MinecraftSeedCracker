/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.ThreadingDetector;
/*    */ 
/*    */ public class LegacyRandomSource
/*    */   implements BitRandomSource {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/*    */   private final AtomicLong seed;
/*    */   private final MarsagliaPolarGaussian gaussianSource;
/*    */   
/*    */   public LegacyRandomSource(long seed) {
/* 19 */     this.seed = new AtomicLong();
/* 20 */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*    */ 
/*    */     
/* 23 */     setSeed(seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public RandomSource fork() { return new LegacyRandomSource(nextLong()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public PositionalRandomFactory forkPositional() { return new LegacyPositionalRandomFactory(nextLong()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSeed(long seed) {
/* 38 */     if (!this.seed.compareAndSet(this.seed.get(), (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL)) {
/* 39 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
/*    */     }
/* 41 */     this.gaussianSource.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/* 46 */     long oldSeed = this.seed.get();
/* 47 */     long newSeed = oldSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 48 */     if (!this.seed.compareAndSet(oldSeed, newSeed)) {
/* 49 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
/*    */     }
/*    */     
/* 52 */     return (int)(newSeed >> 48 - bits);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public double nextGaussian() { return this.gaussianSource.nextGaussian(); }
/*    */   
/*    */   public static class LegacyPositionalRandomFactory
/*    */     implements PositionalRandomFactory
/*    */   {
/*    */     private final long seed;
/*    */     
/* 64 */     public LegacyPositionalRandomFactory(long seed) { this.seed = seed; }
/*    */ 
/*    */ 
/*    */     
/*    */     public RandomSource at(int x, int y, int z) {
/* 69 */       long positionalSeed = Mth.getSeed(x, y, z);
/* 70 */       long randomSeed = positionalSeed ^ this.seed;
/* 71 */       return new LegacyRandomSource(randomSeed);
/*    */     }
/*    */ 
/*    */     
/*    */     public RandomSource fromHashOf(String name) {
/* 76 */       int positionalSeed = name.hashCode();
/* 77 */       return new LegacyRandomSource(positionalSeed ^ this.seed);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 82 */     public RandomSource fromSeed(long seed) { return new LegacyRandomSource(seed); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     @VisibleForTesting
/* 88 */     public void parityConfigString(StringBuilder sb) { sb.append("LegacyPositionalRandomFactory{").append(this.seed).append("}"); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */