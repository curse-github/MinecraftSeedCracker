/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.Command;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ 
/*    */ public interface CustomCommandExecutor<T>
/*    */ {
/*    */   void run(T paramT, ContextChain<T> paramContextChain, ChainModifiers paramChainModifiers, ExecutionControl<T> paramExecutionControl);
/*    */   
/*    */   public static interface CommandAdapter<T>
/*    */     extends CustomCommandExecutor<T>, Command<T>
/*    */   {
/* 16 */     default int run(CommandContext<T> context) throws CommandSyntaxException { throw new UnsupportedOperationException("This function should not run"); }
/*    */   }
/*    */   
/*    */   public static abstract class WithErrorHandling<T extends ExecutionCommandSource<T>>
/*    */     extends Object
/*    */     implements CustomCommandExecutor<T> {
/*    */     public final void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/*    */       try {
/* 24 */         runGuarded(sender, currentStep, modifiers, output);
/* 25 */       } catch (CommandSyntaxException e) {
/* 26 */         onError(e, sender, modifiers, output.tracer());
/* 27 */         sender.callback().onFailure();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 32 */     protected void onError(CommandSyntaxException e, T sender, ChainModifiers modifiers, TraceCallbacks tracer) { sender.handleError(e, modifiers.isForked(), tracer); }
/*    */     
/*    */     protected abstract void runGuarded(T param1T, ContextChain<T> param1ContextChain, ChainModifiers param1ChainModifiers, ExecutionControl<T> param1ExecutionControl);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\CustomCommandExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */