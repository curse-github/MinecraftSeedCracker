package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MenuConstructor {
  AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\MenuConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */