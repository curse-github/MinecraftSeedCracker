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
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KickCommand
/*    */ {
/* 21 */   private static final SimpleCommandExceptionType ERROR_KICKING_OWNER = new SimpleCommandExceptionType(Component.translatable("commands.kick.owner.failed"));
/* 22 */   private static final SimpleCommandExceptionType ERROR_SINGLEPLAYER = new SimpleCommandExceptionType(Component.translatable("commands.kick.singleplayer.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 25 */     dispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kick")
/* 27 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 28 */         .then((
/* 29 */           (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/* 30 */           .executes(c -> kickPlayers((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Component.translatable("multiplayer.disconnect.kicked"))))
/* 31 */           .then(
/* 32 */             Commands.argument("reason", MessageArgument.message())
/* 33 */             .executes(c -> kickPlayers((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), MessageArgument.getMessage(c, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int kickPlayers(CommandSourceStack source, Collection<ServerPlayer> players, Component reason) throws CommandSyntaxException {
/* 40 */     if (!source.getServer().isPublished()) {
/* 41 */       throw ERROR_SINGLEPLAYER.create();
/*    */     }
/*    */     
/* 44 */     int count = 0;
/* 45 */     for (ServerPlayer player : players) {
/* 46 */       if (source.getServer().isSingleplayerOwner(player.nameAndId())) {
/*    */         continue;
/*    */       }
/* 49 */       player.connection.disconnect(reason);
/* 50 */       source.sendSuccess(() -> Component.translatable("commands.kick.success", new Object[] { player.getDisplayName(), reason }), true);
/* 51 */       count++;
/*    */     } 
/*    */     
/* 54 */     if (count == 0) {
/* 55 */       throw ERROR_KICKING_OWNER.create();
/*    */     }
/*    */     
/* 58 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\KickCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */