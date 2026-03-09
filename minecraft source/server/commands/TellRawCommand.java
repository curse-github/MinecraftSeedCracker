/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.ComponentArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ 
/*    */ public class TellRawCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 18 */     dispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw")
/* 20 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 21 */         .then(
/* 22 */           Commands.argument("targets", EntityArgument.players())
/* 23 */           .then(
/* 24 */             Commands.argument("message", ComponentArgument.textComponent(context))
/* 25 */             .executes(c -> {
/* 26 */                 int result = 0;
/* 27 */                 for (ServerPlayer player : EntityArgument.getPlayers(c, "targets")) {
/* 28 */                   player.sendSystemMessage(ComponentArgument.getResolvedComponent(c, "message", player), false);
/* 29 */                   result++;
/*    */                 } 
/* 31 */                 return result;
/*    */               }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TellRawCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */