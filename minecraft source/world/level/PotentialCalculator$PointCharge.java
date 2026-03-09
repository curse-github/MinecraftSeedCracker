/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PointCharge
/*    */ {
/*    */   private final BlockPos pos;
/*    */   private final double charge;
/*    */   
/*    */   public PointCharge(BlockPos pos, double charge) {
/* 15 */     this.pos = pos;
/* 16 */     this.charge = charge;
/*    */   }
/*    */   
/*    */   public double getPotentialChange(BlockPos pos) {
/* 20 */     double distSqr = this.pos.distSqr(pos);
/* 21 */     if (distSqr == 0.0D)
/*    */     {
/* 23 */       return Double.POSITIVE_INFINITY;
/*    */     }
/* 25 */     return this.charge / Math.sqrt(distSqr);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\PotentialCalculator$PointCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */