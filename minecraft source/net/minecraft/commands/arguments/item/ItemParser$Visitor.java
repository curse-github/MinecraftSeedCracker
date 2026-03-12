package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

public interface Visitor {
  default void visitItem(Holder<Item> item) {}
  
  default <T> void visitComponent(DataComponentType<T> type, T value) {}
  
  default <T> void visitRemovedComponent(DataComponentType<T> type) {}
  
  default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemParser$Visitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */