/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ 
/*    */ public class ObjectiveArgument extends Object implements ArgumentType<String> {
/* 21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "*", "012" });
/* 22 */   private static final DynamicCommandExceptionType ERROR_OBJECTIVE_NOT_FOUND = new DynamicCommandExceptionType(name -> Component.translatableEscape("arguments.objective.notFound", new Object[] { name }));
/* 23 */   private static final DynamicCommandExceptionType ERROR_OBJECTIVE_READ_ONLY = new DynamicCommandExceptionType(name -> Component.translatableEscape("arguments.objective.readonly", new Object[] { name }));
/*    */ 
/*    */   
/* 26 */   public static ObjectiveArgument objective() { return new ObjectiveArgument(); }
/*    */ 
/*    */   
/*    */   public static Objective getObjective(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 30 */     String id = (String)context.getArgument(name, String.class);
/* 31 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/* 32 */     Objective objective = serverScoreboard.getObjective(id);
/* 33 */     if (objective == null) {
/* 34 */       throw ERROR_OBJECTIVE_NOT_FOUND.create(id);
/*    */     }
/* 36 */     return objective;
/*    */   }
/*    */   
/*    */   public static Objective getWritableObjective(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 40 */     Objective objective = getObjective(context, name);
/* 41 */     if (objective.getCriteria().isReadOnly()) {
/* 42 */       throw ERROR_OBJECTIVE_READ_ONLY.create(objective.getName());
/*    */     }
/* 44 */     return objective;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String parse(StringReader reader) throws CommandSyntaxException { return reader.readUnquotedString(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 54 */     S rawSource = (S)context.getSource();
/* 55 */     if (rawSource instanceof CommandSourceStack) { CommandSourceStack source = (CommandSourceStack)rawSource;
/* 56 */       return SharedSuggestionProvider.suggest(source.getServer().getScoreboard().getObjectiveNames(), builder); }
/* 57 */      if (rawSource instanceof SharedSuggestionProvider) { SharedSuggestionProvider source = (SharedSuggestionProvider)rawSource;
/* 58 */       return source.customSuggestion(context); }
/*    */     
/* 60 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ObjectiveArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */