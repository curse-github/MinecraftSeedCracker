/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.Util;
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
/*     */ class TraceCustomExecutor
/*     */   extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack>
/*     */   implements CustomCommandExecutor.CommandAdapter<CommandSourceStack>
/*     */ {
/*     */   public void runGuarded(CommandSourceStack source, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> context) throws CommandSyntaxException {
/*  99 */     if (modifiers.isReturn()) {
/* 100 */       throw DebugCommand.NO_RETURN_RUN.create();
/*     */     }
/*     */     
/* 103 */     if (context.tracer() != null) {
/* 104 */       throw DebugCommand.NO_RECURSIVE_TRACES.create();
/*     */     }
/* 106 */     CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext();
/*     */     
/* 108 */     Collection<CommandFunction<CommandSourceStack>> functions = FunctionArgument.getFunctions(currentContext, "name");
/*     */     
/* 110 */     MinecraftServer server = source.getServer();
/* 111 */     String outputName = "debug-trace-" + Util.getFilenameFormattedDateTime() + ".txt";
/*     */     
/* 113 */     CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getFunctions().getDispatcher();
/*     */     
/* 115 */     int commandCount = 0;
/*     */     try {
/* 117 */       Path dirPath = server.getFile("debug");
/* 118 */       Files.createDirectories(dirPath, new java.nio.file.attribute.FileAttribute[0]);
/*     */       
/* 120 */       final PrintWriter output = new PrintWriter(Files.newBufferedWriter(dirPath.resolve(outputName), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]));
/* 121 */       DebugCommand.Tracer tracer = new DebugCommand.Tracer(output);
/* 122 */       context.tracer(tracer);
/*     */       
/* 124 */       for (CommandFunction<CommandSourceStack> function : functions) {
/*     */         try {
/* 126 */           CommandSourceStack functionSource = source.withSource(tracer).withMaximumPermission(LevelBasedPermissionSet.GAMEMASTER);
/*     */           
/* 128 */           InstantiatedFunction<CommandSourceStack> instantiatedFunction = function.instantiate(null, dispatcher);
/* 129 */           context.queueNext((new CallFunction<CommandSourceStack>(this, instantiatedFunction, CommandResultCallback.EMPTY, false)
/*     */               {
/*     */                 public void execute(CommandSourceStack sender, ExecutionContext<CommandSourceStack> context, Frame frame) {
/* 132 */                   output.println(function.id());
/* 133 */                   super.execute(sender, context, frame);
/*     */                 }
/* 135 */               }).bind(functionSource));
/* 136 */           commandCount += instantiatedFunction.entries().size();
/* 137 */         } catch (FunctionInstantiationException exception) {
/* 138 */           source.sendFailure(exception.messageComponent());
/*     */         } 
/*     */       } 
/* 141 */     } catch (UncheckedIOException|java.io.IOException e) {
/* 142 */       DebugCommand.LOGGER.warn("Tracing failed", e);
/* 143 */       source.sendFailure(Component.translatable("commands.debug.function.traceFailed"));
/*     */     } 
/*     */     
/* 146 */     int finalCommandCount = commandCount;
/* 147 */     context.queueNext((c, frame) -> {
/* 148 */           if (functions.size() == 1) {
/* 149 */             source.sendSuccess((), true);
/*     */           } else {
/* 151 */             source.sendSuccess((), true);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugCommand$TraceCustomExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */