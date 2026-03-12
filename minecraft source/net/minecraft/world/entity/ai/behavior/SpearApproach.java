/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class SpearApproach
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   double speedModifierWhenRepositioning;
/*    */   float approachDistanceSq;
/*    */   
/*    */   public SpearApproach(double speedModifierWhenRepositioning, float approachDistance) {
/* 20 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_ABSENT));
/* 21 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/* 22 */     this.approachDistanceSq = approachDistance * approachDistance;
/*    */   }
/*    */ 
/*    */   
/* 26 */   private boolean ableToAttack(PathfinderMob mob) { return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) { return (ableToAttack(body) && !body.isUsingItem()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 36 */     body.setAggressive(true);
/* 37 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearAttack.SpearStatus.APPROACH);
/* 38 */     super.start(level, body, timestamp);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   private LivingEntity getTarget(PathfinderMob mob) { return (LivingEntity)mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) { return (ableToAttack(body) && farEnough(body)); }
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean farEnough(PathfinderMob mob) {
/* 53 */     LivingEntity target = getTarget(mob);
/* 54 */     double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/* 55 */     return (targetDistSqr > this.approachDistanceSq);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/* 60 */     LivingEntity target = getTarget(mob);
/* 61 */     Entity mount = mob.getRootVehicle();
/* 62 */     float speedModifier = 1.0F;
/* 63 */     if (mount instanceof Mob) { Mob vehicleMob = (Mob)mount;
/* 64 */       speedModifier = vehicleMob.chargeSpeedModifier(); }
/*    */     
/* 66 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
/* 67 */     mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenRepositioning);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 72 */     body.getNavigation().stop();
/* 73 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearAttack.SpearStatus.CHARGING);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   protected boolean timedOut(long timestamp) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SpearApproach.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */