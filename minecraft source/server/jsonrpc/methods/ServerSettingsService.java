/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ 
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ 
/*     */ public class ServerSettingsService
/*     */ {
/*  12 */   public static boolean autosave(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().isAutoSave(); }
/*     */ 
/*     */ 
/*     */   
/*  16 */   public static boolean setAutosave(MinecraftApi minecraftApi, boolean enabled, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setAutoSave(enabled, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  20 */   public static Difficulty difficulty(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getDifficulty(); }
/*     */ 
/*     */ 
/*     */   
/*  24 */   public static Difficulty setDifficulty(MinecraftApi minecraftApi, Difficulty difficulty, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setDifficulty(difficulty, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public static boolean enforceAllowlist(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().isEnforceWhitelist(); }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static boolean setEnforceAllowlist(MinecraftApi minecraftApi, boolean enforce, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setEnforceWhitelist(enforce, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static boolean usingAllowlist(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().isUsingWhitelist(); }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static boolean setUsingAllowlist(MinecraftApi minecraftApi, boolean use, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setUsingWhitelist(use, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static int maxPlayers(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getMaxPlayers(); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static int setMaxPlayers(MinecraftApi minecraftApi, int maxPlayers, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setMaxPlayers(maxPlayers, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static int pauseWhenEmpty(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getPauseWhenEmptySeconds(); }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static int setPauseWhenEmpty(MinecraftApi minecraftApi, int emptySeconds, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setPauseWhenEmptySeconds(emptySeconds, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static int playerIdleTimeout(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getPlayerIdleTimeout(); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static int setPlayerIdleTimeout(MinecraftApi minecraftApi, int idleTime, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setPlayerIdleTimeout(idleTime, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static boolean allowFlight(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().allowFlight(); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static boolean setAllowFlight(MinecraftApi minecraftApi, boolean allow, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setAllowFlight(allow, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static int spawnProtection(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getSpawnProtectionRadius(); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static int setSpawnProtection(MinecraftApi minecraftApi, int spawnProtection, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setSpawnProtectionRadius(spawnProtection, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static String motd(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getMotd(); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static String setMotd(MinecraftApi minecraftApi, String motd, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setMotd(motd, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static boolean forceGameMode(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().forceGameMode(); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static boolean setForceGameMode(MinecraftApi minecraftApi, boolean force, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setForceGameMode(force, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static GameType gameMode(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getGameMode(); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static GameType setGameMode(MinecraftApi minecraftApi, GameType gameMode, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setGameMode(gameMode, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static int viewDistance(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getViewDistance(); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static int setViewDistance(MinecraftApi minecraftApi, int viewDistance, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setViewDistance(viewDistance, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static int simulationDistance(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getSimulationDistance(); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static int setSimulationDistance(MinecraftApi minecraftApi, int simulationDistance, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setSimulationDistance(simulationDistance, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public static boolean acceptTransfers(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().acceptsTransfers(); }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static boolean setAcceptTransfers(MinecraftApi minecraftApi, boolean accept, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setAcceptsTransfers(accept, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public static int statusHeartbeatInterval(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getStatusHeartbeatInterval(); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static int setStatusHeartbeatInterval(MinecraftApi minecraftApi, int statusHeartbeatInterval, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setStatusHeartbeatInterval(statusHeartbeatInterval, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public static PermissionLevel operatorUserPermissionLevel(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getOperatorUserPermissions().level(); }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static PermissionLevel setOperatorUserPermissionLevel(MinecraftApi minecraftApi, PermissionLevel level, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setOperatorUserPermissions(LevelBasedPermissionSet.forLevel(level), clientInfo).level(); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static boolean hidesOnlinePlayers(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().hidesOnlinePlayers(); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static boolean setHidesOnlinePlayers(MinecraftApi minecraftApi, boolean hide, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setHidesOnlinePlayers(hide, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static boolean repliesToStatus(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().repliesToStatus(); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public static boolean setRepliesToStatus(MinecraftApi minecraftApi, boolean enable, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setRepliesToStatus(enable, clientInfo); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static int entityBroadcastRangePercentage(MinecraftApi minecraftApi) { return minecraftApi.serverSettingsService().getEntityBroadcastRangePercentage(); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static int setEntityBroadcastRangePercentage(MinecraftApi minecraftApi, int percentage, ClientInfo clientInfo) { return minecraftApi.serverSettingsService().setEntityBroadcastRangePercentage(percentage, clientInfo); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\ServerSettingsService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */