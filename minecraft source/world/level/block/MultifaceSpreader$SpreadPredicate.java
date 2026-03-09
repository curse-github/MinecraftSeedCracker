package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface SpreadPredicate {
  boolean test(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */