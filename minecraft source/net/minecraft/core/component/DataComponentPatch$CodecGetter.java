package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
interface CodecGetter {
  <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> paramDataComponentType);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentPatch$CodecGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */