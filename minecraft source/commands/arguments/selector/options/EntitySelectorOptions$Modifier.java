package net.minecraft.commands.arguments.selector.options;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

@FunctionalInterface
public interface Modifier {
  void handle(EntitySelectorParser paramEntitySelectorParser) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions$Modifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */