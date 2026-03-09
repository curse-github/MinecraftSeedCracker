package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

interface NeighborUpdates {
  boolean runNext(Level paramLevel);
  
  void forEachUpdatedPos(Consumer<BlockPos> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$NeighborUpdates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */