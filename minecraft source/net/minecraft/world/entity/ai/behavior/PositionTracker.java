package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface PositionTracker {
  Vec3 currentPosition();
  
  BlockPos currentBlockPosition();
  
  boolean isVisibleBy(LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PositionTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */