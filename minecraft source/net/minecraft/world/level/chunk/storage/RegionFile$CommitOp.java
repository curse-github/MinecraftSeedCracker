package net.minecraft.world.level.chunk.storage;

import java.io.IOException;

interface CommitOp {
  void run() throws IOException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFile$CommitOp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */