/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class MinecraftServerStateServiceImpl
/*    */   implements MinecraftServerStateService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftServerStateServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
/* 19 */     this.server = server;
/* 20 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean isReady() { return this.server.isReady(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean saveEverything(boolean suppressLogs, boolean flush, boolean force, ClientInfo clientInfo) {
/* 30 */     this.jsonrpcLogger.log(clientInfo, "Save everything. SuppressLogs: {}, flush: {}, force: {}", new Object[] { Boolean.valueOf(suppressLogs), Boolean.valueOf(flush), Boolean.valueOf(force) });
/* 31 */     return this.server.saveEverything(suppressLogs, flush, force);
/*    */   }
/*    */ 
/*    */   
/*    */   public void halt(boolean waitForShutdown, ClientInfo clientInfo) {
/* 36 */     this.jsonrpcLogger.log(clientInfo, "Halt server. WaitForShutdown: {}", new Object[] { Boolean.valueOf(waitForShutdown) });
/* 37 */     this.server.halt(waitForShutdown);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendSystemMessage(Component message, ClientInfo clientInfo) {
/* 42 */     this.jsonrpcLogger.log(clientInfo, "Send system message: '{}'", new Object[] { message.getString() });
/* 43 */     this.server.sendSystemMessage(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendSystemMessage(Component message, boolean overlay, Collection<ServerPlayer> players, ClientInfo clientInfo) {
/* 48 */     List<String> playerNames = players.stream().map(Player::getPlainTextName).toList();
/* 49 */     this.jsonrpcLogger.log(clientInfo, "Send system message to '{}' players (overlay: {}): '{}'", new Object[] { Integer.valueOf(playerNames.size()), Boolean.valueOf(overlay), message.getString() });
/*    */     
/* 51 */     for (ServerPlayer player : players) {
/* 52 */       if (overlay) {
/* 53 */         player.sendSystemMessage(message, true); continue;
/*    */       } 
/* 55 */       player.sendSystemMessage(message);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void broadcastSystemMessage(Component message, boolean overlay, ClientInfo clientInfo) {
/* 62 */     this.jsonrpcLogger.log(clientInfo, "Broadcast system message (overlay: {}): '{}'", new Object[] { Boolean.valueOf(overlay), message.getString() });
/*    */     
/* 64 */     for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 65 */       if (overlay) {
/* 66 */         player.sendSystemMessage(message, true); continue;
/*    */       } 
/* 68 */       player.sendSystemMessage(message);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerStateServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */