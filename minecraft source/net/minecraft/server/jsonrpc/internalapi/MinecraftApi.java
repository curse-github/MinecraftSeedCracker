/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftApi
/*    */ {
/*    */   private final NotificationManager notificationManager;
/*    */   private final MinecraftAllowListService allowListService;
/*    */   private final MinecraftBanListService banListService;
/*    */   private final MinecraftPlayerListService minecraftPlayerListService;
/*    */   private final MinecraftGameRuleService gameRuleService;
/*    */   private final MinecraftOperatorListService minecraftOperatorListService;
/*    */   private final MinecraftServerSettingsService minecraftServerSettingsService;
/*    */   private final MinecraftServerStateService minecraftServerStateService;
/*    */   private final MinecraftExecutorService executorService;
/*    */   
/*    */   public MinecraftApi(NotificationManager notificationManager, MinecraftAllowListService allowListService, MinecraftBanListService banListService, MinecraftPlayerListService minecraftPlayerListService, MinecraftGameRuleService gameRuleService, MinecraftOperatorListService minecraftOperatorListService, MinecraftServerSettingsService minecraftServerSettingsService, MinecraftServerStateService minecraftServerStateService, MinecraftExecutorService executorService) {
/* 32 */     this.notificationManager = notificationManager;
/* 33 */     this.allowListService = allowListService;
/* 34 */     this.banListService = banListService;
/* 35 */     this.minecraftPlayerListService = minecraftPlayerListService;
/* 36 */     this.gameRuleService = gameRuleService;
/* 37 */     this.minecraftOperatorListService = minecraftOperatorListService;
/* 38 */     this.minecraftServerSettingsService = minecraftServerSettingsService;
/* 39 */     this.minecraftServerStateService = minecraftServerStateService;
/* 40 */     this.executorService = executorService;
/*    */   }
/*    */ 
/*    */   
/* 44 */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) { return this.executorService.submit(supplier); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public CompletableFuture<Void> submit(Runnable runnable) { return this.executorService.submit(runnable); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public MinecraftAllowListService allowListService() { return this.allowListService; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public MinecraftBanListService banListService() { return this.banListService; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public MinecraftPlayerListService playerListService() { return this.minecraftPlayerListService; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public MinecraftGameRuleService gameRuleService() { return this.gameRuleService; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public MinecraftOperatorListService operatorListService() { return this.minecraftOperatorListService; }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public MinecraftServerSettingsService serverSettingsService() { return this.minecraftServerSettingsService; }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public MinecraftServerStateService serverStateService() { return this.minecraftServerStateService; }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public NotificationManager notificationManager() { return this.notificationManager; }
/*    */ 
/*    */   
/*    */   public static MinecraftApi of(DedicatedServer server) {
/* 84 */     JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
/* 85 */     MinecraftAllowListServiceImpl allowListService = new MinecraftAllowListServiceImpl(server, jsonrpcLogger);
/* 86 */     MinecraftBanListServiceImpl banListService = new MinecraftBanListServiceImpl(server, jsonrpcLogger);
/* 87 */     MinecraftPlayerListServiceImpl playerListService = new MinecraftPlayerListServiceImpl(server, jsonrpcLogger);
/* 88 */     MinecraftGameRuleServiceImpl gameRuleService = new MinecraftGameRuleServiceImpl(server, jsonrpcLogger);
/* 89 */     MinecraftOperatorListServiceImpl operatorListService = new MinecraftOperatorListServiceImpl(server, jsonrpcLogger);
/* 90 */     MinecraftServerSettingsServiceImpl serverSettingsService = new MinecraftServerSettingsServiceImpl(server, jsonrpcLogger);
/* 91 */     MinecraftServerStateServiceImpl serverStateService = new MinecraftServerStateServiceImpl(server, jsonrpcLogger);
/* 92 */     MinecraftExecutorService executorService = new MinecraftExecutorServiceImpl(server);
/* 93 */     return new MinecraftApi(server
/* 94 */         .notificationManager(), allowListService, banListService, playerListService, gameRuleService, operatorListService, serverSettingsService, serverStateService, executorService);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftApi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */