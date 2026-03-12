/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.ChainModifiers;
/*    */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*    */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*    */ import net.minecraft.commands.execution.ExecutionControl;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*    */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReturnCommand
/*    */ {
/*    */   public static <T extends ExecutionCommandSource<T>> void register(CommandDispatcher<T> dispatcher) {
/* 25 */     dispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)LiteralArgumentBuilder.literal("return")
/* 27 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .then(
/* 29 */           RequiredArgumentBuilder.argument("value", IntegerArgumentType.integer())
/* 30 */           .executes(new ReturnValueCustomExecutor())))
/*    */         
/* 32 */         .then(
/* 33 */           LiteralArgumentBuilder.literal("fail")
/* 34 */           .executes(new ReturnFailCustomExecutor())))
/*    */         
/* 36 */         .then(
/* 37 */           LiteralArgumentBuilder.literal("run")
/* 38 */           .forward(dispatcher.getRoot(), new ReturnFromCommandCustomModifier(), false)));
/*    */   }
/*    */   
/*    */   private static class ReturnValueCustomExecutor<T extends ExecutionCommandSource<T>>
/*    */     extends Object
/*    */     implements CustomCommandExecutor.CommandAdapter<T>
/*    */   {
/*    */     public void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/* 46 */       int returnValue = IntegerArgumentType.getInteger(currentStep.getTopContext(), "value");
/* 47 */       sender.callback().onSuccess(returnValue);
/* 48 */       Frame frame = output.currentFrame();
/* 49 */       frame.returnSuccess(returnValue);
/* 50 */       frame.discard();
/*    */     }
/*    */   }
/*    */   
/*    */   private static class ReturnFailCustomExecutor<T extends ExecutionCommandSource<T>>
/*    */     extends Object implements CustomCommandExecutor.CommandAdapter<T> {
/*    */     public void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/* 57 */       sender.callback().onFailure();
/* 58 */       Frame frame = output.currentFrame();
/* 59 */       frame.returnFailure();
/* 60 */       frame.discard();
/*    */     }
/*    */   }
/*    */   
/*    */   private static class ReturnFromCommandCustomModifier<T extends ExecutionCommandSource<T>>
/*    */     extends Object implements CustomModifierExecutor.ModifierAdapter<T> {
/*    */     public void apply(T originalSource, List<T> currentSources, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/* 67 */       if (currentSources.isEmpty()) {
/*    */ 
/*    */ 
/*    */         
/* 71 */         if (modifiers.isReturn()) {
/* 72 */           output.queueNext(FallthroughTask.instance());
/*    */         }
/*    */         
/*    */         return;
/*    */       } 
/*    */       
/* 78 */       output.currentFrame().discard();
/*    */       
/* 80 */       ContextChain<T> nextState = currentStep.nextStage();
/* 81 */       String command = nextState.getTopContext().getInput();
/*    */       
/* 83 */       output.queueNext(new BuildContexts.Continuation(command, nextState, modifiers.setReturn(), originalSource, currentSources));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ReturnCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */