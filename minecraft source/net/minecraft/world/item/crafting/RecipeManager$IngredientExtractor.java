package net.minecraft.world.item.crafting;

import java.util.Optional;

@FunctionalInterface
public interface IngredientExtractor {
  Optional<Ingredient> apply(Recipe<?> paramRecipe);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeManager$IngredientExtractor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */