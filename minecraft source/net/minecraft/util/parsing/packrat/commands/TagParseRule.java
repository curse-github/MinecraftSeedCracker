/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public class TagParseRule<T>
/*    */   extends Object
/*    */   implements Rule<StringReader, Dynamic<?>>
/*    */ {
/*    */   private final TagParser<T> parser;
/*    */   
/* 16 */   public TagParseRule(DynamicOps<T> ops) { this.parser = TagParser.create(ops); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Dynamic<T> parse(ParseState<StringReader> state) {
/* 21 */     ((StringReader)state.input()).skipWhitespace();
/* 22 */     int mark = state.mark();
/*    */     try {
/* 24 */       return new Dynamic(this.parser.getOps(), this.parser.parseAsArgument((StringReader)state.input()));
/* 25 */     } catch (Exception e) {
/* 26 */       state.errorCollector().store(mark, e);
/* 27 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\TagParseRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */