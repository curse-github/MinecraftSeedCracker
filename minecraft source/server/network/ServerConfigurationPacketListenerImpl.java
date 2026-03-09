/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.game.GameProtocols;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.network.config.JoinWorldTask;
/*     */ import net.minecraft.server.network.config.PrepareSpawnTask;
/*     */ import net.minecraft.server.network.config.ServerCodeOfConductConfigurationTask;
/*     */ import net.minecraft.server.network.config.ServerResourcePackConfigurationTask;
/*     */ import net.minecraft.server.network.config.SynchronizeRegistriesTask;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerConfigurationPacketListenerImpl extends ServerCommonPacketListenerImpl implements ServerConfigurationPacketListener, TickablePacketListener {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  48 */   private static final Component DISCONNECT_REASON_INVALID_DATA = Component.translatable("multiplayer.disconnect.invalid_player_data");
/*  49 */   private static final Component DISCONNECT_REASON_CONFIGURATION_ERROR = Component.translatable("multiplayer.disconnect.configuration_error");
/*     */   
/*     */   private final GameProfile gameProfile;
/*  52 */   private final Queue<ConfigurationTask> configurationTasks = new ConcurrentLinkedQueue();
/*     */   private ConfigurationTask currentTask;
/*     */   private ClientInformation clientInformation;
/*     */   private SynchronizeRegistriesTask synchronizeRegistriesTask;
/*     */   private PrepareSpawnTask prepareSpawnTask;
/*     */   
/*     */   public ServerConfigurationPacketListenerImpl(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
/*  59 */     super(server, connection, cookie);
/*  60 */     this.gameProfile = cookie.gameProfile();
/*  61 */     this.clientInformation = cookie.clientInformation();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected GameProfile playerProfile() { return this.gameProfile; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/*  71 */     LOGGER.info("{} ({}) lost connection: {}", new Object[] { this.gameProfile.name(), this.gameProfile.id(), details.reason().getString() });
/*  72 */     if (this.prepareSpawnTask != null) {
/*  73 */       this.prepareSpawnTask.close();
/*  74 */       this.prepareSpawnTask = null;
/*     */     } 
/*  76 */     super.onDisconnect(details);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isAcceptingMessages() { return this.connection.isConnected(); }
/*     */ 
/*     */   
/*     */   public void startConfiguration() {
/*  85 */     send(new ClientboundCustomPayloadPacket(new BrandPayload(this.server.getServerModName())));
/*     */     
/*  87 */     ServerLinks serverLinks = this.server.serverLinks();
/*  88 */     if (!serverLinks.isEmpty()) {
/*  89 */       send(new ClientboundServerLinksPacket(serverLinks.untrust()));
/*     */     }
/*     */     
/*  92 */     LayeredRegistryAccess<RegistryLayer> registries = this.server.registries();
/*  93 */     List<KnownPack> knownPacks = this.server.getResourceManager().listPacks().flatMap(packResources -> packResources.location().knownPackInfo().stream()).toList();
/*  94 */     send(new ClientboundUpdateEnabledFeaturesPacket(FeatureFlags.REGISTRY.toNames(this.server.getWorldData().enabledFeatures())));
/*     */     
/*  96 */     this.synchronizeRegistriesTask = new SynchronizeRegistriesTask(knownPacks, registries);
/*  97 */     this.configurationTasks.add(this.synchronizeRegistriesTask);
/*     */     
/*  99 */     addOptionalTasks();
/*     */ 
/*     */     
/* 102 */     returnToWorld();
/*     */   }
/*     */   
/*     */   public void returnToWorld() {
/* 106 */     this.prepareSpawnTask = new PrepareSpawnTask(this.server, new NameAndId(this.gameProfile));
/* 107 */     this.configurationTasks.add(this.prepareSpawnTask);
/* 108 */     this.configurationTasks.add(new JoinWorldTask());
/* 109 */     startNextTask();
/*     */   }
/*     */   
/*     */   private void addOptionalTasks() {
/* 113 */     Map<String, String> codeOfConducts = this.server.getCodeOfConducts();
/* 114 */     if (!codeOfConducts.isEmpty()) {
/* 115 */       this.configurationTasks.add(new ServerCodeOfConductConfigurationTask(() -> {
/* 116 */               String codeOfConduct = (String)codeOfConducts.get(this.clientInformation.language().toLowerCase(Locale.ROOT));
/* 117 */               if (codeOfConduct == null) {
/* 118 */                 codeOfConduct = (String)codeOfConducts.get("en_us");
/*     */               }
/* 120 */               if (codeOfConduct == null) {
/* 121 */                 codeOfConduct = (String)codeOfConducts.values().iterator().next();
/*     */               }
/* 123 */               return codeOfConduct;
/*     */             }));
/*     */     }
/* 126 */     this.server.getServerResourcePack().ifPresent(info -> this.configurationTasks.add(new ServerResourcePackConfigurationTask(info)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public void handleClientInformation(ServerboundClientInformationPacket packet) { this.clientInformation = packet.information(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
/* 136 */     super.handleResourcePackResponse(packet);
/*     */     
/* 138 */     if (packet.action().isTerminal()) {
/* 139 */       finishCurrentTask(ServerResourcePackConfigurationTask.TYPE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleSelectKnownPacks(ServerboundSelectKnownPacks packet) {
/* 145 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
/* 146 */     if (this.synchronizeRegistriesTask == null) {
/* 147 */       throw new IllegalStateException("Unexpected response from client: received pack selection, but no negotiation ongoing");
/*     */     }
/* 149 */     this.synchronizeRegistriesTask.handleResponse(packet.knownPacks(), this::send);
/* 150 */     finishCurrentTask(SynchronizeRegistriesTask.TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket packet) { finishCurrentTask(ServerCodeOfConductConfigurationTask.TYPE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleConfigurationFinished(ServerboundFinishConfigurationPacket packet) {
/* 160 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
/* 161 */     finishCurrentTask(JoinWorldTask.TYPE);
/*     */     
/* 163 */     this.connection.setupOutboundProtocol(GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(this.server.registryAccess())));
/*     */     try {
/* 165 */       PlayerList playerList = this.server.getPlayerList();
/* 166 */       if (playerList.getPlayer(this.gameProfile.id()) != null) {
/*     */ 
/*     */         
/* 169 */         disconnect(PlayerList.DUPLICATE_LOGIN_DISCONNECT_MESSAGE);
/*     */         return;
/*     */       } 
/* 172 */       Component loginError = playerList.canPlayerLogin(this.connection.getRemoteAddress(), new NameAndId(this.gameProfile));
/* 173 */       if (loginError != null) {
/* 174 */         disconnect(loginError);
/*     */         
/*     */         return;
/*     */       } 
/* 178 */       ((PrepareSpawnTask)Objects.requireNonNull(this.prepareSpawnTask)).spawnPlayer(this.connection, createCookie(this.clientInformation));
/* 179 */     } catch (Exception e) {
/* 180 */       LOGGER.error("Couldn't place player in world", e);
/* 181 */       disconnect(DISCONNECT_REASON_INVALID_DATA);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 187 */     keepConnectionAlive();
/*     */     
/* 189 */     ConfigurationTask task = this.currentTask;
/* 190 */     if (task != null) {
/*     */       try {
/* 192 */         if (task.tick()) {
/* 193 */           finishCurrentTask(task.type());
/*     */         }
/* 195 */       } catch (Exception e) {
/* 196 */         LOGGER.error("Failed to tick configuration task {}", task.type(), e);
/* 197 */         disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
/*     */       } 
/*     */     }
/*     */     
/* 201 */     if (this.prepareSpawnTask != null) {
/* 202 */       this.prepareSpawnTask.keepAlive();
/*     */     }
/*     */   }
/*     */   
/*     */   private void startNextTask() {
/* 207 */     if (this.currentTask != null) {
/* 208 */       throw new IllegalStateException("Task " + this.currentTask.type().id() + " has not finished yet");
/*     */     }
/*     */     
/* 211 */     if (!isAcceptingMessages()) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     ConfigurationTask task = (ConfigurationTask)this.configurationTasks.poll();
/* 216 */     if (task != null) {
/* 217 */       this.currentTask = task;
/*     */       try {
/* 219 */         task.start(this::send);
/* 220 */       } catch (Exception e) {
/* 221 */         LOGGER.error("Failed to start configuration task {}", task.type(), e);
/* 222 */         disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishCurrentTask(ConfigurationTask.Type taskTypeToFinish) {
/* 228 */     ConfigurationTask.Type currentTaskType = (this.currentTask != null) ? this.currentTask.type() : null;
/* 229 */     if (!taskTypeToFinish.equals(currentTaskType)) {
/* 230 */       throw new IllegalStateException("Unexpected request for task finish, current task: " + String.valueOf(currentTaskType) + ", requested: " + String.valueOf(taskTypeToFinish));
/*     */     }
/* 232 */     this.currentTask = null;
/* 233 */     startNextTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerConfigurationPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */