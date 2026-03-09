/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import net.minecraft.network.chat.FilterMask;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyTextFilter
/*     */   extends ServerTextFilter
/*     */ {
/*     */   private static final String ENDPOINT = "v1/chat";
/*     */   private final URL joinEndpoint;
/*     */   private final JoinOrLeaveEncoder joinEncoder;
/*     */   private final URL leaveEndpoint;
/*     */   private final JoinOrLeaveEncoder leaveEncoder;
/*     */   private final String authKey;
/*     */   
/*     */   private LegacyTextFilter(URL chatEndpoint, ServerTextFilter.MessageEncoder chatEncoder, URL joinEndpoint, JoinOrLeaveEncoder joinEncoder, URL leaveEndpoint, JoinOrLeaveEncoder leaveEncoder, String authKey, ServerTextFilter.IgnoreStrategy chatIgnoreStrategy, ExecutorService workerPool) {
/*  35 */     super(chatEndpoint, chatEncoder, chatIgnoreStrategy, workerPool);
/*     */     
/*  37 */     this.joinEndpoint = joinEndpoint;
/*  38 */     this.joinEncoder = joinEncoder;
/*  39 */     this.leaveEndpoint = leaveEndpoint;
/*  40 */     this.leaveEncoder = leaveEncoder;
/*  41 */     this.authKey = authKey;
/*     */   }
/*     */   public static ServerTextFilter createTextFilterFromConfig(String config) {
/*     */     try {
/*     */       ServerTextFilter.MessageEncoder chatEncoder;
/*  46 */       JsonObject parsedConfig = GsonHelper.parse(config);
/*     */       
/*  48 */       URI host = new URI(GsonHelper.getAsString(parsedConfig, "apiServer"));
/*  49 */       String key = GsonHelper.getAsString(parsedConfig, "apiKey");
/*  50 */       if (key.isEmpty()) {
/*  51 */         throw new IllegalArgumentException("Missing API key");
/*     */       }
/*  53 */       int ruleId = GsonHelper.getAsInt(parsedConfig, "ruleId", 1);
/*  54 */       String serverId = GsonHelper.getAsString(parsedConfig, "serverId", "");
/*  55 */       String roomId = GsonHelper.getAsString(parsedConfig, "roomId", "Java:Chat");
/*  56 */       int hashesToDrop = GsonHelper.getAsInt(parsedConfig, "hashesToDrop", -1);
/*     */       
/*  58 */       int maxConcurrentRequests = GsonHelper.getAsInt(parsedConfig, "maxConcurrentRequests", 7);
/*     */       
/*  60 */       JsonObject endpoints = GsonHelper.getAsJsonObject(parsedConfig, "endpoints", null);
/*  61 */       String chatEndpointConfig = getEndpointFromConfig(endpoints, "chat", "v1/chat");
/*  62 */       boolean isLegacyChatEndpoint = chatEndpointConfig.equals("v1/chat");
/*  63 */       URL chatEndpoint = host.resolve("/" + chatEndpointConfig).toURL();
/*  64 */       URL joinEndpoint = getEndpoint(host, endpoints, "join", "v1/join");
/*  65 */       URL leaveEndpoint = getEndpoint(host, endpoints, "leave", "v1/leave");
/*     */       
/*  67 */       JoinOrLeaveEncoder commonJoinOrLeaveEncoder = user -> {
/*  68 */           JsonObject object = new JsonObject();
/*  69 */           object.addProperty("server", serverId);
/*  70 */           object.addProperty("room", roomId);
/*  71 */           object.addProperty("user_id", user.id().toString());
/*  72 */           object.addProperty("user_display_name", user.name());
/*  73 */           return object;
/*     */         };
/*     */ 
/*     */       
/*  77 */       if (isLegacyChatEndpoint) {
/*  78 */         chatEncoder = ((sender, message) -> {
/*  79 */             JsonObject object = new JsonObject();
/*  80 */             object.addProperty("rule", Integer.valueOf(ruleId));
/*  81 */             object.addProperty("server", serverId);
/*  82 */             object.addProperty("room", roomId);
/*  83 */             object.addProperty("player", sender.id().toString());
/*  84 */             object.addProperty("player_display_name", sender.name());
/*  85 */             object.addProperty("text", message);
/*  86 */             object.addProperty("language", "*");
/*  87 */             return object;
/*     */           });
/*     */       } else {
/*  90 */         String ruleIdStr = String.valueOf(ruleId);
/*  91 */         chatEncoder = ((sender, message) -> {
/*  92 */             JsonObject object = new JsonObject();
/*  93 */             object.addProperty("rule_id", ruleIdStr);
/*  94 */             object.addProperty("category", serverId);
/*  95 */             object.addProperty("subcategory", roomId);
/*  96 */             object.addProperty("user_id", sender.id().toString());
/*  97 */             object.addProperty("user_display_name", sender.name());
/*  98 */             object.addProperty("text", message);
/*  99 */             object.addProperty("language", "*");
/* 100 */             return object;
/*     */           });
/*     */       } 
/*     */       
/* 104 */       ServerTextFilter.IgnoreStrategy ignoreStrategy = ServerTextFilter.IgnoreStrategy.select(hashesToDrop);
/* 105 */       ExecutorService workerPool = createWorkerPool(maxConcurrentRequests);
/*     */       
/* 107 */       String encodedKey = Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.US_ASCII));
/* 108 */       return new LegacyTextFilter(chatEndpoint, chatEncoder, joinEndpoint, commonJoinOrLeaveEncoder, leaveEndpoint, commonJoinOrLeaveEncoder, encodedKey, ignoreStrategy, workerPool);
/* 109 */     } catch (Exception e) {
/* 110 */       LOGGER.warn("Failed to parse chat filter config {}", config, e);
/*     */ 
/*     */       
/* 113 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public TextFilter createContext(GameProfile gameProfile) {
/* 118 */     return new ServerTextFilter.PlayerContext(gameProfile)
/*     */       {
/*     */         public void join() {
/* 121 */           LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.joinEndpoint, LegacyTextFilter.this.joinEncoder, this.streamExecutor);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 126 */         public void leave() { LegacyTextFilter.this.processJoinOrLeave(this.profile, LegacyTextFilter.this.leaveEndpoint, LegacyTextFilter.this.leaveEncoder, this.streamExecutor); }
/*     */       };
/*     */   }
/*     */   @FunctionalInterface
/*     */   private static interface JoinOrLeaveEncoder {
/*     */     JsonObject encode(GameProfile param1GameProfile); }
/*     */   private void processJoinOrLeave(GameProfile user, URL endpoint, JoinOrLeaveEncoder encoder, Executor executor) {
/* 133 */     executor.execute(() -> {
/* 134 */           JsonObject object = encoder.encode(user);
/*     */           try {
/* 136 */             processRequest(object, endpoint);
/* 137 */           } catch (Exception e) {
/* 138 */             LOGGER.warn("Failed to send join/leave packet to {} for player {}", new Object[] { endpoint, user, e });
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void processRequest(JsonObject payload, URL url) throws IOException {
/* 144 */     HttpURLConnection connection = makeRequest(payload, url);
/*     */     
/* 146 */     InputStream is = connection.getInputStream(); 
/* 147 */     try { drainStream(is);
/* 148 */       if (is != null) is.close();  }
/*     */     catch (Throwable throwable) { if (is != null)
/*     */         try { is.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 153 */      } protected void setAuthorizationProperty(HttpURLConnection connection) { connection.setRequestProperty("Authorization", "Basic " + this.authKey); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilteredText filterText(String message, ServerTextFilter.IgnoreStrategy ignoreStrategy, JsonObject result) {
/* 158 */     boolean response = GsonHelper.getAsBoolean(result, "response", false);
/* 159 */     if (response) {
/* 160 */       return FilteredText.passThrough(message);
/*     */     }
/* 162 */     String filteredMessage = GsonHelper.getAsString(result, "hashed", null);
/* 163 */     if (filteredMessage == null) {
/* 164 */       return FilteredText.fullyFiltered(message);
/*     */     }
/*     */     
/* 167 */     JsonArray removedChars = GsonHelper.getAsJsonArray(result, "hashes");
/* 168 */     FilterMask mask = parseMask(message, removedChars, ignoreStrategy);
/* 169 */     return new FilteredText(message, mask);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\LegacyTextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */