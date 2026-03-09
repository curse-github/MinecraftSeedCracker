/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public abstract class ParserBasedArgument<T>
/*    */   extends Object
/*    */   implements ArgumentType<T> {
/*    */   private final CommandArgumentParser<T> parser;
/*    */   
/* 16 */   public ParserBasedArgument(CommandArgumentParser<T> parser) { this.parser = parser; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public T parse(StringReader reader) throws CommandSyntaxException { return (T)this.parser.parseForCommands(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return this.parser.parseForSuggestions(builder); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\ParserBasedArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */