/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public class SeedCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean checkPermissions) {
/* 13 */     dispatcher.register(
/* 14 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed")
/* 15 */         .requires(Commands.hasPermission(checkPermissions ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL)))
/* 16 */         .executes(c -> {
/* 17 */             long seed = ((CommandSourceStack)c.getSource()).getLevel().getSeed();
/* 18 */             MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(seed));
/* 19 */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/* 20 */             return (int)seed;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SeedCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */