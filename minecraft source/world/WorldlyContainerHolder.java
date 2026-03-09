package net.minecraft.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldlyContainerHolder {
  WorldlyContainer getContainer(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\WorldlyContainerHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */