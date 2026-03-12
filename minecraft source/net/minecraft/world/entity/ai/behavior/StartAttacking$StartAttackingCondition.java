package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface StartAttackingCondition<E> {
  boolean test(ServerLevel paramServerLevel, E paramE);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking$StartAttackingCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */