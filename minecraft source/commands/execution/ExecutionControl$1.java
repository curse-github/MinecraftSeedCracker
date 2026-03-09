/*    */ package net.minecraft.commands.execution;
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
/*    */ class null
/*    */   extends Object
/*    */   implements ExecutionControl<T>
/*    */ {
/* 19 */   public void queueNext(EntryAction<T> action) { context.queueNext(new CommandQueueEntry(frame, action)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void tracer(TraceCallbacks tracer) { context.tracer(tracer); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public TraceCallbacks tracer() { return context.tracer(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public Frame currentFrame() { return frame; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\ExecutionControl$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */