/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class MinecraftPlayerListServiceImpl
/*    */   implements MinecraftPlayerListService
/*    */ {
/*    */   private final JsonRpcLogger jsonRpcLogger;
/*    */   private final DedicatedServer server;
/*    */   
/*    */   public MinecraftPlayerListServiceImpl(DedicatedServer server, JsonRpcLogger jsonRpcLogger) {
/* 20 */     this.jsonRpcLogger = jsonRpcLogger;
/* 21 */     this.server = server;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public List<ServerPlayer> getPlayers() { return this.server.getPlayerList().getPlayers(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public ServerPlayer getPlayer(UUID uuid) { return this.server.getPlayerList().getPlayer(uuid); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Optional<NameAndId> fetchUserByName(String name) { return this.server.services().nameToIdCache().get(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public Optional<NameAndId> fetchUserById(UUID id) { return Optional.ofNullable(this.server.services().sessionService().fetchProfile(id, true))
/* 42 */       .map(profile -> new NameAndId(profile.profile())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Optional<NameAndId> getCachedUserById(UUID id) { return this.server.services().nameToIdCache().get(id); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<ServerPlayer> getPlayer(Optional<UUID> id, Optional<String> name) {
/* 52 */     if (id.isPresent())
/* 53 */       return Optional.ofNullable(this.server.getPlayerList().getPlayer((UUID)id.get())); 
/* 54 */     if (name.isPresent()) {
/* 55 */       return Optional.ofNullable(this.server.getPlayerList().getPlayerByName((String)name.get()));
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public List<ServerPlayer> getPlayersWithAddress(String ip) { return this.server.getPlayerList().getPlayersWithAddress(ip); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void remove(ServerPlayer serverPlayer, ClientInfo clientInfo) {
/* 67 */     this.server.getPlayerList().remove(serverPlayer);
/* 68 */     this.jsonRpcLogger.log(clientInfo, "Remove player '{}'", new Object[] { serverPlayer.getPlainTextName() });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public ServerPlayer getPlayerByName(String name) { return this.server.getPlayerList().getPlayerByName(name); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftPlayerListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */