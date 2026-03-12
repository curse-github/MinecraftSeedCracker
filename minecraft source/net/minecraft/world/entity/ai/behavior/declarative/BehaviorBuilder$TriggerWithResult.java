package net.minecraft.world.entity.ai.behavior.declarative;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

interface TriggerWithResult<E extends LivingEntity, R> {
  R tryTrigger(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  String debugString();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$TriggerWithResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */