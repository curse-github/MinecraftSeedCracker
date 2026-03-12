/*   */ package net.minecraft.util.parsing.packrat;
/*   */ 
/*   */ import java.util.stream.Stream;
/*   */ 
/*   */ public interface SuggestionSupplier<S>
/*   */ {
/*   */   Stream<String> possibleValues(ParseState<S> paramParseState);
/*   */   
/* 9 */   static <S> SuggestionSupplier<S> empty() { return state -> Stream.empty(); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\SuggestionSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */