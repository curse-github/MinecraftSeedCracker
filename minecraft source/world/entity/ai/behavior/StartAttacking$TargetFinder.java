package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetFinder<E> {
  Optional<? extends LivingEntity> get(ServerLevel paramServerLevel, E paramE);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking$TargetFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */