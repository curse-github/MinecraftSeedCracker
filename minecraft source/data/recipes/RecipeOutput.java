package net.minecraft.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public interface RecipeOutput {
  void accept(ResourceKey<Recipe<?>> paramResourceKey, Recipe<?> paramRecipe, AdvancementHolder paramAdvancementHolder);
  
  Advancement.Builder advancement();
  
  void includeRootAdvancement();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */