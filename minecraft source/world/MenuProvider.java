package net.minecraft.world;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuConstructor;

public interface MenuProvider extends MenuConstructor {
  Component getDisplayName();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\MenuProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */