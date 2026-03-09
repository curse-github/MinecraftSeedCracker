/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestLivingEntitySensor<T extends LivingEntity>
/*    */   extends Sensor<T>
/*    */ {
/*    */   protected void doTick(ServerLevel level, T body) {
/* 23 */     double followRange = body.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 24 */     AABB boundingBox = body.getBoundingBox().inflate(followRange, followRange, followRange);
/* 25 */     List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, boundingBox, mob -> (mob != body && mob.isAlive()));
/* 26 */     Objects.requireNonNull(body); livingEntities.sort(Comparator.comparingDouble(body::distanceToSqr));
/*    */     
/* 28 */     Brain<?> brain = body.getBrain();
/* 29 */     brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, livingEntities);
/* 30 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(level, body, livingEntities));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestLivingEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */