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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TerminalCharacters
/*    */   extends Object
/*    */   implements Term<StringReader>
/*    */ {
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   private final SuggestionSupplier<StringReader> suggestions;
/*    */   
/*    */   public TerminalCharacters(CharList values) {
/* 55 */     String joinedValues = (String)values.intStream().mapToObj(Character::toString).collect(Collectors.joining("|"));
/* 56 */     this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), joinedValues);
/* 57 */     this.suggestions = (s -> values.intStream().mapToObj(Character::toString));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean parse(ParseState<StringReader> state, Scope scope, Control control) {
/* 62 */     ((StringReader)state.input()).skipWhitespace();
/* 63 */     int cursor = state.mark();
/* 64 */     if (!((StringReader)state.input()).canRead() || !isAccepted(((StringReader)state.input()).read())) {
/* 65 */       state.errorCollector().store(cursor, this.suggestions, this.error);
/* 66 */       return false;
/*    */     } 
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract boolean isAccepted(char paramChar);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms$TerminalCharacters.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */