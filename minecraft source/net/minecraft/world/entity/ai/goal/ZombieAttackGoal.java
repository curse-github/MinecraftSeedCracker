/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ 
/*    */ public class ZombieAttackGoal extends MeleeAttackGoal {
/*    */   private final Zombie zombie;
/*    */   private int raiseArmTicks;
/*    */   
/*    */   public ZombieAttackGoal(Zombie zombie, double speedModifier, boolean trackTarget) {
/* 10 */     super(zombie, speedModifier, trackTarget);
/* 11 */     this.zombie = zombie;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 16 */     super.start();
/* 17 */     this.raiseArmTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 22 */     super.stop();
/* 23 */     this.zombie.setAggressive(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     super.tick();
/*    */     
/* 30 */     this.raiseArmTicks++;
/* 31 */     if (this.raiseArmTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2) {
/* 32 */       this.zombie.setAggressive(true);
/*    */     } else {
/* 34 */       this.zombie.setAggressive(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\ZombieAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */