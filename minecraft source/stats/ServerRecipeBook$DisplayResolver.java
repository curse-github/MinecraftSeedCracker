package net.minecraft.stats;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

@FunctionalInterface
public interface DisplayResolver {
  void displaysForRecipe(ResourceKey<Recipe<?>> paramResourceKey, Consumer<RecipeDisplayEntry> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\ServerRecipeBook$DisplayResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */