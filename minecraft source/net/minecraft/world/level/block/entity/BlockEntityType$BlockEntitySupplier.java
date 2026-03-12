package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
interface BlockEntitySupplier<T extends BlockEntity> {
  T create(BlockPos paramBlockPos, BlockState paramBlockState);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BlockEntityType$BlockEntitySupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */