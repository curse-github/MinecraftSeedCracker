/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
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
/*    */ class null
/*    */   extends Object
/*    */   implements CommandArgumentParser<S>
/*    */ {
/* 24 */   public S parseForCommands(StringReader reader) throws CommandSyntaxException { return (S)mapper.apply(CommandArgumentParser.this.parseForCommands(reader)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) { return CommandArgumentParser.this.parseForSuggestions(suggestionsBuilder); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\CommandArgumentParser$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */