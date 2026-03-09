/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class FunctionCustomExecutor
/*     */   extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack>
/*     */   implements CustomCommandExecutor.CommandAdapter<CommandSourceStack>
/*     */ {
/*     */   public void runGuarded(CommandSourceStack sender, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) throws CommandSyntaxException {
/* 125 */     CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext().copyFor(sender);
/*     */     
/* 127 */     Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> nameAndFunctions = FunctionArgument.getFunctionCollection(currentContext, "name");
/* 128 */     Collection<CommandFunction<CommandSourceStack>> functions = (Collection)nameAndFunctions.getSecond();
/* 129 */     if (functions.isEmpty()) {
/* 130 */       throw FunctionCommand.ERROR_NO_FUNCTIONS.create(Component.translationArg((Identifier)nameAndFunctions.getFirst()));
/*     */     }
/*     */     
/* 133 */     CompoundTag arguments = arguments(currentContext);
/*     */     
/* 135 */     CommandSourceStack commonFunctionContext = FunctionCommand.modifySenderForExecution(sender);
/*     */     
/* 137 */     if (functions.size() == 1) {
/* 138 */       sender.sendSuccess(() -> Component.translatable("commands.function.scheduled.single", new Object[] { Component.translationArg(((CommandFunction)functions.iterator().next()).id()) }), true);
/*     */     } else {
/* 140 */       sender.sendSuccess(() -> Component.translatable("commands.function.scheduled.multiple", new Object[] { ComponentUtils.formatList(functions.stream().map(CommandFunction::id).toList(), Component::translationArg) }), true);
/*     */     } 
/*     */     
/* 143 */     FunctionCommand.queueFunctions(functions, arguments, sender, commonFunctionContext, output, FunctionCommand.FULL_CONTEXT_CALLBACKS, modifiers);
/*     */   }
/*     */   
/*     */   protected abstract CompoundTag arguments(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FunctionCommand$FunctionCustomExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */