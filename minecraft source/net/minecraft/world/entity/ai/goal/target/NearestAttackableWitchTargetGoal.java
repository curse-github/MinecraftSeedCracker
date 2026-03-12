/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestAttackableWitchTargetGoal<T extends LivingEntity>
/*    */   extends NearestAttackableTargetGoal<T> {
/*    */   private boolean canAttack;
/*    */   
/*    */   public NearestAttackableWitchTargetGoal(Raider raider, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, TargetingConditions.Selector subselector) {
/* 12 */     super(raider, targetType, randomInterval, mustSee, mustReach, subselector);
/* 13 */     this.canAttack = true;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public void setCanAttack(boolean canAttack) { this.canAttack = canAttack; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean canUse() { return (this.canAttack && super.canUse()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestAttackableWitchTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */