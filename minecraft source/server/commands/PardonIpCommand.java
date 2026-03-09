/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ 
/*    */ public class PardonIpCommand {
/* 19 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(Component.translatable("commands.pardonip.invalid"));
/* 20 */   private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.pardonip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 23 */     dispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon-ip")
/* 25 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 26 */         .then(
/* 27 */           Commands.argument("target", StringArgumentType.word())
/* 28 */           .suggests((c, p) -> SharedSuggestionProvider.suggest(((CommandSourceStack)c.getSource()).getServer().getPlayerList().getIpBans().getUserList(), p))
/* 29 */           .executes(c -> unban((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "target")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int unban(CommandSourceStack source, String ip) throws CommandSyntaxException {
/* 35 */     if (!InetAddresses.isInetAddress(ip)) {
/* 36 */       throw ERROR_INVALID.create();
/*    */     }
/*    */     
/* 39 */     IpBanList bans = source.getServer().getPlayerList().getIpBans();
/* 40 */     if (!bans.isBanned(ip)) {
/* 41 */       throw ERROR_NOT_BANNED.create();
/*    */     }
/*    */     
/* 44 */     bans.remove(ip);
/* 45 */     source.sendSuccess(() -> Component.translatable("commands.pardonip.success", new Object[] { ip }), true);
/* 46 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\PardonIpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */