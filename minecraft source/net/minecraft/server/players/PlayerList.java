/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.Instant;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.OutgoingChatMessage;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLoginPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
/*     */ import net.minecraft.network.protocol.game.GameProtocols;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.PlayerAdvancements;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.network.CommonListenerCookie;
/*     */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.ServerStatsCounter;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.BorderChangeListener;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PlayerList
/*     */ {
/* 110 */   public static final File USERBANLIST_FILE = new File("banned-players.json");
/* 111 */   public static final File IPBANLIST_FILE = new File("banned-ips.json");
/* 112 */   public static final File OPLIST_FILE = new File("ops.json");
/* 113 */   public static final File WHITELIST_FILE = new File("whitelist.json");
/* 114 */   public static final Component CHAT_FILTERED_FULL = Component.translatable("chat.filtered_full");
/* 115 */   public static final Component DUPLICATE_LOGIN_DISCONNECT_MESSAGE = Component.translatable("multiplayer.disconnect.duplicate_login");
/* 116 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int SEND_PLAYER_INFO_INTERVAL = 600; private final MinecraftServer server; private final List<ServerPlayer> players; private final Map<UUID, ServerPlayer> playersByUUID; private final UserBanList bans; private final IpBanList ipBans; private final ServerOpList ops; private final UserWhiteList whitelist;
/*     */   private final Map<UUID, ServerStatsCounter> stats;
/* 118 */   private static final SimpleDateFormat BAN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z", Locale.ROOT); private final Map<UUID, PlayerAdvancements> advancements; private final PlayerDataStorage playerIo; private final LayeredRegistryAccess<RegistryLayer> registries; private int viewDistance; private int simulationDistance; private boolean allowCommandsForAllPlayers; private int sendAllPlayerInfoIn;
/*     */   
/*     */   public PlayerList(MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registries, PlayerDataStorage playerIo, NotificationService notificationService) {
/* 121 */     this.players = Lists.newArrayList();
/* 122 */     this.playersByUUID = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     this.stats = Maps.newHashMap();
/* 128 */     this.advancements = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     this.server = server;
/* 138 */     this.registries = registries;
/* 139 */     this.playerIo = playerIo;
/* 140 */     this.whitelist = new UserWhiteList(WHITELIST_FILE, notificationService);
/* 141 */     this.ops = new ServerOpList(OPLIST_FILE, notificationService);
/* 142 */     this.bans = new UserBanList(USERBANLIST_FILE, notificationService);
/* 143 */     this.ipBans = new IpBanList(IPBANLIST_FILE, notificationService);
/*     */   }
/*     */   public void placeNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie) {
/*     */     MutableComponent component;
/* 147 */     NameAndId gameProfile = player.nameAndId();
/*     */     
/* 149 */     UserNameToIdResolver profileCache = this.server.services().nameToIdCache();
/*     */ 
/*     */     
/* 152 */     Optional<NameAndId> oldProfile = profileCache.get(gameProfile.id());
/* 153 */     String oldName = (String)oldProfile.map(NameAndId::name).orElse(gameProfile.name());
/* 154 */     profileCache.add(gameProfile);
/*     */     
/* 156 */     ServerLevel level = player.level();
/*     */     
/* 158 */     String address = connection.getLoggableAddress(this.server.logIPs());
/* 159 */     LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", new Object[] { player.getPlainTextName(), address, Integer.valueOf(player.getId()), Double.valueOf(player.getX()), Double.valueOf(player.getY()), Double.valueOf(player.getZ()) });
/*     */     
/* 161 */     LevelData levelData = level.getLevelData();
/*     */     
/* 163 */     ServerGamePacketListenerImpl playerConnection = new ServerGamePacketListenerImpl(this.server, connection, player, cookie);
/* 164 */     connection.setupInboundProtocol(GameProtocols.SERVERBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(this.server.registryAccess()), playerConnection), playerConnection);
/* 165 */     playerConnection.suspendFlushing();
/*     */     
/* 167 */     GameRules gameRules = level.getGameRules();
/* 168 */     boolean immediateRespawn = ((Boolean)gameRules.get(GameRules.IMMEDIATE_RESPAWN)).booleanValue();
/* 169 */     boolean reducedDebugInfo = ((Boolean)gameRules.get(GameRules.REDUCED_DEBUG_INFO)).booleanValue();
/* 170 */     boolean doLimitedCrafting = ((Boolean)gameRules.get(GameRules.LIMITED_CRAFTING)).booleanValue();
/* 171 */     playerConnection.send(new ClientboundLoginPacket(player
/* 172 */           .getId(), levelData
/* 173 */           .isHardcore(), this.server
/* 174 */           .levelKeys(), 
/* 175 */           getMaxPlayers(), 
/* 176 */           getViewDistance(), 
/* 177 */           getSimulationDistance(), reducedDebugInfo, !immediateRespawn, doLimitedCrafting, player
/*     */ 
/*     */ 
/*     */           
/* 181 */           .createCommonSpawnInfo(level), this.server
/* 182 */           .enforceSecureProfile()));
/*     */     
/* 184 */     playerConnection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/* 185 */     playerConnection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
/* 186 */     playerConnection.send(new ClientboundSetHeldSlotPacket(player.getInventory().getSelectedSlot()));
/* 187 */     RecipeManager recipeManager = this.server.getRecipeManager();
/* 188 */     playerConnection.send(new ClientboundUpdateRecipesPacket(recipeManager
/* 189 */           .getSynchronizedItemProperties(), recipeManager
/* 190 */           .getSynchronizedStonecutterRecipes()));
/*     */     
/* 192 */     sendPlayerPermissionLevel(player);
/*     */     
/* 194 */     player.getStats().markAllDirty();
/*     */     
/* 196 */     player.getRecipeBook().sendInitialRecipeBook(player);
/*     */     
/* 198 */     updateEntireScoreboard(level.getScoreboard(), player);
/*     */     
/* 200 */     this.server.invalidateStatus();
/*     */     
/* 202 */     if (player.getGameProfile().name().equalsIgnoreCase(oldName)) {
/* 203 */       component = Component.translatable("multiplayer.player.joined", new Object[] { player.getDisplayName() });
/*     */     } else {
/* 205 */       component = Component.translatable("multiplayer.player.joined.renamed", new Object[] { player.getDisplayName(), oldName });
/*     */     } 
/* 207 */     broadcastSystemMessage(component.withStyle(ChatFormatting.YELLOW), false);
/* 208 */     playerConnection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
/*     */     
/* 210 */     ServerStatus status = this.server.getStatus();
/* 211 */     if (status != null && !cookie.transferred()) {
/* 212 */       player.sendServerStatus(status);
/*     */     }
/* 214 */     player.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(this.players));
/*     */     
/* 216 */     this.players.add(player);
/* 217 */     this.playersByUUID.put(player.getUUID(), player);
/*     */     
/* 219 */     broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
/*     */     
/* 221 */     sendLevelInfo(player, level);
/*     */ 
/*     */     
/* 224 */     level.addNewPlayer(player);
/*     */     
/* 226 */     this.server.getCustomBossEvents().onPlayerConnect(player);
/*     */     
/* 228 */     sendActivePlayerEffects(player);
/*     */     
/* 230 */     player.initInventoryMenu();
/*     */     
/* 232 */     this.server.notificationManager().playerJoined(player);
/*     */     
/* 234 */     playerConnection.resumeFlushing();
/*     */   }
/*     */   
/*     */   protected void updateEntireScoreboard(ServerScoreboard scoreboard, ServerPlayer player) {
/* 238 */     Set<Objective> objectives = Sets.newHashSet();
/*     */     
/* 240 */     for (PlayerTeam team : scoreboard.getPlayerTeams()) {
/* 241 */       player.connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
/*     */     }
/*     */     
/* 244 */     for (DisplaySlot slot : DisplaySlot.values()) {
/* 245 */       Objective objective = scoreboard.getDisplayObjective(slot);
/*     */       
/* 247 */       if (objective != null && !objectives.contains(objective)) {
/* 248 */         List<Packet<?>> packets = scoreboard.getStartTrackingPackets(objective);
/*     */         
/* 250 */         for (Packet<?> packet : packets) {
/* 251 */           player.connection.send(packet);
/*     */         }
/*     */         
/* 254 */         objectives.add(objective);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addWorldborderListener(final ServerLevel level) {
/* 260 */     level.getWorldBorder().addListener(new BorderChangeListener()
/*     */         {
/*     */           public void onSetSize(WorldBorder border, double newSize) {
/* 263 */             PlayerList.this.broadcastAll(new ClientboundSetBorderSizePacket(border), level.dimension());
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 268 */           public void onLerpSize(WorldBorder border, double fromSize, double targetSize, long ticks, long gameTime) { PlayerList.this.broadcastAll(new ClientboundSetBorderLerpSizePacket(border), level.dimension()); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 273 */           public void onSetCenter(WorldBorder border, double x, double z) { PlayerList.this.broadcastAll(new ClientboundSetBorderCenterPacket(border), level.dimension()); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 278 */           public void onSetWarningTime(WorldBorder border, int time) { PlayerList.this.broadcastAll(new ClientboundSetBorderWarningDelayPacket(border), level.dimension()); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 283 */           public void onSetWarningBlocks(WorldBorder border, int blocks) { PlayerList.this.broadcastAll(new ClientboundSetBorderWarningDistancePacket(border), level.dimension()); }
/*     */ 
/*     */ 
/*     */           
/*     */           public void onSetDamagePerBlock(WorldBorder border, double damagePerBlock) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void onSetSafeZone(WorldBorder border, double safeZone) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<CompoundTag> loadPlayerData(NameAndId nameAndId) {
/* 297 */     CompoundTag singleplayerTag = this.server.getWorldData().getLoadedPlayerTag();
/* 298 */     if (this.server.isSingleplayerOwner(nameAndId) && singleplayerTag != null) {
/* 299 */       LOGGER.debug("loading single player");
/* 300 */       return Optional.of(singleplayerTag);
/*     */     } 
/* 302 */     return this.playerIo.load(nameAndId);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void save(ServerPlayer player) {
/* 307 */     this.playerIo.save(player);
/* 308 */     ServerStatsCounter stats = (ServerStatsCounter)this.stats.get(player.getUUID());
/* 309 */     if (stats != null) {
/* 310 */       stats.save();
/*     */     }
/* 312 */     PlayerAdvancements advancements = (PlayerAdvancements)this.advancements.get(player.getUUID());
/* 313 */     if (advancements != null) {
/* 314 */       advancements.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void remove(ServerPlayer player) {
/* 319 */     ServerLevel level = player.level();
/* 320 */     player.awardStat(Stats.LEAVE_GAME);
/* 321 */     save(player);
/* 322 */     if (player.isPassenger()) {
/* 323 */       Entity vehicle = player.getRootVehicle();
/* 324 */       if (vehicle.hasExactlyOnePlayerPassenger()) {
/* 325 */         LOGGER.debug("Removing player mount");
/* 326 */         player.stopRiding();
/* 327 */         vehicle.getPassengersAndSelf().forEach(e -> e.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER));
/*     */       } 
/*     */     } 
/* 330 */     player.unRide();
/*     */     
/* 332 */     for (ThrownEnderpearl enderpearl : player.getEnderPearls()) {
/* 333 */       enderpearl.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
/*     */     }
/*     */     
/* 336 */     level.removePlayerImmediately(player, Entity.RemovalReason.UNLOADED_WITH_PLAYER);
/*     */     
/* 338 */     player.getAdvancements().stopListening();
/* 339 */     this.players.remove(player);
/* 340 */     this.server.getCustomBossEvents().onPlayerDisconnect(player);
/*     */     
/* 342 */     UUID uuid = player.getUUID();
/* 343 */     ServerPlayer serverPlayer = (ServerPlayer)this.playersByUUID.get(uuid);
/* 344 */     if (serverPlayer == player) {
/* 345 */       this.playersByUUID.remove(uuid);
/* 346 */       this.stats.remove(uuid);
/* 347 */       this.advancements.remove(uuid);
/* 348 */       this.server.notificationManager().playerLeft(player);
/*     */     } 
/* 350 */     broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
/*     */   }
/*     */   
/*     */   public Component canPlayerLogin(SocketAddress address, NameAndId nameAndId) {
/* 354 */     if (this.bans.isBanned(nameAndId)) {
/* 355 */       UserBanListEntry ban = (UserBanListEntry)this.bans.get(nameAndId);
/* 356 */       MutableComponent reason = Component.translatable("multiplayer.disconnect.banned.reason", new Object[] { ban.getReasonMessage() });
/*     */       
/* 358 */       if (ban.getExpires() != null) {
/* 359 */         reason.append(Component.translatable("multiplayer.disconnect.banned.expiration", new Object[] { BAN_DATE_FORMAT.format(ban.getExpires()) }));
/*     */       }
/*     */       
/* 362 */       return reason;
/*     */     } 
/*     */     
/* 365 */     if (!isWhiteListed(nameAndId)) {
/* 366 */       return Component.translatable("multiplayer.disconnect.not_whitelisted");
/*     */     }
/*     */     
/* 369 */     if (this.ipBans.isBanned(address)) {
/* 370 */       IpBanListEntry ban = this.ipBans.get(address);
/* 371 */       MutableComponent reason = Component.translatable("multiplayer.disconnect.banned_ip.reason", new Object[] { ban.getReasonMessage() });
/*     */       
/* 373 */       if (ban.getExpires() != null) {
/* 374 */         reason.append(Component.translatable("multiplayer.disconnect.banned_ip.expiration", new Object[] { BAN_DATE_FORMAT.format(ban.getExpires()) }));
/*     */       }
/*     */       
/* 377 */       return reason;
/*     */     } 
/*     */     
/* 380 */     if (this.players.size() >= getMaxPlayers() && !canBypassPlayerLimit(nameAndId)) {
/* 381 */       return Component.translatable("multiplayer.disconnect.server_full");
/*     */     }
/*     */     
/* 384 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean disconnectAllPlayersWithProfile(UUID playerId) {
/* 391 */     Set<ServerPlayer> dupes = Sets.newIdentityHashSet();
/* 392 */     for (ServerPlayer player : this.players) {
/* 393 */       if (player.getUUID().equals(playerId)) {
/* 394 */         dupes.add(player);
/*     */       }
/*     */     } 
/* 397 */     ServerPlayer serverPlayer = (ServerPlayer)this.playersByUUID.get(playerId);
/* 398 */     if (serverPlayer != null) {
/* 399 */       dupes.add(serverPlayer);
/*     */     }
/* 401 */     for (ServerPlayer player : dupes) {
/* 402 */       player.connection.disconnect(DUPLICATE_LOGIN_DISCONNECT_MESSAGE);
/*     */     }
/* 404 */     return !dupes.isEmpty();
/*     */   }
/*     */   
/*     */   public ServerPlayer respawn(ServerPlayer serverPlayer, boolean keepAllPlayerData, Entity.RemovalReason removalReason) {
/* 408 */     TeleportTransition respawnInfo = serverPlayer.findRespawnPositionAndUseSpawnBlock(!keepAllPlayerData, TeleportTransition.DO_NOTHING);
/*     */     
/* 410 */     this.players.remove(serverPlayer);
/*     */     
/* 412 */     serverPlayer.level().removePlayerImmediately(serverPlayer, removalReason);
/*     */     
/* 414 */     ServerLevel level = respawnInfo.newLevel();
/* 415 */     ServerPlayer player = new ServerPlayer(this.server, level, serverPlayer.getGameProfile(), serverPlayer.clientInformation());
/*     */     
/* 417 */     player.connection = serverPlayer.connection;
/* 418 */     player.restoreFrom(serverPlayer, keepAllPlayerData);
/* 419 */     player.setId(serverPlayer.getId());
/* 420 */     player.setMainArm(serverPlayer.getMainArm());
/*     */     
/* 422 */     if (!respawnInfo.missingRespawnBlock()) {
/* 423 */       player.copyRespawnPosition(serverPlayer);
/*     */     }
/*     */     
/* 426 */     for (String tag : serverPlayer.getTags()) {
/* 427 */       player.addTag(tag);
/*     */     }
/*     */     
/* 430 */     Vec3 pos = respawnInfo.position();
/* 431 */     player.snapTo(pos.x, pos.y, pos.z, respawnInfo.yRot(), respawnInfo.xRot());
/* 432 */     if (respawnInfo.missingRespawnBlock()) {
/* 433 */       player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
/*     */     }
/*     */ 
/*     */     
/* 437 */     byte dataToKeep = keepAllPlayerData ? 1 : 0;
/*     */     
/* 439 */     ServerLevel playerLevel = player.level();
/* 440 */     LevelData levelData = playerLevel.getLevelData();
/* 441 */     player.connection.send(new ClientboundRespawnPacket(player
/* 442 */           .createCommonSpawnInfo(playerLevel), dataToKeep));
/*     */ 
/*     */     
/* 445 */     player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
/* 446 */     player.connection.send(new ClientboundSetDefaultSpawnPositionPacket(level.getRespawnData()));
/* 447 */     player.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/* 448 */     player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
/* 449 */     sendActivePlayerEffects(player);
/* 450 */     sendLevelInfo(player, level);
/* 451 */     sendPlayerPermissionLevel(player);
/*     */     
/* 453 */     level.addRespawnedPlayer(player);
/* 454 */     this.players.add(player);
/* 455 */     this.playersByUUID.put(player.getUUID(), player);
/*     */     
/* 457 */     player.initInventoryMenu();
/* 458 */     player.setHealth(player.getHealth());
/*     */     
/* 460 */     ServerPlayer.RespawnConfig respawnConfig = player.getRespawnConfig();
/* 461 */     if (!keepAllPlayerData && respawnConfig != null) {
/* 462 */       LevelData.RespawnData respawnData = respawnConfig.respawnData();
/* 463 */       ServerLevel respawnLevel = this.server.getLevel(respawnData.dimension());
/* 464 */       if (respawnLevel != null) {
/* 465 */         BlockPos respawnPosition = respawnData.pos();
/* 466 */         BlockState blockState = respawnLevel.getBlockState(respawnPosition);
/* 467 */         if (blockState.is(Blocks.RESPAWN_ANCHOR))
/*     */         {
/* 469 */           player.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, respawnPosition.getX(), respawnPosition.getY(), respawnPosition.getZ(), 1.0F, 1.0F, level.getRandom().nextLong()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 474 */     return player;
/*     */   }
/*     */ 
/*     */   
/* 478 */   public void sendActivePlayerEffects(ServerPlayer player) { sendActiveEffects(player, player.connection); }
/*     */ 
/*     */   
/*     */   public void sendActiveEffects(LivingEntity livingEntity, ServerGamePacketListenerImpl connection) {
/* 482 */     for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
/* 483 */       connection.send(new ClientboundUpdateMobEffectPacket(livingEntity.getId(), effect, false));
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendPlayerPermissionLevel(ServerPlayer player) {
/* 488 */     LevelBasedPermissionSet permissions = this.server.getProfilePermissions(player.nameAndId());
/* 489 */     sendPlayerPermissionLevel(player, permissions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 495 */     if (++this.sendAllPlayerInfoIn > 600) {
/* 496 */       broadcastAll(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY), this.players));
/* 497 */       this.sendAllPlayerInfoIn = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastAll(Packet<?> packet) {
/* 502 */     for (ServerPlayer player : this.players) {
/* 503 */       player.connection.send(packet);
/*     */     }
/*     */   }
/*     */   
/*     */   public void broadcastAll(Packet<?> packet, ResourceKey<Level> dimension) {
/* 508 */     for (ServerPlayer player : this.players) {
/* 509 */       if (player.level().dimension() == dimension) {
/* 510 */         player.connection.send(packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastSystemToTeam(Player player, Component message) {
/* 516 */     PlayerTeam playerTeam = player.getTeam();
/* 517 */     if (playerTeam == null) {
/*     */       return;
/*     */     }
/* 520 */     Collection<String> teamPlayers = playerTeam.getPlayers();
/* 521 */     for (String name : teamPlayers) {
/* 522 */       ServerPlayer teamPlayer = getPlayerByName(name);
/* 523 */       if (teamPlayer == null || teamPlayer == player) {
/*     */         continue;
/*     */       }
/* 526 */       teamPlayer.sendSystemMessage(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastSystemToAllExceptTeam(Player player, Component message) {
/* 531 */     PlayerTeam playerTeam = player.getTeam();
/* 532 */     if (playerTeam == null) {
/* 533 */       broadcastSystemMessage(message, false);
/*     */       return;
/*     */     } 
/* 536 */     for (int i = 0; i < this.players.size(); i++) {
/* 537 */       ServerPlayer targetPlayer = (ServerPlayer)this.players.get(i);
/* 538 */       if (targetPlayer.getTeam() != playerTeam) {
/* 539 */         targetPlayer.sendSystemMessage(message);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String[] getPlayerNamesArray() {
/* 545 */     String[] names = new String[this.players.size()];
/* 546 */     for (int i = 0; i < this.players.size(); i++) {
/* 547 */       names[i] = ((ServerPlayer)this.players.get(i)).getGameProfile().name();
/*     */     }
/* 549 */     return names;
/*     */   }
/*     */ 
/*     */   
/* 553 */   public UserBanList getBans() { return this.bans; }
/*     */ 
/*     */ 
/*     */   
/* 557 */   public IpBanList getIpBans() { return this.ipBans; }
/*     */ 
/*     */ 
/*     */   
/* 561 */   public void op(NameAndId nameAndId) { op(nameAndId, Optional.empty(), Optional.empty()); }
/*     */ 
/*     */   
/*     */   public void op(NameAndId nameAndId, Optional<LevelBasedPermissionSet> permissions, Optional<Boolean> canBypassPlayerLimit) {
/* 565 */     this.ops.add(new ServerOpListEntry(nameAndId, (LevelBasedPermissionSet)permissions.orElse(this.server.operatorUserPermissions()), ((Boolean)canBypassPlayerLimit.orElse(Boolean.valueOf(this.ops.canBypassPlayerLimit(nameAndId)))).booleanValue()));
/* 566 */     ServerPlayer player = getPlayer(nameAndId.id());
/* 567 */     if (player != null) {
/* 568 */       sendPlayerPermissionLevel(player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deop(NameAndId nameAndId) {
/* 573 */     if (this.ops.remove(nameAndId)) {
/* 574 */       ServerPlayer player = getPlayer(nameAndId.id());
/* 575 */       if (player != null) {
/* 576 */         sendPlayerPermissionLevel(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendPlayerPermissionLevel(ServerPlayer player, LevelBasedPermissionSet permissions) {
/* 582 */     if (player.connection != null) {
/* 583 */       switch (permissions.level()) { default: throw new MatchException(null, null);
/*     */         case ALL: 
/*     */         case MODERATORS: 
/*     */         case GAMEMASTERS: 
/*     */         case ADMINS: 
/* 588 */         case OWNERS: break; }  byte eventId = 28;
/*     */       
/* 590 */       player.connection.send(new ClientboundEntityEventPacket(player, eventId));
/*     */     } 
/* 592 */     this.server.getCommands().sendCommands(player);
/*     */   }
/*     */ 
/*     */   
/* 596 */   public boolean isWhiteListed(NameAndId nameAndId) { return (!isUsingWhitelist() || this.ops.contains(nameAndId) || this.whitelist.contains(nameAndId)); }
/*     */ 
/*     */ 
/*     */   
/* 600 */   public boolean isOp(NameAndId nameAndId) { return (this.ops.contains(nameAndId) || (this.server.isSingleplayerOwner(nameAndId) && this.server.getWorldData().isAllowCommands()) || this.allowCommandsForAllPlayers); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerPlayer getPlayerByName(String name) {
/* 605 */     int size = this.players.size();
/* 606 */     for (int i = 0; i < size; i++) {
/* 607 */       ServerPlayer player = (ServerPlayer)this.players.get(i);
/* 608 */       if (player.getGameProfile().name().equalsIgnoreCase(name)) {
/* 609 */         return player;
/*     */       }
/*     */     } 
/* 612 */     return null;
/*     */   }
/*     */   
/*     */   public void broadcast(Player except, double x, double y, double z, double range, ResourceKey<Level> dimension, Packet<?> packet) {
/* 616 */     for (int i = 0; i < this.players.size(); i++) {
/* 617 */       ServerPlayer player = (ServerPlayer)this.players.get(i);
/* 618 */       if (player != except)
/*     */       {
/*     */         
/* 621 */         if (player.level().dimension() == dimension) {
/*     */ 
/*     */           
/* 624 */           double xd = x - player.getX();
/* 625 */           double yd = y - player.getY();
/* 626 */           double zd = z - player.getZ();
/* 627 */           if (xd * xd + yd * yd + zd * zd < range * range)
/* 628 */             player.connection.send(packet); 
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAll() {
/* 634 */     for (int i = 0; i < this.players.size(); i++) {
/* 635 */       save((ServerPlayer)this.players.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 640 */   public UserWhiteList getWhiteList() { return this.whitelist; }
/*     */ 
/*     */ 
/*     */   
/* 644 */   public String[] getWhiteListNames() { return this.whitelist.getUserList(); }
/*     */ 
/*     */ 
/*     */   
/* 648 */   public ServerOpList getOps() { return this.ops; }
/*     */ 
/*     */ 
/*     */   
/* 652 */   public String[] getOpNames() { return this.ops.getUserList(); }
/*     */ 
/*     */   
/*     */   public void reloadWhiteList() {}
/*     */ 
/*     */   
/*     */   public void sendLevelInfo(ServerPlayer player, ServerLevel level) {
/* 659 */     WorldBorder worldBorder = level.getWorldBorder();
/* 660 */     player.connection.send(new ClientboundInitializeBorderPacket(worldBorder));
/* 661 */     player.connection.send(new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(), ((Boolean)level.getGameRules().get(GameRules.ADVANCE_TIME)).booleanValue()));
/* 662 */     player.connection.send(new ClientboundSetDefaultSpawnPositionPacket(level.getRespawnData()));
/*     */     
/* 664 */     if (level.isRaining()) {
/* 665 */       player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
/* 666 */       player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, level.getRainLevel(1.0F)));
/* 667 */       player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, level.getThunderLevel(1.0F)));
/*     */     } 
/*     */     
/* 670 */     player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START, 0.0F));
/*     */     
/* 672 */     this.server.tickRateManager().updateJoiningPlayer(player);
/*     */   }
/*     */   
/*     */   public void sendAllPlayerInfo(ServerPlayer player) {
/* 676 */     player.inventoryMenu.sendAllDataToRemote();
/* 677 */     player.resetSentInfo();
/* 678 */     player.connection.send(new ClientboundSetHeldSlotPacket(player.getInventory().getSelectedSlot()));
/*     */   }
/*     */ 
/*     */   
/* 682 */   public int getPlayerCount() { return this.players.size(); }
/*     */ 
/*     */ 
/*     */   
/* 686 */   public int getMaxPlayers() { return this.server.getMaxPlayers(); }
/*     */ 
/*     */ 
/*     */   
/* 690 */   public boolean isUsingWhitelist() { return this.server.isUsingWhitelist(); }
/*     */ 
/*     */   
/*     */   public List<ServerPlayer> getPlayersWithAddress(String ip) {
/* 694 */     List<ServerPlayer> result = Lists.newArrayList();
/*     */     
/* 696 */     for (ServerPlayer player : this.players) {
/* 697 */       if (player.getIpAddress().equals(ip)) {
/* 698 */         result.add(player);
/*     */       }
/*     */     } 
/*     */     
/* 702 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 706 */   public int getViewDistance() { return this.viewDistance; }
/*     */ 
/*     */ 
/*     */   
/* 710 */   public int getSimulationDistance() { return this.simulationDistance; }
/*     */ 
/*     */ 
/*     */   
/* 714 */   public MinecraftServer getServer() { return this.server; }
/*     */ 
/*     */ 
/*     */   
/* 718 */   public CompoundTag getSingleplayerData() { return null; }
/*     */ 
/*     */ 
/*     */   
/* 722 */   public void setAllowCommandsForAllPlayers(boolean allowCommands) { this.allowCommandsForAllPlayers = allowCommands; }
/*     */ 
/*     */   
/*     */   public void removeAll() {
/* 726 */     for (int i = 0; i < this.players.size(); i++) {
/* 727 */       ((ServerPlayer)this.players.get(i)).connection.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 732 */   public void broadcastSystemMessage(Component message, boolean overlay) { broadcastSystemMessage(message, player -> message, overlay); }
/*     */ 
/*     */   
/*     */   public void broadcastSystemMessage(Component message, Function<ServerPlayer, Component> playerMessages, boolean overlay) {
/* 736 */     this.server.sendSystemMessage(message);
/* 737 */     for (ServerPlayer player : this.players) {
/* 738 */       Component playerMessage = (Component)playerMessages.apply(player);
/* 739 */       if (playerMessage != null) {
/* 740 */         player.sendSystemMessage(playerMessage, overlay);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 746 */   public void broadcastChatMessage(PlayerChatMessage message, CommandSourceStack sender, ChatType.Bound chatType) { Objects.requireNonNull(sender); broadcastChatMessage(message, sender::shouldFilterMessageTo, sender.getPlayer(), chatType); }
/*     */ 
/*     */ 
/*     */   
/* 750 */   public void broadcastChatMessage(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound chatType) { Objects.requireNonNull(sender); broadcastChatMessage(message, sender::shouldFilterMessageTo, sender, chatType); }
/*     */ 
/*     */   
/*     */   private void broadcastChatMessage(PlayerChatMessage message, Predicate<ServerPlayer> isFiltered, ServerPlayer senderPlayer, ChatType.Bound chatType) {
/* 754 */     boolean trusted = verifyChatTrusted(message);
/* 755 */     this.server.logChatMessage(message.decoratedContent(), chatType, trusted ? null : "Not Secure");
/* 756 */     OutgoingChatMessage tracked = OutgoingChatMessage.create(message);
/*     */     
/* 758 */     boolean wasFullyFiltered = false;
/*     */     
/* 760 */     for (ServerPlayer player : this.players) {
/* 761 */       boolean filtered = isFiltered.test(player);
/* 762 */       player.sendChatMessage(tracked, filtered, chatType);
/* 763 */       wasFullyFiltered |= ((filtered && message.isFullyFiltered()));
/*     */     } 
/*     */     
/* 766 */     if (wasFullyFiltered && senderPlayer != null) {
/* 767 */       senderPlayer.sendSystemMessage(CHAT_FILTERED_FULL);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 772 */   private boolean verifyChatTrusted(PlayerChatMessage message) { return (message.hasSignature() && !message.hasExpiredServer(Instant.now())); }
/*     */ 
/*     */   
/*     */   public ServerStatsCounter getPlayerStats(Player player) {
/* 776 */     GameProfile gameProfile = player.getGameProfile();
/* 777 */     return (ServerStatsCounter)this.stats.computeIfAbsent(gameProfile.id(), id -> {
/* 778 */           Path targetFile = locateStatsFile(gameProfile);
/* 779 */           return new ServerStatsCounter(this.server, targetFile);
/*     */         });
/*     */   }
/*     */   
/*     */   private Path locateStatsFile(GameProfile gameProfile) {
/* 784 */     Path statFolder = this.server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
/* 785 */     Path uuidStatsFile = statFolder.resolve(String.valueOf(gameProfile.id()) + ".json");
/*     */     
/* 787 */     if (Files.exists(uuidStatsFile, new java.nio.file.LinkOption[0])) {
/* 788 */       return uuidStatsFile;
/*     */     }
/*     */ 
/*     */     
/* 792 */     String playerNameStatsFile = gameProfile.name() + ".json";
/* 793 */     if (FileUtil.isValidPathSegment(playerNameStatsFile)) {
/* 794 */       Path playerNameStatsPath = statFolder.resolve(playerNameStatsFile);
/* 795 */       if (Files.isRegularFile(playerNameStatsPath, new java.nio.file.LinkOption[0])) {
/*     */         try {
/* 797 */           return Files.move(playerNameStatsPath, uuidStatsFile, new java.nio.file.CopyOption[0]);
/* 798 */         } catch (IOException e) {
/*     */           
/* 800 */           LOGGER.warn("Failed to copy file {} to {}", playerNameStatsFile, uuidStatsFile);
/* 801 */           return playerNameStatsPath;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 806 */     return uuidStatsFile;
/*     */   }
/*     */   
/*     */   public PlayerAdvancements getPlayerAdvancements(ServerPlayer player) {
/* 810 */     UUID uuid = player.getUUID();
/* 811 */     PlayerAdvancements result = (PlayerAdvancements)this.advancements.get(uuid);
/* 812 */     if (result == null) {
/* 813 */       Path uuidStatsFile = this.server.getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve(String.valueOf(uuid) + ".json");
/* 814 */       result = new PlayerAdvancements(this.server.getFixerUpper(), this, this.server.getAdvancements(), uuidStatsFile, player);
/* 815 */       this.advancements.put(uuid, result);
/*     */     } 
/*     */     
/* 818 */     result.setPlayer(player);
/*     */     
/* 820 */     return result;
/*     */   }
/*     */   
/*     */   public void setViewDistance(int viewDistance) {
/* 824 */     this.viewDistance = viewDistance;
/* 825 */     broadcastAll(new ClientboundSetChunkCacheRadiusPacket(viewDistance));
/*     */     
/* 827 */     for (ServerLevel level : this.server.getAllLevels()) {
/* 828 */       level.getChunkSource().setViewDistance(viewDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSimulationDistance(int simulationDistance) {
/* 833 */     this.simulationDistance = simulationDistance;
/* 834 */     broadcastAll(new ClientboundSetSimulationDistancePacket(simulationDistance));
/*     */     
/* 836 */     for (ServerLevel level : this.server.getAllLevels()) {
/* 837 */       level.getChunkSource().setSimulationDistance(simulationDistance);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 842 */   public List<ServerPlayer> getPlayers() { return this.players; }
/*     */ 
/*     */ 
/*     */   
/* 846 */   public ServerPlayer getPlayer(UUID uuid) { return (ServerPlayer)this.playersByUUID.get(uuid); }
/*     */ 
/*     */   
/*     */   public ServerPlayer getPlayer(String playerName) {
/* 850 */     for (ServerPlayer player : this.players) {
/* 851 */       if (player.getGameProfile().name().equalsIgnoreCase(playerName)) {
/* 852 */         return player;
/*     */       }
/*     */     } 
/* 855 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 859 */   public boolean canBypassPlayerLimit(NameAndId nameAndId) { return false; }
/*     */ 
/*     */   
/*     */   public void reloadResources() {
/* 863 */     for (PlayerAdvancements advancements : this.advancements.values()) {
/* 864 */       advancements.reload(this.server.getAdvancements());
/*     */     }
/* 866 */     broadcastAll(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
/* 867 */     RecipeManager recipeManager = this.server.getRecipeManager();
/*     */ 
/*     */     
/* 870 */     ClientboundUpdateRecipesPacket recipes = new ClientboundUpdateRecipesPacket(recipeManager.getSynchronizedItemProperties(), recipeManager.getSynchronizedStonecutterRecipes());
/*     */     
/* 872 */     for (ServerPlayer player : this.players) {
/* 873 */       player.connection.send(recipes);
/* 874 */       player.getRecipeBook().sendInitialRecipeBook(player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 879 */   public boolean isAllowCommandsForAllPlayers() { return this.allowCommandsForAllPlayers; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\PlayerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */