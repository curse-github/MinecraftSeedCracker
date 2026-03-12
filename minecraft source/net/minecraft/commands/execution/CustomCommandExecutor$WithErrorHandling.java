/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
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
/*    */ public abstract class WithErrorHandling<T extends ExecutionCommandSource<T>>
/*    */   extends Object
/*    */   implements CustomCommandExecutor<T>
/*    */ {
/*    */   public final void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/*    */     try {
/* 24 */       runGuarded(sender, currentStep, modifiers, output);
/* 25 */     } catch (CommandSyntaxException e) {
/* 26 */       onError(e, sender, modifiers, output.tracer());
/* 27 */       sender.callback().onFailure();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 32 */   protected void onError(CommandSyntaxException e, T sender, ChainModifiers modifiers, TraceCallbacks tracer) { sender.handleError(e, modifiers.isForked(), tracer); }
/*    */   
/*    */   protected abstract void runGuarded(T paramT, ContextChain<T> paramContextChain, ChainModifiers paramChainModifiers, ExecutionControl<T> paramExecutionControl);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\CustomCommandExecutor$WithErrorHandling.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */