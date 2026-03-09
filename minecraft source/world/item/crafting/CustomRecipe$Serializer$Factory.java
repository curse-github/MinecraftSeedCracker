package net.minecraft.world.item.crafting;

@FunctionalInterface
public interface Factory<T extends CraftingRecipe> {
  T create(CraftingBookCategory paramCraftingBookCategory);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\CustomRecipe$Serializer$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */