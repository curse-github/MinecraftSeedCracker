/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.OutgoingChatMessage;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ 
/*    */ public class MsgCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 23 */     LiteralCommandNode<CommandSourceStack> msg = dispatcher.register(
/* 24 */         (LiteralArgumentBuilder)Commands.literal("msg")
/* 25 */         .then(
/* 26 */           Commands.argument("targets", EntityArgument.players())
/* 27 */           .then(
/* 28 */             Commands.argument("message", MessageArgument.message())
/* 29 */             .executes(c -> {
/* 30 */                 Collection<ServerPlayer> players = EntityArgument.getPlayers(c, "targets");
/* 31 */                 if (!players.isEmpty()) {
/* 32 */                   MessageArgument.resolveChatMessage(c, "message", ());
/*    */                 }
/*    */ 
/*    */                 
/* 36 */                 return players.size();
/*    */               }))));
/*    */ 
/*    */ 
/*    */     
/* 41 */     dispatcher.register((LiteralArgumentBuilder)Commands.literal("tell").redirect(msg));
/* 42 */     dispatcher.register((LiteralArgumentBuilder)Commands.literal("w").redirect(msg));
/*    */   }
/*    */   
/*    */   private static void sendMessage(CommandSourceStack source, Collection<ServerPlayer> players, PlayerChatMessage message) {
/* 46 */     ChatType.Bound incomingChatType = ChatType.bind(ChatType.MSG_COMMAND_INCOMING, source);
/* 47 */     OutgoingChatMessage tracked = OutgoingChatMessage.create(message);
/*    */     
/* 49 */     boolean wasFullyFiltered = false;
/*    */     
/* 51 */     for (ServerPlayer player : players) {
/*    */       
/* 53 */       ChatType.Bound outgoingChatType = ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, source).withTargetName(player.getDisplayName());
/* 54 */       source.sendChatMessage(tracked, false, outgoingChatType);
/*    */       
/* 56 */       boolean filtered = source.shouldFilterMessageTo(player);
/* 57 */       player.sendChatMessage(tracked, filtered, incomingChatType);
/*    */       
/* 59 */       wasFullyFiltered |= ((filtered && message.isFullyFiltered()));
/*    */     } 
/*    */     
/* 62 */     if (wasFullyFiltered)
/* 63 */       source.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\MsgCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */