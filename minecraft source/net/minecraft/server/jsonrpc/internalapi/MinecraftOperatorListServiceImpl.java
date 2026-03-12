/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ 
/*    */ public class MinecraftOperatorListServiceImpl
/*    */   implements MinecraftOperatorListService
/*    */ {
/*    */   private final MinecraftServer minecraftServer;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftOperatorListServiceImpl(MinecraftServer minecraftServer, JsonRpcLogger jsonrpcLogger) {
/* 20 */     this.minecraftServer = minecraftServer;
/* 21 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public Collection<ServerOpListEntry> getEntries() { return this.minecraftServer.getPlayerList().getOps().getEntries(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void op(NameAndId nameAndId, Optional<PermissionLevel> permissionLevel, Optional<Boolean> canBypassPlayerLimit, ClientInfo clientInfo) {
/* 31 */     this.jsonrpcLogger.log(clientInfo, "Op '{}'", new Object[] { nameAndId });
/* 32 */     this.minecraftServer.getPlayerList().op(nameAndId, permissionLevel.map(LevelBasedPermissionSet::forLevel), canBypassPlayerLimit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void op(NameAndId nameAndId, ClientInfo clientInfo) {
/* 37 */     this.jsonrpcLogger.log(clientInfo, "Op '{}'", new Object[] { nameAndId });
/* 38 */     this.minecraftServer.getPlayerList().op(nameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deop(NameAndId nameAndId, ClientInfo clientInfo) {
/* 43 */     this.jsonrpcLogger.log(clientInfo, "Deop '{}'", new Object[] { nameAndId });
/* 44 */     this.minecraftServer.getPlayerList().deop(nameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear(ClientInfo clientInfo) {
/* 49 */     this.jsonrpcLogger.log(clientInfo, "Clear operator list", new Object[0]);
/* 50 */     this.minecraftServer.getPlayerList().getOps().clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftOperatorListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */