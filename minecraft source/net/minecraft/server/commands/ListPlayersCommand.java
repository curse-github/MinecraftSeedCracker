/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ListPlayersCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 18 */     dispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/* 20 */         .executes(c -> listPlayers((CommandSourceStack)c.getSource())))
/* 21 */         .then(
/* 22 */           Commands.literal("uuids")
/* 23 */           .executes(c -> listPlayersWithUuids((CommandSourceStack)c.getSource()))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   private static int listPlayers(CommandSourceStack source) { return format(source, Player::getDisplayName); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   private static int listPlayersWithUuids(CommandSourceStack source) { return format(source, player -> Component.translatable("commands.list.nameAndId", new Object[] { player.getName(), Component.translationArg(player.getGameProfile().id()) })); }
/*    */ 
/*    */   
/*    */   private static int format(CommandSourceStack source, Function<ServerPlayer, Component> formatter) {
/* 37 */     PlayerList playerList = source.getServer().getPlayerList();
/* 38 */     List<ServerPlayer> players = playerList.getPlayers();
/* 39 */     Component listComponent = ComponentUtils.formatList(players, formatter);
/* 40 */     source.sendSuccess(() -> Component.translatable("commands.list.players", new Object[] { Integer.valueOf(players.size()), Integer.valueOf(playerList.getMaxPlayers()), listComponent }), false);
/* 41 */     return players.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ListPlayersCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */