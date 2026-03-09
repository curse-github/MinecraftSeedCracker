/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ChunkWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private ChunkPos chunkPos;
/*     */   
/*     */   public ChunkWaypoint(UUID identifier, Waypoint.Icon icon, ChunkPos chunkPos) {
/* 234 */     super(Either.left(identifier), icon, TrackedWaypoint.Type.CHUNK);
/* 235 */     this.chunkPos = chunkPos;
/*     */   }
/*     */   
/*     */   public ChunkWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 239 */     super(identifier, icon, TrackedWaypoint.Type.CHUNK);
/* 240 */     this
/*     */       
/* 242 */       .chunkPos = new ChunkPos(byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint other) {
/* 248 */     if (other instanceof ChunkWaypoint) { ChunkWaypoint chunkWaypoint = (ChunkWaypoint)other;
/* 249 */       this.chunkPos = chunkWaypoint.chunkPos; }
/*     */     else
/* 251 */     { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf buf) {
/* 257 */     VarInt.write(buf, this.chunkPos.x);
/* 258 */     VarInt.write(buf, this.chunkPos.z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 263 */   private Vec3 position(double positionY) { return Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition((int)positionY)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 268 */     Vec3 cameraPosition = camera.position();
/* 269 */     Vec3 direction = cameraPosition.subtract(position(cameraPosition.y())).rotateClockwise90();
/* 270 */     float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 271 */     return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 276 */     double onScreenHorizon = projector.projectHorizonToScreen();
/* 277 */     if (onScreenHorizon < -1.0D) {
/* 278 */       return TrackedWaypoint.PitchDirection.DOWN;
/*     */     }
/* 280 */     if (onScreenHorizon > 1.0D) {
/* 281 */       return TrackedWaypoint.PitchDirection.UP;
/*     */     }
/* 283 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 288 */   public double distanceSquared(Entity fromEntity) { return fromEntity.distanceToSqr(Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY()))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\TrackedWaypoint$ChunkWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */