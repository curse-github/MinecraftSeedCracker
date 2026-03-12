/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerHostilesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/* 14 */   private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
/* 15 */     .put(EntityType.DROWNED, Float.valueOf(8.0F))
/* 16 */     .put(EntityType.EVOKER, Float.valueOf(12.0F))
/* 17 */     .put(EntityType.HUSK, Float.valueOf(8.0F))
/* 18 */     .put(EntityType.ILLUSIONER, Float.valueOf(12.0F))
/* 19 */     .put(EntityType.PILLAGER, Float.valueOf(15.0F))
/* 20 */     .put(EntityType.RAVAGER, Float.valueOf(12.0F))
/* 21 */     .put(EntityType.VEX, Float.valueOf(8.0F))
/* 22 */     .put(EntityType.VINDICATOR, Float.valueOf(10.0F))
/* 23 */     .put(EntityType.ZOGLIN, Float.valueOf(10.0F))
/* 24 */     .put(EntityType.ZOMBIE, Float.valueOf(8.0F))
/* 25 */     .put(EntityType.ZOMBIE_VILLAGER, Float.valueOf(8.0F))
/* 26 */     .build();
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) { return (isHostile(mob) && isClose(body, mob)); }
/*    */ 
/*    */   
/*    */   private boolean isClose(LivingEntity body, LivingEntity mob) {
/* 34 */     float distThreshold = ((Float)ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(mob.getType())).floatValue();
/* 35 */     return (mob.distanceToSqr(body) <= (distThreshold * distThreshold));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected MemoryModuleType<LivingEntity> getMemory() { return MemoryModuleType.NEAREST_HOSTILE; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   private boolean isHostile(LivingEntity entity) { return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(entity.getType()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\VillagerHostilesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */