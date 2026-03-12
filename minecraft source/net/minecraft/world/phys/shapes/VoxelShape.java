/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VoxelShape
/*     */ {
/*     */   protected final DiscreteVoxelShape shape;
/*     */   private VoxelShape[] faces;
/*     */   
/*  28 */   protected VoxelShape(DiscreteVoxelShape shape) { this.shape = shape; }
/*     */ 
/*     */   
/*     */   public double min(Direction.Axis axis) {
/*  32 */     int i = this.shape.firstFull(axis);
/*  33 */     if (i >= this.shape.getSize(axis)) {
/*  34 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*  36 */     return get(axis, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public double max(Direction.Axis axis) {
/*  41 */     int i = this.shape.lastFull(axis);
/*  42 */     if (i <= 0) {
/*  43 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*  45 */     return get(axis, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public AABB bounds() {
/*  50 */     if (isEmpty()) {
/*  51 */       throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("No bounds for empty shape."));
/*     */     }
/*  53 */     return new AABB(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   public VoxelShape singleEncompassing() {
/*  57 */     if (isEmpty()) {
/*  58 */       return Shapes.empty();
/*     */     }
/*  60 */     return Shapes.box(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*  64 */   protected double get(Direction.Axis axis, int i) { return getCoords(axis).getDouble(i); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public boolean isEmpty() { return this.shape.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public VoxelShape move(Vec3 delta) { return move(delta.x, delta.y, delta.z); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public VoxelShape move(Vec3i delta) { return move(delta.getX(), delta.getY(), delta.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape move(double dx, double dy, double dz) {
/*  83 */     if (isEmpty()) {
/*  84 */       return Shapes.empty();
/*     */     }
/*  86 */     return new ArrayVoxelShape(this.shape, new OffsetDoubleList(
/*     */           
/*  88 */           getCoords(Direction.Axis.X), dx), new OffsetDoubleList(
/*  89 */           getCoords(Direction.Axis.Y), dy), new OffsetDoubleList(
/*  90 */           getCoords(Direction.Axis.Z), dz));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape optimize() {
/*  95 */     VoxelShape[] result = { Shapes.empty() };
/*  96 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> 
/*  97 */         result[0] = Shapes.joinUnoptimized(result[0], Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOp.OR));
/*     */     
/*  99 */     return result[0];
/*     */   }
/*     */ 
/*     */   
/* 103 */   public void forAllEdges(Shapes.DoubleLineConsumer consumer) { this.shape.forAllEdges((xi1, yi1, zi1, xi2, yi2, zi2) -> consumer.consume(get(Direction.Axis.X, xi1), get(Direction.Axis.Y, yi1), get(Direction.Axis.Z, zi1), get(Direction.Axis.X, xi2), get(Direction.Axis.Y, yi2), get(Direction.Axis.Z, zi2)), true); }
/*     */ 
/*     */   
/*     */   public void forAllBoxes(Shapes.DoubleLineConsumer consumer) {
/* 107 */     DoubleList xCoords = getCoords(Direction.Axis.X);
/* 108 */     DoubleList yCoords = getCoords(Direction.Axis.Y);
/* 109 */     DoubleList zCoords = getCoords(Direction.Axis.Z);
/*     */     
/* 111 */     this.shape.forAllBoxes((xi1, yi1, zi1, xi2, yi2, zi2) -> consumer.consume(xCoords.getDouble(xi1), yCoords.getDouble(yi1), zCoords.getDouble(zi1), xCoords.getDouble(xi2), yCoords.getDouble(yi2), zCoords.getDouble(zi2)), true);
/*     */   }
/*     */   
/*     */   public List<AABB> toAabbs() {
/* 115 */     List<AABB> list = Lists.newArrayList();
/* 116 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> list.add(new AABB(x1, y1, z1, x2, y2, z2)));
/* 117 */     return list;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis aAxis, double b, double c) {
/* 121 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 122 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 123 */     int bi = findIndex(bAxis, b);
/* 124 */     int ci = findIndex(cAxis, c);
/* 125 */     int i = this.shape.firstFull(aAxis, bi, ci);
/* 126 */     if (i >= this.shape.getSize(aAxis)) {
/* 127 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 129 */     return get(aAxis, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public double max(Direction.Axis aAxis, double b, double c) {
/* 134 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 135 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 136 */     int bi = findIndex(bAxis, b);
/* 137 */     int ci = findIndex(cAxis, c);
/* 138 */     int i = this.shape.lastFull(aAxis, bi, ci);
/* 139 */     if (i <= 0) {
/* 140 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/* 142 */     return get(aAxis, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   protected int findIndex(Direction.Axis axis, double coord) { return Mth.binarySearch(0, this.shape.getSize(axis) + 1, index -> (coord < get(axis, index))) - 1; }
/*     */ 
/*     */   
/*     */   public BlockHitResult clip(Vec3 from, Vec3 to, BlockPos pos) {
/* 155 */     if (isEmpty()) {
/* 156 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 160 */     Vec3 diff = to.subtract(from);
/* 161 */     if (diff.lengthSqr() < 1.0E-7D) {
/* 162 */       return null;
/*     */     }
/*     */     
/* 165 */     Vec3 testPoint = from.add(diff.scale(0.001D));
/*     */ 
/*     */     
/* 168 */     if (this.shape.isFullWide(findIndex(Direction.Axis.X, testPoint.x - pos.getX()), findIndex(Direction.Axis.Y, testPoint.y - pos.getY()), findIndex(Direction.Axis.Z, testPoint.z - pos.getZ()))) {
/* 169 */       return new BlockHitResult(testPoint, Direction.getApproximateNearest(diff.x, diff.y, diff.z).getOpposite(), pos, true);
/*     */     }
/*     */ 
/*     */     
/* 173 */     return AABB.clip(toAabbs(), from, to, pos);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> closestPointTo(Vec3 point) {
/* 177 */     if (isEmpty()) {
/* 178 */       return Optional.empty();
/*     */     }
/* 180 */     MutableObject<Vec3> closest = new MutableObject<Vec3>();
/* 181 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
/* 182 */           double x = Mth.clamp(point.x(), x1, x2);
/* 183 */           double y = Mth.clamp(point.y(), y1, y2);
/* 184 */           double z = Mth.clamp(point.z(), z1, z2);
/* 185 */           Vec3 currentClosest = (Vec3)closest.get();
/* 186 */           if (currentClosest == null || point.distanceToSqr(x, y, z) < point.distanceToSqr(currentClosest)) {
/* 187 */             closest.setValue(new Vec3(x, y, z));
/*     */           }
/*     */         });
/* 190 */     return Optional.of((Vec3)Objects.requireNonNull((Vec3)closest.get()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getFaceShape(Direction direction) {
/* 197 */     if (isEmpty() || this == Shapes.block()) {
/* 198 */       return this;
/*     */     }
/*     */     
/* 201 */     if (this.faces != null) {
/* 202 */       VoxelShape face = this.faces[direction.ordinal()];
/* 203 */       if (face != null) {
/* 204 */         return face;
/*     */       }
/*     */     } else {
/* 207 */       this.faces = new VoxelShape[6];
/*     */     } 
/*     */     
/* 210 */     VoxelShape face = calculateFace(direction);
/* 211 */     this.faces[direction.ordinal()] = face;
/* 212 */     return face;
/*     */   }
/*     */   
/*     */   private VoxelShape calculateFace(Direction direction) {
/* 216 */     Direction.Axis axis = direction.getAxis();
/* 217 */     if (isCubeLikeAlong(axis)) {
/* 218 */       return this;
/*     */     }
/*     */     
/* 221 */     Direction.AxisDirection sign = direction.getAxisDirection();
/* 222 */     int index = findIndex(axis, (sign == Direction.AxisDirection.POSITIVE) ? 0.9999999D : 1.0E-7D);
/* 223 */     SliceShape slice = new SliceShape(this, axis, index);
/*     */ 
/*     */     
/* 226 */     if (slice.isEmpty())
/* 227 */       return Shapes.empty(); 
/* 228 */     if (slice.isCubeLike()) {
/* 229 */       return Shapes.block();
/*     */     }
/*     */     
/* 232 */     return slice;
/*     */   }
/*     */   
/*     */   protected boolean isCubeLike() {
/* 236 */     for (Direction.Axis axis : Direction.Axis.VALUES) {
/* 237 */       if (!isCubeLikeAlong(axis)) {
/* 238 */         return false;
/*     */       }
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isCubeLikeAlong(Direction.Axis axis) {
/* 245 */     DoubleList coords = getCoords(axis);
/* 246 */     return (coords.size() == 2 && DoubleMath.fuzzyEquals(coords.getDouble(0), 0.0D, 1.0E-7D) && DoubleMath.fuzzyEquals(coords.getDouble(1), 1.0D, 1.0E-7D));
/*     */   }
/*     */ 
/*     */   
/* 250 */   public double collide(Direction.Axis axis, AABB moving, double distance) { return collideX(AxisCycle.between(axis, Direction.Axis.X), moving, distance); }
/*     */ 
/*     */   
/*     */   protected double collideX(AxisCycle transform, AABB moving, double distance) {
/* 254 */     if (isEmpty()) {
/* 255 */       return distance;
/*     */     }
/* 257 */     if (Math.abs(distance) < 1.0E-7D) {
/* 258 */       return 0.0D;
/*     */     }
/*     */     
/* 261 */     AxisCycle inverse = transform.inverse();
/* 262 */     Direction.Axis aAxis = inverse.cycle(Direction.Axis.X);
/* 263 */     Direction.Axis bAxis = inverse.cycle(Direction.Axis.Y);
/* 264 */     Direction.Axis cAxis = inverse.cycle(Direction.Axis.Z);
/*     */     
/* 266 */     double maxA = moving.max(aAxis);
/* 267 */     double minA = moving.min(aAxis);
/*     */     
/* 269 */     int aMin = findIndex(aAxis, minA + 1.0E-7D);
/* 270 */     int aMax = findIndex(aAxis, maxA - 1.0E-7D);
/*     */     
/* 272 */     int bMin = Math.max(0, findIndex(bAxis, moving.min(bAxis) + 1.0E-7D));
/* 273 */     int bMax = Math.min(this.shape.getSize(bAxis), findIndex(bAxis, moving.max(bAxis) - 1.0E-7D) + 1);
/*     */     
/* 275 */     int cMin = Math.max(0, findIndex(cAxis, moving.min(cAxis) + 1.0E-7D));
/* 276 */     int cMax = Math.min(this.shape.getSize(cAxis), findIndex(cAxis, moving.max(cAxis) - 1.0E-7D) + 1);
/*     */     
/* 278 */     int aSize = this.shape.getSize(aAxis);
/*     */     
/* 280 */     if (distance > 0.0D) {
/* 281 */       for (int a = aMax + 1; a < aSize; a++) {
/* 282 */         for (int b = bMin; b < bMax; b++) {
/* 283 */           for (int c = cMin; c < cMax; c++) {
/* 284 */             if (this.shape.isFullWide(inverse, a, b, c)) {
/* 285 */               double newDistance = get(aAxis, a) - maxA;
/* 286 */               if (newDistance >= -1.0E-7D) {
/* 287 */                 distance = Math.min(distance, newDistance);
/*     */               }
/* 289 */               return distance;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 294 */     } else if (distance < 0.0D) {
/* 295 */       for (int a = aMin - 1; a >= 0; a--) {
/* 296 */         for (int b = bMin; b < bMax; b++) {
/* 297 */           for (int c = cMin; c < cMax; c++) {
/* 298 */             if (this.shape.isFullWide(inverse, a, b, c)) {
/* 299 */               double newDistance = get(aAxis, a + 1) - minA;
/* 300 */               if (newDistance <= 1.0E-7D) {
/* 301 */                 distance = Math.max(distance, newDistance);
/*     */               }
/* 303 */               return distance;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 309 */     return distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   public boolean equals(Object obj) { return super.equals(obj); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 321 */     return isEmpty() ? "EMPTY" : ("VoxelShape[" + String.valueOf(bounds()) + "]");
/*     */   }
/*     */   
/*     */   public abstract DoubleList getCoords(Direction.Axis paramAxis);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\VoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */