package net.minecraft.core;

import net.minecraft.resources.ResourceKey;

public interface PendingTags<T> {
  ResourceKey<? extends Registry<? extends T>> key();
  
  HolderLookup.RegistryLookup<T> lookup();
  
  void apply();
  
  int size();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Registry$PendingTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */