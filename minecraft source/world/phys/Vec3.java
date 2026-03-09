/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Vec3 implements Position {
/*  20 */   public static final Codec<Vec3> CODEC = Codec.DOUBLE.listOf().comapFlatMap(input -> 
/*  21 */       Util.fixedSize(input, 3).map(()), pos -> 
/*  22 */       List.of(Double.valueOf(pos.x()), Double.valueOf(pos.y()), Double.valueOf(pos.z())));
/*     */ 
/*     */   
/*  25 */   public static final StreamCodec<ByteBuf, Vec3> STREAM_CODEC = new StreamCodec<ByteBuf, Vec3>()
/*     */     {
/*     */       public Vec3 decode(ByteBuf input) {
/*  28 */         return FriendlyByteBuf.readVec3(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  33 */       public void encode(ByteBuf output, Vec3 value) { FriendlyByteBuf.writeVec3(output, value); }
/*     */     };
/*     */ 
/*     */   
/*  37 */   public static final Vec3 ZERO = new Vec3(0.0D, 0.0D, 0.0D);
/*  38 */   public static final Vec3 X_AXIS = new Vec3(1.0D, 0.0D, 0.0D);
/*  39 */   public static final Vec3 Y_AXIS = new Vec3(0.0D, 1.0D, 0.0D);
/*  40 */   public static final Vec3 Z_AXIS = new Vec3(0.0D, 0.0D, 1.0D);
/*     */   
/*     */   public final double x;
/*     */   public final double y;
/*     */   public final double z;
/*     */   
/*  46 */   public static Vec3 atLowerCornerOf(Vec3i pos) { return new Vec3(pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static Vec3 atLowerCornerWithOffset(Vec3i pos, double x, double y, double z) { return new Vec3(pos.getX() + x, pos.getY() + y, pos.getZ() + z); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static Vec3 atCenterOf(Vec3i pos) { return atLowerCornerWithOffset(pos, 0.5D, 0.5D, 0.5D); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static Vec3 atBottomCenterOf(Vec3i pos) { return atLowerCornerWithOffset(pos, 0.5D, 0.0D, 0.5D); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static Vec3 upFromBottomCenterOf(Vec3i pos, double yOffset) { return atLowerCornerWithOffset(pos, 0.5D, yOffset, 0.5D); }
/*     */ 
/*     */   
/*     */   public Vec3(double x, double y, double z) {
/*  66 */     this.x = x;
/*  67 */     this.y = y;
/*  68 */     this.z = z;
/*     */   }
/*     */ 
/*     */   
/*  72 */   public Vec3(Vector3fc vec) { this(vec.x(), vec.y(), vec.z()); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public Vec3(Vec3i vec) { this(vec.getX(), vec.getY(), vec.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public Vec3 vectorTo(Vec3 vec) { return new Vec3(vec.x - this.x, vec.y - this.y, vec.z - this.z); }
/*     */ 
/*     */   
/*     */   public Vec3 normalize() {
/*  84 */     double dist = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*  85 */     if (dist < 9.999999747378752E-6D) {
/*  86 */       return ZERO;
/*     */     }
/*  88 */     return new Vec3(this.x / dist, this.y / dist, this.z / dist);
/*     */   }
/*     */ 
/*     */   
/*  92 */   public double dot(Vec3 vec) { return this.x * vec.x + this.y * vec.y + this.z * vec.z; }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public Vec3 cross(Vec3 vec) { return new Vec3(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public Vec3 subtract(Vec3 vec) { return subtract(vec.x, vec.y, vec.z); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public Vec3 subtract(double value) { return subtract(value, value, value); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public Vec3 subtract(double x, double y, double z) { return add(-x, -y, -z); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public Vec3 add(double value) { return add(value, value, value); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public Vec3 add(Vec3 vec) { return add(vec.x, vec.y, vec.z); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public Vec3 add(double x, double y, double z) { return new Vec3(this.x + x, this.y + y, this.z + z); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean closerThan(Position pos, double distance) { return (distanceToSqr(pos.x(), pos.y(), pos.z()) < distance * distance); }
/*     */ 
/*     */   
/*     */   public double distanceTo(Vec3 vec) {
/* 128 */     double xd = vec.x - this.x;
/* 129 */     double yd = vec.y - this.y;
/* 130 */     double zd = vec.z - this.z;
/* 131 */     return Math.sqrt(xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 vec) {
/* 135 */     double xd = vec.x - this.x;
/* 136 */     double yd = vec.y - this.y;
/* 137 */     double zd = vec.z - this.z;
/* 138 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(double x, double y, double z) {
/* 142 */     double xd = x - this.x;
/* 143 */     double yd = y - this.y;
/* 144 */     double zd = z - this.z;
/* 145 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public boolean closerThan(Vec3 vec, double distanceXZ, double distanceY) {
/* 149 */     double dx = vec.x() - this.x;
/* 150 */     double dy = vec.y() - this.y;
/* 151 */     double dz = vec.z() - this.z;
/* 152 */     return (Mth.lengthSquared(dx, dz) < Mth.square(distanceXZ) && Math.abs(dy) < distanceY);
/*     */   }
/*     */ 
/*     */   
/* 156 */   public Vec3 scale(double scale) { return multiply(scale, scale, scale); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public Vec3 reverse() { return scale(-1.0D); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public Vec3 multiply(Vec3 scale) { return multiply(scale.x, scale.y, scale.z); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public Vec3 multiply(double xScale, double yScale, double zScale) { return new Vec3(this.x * xScale, this.y * yScale, this.z * zScale); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public Vec3 horizontal() { return new Vec3(this.x, 0.0D, this.z); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public Vec3 offsetRandom(RandomSource random, float offset) { return add(((random.nextFloat() - 0.5F) * offset), ((random.nextFloat() - 0.5F) * offset), ((random.nextFloat() - 0.5F) * offset)); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public Vec3 offsetRandomXZ(RandomSource random, float offset) { return add(((random.nextFloat() - 0.5F) * offset), 0.0D, ((random.nextFloat() - 0.5F) * offset)); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public double length() { return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public double lengthSqr() { return this.x * this.x + this.y * this.y + this.z * this.z; }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public double horizontalDistance() { return Math.sqrt(this.x * this.x + this.z * this.z); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public double horizontalDistanceSqr() { return this.x * this.x + this.z * this.z; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 201 */     if (this == o) {
/* 202 */       return true;
/*     */     }
/* 204 */     if (!(o instanceof Vec3)) {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     Vec3 vec3 = (Vec3)o;
/*     */     
/* 210 */     if (Double.compare(vec3.x, this.x) != 0) {
/* 211 */       return false;
/*     */     }
/* 213 */     if (Double.compare(vec3.y, this.y) != 0) {
/* 214 */       return false;
/*     */     }
/* 216 */     return (Double.compare(vec3.z, this.z) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 223 */     long temp = Double.doubleToLongBits(this.x);
/* 224 */     result = (int)(temp ^ temp >>> 32);
/* 225 */     temp = Double.doubleToLongBits(this.y);
/* 226 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 227 */     temp = Double.doubleToLongBits(this.z);
/* 228 */     return 31 * result + (int)(temp ^ temp >>> 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public String toString() { return "(" + this.x + ", " + this.y + ", " + this.z + ")"; }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public Vec3 lerp(Vec3 vec, double a) { return new Vec3(Mth.lerp(a, this.x, vec.x), Mth.lerp(a, this.y, vec.y), Mth.lerp(a, this.z, vec.z)); }
/*     */ 
/*     */   
/*     */   public Vec3 xRot(float radians) {
/* 242 */     float cos = Mth.cos(radians);
/* 243 */     float sin = Mth.sin(radians);
/*     */     
/* 245 */     double xx = this.x;
/* 246 */     double yy = this.y * cos + this.z * sin;
/* 247 */     double zz = this.z * cos - this.y * sin;
/*     */     
/* 249 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */   
/*     */   public Vec3 yRot(float radians) {
/* 253 */     float cos = Mth.cos(radians);
/* 254 */     float sin = Mth.sin(radians);
/*     */     
/* 256 */     double xx = this.x * cos + this.z * sin;
/* 257 */     double yy = this.y;
/* 258 */     double zz = this.z * cos - this.x * sin;
/*     */     
/* 260 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */   
/*     */   public Vec3 zRot(float radians) {
/* 264 */     float cos = Mth.cos(radians);
/* 265 */     float sin = Mth.sin(radians);
/*     */     
/* 267 */     double xx = this.x * cos + this.y * sin;
/* 268 */     double yy = this.y * cos - this.x * sin;
/* 269 */     double zz = this.z;
/*     */     
/* 271 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */ 
/*     */   
/* 275 */   public Vec3 rotateClockwise90() { return new Vec3(-this.z, this.y, this.x); }
/*     */ 
/*     */ 
/*     */   
/* 279 */   public static Vec3 directionFromRotation(Vec2 rotation) { return directionFromRotation(rotation.x, rotation.y); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vec3 directionFromRotation(float rotX, float rotY) {
/* 284 */     float yCos = Mth.cos((-rotY * 0.017453292F - 3.1415927F));
/* 285 */     float ySin = Mth.sin((-rotY * 0.017453292F - 3.1415927F));
/* 286 */     float xCos = -Mth.cos((-rotX * 0.017453292F));
/* 287 */     float xSin = Mth.sin((-rotX * 0.017453292F));
/*     */     
/* 289 */     return new Vec3((ySin * xCos), xSin, (yCos * xCos));
/*     */   }
/*     */   
/*     */   public Vec2 rotation() {
/* 293 */     float yaw = (float)Math.atan2(-this.x, this.z) * 57.295776F;
/* 294 */     float pitch = (float)Math.asin(-this.y / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z)) * 57.295776F;
/* 295 */     return new Vec2(pitch, yaw);
/*     */   }
/*     */   
/*     */   public Vec3 align(EnumSet<Direction.Axis> axes) {
/* 299 */     double x = axes.contains(Direction.Axis.X) ? Mth.floor(this.x) : this.x;
/* 300 */     double y = axes.contains(Direction.Axis.Y) ? Mth.floor(this.y) : this.y;
/* 301 */     double z = axes.contains(Direction.Axis.Z) ? Mth.floor(this.z) : this.z;
/* 302 */     return new Vec3(x, y, z);
/*     */   }
/*     */ 
/*     */   
/* 306 */   public double get(Direction.Axis axis) { return axis.choose(this.x, this.y, this.z); }
/*     */ 
/*     */   
/*     */   public Vec3 with(Direction.Axis axis, double value) {
/* 310 */     double x = (axis == Direction.Axis.X) ? value : this.x;
/* 311 */     double y = (axis == Direction.Axis.Y) ? value : this.y;
/* 312 */     double z = (axis == Direction.Axis.Z) ? value : this.z;
/* 313 */     return new Vec3(x, y, z);
/*     */   }
/*     */   
/*     */   public Vec3 relative(Direction direction, double distance) {
/* 317 */     Vec3i normal = direction.getUnitVec3i();
/* 318 */     return new Vec3(this.x + distance * normal
/* 319 */         .getX(), this.y + distance * normal
/* 320 */         .getY(), this.z + distance * normal
/* 321 */         .getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public final double x() { return this.x; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public final double y() { return this.y; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public final double z() { return this.z; }
/*     */ 
/*     */ 
/*     */   
/* 341 */   public Vector3f toVector3f() { return new Vector3f((float)this.x, (float)this.y, (float)this.z); }
/*     */ 
/*     */   
/*     */   public Vec3 projectedOn(Vec3 onto) {
/* 345 */     if (onto.lengthSqr() == 0.0D) {
/* 346 */       return onto;
/*     */     }
/* 348 */     return onto.scale(dot(onto)).scale(1.0D / onto.lengthSqr());
/*     */   }
/*     */   
/*     */   public static Vec3 applyLocalCoordinatesToRotation(Vec2 rotation, Vec3 direction) {
/* 352 */     float yCos = Mth.cos(((rotation.y + 90.0F) * 0.017453292F));
/* 353 */     float ySin = Mth.sin(((rotation.y + 90.0F) * 0.017453292F));
/* 354 */     float xCos = Mth.cos((-rotation.x * 0.017453292F));
/* 355 */     float xSin = Mth.sin((-rotation.x * 0.017453292F));
/* 356 */     float xCosUp = Mth.cos(((-rotation.x + 90.0F) * 0.017453292F));
/* 357 */     float xSinUp = Mth.sin(((-rotation.x + 90.0F) * 0.017453292F));
/* 358 */     Vec3 forwards = new Vec3((yCos * xCos), xSin, (ySin * xCos));
/* 359 */     Vec3 up = new Vec3((yCos * xCosUp), xSinUp, (ySin * xCosUp));
/* 360 */     Vec3 left = forwards.cross(up).scale(-1.0D);
/* 361 */     double xa = forwards.x * direction.z + up.x * direction.y + left.x * direction.x;
/* 362 */     double ya = forwards.y * direction.z + up.y * direction.y + left.y * direction.x;
/* 363 */     double za = forwards.z * direction.z + up.z * direction.y + left.z * direction.x;
/* 364 */     return new Vec3(xa, ya, za);
/*     */   }
/*     */ 
/*     */   
/* 368 */   public Vec3 addLocalCoordinates(Vec3 direction) { return applyLocalCoordinatesToRotation(rotation(), direction); }
/*     */ 
/*     */ 
/*     */   
/* 372 */   public boolean isFinite() { return (Double.isFinite(this.x) && Double.isFinite(this.y) && Double.isFinite(this.z)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\Vec3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */