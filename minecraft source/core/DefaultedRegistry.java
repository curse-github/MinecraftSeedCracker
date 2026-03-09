package net.minecraft.core;

import net.minecraft.resources.Identifier;

public interface DefaultedRegistry<T> extends Registry<T> {
  Identifier getKey(T paramT);
  
  T getValue(Identifier paramIdentifier);
  
  T byId(int paramInt);
  
  Identifier getDefaultKey();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\DefaultedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */