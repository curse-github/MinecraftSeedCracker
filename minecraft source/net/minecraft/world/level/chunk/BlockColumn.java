package net.minecraft.world.level.chunk;

import net.minecraft.world.level.block.state.BlockState;

public interface BlockColumn {
  BlockState getBlock(int paramInt);
  
  void setBlock(int paramInt, BlockState paramBlockState);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\BlockColumn.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */