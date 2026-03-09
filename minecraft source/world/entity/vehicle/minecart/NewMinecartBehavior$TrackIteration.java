/*    */ package net.minecraft.world.entity.vehicle.minecart;
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
/*    */ class TrackIteration
/*    */ {
/* 56 */   double movementLeft = 0.0D;
/*    */   
/*    */   boolean firstIteration = true;
/*    */   boolean hasGainedSlopeSpeed = false;
/*    */   boolean hasHalted = false;
/*    */   boolean hasBoosted = false;
/*    */   
/* 63 */   public boolean shouldIterate() { return (this.firstIteration || this.movementLeft > 9.999999747378752E-6D); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$TrackIteration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */