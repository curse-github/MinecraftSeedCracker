/*   */ package net.minecraft.commands.execution;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface UnboundEntryAction<T>
/*   */ {
/*   */   void execute(T paramT, ExecutionContext<T> paramExecutionContext, Frame paramFrame);
/*   */   
/* 8 */   default EntryAction<T> bind(T sender) { return (context, frame) -> execute(sender, context, frame); }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\UnboundEntryAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */