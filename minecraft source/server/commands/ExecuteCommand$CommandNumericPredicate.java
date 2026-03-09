package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface CommandNumericPredicate {
  int test(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ExecuteCommand$CommandNumericPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */