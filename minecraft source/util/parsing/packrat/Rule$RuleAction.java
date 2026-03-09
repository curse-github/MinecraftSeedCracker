package net.minecraft.util.parsing.packrat;

@FunctionalInterface
public interface RuleAction<S, T> {
  T run(ParseState<S> paramParseState);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Rule$RuleAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */