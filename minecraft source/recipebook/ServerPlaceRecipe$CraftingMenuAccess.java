package net.minecraft.recipebook;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public interface CraftingMenuAccess<T extends Recipe<?>> {
  void fillCraftSlotsStackedContents(StackedItemContents paramStackedItemContents);
  
  void clearCraftingContent();
  
  boolean recipeMatches(RecipeHolder<T> paramRecipeHolder);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\recipebook\ServerPlaceRecipe$CraftingMenuAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */