package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface DataManipulatorDecorator {
  ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator paramDataManipulator);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\DataCommands$DataManipulatorDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */