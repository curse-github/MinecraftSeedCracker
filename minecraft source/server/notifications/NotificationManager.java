/*     */ package net.minecraft.server.notifications;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class NotificationManager
/*     */   implements NotificationService
/*     */ {
/*  15 */   private final List<NotificationService> notificationServices = Lists.newArrayList();
/*     */ 
/*     */   
/*  18 */   public void registerService(NotificationService notificationService) { this.notificationServices.add(notificationService); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public void playerJoined(ServerPlayer player) { this.notificationServices.forEach(notificationService -> notificationService.playerJoined(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   public void playerLeft(ServerPlayer player) { this.notificationServices.forEach(notificationService -> notificationService.playerLeft(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   public void serverStarted() { this.notificationServices.forEach(NotificationService::serverStarted); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public void serverShuttingDown() { this.notificationServices.forEach(NotificationService::serverShuttingDown); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public void serverSaveStarted() { this.notificationServices.forEach(NotificationService::serverSaveStarted); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public void serverSaveCompleted() { this.notificationServices.forEach(NotificationService::serverSaveCompleted); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public void serverActivityOccured() { this.notificationServices.forEach(NotificationService::serverActivityOccured); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public void playerOped(ServerOpListEntry operator) { this.notificationServices.forEach(notificationService -> notificationService.playerOped(operator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public void playerDeoped(ServerOpListEntry operator) { this.notificationServices.forEach(notificationService -> notificationService.playerDeoped(operator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public void playerAddedToAllowlist(NameAndId player) { this.notificationServices.forEach(notificationService -> notificationService.playerAddedToAllowlist(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public void playerRemovedFromAllowlist(NameAndId player) { this.notificationServices.forEach(notificationService -> notificationService.playerRemovedFromAllowlist(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public void ipBanned(IpBanListEntry ban) { this.notificationServices.forEach(notificationService -> notificationService.ipBanned(ban)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public void ipUnbanned(String ip) { this.notificationServices.forEach(notificationService -> notificationService.ipUnbanned(ip)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public void playerBanned(UserBanListEntry ban) { this.notificationServices.forEach(notificationService -> notificationService.playerBanned(ban)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void playerUnbanned(NameAndId player) { this.notificationServices.forEach(notificationService -> notificationService.playerUnbanned(player)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) { this.notificationServices.forEach(notificationService -> notificationService.onGameRuleChanged(gameRule, value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public void statusHeartbeat() { this.notificationServices.forEach(NotificationService::statusHeartbeat); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\notifications\NotificationManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */