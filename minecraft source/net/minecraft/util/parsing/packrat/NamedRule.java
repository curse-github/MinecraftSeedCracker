package net.minecraft.util.parsing.packrat;

public interface NamedRule<S, T> {
  Atom<T> name();
  
  Rule<S, T> value();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\NamedRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */