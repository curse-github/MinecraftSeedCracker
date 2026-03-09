/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.execution.CommandQueueEntry;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class ContinuationTask<T, P>
/*    */   extends Object implements EntryAction<T> {
/*    */   private final TaskProvider<T, P> taskFactory;
/*    */   private final List<P> arguments;
/*    */   private final CommandQueueEntry<T> selfEntry;
/*    */   private int index;
/*    */   
/*    */   private ContinuationTask(TaskProvider<T, P> taskFactory, List<P> arguments, Frame frame) {
/* 17 */     this.taskFactory = taskFactory;
/* 18 */     this.arguments = arguments;
/* 19 */     this.selfEntry = new CommandQueueEntry(frame, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> context, Frame frame) {
/* 24 */     P argument = (P)this.arguments.get(this.index);
/* 25 */     context.queueNext(this.taskFactory.create(frame, argument));
/* 26 */     if (++this.index < this.arguments.size()) {
/* 27 */       context.queueNext(this.selfEntry);
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T, P> void schedule(ExecutionContext<T> context, Frame frame, List<P> arguments, TaskProvider<T, P> taskFactory) {
/* 32 */     int argumentCount = arguments.size();
/* 33 */     switch (argumentCount) { case 0: return;
/*    */       case 1:
/* 35 */         context.queueNext(taskFactory.create(frame, arguments.get(0)));
/*    */ 
/*    */       
/*    */       case 2:
/* 39 */         context.queueNext(taskFactory.create(frame, arguments.get(0)));
/* 40 */         context.queueNext(taskFactory.create(frame, arguments.get(1))); }
/*    */     
/* 42 */     context.queueNext((new ContinuationTask(taskFactory, arguments, frame)).selfEntry);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TaskProvider<T, P> {
/*    */     CommandQueueEntry<T> create(Frame param1Frame, P param1P);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\ContinuationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */