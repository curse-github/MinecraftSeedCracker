package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.ScoreAccess;

@FunctionalInterface
public interface Operation {
  void apply(ScoreAccess paramScoreAccess1, ScoreAccess paramScoreAccess2) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\OperationArgument$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */