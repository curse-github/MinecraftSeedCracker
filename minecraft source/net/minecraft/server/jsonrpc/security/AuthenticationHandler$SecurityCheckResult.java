/*     */ package net.minecraft.server.jsonrpc.security;
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
/*     */ class SecurityCheckResult
/*     */ {
/*     */   private final boolean allowed;
/*     */   private final String reason;
/*     */   private final boolean tokenSentInSecWebsocketProtocol;
/*     */   
/*     */   private SecurityCheckResult(boolean allowed, String reason, boolean tokenSentInSecWebsocketProtocol) {
/* 175 */     this.allowed = allowed;
/* 176 */     this.reason = reason;
/* 177 */     this.tokenSentInSecWebsocketProtocol = tokenSentInSecWebsocketProtocol;
/*     */   }
/*     */ 
/*     */   
/* 181 */   public static SecurityCheckResult allowed() { return new SecurityCheckResult(true, null, false); }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public static SecurityCheckResult allowed(boolean tokenSentInSecWebsocketProtocol) { return new SecurityCheckResult(true, null, tokenSentInSecWebsocketProtocol); }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public static SecurityCheckResult denied(String reason) { return new SecurityCheckResult(false, reason, false); }
/*     */ 
/*     */   
/* 192 */   public boolean isAllowed() { return this.allowed; }
/* 193 */   public String getReason() { return this.reason; }
/* 194 */   public boolean isTokenSentInSecWebsocketProtocol() { return this.tokenSentInSecWebsocketProtocol; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\security\AuthenticationHandler$SecurityCheckResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */