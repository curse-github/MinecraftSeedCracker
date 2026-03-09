/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class AxolotlAttackablesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 8.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 14 */     return (isClose(body, mob) && mob.isInWater() && (
/* 15 */       isHostileTarget(mob) || isHuntTarget(body, mob)) && 
/* 16 */       Sensor.isEntityAttackable(level, body, mob));
/*    */   }
/*    */ 
/*    */   
/* 20 */   private boolean isHuntTarget(LivingEntity body, LivingEntity mob) { return (!body.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && mob.getType().is(EntityTypeTags.AXOLOTL_HUNT_TARGETS)); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   private boolean isHostileTarget(LivingEntity mob) { return mob.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   private boolean isClose(LivingEntity body, LivingEntity mob) { return (mob.distanceToSqr(body) <= 64.0D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected MemoryModuleType<LivingEntity> getMemory() { return MemoryModuleType.NEAREST_ATTACKABLE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\AxolotlAttackablesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */