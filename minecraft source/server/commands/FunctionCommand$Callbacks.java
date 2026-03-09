package net.minecraft.server.commands;

import net.minecraft.resources.Identifier;

public interface Callbacks<T> {
  void signalResult(T paramT, Identifier paramIdentifier, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FunctionCommand$Callbacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */