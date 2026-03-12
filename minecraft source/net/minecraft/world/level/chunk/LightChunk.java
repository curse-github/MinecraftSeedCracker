package net.minecraft.world.level.chunk;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;

public interface LightChunk extends BlockGetter {
  void findBlockLightSources(BiConsumer<BlockPos, BlockState> paramBiConsumer);
  
  ChunkSkyLightSources getSkyLightSources();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LightChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */