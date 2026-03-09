/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserWhiteListEntry;
/*    */ 
/*    */ public class MinecraftAllowListServiceImpl
/*    */   implements MinecraftAllowListService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftAllowListServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
/* 17 */     this.server = server;
/* 18 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public Collection<UserWhiteListEntry> getEntries() { return this.server.getPlayerList().getWhiteList().getEntries(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean add(UserWhiteListEntry infos, ClientInfo clientInfo) {
/* 28 */     this.jsonrpcLogger.log(clientInfo, "Add player '{}' to allowlist", new Object[] { infos.getUser() });
/* 29 */     return this.server.getPlayerList().getWhiteList().add(infos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear(ClientInfo clientInfo) {
/* 34 */     this.jsonrpcLogger.log(clientInfo, "Clear allowlist", new Object[0]);
/* 35 */     this.server.getPlayerList().getWhiteList().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(NameAndId nameAndId, ClientInfo clientInfo) {
/* 40 */     this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from allowlist", new Object[] { nameAndId });
/* 41 */     this.server.getPlayerList().getWhiteList().remove(nameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void kickUnlistedPlayers(ClientInfo clientInfo) {
/* 46 */     this.jsonrpcLogger.log(clientInfo, "Kick unlisted players", new Object[0]);
/* 47 */     this.server.kickUnlistedPlayers();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftAllowListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */