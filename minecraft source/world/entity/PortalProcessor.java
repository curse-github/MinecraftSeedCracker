/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Portal;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public class PortalProcessor
/*    */ {
/*    */   private final Portal portal;
/*    */   private BlockPos entryPosition;
/*    */   private int portalTime;
/*    */   private boolean insidePortalThisTick;
/*    */   
/*    */   public PortalProcessor(Portal portal, BlockPos portalEntryPosition) {
/* 16 */     this.portal = portal;
/* 17 */     this.entryPosition = portalEntryPosition;
/* 18 */     this.insidePortalThisTick = true;
/*    */   }
/*    */   
/*    */   public boolean processPortalTeleportation(ServerLevel serverLevel, Entity entity, boolean allowedToTeleport) {
/* 22 */     if (this.insidePortalThisTick) {
/* 23 */       this.insidePortalThisTick = false;
/* 24 */       return (allowedToTeleport && this.portalTime++ >= this.portal.getPortalTransitionTime(serverLevel, entity));
/*    */     } 
/* 26 */     decayTick();
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 31 */   public TeleportTransition getPortalDestination(ServerLevel serverLevel, Entity entity) { return this.portal.getPortalDestination(serverLevel, entity, this.entryPosition); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Portal.Transition getPortalLocalTransition() { return this.portal.getLocalTransition(); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   private void decayTick() { this.portalTime = Math.max(this.portalTime - 4, 0); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public boolean hasExpired() { return (this.portalTime <= 0); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public BlockPos getEntryPosition() { return this.entryPosition; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void updateEntryPosition(BlockPos entryPosition) { this.entryPosition = entryPosition; }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int getPortalTime() { return this.portalTime; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public boolean isInsidePortalThisTick() { return this.insidePortalThisTick; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public void setAsInsidePortalThisTick(boolean insidePortal) { this.insidePortalThisTick = insidePortal; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean isSamePortal(Portal portal) { return (this.portal == portal); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\PortalProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */