/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
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
/*     */ 
/*     */ 
/*     */ public class IncomingRpcMethodBuilder<Params, Result>
/*     */   extends Object
/*     */ {
/*     */   private String description;
/*     */   private ParamInfo<Params> paramInfo;
/*     */   private ResultInfo<Result> resultInfo;
/*     */   private boolean discoverable;
/*     */   private boolean runOnMainThread;
/*     */   private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction;
/*     */   private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction;
/*     */   
/*     */   public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> function) {
/* 107 */     this.description = "";
/*     */ 
/*     */     
/* 110 */     this.discoverable = true;
/* 111 */     this.runOnMainThread = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     this.parameterlessFunction = function;
/*     */   } public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> function) { this.description = "";
/*     */     this.discoverable = true;
/*     */     this.runOnMainThread = true;
/* 120 */     this.parameterFunction = function; } public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> supplier) {
/*     */     this.description = "";
/*     */     this.discoverable = true;
/*     */     this.runOnMainThread = true;
/* 124 */     this.parameterlessFunction = ((apiService, clientInfo) -> supplier.apply(apiService));
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> description(String description) {
/* 128 */     this.description = description;
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 133 */     this.resultInfo = new ResultInfo(resultName, resultSchema.info());
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 138 */     this.paramInfo = new ParamInfo(paramName, paramSchema.info());
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 143 */     this.discoverable = false;
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 148 */     this.runOnMainThread = false;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod<Params, Result> build() {
/* 153 */     if (this.resultInfo == null) {
/* 154 */       throw new IllegalStateException("No response defined");
/*     */     }
/*     */     
/* 157 */     IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable);
/* 158 */     MethodInfo<Params, Result> methodInfo = new MethodInfo<Params, Result>(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     if (this.parameterlessFunction != null) {
/* 165 */       return new IncomingRpcMethod.ParameterlessMethod(methodInfo, attributes, this.parameterlessFunction);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (this.parameterFunction != null) {
/* 171 */       if (this.paramInfo == null) {
/* 172 */         throw new IllegalStateException("No param schema defined");
/*     */       }
/* 174 */       return new IncomingRpcMethod.Method(methodInfo, attributes, this.parameterFunction);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     throw new IllegalStateException("No method defined");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, String key) { return register(methodRegistry, Identifier.withDefaultNamespace(key)); }
/*     */ 
/*     */ 
/*     */   
/* 189 */   private IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, Identifier id) { return (IncomingRpcMethod)Registry.register(methodRegistry, id, build()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$IncomingRpcMethodBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */