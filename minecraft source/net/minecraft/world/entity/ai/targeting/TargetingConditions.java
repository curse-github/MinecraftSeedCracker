/*    */ package net.minecraft.world.entity.ai.targeting;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class TargetingConditions
/*    */ {
/* 10 */   public static final TargetingConditions DEFAULT = forCombat(); private static final double MIN_VISIBILITY_DISTANCE_FOR_INVISIBLE_TARGET = 2.0D; private final boolean isCombat;
/*    */   private double range;
/*    */   
/*    */   private TargetingConditions(boolean isCombat) {
/* 14 */     this.range = -1.0D;
/* 15 */     this.checkLineOfSight = true;
/* 16 */     this.testInvisible = true;
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.isCombat = isCombat;
/*    */   }
/*    */   private boolean checkLineOfSight; private boolean testInvisible; private Selector selector;
/*    */   
/* 24 */   public static TargetingConditions forCombat() { return new TargetingConditions(true); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static TargetingConditions forNonCombat() { return new TargetingConditions(false); }
/*    */ 
/*    */   
/*    */   public TargetingConditions copy() {
/* 32 */     TargetingConditions clone = this.isCombat ? forCombat() : forNonCombat();
/* 33 */     clone.range = this.range;
/* 34 */     clone.checkLineOfSight = this.checkLineOfSight;
/* 35 */     clone.testInvisible = this.testInvisible;
/* 36 */     clone.selector = this.selector;
/* 37 */     return clone;
/*    */   }
/*    */   
/*    */   public TargetingConditions range(double range) {
/* 41 */     this.range = range;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions ignoreLineOfSight() {
/* 46 */     this.checkLineOfSight = false;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions ignoreInvisibilityTesting() {
/* 51 */     this.testInvisible = false;
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions selector(Selector selector) {
/* 56 */     this.selector = selector;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public boolean test(ServerLevel level, LivingEntity targeter, LivingEntity target) {
/* 61 */     if (targeter == target) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (!target.canBeSeenByAnyone()) {
/* 65 */       return false;
/*    */     }
/* 67 */     if (this.selector != null && !this.selector.test(target, level)) {
/* 68 */       return false;
/*    */     }
/* 70 */     if (targeter == null) {
/* 71 */       if (this.isCombat && (!target.canBeSeenAsEnemy() || level.getDifficulty() == Difficulty.PEACEFUL)) {
/* 72 */         return false;
/*    */       }
/*    */     } else {
/* 75 */       if (this.isCombat && (!targeter.canAttack(target) || !targeter.canAttackType(target.getType()) || targeter.isAlliedTo(target))) {
/* 76 */         return false;
/*    */       }
/*    */       
/* 79 */       if (this.range > 0.0D) {
/* 80 */         double modifier = this.testInvisible ? target.getVisibilityPercent(targeter) : 1.0D;
/* 81 */         double visibilityDistance = Math.max(this.range * modifier, 2.0D);
/* 82 */         double distanceToSqr = targeter.distanceToSqr(target.getX(), target.getY(), target.getZ());
/* 83 */         if (distanceToSqr > visibilityDistance * visibilityDistance) {
/* 84 */           return false;
/*    */         }
/*    */       } 
/*    */ 
/*    */       
/* 89 */       if (this.checkLineOfSight && targeter instanceof Mob) { Mob mob = (Mob)targeter; if (!mob.getSensing().hasLineOfSight(target))
/* 90 */           return false;  }
/*    */     
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Selector {
/*    */     boolean test(LivingEntity param1LivingEntity, ServerLevel param1ServerLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\targeting\TargetingConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */