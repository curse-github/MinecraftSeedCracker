/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ThreadSafeLegacyRandomSource
/*    */   implements BitRandomSource
/*    */ {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/*    */   private final AtomicLong seed;
/*    */   private final MarsagliaPolarGaussian gaussianSource;
/*    */   
/*    */   public ThreadSafeLegacyRandomSource(long seed) {
/* 19 */     this.seed = new AtomicLong();
/* 20 */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*    */ 
/*    */     
/* 23 */     setSeed(seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public RandomSource fork() { return new ThreadSafeLegacyRandomSource(nextLong()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public PositionalRandomFactory forkPositional() { return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void setSeed(long seed) { this.seed.set((seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/*    */     long nextSeed;
/*    */     long oldSeed;
/*    */     do {
/* 46 */       oldSeed = this.seed.get();
/* 47 */       nextSeed = oldSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 48 */     } while (!this.seed.compareAndSet(oldSeed, nextSeed));
/* 49 */     return (int)(nextSeed >>> 48 - bits);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public double nextGaussian() { return this.gaussianSource.nextGaussian(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\ThreadSafeLegacyRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */