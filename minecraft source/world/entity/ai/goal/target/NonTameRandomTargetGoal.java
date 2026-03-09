/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ public class NonTameRandomTargetGoal<T extends LivingEntity>
/*    */   extends NearestAttackableTargetGoal<T> {
/*    */   private final TamableAnimal tamableMob;
/*    */   
/*    */   public NonTameRandomTargetGoal(TamableAnimal mob, Class<T> targetType, boolean mustSee, TargetingConditions.Selector subselector) {
/* 12 */     super(mob, targetType, 10, mustSee, false, subselector);
/* 13 */     this.tamableMob = mob;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public boolean canUse() { return (!this.tamableMob.isTame() && super.canUse()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 23 */     if (this.targetConditions != null) {
/* 24 */       return this.targetConditions.test(getServerLevel(this.mob), this.mob, this.target);
/*    */     }
/* 26 */     return super.canContinueToUse();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NonTameRandomTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */