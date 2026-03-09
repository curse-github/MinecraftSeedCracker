/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.CancellationException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FutureChain
/*    */   implements TaskChainer, AutoCloseable {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger(); private CompletableFuture<?> head;
/*    */   public FutureChain(Executor executor) {
/* 15 */     this.head = CompletableFuture.completedFuture(null);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     this.executor = executor;
/*    */   }
/*    */   private final Executor executor;
/*    */   
/*    */   public <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask) {
/* 26 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 33 */       .head = this.head.thenCombine(preparation, (ignored, value) -> value).thenAcceptAsync(value -> { if (!this.closed) chainedTask.accept(value);  }this.executor).exceptionally(t -> {
/*    */           
/* 35 */           if (t instanceof CompletionException) { CompletionException c = (CompletionException)t;
/* 36 */             t = c.getCause(); }
/*    */           
/* 38 */           if (t instanceof CancellationException) { CancellationException c = (CancellationException)t;
/* 39 */             throw c; }
/*    */ 
/*    */           
/* 42 */           LOGGER.error("Chain link failed, continuing to next one", t);
/* 43 */           return null;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public void close() { this.closed = true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\FutureChain.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */