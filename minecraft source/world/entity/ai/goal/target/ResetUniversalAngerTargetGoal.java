/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.NeutralMob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResetUniversalAngerTargetGoal<T extends Mob & NeutralMob>
/*    */   extends Goal
/*    */ {
/*    */   private static final int ALERT_RANGE_Y = 10;
/*    */   private final T mob;
/*    */   private final boolean alertOthersOfSameType;
/*    */   private int lastHurtByPlayerTimestamp;
/*    */   
/*    */   public ResetUniversalAngerTargetGoal(T mob, boolean alertOthersOfSameType) {
/* 33 */     this.mob = mob;
/* 34 */     this.alertOthersOfSameType = alertOthersOfSameType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean canUse() { return (((Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue() && wasHurtByPlayer()); }
/*    */ 
/*    */   
/*    */   private boolean wasHurtByPlayer() {
/* 43 */     return (this.mob.getLastHurtByMob() != null && this.mob
/* 44 */       .getLastHurtByMob().getType() == EntityType.PLAYER && this.mob
/* 45 */       .getLastHurtByMobTimestamp() > this.lastHurtByPlayerTimestamp);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 50 */     this.lastHurtByPlayerTimestamp = this.mob.getLastHurtByMobTimestamp();
/* 51 */     ((NeutralMob)this.mob).forgetCurrentTargetAndRefreshUniversalAnger();
/* 52 */     if (this.alertOthersOfSameType) {
/* 53 */       getNearbyMobsOfSameType().stream()
/* 54 */         .filter(otherMob -> (otherMob != this.mob))
/* 55 */         .map(otherMob -> (NeutralMob)otherMob)
/* 56 */         .forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger);
/*    */     }
/* 58 */     super.start();
/*    */   }
/*    */   
/*    */   private List<? extends Mob> getNearbyMobsOfSameType() {
/* 62 */     double within = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 63 */     AABB searchAabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(within, 10.0D, within);
/* 64 */     return this.mob.level().getEntitiesOfClass(this.mob.getClass(), searchAabb, EntitySelector.NO_SPECTATORS);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\ResetUniversalAngerTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */