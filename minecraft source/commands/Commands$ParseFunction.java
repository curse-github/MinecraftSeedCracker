package net.minecraft.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface ParseFunction {
  void parse(StringReader paramStringReader) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\Commands$ParseFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */