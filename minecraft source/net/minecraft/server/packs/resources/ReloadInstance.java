/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface ReloadInstance
/*    */ {
/*    */   CompletableFuture<?> done();
/*    */   
/*    */   float getActualProgress();
/*    */   
/* 11 */   default boolean isDone() { return done().isDone(); }
/*    */ 
/*    */   
/*    */   default void checkExceptions() {
/* 15 */     CompletableFuture<?> done = done();
/* 16 */     if (done.isCompletedExceptionally())
/* 17 */       done.join(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\ReloadInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */