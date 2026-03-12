/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public class UnquotedStringParseRule
/*    */   extends Object
/*    */   implements Rule<StringReader, String> {
/*    */   private final int minSize;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   
/*    */   public UnquotedStringParseRule(int minSize, DelayedException<CommandSyntaxException> error) {
/* 16 */     this.minSize = minSize;
/* 17 */     this.error = error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(ParseState<StringReader> state) {
/* 22 */     ((StringReader)state.input()).skipWhitespace();
/* 23 */     int cursor = state.mark();
/* 24 */     String value = ((StringReader)state.input()).readUnquotedString();
/* 25 */     if (value.length() < this.minSize) {
/* 26 */       state.errorCollector().store(cursor, this.error);
/* 27 */       return null;
/*    */     } 
/* 29 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\UnquotedStringParseRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */