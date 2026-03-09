package net.minecraft.world.level.chunk.storage;

import java.util.concurrent.CompletableFuture;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.world.level.ChunkPos;

public interface ChunkScanAccess {
  CompletableFuture<Void> scanChunk(ChunkPos paramChunkPos, StreamTagVisitor paramStreamTagVisitor);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\ChunkScanAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */