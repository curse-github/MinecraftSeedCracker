/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.util.HttpUtil;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PublishCommand
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.publish.failed"));
/* 27 */   private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType(port -> Component.translatableEscape("commands.publish.alreadyPublished", new Object[] { port }));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 30 */     dispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("publish")
/* 32 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 33 */         .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), false, null)))
/* 34 */         .then((
/* 35 */           (RequiredArgumentBuilder)Commands.argument("allowCommands", BoolArgumentType.bool())
/* 36 */           .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(c, "allowCommands"), null)))
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)Commands.argument("gamemode", GameModeArgument.gameMode())
/* 39 */             .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(c, "allowCommands"), GameModeArgument.getGameMode(c, "gamemode"))))
/* 40 */             .then(
/* 41 */               Commands.argument("port", IntegerArgumentType.integer(0, 65535))
/* 42 */               .executes(c -> publish((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "port"), BoolArgumentType.getBool(c, "allowCommands"), GameModeArgument.getGameMode(c, "gamemode")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int publish(CommandSourceStack source, int port, boolean allowCommands, GameType type) throws CommandSyntaxException {
/* 50 */     if (source.getServer().isPublished()) {
/* 51 */       throw ERROR_ALREADY_PUBLISHED.create(Integer.valueOf(source.getServer().getPort()));
/*    */     }
/* 53 */     if (!source.getServer().publishServer(type, allowCommands, port)) {
/* 54 */       throw ERROR_FAILED.create();
/*    */     }
/* 56 */     source.sendSuccess(() -> getSuccessMessage(port), true);
/* 57 */     return port;
/*    */   }
/*    */   
/*    */   public static MutableComponent getSuccessMessage(int port) {
/* 61 */     MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(port));
/* 62 */     return Component.translatable("commands.publish.started", new Object[] { mutableComponent });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\PublishCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */