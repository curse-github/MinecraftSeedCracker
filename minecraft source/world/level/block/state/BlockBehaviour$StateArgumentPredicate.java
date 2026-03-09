package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface StateArgumentPredicate<A> {
  boolean test(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, A paramA);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\BlockBehaviour$StateArgumentPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */