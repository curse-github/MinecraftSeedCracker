/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class AdultSensorAnyType
/*    */   extends AdultSensor
/*    */ {
/*    */   protected void setNearestVisibleAdult(LivingEntity body, NearestVisibleLivingEntities visibleLivingEntities) {
/* 13 */     Optional<LivingEntity> adult = visibleLivingEntities.findClosest(entity -> (entity.getType().is(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !entity.isBaby()));
/* 14 */     body.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\AdultSensorAnyType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */