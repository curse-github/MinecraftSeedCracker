package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;

public interface CachedCheck<I extends RecipeInput, T extends Recipe<I>> {
  Optional<RecipeHolder<T>> getRecipeFor(I paramI, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeManager$CachedCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */