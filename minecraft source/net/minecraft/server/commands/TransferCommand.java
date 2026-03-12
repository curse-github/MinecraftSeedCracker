/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.common.ClientboundTransferPacket;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransferCommand
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType(Component.translatable("commands.transfer.error.no_players"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 29 */     dispatcher.register(
/* 30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("transfer")
/* 31 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 32 */         .then((
/* 33 */           (RequiredArgumentBuilder)Commands.argument("hostname", StringArgumentType.string())
/* 34 */           .executes(c -> transfer((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "hostname"), 25565, List.of(((CommandSourceStack)c.getSource()).getPlayerOrException()))))
/* 35 */           .then((
/* 36 */             (RequiredArgumentBuilder)Commands.argument("port", IntegerArgumentType.integer(1, 65535))
/* 37 */             .executes(c -> transfer((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "hostname"), IntegerArgumentType.getInteger(c, "port"), List.of(((CommandSourceStack)c.getSource()).getPlayerOrException()))))
/* 38 */             .then(
/* 39 */               Commands.argument("players", EntityArgument.players())
/* 40 */               .executes(c -> transfer((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "hostname"), IntegerArgumentType.getInteger(c, "port"), EntityArgument.getPlayers(c, "players")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int transfer(CommandSourceStack source, String hostname, int port, Collection<ServerPlayer> players) throws CommandSyntaxException {
/* 48 */     if (players.isEmpty()) {
/* 49 */       throw ERROR_NO_PLAYERS.create();
/*    */     }
/*    */     
/* 52 */     for (ServerPlayer player : players) {
/* 53 */       player.connection.send(new ClientboundTransferPacket(hostname, port));
/*    */     }
/* 55 */     if (players.size() == 1) {
/* 56 */       source.sendSuccess(() -> Component.translatable("commands.transfer.success.single", new Object[] { ((ServerPlayer)players.iterator().next()).getDisplayName(), hostname, Integer.valueOf(port) }), true);
/*    */     } else {
/* 58 */       source.sendSuccess(() -> Component.translatable("commands.transfer.success.multiple", new Object[] { Integer.valueOf(players.size()), hostname, Integer.valueOf(port) }), true);
/*    */     } 
/* 60 */     return players.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TransferCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */