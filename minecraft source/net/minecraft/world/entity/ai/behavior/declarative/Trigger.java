package net.minecraft.world.entity.ai.behavior.declarative;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public interface Trigger<E extends LivingEntity> {
  boolean trigger(ServerLevel paramServerLevel, E paramE, long paramLong);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\Trigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */