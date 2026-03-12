/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.NamedRule;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Rule;
/*    */ 
/*    */ public abstract class ResourceLookupRule<C, V>
/*    */   extends Object implements Rule<StringReader, V>, ResourceSuggestion {
/*    */   private final NamedRule<StringReader, Identifier> idParser;
/*    */   protected final C context;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   
/*    */   protected ResourceLookupRule(NamedRule<StringReader, Identifier> idParser, C context) {
/* 19 */     this.idParser = idParser;
/* 20 */     this.context = context;
/* 21 */     this.error = DelayedException.create(Identifier.ERROR_INVALID);
/*    */   }
/*    */ 
/*    */   
/*    */   public V parse(ParseState<StringReader> state) {
/* 26 */     ((StringReader)state.input()).skipWhitespace();
/* 27 */     int mark = state.mark();
/*    */     
/* 29 */     Identifier id = (Identifier)state.parse(this.idParser);
/* 30 */     if (id != null) {
/*    */       try {
/* 32 */         return (V)validateElement((ImmutableStringReader)state.input(), id);
/* 33 */       } catch (Exception e) {
/* 34 */         state.errorCollector().store(mark, this, e);
/* 35 */         return null;
/*    */       } 
/*    */     }
/*    */     
/* 39 */     state.errorCollector().store(mark, this, this.error);
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   protected abstract V validateElement(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws Exception;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\ResourceLookupRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */