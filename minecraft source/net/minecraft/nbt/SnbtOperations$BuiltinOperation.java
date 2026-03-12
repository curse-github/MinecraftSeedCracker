package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import net.minecraft.util.parsing.packrat.ParseState;

public interface BuiltinOperation {
  <T> T run(DynamicOps<T> paramDynamicOps, List<T> paramList, ParseState<StringReader> paramParseState);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\SnbtOperations$BuiltinOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */