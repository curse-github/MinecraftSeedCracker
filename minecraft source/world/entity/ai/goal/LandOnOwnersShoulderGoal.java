/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
/*    */ 
/*    */ public class LandOnOwnersShoulderGoal
/*    */   extends Goal {
/*    */   private final ShoulderRidingEntity entity;
/*    */   private boolean isSittingOnShoulder;
/*    */   
/* 12 */   public LandOnOwnersShoulderGoal(ShoulderRidingEntity entity) { this.entity = entity; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 17 */     LivingEntity livingEntity = this.entity.getOwner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer owner = (ServerPlayer)livingEntity;
/* 18 */       boolean ownerThatCanBeSatOn = (!owner.isSpectator() && !(owner.getAbilities()).flying && !owner.isInWater() && !owner.isInPowderSnow);
/* 19 */       return (!this.entity.isOrderedToSit() && ownerThatCanBeSatOn && this.entity.canSitOnShoulder()); }
/*    */     
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean isInterruptable() { return !this.isSittingOnShoulder; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void start() { this.isSittingOnShoulder = false; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     if (this.isSittingOnShoulder || this.entity.isInSittingPose() || this.entity.isLeashed()) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     LivingEntity livingEntity = this.entity.getOwner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer owner = (ServerPlayer)livingEntity;
/* 41 */       if (this.entity.getBoundingBox().intersects(owner.getBoundingBox()))
/* 42 */         this.isSittingOnShoulder = this.entity.setEntityOnShoulder(owner);  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\LandOnOwnersShoulderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */