/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class PotentialCalculator
/*    */ {
/*    */   private static class PointCharge
/*    */   {
/*    */     private final BlockPos pos;
/*    */     private final double charge;
/*    */     
/*    */     public PointCharge(BlockPos pos, double charge) {
/* 15 */       this.pos = pos;
/* 16 */       this.charge = charge;
/*    */     }
/*    */     
/*    */     public double getPotentialChange(BlockPos pos) {
/* 20 */       double distSqr = this.pos.distSqr(pos);
/* 21 */       if (distSqr == 0.0D)
/*    */       {
/* 23 */         return Double.POSITIVE_INFINITY;
/*    */       }
/* 25 */       return this.charge / Math.sqrt(distSqr);
/*    */     }
/*    */   }
/*    */   
/* 29 */   private final List<PointCharge> charges = Lists.newArrayList();
/*    */   
/*    */   public void addCharge(BlockPos pos, double charge) {
/* 32 */     if (charge != 0.0D) {
/* 33 */       this.charges.add(new PointCharge(pos, charge));
/*    */     }
/*    */   }
/*    */   
/*    */   public double getPotentialEnergyChange(BlockPos pos, double charge) {
/* 38 */     if (charge == 0.0D) {
/* 39 */       return 0.0D;
/*    */     }
/* 41 */     double potentialChange = 0.0D;
/* 42 */     for (PointCharge point : this.charges) {
/* 43 */       potentialChange += point.getPotentialChange(pos);
/*    */     }
/* 45 */     return potentialChange * charge;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\PotentialCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */