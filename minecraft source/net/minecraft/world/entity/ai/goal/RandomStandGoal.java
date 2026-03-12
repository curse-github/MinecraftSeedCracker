/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ 
/*    */ public class RandomStandGoal extends Goal {
/*    */   private final AbstractHorse horse;
/*    */   private int nextStand;
/*    */   
/*    */   public RandomStandGoal(AbstractHorse horse) {
/* 11 */     this.horse = horse;
/* 12 */     resetStandInterval(horse);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 17 */     this.horse.standIfPossible();
/* 18 */     playStandSound();
/*    */   }
/*    */   
/*    */   private void playStandSound() {
/* 22 */     SoundEvent ambientStandSound = this.horse.getAmbientStandSound();
/* 23 */     if (ambientStandSound != null) {
/* 24 */       this.horse.playSound(ambientStandSound);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean canContinueToUse() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 37 */     this.nextStand++;
/* 38 */     if (this.nextStand > 0 && this.horse.getRandom().nextInt(1000) < this.nextStand) {
/* 39 */       resetStandInterval(this.horse);
/* 40 */       return (!this.horse.isImmobile() && this.horse.getRandom().nextInt(10) == 0);
/*    */     } 
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 46 */   private void resetStandInterval(AbstractHorse horse) { this.nextStand = -horse.getAmbientStandInterval(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomStandGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */