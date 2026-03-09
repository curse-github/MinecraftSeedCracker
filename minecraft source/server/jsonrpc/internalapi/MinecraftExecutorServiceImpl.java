/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ 
/*    */ 
/*    */ public class MinecraftExecutorServiceImpl
/*    */   implements MinecraftExecutorService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   
/* 13 */   public MinecraftExecutorServiceImpl(DedicatedServer server) { this.server = server; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) { return this.server.submit(supplier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public CompletableFuture<Void> submit(Runnable runnable) { return this.server.submit(runnable); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftExecutorServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */