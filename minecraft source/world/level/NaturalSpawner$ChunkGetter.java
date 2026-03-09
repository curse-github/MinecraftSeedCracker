package net.minecraft.world.level;

import java.util.function.Consumer;
import net.minecraft.world.level.chunk.LevelChunk;

@FunctionalInterface
public interface ChunkGetter {
  void query(long paramLong, Consumer<LevelChunk> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\NaturalSpawner$ChunkGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */