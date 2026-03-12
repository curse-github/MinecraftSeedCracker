package net.minecraft.world.level.chunk;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;

public interface LightChunkGetter {
  LightChunk getChunkForLighting(int paramInt1, int paramInt2);
  
  default void onLightUpdate(LightLayer layer, SectionPos pos) {}
  
  BlockGetter getLevel();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LightChunkGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */