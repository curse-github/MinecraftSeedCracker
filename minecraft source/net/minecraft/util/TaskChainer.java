/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TaskChainer
/*    */ {
/* 12 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   static TaskChainer immediate(final Executor executor) {
/* 15 */     return new TaskChainer()
/*    */       {
/*    */         public <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask) {
/* 18 */           preparation.thenAcceptAsync(chainedTask, executor).exceptionally(e -> {
/* 19 */                 LOGGER.error("Task failed", e);
/* 20 */                 return null;
/*    */               });
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 27 */   default void append(Runnable task) { append(CompletableFuture.completedFuture(null), ignored -> task.run()); }
/*    */   
/*    */   <T> void append(CompletableFuture<T> paramCompletableFuture, Consumer<T> paramConsumer);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\TaskChainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */