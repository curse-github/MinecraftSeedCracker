/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GaussianSampler {
/*    */   private static final int GAUSSIAN_SAMPLE_RADIUS = 2;
/*    */   private static final int GAUSSIAN_SAMPLE_BREADTH = 6;
/*  9 */   private static final double[] GAUSSIAN_SAMPLE_KERNEL = { 0.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 0.0D };
/*    */   
/*    */   public static <V> void sample(Vec3 position, Sampler<V> sampler, Accumulator<V> accumulator) {
/* 12 */     position = position.subtract(0.5D, 0.5D, 0.5D);
/*    */     
/* 14 */     int integralX = Mth.floor(position.x());
/* 15 */     int integralY = Mth.floor(position.y());
/* 16 */     int integralZ = Mth.floor(position.z());
/*    */     
/* 18 */     double relativeX = position.x() - integralX;
/* 19 */     double relativeY = position.y() - integralY;
/* 20 */     double relativeZ = position.z() - integralZ;
/*    */     
/* 22 */     for (int z = 0; z < 6; z++) {
/* 23 */       double weightZ = Mth.lerp(relativeZ, GAUSSIAN_SAMPLE_KERNEL[z + 1], GAUSSIAN_SAMPLE_KERNEL[z]);
/* 24 */       int sampleZ = integralZ - 2 + z;
/*    */       
/* 26 */       for (int x = 0; x < 6; x++) {
/* 27 */         double weightX = Mth.lerp(relativeX, GAUSSIAN_SAMPLE_KERNEL[x + 1], GAUSSIAN_SAMPLE_KERNEL[x]);
/* 28 */         int sampleX = integralX - 2 + x;
/*    */         
/* 30 */         for (int y = 0; y < 6; y++) {
/* 31 */           double weightY = Mth.lerp(relativeY, GAUSSIAN_SAMPLE_KERNEL[y + 1], GAUSSIAN_SAMPLE_KERNEL[y]);
/* 32 */           int sampleY = integralY - 2 + y;
/*    */           
/* 34 */           double sampleWeight = weightX * weightY * weightZ;
/* 35 */           V value = (V)sampler.get(sampleX, sampleY, sampleZ);
/* 36 */           accumulator.accumulate(sampleWeight, value);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Sampler<V> {
/*    */     V get(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Accumulator<V> {
/*    */     void accumulate(double param1Double, V param1V);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\GaussianSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */