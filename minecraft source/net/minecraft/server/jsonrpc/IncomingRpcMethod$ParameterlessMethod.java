/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
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
/*    */ public final class ParameterlessMethod<Params, Result>
/*    */   extends Record
/*    */   implements IncomingRpcMethod<Params, Result>
/*    */ {
/*    */   private final MethodInfo<Params, Result> info;
/*    */   private final IncomingRpcMethod.Attributes attributes;
/*    */   private final IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; }
/*    */   
/* 44 */   public ParameterlessMethod(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier) { this.info = info; this.attributes = attributes; this.supplier = supplier; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier() { return this.supplier; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/* 52 */     if (paramsJson != null && (!paramsJson.isJsonArray() || !paramsJson.getAsJsonArray().isEmpty())) {
/* 53 */       throw new InvalidParameterJsonRpcException("Expected no params, or an empty array");
/*    */     }
/* 55 */     if (this.info.params().isPresent()) {
/* 56 */       throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description");
/*    */     }
/* 58 */     Result result = (Result)this.supplier.apply(minecraftApi, clientInfo);
/* 59 */     if (this.info.result().isEmpty()) {
/* 60 */       throw new IllegalStateException("No result codec defined");
/*    */     }
/* 62 */     return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart(JsonOps.INSTANCE, result).getOrThrow(InvalidParameterJsonRpcException::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$ParameterlessMethod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */