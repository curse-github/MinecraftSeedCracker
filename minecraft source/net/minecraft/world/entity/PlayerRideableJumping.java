/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public interface PlayerRideableJumping
/*    */   extends PlayerRideable {
/*    */   void onPlayerJump(int paramInt);
/*    */   
/*    */   boolean canJump();
/*    */   
/*    */   void handleStartJump(int paramInt);
/*    */   
/*    */   void handleStopJump();
/*    */   
/* 13 */   default int getJumpCooldown() { return 0; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   default float getPlayerJumpPendingScale(int jumpAmount) { return (jumpAmount >= 90) ? 1.0F : (0.4F + 0.4F * jumpAmount / 90.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\PlayerRideableJumping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */