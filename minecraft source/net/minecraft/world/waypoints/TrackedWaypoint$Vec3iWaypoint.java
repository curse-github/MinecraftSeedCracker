/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
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
/*     */ class Vec3iWaypoint
/*     */   extends TrackedWaypoint
/*     */ {
/*     */   private Vec3i vector;
/*     */   
/*     */   public Vec3iWaypoint(UUID identifier, Waypoint.Icon icon, Vec3i vector) {
/* 153 */     super(Either.left(identifier), icon, TrackedWaypoint.Type.VEC3I);
/* 154 */     this.vector = vector;
/*     */   }
/*     */   
/*     */   public Vec3iWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 158 */     super(identifier, icon, TrackedWaypoint.Type.VEC3I);
/* 159 */     this
/*     */ 
/*     */       
/* 162 */       .vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(TrackedWaypoint other) {
/* 168 */     if (other instanceof Vec3iWaypoint) { Vec3iWaypoint vec3iWaypoint = (Vec3iWaypoint)other;
/* 169 */       this.vector = vec3iWaypoint.vector; }
/*     */     else
/* 171 */     { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass()); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeContents(ByteBuf buf) {
/* 177 */     VarInt.write(buf, this.vector.getX());
/* 178 */     VarInt.write(buf, this.vector.getY());
/* 179 */     VarInt.write(buf, this.vector.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Vec3 position(Level level, PartialTickSupplier partialTick) {
/* 185 */     Objects.requireNonNull(level); return (Vec3)this.identifier.left().map(level::getEntity).map(e -> {
/* 186 */           if (e.blockPosition().distManhattan(this.vector) > 3) {
/* 187 */             return null;
/*     */           }
/* 189 */           return e.getEyePosition(partialTick.apply(e));
/*     */         
/* 191 */         }).orElseGet(() -> Vec3.atCenterOf(this.vector));
/*     */   }
/*     */ 
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 196 */     Vec3 direction = camera.position().subtract(position(level, partialTickSupplier)).rotateClockwise90();
/* 197 */     float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 198 */     return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 203 */     Vec3 pointOnScreen = projector.projectPointToScreen(position(level, partialTickSupplier));
/* 204 */     boolean isBehindCamera = (pointOnScreen.z > 1.0D);
/*     */     
/* 206 */     double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
/* 207 */     if (yInFrontOfCamera < -1.0D) {
/* 208 */       return TrackedWaypoint.PitchDirection.DOWN;
/*     */     }
/* 210 */     if (yInFrontOfCamera > 1.0D) {
/* 211 */       return TrackedWaypoint.PitchDirection.UP;
/*     */     }
/* 213 */     if (isBehindCamera) {
/* 214 */       if (pointOnScreen.y > 0.0D) {
/* 215 */         return TrackedWaypoint.PitchDirection.UP;
/*     */       }
/* 217 */       if (pointOnScreen.y < 0.0D) {
/* 218 */         return TrackedWaypoint.PitchDirection.DOWN;
/*     */       }
/*     */     } 
/* 221 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public double distanceSquared(Entity fromEntity) { return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Vec3iWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */