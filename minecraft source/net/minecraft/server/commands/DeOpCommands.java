/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class DeOpCommands
/*    */ {
/* 21 */   private static final SimpleCommandExceptionType ERROR_NOT_OP = new SimpleCommandExceptionType(Component.translatable("commands.deop.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 24 */     dispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deop")
/* 26 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", GameProfileArgument.gameProfile())
/* 29 */           .suggests((c, p) -> SharedSuggestionProvider.suggest(((CommandSourceStack)c.getSource()).getServer().getPlayerList().getOpNames(), p))
/* 30 */           .executes(c -> deopPlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int deopPlayers(CommandSourceStack source, Collection<NameAndId> players) throws CommandSyntaxException {
/* 36 */     PlayerList list = source.getServer().getPlayerList();
/* 37 */     int count = 0;
/*    */     
/* 39 */     for (NameAndId player : players) {
/* 40 */       if (list.isOp(player)) {
/* 41 */         list.deop(player);
/* 42 */         count++;
/* 43 */         source.sendSuccess(() -> Component.translatable("commands.deop.success", new Object[] { ((NameAndId)players.iterator().next()).name() }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     if (count == 0) {
/* 48 */       throw ERROR_NOT_OP.create();
/*    */     }
/*    */     
/* 51 */     source.getServer().kickUnlistedPlayers();
/* 52 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DeOpCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */