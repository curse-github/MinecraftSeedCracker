package net.minecraft.world.level.chunk;

import com.mojang.serialization.DataResult;

public interface Unpacker<T, C extends PalettedContainerRO<T>> {
  DataResult<C> read(Strategy<T> paramStrategy, PalettedContainerRO.PackedData<T> paramPackedData);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\PalettedContainerRO$Unpacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */