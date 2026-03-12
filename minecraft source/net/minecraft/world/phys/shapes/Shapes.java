/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public final class Shapes
/*     */ {
/*     */   public static final double EPSILON = 1.0E-7D;
/*     */   public static final double BIG_EPSILON = 1.0E-6D;
/*  25 */   private static final VoxelShape BLOCK = (VoxelShape)Util.make(() -> {
/*  26 */         shape = new BitSetDiscreteVoxelShape(1, 1, 1);
/*  27 */         shape.fill(0, 0, 0);
/*  28 */         return new CubeVoxelShape(shape);
/*     */       });
/*     */   
/*  31 */   private static final Vec3 BLOCK_CENTER = new Vec3(0.5D, 0.5D, 0.5D);
/*     */   
/*  33 */   public static final VoxelShape INFINITY = box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final VoxelShape EMPTY = new ArrayVoxelShape(new BitSetDiscreteVoxelShape(0, 0, 0), new DoubleArrayList(new double[] { 0.0D }, ), new DoubleArrayList(new double[] { 0.0D }, ), new DoubleArrayList(new double[] { 0.0D }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static VoxelShape empty() { return EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static VoxelShape block() { return BLOCK; }
/*     */ 
/*     */   
/*     */   public static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  54 */     if (minX > maxX || minY > maxY || minZ > maxZ) {
/*  55 */       throw new IllegalArgumentException("The min values need to be smaller or equals to the max values");
/*     */     }
/*  57 */     return create(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  61 */     if (maxX - minX < 1.0E-7D || maxY - minY < 1.0E-7D || maxZ - minZ < 1.0E-7D) {
/*  62 */       return empty();
/*     */     }
/*     */     
/*  65 */     int xBits = findBits(minX, maxX);
/*  66 */     int yBits = findBits(minY, maxY);
/*  67 */     int zBits = findBits(minZ, maxZ);
/*     */     
/*  69 */     if (xBits < 0 || yBits < 0 || zBits < 0) {
/*  70 */       return new ArrayVoxelShape(BLOCK.shape, 
/*     */           
/*  72 */           DoubleArrayList.wrap(new double[] { minX, maxX
/*  73 */             }, ), DoubleArrayList.wrap(new double[] { minY, maxY
/*  74 */             }, ), DoubleArrayList.wrap(new double[] { minZ, maxZ }));
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (xBits == 0 && yBits == 0 && zBits == 0) {
/*  79 */       return block();
/*     */     }
/*     */     
/*  82 */     int xSize = 1 << xBits;
/*  83 */     int ySize = 1 << yBits;
/*  84 */     int zSize = 1 << zBits;
/*     */     
/*  86 */     BitSetDiscreteVoxelShape voxelShape = BitSetDiscreteVoxelShape.withFilledBounds(xSize, ySize, zSize, 
/*     */ 
/*     */ 
/*     */         
/*  90 */         (int)Math.round(minX * xSize), 
/*  91 */         (int)Math.round(minY * ySize), 
/*  92 */         (int)Math.round(minZ * zSize), 
/*  93 */         (int)Math.round(maxX * xSize), 
/*  94 */         (int)Math.round(maxY * ySize), 
/*  95 */         (int)Math.round(maxZ * zSize));
/*     */     
/*  97 */     return new CubeVoxelShape(voxelShape);
/*     */   }
/*     */ 
/*     */   
/* 101 */   public static VoxelShape create(AABB aabb) { return create(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static int findBits(double min, double max) {
/* 106 */     if (min < -1.0E-7D || max > 1.0000001D) {
/* 107 */       return -1;
/*     */     }
/* 109 */     for (int bits = 0; bits <= 3; bits++) {
/* 110 */       int intervals = 1 << bits;
/* 111 */       double shMin = min * intervals;
/* 112 */       double shMax = max * intervals;
/* 113 */       boolean foundMin = (Math.abs(shMin - Math.round(shMin)) < 1.0E-7D * intervals);
/* 114 */       boolean foundMax = (Math.abs(shMax - Math.round(shMax)) < 1.0E-7D * intervals);
/* 115 */       if (foundMin && foundMax) {
/* 116 */         return bits;
/*     */       }
/*     */     } 
/* 119 */     return -1;
/*     */   }
/*     */ 
/*     */   
/* 123 */   protected static long lcm(int first, int second) { return first * (second / IntMath.gcd(first, second)); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static VoxelShape or(VoxelShape first, VoxelShape second) { return join(first, second, BooleanOp.OR); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static VoxelShape or(VoxelShape first, VoxelShape... tail) { return (VoxelShape)Arrays.stream(tail).reduce(first, Shapes::or); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static VoxelShape join(VoxelShape first, VoxelShape second, BooleanOp op) { return joinUnoptimized(first, second, op).optimize(); }
/*     */ 
/*     */   
/*     */   public static VoxelShape joinUnoptimized(VoxelShape first, VoxelShape second, BooleanOp op) {
/* 139 */     if (op.apply(false, false)) {
/* 140 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/* 142 */     if (first == second) {
/* 143 */       return op.apply(true, true) ? first : empty();
/*     */     }
/* 145 */     boolean firstOnlyMatters = op.apply(true, false);
/* 146 */     boolean secondOnlyMatters = op.apply(false, true);
/*     */     
/* 148 */     if (first.isEmpty()) {
/* 149 */       return secondOnlyMatters ? second : empty();
/*     */     }
/* 151 */     if (second.isEmpty()) {
/* 152 */       return firstOnlyMatters ? first : empty();
/*     */     }
/*     */     
/* 155 */     IndexMerger xMerger = createIndexMerger(1, first.getCoords(Direction.Axis.X), second.getCoords(Direction.Axis.X), firstOnlyMatters, secondOnlyMatters);
/* 156 */     IndexMerger yMerger = createIndexMerger(xMerger.size() - 1, first.getCoords(Direction.Axis.Y), second.getCoords(Direction.Axis.Y), firstOnlyMatters, secondOnlyMatters);
/* 157 */     IndexMerger zMerger = createIndexMerger((xMerger.size() - 1) * (yMerger.size() - 1), first.getCoords(Direction.Axis.Z), second.getCoords(Direction.Axis.Z), firstOnlyMatters, secondOnlyMatters);
/*     */     
/* 159 */     BitSetDiscreteVoxelShape voxelShape = BitSetDiscreteVoxelShape.join(first.shape, second.shape, xMerger, yMerger, zMerger, op);
/* 160 */     if (xMerger instanceof DiscreteCubeMerger && yMerger instanceof DiscreteCubeMerger && zMerger instanceof DiscreteCubeMerger) {
/* 161 */       return new CubeVoxelShape(voxelShape);
/*     */     }
/* 163 */     return new ArrayVoxelShape(voxelShape, xMerger.getList(), yMerger.getList(), zMerger.getList());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean joinIsNotEmpty(VoxelShape first, VoxelShape second, BooleanOp op) {
/* 168 */     if (op.apply(false, false)) {
/* 169 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/*     */     
/* 172 */     boolean firstEmpty = first.isEmpty();
/* 173 */     boolean secondEmpty = second.isEmpty();
/* 174 */     if (firstEmpty || secondEmpty) {
/* 175 */       return op.apply(!firstEmpty, !secondEmpty);
/*     */     }
/* 177 */     if (first == second) {
/* 178 */       return op.apply(true, true);
/*     */     }
/*     */     
/* 181 */     boolean firstOnlyMatters = op.apply(true, false);
/* 182 */     boolean secondOnlyMatters = op.apply(false, true);
/* 183 */     for (Direction.Axis axis : AxisCycle.AXIS_VALUES) {
/* 184 */       if (first.max(axis) < second.min(axis) - 1.0E-7D) {
/* 185 */         return (firstOnlyMatters || secondOnlyMatters);
/*     */       }
/* 187 */       if (second.max(axis) < first.min(axis) - 1.0E-7D) {
/* 188 */         return (firstOnlyMatters || secondOnlyMatters);
/*     */       }
/*     */     } 
/*     */     
/* 192 */     IndexMerger xMerger = createIndexMerger(1, first.getCoords(Direction.Axis.X), second.getCoords(Direction.Axis.X), firstOnlyMatters, secondOnlyMatters);
/* 193 */     IndexMerger yMerger = createIndexMerger(xMerger.size() - 1, first.getCoords(Direction.Axis.Y), second.getCoords(Direction.Axis.Y), firstOnlyMatters, secondOnlyMatters);
/* 194 */     IndexMerger zMerger = createIndexMerger((xMerger.size() - 1) * (yMerger.size() - 1), first.getCoords(Direction.Axis.Z), second.getCoords(Direction.Axis.Z), firstOnlyMatters, secondOnlyMatters);
/* 195 */     return joinIsNotEmpty(xMerger, yMerger, zMerger, first.shape, second.shape, op);
/*     */   }
/*     */ 
/*     */   
/* 199 */   private static boolean joinIsNotEmpty(IndexMerger xMerger, IndexMerger yMerger, IndexMerger zMerger, DiscreteVoxelShape first, DiscreteVoxelShape second, BooleanOp op) { return !xMerger.forMergedIndexes((x1, x2, xr) -> 
/* 200 */         yMerger.forMergedIndexes(())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double collide(Direction.Axis axis, AABB moving, Iterable<VoxelShape> shapes, double distance) {
/* 209 */     for (VoxelShape shape : shapes) {
/* 210 */       if (Math.abs(distance) < 1.0E-7D) {
/* 211 */         return 0.0D;
/*     */       }
/* 213 */       distance = shape.collide(axis, moving, distance);
/*     */     } 
/* 215 */     return distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean blockOccludes(VoxelShape shape, VoxelShape occluder, Direction direction) {
/* 222 */     if (shape == block() && occluder == block()) {
/* 223 */       return true;
/*     */     }
/* 225 */     if (occluder.isEmpty()) {
/* 226 */       return false;
/*     */     }
/* 228 */     Direction.Axis axis = direction.getAxis();
/* 229 */     Direction.AxisDirection sign = direction.getAxisDirection();
/*     */     
/* 231 */     VoxelShape first = (sign == Direction.AxisDirection.POSITIVE) ? shape : occluder;
/* 232 */     VoxelShape second = (sign == Direction.AxisDirection.POSITIVE) ? occluder : shape;
/* 233 */     BooleanOp op = (sign == Direction.AxisDirection.POSITIVE) ? BooleanOp.ONLY_FIRST : BooleanOp.ONLY_SECOND;
/*     */     
/* 235 */     return (DoubleMath.fuzzyEquals(first.max(axis), 1.0D, 1.0E-7D) && 
/* 236 */       DoubleMath.fuzzyEquals(second.min(axis), 0.0D, 1.0E-7D) && 
/* 237 */       !joinIsNotEmpty(new SliceShape(first, axis, first.shape.getSize(axis) - 1), new SliceShape(second, axis, 0), op));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mergedFaceOccludes(VoxelShape shape, VoxelShape occluder, Direction direction) {
/* 244 */     if (shape == block() || occluder == block()) {
/* 245 */       return true;
/*     */     }
/*     */     
/* 248 */     Direction.Axis axis = direction.getAxis();
/* 249 */     Direction.AxisDirection sign = direction.getAxisDirection();
/*     */     
/* 251 */     VoxelShape first = (sign == Direction.AxisDirection.POSITIVE) ? shape : occluder;
/* 252 */     VoxelShape second = (sign == Direction.AxisDirection.POSITIVE) ? occluder : shape;
/*     */     
/* 254 */     if (!DoubleMath.fuzzyEquals(first.max(axis), 1.0D, 1.0E-7D)) {
/* 255 */       first = empty();
/*     */     }
/* 257 */     if (!DoubleMath.fuzzyEquals(second.min(axis), 0.0D, 1.0E-7D)) {
/* 258 */       second = empty();
/*     */     }
/*     */     
/* 261 */     return !joinIsNotEmpty(block(), joinUnoptimized(new SliceShape(first, axis, first.shape.getSize(axis) - 1), new SliceShape(second, axis, 0), BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean faceShapeOccludes(VoxelShape shape, VoxelShape occluder) {
/* 268 */     if (shape == block() || occluder == block()) {
/* 269 */       return true;
/*     */     }
/*     */     
/* 272 */     if (shape.isEmpty() && occluder.isEmpty()) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     return !joinIsNotEmpty(
/* 277 */         block(), 
/* 278 */         joinUnoptimized(shape, occluder, BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static IndexMerger createIndexMerger(int cost, DoubleList first, DoubleList second, boolean firstOnlyMatters, boolean secondOnlyMatters) {
/* 289 */     int firstSize = first.size() - 1;
/* 290 */     int secondSize = second.size() - 1;
/* 291 */     if (first instanceof CubePointRange && second instanceof CubePointRange) {
/* 292 */       long size = lcm(firstSize, secondSize);
/* 293 */       if (cost * size <= 256L) {
/* 294 */         return new DiscreteCubeMerger(firstSize, secondSize);
/*     */       }
/*     */     } 
/*     */     
/* 298 */     if (first.getDouble(firstSize) < second.getDouble(0) - 1.0E-7D)
/* 299 */       return new NonOverlappingMerger(first, second, false); 
/* 300 */     if (second.getDouble(secondSize) < first.getDouble(0) - 1.0E-7D) {
/* 301 */       return new NonOverlappingMerger(second, first, true);
/*     */     }
/*     */     
/* 304 */     if (firstSize == secondSize && Objects.equals(first, second)) {
/* 305 */       return new IdenticalMerger(first);
/*     */     }
/*     */     
/* 308 */     return new IndirectMerger(first, second, firstOnlyMatters, secondOnlyMatters);
/*     */   }
/*     */ 
/*     */   
/* 312 */   public static VoxelShape rotate(VoxelShape shape, OctahedralGroup rotation) { return rotate(shape, rotation, BLOCK_CENTER); }
/*     */ 
/*     */   
/*     */   public static VoxelShape rotate(VoxelShape shape, OctahedralGroup rotation, Vec3 rotationPoint) {
/* 316 */     if (rotation == OctahedralGroup.IDENTITY) {
/* 317 */       return shape;
/*     */     }
/*     */     
/* 320 */     DiscreteVoxelShape newDiscreteShape = shape.shape.rotate(rotation);
/* 321 */     if (shape instanceof CubeVoxelShape && BLOCK_CENTER.equals(rotationPoint)) {
/* 322 */       return new CubeVoxelShape(newDiscreteShape);
/*     */     }
/*     */     
/* 325 */     Direction.Axis newX = rotation.permutation().permuteAxis(Direction.Axis.X);
/* 326 */     Direction.Axis newY = rotation.permutation().permuteAxis(Direction.Axis.Y);
/* 327 */     Direction.Axis newZ = rotation.permutation().permuteAxis(Direction.Axis.Z);
/*     */     
/* 329 */     DoubleList newXs = shape.getCoords(newX);
/* 330 */     DoubleList newYs = shape.getCoords(newY);
/* 331 */     DoubleList newZs = shape.getCoords(newZ);
/*     */     
/* 333 */     boolean flipX = rotation.inverts(Direction.Axis.X);
/* 334 */     boolean flipY = rotation.inverts(Direction.Axis.Y);
/* 335 */     boolean flipZ = rotation.inverts(Direction.Axis.Z);
/*     */     
/* 337 */     return new ArrayVoxelShape(newDiscreteShape, 
/*     */         
/* 339 */         flipAxisIfNeeded(newXs, flipX, rotationPoint.get(newX), rotationPoint.x), 
/* 340 */         flipAxisIfNeeded(newYs, flipY, rotationPoint.get(newY), rotationPoint.y), 
/* 341 */         flipAxisIfNeeded(newZs, flipZ, rotationPoint.get(newZ), rotationPoint.z));
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static DoubleList flipAxisIfNeeded(DoubleList newAxis, boolean flip, double newRelative, double oldRelative) {
/* 347 */     if (!flip && newRelative == oldRelative) {
/* 348 */       return newAxis;
/*     */     }
/* 350 */     int size = newAxis.size();
/* 351 */     DoubleArrayList doubleArrayList = new DoubleArrayList(size);
/*     */     
/* 353 */     if (flip) {
/* 354 */       for (int i = size - 1; i >= 0; i--) {
/* 355 */         doubleArrayList.add(-(newAxis.getDouble(i) - newRelative) + oldRelative);
/*     */       }
/*     */     } else {
/* 358 */       for (int i = 0; i >= 0 && i < size; i++) {
/* 359 */         doubleArrayList.add(newAxis.getDouble(i) - newRelative + oldRelative);
/*     */       }
/*     */     } 
/* 362 */     return doubleArrayList;
/*     */   }
/*     */ 
/*     */   
/* 366 */   public static boolean equal(VoxelShape first, VoxelShape second) { return !joinIsNotEmpty(first, second, BooleanOp.NOT_SAME); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape zAxis) { return rotateHorizontalAxis(zAxis, BLOCK_CENTER); }
/*     */ 
/*     */ 
/*     */   
/* 379 */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape zAxis, Vec3 rotationCenter) { return Maps.newEnumMap(Map.of(Direction.Axis.Z, zAxis, Direction.Axis.X, 
/*     */           
/* 381 */           rotate(zAxis, OctahedralGroup.BLOCK_ROT_Y_90, rotationCenter))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 386 */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape north) { return rotateAllAxis(north, BLOCK_CENTER); }
/*     */ 
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape north, Vec3 rotationCenter) {
/* 390 */     return Maps.newEnumMap(Map.of(Direction.Axis.Z, north, Direction.Axis.X, 
/*     */           
/* 392 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90, rotationCenter), Direction.Axis.Y, 
/* 393 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_90, rotationCenter)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 398 */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north) { return rotateHorizontal(north, OctahedralGroup.IDENTITY, BLOCK_CENTER); }
/*     */ 
/*     */ 
/*     */   
/* 402 */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north, OctahedralGroup initial) { return rotateHorizontal(north, initial, BLOCK_CENTER); }
/*     */ 
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north, OctahedralGroup initial, Vec3 rotationCenter) {
/* 406 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 407 */           rotate(north, initial), Direction.EAST, 
/* 408 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90.compose(initial), rotationCenter), Direction.SOUTH, 
/* 409 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(initial), rotationCenter), Direction.WEST, 
/* 410 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_270.compose(initial), rotationCenter)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 415 */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north) { return rotateAll(north, OctahedralGroup.IDENTITY, BLOCK_CENTER); }
/*     */ 
/*     */ 
/*     */   
/* 419 */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north, Vec3 rotationCenter) { return rotateAll(north, OctahedralGroup.IDENTITY, rotationCenter); }
/*     */ 
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north, OctahedralGroup initial, Vec3 rotationCenter) {
/* 423 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 424 */           rotate(north, initial), Direction.EAST, 
/* 425 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90.compose(initial), rotationCenter), Direction.SOUTH, 
/* 426 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(initial), rotationCenter), Direction.WEST, 
/* 427 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_270.compose(initial), rotationCenter), Direction.UP, 
/* 428 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_270.compose(initial), rotationCenter), Direction.DOWN, 
/* 429 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_90.compose(initial), rotationCenter)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 434 */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape north) { return rotateAttachFace(north, OctahedralGroup.IDENTITY); }
/*     */ 
/*     */   
/*     */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape north, OctahedralGroup initial) {
/* 438 */     return Map.of(AttachFace.WALL, 
/* 439 */         rotateHorizontal(north, initial), AttachFace.FLOOR, 
/* 440 */         rotateHorizontal(north, OctahedralGroup.BLOCK_ROT_X_270.compose(initial)), AttachFace.CEILING, 
/*     */         
/* 442 */         rotateHorizontal(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(OctahedralGroup.BLOCK_ROT_X_90).compose(initial)));
/*     */   }
/*     */   
/*     */   public static interface DoubleLineConsumer {
/*     */     void consume(double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\Shapes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */