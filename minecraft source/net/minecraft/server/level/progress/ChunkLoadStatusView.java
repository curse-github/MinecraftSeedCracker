package net.minecraft.server.level.progress;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public interface ChunkLoadStatusView {
  void moveTo(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos);
  
  ChunkStatus get(int paramInt1, int paramInt2);
  
  int radius();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\progress\ChunkLoadStatusView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */