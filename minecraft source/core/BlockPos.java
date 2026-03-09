/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.Queue;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ @Immutable
/*     */ public class BlockPos
/*     */   extends Vec3i {
/*  36 */   public static final Codec<BlockPos> CODEC = Codec.INT_STREAM.comapFlatMap(input -> 
/*  37 */       Util.fixedSize(input, 3).map(()), pos -> 
/*  38 */       IntStream.of(new int[] { pos.getX(), pos.getY(), pos.getZ()
/*  39 */         })).stable();
/*     */   
/*  41 */   public static final StreamCodec<ByteBuf, BlockPos> STREAM_CODEC = new StreamCodec<ByteBuf, BlockPos>()
/*     */     {
/*     */       public BlockPos decode(ByteBuf input) {
/*  44 */         return FriendlyByteBuf.readBlockPos(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  49 */       public void encode(ByteBuf output, BlockPos value) { FriendlyByteBuf.writeBlockPos(output, value); }
/*     */     };
/*     */ 
/*     */   
/*  53 */   public static final BlockPos ZERO = new BlockPos(0, 0, 0);
/*     */ 
/*     */   
/*  56 */   public static final int PACKED_HORIZONTAL_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));
/*     */   
/*  58 */   public static final int PACKED_Y_LENGTH = 64 - 2 * PACKED_HORIZONTAL_LENGTH;
/*     */   
/*  60 */   private static final long PACKED_X_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
/*  61 */   private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
/*  62 */   private static final long PACKED_Z_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
/*     */   
/*     */   private static final int Y_OFFSET = 0;
/*  65 */   private static final int Z_OFFSET = PACKED_Y_LENGTH;
/*  66 */   private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_HORIZONTAL_LENGTH;
/*     */   
/*  68 */   public static final int MAX_HORIZONTAL_COORDINATE = (1 << PACKED_HORIZONTAL_LENGTH) / 2 - 1;
/*     */ 
/*     */   
/*  71 */   public BlockPos(int x, int y, int z) { super(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public BlockPos(Vec3i vec3i) { this(vec3i.getX(), vec3i.getY(), vec3i.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static long offset(long blockNode, Direction offset) { return offset(blockNode, offset.getStepX(), offset.getStepY(), offset.getStepZ()); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static long offset(long blockNode, int stepX, int stepY, int stepZ) { return asLong(getX(blockNode) + stepX, getY(blockNode) + stepY, getZ(blockNode) + stepZ); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static int getX(long blockNode) { return (int)(blockNode << 64 - X_OFFSET - PACKED_HORIZONTAL_LENGTH >> 64 - PACKED_HORIZONTAL_LENGTH); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static int getY(long blockNode) { return (int)(blockNode << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static int getZ(long blockNode) { return (int)(blockNode << 64 - Z_OFFSET - PACKED_HORIZONTAL_LENGTH >> 64 - PACKED_HORIZONTAL_LENGTH); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static BlockPos of(long blockNode) { return new BlockPos(getX(blockNode), getY(blockNode), getZ(blockNode)); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static BlockPos containing(double x, double y, double z) { return new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z)); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static BlockPos containing(Position pos) { return containing(pos.x(), pos.y(), pos.z()); }
/*     */ 
/*     */   
/*     */   public static BlockPos min(BlockPos a, BlockPos b) {
/* 111 */     return new BlockPos(
/* 112 */         Math.min(a.getX(), b.getX()), 
/* 113 */         Math.min(a.getY(), b.getY()), 
/* 114 */         Math.min(a.getZ(), b.getZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos max(BlockPos a, BlockPos b) {
/* 119 */     return new BlockPos(
/* 120 */         Math.max(a.getX(), b.getX()), 
/* 121 */         Math.max(a.getY(), b.getY()), 
/* 122 */         Math.max(a.getZ(), b.getZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public long asLong() { return asLong(getX(), getY(), getZ()); }
/*     */ 
/*     */   
/*     */   public static long asLong(int x, int y, int z) {
/* 131 */     node = 0L;
/* 132 */     node |= (x & PACKED_X_MASK) << X_OFFSET;
/* 133 */     node |= (y & PACKED_Y_MASK) << false;
/* 134 */     return (z & PACKED_Z_MASK) << Z_OFFSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static long getFlatIndex(long neighborBlockNode) { return neighborBlockNode & 0xFFFFFFFFFFFFFFF0L; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos offset(int x, int y, int z) {
/* 148 */     if (x == 0 && y == 0 && z == 0) {
/* 149 */       return this;
/*     */     }
/* 151 */     return new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */ 
/*     */   
/* 155 */   public Vec3 getCenter() { return Vec3.atCenterOf(this); }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public Vec3 getBottomCenter() { return Vec3.atBottomCenterOf(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public BlockPos offset(Vec3i vec) { return offset(vec.getX(), vec.getY(), vec.getZ()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public BlockPos subtract(Vec3i vec) { return offset(-vec.getX(), -vec.getY(), -vec.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos multiply(int scale) {
/* 174 */     if (scale == 1)
/* 175 */       return this; 
/* 176 */     if (scale == 0) {
/* 177 */       return ZERO;
/*     */     }
/* 179 */     return new BlockPos(getX() * scale, getY() * scale, getZ() * scale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public BlockPos above() { return relative(Direction.UP); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public BlockPos above(int steps) { return relative(Direction.UP, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public BlockPos below() { return relative(Direction.DOWN); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public BlockPos below(int steps) { return relative(Direction.DOWN, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   public BlockPos north() { return relative(Direction.NORTH); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public BlockPos north(int steps) { return relative(Direction.NORTH, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   public BlockPos south() { return relative(Direction.SOUTH); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   public BlockPos south(int steps) { return relative(Direction.SOUTH, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   public BlockPos west() { return relative(Direction.WEST); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 229 */   public BlockPos west(int steps) { return relative(Direction.WEST, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public BlockPos east() { return relative(Direction.EAST); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public BlockPos east(int steps) { return relative(Direction.EAST, steps); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public BlockPos relative(Direction direction) { return new BlockPos(getX() + direction.getStepX(), getY() + direction.getStepY(), getZ() + direction.getStepZ()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos relative(Direction direction, int steps) {
/* 249 */     if (steps == 0) {
/* 250 */       return this;
/*     */     }
/* 252 */     return new BlockPos(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos relative(Direction.Axis axis, int steps) {
/* 257 */     if (steps == 0) {
/* 258 */       return this;
/*     */     }
/* 260 */     int xStep = (axis == Direction.Axis.X) ? steps : 0;
/* 261 */     int yStep = (axis == Direction.Axis.Y) ? steps : 0;
/* 262 */     int zStep = (axis == Direction.Axis.Z) ? steps : 0;
/* 263 */     return new BlockPos(getX() + xStep, getY() + yStep, getZ() + zStep);
/*     */   }
/*     */   
/*     */   public BlockPos rotate(Rotation rotation) {
/* 267 */     switch (rotation) { default: throw new MatchException(null, null);case X: case Y: case Z: case null: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 271 */       this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 277 */   public BlockPos cross(Vec3i upVector) { return new BlockPos(getY() * upVector.getZ() - getZ() * upVector.getY(), getZ() * upVector.getX() - getX() * upVector.getZ(), getX() * upVector.getY() - getY() * upVector.getX()); }
/*     */ 
/*     */ 
/*     */   
/* 281 */   public BlockPos atY(int y) { return new BlockPos(getX(), y, getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public BlockPos immutable() { return this; }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public MutableBlockPos mutable() { return new MutableBlockPos(getX(), getY(), getZ()); }
/*     */ 
/*     */   
/*     */   public Vec3 clampLocationWithin(Vec3 location) {
/* 293 */     return new Vec3(
/* 294 */         Mth.clamp(location.x, (getX() + 1.0E-5F), getX() + 1.0D - 9.999999747378752E-6D), 
/* 295 */         Mth.clamp(location.y, (getY() + 1.0E-5F), getY() + 1.0D - 9.999999747378752E-6D), 
/* 296 */         Mth.clamp(location.z, (getZ() + 1.0E-5F), getZ() + 1.0D - 9.999999747378752E-6D));
/*     */   }
/*     */   
/*     */   public static class MutableBlockPos
/*     */     extends BlockPos {
/* 301 */     public MutableBlockPos() { this(0, 0, 0); }
/*     */ 
/*     */ 
/*     */     
/* 305 */     public MutableBlockPos(int x, int y, int z) { super(x, y, z); }
/*     */ 
/*     */ 
/*     */     
/* 309 */     public MutableBlockPos(double x, double y, double z) { this(Mth.floor(x), Mth.floor(y), Mth.floor(z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     public BlockPos offset(int x, int y, int z) { return super.offset(x, y, z).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 319 */     public BlockPos multiply(int scale) { return super.multiply(scale).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     public BlockPos relative(Direction direction, int steps) { return super.relative(direction, steps).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     public BlockPos relative(Direction.Axis axis, int steps) { return super.relative(axis, steps).immutable(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     public BlockPos rotate(Rotation rotation) { return super.rotate(rotation).immutable(); }
/*     */ 
/*     */     
/*     */     public MutableBlockPos set(int x, int y, int z) {
/* 338 */       setX(x);
/* 339 */       setY(y);
/* 340 */       setZ(z);
/* 341 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 345 */     public MutableBlockPos set(double x, double y, double z) { return set(Mth.floor(x), Mth.floor(y), Mth.floor(z)); }
/*     */ 
/*     */ 
/*     */     
/* 349 */     public MutableBlockPos set(Vec3i vec) { return set(vec.getX(), vec.getY(), vec.getZ()); }
/*     */ 
/*     */ 
/*     */     
/* 353 */     public MutableBlockPos set(long pos) { return set(getX(pos), getY(pos), getZ(pos)); }
/*     */ 
/*     */     
/*     */     public MutableBlockPos set(AxisCycle transform, int x, int y, int z) {
/* 357 */       return set(transform
/* 358 */           .cycle(x, y, z, Direction.Axis.X), transform
/* 359 */           .cycle(x, y, z, Direction.Axis.Y), transform
/* 360 */           .cycle(x, y, z, Direction.Axis.Z));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 365 */     public MutableBlockPos setWithOffset(Vec3i pos, Direction direction) { return set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ()); }
/*     */ 
/*     */ 
/*     */     
/* 369 */     public MutableBlockPos setWithOffset(Vec3i pos, int x, int y, int z) { return set(pos.getX() + x, pos.getY() + y, pos.getZ() + z); }
/*     */ 
/*     */ 
/*     */     
/* 373 */     public MutableBlockPos setWithOffset(Vec3i pos, Vec3i offset) { return set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ()); }
/*     */ 
/*     */ 
/*     */     
/* 377 */     public MutableBlockPos move(Direction direction) { return move(direction, 1); }
/*     */ 
/*     */ 
/*     */     
/* 381 */     public MutableBlockPos move(Direction direction, int steps) { return set(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps); }
/*     */ 
/*     */ 
/*     */     
/* 385 */     public MutableBlockPos move(int x, int y, int z) { return set(getX() + x, getY() + y, getZ() + z); }
/*     */ 
/*     */ 
/*     */     
/* 389 */     public MutableBlockPos move(Vec3i pos) { return set(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ()); }
/*     */ 
/*     */     
/*     */     public MutableBlockPos clamp(Direction.Axis axis, int minimum, int maximum) {
/* 393 */       switch (BlockPos.null.$SwitchMap$net$minecraft$core$Direction$Axis[axis.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: case 3: break; }  return 
/*     */ 
/*     */         
/* 396 */         set(getX(), getY(), Mth.clamp(getZ(), minimum, maximum));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MutableBlockPos setX(int x) {
/* 402 */       super.setX(x);
/* 403 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public MutableBlockPos setY(int y) {
/* 408 */       super.setY(y);
/* 409 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public MutableBlockPos setZ(int z) {
/* 414 */       super.setZ(z);
/* 415 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 420 */     public BlockPos immutable() { return new BlockPos(this); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 425 */   public static Iterable<BlockPos> randomInCube(RandomSource random, int limit, BlockPos center, int sizeToScanInAllDirections) { return randomBetweenClosed(random, limit, center.getX() - sizeToScanInAllDirections, center.getY() - sizeToScanInAllDirections, center.getZ() - sizeToScanInAllDirections, center.getX() + sizeToScanInAllDirections, center.getY() + sizeToScanInAllDirections, center.getZ() + sizeToScanInAllDirections); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Stream<BlockPos> squareOutSouthEast(BlockPos from) {
/* 437 */     return Stream.of(new BlockPos[] { from, from
/*     */           
/* 439 */           .south(), from
/* 440 */           .east(), from
/* 441 */           .south().east() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> randomBetweenClosed(final RandomSource random, final int limit, final int minX, final int minY, final int minZ, int maxX, int maxY, int maxZ) {
/* 447 */     final int width = maxX - minX + 1;
/* 448 */     final int height = maxY - minY + 1;
/* 449 */     final int depth = maxZ - minZ + 1;
/*     */     
/* 451 */     return () -> new AbstractIterator<BlockPos>() {
/* 452 */         final BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/* 453 */         int counter = limit;
/*     */ 
/*     */         
/*     */         protected BlockPos computeNext() {
/* 457 */           if (this.counter <= 0) {
/* 458 */             return (BlockPos)endOfData();
/*     */           }
/*     */           
/* 461 */           BlockPos next = this.nextPos.set(minX + random
/* 462 */               .nextInt(width), minY + random
/* 463 */               .nextInt(height), minZ + random
/* 464 */               .nextInt(depth));
/*     */           
/* 466 */           this.counter--;
/* 467 */           return next;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> withinManhattan(BlockPos origin, final int reachX, final int reachY, final int reachZ) {
/* 473 */     final int maxDepth = reachX + reachY + reachZ;
/* 474 */     final int originX = origin.getX();
/* 475 */     final int originY = origin.getY();
/* 476 */     final int originZ = origin.getZ();
/*     */     
/* 478 */     return () -> new AbstractIterator<BlockPos>() {
/* 479 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int currentDepth;
/*     */         
/*     */         private int maxX;
/*     */         
/*     */         private int maxY;
/*     */         
/*     */         private int x;
/*     */         private int y;
/*     */         private boolean zMirror;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 492 */           if (this.zMirror) {
/* 493 */             this.zMirror = false;
/* 494 */             this.cursor.setZ(originZ - this.cursor.getZ() - originZ);
/* 495 */             return this.cursor;
/*     */           } 
/*     */           
/* 498 */           BlockPos found = null;
/* 499 */           while (found == null) {
/* 500 */             if (this.y > this.maxY) {
/* 501 */               this.x++;
/* 502 */               if (this.x > this.maxX) {
/* 503 */                 this.currentDepth++;
/* 504 */                 if (this.currentDepth > maxDepth) {
/* 505 */                   return (BlockPos)endOfData();
/*     */                 }
/* 507 */                 this.maxX = Math.min(reachX, this.currentDepth);
/* 508 */                 this.x = -this.maxX;
/*     */               } 
/* 510 */               this.maxY = Math.min(reachY, this.currentDepth - Math.abs(this.x));
/* 511 */               this.y = -this.maxY;
/*     */             } 
/*     */             
/* 514 */             int xx = this.x;
/* 515 */             int yy = this.y;
/* 516 */             int zz = this.currentDepth - Math.abs(xx) - Math.abs(yy);
/* 517 */             if (zz <= reachZ) {
/* 518 */               this.zMirror = (zz != 0);
/* 519 */               found = this.cursor.set(originX + xx, originY + yy, originZ + zz);
/*     */             } 
/* 521 */             this.y++;
/*     */           } 
/* 523 */           return found;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findClosestMatch(BlockPos startPos, int horizontalSearchRadius, int verticalSearchRadius, Predicate<BlockPos> predicate) {
/* 529 */     for (BlockPos blockPos : withinManhattan(startPos, horizontalSearchRadius, verticalSearchRadius, horizontalSearchRadius)) {
/* 530 */       if (predicate.test(blockPos)) {
/* 531 */         return Optional.of(blockPos);
/*     */       }
/*     */     } 
/* 534 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 538 */   public static Stream<BlockPos> withinManhattanStream(BlockPos origin, int reachX, int reachY, int reachZ) { return StreamSupport.stream(withinManhattan(origin, reachX, reachY, reachZ).spliterator(), false); }
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(AABB box) {
/* 542 */     BlockPos startPos = containing(box.minX, box.minY, box.minZ);
/* 543 */     BlockPos endPos = containing(box.maxX, box.maxY, box.maxZ);
/* 544 */     return betweenClosed(startPos, endPos);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(BlockPos a, BlockPos b) {
/* 548 */     return betweenClosed(
/* 549 */         Math.min(a.getX(), b.getX()), 
/* 550 */         Math.min(a.getY(), b.getY()), 
/* 551 */         Math.min(a.getZ(), b.getZ()), 
/* 552 */         Math.max(a.getX(), b.getX()), 
/* 553 */         Math.max(a.getY(), b.getY()), 
/* 554 */         Math.max(a.getZ(), b.getZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 559 */   public static Stream<BlockPos> betweenClosedStream(BlockPos a, BlockPos b) { return StreamSupport.stream(betweenClosed(a, b).spliterator(), false); }
/*     */ 
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(BoundingBox boundingBox) {
/* 563 */     return betweenClosedStream(
/* 564 */         Math.min(boundingBox.minX(), boundingBox.maxX()), 
/* 565 */         Math.min(boundingBox.minY(), boundingBox.maxY()), 
/* 566 */         Math.min(boundingBox.minZ(), boundingBox.maxZ()), 
/* 567 */         Math.max(boundingBox.minX(), boundingBox.maxX()), 
/* 568 */         Math.max(boundingBox.minY(), boundingBox.maxY()), 
/* 569 */         Math.max(boundingBox.minZ(), boundingBox.maxZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 574 */   public static Stream<BlockPos> betweenClosedStream(AABB box) { return betweenClosedStream(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ)); }
/*     */ 
/*     */ 
/*     */   
/* 578 */   public static Stream<BlockPos> betweenClosedStream(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) { return StreamSupport.stream(betweenClosed(minX, minY, minZ, maxX, maxY, maxZ).spliterator(), false); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(final int minX, final int minY, final int minZ, int maxX, int maxY, int maxZ) {
/* 583 */     final int width = maxX - minX + 1;
/* 584 */     final int height = maxY - minY + 1;
/* 585 */     int depth = maxZ - minZ + 1;
/* 586 */     final int end = width * height * depth;
/*     */     
/* 588 */     return () -> new AbstractIterator<BlockPos>() {
/* 589 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int index;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 594 */           if (this.index == end) {
/* 595 */             return (BlockPos)endOfData();
/*     */           }
/*     */           
/* 598 */           int x = this.index % width;
/* 599 */           int slice = this.index / width;
/* 600 */           int y = slice % height;
/* 601 */           int z = slice / height;
/*     */           
/* 603 */           this.index++;
/* 604 */           return this.cursor.set(minX + x, minY + y, minZ + z);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<MutableBlockPos> spiralAround(final BlockPos center, final int radius, final Direction firstDirection, final Direction secondDirection) {
/* 610 */     Validate.validState((firstDirection.getAxis() != secondDirection.getAxis()), "The two directions cannot be on the same axis", new Object[0]);
/*     */     
/* 612 */     return () -> new AbstractIterator<MutableBlockPos>() {
/* 613 */         private final Direction[] directions = { firstDirection, secondDirection, firstDirection
/*     */ 
/*     */             
/* 616 */             .getOpposite(), secondDirection
/* 617 */             .getOpposite() };
/*     */         
/* 619 */         private final BlockPos.MutableBlockPos cursor = center.mutable().move(secondDirection);
/* 620 */         private final int legs = 4 * radius;
/* 621 */         private int leg = -1;
/*     */         
/*     */         private int legSize;
/*     */         private int legIndex;
/* 625 */         private int lastX = this.cursor.getX();
/* 626 */         private int lastY = this.cursor.getY();
/* 627 */         private int lastZ = this.cursor.getZ();
/*     */ 
/*     */         
/*     */         protected BlockPos.MutableBlockPos computeNext() {
/* 631 */           this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
/*     */           
/* 633 */           this.lastX = this.cursor.getX();
/* 634 */           this.lastY = this.cursor.getY();
/* 635 */           this.lastZ = this.cursor.getZ();
/*     */           
/* 637 */           if (this.legIndex >= this.legSize) {
/* 638 */             if (this.leg >= this.legs) {
/* 639 */               return (BlockPos.MutableBlockPos)endOfData();
/*     */             }
/* 641 */             this.leg++;
/* 642 */             this.legIndex = 0;
/* 643 */             this.legSize = this.leg / 2 + 1;
/*     */           } 
/* 645 */           this.legIndex++;
/*     */           
/* 647 */           return this.cursor;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public enum TraversalNodeStatus {
/* 653 */     ACCEPT, SKIP, STOP;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int breadthFirstTraversal(BlockPos startPos, int maxDepth, int maxCount, BiConsumer<BlockPos, Consumer<BlockPos>> neighbourProvider, Function<BlockPos, TraversalNodeStatus> nodeProcessor) {
/* 672 */     Queue<Pair<BlockPos, Integer>> nodes = new ArrayDeque<Pair<BlockPos, Integer>>();
/* 673 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 674 */     nodes.add(Pair.of(startPos, Integer.valueOf(0)));
/* 675 */     int count = 0;
/* 676 */     while (!nodes.isEmpty()) {
/* 677 */       Pair<BlockPos, Integer> node = (Pair)nodes.poll();
/* 678 */       BlockPos currentPos = (BlockPos)node.getLeft();
/* 679 */       int depth = ((Integer)node.getRight()).intValue();
/* 680 */       long currentPosLong = currentPos.asLong();
/* 681 */       if (!longOpenHashSet.add(currentPosLong)) {
/*     */         continue;
/*     */       }
/* 684 */       TraversalNodeStatus next = (TraversalNodeStatus)nodeProcessor.apply(currentPos);
/* 685 */       if (next == TraversalNodeStatus.SKIP)
/*     */         continue; 
/* 687 */       if (next == TraversalNodeStatus.STOP) {
/*     */         break;
/*     */       }
/* 690 */       count++;
/* 691 */       if (count >= maxCount) {
/* 692 */         return count;
/*     */       }
/* 694 */       if (depth >= maxDepth) {
/*     */         continue;
/*     */       }
/* 697 */       neighbourProvider.accept(currentPos, pos -> nodes.add(Pair.of(pos, Integer.valueOf(depth + 1))));
/*     */     } 
/* 699 */     return count;
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenCornersInDirection(AABB aabb, Vec3 direction) {
/* 703 */     Vec3 minCorner = aabb.getMinPosition();
/* 704 */     int firstCornerX = Mth.floor(minCorner.x());
/* 705 */     int firstCornerY = Mth.floor(minCorner.y());
/* 706 */     int firstCornerZ = Mth.floor(minCorner.z());
/*     */     
/* 708 */     Vec3 maxCorner = aabb.getMaxPosition();
/* 709 */     int secondCornerX = Mth.floor(maxCorner.x());
/* 710 */     int secondCornerY = Mth.floor(maxCorner.y());
/* 711 */     int secondCornerZ = Mth.floor(maxCorner.z());
/* 712 */     return betweenCornersInDirection(firstCornerX, firstCornerY, firstCornerZ, secondCornerX, secondCornerY, secondCornerZ, direction);
/*     */   }
/*     */ 
/*     */   
/* 716 */   public static Iterable<BlockPos> betweenCornersInDirection(BlockPos firstCorner, BlockPos secondCorner, Vec3 direction) { return betweenCornersInDirection(firstCorner.getX(), firstCorner.getY(), firstCorner.getZ(), secondCorner.getX(), secondCorner.getY(), secondCorner.getZ(), direction); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> betweenCornersInDirection(int firstCornerX, int firstCornerY, int firstCornerZ, int secondCornerX, int secondCornerY, int secondCornerZ, Vec3 direction) {
/* 723 */     int minCornerX = Math.min(firstCornerX, secondCornerX);
/* 724 */     int minCornerY = Math.min(firstCornerY, secondCornerY);
/* 725 */     int minCornerZ = Math.min(firstCornerZ, secondCornerZ);
/*     */     
/* 727 */     int maxCornerX = Math.max(firstCornerX, secondCornerX);
/* 728 */     int maxCornerY = Math.max(firstCornerY, secondCornerY);
/* 729 */     int maxCornerZ = Math.max(firstCornerZ, secondCornerZ);
/*     */     
/* 731 */     int diffX = maxCornerX - minCornerX;
/* 732 */     int diffY = maxCornerY - minCornerY;
/* 733 */     int diffZ = maxCornerZ - minCornerZ;
/*     */     
/* 735 */     final int startCornerX = (direction.x >= 0.0D) ? minCornerX : maxCornerX;
/* 736 */     final int startCornerY = (direction.y >= 0.0D) ? minCornerY : maxCornerY;
/* 737 */     final int startCornerZ = (direction.z >= 0.0D) ? minCornerZ : maxCornerZ;
/*     */     
/* 739 */     ImmutableList immutableList = Direction.axisStepOrder(direction);
/* 740 */     Direction.Axis firstVisitAxis = (Direction.Axis)immutableList.get(0);
/* 741 */     Direction.Axis secondVisitAxis = (Direction.Axis)immutableList.get(1);
/* 742 */     Direction.Axis thirdVisitAxis = (Direction.Axis)immutableList.get(2);
/*     */     
/* 744 */     final Direction firstVisitDir = (direction.get(firstVisitAxis) >= 0.0D) ? firstVisitAxis.getPositive() : firstVisitAxis.getNegative();
/* 745 */     final Direction secondVisitDir = (direction.get(secondVisitAxis) >= 0.0D) ? secondVisitAxis.getPositive() : secondVisitAxis.getNegative();
/* 746 */     final Direction thirdVisitDir = (direction.get(thirdVisitAxis) >= 0.0D) ? thirdVisitAxis.getPositive() : thirdVisitAxis.getNegative();
/*     */     
/* 748 */     final int firstMax = firstVisitAxis.choose(diffX, diffY, diffZ);
/* 749 */     final int secondMax = secondVisitAxis.choose(diffX, diffY, diffZ);
/* 750 */     final int thirdMax = thirdVisitAxis.choose(diffX, diffY, diffZ);
/*     */     
/* 752 */     return () -> new AbstractIterator<BlockPos>() {
/* 753 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int firstIndex;
/*     */         private int secondIndex;
/*     */         private int thirdIndex;
/*     */         private boolean end;
/* 759 */         private final int firstDirX = firstVisitDir.getStepX();
/* 760 */         private final int firstDirY = firstVisitDir.getStepY();
/* 761 */         private final int firstDirZ = firstVisitDir.getStepZ();
/*     */         
/* 763 */         private final int secondDirX = secondVisitDir.getStepX();
/* 764 */         private final int secondDirY = secondVisitDir.getStepY();
/* 765 */         private final int secondDirZ = secondVisitDir.getStepZ();
/*     */         
/* 767 */         private final int thirdDirX = thirdVisitDir.getStepX();
/* 768 */         private final int thirdDirY = thirdVisitDir.getStepY();
/* 769 */         private final int thirdDirZ = thirdVisitDir.getStepZ();
/*     */ 
/*     */         
/*     */         protected BlockPos computeNext() {
/* 773 */           if (this.end) {
/* 774 */             return (BlockPos)endOfData();
/*     */           }
/*     */           
/* 777 */           this.cursor.set(startCornerX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex, startCornerY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex, startCornerZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 783 */           if (this.thirdIndex < thirdMax) {
/* 784 */             this.thirdIndex++;
/* 785 */           } else if (this.secondIndex < secondMax) {
/* 786 */             this.secondIndex++;
/* 787 */             this.thirdIndex = 0;
/* 788 */           } else if (this.firstIndex < firstMax) {
/* 789 */             this.firstIndex++;
/* 790 */             this.thirdIndex = 0;
/* 791 */             this.secondIndex = 0;
/*     */           } else {
/* 793 */             this.end = true;
/*     */           } 
/*     */           
/* 796 */           return this.cursor;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\BlockPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */