/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BanIpCommands
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_INVALID_IP = new SimpleCommandExceptionType(Component.translatable("commands.banip.invalid"));
/* 27 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.banip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 30 */     dispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban-ip")
/* 32 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 33 */         .then((
/* 34 */           (RequiredArgumentBuilder)Commands.argument("target", StringArgumentType.word())
/* 35 */           .executes(c -> banIpOrName((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "target"), null)))
/* 36 */           .then(
/* 37 */             Commands.argument("reason", MessageArgument.message())
/* 38 */             .executes(c -> banIpOrName((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "target"), MessageArgument.getMessage(c, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banIpOrName(CommandSourceStack source, String target, Component reason) throws CommandSyntaxException {
/* 45 */     if (InetAddresses.isInetAddress(target)) {
/* 46 */       return banIp(source, target, reason);
/*    */     }
/* 48 */     ServerPlayer player = source.getServer().getPlayerList().getPlayerByName(target);
/* 49 */     if (player != null) {
/* 50 */       return banIp(source, player.getIpAddress(), reason);
/*    */     }
/*    */     
/* 53 */     throw ERROR_INVALID_IP.create();
/*    */   }
/*    */   
/*    */   private static int banIp(CommandSourceStack source, String ip, Component reason) throws CommandSyntaxException {
/* 57 */     IpBanList list = source.getServer().getPlayerList().getIpBans();
/* 58 */     if (list.isBanned(ip)) {
/* 59 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/* 61 */     List<ServerPlayer> players = source.getServer().getPlayerList().getPlayersWithAddress(ip);
/* 62 */     IpBanListEntry entry = new IpBanListEntry(ip, null, source.getTextName(), null, (reason == null) ? null : reason.getString());
/* 63 */     list.add(entry);
/*    */     
/* 65 */     source.sendSuccess(() -> Component.translatable("commands.banip.success", new Object[] { ip, entry.getReasonMessage() }), true);
/* 66 */     if (!players.isEmpty()) {
/* 67 */       source.sendSuccess(() -> Component.translatable("commands.banip.info", new Object[] { Integer.valueOf(players.size()), EntitySelector.joinNames(players) }), true);
/*    */     }
/*    */     
/* 70 */     for (ServerPlayer player : players) {
/* 71 */       player.connection.disconnect(Component.translatable("multiplayer.disconnect.ip_banned"));
/*    */     }
/*    */     
/* 74 */     return players.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\BanIpCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */