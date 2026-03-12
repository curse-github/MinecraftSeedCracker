/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ExecutionControl<T>
/*    */ {
/*    */   void queueNext(EntryAction<T> paramEntryAction);
/*    */   
/*    */   void tracer(TraceCallbacks paramTraceCallbacks);
/*    */   
/*    */   TraceCallbacks tracer();
/*    */   
/*    */   Frame currentFrame();
/*    */   
/*    */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> ExecutionControl<T> create(final ExecutionContext<T> context, final Frame frame) {
/* 16 */     return new ExecutionControl<T>()
/*    */       {
/*    */         public void queueNext(EntryAction<T> action) {
/* 19 */           context.queueNext(new CommandQueueEntry(frame, action));
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 24 */         public void tracer(TraceCallbacks tracer) { context.tracer(tracer); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 29 */         public TraceCallbacks tracer() { return context.tracer(); }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 34 */         public Frame currentFrame() { return frame; }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\ExecutionControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */