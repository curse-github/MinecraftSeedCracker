/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.parsing.packrat.Control;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Scope;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ import net.minecraft.util.parsing.packrat.Term;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TerminalWord
/*    */   extends Object
/*    */   implements Term<StringReader>
/*    */ {
/*    */   private final String value;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   private final SuggestionSupplier<StringReader> suggestions;
/*    */   
/*    */   public TerminalWord(String value) {
/* 27 */     this.value = value;
/* 28 */     this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), value);
/* 29 */     this.suggestions = (s -> Stream.of(value));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean parse(ParseState<StringReader> state, Scope scope, Control control) {
/* 34 */     ((StringReader)state.input()).skipWhitespace();
/* 35 */     int cursor = state.mark();
/* 36 */     String value = ((StringReader)state.input()).readUnquotedString();
/* 37 */     if (!value.equals(this.value)) {
/* 38 */       state.errorCollector().store(cursor, this.suggestions, this.error);
/* 39 */       return false;
/*    */     } 
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public String toString() { return "terminal[" + this.value + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms$TerminalWord.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */