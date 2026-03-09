/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface OutgoingRpcMethod<Params, Result>
/*     */ {
/*     */   public static final String NOTIFICATION_PREFIX = "notification/";
/*     */   
/*     */   MethodInfo<Params, Result> info();
/*     */   
/*     */   Attributes attributes();
/*     */   
/*  24 */   default JsonElement encodeParams(Params params) { return null; }
/*     */ 
/*     */ 
/*     */   
/*  28 */   default Result decodeResult(JsonElement result) { return null; }
/*     */   public static final class Attributes extends Record { private final boolean discoverable;
/*     */     
/*  31 */     public Attributes(boolean discoverable) { this.discoverable = discoverable; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  31 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes; } public boolean discoverable() { return this.discoverable; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*     */       //   0	8	1	o	Ljava/lang/Object; } }
/*     */   public static final class ParmeterlessNotification extends Record implements OutgoingRpcMethod<Void, Void> { private final MethodInfo<Void, Void> info;
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification; }
/*     */     
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification; }
/*     */     
/*  41 */     public ParmeterlessNotification(MethodInfo<Void, Void> info, OutgoingRpcMethod.Attributes attributes) { this.info = info; this.attributes = attributes; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*  41 */       //   0	8	1	o	Ljava/lang/Object; } public MethodInfo<Void, Void> info() { return this.info; } public OutgoingRpcMethod.Attributes attributes() { return this.attributes; } }
/*     */ 
/*     */   
/*     */   public static final class Notification<Params> extends Record implements OutgoingRpcMethod<Params, Void> { private final MethodInfo<Params, Void> info;
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*  47 */     public Notification(MethodInfo<Params, Void> info, OutgoingRpcMethod.Attributes attributes) { this.info = info; this.attributes = attributes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  47 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>; } public MethodInfo<Params, Void> info() { return this.info; } public OutgoingRpcMethod.Attributes attributes() { return this.attributes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement encodeParams(Params params) {
/*  53 */       if (this.info.params().isEmpty()) {
/*  54 */         throw new IllegalStateException("Method defined as having no parameters");
/*     */       }
/*  56 */       return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart(JsonOps.INSTANCE, params).getOrThrow();
/*     */     } }
/*     */   public static final class ParameterlessMethod<Result> extends Record implements OutgoingRpcMethod<Void, Result> { private final MethodInfo<Void, Result> info; private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*  60 */     public ParameterlessMethod(MethodInfo<Void, Result> info, OutgoingRpcMethod.Attributes attributes) { this.info = info; this.attributes = attributes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  60 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>; } public MethodInfo<Void, Result> info() { return this.info; } public OutgoingRpcMethod.Attributes attributes() { return this.attributes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Result decodeResult(JsonElement result) {
/*  66 */       if (this.info.result().isEmpty()) {
/*  67 */         throw new IllegalStateException("Method defined as having no result");
/*     */       }
/*  69 */       return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse(JsonOps.INSTANCE, result).getOrThrow();
/*     */     } }
/*     */   public static final class Method<Params, Result> extends Record implements OutgoingRpcMethod<Params, Result> { private final MethodInfo<Params, Result> info; private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*  73 */     public Method(MethodInfo<Params, Result> info, OutgoingRpcMethod.Attributes attributes) { this.info = info; this.attributes = attributes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  73 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>; } public MethodInfo<Params, Result> info() { return this.info; } public OutgoingRpcMethod.Attributes attributes() { return this.attributes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement encodeParams(Params params) {
/*  79 */       if (this.info.params().isEmpty()) {
/*  80 */         throw new IllegalStateException("Method defined as having no parameters");
/*     */       }
/*  82 */       return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart(JsonOps.INSTANCE, params).getOrThrow();
/*     */     }
/*     */ 
/*     */     
/*     */     public Result decodeResult(JsonElement result) {
/*  87 */       if (this.info.result().isEmpty()) {
/*  88 */         throw new IllegalStateException("Method defined as having no result");
/*     */       }
/*  90 */       return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse(JsonOps.INSTANCE, result).getOrThrow();
/*     */     } }
/*     */   @FunctionalInterface
/*     */   public static interface Factory<Params, Result> { OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> param1MethodInfo, OutgoingRpcMethod.Attributes param1Attributes); }
/*     */   
/*  95 */   public static class OutgoingRpcMethodBuilder<Params, Result> extends Object { public static final OutgoingRpcMethod.Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod.Attributes(true); private final OutgoingRpcMethod.Factory<Params, Result> method; private String description; private ParamInfo<Params> paramInfo; private ResultInfo<Result> resultInfo;
/*     */     public OutgoingRpcMethodBuilder(OutgoingRpcMethod.Factory<Params, Result> method) {
/*  97 */       this.description = "";
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       this.method = method;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> description(String description) {
/* 106 */       this.description = description;
/* 107 */       return this;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 111 */       this.resultInfo = new ResultInfo(resultName, resultSchema);
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 116 */       this.paramInfo = new ParamInfo(paramName, paramSchema);
/* 117 */       return this;
/*     */     }
/*     */     
/*     */     private OutgoingRpcMethod<Params, Result> build() {
/* 121 */       MethodInfo<Params, Result> methodInfo = new MethodInfo<Params, Result>(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       return this.method.create(methodInfo, DEFAULT_ATTRIBUTES);
/*     */     }
/*     */ 
/*     */     
/* 130 */     public Holder.Reference<OutgoingRpcMethod<Params, Result>> register(String key) { return register(Identifier.withDefaultNamespace("notification/" + key)); }
/*     */ 
/*     */ 
/*     */     
/* 134 */     private Holder.Reference<OutgoingRpcMethod<Params, Result>> register(Identifier id) { return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, id, build()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   static OutgoingRpcMethodBuilder<Void, Void> notification() { return new OutgoingRpcMethodBuilder(ParmeterlessNotification::new); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   static <Params> OutgoingRpcMethodBuilder<Params, Void> notificationWithParams() { return new OutgoingRpcMethodBuilder(Notification::new); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   static <Result> OutgoingRpcMethodBuilder<Void, Result> request() { return new OutgoingRpcMethodBuilder(ParameterlessMethod::new); }
/*     */ 
/*     */ 
/*     */   
/* 151 */   static <Params, Result> OutgoingRpcMethodBuilder<Params, Result> requestWithParams() { return new OutgoingRpcMethodBuilder(Method::new); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */