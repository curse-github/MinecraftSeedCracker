/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ public class EntityChunkConnection
/*     */   implements WaypointTransmitter.ChunkConnection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private ChunkPos lastPosition;
/*     */   
/*     */   public EntityChunkConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 120 */     this.source = source;
/* 121 */     this.icon = icon;
/* 122 */     this.receiver = receiver;
/* 123 */     this.lastPosition = source.chunkPosition();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public int distanceChessboard() { return this.lastPosition.getChessboardDistance(this.source.chunkPosition()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public void connect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public void disconnect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 143 */     ChunkPos currentPosition = this.source.chunkPosition();
/* 144 */     if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
/* 145 */       this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition));
/* 146 */       this.lastPosition = currentPosition;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBroken() {
/* 152 */     if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)) {
/* 153 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 157 */     return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityChunkConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */