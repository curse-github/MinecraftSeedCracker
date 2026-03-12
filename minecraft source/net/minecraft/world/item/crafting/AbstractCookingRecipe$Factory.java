package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface Factory<T extends AbstractCookingRecipe> {
  T create(String paramString, CookingBookCategory paramCookingBookCategory, Ingredient paramIngredient, ItemStack paramItemStack, float paramFloat, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */