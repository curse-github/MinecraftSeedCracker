package net.minecraft.server.level;

import java.util.concurrent.CompletableFuture;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.status.ChunkStep;

public interface GeneratingChunkMap {
  GenerationChunkHolder acquireGeneration(long paramLong);
  
  void releaseGeneration(GenerationChunkHolder paramGenerationChunkHolder);
  
  CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder paramGenerationChunkHolder, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D);
  
  ChunkGenerationTask scheduleGenerationTask(ChunkStatus paramChunkStatus, ChunkPos paramChunkPos);
  
  void runGenerationTasks();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\GeneratingChunkMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */