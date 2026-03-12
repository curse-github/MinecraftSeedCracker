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
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public class TeamArgument extends Object implements ArgumentType<String> {
/* 21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "123" });
/* 22 */   private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(name -> Component.translatableEscape("team.notFound", new Object[] { name }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static TeamArgument team() { return new TeamArgument(); }
/*    */ 
/*    */   
/*    */   public static PlayerTeam getTeam(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 32 */     String id = (String)context.getArgument(name, String.class);
/* 33 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/* 34 */     PlayerTeam team = serverScoreboard.getPlayerTeam(id);
/* 35 */     if (team == null) {
/* 36 */       throw ERROR_TEAM_NOT_FOUND.create(id);
/*    */     }
/* 38 */     return team;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public String parse(StringReader reader) throws CommandSyntaxException { return reader.readUnquotedString(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 48 */     if (contextBuilder.getSource() instanceof SharedSuggestionProvider) {
/* 49 */       return SharedSuggestionProvider.suggest(((SharedSuggestionProvider)contextBuilder.getSource()).getAllTeams(), builder);
/*    */     }
/* 51 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\TeamArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */