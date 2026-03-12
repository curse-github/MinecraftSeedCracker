/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public static enum Direction
/*     */   implements StringRepresentable
/*     */ {
/*  34 */   DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
/*  35 */   UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
/*  36 */   NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
/*  37 */   SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
/*  38 */   WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
/*  39 */   EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0)); public static final StringRepresentable.EnumCodec<Direction> CODEC; public static final Codec<Direction> VERTICAL_CODEC; public static final IntFunction<Direction> BY_ID; public static final StreamCodec<ByteBuf, Direction> STREAM_CODEC; @Deprecated
/*     */   public static final Codec<Direction> LEGACY_ID_CODEC; @Deprecated
/*     */   public static final Codec<Direction> LEGACY_ID_CODEC_2D; private static final ImmutableList<Axis> YXZ_AXIS_ORDER; private static final ImmutableList<Axis> YZX_AXIS_ORDER; private final int data3d; private final int oppositeIndex; private final int data2d; private final String name; private final Axis axis; private final AxisDirection axisDirection; private final Vec3i normal; private final Vec3 normalVec3; private final Vector3fc normalVec3f; private static final Direction[] VALUES; private static final Direction[] BY_3D_DATA; private static final Direction[] BY_2D_DATA; static  {
/*  42 */     CODEC = StringRepresentable.fromEnum(Direction::values);
/*  43 */     VERTICAL_CODEC = CODEC.validate(Direction::verifyVertical);
/*     */     
/*  45 */     BY_ID = ByIdMap.continuous(Direction::get3DDataValue, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*     */     
/*  47 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Direction::get3DDataValue);
/*     */ 
/*     */     
/*  50 */     LEGACY_ID_CODEC = Codec.BYTE.xmap(Direction::from3DDataValue, d -> Byte.valueOf((byte)d.get3DDataValue()));
/*     */     
/*  52 */     LEGACY_ID_CODEC_2D = Codec.BYTE.xmap(Direction::from2DDataValue, d -> Byte.valueOf((byte)d.get2DDataValue()));
/*     */     
/*  54 */     YXZ_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.X, Axis.Z);
/*  55 */     YZX_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.Z, Axis.X);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     VALUES = values();
/*     */     
/*  69 */     BY_3D_DATA = (Direction[])Arrays.stream(VALUES).sorted(Comparator.comparingInt(d -> d.data3d)).toArray(x$0 -> new Direction[x$0]);
/*  70 */     BY_2D_DATA = (Direction[])Arrays.stream(VALUES).filter(d -> d.getAxis().isHorizontal()).sorted(Comparator.comparingInt(d -> d.data2d)).toArray(x$0 -> new Direction[x$0]);
/*     */   }
/*     */   Direction(int data3d, int oppositeIndex, int data2d, String name, AxisDirection axisDirection, Axis axis, Vec3i normal) {
/*  73 */     this.data3d = data3d;
/*  74 */     this.data2d = data2d;
/*  75 */     this.oppositeIndex = oppositeIndex;
/*  76 */     this.name = name;
/*  77 */     this.axis = axis;
/*  78 */     this.axisDirection = axisDirection;
/*  79 */     this.normal = normal;
/*  80 */     this.normalVec3 = Vec3.atLowerCornerOf(normal);
/*  81 */     this.normalVec3f = new Vector3f(normal.getX(), normal.getY(), normal.getZ());
/*     */   }
/*     */   
/*     */   public static Direction[] orderedByNearest(Entity entity) {
/*  85 */     float pitch = entity.getViewXRot(1.0F) * 0.017453292F;
/*  86 */     float yaw = -entity.getViewYRot(1.0F) * 0.017453292F;
/*     */     
/*  88 */     float pitchSin = Mth.sin(pitch);
/*  89 */     float pitchCos = Mth.cos(pitch);
/*  90 */     float yawSin = Mth.sin(yaw);
/*  91 */     float yawCos = Mth.cos(yaw);
/*     */     
/*  93 */     boolean xPos = (yawSin > 0.0F);
/*  94 */     boolean yPos = (pitchSin < 0.0F);
/*  95 */     boolean zPos = (yawCos > 0.0F);
/*     */     
/*  97 */     float xYaw = xPos ? yawSin : -yawSin;
/*  98 */     float yMag = yPos ? -pitchSin : pitchSin;
/*  99 */     float zYaw = zPos ? yawCos : -yawCos;
/*     */     
/* 101 */     float xMag = xYaw * pitchCos;
/* 102 */     float zMag = zYaw * pitchCos;
/*     */     
/* 104 */     Direction axisX = xPos ? EAST : WEST;
/* 105 */     Direction axisY = yPos ? UP : DOWN;
/* 106 */     Direction axisZ = zPos ? SOUTH : NORTH;
/*     */     
/* 108 */     if (xYaw > zYaw) {
/* 109 */       if (yMag > xMag)
/* 110 */         return makeDirectionArray(axisY, axisX, axisZ); 
/* 111 */       if (zMag > yMag) {
/* 112 */         return makeDirectionArray(axisX, axisZ, axisY);
/*     */       }
/* 114 */       return makeDirectionArray(axisX, axisY, axisZ);
/*     */     } 
/*     */     
/* 117 */     if (yMag > zMag)
/* 118 */       return makeDirectionArray(axisY, axisZ, axisX); 
/* 119 */     if (xMag > yMag) {
/* 120 */       return makeDirectionArray(axisZ, axisX, axisY);
/*     */     }
/* 122 */     return makeDirectionArray(axisZ, axisY, axisX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   private static Direction[] makeDirectionArray(Direction axis1, Direction axis2, Direction axis3) { return new Direction[] { axis1, axis2, axis3, axis3.getOpposite(), axis2.getOpposite(), axis1.getOpposite() }; }
/*     */ 
/*     */   
/*     */   public static Direction rotate(Matrix4fc matrix, Direction facing) {
/* 132 */     Vector3f vec = matrix.transformDirection(facing.normalVec3f, new Vector3f());
/* 133 */     return getApproximateNearest(vec.x(), vec.y(), vec.z());
/*     */   }
/*     */ 
/*     */   
/* 137 */   public static Collection<Direction> allShuffled(RandomSource random) { return Util.shuffledCopy(values(), random); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static Stream<Direction> stream() { return Stream.of(VALUES); }
/*     */ 
/*     */   
/*     */   public static float getYRot(Direction direction) {
/* 145 */     switch (direction.ordinal()) { case 2: 
/*     */       case 3: 
/*     */       case 4:
/*     */       
/*     */       case 5:
/* 150 */        }  throw new IllegalStateException("No y-Rot for vertical axis: " + String.valueOf(direction));
/*     */   }
/*     */ 
/*     */   
/*     */   public Quaternionf getRotation() {
/* 155 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: case 4: case 5: break; }  return (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       new Quaternionf()).rotationXYZ(1.5707964F, 0.0F, -1.5707964F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public int get3DDataValue() { return this.data3d; }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public int get2DDataValue() { return this.data2d; }
/*     */ 
/*     */ 
/*     */   
/* 174 */   public AxisDirection getAxisDirection() { return this.axisDirection; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Direction getFacingAxis(Entity entity, Axis axis) {
/* 181 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 182 */           EAST.isFacingAngle(entity.getViewYRot(1.0F)) ? EAST : WEST;
/* 183 */       case 2: return SOUTH.isFacingAngle(entity.getViewYRot(1.0F)) ? SOUTH : NORTH;
/* 184 */       case 1: break; }  return (entity.getViewXRot(1.0F) < 0.0F) ? UP : DOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public Direction getOpposite() { return from3DDataValue(this.oppositeIndex); }
/*     */ 
/*     */   
/*     */   public Direction getClockWise(Axis axis) {
/* 193 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 194 */           (this == WEST || this == EAST) ? this : getClockWiseX();
/* 195 */       case 1: return (this == UP || this == DOWN) ? this : getClockWise();
/* 196 */       case 2: break; }  return (this == NORTH || this == SOUTH) ? this : getClockWiseZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getCounterClockWise(Axis axis) {
/* 201 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 202 */           (this == WEST || this == EAST) ? this : getCounterClockWiseX();
/* 203 */       case 1: return (this == UP || this == DOWN) ? this : getCounterClockWise();
/* 204 */       case 2: break; }  return (this == NORTH || this == SOUTH) ? this : getCounterClockWiseZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getClockWise() {
/* 209 */     switch (ordinal()) { case 2: 
/*     */       case 5: 
/*     */       case 3:
/*     */       
/*     */       case 4:
/* 214 */        }  throw new IllegalStateException("Unable to get Y-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */   
/*     */   private Direction getClockWiseX() {
/* 219 */     switch (ordinal()) { case 1: 
/*     */       case 2: 
/*     */       case 0:
/*     */       
/*     */       case 3:
/* 224 */        }  throw new IllegalStateException("Unable to get X-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */   
/*     */   private Direction getCounterClockWiseX() {
/* 229 */     switch (ordinal()) { case 1: 
/*     */       case 3: 
/*     */       case 0:
/*     */       
/*     */       case 2:
/* 234 */        }  throw new IllegalStateException("Unable to get X-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */   
/*     */   private Direction getClockWiseZ() {
/* 239 */     switch (ordinal()) { case 1: 
/*     */       case 5: 
/*     */       case 0:
/*     */       
/*     */       case 4:
/* 244 */        }  throw new IllegalStateException("Unable to get Z-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */   
/*     */   private Direction getCounterClockWiseZ() {
/* 249 */     switch (ordinal()) { case 1: 
/*     */       case 4: 
/*     */       case 0:
/*     */       
/*     */       case 5:
/* 254 */        }  throw new IllegalStateException("Unable to get Z-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getCounterClockWise() {
/* 259 */     switch (ordinal()) { case 2: 
/*     */       case 5: 
/*     */       case 3:
/*     */       
/*     */       case 4:
/* 264 */        }  throw new IllegalStateException("Unable to get CCW facing of " + String.valueOf(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 269 */   public int getStepX() { return this.normal.getX(); }
/*     */ 
/*     */ 
/*     */   
/* 273 */   public int getStepY() { return this.normal.getY(); }
/*     */ 
/*     */ 
/*     */   
/* 277 */   public int getStepZ() { return this.normal.getZ(); }
/*     */ 
/*     */ 
/*     */   
/* 281 */   public Vector3f step() { return new Vector3f(this.normalVec3f); }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public Axis getAxis() { return this.axis; }
/*     */ 
/*     */ 
/*     */   
/* 293 */   public static Direction byName(String name) { return (Direction)CODEC.byName(name); }
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static Direction from3DDataValue(int data) { return BY_3D_DATA[Mth.abs(data % BY_3D_DATA.length)]; }
/*     */ 
/*     */ 
/*     */   
/* 301 */   public static Direction from2DDataValue(int data) { return BY_2D_DATA[Mth.abs(data % BY_2D_DATA.length)]; }
/*     */ 
/*     */ 
/*     */   
/* 305 */   public static Direction fromYRot(double yRot) { return from2DDataValue(Mth.floor(yRot / 90.0D + 0.5D) & 0x3); }
/*     */ 
/*     */   
/*     */   public static Direction fromAxisAndDirection(Axis axis, AxisDirection direction) {
/* 309 */     switch (axis.ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 310 */           (direction == AxisDirection.POSITIVE) ? EAST : WEST;
/* 311 */       case 1: return (direction == AxisDirection.POSITIVE) ? UP : DOWN;
/* 312 */       case 2: break; }  return (direction == AxisDirection.POSITIVE) ? SOUTH : NORTH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public float toYRot() { return ((this.data2d & 0x3) * 90); }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public static Direction getRandom(RandomSource random) { return (Direction)Util.getRandom(VALUES, random); }
/*     */ 
/*     */ 
/*     */   
/* 325 */   public static Direction getApproximateNearest(double dx, double dy, double dz) { return getApproximateNearest((float)dx, (float)dy, (float)dz); }
/*     */ 
/*     */   
/*     */   public static Direction getApproximateNearest(float dx, float dy, float dz) {
/* 329 */     Direction result = NORTH;
/* 330 */     float highestDot = Float.MIN_VALUE;
/* 331 */     for (Direction direction : VALUES) {
/* 332 */       float dot = dx * direction.normal.getX() + dy * direction.normal.getY() + dz * direction.normal.getZ();
/*     */       
/* 334 */       if (dot > highestDot) {
/* 335 */         highestDot = dot;
/* 336 */         result = direction;
/*     */       } 
/*     */     } 
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 343 */   public static Direction getApproximateNearest(Vec3 vec) { return getApproximateNearest(vec.x, vec.y, vec.z); }
/*     */ 
/*     */   
/*     */   @Contract("_,_,_,!null->!null;_,_,_,_->_")
/*     */   public static Direction getNearest(int x, int y, int z, Direction orElse) {
/* 348 */     int absX = Math.abs(x);
/* 349 */     int absY = Math.abs(y);
/* 350 */     int absZ = Math.abs(z);
/* 351 */     if (absX > absZ && absX > absY)
/* 352 */       return (x < 0) ? WEST : EAST; 
/* 353 */     if (absZ > absX && absZ > absY)
/* 354 */       return (z < 0) ? NORTH : SOUTH; 
/* 355 */     if (absY > absX && absY > absZ) {
/* 356 */       return (y < 0) ? DOWN : UP;
/*     */     }
/* 358 */     return orElse;
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,!null->!null;_,_->_")
/* 363 */   public static Direction getNearest(Vec3i vec, Direction orElse) { return getNearest(vec.getX(), vec.getY(), vec.getZ(), orElse); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 368 */   public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 373 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 377 */   private static DataResult<Direction> verifyVertical(Direction v) { return v.getAxis().isVertical() ? DataResult.success(v) : DataResult.error(() -> "Expected a vertical direction"); }
/*     */ 
/*     */   
/*     */   public static Direction get(AxisDirection axisDirection, Axis axis) {
/* 381 */     for (Direction direction : VALUES) {
/* 382 */       if (direction.getAxisDirection() == axisDirection && direction.getAxis() == axis) {
/* 383 */         return direction;
/*     */       }
/*     */     } 
/* 386 */     throw new IllegalArgumentException("No such direction: " + String.valueOf(axisDirection) + " " + String.valueOf(axis));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Axis> axisStepOrder(Vec3 movement) {
/* 390 */     if (Math.abs(movement.x) < Math.abs(movement.z)) {
/* 391 */       return YZX_AXIS_ORDER;
/*     */     }
/* 393 */     return YXZ_AXIS_ORDER;
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
/*     */   public final abstract enum Axis
/*     */     implements Predicate<Direction>, StringRepresentable
/*     */   {
/*     */     X, Y, Z;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final Axis[] VALUES;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final StringRepresentable.EnumCodec<Axis> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/core/Direction$Axis$1
/*     */       //   3: dup
/*     */       //   4: ldc 'X'
/*     */       //   6: iconst_0
/*     */       //   7: ldc 'x'
/*     */       //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   12: putstatic net/minecraft/core/Direction$Axis.X : Lnet/minecraft/core/Direction$Axis;
/*     */       //   15: new net/minecraft/core/Direction$Axis$2
/*     */       //   18: dup
/*     */       //   19: ldc 'Y'
/*     */       //   21: iconst_1
/*     */       //   22: ldc 'y'
/*     */       //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   27: putstatic net/minecraft/core/Direction$Axis.Y : Lnet/minecraft/core/Direction$Axis;
/*     */       //   30: new net/minecraft/core/Direction$Axis$3
/*     */       //   33: dup
/*     */       //   34: ldc 'Z'
/*     */       //   36: iconst_2
/*     */       //   37: ldc 'z'
/*     */       //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   42: putstatic net/minecraft/core/Direction$Axis.Z : Lnet/minecraft/core/Direction$Axis;
/*     */       //   45: invokestatic $values : ()[Lnet/minecraft/core/Direction$Axis;
/*     */       //   48: putstatic net/minecraft/core/Direction$Axis.$VALUES : [Lnet/minecraft/core/Direction$Axis;
/*     */       //   51: invokestatic values : ()[Lnet/minecraft/core/Direction$Axis;
/*     */       //   54: putstatic net/minecraft/core/Direction$Axis.VALUES : [Lnet/minecraft/core/Direction$Axis;
/*     */       //   57: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */       //   62: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   65: putstatic net/minecraft/core/Direction$Axis.CODEC : Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   68: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #397	-> 0
/*     */       //   #423	-> 15
/*     */       //   #449	-> 30
/*     */       //   #396	-> 45
/*     */       //   #477	-> 51
/*     */       //   #479	-> 57
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 484 */     Axis(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */     
/* 488 */     public static Axis byName(String name) { return (Axis)CODEC.byName(name); }
/*     */ 
/*     */ 
/*     */     
/* 492 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 496 */     public boolean isVertical() { return (this == Y); }
/*     */ 
/*     */ 
/*     */     
/* 500 */     public boolean isHorizontal() { return (this == X || this == Z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 508 */     public Direction[] getDirections() { return new Direction[] { getPositive(), getNegative() }; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 513 */     public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 517 */     public static Axis getRandom(RandomSource random) { return (Axis)Util.getRandom(VALUES, random); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     public boolean test(Direction input) { return (input != null && input.getAxis() == this); }
/*     */ 
/*     */     
/*     */     public Direction.Plane getPlane() {
/* 526 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  return 
/*     */         
/* 528 */         Direction.Plane.VERTICAL;
/*     */     }
/*     */     public abstract Direction getPositive();
/*     */     public abstract Direction getNegative();
/*     */     public abstract int choose(int param1Int1, int param1Int2, int param1Int3);
/*     */     public abstract double choose(double param1Double1, double param1Double2, double param1Double3);
/* 534 */     public String getSerializedName() { return this.name; } public abstract boolean choose(boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3);
/*     */   } static enum null { public int choose(int x, int y, int z) { return x; } public boolean choose(boolean x, boolean y, boolean z) { return x; } public double choose(double x, double y, double z) { return x; } public Direction getPositive() { return Direction.EAST; } public Direction getNegative() { return Direction.WEST; } } static enum null {
/*     */     public int choose(int x, int y, int z) { return y; } public double choose(double x, double y, double z) { return y; } public boolean choose(boolean x, boolean y, boolean z) { return y; }
/*     */     public Direction getPositive() { return Direction.UP; }
/*     */     public Direction getNegative() { return Direction.DOWN; }
/*     */   }
/*     */   static enum null { public int choose(int x, int y, int z) { return z; }
/*     */     public double choose(double x, double y, double z) { return z; }
/*     */     public boolean choose(boolean x, boolean y, boolean z) { return z; }
/*     */     public Direction getPositive() { return Direction.SOUTH; }
/*     */     public Direction getNegative() { return Direction.NORTH; } }
/* 545 */   public enum AxisDirection { POSITIVE(1, "Towards positive"),
/* 546 */     NEGATIVE(-1, "Towards negative");
/*     */     
/*     */     private final int step;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     AxisDirection(int step, String name) {
/* 553 */       this.step = step;
/* 554 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/* 558 */     public int getStep() { return this.step; }
/*     */ 
/*     */ 
/*     */     
/* 562 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 567 */     public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 571 */     public AxisDirection opposite() { return (this == POSITIVE) ? NEGATIVE : POSITIVE; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 576 */   public Vec3i getUnitVec3i() { return this.normal; }
/*     */ 
/*     */ 
/*     */   
/* 580 */   public Vec3 getUnitVec3() { return this.normalVec3; }
/*     */ 
/*     */ 
/*     */   
/* 584 */   public Vector3fc getUnitVec3f() { return this.normalVec3f; }
/*     */ 
/*     */   
/*     */   public boolean isFacingAngle(float yAngle) {
/* 588 */     float radians = yAngle * 0.017453292F;
/* 589 */     float dx = -Mth.sin(radians);
/* 590 */     float dz = Mth.cos(radians);
/* 591 */     return (this.normal.getX() * dx + this.normal.getZ() * dz > 0.0F);
/*     */   }
/*     */   
/*     */   public enum Plane implements Predicate<Direction>, Iterable<Direction> {
/* 595 */     HORIZONTAL(new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }),
/* 596 */     VERTICAL(new Direction[] { Direction.UP, Direction.DOWN }, new Direction.Axis[] { Direction.Axis.Y });
/*     */     
/*     */     private final Direction[] faces;
/*     */     
/*     */     private final Direction.Axis[] axis;
/*     */     
/*     */     Plane(Direction[] faces, Axis[] axis) {
/* 603 */       this.faces = faces;
/* 604 */       this.axis = axis;
/*     */     }
/*     */ 
/*     */     
/* 608 */     public Direction getRandomDirection(RandomSource random) { return (Direction)Util.getRandom(this.faces, random); }
/*     */ 
/*     */ 
/*     */     
/* 612 */     public Direction.Axis getRandomAxis(RandomSource random) { return (Direction.Axis)Util.getRandom(this.axis, random); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 617 */     public boolean test(Direction input) { return (input != null && input.getAxis().getPlane() == this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 622 */     public Iterator<Direction> iterator() { return Iterators.forArray(this.faces); }
/*     */ 
/*     */ 
/*     */     
/* 626 */     public Stream<Direction> stream() { return Arrays.stream(this.faces); }
/*     */ 
/*     */ 
/*     */     
/* 630 */     public List<Direction> shuffledCopy(RandomSource random) { return Util.shuffledCopy(this.faces, random); }
/*     */ 
/*     */ 
/*     */     
/* 634 */     public int length() { return this.faces.length; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Direction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */