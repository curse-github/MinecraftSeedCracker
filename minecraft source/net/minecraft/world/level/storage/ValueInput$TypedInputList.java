package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface TypedInputList<T> extends Iterable<T> {
  boolean isEmpty();
  
  Stream<T> stream();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueInput$TypedInputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */