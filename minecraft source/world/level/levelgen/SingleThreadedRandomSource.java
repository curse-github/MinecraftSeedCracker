/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SingleThreadedRandomSource
/*    */   implements BitRandomSource {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   private static final long INCREMENT = 11L;
/*    */   private long seed;
/*    */   private final MarsagliaPolarGaussian gaussianSource;
/*    */   
/*    */   public SingleThreadedRandomSource(long seed) {
/* 15 */     this.gaussianSource = new MarsagliaPolarGaussian(this);
/*    */ 
/*    */     
/* 18 */     setSeed(seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public RandomSource fork() { return new SingleThreadedRandomSource(nextLong()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PositionalRandomFactory forkPositional() { return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSeed(long seed) {
/* 33 */     this.seed = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
/* 34 */     this.gaussianSource.reset();
/*    */   }
/*    */ 
/*    */   
/*    */   public int next(int bits) {
/* 39 */     long newSeed = this.seed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 40 */     this.seed = newSeed;
/* 41 */     return (int)(newSeed >> 48 - bits);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public double nextGaussian() { return this.gaussianSource.nextGaussian(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SingleThreadedRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */