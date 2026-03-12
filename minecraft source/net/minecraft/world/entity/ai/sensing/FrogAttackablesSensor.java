/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.animal.frog.Frog;
/*    */ 
/*    */ public class FrogAttackablesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 10.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 17 */     if (!body.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && 
/* 18 */       Sensor.isEntityAttackable(level, body, mob) && 
/* 19 */       Frog.canEat(mob) && 
/* 20 */       !isUnreachableAttackTarget(body, mob))
/*    */     {
/* 22 */       return mob.closerThan(body, 10.0D);
/*    */     }
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   private boolean isUnreachableAttackTarget(LivingEntity body, LivingEntity mob) {
/* 28 */     List<UUID> unreachableAttackTargets = (List)body.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(java.util.ArrayList::new);
/* 29 */     return unreachableAttackTargets.contains(mob.getUUID());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected MemoryModuleType<LivingEntity> getMemory() { return MemoryModuleType.NEAREST_ATTACKABLE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\FrogAttackablesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */