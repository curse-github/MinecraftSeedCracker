/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class EntityAzimuthConnection
/*     */   implements WaypointTransmitter.Connection
/*     */ {
/*     */   private final LivingEntity source;
/*     */   private final Waypoint.Icon icon;
/*     */   private final ServerPlayer receiver;
/*     */   private float lastAngle;
/*     */   
/*     */   public EntityAzimuthConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 168 */     this.source = source;
/* 169 */     this.icon = icon;
/* 170 */     this.receiver = receiver;
/*     */     
/* 172 */     Vec3 direction = receiver.position().subtract(source.position()).rotateClockwise90();
/* 173 */     this.lastAngle = (float)Mth.atan2(direction.z(), direction.x());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 178 */   public boolean isBroken() { return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public void connect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public void disconnect() { this.receiver.connection.send(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 193 */     Vec3 direction = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 194 */     float currentAngle = (float)Mth.atan2(direction.z(), direction.x());
/* 195 */     if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
/* 196 */       this.receiver.connection.send(ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle));
/* 197 */       this.lastAngle = currentAngle;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityAzimuthConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */