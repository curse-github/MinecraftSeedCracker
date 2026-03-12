/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ public class OwnerHurtTargetGoal
/*    */   extends TargetGoal {
/*    */   private final TamableAnimal tameAnimal;
/*    */   private LivingEntity ownerLastHurt;
/*    */   private int timestamp;
/*    */   
/*    */   public OwnerHurtTargetGoal(TamableAnimal tameAnimal) {
/* 16 */     super(tameAnimal, false);
/* 17 */     this.tameAnimal = tameAnimal;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (!this.tameAnimal.isTame() || this.tameAnimal.isOrderedToSit()) {
/* 24 */       return false;
/*    */     }
/* 26 */     LivingEntity owner = this.tameAnimal.getOwner();
/* 27 */     if (owner == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     this.ownerLastHurt = owner.getLastHurtMob();
/* 31 */     int ts = owner.getLastHurtMobTimestamp();
/* 32 */     return (ts != this.timestamp && canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurt, owner));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setTarget(this.ownerLastHurt);
/*    */     
/* 39 */     LivingEntity owner = this.tameAnimal.getOwner();
/* 40 */     if (owner != null) {
/* 41 */       this.timestamp = owner.getLastHurtMobTimestamp();
/*    */     }
/*    */     
/* 44 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\OwnerHurtTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */