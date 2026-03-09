/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements Aquifer
/*    */ {
/*    */   public BlockState computeSubstance(DensityFunction.FunctionContext context, double density) {
/* 64 */     if (density > 0.0D) {
/* 65 */       return null;
/*    */     }
/* 67 */     return fluidRule.computeFluid(context.blockX(), context.blockY(), context.blockZ()).at(context.blockY());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public boolean shouldScheduleFluidUpdate() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Aquifer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */