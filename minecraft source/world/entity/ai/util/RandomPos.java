/*     */ package net.minecraft.world.entity.ai.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RandomPos
/*     */ {
/*     */   private static final int RANDOM_POS_ATTEMPTS = 10;
/*     */   
/*     */   public static BlockPos generateRandomDirection(RandomSource random, int horizontalDist, int verticalDist) {
/*  20 */     int xt = random.nextInt(2 * horizontalDist + 1) - horizontalDist;
/*  21 */     int yt = random.nextInt(2 * verticalDist + 1) - verticalDist;
/*  22 */     int zt = random.nextInt(2 * horizontalDist + 1) - horizontalDist;
/*     */     
/*  24 */     return new BlockPos(xt, yt, zt);
/*     */   }
/*     */   
/*     */   public static BlockPos generateRandomDirectionWithinRadians(RandomSource random, double minHorizontalDist, double maxHorizontalDist, int verticalDist, int flyingHeight, double xDir, double zDir, double maxXzRadiansFromDir) {
/*  28 */     double yRadiansCenter = Mth.atan2(zDir, xDir) - 1.5707963705062866D;
/*  29 */     double yRadians = yRadiansCenter + (2.0F * random.nextFloat() - 1.0F) * maxXzRadiansFromDir;
/*  30 */     double dist = Mth.lerp(Math.sqrt(random.nextDouble()), minHorizontalDist, maxHorizontalDist) * Mth.SQRT_OF_TWO;
/*  31 */     double xt = -dist * Math.sin(yRadians);
/*  32 */     double zt = dist * Math.cos(yRadians);
/*     */     
/*  34 */     if (Math.abs(xt) > maxHorizontalDist || Math.abs(zt) > maxHorizontalDist) {
/*  35 */       return null;
/*     */     }
/*     */     
/*  38 */     int yt = random.nextInt(2 * verticalDist + 1) - verticalDist + flyingHeight;
/*  39 */     return BlockPos.containing(xt, yt, zt);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static BlockPos moveUpOutOfSolid(BlockPos pos, int maxY, Predicate<BlockPos> solidityTester) {
/*  44 */     if (solidityTester.test(pos)) {
/*     */       
/*  46 */       BlockPos.MutableBlockPos onGroundPos = pos.mutable().move(Direction.UP);
/*  47 */       while (onGroundPos.getY() <= maxY && solidityTester.test(onGroundPos)) {
/*  48 */         onGroundPos.move(Direction.UP);
/*     */       }
/*     */       
/*  51 */       return onGroundPos.immutable();
/*     */     } 
/*     */     
/*  54 */     return pos;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static BlockPos moveUpToAboveSolid(BlockPos pos, int aboveSolidAmount, int maxY, Predicate<BlockPos> solidityTester) {
/*  59 */     if (aboveSolidAmount < 0) {
/*  60 */       throw new IllegalArgumentException("aboveSolidAmount was " + aboveSolidAmount + ", expected >= 0");
/*     */     }
/*     */     
/*  63 */     if (solidityTester.test(pos)) {
/*     */       
/*  65 */       BlockPos.MutableBlockPos mutablePos = pos.mutable().move(Direction.UP);
/*  66 */       while (mutablePos.getY() <= maxY && solidityTester.test(mutablePos)) {
/*  67 */         mutablePos.move(Direction.UP);
/*     */       }
/*  69 */       int firstNonSolidY = mutablePos.getY();
/*     */       
/*  71 */       while (mutablePos.getY() <= maxY && mutablePos.getY() - firstNonSolidY < aboveSolidAmount) {
/*  72 */         mutablePos.move(Direction.UP);
/*  73 */         if (solidityTester.test(mutablePos)) {
/*  74 */           mutablePos.move(Direction.DOWN);
/*     */           break;
/*     */         } 
/*     */       } 
/*  78 */       return mutablePos.immutable();
/*     */     } 
/*     */     
/*  81 */     return pos;
/*     */   }
/*     */ 
/*     */   
/*  85 */   public static Vec3 generateRandomPos(PathfinderMob mob, Supplier<BlockPos> posSupplier) { Objects.requireNonNull(mob); return generateRandomPos(posSupplier, mob::getWalkTargetValue); }
/*     */ 
/*     */   
/*     */   public static Vec3 generateRandomPos(Supplier<BlockPos> posSupplier, ToDoubleFunction<BlockPos> positionWeightFunction) {
/*  89 */     double bestWeight = Double.NEGATIVE_INFINITY;
/*  90 */     BlockPos bestPos = null;
/*     */     
/*  92 */     for (int i = 0; i < 10; i++) {
/*  93 */       BlockPos pos = (BlockPos)posSupplier.get();
/*  94 */       if (pos != null) {
/*     */ 
/*     */ 
/*     */         
/*  98 */         double value = positionWeightFunction.applyAsDouble(pos);
/*  99 */         if (value > bestWeight) {
/* 100 */           bestWeight = value;
/* 101 */           bestPos = pos;
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     return (bestPos != null) ? Vec3.atBottomCenterOf(bestPos) : null;
/*     */   }
/*     */   
/*     */   public static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, double xzDist, RandomSource random, BlockPos direction) {
/* 109 */     double xt = direction.getX();
/* 110 */     double zt = direction.getZ();
/*     */     
/* 112 */     if (mob.hasHome() && xzDist > 1.0D) {
/* 113 */       BlockPos center = mob.getHomePosition();
/*     */       
/* 115 */       if (mob.getX() > center.getX()) {
/* 116 */         xt -= random.nextDouble() * xzDist / 2.0D;
/*     */       } else {
/* 118 */         xt += random.nextDouble() * xzDist / 2.0D;
/*     */       } 
/*     */       
/* 121 */       if (mob.getZ() > center.getZ()) {
/* 122 */         zt -= random.nextDouble() * xzDist / 2.0D;
/*     */       } else {
/* 124 */         zt += random.nextDouble() * xzDist / 2.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return BlockPos.containing(xt + mob.getX(), direction.getY() + mob.getY(), zt + mob.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\a\\util\RandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */