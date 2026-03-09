/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NearestVisibleLivingEntitySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return ImmutableSet.of(
/* 23 */         getMemory());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected void doTick(ServerLevel level, LivingEntity body) { body.getBrain().setMemory(getMemory(), getNearestEntity(level, body)); }
/*    */ 
/*    */   
/*    */   private Optional<LivingEntity> getNearestEntity(ServerLevel level, LivingEntity body) {
/* 33 */     return getVisibleEntities(body).flatMap(livingEntities -> 
/* 34 */         livingEntities.findClosest(()));
/*    */   }
/*    */ 
/*    */   
/* 38 */   protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity body) { return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES); }
/*    */   
/*    */   protected abstract boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2);
/*    */   
/*    */   protected abstract MemoryModuleType<LivingEntity> getMemory();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestVisibleLivingEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */