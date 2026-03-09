package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityTagOutput {
  void accept(BlockPos paramBlockPos, BlockEntityType<?> paramBlockEntityType, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData$BlockEntityTagOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */