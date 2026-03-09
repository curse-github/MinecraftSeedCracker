/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NumberRunParseRule
/*    */   extends Object
/*    */   implements Rule<StringReader, String>
/*    */ {
/*    */   private final DelayedException<CommandSyntaxException> noValueError;
/*    */   private final DelayedException<CommandSyntaxException> underscoreNotAllowedError;
/*    */   
/*    */   public NumberRunParseRule(DelayedException<CommandSyntaxException> noValueError, DelayedException<CommandSyntaxException> underscoreNotAllowedError) {
/* 20 */     this.noValueError = noValueError;
/* 21 */     this.underscoreNotAllowedError = underscoreNotAllowedError;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(ParseState<StringReader> state) {
/* 26 */     StringReader input = (StringReader)state.input();
/* 27 */     input.skipWhitespace();
/* 28 */     String fullString = input.getString();
/* 29 */     int start = input.getCursor();
/* 30 */     int pos = start;
/* 31 */     while (pos < fullString.length() && isAccepted(fullString.charAt(pos))) {
/* 32 */       pos++;
/*    */     }
/*    */     
/* 35 */     int length = pos - start;
/* 36 */     if (length == 0) {
/* 37 */       state.errorCollector().store(state.mark(), this.noValueError);
/* 38 */       return null;
/*    */     } 
/*    */     
/* 41 */     if (fullString.charAt(start) == '_' || fullString.charAt(pos - 1) == '_') {
/* 42 */       state.errorCollector().store(state.mark(), this.underscoreNotAllowedError);
/* 43 */       return null;
/*    */     } 
/*    */     
/* 46 */     input.setCursor(pos);
/* 47 */     return fullString.substring(start, pos);
/*    */   }
/*    */   
/*    */   protected abstract boolean isAccepted(char paramChar);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\NumberRunParseRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */