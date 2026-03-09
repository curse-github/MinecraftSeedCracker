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
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SpearRetreat
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   public static final int MIN_COOLDOWN_DISTANCE = 9;
/*    */   public static final int MAX_COOLDOWN_DISTANCE = 11;
/*    */   public static final int MAX_FLEEING_TIME = 100;
/*    */   double speedModifierWhenRepositioning;
/*    */   
/*    */   public SpearRetreat(double speedModifierWhenRepositioning) {
/* 24 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_PRESENT), 100);
/* 25 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   private LivingEntity getTarget(PathfinderMob mob) { return (LivingEntity)mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   private boolean ableToAttack(PathfinderMob mob) { return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) {
/* 39 */     if (!ableToAttack(body) || body.isUsingItem()) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (body.getBrain().getMemory(MemoryModuleType.SPEAR_STATUS).orElse(SpearAttack.SpearStatus.APPROACH) != SpearAttack.SpearStatus.RETREAT) {
/* 43 */       return false;
/*    */     }
/* 45 */     LivingEntity target = getTarget(body);
/* 46 */     double targetDistSqr = body.distanceToSqr(target.getX(), target.getY(), target.getZ());
/*    */     
/* 48 */     int mountDistance = body.isPassenger() ? 2 : 0;
/* 49 */     double distance = Math.sqrt(targetDistSqr);
/* 50 */     Vec3 awayPos = LandRandomPos.getPosAway(body, Math.max(0.0D, (9 + mountDistance) - distance), Math.max(1.0D, (11 + mountDistance) - distance), 7, target.position());
/*    */     
/* 52 */     if (awayPos == null) {
/* 53 */       return false;
/*    */     }
/*    */     
/* 56 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_POSITION, awayPos);
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 62 */     body.setAggressive(true);
/* 63 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_TIME, Integer.valueOf(0));
/* 64 */     super.start(level, body, timestamp);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/* 69 */     return (((Integer)body.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse(Integer.valueOf(100))).intValue() < 100 && body
/* 70 */       .getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).isPresent() && 
/* 71 */       !body.getNavigation().isDone() && 
/* 72 */       ableToAttack(body));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/* 77 */     LivingEntity target = getTarget(mob);
/* 78 */     Entity mount = mob.getRootVehicle();
/* 79 */     Mob vehicleMob = (Mob)mount; float speedModifier = (mount instanceof Mob) ? vehicleMob.chargeSpeedModifier() : 1.0F;
/*    */     
/* 81 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
/*    */     
/* 83 */     mob.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_TIME, Integer.valueOf(((Integer)mob.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse(Integer.valueOf(0))).intValue() + 1));
/*    */     
/* 85 */     mob.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).ifPresent(fleePos -> 
/* 86 */         mob.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, speedModifier * this.speedModifierWhenRepositioning));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 92 */     body.getNavigation().stop();
/* 93 */     body.setAggressive(false);
/* 94 */     body.stopUsingItem();
/* 95 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_FLEEING_TIME);
/* 96 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_FLEEING_POSITION);
/* 97 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_STATUS);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SpearRetreat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */