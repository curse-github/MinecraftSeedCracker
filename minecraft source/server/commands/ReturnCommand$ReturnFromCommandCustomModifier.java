/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.ChainModifiers;
/*    */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*    */ import net.minecraft.commands.execution.ExecutionControl;
/*    */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*    */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ReturnFromCommandCustomModifier<T extends ExecutionCommandSource<T>>
/*    */   extends Object
/*    */   implements CustomModifierExecutor.ModifierAdapter<T>
/*    */ {
/*    */   public void apply(T originalSource, List<T> currentSources, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/* 67 */     if (currentSources.isEmpty()) {
/*    */ 
/*    */ 
/*    */       
/* 71 */       if (modifiers.isReturn()) {
/* 72 */         output.queueNext(FallthroughTask.instance());
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 78 */     output.currentFrame().discard();
/*    */     
/* 80 */     ContextChain<T> nextState = currentStep.nextStage();
/* 81 */     String command = nextState.getTopContext().getInput();
/*    */     
/* 83 */     output.queueNext(new BuildContexts.Continuation(command, nextState, modifiers.setReturn(), originalSource, currentSources));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ReturnCommand$ReturnFromCommandCustomModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */