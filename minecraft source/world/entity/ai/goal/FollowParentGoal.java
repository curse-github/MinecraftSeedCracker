/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ 
/*    */ public class FollowParentGoal
/*    */   extends Goal
/*    */ {
/*    */   public static final int HORIZONTAL_SCAN_RANGE = 8;
/*    */   public static final int VERTICAL_SCAN_RANGE = 4;
/*    */   public static final int DONT_FOLLOW_IF_CLOSER_THAN = 3;
/*    */   private final Animal animal;
/*    */   private Animal parent;
/*    */   private final double speedModifier;
/*    */   private int timeToRecalcPath;
/*    */   
/*    */   public FollowParentGoal(Animal animal, double speedModifier) {
/* 18 */     this.animal = animal;
/* 19 */     this.speedModifier = speedModifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 24 */     if (this.animal.getAge() >= 0) {
/* 25 */       return false;
/*    */     }
/*    */     
/* 28 */     List<? extends Animal> parents = this.animal.level().getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/*    */     
/* 30 */     Animal closest = null;
/* 31 */     double closestDistSqr = Double.MAX_VALUE;
/* 32 */     for (Animal parent : parents) {
/* 33 */       if (parent.getAge() < 0) {
/*    */         continue;
/*    */       }
/* 36 */       double distSqr = this.animal.distanceToSqr(parent);
/* 37 */       if (distSqr > closestDistSqr) {
/*    */         continue;
/*    */       }
/* 40 */       closestDistSqr = distSqr;
/* 41 */       closest = parent;
/*    */     } 
/*    */     
/* 44 */     if (closest == null) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (closestDistSqr < 9.0D) {
/* 48 */       return false;
/*    */     }
/* 50 */     this.parent = closest;
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 56 */     if (this.animal.getAge() >= 0) {
/* 57 */       return false;
/*    */     }
/* 59 */     if (!this.parent.isAlive()) {
/* 60 */       return false;
/*    */     }
/* 62 */     double distSqr = this.animal.distanceToSqr(this.parent);
/* 63 */     if (distSqr < 9.0D || distSqr > 256.0D) {
/* 64 */       return false;
/*    */     }
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public void start() { this.timeToRecalcPath = 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public void stop() { this.parent = null; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 81 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 84 */     this.timeToRecalcPath = adjustedTickDelay(10);
/* 85 */     this.animal.getNavigation().moveTo(this.parent, this.speedModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowParentGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */