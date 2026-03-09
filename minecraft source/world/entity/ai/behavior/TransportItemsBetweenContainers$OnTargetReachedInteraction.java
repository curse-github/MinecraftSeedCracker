package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.function.TriConsumer;

@FunctionalInterface
public interface OnTargetReachedInteraction extends TriConsumer<PathfinderMob, TransportItemsBetweenContainers.TransportItemTarget, Integer> {}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TransportItemsBetweenContainers$OnTargetReachedInteraction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */