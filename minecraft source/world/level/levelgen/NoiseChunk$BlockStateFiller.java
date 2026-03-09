package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockStateFiller {
  BlockState calculate(DensityFunction.FunctionContext paramFunctionContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlockStateFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */