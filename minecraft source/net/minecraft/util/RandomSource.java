/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
/*    */ import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;
/*    */ 
/*    */ public interface RandomSource
/*    */ {
/* 12 */   static RandomSource create() { return create(RandomSupport.generateUniqueSeed()); }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static final double GAUSSIAN_SPREAD_FACTOR = 2.297D;
/*    */ 
/*    */   
/*    */   @Deprecated
/* 20 */   static RandomSource createThreadSafe() { return new ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed()); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   static RandomSource create(long seed) { return new LegacyRandomSource(seed); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   static RandomSource createNewThreadLocalInstance() { return new SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong()); }
/*    */ 
/*    */ 
/*    */   
/*    */   RandomSource fork();
/*    */ 
/*    */ 
/*    */   
/*    */   PositionalRandomFactory forkPositional();
/*    */ 
/*    */   
/*    */   void setSeed(long paramLong);
/*    */ 
/*    */   
/*    */   int nextInt();
/*    */ 
/*    */   
/*    */   int nextInt(int paramInt);
/*    */ 
/*    */   
/* 52 */   default int nextIntBetweenInclusive(int min, int maxInclusive) { return nextInt(maxInclusive - min + 1) + min; }
/*    */ 
/*    */ 
/*    */   
/*    */   long nextLong();
/*    */ 
/*    */ 
/*    */   
/*    */   boolean nextBoolean();
/*    */ 
/*    */ 
/*    */   
/*    */   float nextFloat();
/*    */ 
/*    */ 
/*    */   
/*    */   double nextDouble();
/*    */ 
/*    */ 
/*    */   
/*    */   double nextGaussian();
/*    */ 
/*    */   
/* 75 */   default double triangle(double mean, double spread) { return mean + spread * (nextDouble() - nextDouble()); }
/*    */ 
/*    */ 
/*    */   
/* 79 */   default float triangle(float mean, float spread) { return mean + spread * (nextFloat() - nextFloat()); }
/*    */ 
/*    */   
/*    */   default void consumeCount(int rounds) {
/* 83 */     for (int i = 0; i < rounds; i++) {
/* 84 */       nextInt();
/*    */     }
/*    */   }
/*    */   
/*    */   default int nextInt(int origin, int bound) {
/* 89 */     if (origin >= bound) {
/* 90 */       throw new IllegalArgumentException("bound - origin is non positive");
/*    */     }
/* 92 */     return origin + nextInt(bound - origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\RandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */