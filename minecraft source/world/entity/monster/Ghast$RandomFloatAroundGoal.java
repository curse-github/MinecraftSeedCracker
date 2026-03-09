/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
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
/*     */ public class RandomFloatAroundGoal
/*     */   extends Goal
/*     */ {
/*     */   private static final int MAX_ATTEMPTS = 64;
/*     */   private final Mob ghast;
/*     */   private final int distanceToBlocks;
/*     */   
/* 300 */   public RandomFloatAroundGoal(Mob ghast) { this(ghast, 0); }
/*     */ 
/*     */   
/*     */   public RandomFloatAroundGoal(Mob ghast, int distanceToBlocks) {
/* 304 */     this.ghast = ghast;
/* 305 */     this.distanceToBlocks = distanceToBlocks;
/* 306 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 311 */     MoveControl moveControl = this.ghast.getMoveControl();
/* 312 */     if (!moveControl.hasWanted()) {
/* 313 */       return true;
/*     */     }
/*     */     
/* 316 */     double xd = moveControl.getWantedX() - this.ghast.getX();
/* 317 */     double yd = moveControl.getWantedY() - this.ghast.getY();
/* 318 */     double zd = moveControl.getWantedZ() - this.ghast.getZ();
/*     */     
/* 320 */     double dd = xd * xd + yd * yd + zd * zd;
/*     */     
/* 322 */     return (dd < 1.0D || dd > 3600.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 327 */   public boolean canContinueToUse() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 332 */     Vec3 result = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 333 */     this.ghast.getMoveControl().setWantedPosition(result.x(), result.y(), result.z(), 1.0D);
/*     */   }
/*     */   
/*     */   public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
/* 337 */     Level level = mob.level();
/* 338 */     RandomSource random = mob.getRandom();
/* 339 */     Vec3 center = mob.position();
/* 340 */     Vec3 result = null;
/* 341 */     for (int i = 0; i < 64; i++) {
/* 342 */       result = chooseRandomPositionWithRestriction(mob, center, random);
/* 343 */       if (result != null && isGoodTarget(level, result, distanceToBlocks)) {
/* 344 */         return result;
/*     */       }
/*     */     } 
/* 347 */     if (result == null) {
/* 348 */       result = chooseRandomPosition(center, random);
/*     */     }
/*     */ 
/*     */     
/* 352 */     BlockPos pos = BlockPos.containing(result);
/* 353 */     int heightY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
/* 354 */     if (heightY < pos.getY() && heightY > level.getMinY()) {
/* 355 */       result = new Vec3(result.x(), mob.getY() - Math.abs(mob.getY() - result.y()), result.z());
/*     */     }
/* 357 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
/* 361 */     if (distanceToBlocks <= 0) {
/* 362 */       return true;
/*     */     }
/* 364 */     BlockPos pos = BlockPos.containing(target);
/* 365 */     if (!level.getBlockState(pos).isAir()) {
/* 366 */       return false;
/*     */     }
/*     */     
/* 369 */     for (Direction dir : Direction.values()) {
/* 370 */       for (int i = 1; i < distanceToBlocks; i++) {
/* 371 */         BlockPos offset = pos.relative(dir, i);
/* 372 */         if (!level.getBlockState(offset).isAir()) {
/* 373 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 377 */     return false;
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPosition(Vec3 center, RandomSource random) {
/* 381 */     double xTarget = center.x() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */     double yTarget = center.y() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 383 */     double zTarget = center.z() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 384 */     return new Vec3(xTarget, yTarget, zTarget);
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 center, RandomSource random) {
/* 388 */     Vec3 target = chooseRandomPosition(center, random);
/* 389 */     if (mob.hasHome() && !mob.isWithinHome(target)) {
/* 390 */       return null;
/*     */     }
/* 392 */     return target;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Ghast$RandomFloatAroundGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */