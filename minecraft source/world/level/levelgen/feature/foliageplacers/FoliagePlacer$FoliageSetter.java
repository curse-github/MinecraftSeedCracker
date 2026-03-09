package net.minecraft.world.level.levelgen.feature.foliageplacers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface FoliageSetter {
  void set(BlockPos paramBlockPos, BlockState paramBlockState);
  
  boolean isSet(BlockPos paramBlockPos);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer$FoliageSetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */