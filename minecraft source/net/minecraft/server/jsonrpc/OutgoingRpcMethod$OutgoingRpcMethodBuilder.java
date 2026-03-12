/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class OutgoingRpcMethodBuilder<Params, Result>
/*     */   extends Object
/*     */ {
/*  95 */   public static final OutgoingRpcMethod.Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod.Attributes(true); private final OutgoingRpcMethod.Factory<Params, Result> method; private String description;
/*     */   public OutgoingRpcMethodBuilder(OutgoingRpcMethod.Factory<Params, Result> method) {
/*  97 */     this.description = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.method = method;
/*     */   }
/*     */   private ParamInfo<Params> paramInfo; private ResultInfo<Result> resultInfo;
/*     */   public OutgoingRpcMethodBuilder<Params, Result> description(String description) {
/* 106 */     this.description = description;
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 111 */     this.resultInfo = new ResultInfo(resultName, resultSchema);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 116 */     this.paramInfo = new ParamInfo(paramName, paramSchema);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   private OutgoingRpcMethod<Params, Result> build() {
/* 121 */     MethodInfo<Params, Result> methodInfo = new MethodInfo<Params, Result>(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return this.method.create(methodInfo, DEFAULT_ATTRIBUTES);
/*     */   }
/*     */ 
/*     */   
/* 130 */   public Holder.Reference<OutgoingRpcMethod<Params, Result>> register(String key) { return register(Identifier.withDefaultNamespace("notification/" + key)); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   private Holder.Reference<OutgoingRpcMethod<Params, Result>> register(Identifier id) { return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, id, build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$OutgoingRpcMethodBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */