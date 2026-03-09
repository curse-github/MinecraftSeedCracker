/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.microsoft.aad.msal4j.ClientCredentialFactory;
/*     */ import com.microsoft.aad.msal4j.ClientCredentialParameters;
/*     */ import com.microsoft.aad.msal4j.ConfidentialClientApplication;
/*     */ import com.microsoft.aad.msal4j.IAuthenticationResult;
/*     */ import com.microsoft.aad.msal4j.IClientCertificate;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ 
/*     */ public class PlayerSafetyServiceTextFilter
/*     */   extends ServerTextFilter
/*     */ {
/*     */   private final ConfidentialClientApplication client;
/*     */   private final ClientCredentialParameters clientParameters;
/*     */   private final Set<String> fullyFilteredEvents;
/*     */   private final int connectionReadTimeoutMs;
/*     */   
/*     */   private PlayerSafetyServiceTextFilter(URL chatEndpoint, ServerTextFilter.MessageEncoder chatEncoder, ServerTextFilter.IgnoreStrategy chatIgnoreStrategy, ExecutorService workerPool, ConfidentialClientApplication client, ClientCredentialParameters clientParameters, Set<String> fullyFilteredEvents, int connectionReadTimeoutMs) {
/*  34 */     super(chatEndpoint, chatEncoder, chatIgnoreStrategy, workerPool);
/*  35 */     this.client = client;
/*  36 */     this.clientParameters = clientParameters;
/*  37 */     this.fullyFilteredEvents = fullyFilteredEvents;
/*  38 */     this.connectionReadTimeoutMs = connectionReadTimeoutMs; } public static ServerTextFilter createTextFilterFromConfig(String textFilteringConfig) {
/*     */     ConfidentialClientApplication client;
/*     */     IClientCertificate certificate;
/*     */     URL chatEndpoint;
/*  42 */     JsonObject parsedConfig = GsonHelper.parse(textFilteringConfig);
/*  43 */     URI host = URI.create(GsonHelper.getAsString(parsedConfig, "apiServer"));
/*  44 */     String apiPath = GsonHelper.getAsString(parsedConfig, "apiPath");
/*  45 */     String scope = GsonHelper.getAsString(parsedConfig, "scope");
/*  46 */     String serverId = GsonHelper.getAsString(parsedConfig, "serverId", "");
/*  47 */     String applicationId = GsonHelper.getAsString(parsedConfig, "applicationId");
/*  48 */     String tenantId = GsonHelper.getAsString(parsedConfig, "tenantId");
/*  49 */     String roomId = GsonHelper.getAsString(parsedConfig, "roomId", "Java:Chat");
/*  50 */     String certificatePath = GsonHelper.getAsString(parsedConfig, "certificatePath");
/*  51 */     String certificatePassword = GsonHelper.getAsString(parsedConfig, "certificatePassword", "");
/*  52 */     int hashesToDrop = GsonHelper.getAsInt(parsedConfig, "hashesToDrop", -1);
/*  53 */     int maxConcurrentRequests = GsonHelper.getAsInt(parsedConfig, "maxConcurrentRequests", 7);
/*  54 */     JsonArray fullyFilteredEvents = GsonHelper.getAsJsonArray(parsedConfig, "fullyFilteredEvents");
/*  55 */     Set<String> fullyFilteredEventsSet = new HashSet<String>();
/*  56 */     fullyFilteredEvents.forEach(elements -> fullyFilteredEventsSet.add(GsonHelper.convertToString(elements, "filteredEvent")));
/*  57 */     int connectionReadTimeoutMs = GsonHelper.getAsInt(parsedConfig, "connectionReadTimeoutMs", 2000);
/*     */ 
/*     */     
/*     */     try {
/*  61 */       chatEndpoint = host.resolve(apiPath).toURL();
/*  62 */     } catch (MalformedURLException e) {
/*  63 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/*  66 */     ServerTextFilter.MessageEncoder chatEncoder = (sender, message) -> {
/*  67 */         JsonObject object = new JsonObject();
/*  68 */         object.addProperty("userId", sender.id().toString());
/*  69 */         object.addProperty("userDisplayName", sender.name());
/*  70 */         object.addProperty("server", serverId);
/*  71 */         object.addProperty("room", roomId);
/*  72 */         object.addProperty("area", "JavaChatRealms");
/*  73 */         object.addProperty("data", message);
/*  74 */         object.addProperty("language", "*");
/*  75 */         return object;
/*     */       };
/*  77 */     ServerTextFilter.IgnoreStrategy ignoreStrategy = ServerTextFilter.IgnoreStrategy.select(hashesToDrop);
/*     */     
/*  79 */     ExecutorService workerPool = createWorkerPool(maxConcurrentRequests);
/*     */ 
/*     */     
/*  82 */     try { client = Files.newInputStream(Path.of(certificatePath, new String[0]), new java.nio.file.OpenOption[0]); 
/*  83 */       try { certificate = ClientCredentialFactory.createFromCertificate(client, certificatePassword);
/*  84 */         if (client != null) client.close();  } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  85 */     { LOGGER.warn("Failed to open certificate file");
/*  86 */       return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       client = ((ConfidentialClientApplication.Builder)((ConfidentialClientApplication.Builder)ConfidentialClientApplication.builder(applicationId, certificate).sendX5c(true).executorService(workerPool)).authority(String.format(Locale.ROOT, "https://login.microsoftonline.com/%s/", new Object[] { tenantId }))).build();
/*  97 */     } catch (Exception e) {
/*  98 */       LOGGER.warn("Failed to create confidential client application");
/*  99 */       return null;
/*     */     } 
/*     */     
/* 102 */     ClientCredentialParameters parameters = ClientCredentialParameters.builder(Set.of(scope)).build();
/* 103 */     return new PlayerSafetyServiceTextFilter(chatEndpoint, chatEncoder, ignoreStrategy, workerPool, client, parameters, fullyFilteredEventsSet, connectionReadTimeoutMs);
/*     */   }
/*     */ 
/*     */   
/* 107 */   private IAuthenticationResult aquireIAuthenticationResult() { return (IAuthenticationResult)this.client.acquireToken(this.clientParameters).join(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAuthorizationProperty(HttpURLConnection connection) {
/* 112 */     IAuthenticationResult authenticationResult = aquireIAuthenticationResult();
/* 113 */     connection.setRequestProperty("Authorization", "Bearer " + authenticationResult.accessToken());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FilteredText filterText(String message, ServerTextFilter.IgnoreStrategy ignoreStrategy, JsonObject response) {
/* 118 */     JsonObject result = GsonHelper.getAsJsonObject(response, "result", null);
/* 119 */     if (result == null) {
/* 120 */       return FilteredText.fullyFiltered(message);
/*     */     }
/* 122 */     boolean filtered = GsonHelper.getAsBoolean(result, "filtered", true);
/* 123 */     if (!filtered) {
/* 124 */       return FilteredText.passThrough(message);
/*     */     }
/* 126 */     JsonArray events = GsonHelper.getAsJsonArray(result, "events", new JsonArray());
/* 127 */     for (JsonElement element : events) {
/* 128 */       JsonObject object = element.getAsJsonObject();
/*     */       
/* 130 */       String event = GsonHelper.getAsString(object, "id", "");
/* 131 */       if (this.fullyFilteredEvents.contains(event)) {
/* 132 */         return FilteredText.fullyFiltered(message);
/*     */       }
/*     */     } 
/*     */     
/* 136 */     JsonArray redactedTextIndices = GsonHelper.getAsJsonArray(result, "redactedTextIndex", new JsonArray());
/* 137 */     return new FilteredText(message, parseMask(message, redactedTextIndices, ignoreStrategy));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected int connectionReadTimeout() { return this.connectionReadTimeoutMs; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\PlayerSafetyServiceTextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */