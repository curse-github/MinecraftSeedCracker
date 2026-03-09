/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanList;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BanPlayerCommands
/*    */ {
/* 25 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.ban.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 28 */     dispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban")
/* 30 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("targets", GameProfileArgument.gameProfile())
/* 33 */           .executes(c -> banPlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets"), null)))
/* 34 */           .then(
/* 35 */             Commands.argument("reason", MessageArgument.message())
/* 36 */             .executes(c -> banPlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets"), MessageArgument.getMessage(c, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banPlayers(CommandSourceStack source, Collection<NameAndId> players, Component reason) throws CommandSyntaxException {
/* 43 */     UserBanList list = source.getServer().getPlayerList().getBans();
/* 44 */     int count = 0;
/*    */     
/* 46 */     for (NameAndId player : players) {
/* 47 */       if (!list.isBanned(player)) {
/* 48 */         UserBanListEntry entry = new UserBanListEntry(player, null, source.getTextName(), null, (reason == null) ? null : reason.getString());
/* 49 */         list.add(entry);
/* 50 */         count++;
/* 51 */         source.sendSuccess(() -> Component.translatable("commands.ban.success", new Object[] { Component.literal(player.name()), entry.getReasonMessage() }), true);
/*    */         
/* 53 */         ServerPlayer online = source.getServer().getPlayerList().getPlayer(player.id());
/* 54 */         if (online != null) {
/* 55 */           online.connection.disconnect(Component.translatable("multiplayer.disconnect.banned"));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     if (count == 0) {
/* 61 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/*    */     
/* 64 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\BanPlayerCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */