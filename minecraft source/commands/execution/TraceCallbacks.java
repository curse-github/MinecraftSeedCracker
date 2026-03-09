package net.minecraft.commands.execution;

import net.minecraft.resources.Identifier;

public interface TraceCallbacks extends AutoCloseable {
  void onCommand(int paramInt, String paramString);
  
  void onReturn(int paramInt1, String paramString, int paramInt2);
  
  void onError(String paramString);
  
  void onCall(int paramInt1, Identifier paramIdentifier, int paramInt2);
  
  void close();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\TraceCallbacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */