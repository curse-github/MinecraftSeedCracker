/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class SaveAllCommand {
/* 14 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.save.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 17 */     dispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-all")
/* 19 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 20 */         .executes(c -> saveAll((CommandSourceStack)c.getSource(), false)))
/* 21 */         .then(
/* 22 */           Commands.literal("flush")
/* 23 */           .executes(c -> saveAll((CommandSourceStack)c.getSource(), true))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int saveAll(CommandSourceStack source, boolean flush) throws CommandSyntaxException {
/* 29 */     source.sendSuccess(() -> Component.translatable("commands.save.saving"), false);
/*    */     
/* 31 */     MinecraftServer server = source.getServer();
/* 32 */     boolean success = server.saveEverything(true, flush, true);
/*    */     
/* 34 */     if (!success) {
/* 35 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 38 */     source.sendSuccess(() -> Component.translatable("commands.save.success"), true);
/*    */     
/* 40 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SaveAllCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */