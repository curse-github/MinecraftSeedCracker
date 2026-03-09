/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ResultInfo;
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
/*    */ public final class Method<Params, Result>
/*    */   extends Record
/*    */   implements OutgoingRpcMethod<Params, Result>
/*    */ {
/*    */   private final MethodInfo<Params, Result> info;
/*    */   private final OutgoingRpcMethod.Attributes attributes;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; }
/*    */   
/* 73 */   public Method(MethodInfo<Params, Result> info, OutgoingRpcMethod.Attributes attributes) { this.info = info; this.attributes = attributes; } public MethodInfo<Params, Result> info() { return this.info; } public OutgoingRpcMethod.Attributes attributes() { return this.attributes; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonElement encodeParams(Params params) {
/* 79 */     if (this.info.params().isEmpty()) {
/* 80 */       throw new IllegalStateException("Method defined as having no parameters");
/*    */     }
/* 82 */     return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart(JsonOps.INSTANCE, params).getOrThrow();
/*    */   }
/*    */ 
/*    */   
/*    */   public Result decodeResult(JsonElement result) {
/* 87 */     if (this.info.result().isEmpty()) {
/* 88 */       throw new IllegalStateException("Method defined as having no result");
/*    */     }
/* 90 */     return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse(JsonOps.INSTANCE, result).getOrThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Method.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */