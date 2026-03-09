/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityBlockConnection
/*     */   implements WaypointTransmitter.BlockConnection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private BlockPos lastPosition;
/*     */   
/*     */   public EntityBlockConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/*  68 */     this.source = source;
/*  69 */     this.receiver = receiver;
/*  70 */     this.icon = icon;
/*  71 */     this.lastPosition = source.blockPosition();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public void connect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, this.lastPosition)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public void disconnect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/*  86 */     BlockPos currentPosition = this.source.blockPosition();
/*  87 */     if (currentPosition.distManhattan(this.lastPosition) > 0) {
/*  88 */       this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, currentPosition));
/*  89 */       this.lastPosition = currentPosition;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public int distanceManhattan() { return this.lastPosition.distManhattan(this.source.blockPosition()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public boolean isBroken() { return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityBlockConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */