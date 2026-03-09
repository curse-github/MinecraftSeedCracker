package net.minecraft.world.level.chunk;

import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface UnsavedListener {
  void setUnsaved(ChunkPos paramChunkPos);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LevelChunk$UnsavedListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */