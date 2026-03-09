package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;

public interface RegistryContextSwapper {
  <T> DataResult<T> swapTo(Codec<T> paramCodec, T paramT, HolderLookup.Provider paramProvider);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\RegistryContextSwapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */