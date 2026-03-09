package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

interface Entry<T> {
  IntList parameters();
  
  UnboundEntryAction<T> instantiate(List<String> paramList, CommandDispatcher<T> paramCommandDispatcher, Identifier paramIdentifier) throws FunctionInstantiationException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\functions\MacroFunction$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */