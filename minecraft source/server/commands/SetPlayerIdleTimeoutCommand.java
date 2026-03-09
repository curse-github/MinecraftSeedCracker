/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SetPlayerIdleTimeoutCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 15 */     dispatcher.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setidletimeout")
/* 17 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 18 */         .then(
/* 19 */           Commands.argument("minutes", IntegerArgumentType.integer(0))
/* 20 */           .executes(c -> setIdleTimeout((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "minutes")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setIdleTimeout(CommandSourceStack source, int time) {
/* 26 */     source.getServer().setPlayerIdleTimeout(time);
/* 27 */     if (time > 0) {
/* 28 */       source.sendSuccess(() -> Component.translatable("commands.setidletimeout.success", new Object[] { Integer.valueOf(time) }), true);
/*    */     } else {
/* 30 */       source.sendSuccess(() -> Component.translatable("commands.setidletimeout.success.disabled"), true);
/*    */     } 
/* 32 */     return time;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SetPlayerIdleTimeoutCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */