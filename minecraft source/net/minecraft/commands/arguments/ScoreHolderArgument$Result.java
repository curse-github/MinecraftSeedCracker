package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.scores.ScoreHolder;

@FunctionalInterface
public interface Result {
  Collection<ScoreHolder> getNames(CommandSourceStack paramCommandSourceStack, Supplier<Collection<ScoreHolder>> paramSupplier) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */