package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface SpawnPredicate {
  boolean test(EntityType<?> paramEntityType, BlockPos paramBlockPos, ChunkAccess paramChunkAccess);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\NaturalSpawner$SpawnPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */