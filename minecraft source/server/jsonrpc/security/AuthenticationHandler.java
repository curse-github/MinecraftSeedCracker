/*     */ package net.minecraft.server.jsonrpc.security;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @Sharable
/*     */ public class AuthenticationHandler
/*     */   extends ChannelDuplexHandler {
/*     */   private final Logger LOGGER;
/*  28 */   private static final AttributeKey<Boolean> AUTHENTICATED_KEY = AttributeKey.valueOf("authenticated");
/*  29 */   private static final AttributeKey<Boolean> ATTR_WEBSOCKET_ALLOWED = AttributeKey.valueOf("websocket_auth_allowed");
/*     */   
/*     */   private static final String SUBPROTOCOL_VALUE = "minecraft-v1";
/*     */   
/*     */   private static final String SUBPROTOCOL_HEADER_PREFIX = "minecraft-v1,";
/*     */   
/*     */   public AuthenticationHandler(SecurityConfig securityConfig, String allowedOrigins) {
/*     */     this.LOGGER = LogUtils.getLogger();
/*  37 */     this.securityConfig = securityConfig;
/*  38 */     this.allowedOrigins = Sets.newHashSet(allowedOrigins.split(","));
/*     */   }
/*     */   public static final String BEARER_PREFIX = "Bearer "; private final SecurityConfig securityConfig; private final Set<String> allowedOrigins;
/*     */   
/*     */   public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
/*  43 */     String clientIp = getClientIp(context);
/*  44 */     if (msg instanceof HttpRequest) { HttpRequest request = (HttpRequest)msg;
/*  45 */       SecurityCheckResult result = performSecurityChecks(request);
/*     */       
/*  47 */       if (result.isAllowed()) {
/*  48 */         context.channel().attr(AUTHENTICATED_KEY).set(Boolean.valueOf(true));
/*  49 */         if (result.isTokenSentInSecWebsocketProtocol()) {
/*  50 */           context.channel().attr(ATTR_WEBSOCKET_ALLOWED).set(Boolean.TRUE);
/*     */         }
/*     */       } else {
/*  53 */         this.LOGGER.debug("Authentication rejected for connection with ip {}: {}", clientIp, result.getReason());
/*  54 */         context.channel().attr(AUTHENTICATED_KEY).set(Boolean.valueOf(false));
/*  55 */         sendUnauthorizedResponse(context, result.getReason());
/*     */         
/*     */         return;
/*     */       }  }
/*     */     
/*  60 */     Boolean isAuthenticated = (Boolean)context.channel().attr(AUTHENTICATED_KEY).get();
/*     */     
/*  62 */     if (Boolean.TRUE.equals(isAuthenticated)) {
/*  63 */       super.channelRead(context, msg);
/*     */     } else {
/*  65 */       this.LOGGER.debug("Dropping unauthenticated connection with ip {}", clientIp);
/*  66 */       context.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  72 */     if (msg instanceof HttpResponse) { HttpResponse response = (HttpResponse)msg; if (response.status().code() == HttpResponseStatus.SWITCHING_PROTOCOLS.code() && 
/*  73 */         ctx.channel().attr(ATTR_WEBSOCKET_ALLOWED).get() != null && ((Boolean)ctx.channel().attr(ATTR_WEBSOCKET_ALLOWED).get()).equals(Boolean.TRUE)) {
/*  74 */         response.headers().set(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, "minecraft-v1");
/*     */       } }
/*     */     
/*  77 */     super.write(ctx, msg, promise);
/*     */   }
/*     */   
/*     */   private SecurityCheckResult performSecurityChecks(HttpRequest request) {
/*  81 */     String tokenInAuthorizationHeader = parseTokenInAuthorizationHeader(request);
/*  82 */     if (tokenInAuthorizationHeader != null) {
/*  83 */       if (isValidApiKey(tokenInAuthorizationHeader)) {
/*  84 */         return SecurityCheckResult.allowed();
/*     */       }
/*  86 */       return SecurityCheckResult.denied("Invalid API key");
/*     */     } 
/*     */     
/*  89 */     String tokenInSecWebsocketProtocolHeader = parseTokenInSecWebsocketProtocolHeader(request);
/*  90 */     if (tokenInSecWebsocketProtocolHeader != null) {
/*  91 */       if (!isAllowedOriginHeader(request)) {
/*  92 */         return SecurityCheckResult.denied("Origin Not Allowed");
/*     */       }
/*     */       
/*  95 */       if (isValidApiKey(tokenInSecWebsocketProtocolHeader)) {
/*  96 */         return SecurityCheckResult.allowed(true);
/*     */       }
/*  98 */       return SecurityCheckResult.denied("Invalid API key");
/*     */     } 
/*     */ 
/*     */     
/* 102 */     return SecurityCheckResult.denied("Missing API key");
/*     */   }
/*     */   
/*     */   private boolean isAllowedOriginHeader(HttpRequest request) {
/* 106 */     String originHeader = request.headers().get(HttpHeaderNames.ORIGIN);
/*     */     
/* 108 */     if (originHeader == null || originHeader.isEmpty()) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     return this.allowedOrigins.contains(originHeader);
/*     */   }
/*     */   
/*     */   private String parseTokenInAuthorizationHeader(HttpRequest request) {
/* 116 */     String authHeader = request.headers().get(HttpHeaderNames.AUTHORIZATION);
/*     */     
/* 118 */     if (authHeader != null && authHeader.startsWith("Bearer ")) {
/* 119 */       return authHeader.substring("Bearer ".length()).trim();
/*     */     }
/*     */     
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   private String parseTokenInSecWebsocketProtocolHeader(HttpRequest request) {
/* 126 */     String authHeader = request.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/*     */     
/* 128 */     if (authHeader != null && authHeader.startsWith("minecraft-v1,")) {
/* 129 */       return authHeader.substring("minecraft-v1,".length()).trim();
/*     */     }
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isValidApiKey(String suppliedKey) {
/* 136 */     if (suppliedKey.isEmpty()) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     byte[] suppliedKeyBytes = suppliedKey.getBytes(StandardCharsets.UTF_8);
/* 141 */     byte[] configuredKeyBytes = this.securityConfig.secretKey().getBytes(StandardCharsets.UTF_8);
/*     */ 
/*     */     
/* 144 */     return MessageDigest.isEqual(suppliedKeyBytes, configuredKeyBytes);
/*     */   }
/*     */   
/*     */   private String getClientIp(ChannelHandlerContext context) {
/* 148 */     InetSocketAddress remoteAddress = (InetSocketAddress)context.channel().remoteAddress();
/* 149 */     return remoteAddress.getAddress().getHostAddress();
/*     */   }
/*     */   
/*     */   private void sendUnauthorizedResponse(ChannelHandlerContext context, String reason) {
/* 153 */     String responseBody = "{\"error\":\"Unauthorized\",\"message\":\"" + reason + "\"}";
/* 154 */     byte[] content = responseBody.getBytes(StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED, Unpooled.wrappedBuffer(content));
/*     */ 
/*     */     
/* 162 */     response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
/* 163 */     response.headers().set(HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(content.length));
/* 164 */     response.headers().set(HttpHeaderNames.CONNECTION, "close");
/*     */     
/* 166 */     context.writeAndFlush(response).addListener(future -> context.close());
/*     */   }
/*     */   
/*     */   private static class SecurityCheckResult {
/*     */     private final boolean allowed;
/*     */     private final String reason;
/*     */     private final boolean tokenSentInSecWebsocketProtocol;
/*     */     
/*     */     private SecurityCheckResult(boolean allowed, String reason, boolean tokenSentInSecWebsocketProtocol) {
/* 175 */       this.allowed = allowed;
/* 176 */       this.reason = reason;
/* 177 */       this.tokenSentInSecWebsocketProtocol = tokenSentInSecWebsocketProtocol;
/*     */     }
/*     */ 
/*     */     
/* 181 */     public static SecurityCheckResult allowed() { return new SecurityCheckResult(true, null, false); }
/*     */ 
/*     */ 
/*     */     
/* 185 */     public static SecurityCheckResult allowed(boolean tokenSentInSecWebsocketProtocol) { return new SecurityCheckResult(true, null, tokenSentInSecWebsocketProtocol); }
/*     */ 
/*     */ 
/*     */     
/* 189 */     public static SecurityCheckResult denied(String reason) { return new SecurityCheckResult(false, reason, false); }
/*     */ 
/*     */     
/* 192 */     public boolean isAllowed() { return this.allowed; }
/* 193 */     public String getReason() { return this.reason; }
/* 194 */     public boolean isTokenSentInSecWebsocketProtocol() { return this.tokenSentInSecWebsocketProtocol; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\security\AuthenticationHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */