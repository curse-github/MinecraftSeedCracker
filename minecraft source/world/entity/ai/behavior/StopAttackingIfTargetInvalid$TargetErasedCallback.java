package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetErasedCallback<E> {
  void accept(ServerLevel paramServerLevel, E paramE, LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid$TargetErasedCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */