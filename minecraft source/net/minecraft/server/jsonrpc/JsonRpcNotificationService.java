/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class JsonRpcNotificationService
/*     */   implements NotificationService
/*     */ {
/*     */   private final ManagementServer managementServer;
/*     */   private final MinecraftApi minecraftApi;
/*     */   
/*     */   public JsonRpcNotificationService(MinecraftApi minecraftApi, ManagementServer managementServer) {
/*  26 */     this.minecraftApi = minecraftApi;
/*  27 */     this.managementServer = managementServer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public void playerJoined(ServerPlayer player) { broadcastNotification(OutgoingRpcMethods.PLAYER_JOINED, PlayerDto.from(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public void playerLeft(ServerPlayer player) { broadcastNotification(OutgoingRpcMethods.PLAYER_LEFT, PlayerDto.from(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public void serverStarted() { broadcastNotification(OutgoingRpcMethods.SERVER_STARTED); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public void serverShuttingDown() { broadcastNotification(OutgoingRpcMethods.SERVER_SHUTTING_DOWN); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public void serverSaveStarted() { broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_STARTED); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public void serverSaveCompleted() { broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_COMPLETED); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public void serverActivityOccured() { broadcastNotification(OutgoingRpcMethods.SERVER_ACTIVITY_OCCURRED); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public void playerOped(ServerOpListEntry operator) { broadcastNotification(OutgoingRpcMethods.PLAYER_OPED, OperatorService.OperatorDto.from(operator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public void playerDeoped(ServerOpListEntry operator) { broadcastNotification(OutgoingRpcMethods.PLAYER_DEOPED, OperatorService.OperatorDto.from(operator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public void playerAddedToAllowlist(NameAndId player) { broadcastNotification(OutgoingRpcMethods.PLAYER_ADDED_TO_ALLOWLIST, PlayerDto.from(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public void playerRemovedFromAllowlist(NameAndId player) { broadcastNotification(OutgoingRpcMethods.PLAYER_REMOVED_FROM_ALLOWLIST, PlayerDto.from(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void ipBanned(IpBanListEntry ban) { broadcastNotification(OutgoingRpcMethods.IP_BANNED, IpBanlistService.IpBanDto.from(ban)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void ipUnbanned(String ip) { broadcastNotification(OutgoingRpcMethods.IP_UNBANNED, ip); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void playerBanned(UserBanListEntry ban) { broadcastNotification(OutgoingRpcMethods.PLAYER_BANNED, BanlistService.UserBanDto.from(ban)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public void playerUnbanned(NameAndId player) { broadcastNotification(OutgoingRpcMethods.PLAYER_UNBANNED, PlayerDto.from(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) { broadcastNotification(OutgoingRpcMethods.GAMERULE_CHANGED, GameRulesService.getTypedRule(this.minecraftApi, gameRule, value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public void statusHeartbeat() { broadcastNotification(OutgoingRpcMethods.STATUS_HEARTBEAT, ServerStateService.status(this.minecraftApi)); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   private void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> method) { this.managementServer.forEachConnection(connection -> connection.sendNotification(method)); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   private <Params> void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> method, Params params) { this.managementServer.forEachConnection(connection -> connection.sendNotification(method, params)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\JsonRpcNotificationService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */