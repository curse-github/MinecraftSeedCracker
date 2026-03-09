package net.minecraft.util;

import java.util.function.Consumer;

public interface Entry<K> {
  void visitRequiredDependencies(Consumer<K> paramConsumer);
  
  void visitOptionalDependencies(Consumer<K> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\DependencySorter$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */