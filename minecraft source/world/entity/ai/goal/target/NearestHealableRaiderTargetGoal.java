/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestHealableRaiderTargetGoal<T extends LivingEntity>
/*    */   extends NearestAttackableTargetGoal<T>
/*    */ {
/*    */   private static final int DEFAULT_COOLDOWN = 200;
/*    */   private int cooldown;
/*    */   
/*    */   public NearestHealableRaiderTargetGoal(Raider raider, Class<T> targetType, boolean mustSee, TargetingConditions.Selector subselector) {
/* 14 */     super(raider, targetType, 500, mustSee, false, subselector);
/* 15 */     this.cooldown = 0;
/*    */   }
/*    */ 
/*    */   
/* 19 */   public int getCooldown() { return this.cooldown; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void decrementCooldown() { this.cooldown--; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 28 */     if (this.cooldown > 0 || !this.mob.getRandom().nextBoolean()) {
/* 29 */       return false;
/*    */     }
/* 31 */     if (!((Raider)this.mob).hasActiveRaid()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     findTarget();
/* 36 */     return (this.target != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 41 */     this.cooldown = reducedTickDelay(200);
/* 42 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestHealableRaiderTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */