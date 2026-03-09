package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;

interface MenuSupplier<T extends AbstractContainerMenu> {
  T create(int paramInt, Inventory paramInventory);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\MenuType$MenuSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */