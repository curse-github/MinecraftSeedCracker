/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntPredicate;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.joml.Math;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mth
/*     */ {
/*     */   private static final long UUID_VERSION = 61440L;
/*     */   private static final long UUID_VERSION_TYPE_4 = 16384L;
/*     */   private static final long UUID_VARIANT = -4611686018427387904L;
/*     */   private static final long UUID_VARIANT_2 = -9223372036854775808L;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float HALF_PI = 1.5707964F;
/*     */   public static final float TWO_PI = 6.2831855F;
/*     */   public static final float DEG_TO_RAD = 0.017453292F;
/*     */   public static final float RAD_TO_DEG = 57.295776F;
/*     */   public static final float EPSILON = 1.0E-5F;
/*  34 */   public static final float SQRT_OF_TWO = sqrt(2.0F);
/*     */   
/*  36 */   public static final Vector3f Y_AXIS = new Vector3f(0.0F, 1.0F, 0.0F);
/*  37 */   public static final Vector3f X_AXIS = new Vector3f(1.0F, 0.0F, 0.0F);
/*  38 */   public static final Vector3f Z_AXIS = new Vector3f(0.0F, 0.0F, 1.0F);
/*     */   
/*     */   private static final int SIN_QUANTIZATION = 65536;
/*     */   
/*     */   private static final int SIN_MASK = 65535;
/*     */   private static final int COS_OFFSET = 16384;
/*     */   private static final double SIN_SCALE = 10430.378350470453D;
/*  45 */   private static final float[] SIN = (float[])Util.make(new float[65536], sin -> {
/*  46 */         for (int i = 0; i < sin.length; i++) {
/*  47 */           sin[i] = (float)Math.sin(i / 10430.378350470453D);
/*     */         }
/*     */       });
/*     */   
/*  51 */   private static final RandomSource RANDOM = RandomSource.createThreadSafe();
/*     */ 
/*     */   
/*  54 */   public static float sin(double i) { return SIN[(int)((long)(i * 10430.378350470453D) & 0xFFFFL)]; }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static float cos(double i) { return SIN[(int)((long)(i * 10430.378350470453D + 16384.0D) & 0xFFFFL)]; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static float sqrt(float x) { return (float)Math.sqrt(x); }
/*     */ 
/*     */   
/*     */   public static int floor(float v) {
/*  66 */     int i = (int)v;
/*  67 */     return (v < i) ? (i - 1) : i;
/*     */   }
/*     */   
/*     */   public static int floor(double v) {
/*  71 */     int i = (int)v;
/*  72 */     return (v < i) ? (i - 1) : i;
/*     */   }
/*     */   
/*     */   public static long lfloor(double v) {
/*  76 */     long i = (long)v;
/*  77 */     return (v < i) ? (i - 1L) : i;
/*     */   }
/*     */ 
/*     */   
/*  81 */   public static float abs(float v) { return Math.abs(v); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static int abs(int v) { return Math.abs(v); }
/*     */ 
/*     */   
/*     */   public static int ceil(float v) {
/*  89 */     int i = (int)v;
/*  90 */     return (v > i) ? (i + 1) : i;
/*     */   }
/*     */   
/*     */   public static int ceil(double v) {
/*  94 */     int i = (int)v;
/*  95 */     return (v > i) ? (i + 1) : i;
/*     */   }
/*     */   
/*     */   public static long ceilLong(double v) {
/*  99 */     long l = (long)v;
/* 100 */     return (v > l) ? (l + 1L) : l;
/*     */   }
/*     */ 
/*     */   
/* 104 */   public static int clamp(int value, int min, int max) { return Math.min(Math.max(value, min), max); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static long clamp(long value, long min, long max) { return Math.min(Math.max(value, min), max); }
/*     */ 
/*     */   
/*     */   public static float clamp(float value, float min, float max) {
/* 112 */     if (value < min) {
/* 113 */       return min;
/*     */     }
/* 115 */     return Math.min(value, max);
/*     */   }
/*     */   
/*     */   public static double clamp(double value, double min, double max) {
/* 119 */     if (value < min) {
/* 120 */       return min;
/*     */     }
/* 122 */     return Math.min(value, max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double clampedLerp(double factor, double min, double max) {
/* 130 */     if (factor < 0.0D) {
/* 131 */       return min;
/*     */     }
/* 133 */     if (factor > 1.0D) {
/* 134 */       return max;
/*     */     }
/* 136 */     return lerp(factor, min, max);
/*     */   }
/*     */   
/*     */   public static float clampedLerp(float factor, float min, float max) {
/* 140 */     if (factor < 0.0F) {
/* 141 */       return min;
/*     */     }
/* 143 */     if (factor > 1.0F) {
/* 144 */       return max;
/*     */     }
/* 146 */     return lerp(factor, min, max);
/*     */   }
/*     */ 
/*     */   
/* 150 */   public static int absMax(int a, int b) { return Math.max(Math.abs(a), Math.abs(b)); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static float absMax(float a, float b) { return Math.max(Math.abs(a), Math.abs(b)); }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public static double absMax(double a, double b) { return Math.max(Math.abs(a), Math.abs(b)); }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static int chessboardDistance(int x0, int z0, int x1, int z1) { return absMax(x1 - x0, z1 - z0); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public static int floorDiv(int a, int b) { return Math.floorDiv(a, b); }
/*     */ 
/*     */   
/*     */   public static int nextInt(RandomSource random, int minInclusive, int maxInclusive) {
/* 170 */     if (minInclusive >= maxInclusive) {
/* 171 */       return minInclusive;
/*     */     }
/* 173 */     return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
/*     */   }
/*     */   
/*     */   public static float nextFloat(RandomSource random, float min, float max) {
/* 177 */     if (min >= max) {
/* 178 */       return min;
/*     */     }
/* 180 */     return random.nextFloat() * (max - min) + min;
/*     */   }
/*     */   
/*     */   public static double nextDouble(RandomSource random, double min, double max) {
/* 184 */     if (min >= max) {
/* 185 */       return min;
/*     */     }
/* 187 */     return random.nextDouble() * (max - min) + min;
/*     */   }
/*     */ 
/*     */   
/* 191 */   public static boolean equal(float a, float b) { return (Math.abs(b - a) < 1.0E-5F); }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public static boolean equal(double a, double b) { return (Math.abs(b - a) < 9.999999747378752E-6D); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public static int positiveModulo(int input, int mod) { return Math.floorMod(input, mod); }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public static float positiveModulo(float input, float mod) { return (input % mod + mod) % mod; }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public static double positiveModulo(double input, double mod) { return (input % mod + mod) % mod; }
/*     */ 
/*     */ 
/*     */   
/* 211 */   public static boolean isMultipleOf(int dividend, int divisor) { return (dividend % divisor == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static byte packDegrees(float angle) { return (byte)floor(angle * 256.0F / 360.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   public static float unpackDegrees(byte rot) { return (rot * 360) / 256.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int wrapDegrees(int angle) {
/* 232 */     int normalizedAngle = angle % 360;
/* 233 */     if (normalizedAngle >= 180) {
/* 234 */       normalizedAngle -= 360;
/*     */     }
/* 236 */     if (normalizedAngle < -180) {
/* 237 */       normalizedAngle += 360;
/*     */     }
/* 239 */     return normalizedAngle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float wrapDegrees(long angle) {
/* 246 */     float normalizedAngle = (float)(angle % 360L);
/* 247 */     if (normalizedAngle >= 180.0F) {
/* 248 */       normalizedAngle -= 360.0F;
/*     */     }
/* 250 */     if (normalizedAngle < -180.0F) {
/* 251 */       normalizedAngle += 360.0F;
/*     */     }
/* 253 */     return normalizedAngle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float wrapDegrees(float angle) {
/* 260 */     float normalizedAngle = angle % 360.0F;
/* 261 */     if (normalizedAngle >= 180.0F) {
/* 262 */       normalizedAngle -= 360.0F;
/*     */     }
/* 264 */     if (normalizedAngle < -180.0F) {
/* 265 */       normalizedAngle += 360.0F;
/*     */     }
/* 267 */     return normalizedAngle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double wrapDegrees(double angle) {
/* 274 */     double normalizedAngle = angle % 360.0D;
/* 275 */     if (normalizedAngle >= 180.0D) {
/* 276 */       normalizedAngle -= 360.0D;
/*     */     }
/* 278 */     if (normalizedAngle < -180.0D) {
/* 279 */       normalizedAngle += 360.0D;
/*     */     }
/* 281 */     return normalizedAngle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static float degreesDifference(float fromAngle, float toAngle) { return wrapDegrees(toAngle - fromAngle); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static float degreesDifferenceAbs(float angleA, float angleB) { return abs(degreesDifference(angleA, angleB)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float rotateIfNecessary(float baseAngle, float targetAngle, float maxAngleDiff) {
/* 306 */     float deltaAngle = degreesDifference(baseAngle, targetAngle);
/* 307 */     float deltaAngleClamped = clamp(deltaAngle, -maxAngleDiff, maxAngleDiff);
/* 308 */     return targetAngle - deltaAngleClamped;
/*     */   }
/*     */   
/*     */   public static float approach(float current, float target, float increment) {
/* 312 */     increment = abs(increment);
/*     */     
/* 314 */     if (current < target) {
/* 315 */       return clamp(current + increment, current, target);
/*     */     }
/* 317 */     return clamp(current - increment, target, current);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float approachDegrees(float current, float target, float increment) {
/* 322 */     float difference = degreesDifference(current, target);
/* 323 */     return approach(current, current + difference, increment);
/*     */   }
/*     */ 
/*     */   
/* 327 */   public static int getInt(String input, int def) { return NumberUtils.toInt(input, def); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int smallestEncompassingPowerOfTwo(int input) {
/* 332 */     int result = input - 1;
/* 333 */     result |= result >> 1;
/* 334 */     result |= result >> 2;
/* 335 */     result |= result >> 4;
/* 336 */     result |= result >> 8;
/* 337 */     result |= result >> 16;
/* 338 */     return result + 1;
/*     */   }
/*     */   
/*     */   public static int smallestSquareSide(int itemCount) {
/* 342 */     if (itemCount < 0) {
/* 343 */       throw new IllegalArgumentException("itemCount must be greater than or equal to zero");
/*     */     }
/* 345 */     return ceil(Math.sqrt(itemCount));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 350 */   public static boolean isPowerOfTwo(int input) { return (input != 0 && (input & input - 1) == 0); }
/*     */ 
/*     */   
/*     */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = { 
/* 354 */       0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 }; private static final double ONE_SIXTH = 0.16666666666666666D;
/*     */   private static final int FRAC_EXP = 8;
/*     */   private static final int LUT_SIZE = 257;
/*     */   
/*     */   public static int ceillog2(int input) {
/* 359 */     input = isPowerOfTwo(input) ? input : smallestEncompassingPowerOfTwo(input);
/* 360 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(input * 125613361L >> 27) & 0x1F];
/*     */   }
/*     */ 
/*     */   
/* 364 */   public static int log2(int input) { return ceillog2(input) - (isPowerOfTwo(input) ? 0 : 1); }
/*     */ 
/*     */ 
/*     */   
/* 368 */   public static float frac(float num) { return num - floor(num); }
/*     */ 
/*     */ 
/*     */   
/* 372 */   public static double frac(double num) { return num - lfloor(num); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 377 */   public static long getSeed(Vec3i vec) { return getSeed(vec.getX(), vec.getY(), vec.getZ()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long getSeed(int x, int y, int z) {
/* 387 */     long seed = (x * 3129871) ^ z * 116129781L ^ y;
/* 388 */     seed = seed * seed * 42317861L + seed * 11L;
/* 389 */     return seed >> 16;
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID(RandomSource random) {
/* 393 */     long most = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
/* 394 */     long least = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Float.MIN_VALUE;
/* 395 */     return new UUID(most, least);
/*     */   }
/*     */ 
/*     */   
/* 399 */   public static UUID createInsecureUUID() { return createInsecureUUID(RANDOM); }
/*     */ 
/*     */ 
/*     */   
/* 403 */   public static double inverseLerp(double value, double min, double max) { return (value - min) / (max - min); }
/*     */ 
/*     */ 
/*     */   
/* 407 */   public static float inverseLerp(float value, float min, float max) { return (value - min) / (max - min); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean rayIntersectsAABB(Vec3 rayStart, Vec3 rayDir, AABB aabb) {
/* 412 */     double centerX = (aabb.minX + aabb.maxX) * 0.5D;
/* 413 */     double boxExtentX = (aabb.maxX - aabb.minX) * 0.5D;
/* 414 */     double diffX = rayStart.x - centerX;
/* 415 */     if (Math.abs(diffX) > boxExtentX && diffX * rayDir.x >= 0.0D) {
/* 416 */       return false;
/*     */     }
/*     */     
/* 419 */     double centerY = (aabb.minY + aabb.maxY) * 0.5D;
/* 420 */     double boxExtentY = (aabb.maxY - aabb.minY) * 0.5D;
/* 421 */     double diffY = rayStart.y - centerY;
/* 422 */     if (Math.abs(diffY) > boxExtentY && diffY * rayDir.y >= 0.0D) {
/* 423 */       return false;
/*     */     }
/*     */     
/* 426 */     double centerZ = (aabb.minZ + aabb.maxZ) * 0.5D;
/* 427 */     double boxExtentZ = (aabb.maxZ - aabb.minZ) * 0.5D;
/* 428 */     double diffZ = rayStart.z - centerZ;
/* 429 */     if (Math.abs(diffZ) > boxExtentZ && diffZ * rayDir.z >= 0.0D) {
/* 430 */       return false;
/*     */     }
/*     */     
/* 433 */     double andrewWooDiffX = Math.abs(rayDir.x);
/* 434 */     double andrewWooDiffY = Math.abs(rayDir.y);
/* 435 */     double andrewWooDiffZ = Math.abs(rayDir.z);
/*     */     
/* 437 */     double f = rayDir.y * diffZ - rayDir.z * diffY;
/* 438 */     if (Math.abs(f) > boxExtentY * andrewWooDiffZ + boxExtentZ * andrewWooDiffY) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     f = rayDir.z * diffX - rayDir.x * diffZ;
/* 443 */     if (Math.abs(f) > boxExtentX * andrewWooDiffZ + boxExtentZ * andrewWooDiffX) {
/* 444 */       return false;
/*     */     }
/*     */     
/* 447 */     f = rayDir.x * diffY - rayDir.y * diffX;
/*     */     
/* 449 */     return (Math.abs(f) < boxExtentX * andrewWooDiffY + boxExtentY * andrewWooDiffX);
/*     */   }
/*     */   
/*     */   public static double atan2(double y, double x) {
/* 453 */     double d2 = x * x + y * y;
/*     */ 
/*     */     
/* 456 */     if (Double.isNaN(d2)) {
/* 457 */       return NaND;
/*     */     }
/*     */ 
/*     */     
/* 461 */     boolean negY = (y < 0.0D);
/* 462 */     if (negY) {
/* 463 */       y = -y;
/*     */     }
/* 465 */     boolean negX = (x < 0.0D);
/* 466 */     if (negX) {
/* 467 */       x = -x;
/*     */     }
/* 469 */     boolean steep = (y > x);
/* 470 */     if (steep) {
/* 471 */       double t = x;
/*     */       
/* 473 */       x = y;
/* 474 */       y = t;
/*     */     } 
/*     */ 
/*     */     
/* 478 */     double rinv = fastInvSqrt(d2);
/* 479 */     x *= rinv;
/* 480 */     y *= rinv;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     double yp = FRAC_BIAS + y;
/* 490 */     int index = (int)Double.doubleToRawLongBits(yp);
/*     */ 
/*     */     
/* 493 */     double phi = ASIN_TAB[index];
/* 494 */     double cPhi = COS_TAB[index];
/*     */ 
/*     */ 
/*     */     
/* 498 */     double sPhi = yp - FRAC_BIAS;
/* 499 */     double sd = y * cPhi - x * sPhi;
/*     */ 
/*     */     
/* 502 */     double d = (6.0D + sd * sd) * sd * 0.16666666666666666D;
/* 503 */     double theta = phi + d;
/*     */ 
/*     */     
/* 506 */     if (steep) {
/* 507 */       theta = 1.5707963267948966D - theta;
/*     */     }
/* 509 */     if (negX) {
/* 510 */       theta = Math.PI - theta;
/*     */     }
/* 512 */     if (negY) {
/* 513 */       theta = -theta;
/*     */     }
/*     */     
/* 516 */     return theta;
/*     */   }
/*     */ 
/*     */   
/* 520 */   public static float invSqrt(float x) { return Math.invsqrt(x); }
/*     */ 
/*     */ 
/*     */   
/* 524 */   public static double invSqrt(double x) { return Math.invsqrt(x); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static double fastInvSqrt(double x) {
/* 535 */     double xhalf = 0.5D * x;
/* 536 */     long i = Double.doubleToRawLongBits(x);
/* 537 */     i = 6910469410427058090L - (i >> true);
/* 538 */     x = Double.longBitsToDouble(i);
/* 539 */     return 1.5D - xhalf * x * x;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float fastInvCubeRoot(float x) {
/* 544 */     int i = Float.floatToIntBits(x);
/* 545 */     i = 1419967116 - i / 3;
/* 546 */     y = Float.intBitsToFloat(i);
/* 547 */     y = 0.6666667F * y + 1.0F / 3.0F * y * y * x;
/* 548 */     return 0.6666667F * y + 1.0F / 3.0F * y * y * x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 555 */   private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
/* 556 */   private static final double[] ASIN_TAB = new double[257];
/* 557 */   private static final double[] COS_TAB = new double[257];
/*     */ 
/*     */   
/*     */   static  {
/* 561 */     for (ind = 0; ind < 257; ind++) {
/* 562 */       double v = ind / 256.0D;
/* 563 */       double asinv = Math.asin(v);
/* 564 */       COS_TAB[ind] = Math.cos(asinv);
/* 565 */       ASIN_TAB[ind] = asinv;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 570 */   public static int hsvToRgb(float hue, float saturation, float value) { return hsvToArgb(hue, saturation, value, 0); }
/*     */   
/*     */   public static int hsvToArgb(float hue, float saturation, float value, int alpha) {
/*     */     float blue, blue, blue, blue, blue, blue, green, green, green, green, green, green, red, red, red, red, red, red;
/* 574 */     int h = (int)(hue * 6.0F) % 6;
/* 575 */     float f = hue * 6.0F - h;
/* 576 */     float p = value * (1.0F - saturation);
/* 577 */     float q = value * (1.0F - f * saturation);
/* 578 */     float t = value * (1.0F - (1.0F - f) * saturation);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 584 */     switch (h) {
/*     */       case 0:
/* 586 */         red = value;
/* 587 */         green = t;
/* 588 */         blue = p;
/*     */         break;
/*     */       case 1:
/* 591 */         red = q;
/* 592 */         green = value;
/* 593 */         blue = p;
/*     */         break;
/*     */       case 2:
/* 596 */         red = p;
/* 597 */         green = value;
/* 598 */         blue = t;
/*     */         break;
/*     */       case 3:
/* 601 */         red = p;
/* 602 */         green = q;
/* 603 */         blue = value;
/*     */         break;
/*     */       case 4:
/* 606 */         red = t;
/* 607 */         green = p;
/* 608 */         blue = value;
/*     */         break;
/*     */       case 5:
/* 611 */         red = value;
/* 612 */         green = p;
/* 613 */         blue = q;
/*     */         break;
/*     */       default:
/* 616 */         throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
/*     */     } 
/*     */     
/* 619 */     return ARGB.color(alpha, 
/* 620 */         clamp((int)(red * 255.0F), 0, 255), 
/* 621 */         clamp((int)(green * 255.0F), 0, 255), 
/* 622 */         clamp((int)(blue * 255.0F), 0, 255));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int murmurHash3Mixer(int hash) {
/* 628 */     hash ^= hash >>> 16;
/* 629 */     hash *= -2048144789;
/* 630 */     hash ^= hash >>> 13;
/* 631 */     hash *= -1028477387;
/* 632 */     return hash >>> 16;
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
/*     */   public static int binarySearch(int from, int to, IntPredicate condition) {
/* 649 */     int len = to - from;
/* 650 */     while (len > 0) {
/* 651 */       int half = len / 2;
/* 652 */       int middle = from + half;
/* 653 */       if (condition.test(middle)) {
/*     */         
/* 655 */         len = half; continue;
/*     */       } 
/* 657 */       from = middle + 1;
/* 658 */       len -= half + 1;
/*     */     } 
/*     */     
/* 661 */     return from;
/*     */   }
/*     */ 
/*     */   
/* 665 */   public static int lerpInt(float alpha1, int p0, int p1) { return p0 + floor(alpha1 * (p1 - p0)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int lerpDiscrete(float alpha1, int p0, int p1) {
/* 670 */     int delta = p1 - p0;
/* 671 */     return p0 + floor(alpha1 * (delta - 1)) + ((alpha1 > 0.0F) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/* 675 */   public static float lerp(float alpha1, float p0, float p1) { return p0 + alpha1 * (p1 - p0); }
/*     */ 
/*     */   
/*     */   public static Vec3 lerp(double alpha, Vec3 p1, Vec3 p2) {
/* 679 */     return new Vec3(lerp(alpha, p1.x, p2.x), 
/* 680 */         lerp(alpha, p1.y, p2.y), 
/* 681 */         lerp(alpha, p1.z, p2.z));
/*     */   }
/*     */ 
/*     */   
/* 685 */   public static double lerp(double alpha1, double p0, double p1) { return p0 + alpha1 * (p1 - p0); }
/*     */ 
/*     */   
/*     */   public static double lerp2(double alpha1, double alpha2, double x00, double x10, double x01, double x11) {
/* 689 */     return lerp(alpha2, 
/*     */         
/* 691 */         lerp(alpha1, x00, x10), 
/* 692 */         lerp(alpha1, x01, x11));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double lerp3(double alpha1, double alpha2, double alpha3, double x000, double x100, double x010, double x110, double x001, double x101, double x011, double x111) {
/* 701 */     return lerp(alpha3, 
/*     */         
/* 703 */         lerp2(alpha1, alpha2, x000, x100, x010, x110), 
/* 704 */         lerp2(alpha1, alpha2, x001, x101, x011, x111));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 709 */   public static float catmullrom(float alpha, float p0, float p1, float p2, float p3) { return 0.5F * (2.0F * p1 + (p2 - p0) * alpha + (2.0F * p0 - 5.0F * p1 + 4.0F * p2 - p3) * alpha * alpha + (3.0F * p1 - p0 - 3.0F * p2 + p3) * alpha * alpha * alpha); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 722 */   public static double smoothstep(double x) { return x * x * x * (x * (x * 6.0D - 15.0D) + 10.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 727 */   public static double smoothstepDerivative(double x) { return 30.0D * x * x * (x - 1.0D) * (x - 1.0D); }
/*     */ 
/*     */   
/*     */   public static int sign(double number) {
/* 731 */     if (number == 0.0D) {
/* 732 */       return 0;
/*     */     }
/* 734 */     return (number > 0.0D) ? 1 : -1;
/*     */   }
/*     */ 
/*     */   
/* 738 */   public static float rotLerp(float a, float from, float to) { return from + a * wrapDegrees(to - from); }
/*     */ 
/*     */ 
/*     */   
/* 742 */   public static double rotLerp(double a, double from, double to) { return from + a * wrapDegrees(to - from); }
/*     */ 
/*     */   
/*     */   public static float rotLerpRad(float a, float from, float to) {
/* 746 */     float diff = to - from;
/* 747 */     while (diff < -3.1415927F) {
/* 748 */       diff += 6.2831855F;
/*     */     }
/* 750 */     while (diff >= 3.1415927F) {
/* 751 */       diff -= 6.2831855F;
/*     */     }
/* 753 */     return from + a * diff;
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
/* 765 */   public static float triangleWave(float index, float period) { return (Math.abs(index % period - period * 0.5F) - period * 0.25F) / period * 0.25F; }
/*     */ 
/*     */ 
/*     */   
/* 769 */   public static float square(float x) { return x * x; }
/*     */ 
/*     */ 
/*     */   
/* 773 */   public static float cube(float x) { return x * x * x; }
/*     */ 
/*     */ 
/*     */   
/* 777 */   public static double square(double x) { return x * x; }
/*     */ 
/*     */ 
/*     */   
/* 781 */   public static int square(int x) { return x * x; }
/*     */ 
/*     */ 
/*     */   
/* 785 */   public static long square(long x) { return x * x; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 793 */   public static double clampedMap(double value, double fromMin, double fromMax, double toMin, double toMax) { return clampedLerp(inverseLerp(value, fromMin, fromMax), toMin, toMax); }
/*     */ 
/*     */ 
/*     */   
/* 797 */   public static float clampedMap(float value, float fromMin, float fromMax, float toMin, float toMax) { return clampedLerp(inverseLerp(value, fromMin, fromMax), toMin, toMax); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 804 */   public static double map(double value, double fromMin, double fromMax, double toMin, double toMax) { return lerp(inverseLerp(value, fromMin, fromMax), toMin, toMax); }
/*     */ 
/*     */ 
/*     */   
/* 808 */   public static float map(float value, float fromMin, float fromMax, float toMin, float toMax) { return lerp(inverseLerp(value, fromMin, fromMax), toMin, toMax); }
/*     */ 
/*     */ 
/*     */   
/* 812 */   public static double wobble(double coord) { return coord + (2.0D * RandomSource.create(floor(coord * 3000.0D)).nextDouble() - 1.0D) * 1.0E-7D / 2.0D; }
/*     */ 
/*     */ 
/*     */   
/* 816 */   public static int roundToward(int input, int multiple) { return positiveCeilDiv(input, multiple) * multiple; }
/*     */ 
/*     */ 
/*     */   
/* 820 */   public static int positiveCeilDiv(int input, int divisor) { return -Math.floorDiv(-input, divisor); }
/*     */ 
/*     */ 
/*     */   
/* 824 */   public static int randomBetweenInclusive(RandomSource random, int min, int maxInclusive) { return random.nextInt(maxInclusive - min + 1) + min; }
/*     */ 
/*     */ 
/*     */   
/* 828 */   public static float randomBetween(RandomSource random, float min, float maxExclusive) { return random.nextFloat() * (maxExclusive - min) + min; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 835 */   public static float normal(RandomSource random, float mean, float deviation) { return mean + (float)random.nextGaussian() * deviation; }
/*     */ 
/*     */ 
/*     */   
/* 839 */   public static double lengthSquared(double x, double y) { return x * x + y * y; }
/*     */ 
/*     */ 
/*     */   
/* 843 */   public static double length(double x, double y) { return Math.sqrt(lengthSquared(x, y)); }
/*     */ 
/*     */ 
/*     */   
/* 847 */   public static float length(float x, float y) { return (float)Math.sqrt(lengthSquared(x, y)); }
/*     */ 
/*     */ 
/*     */   
/* 851 */   public static double lengthSquared(double x, double y, double z) { return x * x + y * y + z * z; }
/*     */ 
/*     */ 
/*     */   
/* 855 */   public static double length(double x, double y, double z) { return Math.sqrt(lengthSquared(x, y, z)); }
/*     */ 
/*     */ 
/*     */   
/* 859 */   public static float lengthSquared(float x, float y, float z) { return x * x + y * y + z * z; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 866 */   public static int quantize(double value, int quantizeResolution) { return floor(value / quantizeResolution) * quantizeResolution; }
/*     */ 
/*     */ 
/*     */   
/* 870 */   public static IntStream outFromOrigin(int origin, int lowerBound, int upperBound) { return outFromOrigin(origin, lowerBound, upperBound, 1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntStream outFromOrigin(int origin, int lowerBound, int upperBound, int stepSize) {
/* 878 */     if (lowerBound > upperBound) {
/* 879 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "upperBound %d expected to be > lowerBound %d", new Object[] { Integer.valueOf(upperBound), Integer.valueOf(lowerBound) }));
/*     */     }
/*     */     
/* 882 */     if (stepSize < 1) {
/* 883 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "step size expected to be >= 1, was %d", new Object[] { Integer.valueOf(stepSize) }));
/*     */     }
/*     */     
/* 886 */     int clampedOrigin = clamp(origin, lowerBound, upperBound);
/* 887 */     return IntStream.iterate(clampedOrigin, cursor -> {
/* 888 */           int currentDistance = Math.abs(clampedOrigin - cursor);
/* 889 */           return (clampedOrigin - currentDistance >= lowerBound || clampedOrigin + currentDistance <= upperBound);
/*     */         }cursor -> {
/* 891 */           boolean previousWasNegative = (cursor <= clampedOrigin);
/* 892 */           int currentDistance = Math.abs(clampedOrigin - cursor);
/* 893 */           boolean canMovePositive = (clampedOrigin + currentDistance + stepSize <= upperBound);
/*     */           
/* 895 */           if (!previousWasNegative || !canMovePositive) {
/* 896 */             int attemptedStep = clampedOrigin - currentDistance - (previousWasNegative ? stepSize : 0);
/* 897 */             if (attemptedStep >= lowerBound) {
/* 898 */               return attemptedStep;
/*     */             }
/*     */           } 
/*     */           
/* 902 */           return clampedOrigin + currentDistance + stepSize;
/*     */         });
/*     */   }
/*     */   
/*     */   public static Quaternionf rotationAroundAxis(Vector3f axis, Quaternionf rotation, Quaternionf result) {
/* 907 */     float projectedLength = axis.dot(rotation.x, rotation.y, rotation.z);
/* 908 */     return result.set(axis.x * projectedLength, axis.y * projectedLength, axis.z * projectedLength, rotation.w).normalize();
/*     */   }
/*     */ 
/*     */   
/* 912 */   public static int mulAndTruncate(Fraction fraction, int factor) { return fraction.getNumerator() * factor / fraction.getDenominator(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Mth.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */