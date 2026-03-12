package net.minecraft.world.entity;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface LineSplitter {
  Display.TextDisplay.CachedInfo split(Component paramComponent, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Display$TextDisplay$LineSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */