package net.minecraft.world.level.levelgen.material;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;

public interface WorldGenMaterialRule {
  BlockState apply(NoiseChunk paramNoiseChunk, int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\material\WorldGenMaterialRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */