package net.minecraft.network;

import java.util.function.Function;
import net.minecraft.core.component.TypedDataComponent;

@FunctionalInterface
public interface HashGenerator extends Function<TypedDataComponent<?>, Integer> {}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\HashedPatchMap$HashGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */