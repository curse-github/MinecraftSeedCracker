/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.timeout.ReadTimeoutException;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidRequestJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.MethodNotFoundJsonRpcException;
/*     */ import net.minecraft.server.jsonrpc.methods.RemoteRpcErrorException;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class Connection
/*     */   extends SimpleChannelInboundHandler<JsonElement>
/*     */ {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  43 */   private static final AtomicInteger CONNECTION_ID_COUNTER = new AtomicInteger(0);
/*     */   
/*     */   private final JsonRpcLogger jsonRpcLogger;
/*     */   
/*     */   private final ClientInfo clientInfo;
/*     */   private final ManagementServer managementServer;
/*     */   
/*     */   public Connection(Channel channel, ManagementServer managementServer, MinecraftApi minecraftApi, JsonRpcLogger jsonrpcLogger) {
/*  51 */     this.transactionId = new AtomicInteger();
/*  52 */     this.pendingRequests = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap());
/*     */ 
/*     */     
/*  55 */     this.clientInfo = ClientInfo.of(Integer.valueOf(CONNECTION_ID_COUNTER.incrementAndGet()));
/*  56 */     this.managementServer = managementServer;
/*  57 */     this.minecraftApi = minecraftApi;
/*  58 */     this.channel = channel;
/*  59 */     this.jsonRpcLogger = jsonrpcLogger;
/*     */   }
/*     */   private final Channel channel; private final MinecraftApi minecraftApi; private final AtomicInteger transactionId; private final Int2ObjectMap<PendingRpcRequest<?>> pendingRequests;
/*     */   public void tick() {
/*  63 */     long time = Util.getMillis();
/*  64 */     this.pendingRequests.int2ObjectEntrySet().removeIf(entry -> {
/*  65 */           boolean timedOut = ((PendingRpcRequest)entry.getValue()).timedOut(time);
/*  66 */           if (timedOut) {
/*  67 */             ((PendingRpcRequest)entry.getValue()).resultFuture().completeExceptionally(new ReadTimeoutException("RPC method " + String.valueOf(((PendingRpcRequest)entry.getValue()).method().key().identifier()) + " timed out waiting for response"));
/*     */           }
/*  69 */           return timedOut;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/*  75 */     this.jsonRpcLogger.log(this.clientInfo, "Management connection opened for {}", new Object[] { this.channel.remoteAddress() });
/*  76 */     super.channelActive(ctx);
/*  77 */     this.managementServer.onConnected(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/*  82 */     this.jsonRpcLogger.log(this.clientInfo, "Management connection closed for {}", new Object[] { this.channel.remoteAddress() });
/*  83 */     super.channelInactive(ctx);
/*  84 */     this.managementServer.onDisconnected(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/*  89 */     if (cause.getCause() instanceof com.google.gson.JsonParseException) {
/*  90 */       this.channel.writeAndFlush(JsonRPCErrors.PARSE_ERROR.createWithUnknownId(cause.getMessage()));
/*     */       return;
/*     */     } 
/*  93 */     super.exceptionCaught(ctx, cause);
/*  94 */     this.channel.close().awaitUninterruptibly();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext channelHandlerContext, JsonElement jsonElement) {
/*  99 */     if (jsonElement.isJsonObject()) {
/* 100 */       JsonObject response = handleJsonObject(jsonElement.getAsJsonObject());
/* 101 */       if (response != null) {
/* 102 */         this.channel.writeAndFlush(response);
/*     */       }
/* 104 */     } else if (jsonElement.isJsonArray()) {
/* 105 */       this.channel.writeAndFlush(handleBatchRequest(jsonElement.getAsJsonArray().asList()));
/*     */     } else {
/* 107 */       this.channel.writeAndFlush(JsonRPCErrors.INVALID_REQUEST.createWithUnknownId(null));
/*     */     } 
/*     */   }
/*     */   
/*     */   private JsonArray handleBatchRequest(List<JsonElement> batchRequests) {
/* 112 */     JsonArray batchResponses = new JsonArray();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     Objects.requireNonNull(batchResponses); batchRequests.stream().map(batchEntry -> handleJsonObject(batchEntry.getAsJsonObject())).filter(Objects::nonNull).forEach(batchResponses::add);
/* 118 */     return batchResponses;
/*     */   }
/*     */ 
/*     */   
/* 122 */   public void sendNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> method) { sendRequest(method, null, false); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public <Params> void sendNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> method, Params params) { sendRequest(method, params, false); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public <Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Void, Result>> method) { return sendRequest(method, null, true); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public <Params, Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Params, Result>> method, Params params) { return sendRequest(method, params, true); }
/*     */ 
/*     */   
/*     */   @Contract("_,_,false->null;_,_,true->!null")
/*     */   private <Params, Result> CompletableFuture<Result> sendRequest(Holder.Reference<? extends OutgoingRpcMethod<Params, ? extends Result>> method, Params params, boolean expectReply) {
/* 139 */     List<JsonElement> jsonParams = (params != null) ? List.of((JsonElement)Objects.requireNonNull(((OutgoingRpcMethod)method.value()).encodeParams(params))) : List.of();
/* 140 */     if (expectReply) {
/* 141 */       CompletableFuture<Result> future = new CompletableFuture<Result>();
/* 142 */       int id = this.transactionId.incrementAndGet();
/* 143 */       long time = Util.timeSource.get(TimeUnit.MILLISECONDS);
/* 144 */       this.pendingRequests.put(id, new PendingRpcRequest(method, future, time + 5000L));
/* 145 */       this.channel.writeAndFlush(JsonRPCUtils.createRequest(Integer.valueOf(id), method.key().identifier(), jsonParams));
/* 146 */       return future;
/*     */     } 
/* 148 */     this.channel.writeAndFlush(JsonRPCUtils.createRequest(null, method.key().identifier(), jsonParams));
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   JsonObject handleJsonObject(JsonObject jsonObject) {
/*     */     try {
/* 162 */       JsonElement id = JsonRPCUtils.getRequestId(jsonObject);
/* 163 */       String method = JsonRPCUtils.getMethodName(jsonObject);
/* 164 */       JsonElement result = JsonRPCUtils.getResult(jsonObject);
/* 165 */       JsonElement params = JsonRPCUtils.getParams(jsonObject);
/* 166 */       JsonObject error = JsonRPCUtils.getError(jsonObject);
/* 167 */       if (method != null && result == null && error == null) {
/* 168 */         if (id != null && !isValidRequestId(id)) {
/* 169 */           return JsonRPCErrors.INVALID_REQUEST.createWithUnknownId("Invalid request id - only String, Number and NULL supported");
/*     */         }
/* 171 */         return handleIncomingRequest(id, method, params);
/* 172 */       }  if (method == null && result != null && error == null && id != null) {
/*     */         
/* 174 */         if (isValidResponseId(id)) {
/* 175 */           handleRequestResponse(id.getAsInt(), result);
/*     */         } else {
/* 177 */           LOGGER.warn("Received respose {} with id {} we did not request", result, id);
/*     */         } 
/*     */         
/* 180 */         return null;
/* 181 */       }  if (method == null && result == null && error != null) {
/* 182 */         return handleError(id, error);
/*     */       }
/* 184 */       return JsonRPCErrors.INVALID_REQUEST.createWithoutData((JsonElement)Objects.requireNonNullElse(id, JsonNull.INSTANCE));
/*     */     }
/* 186 */     catch (Exception e) {
/* 187 */       LOGGER.error("Error while handling rpc request", e);
/* 188 */       return JsonRPCErrors.INTERNAL_ERROR.createWithUnknownId("Unknown error handling request - check server logs for stack trace");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 194 */   private static boolean isValidRequestId(JsonElement id) { return (id.isJsonNull() || GsonHelper.isNumberValue(id) || GsonHelper.isStringValue(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   private static boolean isValidResponseId(JsonElement id) { return GsonHelper.isNumberValue(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonObject handleIncomingRequest(JsonElement id, String method, JsonElement params) {
/* 205 */     boolean sendResponse = (id != null);
/*     */     try {
/* 207 */       JsonElement result = dispatchIncomingRequest(method, params);
/* 208 */       if (result == null || !sendResponse) {
/* 209 */         return null;
/*     */       }
/* 211 */       return JsonRPCUtils.createSuccessResult(id, result);
/* 212 */     } catch (InvalidParameterJsonRpcException e) {
/* 213 */       LOGGER.debug("Invalid parameter invocation {}: {}, {}", new Object[] { method, params, e.getMessage() });
/* 214 */       return sendResponse ? JsonRPCErrors.INVALID_PARAMS.create(id, e.getMessage()) : null;
/* 215 */     } catch (EncodeJsonRpcException e) {
/* 216 */       LOGGER.error("Failed to encode json rpc response {}: {}", method, e.getMessage());
/* 217 */       return sendResponse ? JsonRPCErrors.INTERNAL_ERROR.create(id, e.getMessage()) : null;
/* 218 */     } catch (InvalidRequestJsonRpcException e) {
/* 219 */       return sendResponse ? JsonRPCErrors.INVALID_REQUEST.create(id, e.getMessage()) : null;
/* 220 */     } catch (MethodNotFoundJsonRpcException e) {
/* 221 */       return sendResponse ? JsonRPCErrors.METHOD_NOT_FOUND.create(id, e.getMessage()) : null;
/* 222 */     } catch (Exception e) {
/* 223 */       LOGGER.error("Error while dispatching rpc method {}", method, e);
/* 224 */       return sendResponse ? JsonRPCErrors.INTERNAL_ERROR.createWithoutData(id) : null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public JsonElement dispatchIncomingRequest(String method, JsonElement params) {
/* 229 */     Identifier identifier = Identifier.tryParse(method);
/* 230 */     if (identifier == null) {
/* 231 */       throw new InvalidRequestJsonRpcException("Failed to parse method value: " + method);
/*     */     }
/*     */     
/* 234 */     Optional<IncomingRpcMethod<?, ?>> incomingRpcMethod = BuiltInRegistries.INCOMING_RPC_METHOD.getOptional(identifier);
/* 235 */     if (incomingRpcMethod.isEmpty()) {
/* 236 */       throw new MethodNotFoundJsonRpcException("Method not found: " + method);
/*     */     }
/*     */     
/* 239 */     if (((IncomingRpcMethod)incomingRpcMethod.get()).attributes().runOnMainThread()) {
/*     */       
/*     */       try {
/* 242 */         return (JsonElement)this.minecraftApi.submit(() -> ((IncomingRpcMethod)incomingRpcMethod.get()).apply(this.minecraftApi, params, this.clientInfo)).join();
/* 243 */       } catch (CompletionException e) {
/* 244 */         Throwable throwable = e.getCause(); if (throwable instanceof RuntimeException) { RuntimeException re = (RuntimeException)throwable;
/* 245 */           throw re; }
/*     */         
/* 247 */         throw e;
/*     */       } 
/*     */     }
/* 250 */     return ((IncomingRpcMethod)incomingRpcMethod.get()).apply(this.minecraftApi, params, this.clientInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleRequestResponse(int id, JsonElement result) {
/* 255 */     PendingRpcRequest<?> request = (PendingRpcRequest)this.pendingRequests.remove(id);
/* 256 */     if (request == null) {
/* 257 */       LOGGER.warn("Received unknown response (id: {}): {}", Integer.valueOf(id), result);
/*     */     } else {
/* 259 */       request.accept(result);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private JsonObject handleError(JsonElement id, JsonObject error) {
/* 265 */     if (id != null && isValidResponseId(id)) {
/* 266 */       PendingRpcRequest<?> request = (PendingRpcRequest)this.pendingRequests.remove(id.getAsInt());
/* 267 */       if (request != null) {
/* 268 */         request.resultFuture().completeExceptionally(new RemoteRpcErrorException(id, error));
/*     */       }
/*     */     } 
/* 271 */     LOGGER.error("Received error (id: {}): {}", id, error);
/* 272 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\Connection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */