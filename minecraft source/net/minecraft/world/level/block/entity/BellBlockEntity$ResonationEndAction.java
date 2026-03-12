package net.minecraft.world.level.block.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@FunctionalInterface
interface ResonationEndAction {
  void run(Level paramLevel, BlockPos paramBlockPos, List<LivingEntity> paramList);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BellBlockEntity$ResonationEndAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */