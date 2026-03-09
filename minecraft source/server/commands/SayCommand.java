/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class SayCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 16 */     dispatcher.register(
/* 17 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("say")
/* 18 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 19 */         .then(
/* 20 */           Commands.argument("message", MessageArgument.message())
/* 21 */           .executes(c -> {
/* 22 */               MessageArgument.resolveChatMessage(c, "message", ());
/*    */ 
/*    */ 
/*    */ 
/*    */               
/* 27 */               return 1;
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SayCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */