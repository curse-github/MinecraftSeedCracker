/*     */ package net.minecraft.server.jsonrpc.internalapi;
/*     */ 
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class MinecraftServerSettingsServiceImpl
/*     */   implements MinecraftServerSettingsService {
/*     */   private final DedicatedServer server;
/*     */   private final JsonRpcLogger jsonrpcLogger;
/*     */   
/*     */   public MinecraftServerSettingsServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
/*  16 */     this.server = server;
/*  17 */     this.jsonrpcLogger = jsonrpcLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  22 */   public boolean isAutoSave() { return this.server.isAutoSave(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setAutoSave(boolean enabled, ClientInfo clientInfo) {
/*  27 */     this.jsonrpcLogger.log(clientInfo, "Update autosave from {} to {}", new Object[] { Boolean.valueOf(isAutoSave()), Boolean.valueOf(enabled) });
/*  28 */     this.server.setAutoSave(enabled);
/*  29 */     return isAutoSave();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public Difficulty getDifficulty() { return this.server.getWorldData().getDifficulty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Difficulty setDifficulty(Difficulty difficulty, ClientInfo clientInfo) {
/*  39 */     this.jsonrpcLogger.log(clientInfo, "Update difficulty from '{}' to '{}'", new Object[] { getDifficulty(), difficulty });
/*  40 */     this.server.setDifficulty(difficulty);
/*  41 */     return getDifficulty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public boolean isEnforceWhitelist() { return this.server.isEnforceWhitelist(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setEnforceWhitelist(boolean enforce, ClientInfo clientInfo) {
/*  51 */     this.jsonrpcLogger.log(clientInfo, "Update enforce allowlist from {} to {}", new Object[] { Boolean.valueOf(isEnforceWhitelist()), Boolean.valueOf(enforce) });
/*  52 */     this.server.setEnforceWhitelist(enforce);
/*  53 */     this.server.kickUnlistedPlayers();
/*  54 */     return isEnforceWhitelist();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public boolean isUsingWhitelist() { return this.server.isUsingWhitelist(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setUsingWhitelist(boolean use, ClientInfo clientInfo) {
/*  64 */     this.jsonrpcLogger.log(clientInfo, "Update using allowlist from {} to {}", new Object[] { Boolean.valueOf(isUsingWhitelist()), Boolean.valueOf(use) });
/*  65 */     this.server.setUsingWhitelist(use);
/*  66 */     this.server.kickUnlistedPlayers();
/*  67 */     return isUsingWhitelist();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public int getMaxPlayers() { return this.server.getMaxPlayers(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setMaxPlayers(int maxPlayers, ClientInfo clientInfo) {
/*  77 */     this.jsonrpcLogger.log(clientInfo, "Update max players from {} to {}", new Object[] { Integer.valueOf(getMaxPlayers()), Integer.valueOf(maxPlayers) });
/*  78 */     this.server.setMaxPlayers(maxPlayers);
/*  79 */     return getMaxPlayers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public int getPauseWhenEmptySeconds() { return this.server.pauseWhenEmptySeconds(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setPauseWhenEmptySeconds(int emptySeconds, ClientInfo clientInfo) {
/*  89 */     this.jsonrpcLogger.log(clientInfo, "Update pause when empty from {} seconds to {} seconds", new Object[] { Integer.valueOf(getPauseWhenEmptySeconds()), Integer.valueOf(emptySeconds) });
/*  90 */     this.server.setPauseWhenEmptySeconds(emptySeconds);
/*  91 */     return getPauseWhenEmptySeconds();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public int getPlayerIdleTimeout() { return this.server.playerIdleTimeout(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setPlayerIdleTimeout(int idleTime, ClientInfo clientInfo) {
/* 101 */     this.jsonrpcLogger.log(clientInfo, "Update player idle timeout from {} minutes to {} minutes", new Object[] { Integer.valueOf(getPlayerIdleTimeout()), Integer.valueOf(idleTime) });
/* 102 */     this.server.setPlayerIdleTimeout(idleTime);
/* 103 */     return getPlayerIdleTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean allowFlight() { return this.server.allowFlight(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setAllowFlight(boolean allow, ClientInfo clientInfo) {
/* 113 */     this.jsonrpcLogger.log(clientInfo, "Update allow flight from {} to {}", new Object[] { Boolean.valueOf(allowFlight()), Boolean.valueOf(allow) });
/* 114 */     this.server.setAllowFlight(allow);
/* 115 */     return allowFlight();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public int getSpawnProtectionRadius() { return this.server.spawnProtectionRadius(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setSpawnProtectionRadius(int spawnProtection, ClientInfo clientInfo) {
/* 125 */     this.jsonrpcLogger.log(clientInfo, "Update spawn protection radius from {} to {}", new Object[] { Integer.valueOf(getSpawnProtectionRadius()), Integer.valueOf(spawnProtection) });
/* 126 */     this.server.setSpawnProtectionRadius(spawnProtection);
/* 127 */     return getSpawnProtectionRadius();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public String getMotd() { return this.server.getMotd(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMotd(String motd, ClientInfo clientInfo) {
/* 137 */     this.jsonrpcLogger.log(clientInfo, "Update MOTD from '{}' to '{}'", new Object[] { getMotd(), motd });
/* 138 */     this.server.setMotd(motd);
/* 139 */     return getMotd();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public boolean forceGameMode() { return this.server.forceGameMode(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setForceGameMode(boolean force, ClientInfo clientInfo) {
/* 149 */     this.jsonrpcLogger.log(clientInfo, "Update force game mode from {} to {}", new Object[] { Boolean.valueOf(forceGameMode()), Boolean.valueOf(force) });
/* 150 */     this.server.setForceGameMode(force);
/* 151 */     return forceGameMode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public GameType getGameMode() { return this.server.gameMode(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public GameType setGameMode(GameType gameMode, ClientInfo clientInfo) {
/* 161 */     this.jsonrpcLogger.log(clientInfo, "Update game mode from '{}' to '{}'", new Object[] { getGameMode(), gameMode });
/* 162 */     this.server.setGameMode(gameMode);
/* 163 */     return getGameMode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public int getViewDistance() { return this.server.viewDistance(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setViewDistance(int viewDistance, ClientInfo clientInfo) {
/* 173 */     this.jsonrpcLogger.log(clientInfo, "Update view distance from {} to {}", new Object[] { Integer.valueOf(getViewDistance()), Integer.valueOf(viewDistance) });
/* 174 */     this.server.setViewDistance(viewDistance);
/* 175 */     return getViewDistance();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public int getSimulationDistance() { return this.server.simulationDistance(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setSimulationDistance(int simulationDistance, ClientInfo clientInfo) {
/* 185 */     this.jsonrpcLogger.log(clientInfo, "Update simulation distance from {} to {}", new Object[] { Integer.valueOf(getSimulationDistance()), Integer.valueOf(simulationDistance) });
/* 186 */     this.server.setSimulationDistance(simulationDistance);
/* 187 */     return getSimulationDistance();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public boolean acceptsTransfers() { return this.server.acceptsTransfers(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setAcceptsTransfers(boolean accept, ClientInfo clientInfo) {
/* 197 */     this.jsonrpcLogger.log(clientInfo, "Update accepts transfers from {} to {}", new Object[] { Boolean.valueOf(acceptsTransfers()), Boolean.valueOf(accept) });
/* 198 */     this.server.setAcceptsTransfers(accept);
/* 199 */     return acceptsTransfers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public int getStatusHeartbeatInterval() { return this.server.statusHeartbeatInterval(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setStatusHeartbeatInterval(int statusHeartbeatInterval, ClientInfo clientInfo) {
/* 209 */     this.jsonrpcLogger.log(clientInfo, "Update status heartbeat interval from {} to {}", new Object[] { Integer.valueOf(getStatusHeartbeatInterval()), Integer.valueOf(statusHeartbeatInterval) });
/* 210 */     this.server.setStatusHeartbeatInterval(statusHeartbeatInterval);
/* 211 */     return getStatusHeartbeatInterval();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 216 */   public LevelBasedPermissionSet getOperatorUserPermissions() { return this.server.operatorUserPermissions(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet permissions, ClientInfo clientInfo) {
/* 221 */     this.jsonrpcLogger.log(clientInfo, "Update operator user permission level from {} to {}", new Object[] { getOperatorUserPermissions(), permissions.level() });
/* 222 */     this.server.setOperatorUserPermissions(permissions);
/* 223 */     return getOperatorUserPermissions();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 228 */   public boolean hidesOnlinePlayers() { return this.server.hidesOnlinePlayers(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setHidesOnlinePlayers(boolean hide, ClientInfo clientInfo) {
/* 233 */     this.jsonrpcLogger.log(clientInfo, "Update hides online players from {} to {}", new Object[] { Boolean.valueOf(hidesOnlinePlayers()), Boolean.valueOf(hide) });
/* 234 */     this.server.setHidesOnlinePlayers(hide);
/* 235 */     return hidesOnlinePlayers();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public boolean repliesToStatus() { return this.server.repliesToStatus(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setRepliesToStatus(boolean enable, ClientInfo clientInfo) {
/* 245 */     this.jsonrpcLogger.log(clientInfo, "Update replies to status from {} to {}", new Object[] { Boolean.valueOf(repliesToStatus()), Boolean.valueOf(enable) });
/* 246 */     this.server.setRepliesToStatus(enable);
/* 247 */     return repliesToStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 252 */   public int getEntityBroadcastRangePercentage() { return this.server.entityBroadcastRangePercentage(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int setEntityBroadcastRangePercentage(int percentage, ClientInfo clientInfo) {
/* 257 */     this.jsonrpcLogger.log(clientInfo, "Update entity broadcast range percentage from {}% to {}%", new Object[] { Integer.valueOf(getEntityBroadcastRangePercentage()), Integer.valueOf(percentage) });
/* 258 */     this.server.setEntityBroadcastRangePercentage(percentage);
/* 259 */     return getEntityBroadcastRangePercentage();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerSettingsServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */