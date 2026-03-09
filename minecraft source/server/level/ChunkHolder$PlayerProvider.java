package net.minecraft.server.level;

import java.util.List;
import net.minecraft.world.level.ChunkPos;

public interface PlayerProvider {
  List<ServerPlayer> getPlayers(ChunkPos paramChunkPos, boolean paramBoolean);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkHolder$PlayerProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */