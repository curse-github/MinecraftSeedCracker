/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import it.unimi.dsi.fastutil.chars.CharList;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.parsing.packrat.Control;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Scope;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ import net.minecraft.util.parsing.packrat.Term;
/*    */ 
/*    */ 
/*    */ public interface StringReaderTerms
/*    */ {
/* 18 */   static Term<StringReader> word(String value) { return new TerminalWord(value); }
/*    */   
/*    */   public static final class TerminalWord
/*    */     extends Object implements Term<StringReader> {
/*    */     private final String value;
/*    */     private final DelayedException<CommandSyntaxException> error;
/*    */     private final SuggestionSupplier<StringReader> suggestions;
/*    */     
/*    */     public TerminalWord(String value) {
/* 27 */       this.value = value;
/* 28 */       this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), value);
/* 29 */       this.suggestions = (s -> Stream.of(value));
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean parse(ParseState<StringReader> state, Scope scope, Control control) {
/* 34 */       ((StringReader)state.input()).skipWhitespace();
/* 35 */       int cursor = state.mark();
/* 36 */       String value = ((StringReader)state.input()).readUnquotedString();
/* 37 */       if (!value.equals(this.value)) {
/* 38 */         state.errorCollector().store(cursor, this.suggestions, this.error);
/* 39 */         return false;
/*    */       } 
/* 41 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 46 */     public String toString() { return "terminal[" + this.value + "]"; }
/*    */   }
/*    */   
/*    */   public static abstract class TerminalCharacters
/*    */     extends Object implements Term<StringReader> {
/*    */     private final DelayedException<CommandSyntaxException> error;
/*    */     private final SuggestionSupplier<StringReader> suggestions;
/*    */     
/*    */     public TerminalCharacters(CharList values) {
/* 55 */       String joinedValues = (String)values.intStream().mapToObj(Character::toString).collect(Collectors.joining("|"));
/* 56 */       this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), joinedValues);
/* 57 */       this.suggestions = (s -> values.intStream().mapToObj(Character::toString));
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean parse(ParseState<StringReader> state, Scope scope, Control control) {
/* 62 */       ((StringReader)state.input()).skipWhitespace();
/* 63 */       int cursor = state.mark();
/* 64 */       if (!((StringReader)state.input()).canRead() || !isAccepted(((StringReader)state.input()).read())) {
/* 65 */         state.errorCollector().store(cursor, this.suggestions, this.error);
/* 66 */         return false;
/*    */       } 
/* 68 */       return true;
/*    */     }
/*    */     
/*    */     protected abstract boolean isAccepted(char param1Char);
/*    */   }
/*    */   
/*    */   static Term<StringReader> character(final char value) {
/* 75 */     return new TerminalCharacters(CharList.of(value))
/*    */       {
/*    */         protected boolean isAccepted(char v) {
/* 78 */           return (value == v);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static Term<StringReader> characters(final char v1, final char v2) {
/* 84 */     return new TerminalCharacters(CharList.of(v1, v2))
/*    */       {
/*    */         protected boolean isAccepted(char v) {
/* 87 */           return (v == v1 || v == v2);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static StringReader createReader(String contents, int cursor) {
/* 93 */     StringReader reader = new StringReader(contents);
/* 94 */     reader.setCursor(cursor);
/* 95 */     return reader;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */