/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class StopCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 12 */     dispatcher.register(
/* 13 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop")
/* 14 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 15 */         .executes(c -> {
/* 16 */             ((CommandSourceStack)c.getSource()).sendSuccess((), true);
/* 17 */             ((CommandSourceStack)c.getSource()).getServer().halt(false);
/* 18 */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\StopCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */