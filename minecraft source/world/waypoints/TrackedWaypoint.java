/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.function.TriFunction;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class TrackedWaypoint implements Waypoint {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  24 */   public static final StreamCodec<ByteBuf, TrackedWaypoint> STREAM_CODEC = StreamCodec.ofMember(TrackedWaypoint::write, TrackedWaypoint::read);
/*     */   
/*     */   protected final Either<UUID, String> identifier;
/*     */   private final Waypoint.Icon icon;
/*     */   private final Type type;
/*     */   
/*     */   private TrackedWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, Type type) {
/*  31 */     this.identifier = identifier;
/*  32 */     this.icon = icon;
/*  33 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public Either<UUID, String> id() { return this.identifier; }
/*     */ 
/*     */   
/*     */   public abstract void update(TrackedWaypoint paramTrackedWaypoint);
/*     */   
/*     */   public void write(ByteBuf buf) {
/*  43 */     FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
/*  44 */     byteBuf.writeEither(this.identifier, UUIDUtil.STREAM_CODEC, FriendlyByteBuf::writeUtf);
/*  45 */     Waypoint.Icon.STREAM_CODEC.encode(byteBuf, this.icon);
/*  46 */     byteBuf.writeEnum(this.type);
/*  47 */     writeContents(buf);
/*     */   }
/*     */   
/*     */   public abstract void writeContents(ByteBuf paramByteBuf);
/*     */   
/*     */   private static TrackedWaypoint read(ByteBuf buf) {
/*  53 */     FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
/*     */     
/*  55 */     Either<UUID, String> identifier = byteBuf.readEither(UUIDUtil.STREAM_CODEC, FriendlyByteBuf::readUtf);
/*  56 */     Waypoint.Icon icon = (Waypoint.Icon)Waypoint.Icon.STREAM_CODEC.decode(byteBuf);
/*  57 */     Type type = (Type)byteBuf.readEnum(Type.class);
/*     */     
/*  59 */     return (TrackedWaypoint)type.constructor.apply(identifier, icon, byteBuf);
/*     */   }
/*     */ 
/*     */   
/*  63 */   public static TrackedWaypoint setPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) { return new Vec3iWaypoint(identifier, icon, position); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static TrackedWaypoint setChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) { return new ChunkWaypoint(identifier, icon, chunk); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static TrackedWaypoint setAzimuth(UUID identifier, Waypoint.Icon icon, float angle) { return new AzimuthWaypoint(identifier, icon, angle); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static TrackedWaypoint empty(UUID identifier) { return new EmptyWaypoint(identifier); }
/*     */ 
/*     */   
/*     */   public abstract double yawAngleToCamera(Level paramLevel, Camera paramCamera, PartialTickSupplier paramPartialTickSupplier);
/*     */ 
/*     */   
/*     */   public abstract PitchDirection pitchDirectionToCamera(Level paramLevel, Projector paramProjector, PartialTickSupplier paramPartialTickSupplier);
/*     */   
/*     */   public abstract double distanceSquared(Entity paramEntity);
/*     */   
/*  85 */   public Waypoint.Icon icon() { return this.icon; }
/*     */   
/*     */   public enum PitchDirection
/*     */   {
/*  89 */     NONE,
/*  90 */     UP,
/*  91 */     DOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum Type
/*     */   {
/* 105 */     EMPTY(EmptyWaypoint::new),
/* 106 */     VEC3I(Vec3iWaypoint::new),
/* 107 */     CHUNK(ChunkWaypoint::new),
/* 108 */     AZIMUTH(AzimuthWaypoint::new);
/*     */ 
/*     */     
/*     */     private final TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor;
/*     */ 
/*     */     
/* 114 */     Type(TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor) { this.constructor = constructor; }
/*     */   }
/*     */   
/*     */   private static class EmptyWaypoint
/*     */     extends TrackedWaypoint
/*     */   {
/* 120 */     private EmptyWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) { super(identifier, icon, TrackedWaypoint.Type.EMPTY); }
/*     */ 
/*     */ 
/*     */     
/* 124 */     private EmptyWaypoint(UUID identifier) { super(Either.left(identifier), Waypoint.Icon.NULL, TrackedWaypoint.Type.EMPTY); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(TrackedWaypoint other) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeContents(ByteBuf buf) {}
/*     */ 
/*     */     
/* 135 */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) { return NaND; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) { return TrackedWaypoint.PitchDirection.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     public double distanceSquared(Entity fromEntity) { return Double.POSITIVE_INFINITY; }
/*     */   }
/*     */   
/*     */   private static class Vec3iWaypoint
/*     */     extends TrackedWaypoint {
/*     */     private Vec3i vector;
/*     */     
/*     */     public Vec3iWaypoint(UUID identifier, Waypoint.Icon icon, Vec3i vector) {
/* 153 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.VEC3I);
/* 154 */       this.vector = vector;
/*     */     }
/*     */     
/*     */     public Vec3iWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 158 */       super(identifier, icon, TrackedWaypoint.Type.VEC3I);
/* 159 */       this
/*     */ 
/*     */         
/* 162 */         .vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 168 */       if (other instanceof Vec3iWaypoint) { Vec3iWaypoint vec3iWaypoint = (Vec3iWaypoint)other;
/* 169 */         this.vector = vec3iWaypoint.vector; }
/*     */       else
/* 171 */       { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass()); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeContents(ByteBuf buf) {
/* 177 */       VarInt.write(buf, this.vector.getX());
/* 178 */       VarInt.write(buf, this.vector.getY());
/* 179 */       VarInt.write(buf, this.vector.getZ());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Vec3 position(Level level, PartialTickSupplier partialTick) {
/* 185 */       Objects.requireNonNull(level); return (Vec3)this.identifier.left().map(level::getEntity).map(e -> {
/* 186 */             if (e.blockPosition().distManhattan(this.vector) > 3) {
/* 187 */               return null;
/*     */             }
/* 189 */             return e.getEyePosition(partialTick.apply(e));
/*     */           
/* 191 */           }).orElseGet(() -> Vec3.atCenterOf(this.vector));
/*     */     }
/*     */ 
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 196 */       Vec3 direction = camera.position().subtract(position(level, partialTickSupplier)).rotateClockwise90();
/* 197 */       float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 198 */       return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */     }
/*     */ 
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 203 */       Vec3 pointOnScreen = projector.projectPointToScreen(position(level, partialTickSupplier));
/* 204 */       boolean isBehindCamera = (pointOnScreen.z > 1.0D);
/*     */       
/* 206 */       double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
/* 207 */       if (yInFrontOfCamera < -1.0D) {
/* 208 */         return TrackedWaypoint.PitchDirection.DOWN;
/*     */       }
/* 210 */       if (yInFrontOfCamera > 1.0D) {
/* 211 */         return TrackedWaypoint.PitchDirection.UP;
/*     */       }
/* 213 */       if (isBehindCamera) {
/* 214 */         if (pointOnScreen.y > 0.0D) {
/* 215 */           return TrackedWaypoint.PitchDirection.UP;
/*     */         }
/* 217 */         if (pointOnScreen.y < 0.0D) {
/* 218 */           return TrackedWaypoint.PitchDirection.DOWN;
/*     */         }
/*     */       } 
/* 221 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 226 */     public double distanceSquared(Entity fromEntity) { return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector)); }
/*     */   }
/*     */   
/*     */   private static class ChunkWaypoint
/*     */     extends TrackedWaypoint {
/*     */     private ChunkPos chunkPos;
/*     */     
/*     */     public ChunkWaypoint(UUID identifier, Waypoint.Icon icon, ChunkPos chunkPos) {
/* 234 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.CHUNK);
/* 235 */       this.chunkPos = chunkPos;
/*     */     }
/*     */     
/*     */     public ChunkWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 239 */       super(identifier, icon, TrackedWaypoint.Type.CHUNK);
/* 240 */       this
/*     */         
/* 242 */         .chunkPos = new ChunkPos(byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 248 */       if (other instanceof ChunkWaypoint) { ChunkWaypoint chunkWaypoint = (ChunkWaypoint)other;
/* 249 */         this.chunkPos = chunkWaypoint.chunkPos; }
/*     */       else
/* 251 */       { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass()); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeContents(ByteBuf buf) {
/* 257 */       VarInt.write(buf, this.chunkPos.x);
/* 258 */       VarInt.write(buf, this.chunkPos.z);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 263 */     private Vec3 position(double positionY) { return Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition((int)positionY)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 268 */       Vec3 cameraPosition = camera.position();
/* 269 */       Vec3 direction = cameraPosition.subtract(position(cameraPosition.y())).rotateClockwise90();
/* 270 */       float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 271 */       return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */     }
/*     */ 
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 276 */       double onScreenHorizon = projector.projectHorizonToScreen();
/* 277 */       if (onScreenHorizon < -1.0D) {
/* 278 */         return TrackedWaypoint.PitchDirection.DOWN;
/*     */       }
/* 280 */       if (onScreenHorizon > 1.0D) {
/* 281 */         return TrackedWaypoint.PitchDirection.UP;
/*     */       }
/* 283 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 288 */     public double distanceSquared(Entity fromEntity) { return fromEntity.distanceToSqr(Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY()))); }
/*     */   }
/*     */   
/*     */   private static class AzimuthWaypoint
/*     */     extends TrackedWaypoint {
/*     */     private float angle;
/*     */     
/*     */     public AzimuthWaypoint(UUID identifier, Waypoint.Icon icon, float angle) {
/* 296 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.AZIMUTH);
/* 297 */       this.angle = angle;
/*     */     }
/*     */     
/*     */     public AzimuthWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 301 */       super(identifier, icon, TrackedWaypoint.Type.AZIMUTH);
/* 302 */       this.angle = byteBuf.readFloat();
/*     */     }
/*     */ 
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 307 */       if (other instanceof AzimuthWaypoint) { AzimuthWaypoint azimuthWaypoint = (AzimuthWaypoint)other;
/* 308 */         this.angle = azimuthWaypoint.angle; }
/*     */       else
/* 310 */       { TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass()); }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 316 */     public void writeContents(ByteBuf buf) { buf.writeFloat(this.angle); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) { return Mth.degreesDifference(camera.yaw(), this.angle * 57.295776F); }
/*     */ 
/*     */ 
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 326 */       double horizon = projector.projectHorizonToScreen();
/* 327 */       if (horizon < -1.0D) {
/* 328 */         return TrackedWaypoint.PitchDirection.DOWN;
/*     */       }
/* 330 */       if (horizon > 1.0D) {
/* 331 */         return TrackedWaypoint.PitchDirection.UP;
/*     */       }
/* 333 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 338 */     public double distanceSquared(Entity fromEntity) { return Double.POSITIVE_INFINITY; }
/*     */   }
/*     */   
/*     */   public static interface Camera {
/*     */     float yaw();
/*     */     
/*     */     Vec3 position();
/*     */   }
/*     */   
/*     */   public static interface Projector {
/*     */     Vec3 projectPointToScreen(Vec3 param1Vec3);
/*     */     
/*     */     double projectHorizonToScreen();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\TrackedWaypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */