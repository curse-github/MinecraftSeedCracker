package net.minecraft.nbt.visitors;

import net.minecraft.nbt.Tag;

interface ContainerBuilder {
  default void acceptKey(String id) {}
  
  void acceptValue(Tag paramTag);
  
  Tag build();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\visitors\CollectToTag$ContainerBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */