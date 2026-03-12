/*    */ package net.minecraft.world.entity.monster.breeze;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class Slide
/*    */   extends Behavior<Breeze>
/*    */ {
/* 20 */   public Slide() { super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_JUMP_COOLDOWN, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT, MemoryStatus.VALUE_ABSENT)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected boolean checkExtraStartConditions(ServerLevel level, Breeze breeze) { return (breeze.onGround() && !breeze.isInWater() && breeze.getPose() == Pose.STANDING); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Breeze breeze, long timestamp) {
/* 35 */     LivingEntity enemy = (LivingEntity)breeze.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/* 36 */     if (enemy == null) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     boolean isWithinInnerRing = breeze.withinInnerCircleRange(enemy.position());
/*    */     
/* 42 */     Vec3 position = null;
/*    */     
/* 44 */     if (isWithinInnerRing) {
/*    */       
/* 46 */       Vec3 position0 = DefaultRandomPos.getPosAway(breeze, 5, 5, enemy.position());
/*    */       
/* 48 */       if (position0 != null && BreezeUtil.hasLineOfSight(breeze, position0) && enemy.distanceToSqr(position0.x, position0.y, position0.z) > enemy.distanceToSqr(breeze)) {
/* 49 */         position = position0;
/*    */       }
/*    */     } 
/*    */     
/* 53 */     if (position == null)
/*    */     {
/* 55 */       position = breeze.getRandom().nextBoolean() ? BreezeUtil.randomPointBehindTarget(enemy, breeze.getRandom()) : randomPointInMiddleCircle(breeze, enemy);
/*    */     }
/*    */     
/* 58 */     breeze.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(BlockPos.containing(position), 0.6F, 1));
/*    */   }
/*    */   
/*    */   private static Vec3 randomPointInMiddleCircle(Breeze breeze, LivingEntity enemy) {
/* 62 */     Vec3 direction = enemy.position().subtract(breeze.position());
/* 63 */     double distance = direction.length() - Mth.lerp(breeze.getRandom().nextDouble(), 8.0D, 4.0D);
/*    */     
/* 65 */     Vec3 target = direction.normalize().multiply(distance, distance, distance);
/* 66 */     return breeze.position().add(target);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\Slide.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */