package net.minecraft.world.level.chunk;

import java.util.List;

public interface Factory {
  <A> Palette<A> create(int paramInt, List<A> paramList);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\Palette$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */