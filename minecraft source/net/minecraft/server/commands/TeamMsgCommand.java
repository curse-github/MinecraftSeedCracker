/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.OutgoingChatMessage;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public class TeamMsgCommand {
/* 27 */   private static final Style SUGGEST_STYLE = Style.EMPTY
/* 28 */     .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.type.team.hover")))
/* 29 */     .withClickEvent(new ClickEvent.SuggestCommand("/teammsg "));
/*    */   
/* 31 */   private static final SimpleCommandExceptionType ERROR_NOT_ON_TEAM = new SimpleCommandExceptionType(Component.translatable("commands.teammsg.failed.noteam"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 34 */     LiteralCommandNode<CommandSourceStack> msg = dispatcher.register(
/* 35 */         (LiteralArgumentBuilder)Commands.literal("teammsg")
/* 36 */         .then(
/* 37 */           Commands.argument("message", MessageArgument.message())
/* 38 */           .executes(c -> {
/* 39 */               CommandSourceStack source = (CommandSourceStack)c.getSource();
/* 40 */               Entity entity = source.getEntityOrException();
/* 41 */               PlayerTeam team = entity.getTeam();
/* 42 */               if (team == null) {
/* 43 */                 throw ERROR_NOT_ON_TEAM.create();
/*    */               }
/*    */ 
/*    */ 
/*    */               
/* 48 */               List<ServerPlayer> receivers = source.getServer().getPlayerList().getPlayers().stream().filter(()).toList();
/*    */               
/* 50 */               if (!receivers.isEmpty()) {
/* 51 */                 MessageArgument.resolveChatMessage(c, "message", ());
/*    */               }
/*    */ 
/*    */ 
/*    */               
/* 56 */               return receivers.size();
/*    */             })));
/*    */ 
/*    */     
/* 60 */     dispatcher.register((LiteralArgumentBuilder)Commands.literal("tm").redirect(msg));
/*    */   }
/*    */   
/*    */   private static void sendMessage(CommandSourceStack source, Entity entity, PlayerTeam team, List<ServerPlayer> receivers, PlayerChatMessage message) {
/* 64 */     MutableComponent mutableComponent = team.getFormattedDisplayName().withStyle(SUGGEST_STYLE);
/* 65 */     ChatType.Bound incomingChatType = ChatType.bind(ChatType.TEAM_MSG_COMMAND_INCOMING, source).withTargetName(mutableComponent);
/* 66 */     ChatType.Bound outgoingChatType = ChatType.bind(ChatType.TEAM_MSG_COMMAND_OUTGOING, source).withTargetName(mutableComponent);
/* 67 */     OutgoingChatMessage tracked = OutgoingChatMessage.create(message);
/*    */     
/* 69 */     boolean wasFullyFiltered = false;
/*    */     
/* 71 */     for (ServerPlayer teamPlayer : receivers) {
/* 72 */       ChatType.Bound chatType = (teamPlayer == entity) ? outgoingChatType : incomingChatType;
/*    */       
/* 74 */       boolean filtered = source.shouldFilterMessageTo(teamPlayer);
/* 75 */       teamPlayer.sendChatMessage(tracked, filtered, chatType);
/*    */       
/* 77 */       wasFullyFiltered |= ((filtered && message.isFullyFiltered()));
/*    */     } 
/*    */     
/* 80 */     if (wasFullyFiltered)
/* 81 */       source.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TeamMsgCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */