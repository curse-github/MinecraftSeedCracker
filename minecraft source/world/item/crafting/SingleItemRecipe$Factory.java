package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface Factory<T extends SingleItemRecipe> {
  T create(String paramString, Ingredient paramIngredient, ItemStack paramItemStack);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SingleItemRecipe$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */