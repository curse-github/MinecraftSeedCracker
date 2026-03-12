/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public abstract class GreedyPredicateParseRule
/*    */   extends Object
/*    */   implements Rule<StringReader, String> {
/*    */   private final int minSize;
/*    */   private final int maxSize;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   
/* 16 */   public GreedyPredicateParseRule(int minSize, DelayedException<CommandSyntaxException> error) { this(minSize, 2147483647, error); }
/*    */ 
/*    */   
/*    */   public GreedyPredicateParseRule(int minSize, int maxSize, DelayedException<CommandSyntaxException> error) {
/* 20 */     this.minSize = minSize;
/* 21 */     this.maxSize = maxSize;
/* 22 */     this.error = error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(ParseState<StringReader> state) {
/* 27 */     StringReader input = (StringReader)state.input();
/* 28 */     String fullString = input.getString();
/* 29 */     int start = input.getCursor();
/* 30 */     int pos = start;
/* 31 */     while (pos < fullString.length() && isAccepted(fullString.charAt(pos)) && pos - start < this.maxSize) {
/* 32 */       pos++;
/*    */     }
/*    */     
/* 35 */     int length = pos - start;
/* 36 */     if (length < this.minSize) {
/* 37 */       state.errorCollector().store(state.mark(), this.error);
/* 38 */       return null;
/*    */     } 
/* 40 */     input.setCursor(pos);
/* 41 */     return fullString.substring(start, pos);
/*    */   }
/*    */   
/*    */   protected abstract boolean isAccepted(char paramChar);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\GreedyPredicateParseRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */