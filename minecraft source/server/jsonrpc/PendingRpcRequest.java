/*    */ package net.minecraft.server.jsonrpc;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public final class PendingRpcRequest<Result> extends Record {
/*    */   private final Holder.Reference<? extends OutgoingRpcMethod<?, ? extends Result>> method;
/*    */   private final CompletableFuture<Result> resultFuture;
/*    */   private final long timeoutTime;
/*    */   
/*  9 */   public PendingRpcRequest(Holder.Reference<? extends OutgoingRpcMethod<?, ? extends Result>> method, CompletableFuture<Result> resultFuture, long timeoutTime) { this.method = method; this.resultFuture = resultFuture; this.timeoutTime = timeoutTime; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/PendingRpcRequest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest<TResult;>; } public Holder.Reference<? extends OutgoingRpcMethod<?, ? extends Result>> method() { return this.method; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/PendingRpcRequest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest<TResult;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/PendingRpcRequest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  9 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/PendingRpcRequest<TResult;>; } public CompletableFuture<Result> resultFuture() { return this.resultFuture; } public long timeoutTime() { return this.timeoutTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept(JsonElement response) {
/*    */     try {
/* 16 */       Result result = (Result)((OutgoingRpcMethod)this.method.value()).decodeResult(response);
/* 17 */       this.resultFuture.complete(Objects.requireNonNull(result));
/* 18 */     } catch (Exception e) {
/* 19 */       this.resultFuture.completeExceptionally(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 24 */   public boolean timedOut(long currentTime) { return (currentTime > this.timeoutTime); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\PendingRpcRequest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */