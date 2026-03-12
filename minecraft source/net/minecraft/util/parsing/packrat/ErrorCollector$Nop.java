package net.minecraft.util.parsing.packrat;

public class Nop<S> extends Object implements ErrorCollector<S> {
  public void store(int cursor, SuggestionSupplier<S> suggestions, Object reason) {}
  
  public void finish(int finalCursor) {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\ErrorCollector$Nop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */