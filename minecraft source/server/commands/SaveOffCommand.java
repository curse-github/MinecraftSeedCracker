/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SaveOffCommand {
/* 12 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OFF = new SimpleCommandExceptionType(Component.translatable("commands.save.alreadyOff"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 15 */     dispatcher.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-off")
/* 17 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 18 */         .executes(c -> {
/* 19 */             CommandSourceStack source = (CommandSourceStack)c.getSource();
/* 20 */             boolean success = source.getServer().setAutoSave(false);
/* 21 */             if (!success) {
/* 22 */               throw ERROR_ALREADY_OFF.create();
/*    */             }
/* 24 */             source.sendSuccess((), true);
/* 25 */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SaveOffCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */