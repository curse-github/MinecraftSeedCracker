/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ public class MinecraftBanListServiceImpl
/*    */   implements MinecraftBanListService
/*    */ {
/*    */   private final MinecraftServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftBanListServiceImpl(MinecraftServer server, JsonRpcLogger jsonrpcLogger) {
/* 18 */     this.server = server;
/* 19 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addUserBan(UserBanListEntry ban, ClientInfo clientInfo) {
/* 24 */     this.jsonrpcLogger.log(clientInfo, "Add player '{}' to banlist. Reason: '{}'", new Object[] { ban.getDisplayName(), ban.getReasonMessage().getString() });
/* 25 */     this.server.getPlayerList().getBans().add(ban);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeUserBan(NameAndId nameAndId, ClientInfo clientInfo) {
/* 30 */     this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from banlist", new Object[] { nameAndId });
/* 31 */     this.server.getPlayerList().getBans().remove(nameAndId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void clearUserBans(ClientInfo clientInfo) { this.server.getPlayerList().getBans().clear(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Collection<UserBanListEntry> getUserBanEntries() { return this.server.getPlayerList().getBans().getEntries(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Collection<IpBanListEntry> getIpBanEntries() { return this.server.getPlayerList().getIpBans().getEntries(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addIpBan(IpBanListEntry ipBanEntry, ClientInfo clientInfo) {
/* 51 */     this.jsonrpcLogger.log(clientInfo, "Add ip '{}' to ban list", new Object[] { ipBanEntry.getUser() });
/* 52 */     this.server.getPlayerList().getIpBans().add(ipBanEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearIpBans(ClientInfo clientInfo) {
/* 57 */     this.jsonrpcLogger.log(clientInfo, "Clear ip ban list", new Object[0]);
/* 58 */     this.server.getPlayerList().getIpBans().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeIpBan(String ip, ClientInfo clientInfo) {
/* 63 */     this.jsonrpcLogger.log(clientInfo, "Remove ip '{}' from ban list", new Object[] { ip });
/* 64 */     this.server.getPlayerList().getIpBans().remove(ip);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftBanListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */