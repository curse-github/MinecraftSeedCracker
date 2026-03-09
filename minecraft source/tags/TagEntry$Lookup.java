package net.minecraft.tags;

import java.util.Collection;
import net.minecraft.resources.Identifier;

public interface Lookup<T> {
  T element(Identifier paramIdentifier, boolean paramBoolean);
  
  Collection<T> tag(Identifier paramIdentifier);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagEntry$Lookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */