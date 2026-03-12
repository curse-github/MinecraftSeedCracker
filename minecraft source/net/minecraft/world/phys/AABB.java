/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AABB
/*     */ {
/*     */   private static final double EPSILON = 1.0E-7D;
/*     */   public final double minX;
/*     */   public final double minY;
/*     */   public final double minZ;
/*     */   public final double maxX;
/*     */   public final double maxY;
/*     */   public final double maxZ;
/*     */   
/*     */   public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  30 */     this.minX = Math.min(minX, maxX);
/*  31 */     this.minY = Math.min(minY, maxY);
/*  32 */     this.minZ = Math.min(minZ, maxZ);
/*  33 */     this.maxX = Math.max(minX, maxX);
/*  34 */     this.maxY = Math.max(minY, maxY);
/*  35 */     this.maxZ = Math.max(minZ, maxZ);
/*     */   }
/*     */ 
/*     */   
/*  39 */   public AABB(BlockPos pos) { this(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public AABB(Vec3 begin, Vec3 end) { this(begin.x, begin.y, begin.z, end.x, end.y, end.z); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static AABB of(BoundingBox box) { return new AABB(box.minX(), box.minY(), box.minZ(), (box.maxX() + 1), (box.maxY() + 1), (box.maxZ() + 1)); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static AABB unitCubeFromLowerCorner(Vec3 pos) { return new AABB(pos.x, pos.y, pos.z, pos.x + 1.0D, pos.y + 1.0D, pos.z + 1.0D); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static AABB encapsulatingFullBlocks(BlockPos pos0, BlockPos pos1) { return new AABB(Math.min(pos0.getX(), pos1.getX()), Math.min(pos0.getY(), pos1.getY()), Math.min(pos0.getZ(), pos1.getZ()), (Math.max(pos0.getX(), pos1.getX()) + 1), (Math.max(pos0.getY(), pos1.getY()) + 1), (Math.max(pos0.getZ(), pos1.getZ()) + 1)); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public AABB setMinX(double minX) { return new AABB(minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public AABB setMinY(double minY) { return new AABB(this.minX, minY, this.minZ, this.maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public AABB setMinZ(double minZ) { return new AABB(this.minX, this.minY, minZ, this.maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public AABB setMaxX(double maxX) { return new AABB(this.minX, this.minY, this.minZ, maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public AABB setMaxY(double maxY) { return new AABB(this.minX, this.minY, this.minZ, this.maxX, maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public AABB setMaxZ(double maxZ) { return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, maxZ); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public double min(Direction.Axis axis) { return axis.choose(this.minX, this.minY, this.minZ); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public double max(Direction.Axis axis) { return axis.choose(this.maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  92 */     if (this == o) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!(o instanceof AABB)) {
/*  96 */       return false;
/*     */     }
/*     */     
/*  99 */     AABB aabb = (AABB)o;
/*     */     
/* 101 */     if (Double.compare(aabb.minX, this.minX) != 0) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (Double.compare(aabb.minY, this.minY) != 0) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (Double.compare(aabb.minZ, this.minZ) != 0) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (Double.compare(aabb.maxX, this.maxX) != 0) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (Double.compare(aabb.maxY, this.maxY) != 0) {
/* 114 */       return false;
/*     */     }
/* 116 */     return (Double.compare(aabb.maxZ, this.maxZ) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     long temp = Double.doubleToLongBits(this.minX);
/* 122 */     result = (int)(temp ^ temp >>> 32);
/* 123 */     temp = Double.doubleToLongBits(this.minY);
/* 124 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 125 */     temp = Double.doubleToLongBits(this.minZ);
/* 126 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 127 */     temp = Double.doubleToLongBits(this.maxX);
/* 128 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 129 */     temp = Double.doubleToLongBits(this.maxY);
/* 130 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/* 131 */     temp = Double.doubleToLongBits(this.maxZ);
/* 132 */     return 31 * result + (int)(temp ^ temp >>> 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB contract(double xa, double ya, double za) {
/* 143 */     double minX = this.minX;
/* 144 */     double minY = this.minY;
/* 145 */     double minZ = this.minZ;
/* 146 */     double maxX = this.maxX;
/* 147 */     double maxY = this.maxY;
/* 148 */     double maxZ = this.maxZ;
/*     */     
/* 150 */     if (xa < 0.0D) {
/* 151 */       minX -= xa;
/* 152 */     } else if (xa > 0.0D) {
/* 153 */       maxX -= xa;
/*     */     } 
/*     */     
/* 156 */     if (ya < 0.0D) {
/* 157 */       minY -= ya;
/* 158 */     } else if (ya > 0.0D) {
/* 159 */       maxY -= ya;
/*     */     } 
/*     */     
/* 162 */     if (za < 0.0D) {
/* 163 */       minZ -= za;
/* 164 */     } else if (za > 0.0D) {
/* 165 */       maxZ -= za;
/*     */     } 
/*     */     
/* 168 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */ 
/*     */   
/* 172 */   public AABB expandTowards(Vec3 delta) { return expandTowards(delta.x, delta.y, delta.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB expandTowards(double xa, double ya, double za) {
/* 182 */     double minX = this.minX;
/* 183 */     double minY = this.minY;
/* 184 */     double minZ = this.minZ;
/* 185 */     double maxX = this.maxX;
/* 186 */     double maxY = this.maxY;
/* 187 */     double maxZ = this.maxZ;
/*     */     
/* 189 */     if (xa < 0.0D) {
/* 190 */       minX += xa;
/* 191 */     } else if (xa > 0.0D) {
/* 192 */       maxX += xa;
/*     */     } 
/*     */     
/* 195 */     if (ya < 0.0D) {
/* 196 */       minY += ya;
/* 197 */     } else if (ya > 0.0D) {
/* 198 */       maxY += ya;
/*     */     } 
/*     */     
/* 201 */     if (za < 0.0D) {
/* 202 */       minZ += za;
/* 203 */     } else if (za > 0.0D) {
/* 204 */       maxZ += za;
/*     */     } 
/*     */     
/* 207 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB inflate(double xAdd, double yAdd, double zAdd) {
/* 217 */     double minX = this.minX - xAdd;
/* 218 */     double minY = this.minY - yAdd;
/* 219 */     double minZ = this.minZ - zAdd;
/* 220 */     double maxX = this.maxX + xAdd;
/* 221 */     double maxY = this.maxY + yAdd;
/* 222 */     double maxZ = this.maxZ + zAdd;
/*     */     
/* 224 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */ 
/*     */   
/* 228 */   public AABB inflate(double amountToAddInAllDirections) { return inflate(amountToAddInAllDirections, amountToAddInAllDirections, amountToAddInAllDirections); }
/*     */ 
/*     */   
/*     */   public AABB intersect(AABB other) {
/* 232 */     double minX = Math.max(this.minX, other.minX);
/* 233 */     double minY = Math.max(this.minY, other.minY);
/* 234 */     double minZ = Math.max(this.minZ, other.minZ);
/* 235 */     double maxX = Math.min(this.maxX, other.maxX);
/* 236 */     double maxY = Math.min(this.maxY, other.maxY);
/* 237 */     double maxZ = Math.min(this.maxZ, other.maxZ);
/*     */     
/* 239 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB minmax(AABB other) {
/* 243 */     double minX = Math.min(this.minX, other.minX);
/* 244 */     double minY = Math.min(this.minY, other.minY);
/* 245 */     double minZ = Math.min(this.minZ, other.minZ);
/* 246 */     double maxX = Math.max(this.maxX, other.maxX);
/* 247 */     double maxY = Math.max(this.maxY, other.maxY);
/* 248 */     double maxZ = Math.max(this.maxZ, other.maxZ);
/*     */     
/* 250 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */ 
/*     */   
/* 254 */   public AABB move(double xa, double ya, double za) { return new AABB(this.minX + xa, this.minY + ya, this.minZ + za, this.maxX + xa, this.maxY + ya, this.maxZ + za); }
/*     */ 
/*     */ 
/*     */   
/* 258 */   public AABB move(BlockPos pos) { return new AABB(this.minX + pos.getX(), this.minY + pos.getY(), this.minZ + pos.getZ(), this.maxX + pos.getX(), this.maxY + pos.getY(), this.maxZ + pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 262 */   public AABB move(Vec3 pos) { return move(pos.x, pos.y, pos.z); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   public AABB move(Vector3f pos) { return move(pos.x, pos.y, pos.z); }
/*     */ 
/*     */ 
/*     */   
/* 270 */   public boolean intersects(AABB aabb) { return intersects(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 275 */   public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) { return (this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public boolean intersects(Vec3 min, Vec3 max) { return intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z)); }
/*     */ 
/*     */ 
/*     */   
/* 288 */   public boolean intersects(BlockPos pos) { return intersects(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)); }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public boolean contains(Vec3 vec) { return contains(vec.x, vec.y, vec.z); }
/*     */ 
/*     */ 
/*     */   
/* 296 */   public boolean contains(double x, double y, double z) { return (x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ); }
/*     */ 
/*     */   
/*     */   public double getSize() {
/* 300 */     double xs = getXsize();
/* 301 */     double ys = getYsize();
/* 302 */     double zs = getZsize();
/* 303 */     return (xs + ys + zs) / 3.0D;
/*     */   }
/*     */ 
/*     */   
/* 307 */   public double getXsize() { return this.maxX - this.minX; }
/*     */ 
/*     */ 
/*     */   
/* 311 */   public double getYsize() { return this.maxY - this.minY; }
/*     */ 
/*     */ 
/*     */   
/* 315 */   public double getZsize() { return this.maxZ - this.minZ; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public AABB deflate(double xSubstract, double ySubtract, double zSubtract) { return inflate(-xSubstract, -ySubtract, -zSubtract); }
/*     */ 
/*     */ 
/*     */   
/* 331 */   public AABB deflate(double amount) { return inflate(-amount); }
/*     */ 
/*     */ 
/*     */   
/* 335 */   public Optional<Vec3> clip(Vec3 from, Vec3 to) { return clip(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, from, to); }
/*     */ 
/*     */   
/*     */   public static Optional<Vec3> clip(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vec3 from, Vec3 to) {
/* 339 */     double[] scaleReference = { 1.0D };
/* 340 */     double dx = to.x - from.x;
/* 341 */     double dy = to.y - from.y;
/* 342 */     double dz = to.z - from.z;
/*     */     
/* 344 */     Direction direction = getDirection(minX, minY, minZ, maxX, maxY, maxZ, from, scaleReference, null, dx, dy, dz);
/* 345 */     if (direction == null) {
/* 346 */       return Optional.empty();
/*     */     }
/*     */     
/* 349 */     double scale = scaleReference[0];
/* 350 */     return Optional.of(from.add(scale * dx, scale * dy, scale * dz));
/*     */   }
/*     */   
/*     */   public static BlockHitResult clip(Iterable<AABB> aabBs, Vec3 from, Vec3 to, BlockPos pos) {
/* 354 */     double[] scaleReference = { 1.0D };
/* 355 */     Direction direction = null;
/*     */     
/* 357 */     double dx = to.x - from.x;
/* 358 */     double dy = to.y - from.y;
/* 359 */     double dz = to.z - from.z;
/*     */     
/* 361 */     for (AABB aabb : aabBs) {
/* 362 */       direction = getDirection(aabb.move(pos), from, scaleReference, direction, dx, dy, dz);
/*     */     }
/*     */     
/* 365 */     if (direction == null) {
/* 366 */       return null;
/*     */     }
/*     */     
/* 369 */     double scale = scaleReference[0];
/* 370 */     return new BlockHitResult(from.add(scale * dx, scale * dy, scale * dz), direction, pos, false);
/*     */   }
/*     */ 
/*     */   
/* 374 */   private static Direction getDirection(AABB aabb, Vec3 from, double[] scaleReference, Direction direction, double dx, double dy, double dz) { return getDirection(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, from, scaleReference, direction, dx, dy, dz); }
/*     */ 
/*     */   
/*     */   private static Direction getDirection(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vec3 from, double[] scaleReference, Direction direction, double dx, double dy, double dz) {
/* 378 */     if (dx > 1.0E-7D) {
/* 379 */       direction = clipPoint(scaleReference, direction, dx, dy, dz, minX, minY, maxY, minZ, maxZ, Direction.WEST, from.x, from.y, from.z);
/* 380 */     } else if (dx < -1.0E-7D) {
/* 381 */       direction = clipPoint(scaleReference, direction, dx, dy, dz, maxX, minY, maxY, minZ, maxZ, Direction.EAST, from.x, from.y, from.z);
/*     */     } 
/*     */     
/* 384 */     if (dy > 1.0E-7D) {
/* 385 */       direction = clipPoint(scaleReference, direction, dy, dz, dx, minY, minZ, maxZ, minX, maxX, Direction.DOWN, from.y, from.z, from.x);
/* 386 */     } else if (dy < -1.0E-7D) {
/* 387 */       direction = clipPoint(scaleReference, direction, dy, dz, dx, maxY, minZ, maxZ, minX, maxX, Direction.UP, from.y, from.z, from.x);
/*     */     } 
/*     */     
/* 390 */     if (dz > 1.0E-7D) {
/* 391 */       direction = clipPoint(scaleReference, direction, dz, dx, dy, minZ, minX, maxX, minY, maxY, Direction.NORTH, from.z, from.x, from.y);
/* 392 */     } else if (dz < -1.0E-7D) {
/* 393 */       direction = clipPoint(scaleReference, direction, dz, dx, dy, maxZ, minX, maxX, minY, maxY, Direction.SOUTH, from.z, from.x, from.y);
/*     */     } 
/* 395 */     return direction;
/*     */   }
/*     */   
/*     */   private static Direction clipPoint(double[] scaleReference, Direction direction, double da, double db, double dc, double point, double minB, double maxB, double minC, double maxC, Direction newDirection, double fromA, double fromB, double fromC) {
/* 399 */     double s = (point - fromA) / da;
/* 400 */     double pb = fromB + s * db;
/* 401 */     double pc = fromC + s * dc;
/* 402 */     if (0.0D < s && s < scaleReference[0] && minB - 1.0E-7D < pb && pb < maxB + 1.0E-7D && minC - 1.0E-7D < pc && pc < maxC + 1.0E-7D) {
/*     */ 
/*     */ 
/*     */       
/* 406 */       scaleReference[0] = s;
/* 407 */       return newDirection;
/*     */     } 
/* 409 */     return direction;
/*     */   }
/*     */   
/*     */   public boolean collidedAlongVector(Vec3 vector, List<AABB> aabbs) {
/* 413 */     Vec3 from = getCenter();
/* 414 */     Vec3 to = from.add(vector);
/* 415 */     for (AABB shapePart : aabbs) {
/*     */       
/* 417 */       AABB inflated = shapePart.inflate(
/* 418 */           getXsize() * 0.5D - 1.0E-7D, 
/* 419 */           getYsize() * 0.5D - 1.0E-7D, 
/* 420 */           getZsize() * 0.5D - 1.0E-7D);
/*     */       
/* 422 */       if (inflated.contains(to) || inflated.contains(from))
/* 423 */         return true; 
/* 424 */       if (inflated.clip(from, to).isPresent()) {
/* 425 */         return true;
/*     */       }
/*     */     } 
/* 428 */     return false;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 point) {
/* 432 */     double dx = Math.max(Math.max(this.minX - point.x, point.x - this.maxX), 0.0D);
/* 433 */     double dy = Math.max(Math.max(this.minY - point.y, point.y - this.maxY), 0.0D);
/* 434 */     double dz = Math.max(Math.max(this.minZ - point.z, point.z - this.maxZ), 0.0D);
/* 435 */     return Mth.lengthSquared(dx, dy, dz);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(AABB boundingBox) {
/* 439 */     double dx = Math.max(Math.max(this.minX - boundingBox.maxX, boundingBox.minX - this.maxX), 0.0D);
/* 440 */     double dy = Math.max(Math.max(this.minY - boundingBox.maxY, boundingBox.minY - this.maxY), 0.0D);
/* 441 */     double dz = Math.max(Math.max(this.minZ - boundingBox.maxZ, boundingBox.minZ - this.maxZ), 0.0D);
/* 442 */     return Mth.lengthSquared(dx, dy, dz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 447 */   public String toString() { return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 451 */   public boolean hasNaN() { return (Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ)); }
/*     */ 
/*     */ 
/*     */   
/* 455 */   public Vec3 getCenter() { return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), Mth.lerp(0.5D, this.minY, this.maxY), Mth.lerp(0.5D, this.minZ, this.maxZ)); }
/*     */ 
/*     */ 
/*     */   
/* 459 */   public Vec3 getBottomCenter() { return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), this.minY, Mth.lerp(0.5D, this.minZ, this.maxZ)); }
/*     */ 
/*     */ 
/*     */   
/* 463 */   public Vec3 getMinPosition() { return new Vec3(this.minX, this.minY, this.minZ); }
/*     */ 
/*     */ 
/*     */   
/* 467 */   public Vec3 getMaxPosition() { return new Vec3(this.maxX, this.maxY, this.maxZ); }
/*     */ 
/*     */ 
/*     */   
/* 471 */   public static AABB ofSize(Vec3 center, double sizeX, double sizeY, double sizeZ) { return new AABB(center.x - sizeX / 2.0D, center.y - sizeY / 2.0D, center.z - sizeZ / 2.0D, center.x + sizeX / 2.0D, center.y + sizeY / 2.0D, center.z + sizeZ / 2.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 482 */     private float minX = Float.POSITIVE_INFINITY;
/* 483 */     private float minY = Float.POSITIVE_INFINITY;
/* 484 */     private float minZ = Float.POSITIVE_INFINITY;
/*     */     
/* 486 */     private float maxX = Float.NEGATIVE_INFINITY;
/* 487 */     private float maxY = Float.NEGATIVE_INFINITY;
/* 488 */     private float maxZ = Float.NEGATIVE_INFINITY;
/*     */     
/*     */     public void include(Vector3fc v) {
/* 491 */       this.minX = Math.min(this.minX, v.x());
/* 492 */       this.minY = Math.min(this.minY, v.y());
/* 493 */       this.minZ = Math.min(this.minZ, v.z());
/*     */       
/* 495 */       this.maxX = Math.max(this.maxX, v.x());
/* 496 */       this.maxY = Math.max(this.maxY, v.y());
/* 497 */       this.maxZ = Math.max(this.maxZ, v.z());
/*     */     }
/*     */ 
/*     */     
/* 501 */     public AABB build() { return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\AABB.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */