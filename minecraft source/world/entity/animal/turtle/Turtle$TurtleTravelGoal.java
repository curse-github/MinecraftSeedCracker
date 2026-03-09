/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TurtleTravelGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Turtle turtle;
/*     */   private final double speedModifier;
/*     */   private boolean stuck;
/*     */   
/*     */   TurtleTravelGoal(Turtle turtle, double speedModifier) {
/* 348 */     this.turtle = turtle;
/* 349 */     this.speedModifier = speedModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 354 */   public boolean canUse() { return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 359 */     int xzDist = 512;
/* 360 */     int yDist = 4;
/* 361 */     RandomSource random = Turtle.access$000(this.turtle);
/* 362 */     int xt = random.nextInt(1025) - 512;
/* 363 */     int yt = random.nextInt(9) - 4;
/* 364 */     int zt = random.nextInt(1025) - 512;
/*     */     
/* 366 */     if (yt + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1)) {
/* 367 */       yt = 0;
/*     */     }
/* 369 */     this.turtle.travelPos = BlockPos.containing(xt + this.turtle.getX(), yt + this.turtle.getY(), zt + this.turtle.getZ());
/* 370 */     this.stuck = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 375 */     if (this.turtle.travelPos == null) {
/* 376 */       this.stuck = true;
/*     */       
/*     */       return;
/*     */     } 
/* 380 */     if (this.turtle.getNavigation().isDone()) {
/* 381 */       Vec3 targetPos = Vec3.atBottomCenterOf(this.turtle.travelPos);
/* 382 */       Vec3 nextPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, targetPos, 0.3141592741012573D);
/* 383 */       if (nextPos == null) {
/* 384 */         nextPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, targetPos, 1.5707963705062866D);
/*     */       }
/*     */ 
/*     */       
/* 388 */       if (nextPos != null) {
/* 389 */         int xc = Mth.floor(nextPos.x);
/* 390 */         int zc = Mth.floor(nextPos.z);
/* 391 */         int r = 34;
/* 392 */         if (!this.turtle.level().hasChunksAt(xc - 34, zc - 34, xc + 34, zc + 34)) {
/* 393 */           nextPos = null;
/*     */         }
/*     */       } 
/*     */       
/* 397 */       if (nextPos == null) {
/* 398 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 402 */       this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 408 */   public boolean canContinueToUse() { return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 413 */     this.turtle.travelPos = null;
/* 414 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleTravelGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */