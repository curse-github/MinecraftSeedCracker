/*     */ package net.minecraft.server.jsonrpc;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ 
/*     */ public interface IncomingRpcMethod<Params, Result> {
/*     */   MethodInfo<Params, Result> info();
/*     */   
/*     */   Attributes attributes();
/*     */   
/*     */   JsonElement apply(MinecraftApi paramMinecraftApi, JsonElement paramJsonElement, ClientInfo paramClientInfo);
/*     */   
/*     */   public static final class Attributes extends Record {
/*     */     private final boolean runOnMainThread;
/*     */     private final boolean discoverable;
/*     */     
/*  28 */     public Attributes(boolean runOnMainThread, boolean discoverable) { this.runOnMainThread = runOnMainThread; this.discoverable = discoverable; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public boolean runOnMainThread() { return this.runOnMainThread; } public boolean discoverable() { return this.discoverable; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ParameterlessMethod<Params, Result>
/*     */     extends Record
/*     */     implements IncomingRpcMethod<Params, Result>
/*     */   {
/*     */     private final MethodInfo<Params, Result> info;
/*     */     
/*     */     private final IncomingRpcMethod.Attributes attributes;
/*     */     
/*     */     private final IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier;
/*     */ 
/*     */     
/*  44 */     public ParameterlessMethod(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier) { this.info = info; this.attributes = attributes; this.supplier = supplier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  44 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier() { return this.supplier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*  52 */       if (paramsJson != null && (!paramsJson.isJsonArray() || !paramsJson.getAsJsonArray().isEmpty())) {
/*  53 */         throw new InvalidParameterJsonRpcException("Expected no params, or an empty array");
/*     */       }
/*  55 */       if (this.info.params().isPresent()) {
/*  56 */         throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description");
/*     */       }
/*  58 */       Result result = (Result)this.supplier.apply(minecraftApi, clientInfo);
/*  59 */       if (this.info.result().isEmpty()) {
/*  60 */         throw new IllegalStateException("No result codec defined");
/*     */       }
/*  62 */       return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart(JsonOps.INSTANCE, result).getOrThrow(InvalidParameterJsonRpcException::new);
/*     */     } }
/*     */   public static final class Method<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> { private final MethodInfo<Params, Result> info; private final IncomingRpcMethod.Attributes attributes; private final IncomingRpcMethod.RpcMethodFunction<Params, Result> function;
/*     */     
/*  66 */     public Method(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.RpcMethodFunction<Params, Result> function) { this.info = info; this.attributes = attributes; this.function = function; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  66 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.RpcMethodFunction<Params, Result> function() { return this.function; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*     */       JsonElement paramsJsonElement;
/*  74 */       if (paramsJson == null || (!paramsJson.isJsonArray() && !paramsJson.isJsonObject())) {
/*  75 */         throw new InvalidParameterJsonRpcException("Expected params as array or named");
/*     */       }
/*     */       
/*  78 */       if (this.info.params().isEmpty()) {
/*  79 */         throw new IllegalArgumentException("Method defined as having parameters without describing them");
/*     */       }
/*     */ 
/*     */       
/*  83 */       if (paramsJson.isJsonObject()) {
/*  84 */         String parameterName = ((ParamInfo)this.info.params().get()).name();
/*  85 */         JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
/*  86 */         if (jsonElement == null) {
/*  87 */           throw new InvalidParameterJsonRpcException(String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", new Object[] { parameterName }));
/*     */         }
/*  89 */         paramsJsonElement = jsonElement;
/*     */       } else {
/*  91 */         JsonArray jsonArray = paramsJson.getAsJsonArray();
/*  92 */         if (jsonArray.isEmpty() || jsonArray.size() > 1) {
/*  93 */           throw new InvalidParameterJsonRpcException("Expected exactly one element in the params array");
/*     */         }
/*  95 */         paramsJsonElement = jsonArray.get(0);
/*     */       } 
/*  97 */       Params params = (Params)((ParamInfo)this.info.params().get()).schema().codec().parse(JsonOps.INSTANCE, paramsJsonElement).getOrThrow(InvalidParameterJsonRpcException::new);
/*  98 */       Result result = (Result)this.function.apply(minecraftApi, params, clientInfo);
/*  99 */       if (this.info.result().isEmpty()) {
/* 100 */         throw new IllegalStateException("No result codec defined");
/*     */       }
/* 102 */       return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart(JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException::new);
/*     */     } } @FunctionalInterface
/*     */   public static interface RpcMethodFunction<Params, Result> {
/*     */     Result apply(MinecraftApi param1MinecraftApi, Params param1Params, ClientInfo param1ClientInfo);
/*     */   } @FunctionalInterface
/* 107 */   public static interface ParameterlessRpcMethodFunction<Result> { Result apply(MinecraftApi param1MinecraftApi, ClientInfo param1ClientInfo); } public static class IncomingRpcMethodBuilder<Params, Result> extends Object { private String description; private ParamInfo<Params> paramInfo; private ResultInfo<Result> resultInfo; public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> function) { this.description = "";
/*     */ 
/*     */       
/* 110 */       this.discoverable = true;
/* 111 */       this.runOnMainThread = true;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       this.parameterlessFunction = function; } private boolean discoverable; private boolean runOnMainThread; private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction; private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction; public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/*     */       this.description = "";
/*     */       this.discoverable = true;
/*     */       this.runOnMainThread = true;
/* 120 */       this.parameterFunction = function; } public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> supplier) {
/*     */       this.description = "";
/*     */       this.discoverable = true;
/*     */       this.runOnMainThread = true;
/* 124 */       this.parameterlessFunction = ((apiService, clientInfo) -> supplier.apply(apiService));
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> description(String description) {
/* 128 */       this.description = description;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 133 */       this.resultInfo = new ResultInfo(resultName, resultSchema.info());
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 138 */       this.paramInfo = new ParamInfo(paramName, paramSchema.info());
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 143 */       this.discoverable = false;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 148 */       this.runOnMainThread = false;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod<Params, Result> build() {
/* 153 */       if (this.resultInfo == null) {
/* 154 */         throw new IllegalStateException("No response defined");
/*     */       }
/*     */       
/* 157 */       IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable);
/* 158 */       MethodInfo<Params, Result> methodInfo = new MethodInfo<Params, Result>(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       if (this.parameterlessFunction != null) {
/* 165 */         return new IncomingRpcMethod.ParameterlessMethod(methodInfo, attributes, this.parameterlessFunction);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 170 */       if (this.parameterFunction != null) {
/* 171 */         if (this.paramInfo == null) {
/* 172 */           throw new IllegalStateException("No param schema defined");
/*     */         }
/* 174 */         return new IncomingRpcMethod.Method(methodInfo, attributes, this.parameterFunction);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       throw new IllegalStateException("No method defined");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     public IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, String key) { return register(methodRegistry, Identifier.withDefaultNamespace(key)); }
/*     */ 
/*     */ 
/*     */     
/* 189 */     private IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, Identifier id) { return (IncomingRpcMethod)Registry.register(methodRegistry, id, build()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(ParameterlessRpcMethodFunction<Result> function) { return new IncomingRpcMethodBuilder(function); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   static <Params, Result> IncomingRpcMethodBuilder<Params, Result> method(RpcMethodFunction<Params, Result> function) { return new IncomingRpcMethodBuilder(function); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(Function<MinecraftApi, Result> supplier) { return new IncomingRpcMethodBuilder(supplier); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */