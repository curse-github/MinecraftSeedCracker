package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;

interface SubFixer<F> {
  Typed<F> fix(Typed<?> paramTyped, Type<F> paramType);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FixProjectileStoredItem$SubFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */