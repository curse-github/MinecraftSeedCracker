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
/*    */ public class AdultSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 19 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 26 */     body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(livingEntities -> 
/* 27 */         setNearestVisibleAdult(body, livingEntities));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setNearestVisibleAdult(LivingEntity body, NearestVisibleLivingEntities visibleLivingEntities) {
/* 32 */     Optional<LivingEntity> adult = visibleLivingEntities.findClosest(entity -> (entity.getType() == body.getType() && !entity.isBaby()));
/* 33 */     body.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\AdultSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */