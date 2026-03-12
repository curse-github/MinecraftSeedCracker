package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface ValueInputList extends Iterable<ValueInput> {
  boolean isEmpty();
  
  Stream<ValueInput> stream();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueInput$ValueInputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */