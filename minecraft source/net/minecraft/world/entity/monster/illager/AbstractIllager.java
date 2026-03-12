/*    */ package net.minecraft.world.entity.monster.illager;
/*    */ 
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractIllager
/*    */   extends Raider {
/*    */   public enum IllagerArmPose {
/* 14 */     CROSSED,
/* 15 */     ATTACKING,
/* 16 */     SPELLCASTING,
/* 17 */     BOW_AND_ARROW,
/* 18 */     CROSSBOW_HOLD,
/* 19 */     CROSSBOW_CHARGE,
/* 20 */     CELEBRATING,
/* 21 */     NEUTRAL;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected AbstractIllager(EntityType<? extends AbstractIllager> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected void registerGoals() { super.registerGoals(); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public IllagerArmPose getArmPose() { return IllagerArmPose.CROSSED; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canAttack(LivingEntity target) {
/* 42 */     if (target instanceof net.minecraft.world.entity.npc.villager.AbstractVillager && target.isBaby()) {
/* 43 */       return false;
/*    */     }
/* 45 */     return super.canAttack(target);
/*    */   }
/*    */   
/*    */   protected class RaiderOpenDoorGoal
/*    */     extends OpenDoorGoal {
/* 50 */     public RaiderOpenDoorGoal(Raider raider) { super(raider, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     public boolean canUse() { return (super.canUse() && AbstractIllager.this.hasActiveRaid()); }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean considersEntityAsAlly(Entity other) {
/* 61 */     if (super.considersEntityAsAlly(other)) {
/* 62 */       return true;
/*    */     }
/* 64 */     if (other.getType().is(EntityTypeTags.ILLAGER_FRIENDS))
/*    */     {
/* 66 */       return (getTeam() == null && other.getTeam() == null);
/*    */     }
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\AbstractIllager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */