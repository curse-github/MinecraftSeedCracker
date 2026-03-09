package net.minecraft.commands.synchronization;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;

public interface Template<A extends ArgumentType<?>> {
  A instantiate(CommandBuildContext paramCommandBuildContext);
  
  ArgumentTypeInfo<A, ?> type();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */