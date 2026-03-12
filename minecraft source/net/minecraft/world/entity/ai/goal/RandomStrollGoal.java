/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomStrollGoal
/*    */   extends Goal
/*    */ {
/*    */   public static final int DEFAULT_INTERVAL = 120;
/*    */   protected final PathfinderMob mob;
/*    */   protected double wantedX;
/*    */   protected double wantedY;
/*    */   protected double wantedZ;
/*    */   protected final double speedModifier;
/*    */   protected int interval;
/*    */   protected boolean forceTrigger;
/*    */   private final boolean checkNoActionTime;
/*    */   
/* 23 */   public RandomStrollGoal(PathfinderMob mob, double speedModifier) { this(mob, speedModifier, 120); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public RandomStrollGoal(PathfinderMob mob, double speedModifier, int interval) { this(mob, speedModifier, interval, true); }
/*    */ 
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob mob, double speedModifier, int interval, boolean checkNoActionTime) {
/* 31 */     this.mob = mob;
/* 32 */     this.speedModifier = speedModifier;
/* 33 */     this.interval = interval;
/* 34 */     this.checkNoActionTime = checkNoActionTime;
/* 35 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 40 */     if (this.mob.hasControllingPassenger()) {
/* 41 */       return false;
/*    */     }
/* 43 */     if (!this.forceTrigger) {
/* 44 */       if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
/* 45 */         return false;
/*    */       }
/* 47 */       if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
/* 48 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 52 */     Vec3 pos = getPosition();
/*    */     
/* 54 */     if (pos == null) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     this.wantedX = pos.x;
/* 59 */     this.wantedY = pos.y;
/* 60 */     this.wantedZ = pos.z;
/* 61 */     this.forceTrigger = false;
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 66 */   protected Vec3 getPosition() { return DefaultRandomPos.getPos(this.mob, 10, 7); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean canContinueToUse() { return (!this.mob.getNavigation().isDone() && !this.mob.hasControllingPassenger()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public void start() { this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 81 */     this.mob.getNavigation().stop();
/* 82 */     super.stop();
/*    */   }
/*    */ 
/*    */   
/* 86 */   public void trigger() { this.forceTrigger = true; }
/*    */ 
/*    */ 
/*    */   
/* 90 */   public void setInterval(int interval) { this.interval = interval; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomStrollGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */