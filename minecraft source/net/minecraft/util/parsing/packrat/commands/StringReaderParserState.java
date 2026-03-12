/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.util.parsing.packrat.CachedParseState;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ 
/*    */ public class StringReaderParserState extends CachedParseState<StringReader> {
/*    */   private final StringReader input;
/*    */   
/*    */   public StringReaderParserState(ErrorCollector<StringReader> errorCollector, StringReader input) {
/* 11 */     super(errorCollector);
/* 12 */     this.input = input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public StringReader input() { return this.input; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int mark() { return this.input.getCursor(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void restore(int mark) { this.input.setCursor(mark); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderParserState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */