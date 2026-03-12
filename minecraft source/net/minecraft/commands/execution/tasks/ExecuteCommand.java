/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.ChainModifiers;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ import net.minecraft.commands.execution.TraceCallbacks;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ 
/*    */ public class ExecuteCommand<T extends ExecutionCommandSource<T>> extends Object implements UnboundEntryAction<T> {
/*    */   private final String commandInput;
/*    */   private final ChainModifiers modifiers;
/*    */   private final CommandContext<T> executionContext;
/*    */   
/*    */   public ExecuteCommand(String commandInput, ChainModifiers modifiers, CommandContext<T> executionContext) {
/* 19 */     this.commandInput = commandInput;
/* 20 */     this.modifiers = modifiers;
/* 21 */     this.executionContext = executionContext;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(T sender, ExecutionContext<T> context, Frame frame) {
/* 26 */     context.profiler().push(() -> "execute " + this.commandInput);
/*    */     try {
/* 28 */       context.incrementCost();
/* 29 */       int result = ContextChain.runExecutable(this.executionContext, sender, ExecutionCommandSource.resultConsumer(), this.modifiers.isForked());
/* 30 */       TraceCallbacks tracer = context.tracer();
/* 31 */       if (tracer != null) {
/* 32 */         tracer.onReturn(frame.depth(), this.commandInput, result);
/*    */       }
/* 34 */     } catch (CommandSyntaxException e) {
/* 35 */       sender.handleError(e, this.modifiers.isForked(), context.tracer());
/*    */     } finally {
/* 37 */       context.profiler().pop();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\ExecuteCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */