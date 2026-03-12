/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Method<Params, Result>
/*     */   extends Record
/*     */   implements IncomingRpcMethod<Params, Result>
/*     */ {
/*     */   private final MethodInfo<Params, Result> info;
/*     */   private final IncomingRpcMethod.Attributes attributes;
/*     */   private final IncomingRpcMethod.RpcMethodFunction<Params, Result> function;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #66	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; }
/*     */   
/*  66 */   public Method(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.RpcMethodFunction<Params, Result> function) { this.info = info; this.attributes = attributes; this.function = function; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.RpcMethodFunction<Params, Result> function() { return this.function; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*     */     JsonElement paramsJsonElement;
/*  74 */     if (paramsJson == null || (!paramsJson.isJsonArray() && !paramsJson.isJsonObject())) {
/*  75 */       throw new InvalidParameterJsonRpcException("Expected params as array or named");
/*     */     }
/*     */     
/*  78 */     if (this.info.params().isEmpty()) {
/*  79 */       throw new IllegalArgumentException("Method defined as having parameters without describing them");
/*     */     }
/*     */ 
/*     */     
/*  83 */     if (paramsJson.isJsonObject()) {
/*  84 */       String parameterName = ((ParamInfo)this.info.params().get()).name();
/*  85 */       JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
/*  86 */       if (jsonElement == null) {
/*  87 */         throw new InvalidParameterJsonRpcException(String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", new Object[] { parameterName }));
/*     */       }
/*  89 */       paramsJsonElement = jsonElement;
/*     */     } else {
/*  91 */       JsonArray jsonArray = paramsJson.getAsJsonArray();
/*  92 */       if (jsonArray.isEmpty() || jsonArray.size() > 1) {
/*  93 */         throw new InvalidParameterJsonRpcException("Expected exactly one element in the params array");
/*     */       }
/*  95 */       paramsJsonElement = jsonArray.get(0);
/*     */     } 
/*  97 */     Params params = (Params)((ParamInfo)this.info.params().get()).schema().codec().parse(JsonOps.INSTANCE, paramsJsonElement).getOrThrow(InvalidParameterJsonRpcException::new);
/*  98 */     Result result = (Result)this.function.apply(minecraftApi, params, clientInfo);
/*  99 */     if (this.info.result().isEmpty()) {
/* 100 */       throw new IllegalStateException("No result codec defined");
/*     */     }
/* 102 */     return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart(JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException::new);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$Method.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */