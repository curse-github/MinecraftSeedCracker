package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceKey;

public interface RecipeAccess {
  RecipePropertySet propertySet(ResourceKey<RecipePropertySet> paramResourceKey);
  
  SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */