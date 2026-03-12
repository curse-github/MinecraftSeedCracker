package net.minecraft.data.structures;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface Filter {
  CompoundTag apply(String paramString, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\structures\SnbtToNbt$Filter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */