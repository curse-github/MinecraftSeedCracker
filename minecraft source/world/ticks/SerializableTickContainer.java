package net.minecraft.world.ticks;

import java.util.List;

public interface SerializableTickContainer<T> {
  List<SavedTick<T>> pack(long paramLong);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\SerializableTickContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */