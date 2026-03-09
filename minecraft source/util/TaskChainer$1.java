/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements TaskChainer
/*    */ {
/*    */   public <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask) {
/* 18 */     preparation.thenAcceptAsync(chainedTask, executor).exceptionally(e -> {
/* 19 */           LOGGER.error("Task failed", e);
/* 20 */           return null;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\TaskChainer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */