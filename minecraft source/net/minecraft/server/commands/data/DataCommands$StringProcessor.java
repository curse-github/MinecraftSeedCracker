package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
interface StringProcessor {
  String process(String paramString) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\DataCommands$StringProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */