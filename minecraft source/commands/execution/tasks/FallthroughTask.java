/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class FallthroughTask<T extends ExecutionCommandSource<T>> extends Object implements EntryAction<T> {
/*  9 */   private static final FallthroughTask<? extends ExecutionCommandSource<?>> INSTANCE = new FallthroughTask();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static <T extends ExecutionCommandSource<T>> EntryAction<T> instance() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> context, Frame frame) {
/* 19 */     frame.returnFailure();
/*    */ 
/*    */     
/* 22 */     frame.discard();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\FallthroughTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */