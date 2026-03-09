package net.minecraft.data.recipes;

import net.minecraft.world.level.ItemLike;

@FunctionalInterface
interface FamilyRecipeProvider {
  RecipeBuilder create(RecipeProvider paramRecipeProvider, ItemLike paramItemLike1, ItemLike paramItemLike2);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeProvider$FamilyRecipeProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */