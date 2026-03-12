/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HelpCommand
/*    */ {
/* 20 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.help.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 23 */     dispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help")
/* 25 */         .executes(s -> {
/* 26 */             Map<CommandNode<CommandSourceStack>, String> usage = dispatcher.getSmartUsage(dispatcher.getRoot(), (CommandSourceStack)s.getSource());
/* 27 */             for (String line : usage.values()) {
/* 28 */               ((CommandSourceStack)s.getSource()).sendSuccess((), false);
/*    */             }
/* 30 */             return usage.size();
/*    */           
/* 32 */           })).then(
/* 33 */           Commands.argument("command", StringArgumentType.greedyString())
/* 34 */           .executes(s -> {
/* 35 */               ParseResults<CommandSourceStack> command = dispatcher.parse(StringArgumentType.getString(s, "command"), (CommandSourceStack)s.getSource());
/* 36 */               if (command.getContext().getNodes().isEmpty()) {
/* 37 */                 throw ERROR_FAILED.create();
/*    */               }
/* 39 */               Map<CommandNode<CommandSourceStack>, String> usage = dispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast(command.getContext().getNodes())).getNode(), (CommandSourceStack)s.getSource());
/* 40 */               for (String line : usage.values()) {
/* 41 */                 ((CommandSourceStack)s.getSource()).sendSuccess((), false);
/*    */               }
/* 43 */               return usage.size();
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\HelpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */