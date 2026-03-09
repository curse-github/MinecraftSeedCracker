/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.permissions.PermissionCheck;
/*    */ import net.minecraft.server.permissions.Permissions;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ public class GameModeCommand
/*    */ {
/* 25 */   public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 28 */     dispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("gamemode")
/* 30 */         .requires(Commands.hasPermission(PERMISSION_CHECK)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("gamemode", GameModeArgument.gameMode())
/* 33 */           .executes(c -> setMode(c, Collections.singleton(((CommandSourceStack)c.getSource()).getPlayerOrException()), GameModeArgument.getGameMode(c, "gamemode"))))
/* 34 */           .then(
/* 35 */             Commands.argument("target", EntityArgument.players())
/* 36 */             .executes(c -> setMode(c, EntityArgument.getPlayers(c, "target"), GameModeArgument.getGameMode(c, "gamemode"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void logGamemodeChange(CommandSourceStack source, ServerPlayer target, GameType newType) {
/* 43 */     MutableComponent mutableComponent = Component.translatable("gameMode." + newType.getName());
/* 44 */     if (source.getEntity() == target) {
/* 45 */       source.sendSuccess(() -> Component.translatable("commands.gamemode.success.self", new Object[] { mode }), true);
/*    */     } else {
/* 47 */       if (((Boolean)source.getLevel().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue()) {
/* 48 */         target.sendSystemMessage(Component.translatable("gameMode.changed", new Object[] { mutableComponent }));
/*    */       }
/*    */       
/* 51 */       source.sendSuccess(() -> Component.translatable("commands.gamemode.success.other", new Object[] { target.getDisplayName(), mode }), true);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int setMode(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, GameType type) {
/* 56 */     int count = 0;
/* 57 */     for (ServerPlayer player : players) {
/* 58 */       if (setGameMode((CommandSourceStack)context.getSource(), player, type)) {
/* 59 */         count++;
/*    */       }
/*    */     } 
/* 62 */     return count;
/*    */   }
/*    */ 
/*    */   
/* 66 */   public static void setGameMode(ServerPlayer player, GameType type) { setGameMode(player.createCommandSourceStack(), player, type); }
/*    */ 
/*    */   
/*    */   private static boolean setGameMode(CommandSourceStack source, ServerPlayer player, GameType type) {
/* 70 */     if (player.setGameMode(type)) {
/* 71 */       logGamemodeChange(source, player, type);
/* 72 */       return true;
/*    */     } 
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\GameModeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */