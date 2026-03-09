package net.minecraft.commands.functions;

import java.util.List;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

public interface InstantiatedFunction<T> {
  Identifier id();
  
  List<UnboundEntryAction<T>> entries();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\functions\InstantiatedFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */