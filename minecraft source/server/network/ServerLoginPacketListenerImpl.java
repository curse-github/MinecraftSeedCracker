/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
/*     */ import net.minecraft.network.protocol.login.ServerLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundKeyPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.notifications.ServerActivityMonitor;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.CryptException;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ServerLoginPacketListenerImpl
/*     */   implements ServerLoginPacketListener, TickablePacketListener
/*     */ {
/*  52 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*  53 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TICKS_BEFORE_LOGIN = 600;
/*     */   
/*     */   private final byte[] challenge;
/*     */   
/*     */   public ServerLoginPacketListenerImpl(MinecraftServer minecraftserver, Connection connection, boolean transferred) {
/*  60 */     this.state = State.HELLO;
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.serverId = "";
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.server = minecraftserver;
/*  69 */     this.connection = connection;
/*  70 */     this.serverActivityMonitor = this.server.getServerActivityMonitor();
/*  71 */     this.challenge = Ints.toByteArray(RandomSource.create().nextInt());
/*  72 */     this.transferred = transferred;
/*     */   }
/*     */   private final MinecraftServer server; private final Connection connection; private final ServerActivityMonitor serverActivityMonitor;
/*     */   private int tick;
/*     */   private String requestedUsername;
/*     */   
/*     */   public void tick() {
/*  79 */     if (this.state == State.VERIFYING) {
/*  80 */       verifyLoginAndFinishConnectionSetup((GameProfile)Objects.requireNonNull(this.authenticatedProfile));
/*     */     }
/*     */     
/*  83 */     if (this.state == State.WAITING_FOR_DUPE_DISCONNECT && 
/*  84 */       !isPlayerAlreadyInWorld((GameProfile)Objects.requireNonNull(this.authenticatedProfile))) {
/*  85 */       finishLoginAndWaitForClient(this.authenticatedProfile);
/*     */     }
/*     */ 
/*     */     
/*  89 */     if (this.tick++ == 600)
/*  90 */       disconnect(Component.translatable("multiplayer.disconnect.slow_login")); 
/*     */   }
/*     */   private GameProfile authenticatedProfile;
/*     */   private final String serverId = "";
/*     */   private final boolean transferred;
/*     */   
/*  96 */   public boolean isAcceptingMessages() { return this.connection.isConnected(); }
/*     */ 
/*     */   
/*     */   public void disconnect(Component component) {
/*     */     try {
/* 101 */       LOGGER.info("Disconnecting {}: {}", getUserName(), component.getString());
/* 102 */       this.connection.send(new ClientboundLoginDisconnectPacket(component));
/* 103 */       this.connection.disconnect(component);
/* 104 */     } catch (Exception e) {
/* 105 */       LOGGER.error("Error whilst disconnecting player", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 110 */   private boolean isPlayerAlreadyInWorld(GameProfile gameProfile) { return (this.server.getPlayerList().getPlayer(gameProfile.id()) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void onDisconnect(DisconnectionDetails details) { LOGGER.info("{} lost connection: {}", getUserName(), details.reason().getString()); }
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 119 */     String loggableAddress = this.connection.getLoggableAddress(this.server.logIPs());
/* 120 */     if (this.requestedUsername != null) {
/* 121 */       return this.requestedUsername + " (" + this.requestedUsername + ")";
/*     */     }
/* 123 */     return loggableAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleHello(ServerboundHelloPacket packet) {
/* 128 */     Validate.validState((this.state == State.HELLO), "Unexpected hello packet", new Object[0]);
/* 129 */     Validate.validState(StringUtil.isValidPlayerName(packet.name()), "Invalid characters in username", new Object[0]);
/*     */     
/* 131 */     this.requestedUsername = packet.name();
/*     */     
/* 133 */     GameProfile singleplayerProfile = this.server.getSingleplayerProfile();
/* 134 */     if (singleplayerProfile != null && this.requestedUsername.equalsIgnoreCase(singleplayerProfile.name())) {
/* 135 */       startClientVerification(singleplayerProfile);
/*     */       
/*     */       return;
/*     */     } 
/* 139 */     if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
/* 140 */       this.state = State.KEY;
/* 141 */       this.connection.send(new ClientboundHelloPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.challenge, true));
/*     */     } else {
/* 143 */       startClientVerification(UUIDUtil.createOfflineProfile(this.requestedUsername));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startClientVerification(GameProfile profile) {
/* 148 */     this.authenticatedProfile = profile;
/* 149 */     this.state = State.VERIFYING;
/*     */   }
/*     */ 
/*     */   
/*     */   private void verifyLoginAndFinishConnectionSetup(GameProfile profile) {
/* 154 */     PlayerList playerList = this.server.getPlayerList();
/* 155 */     Component error = playerList.canPlayerLogin(this.connection.getRemoteAddress(), new NameAndId(profile));
/* 156 */     if (error != null) {
/* 157 */       disconnect(error);
/*     */     } else {
/* 159 */       if (this.server.getCompressionThreshold() >= 0 && !this.connection.isMemoryConnection()) {
/* 160 */         this.connection.send(new ClientboundLoginCompressionPacket(this.server.getCompressionThreshold()), PacketSendListener.thenRun(() -> this.connection.setupCompression(this.server.getCompressionThreshold(), true)));
/*     */       }
/*     */ 
/*     */       
/* 164 */       boolean waitForDisconnection = playerList.disconnectAllPlayersWithProfile(profile.id());
/*     */       
/* 166 */       if (waitForDisconnection) {
/* 167 */         this.state = State.WAITING_FOR_DUPE_DISCONNECT;
/*     */       } else {
/* 169 */         finishLoginAndWaitForClient(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishLoginAndWaitForClient(GameProfile gameProfile) {
/* 175 */     this.state = State.PROTOCOL_SWITCHING;
/*     */     
/* 177 */     this.connection.send(new ClientboundLoginFinishedPacket(gameProfile));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleKey(ServerboundKeyPacket packet) {
/*     */     final String digest;
/* 184 */     Validate.validState((this.state == State.KEY), "Unexpected key packet", new Object[0]);
/*     */ 
/*     */     
/*     */     try {
/* 188 */       PrivateKey serverPrivateKey = this.server.getKeyPair().getPrivate();
/*     */       
/* 190 */       if (!packet.isChallengeValid(this.challenge, serverPrivateKey)) {
/* 191 */         throw new IllegalStateException("Protocol error");
/*     */       }
/*     */       
/* 194 */       SecretKey secretKey = packet.getSecretKey(serverPrivateKey);
/* 195 */       Cipher decryptCipher = Crypt.getCipher(2, secretKey);
/* 196 */       Cipher encryptCipher = Crypt.getCipher(1, secretKey);
/* 197 */       digest = (new BigInteger(Crypt.digestData("", this.server.getKeyPair().getPublic(), secretKey))).toString(16);
/*     */       
/* 199 */       this.state = State.AUTHENTICATING;
/* 200 */       this.connection.setEncryptionKey(decryptCipher, encryptCipher);
/* 201 */     } catch (CryptException e) {
/* 202 */       throw new IllegalStateException("Protocol error", e);
/*     */     } 
/*     */     
/* 205 */     Thread thread = new Thread("User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet())
/*     */       {
/*     */         public void run() {
/* 208 */           String name = (String)Objects.requireNonNull(ServerLoginPacketListenerImpl.this.requestedUsername, "Player name not initialized");
/*     */           try {
/* 210 */             ProfileResult result = ServerLoginPacketListenerImpl.this.server.services().sessionService().hasJoinedServer(name, digest, getAddress());
/*     */             
/* 212 */             if (result != null) {
/* 213 */               GameProfile profile = result.profile();
/* 214 */               ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", profile.name(), profile.id());
/* 215 */               ServerLoginPacketListenerImpl.this.serverActivityMonitor.reportLoginActivity();
/* 216 */               ServerLoginPacketListenerImpl.this.startClientVerification(profile);
/* 217 */             } else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 218 */               ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
/* 219 */               ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */             } else {
/* 221 */               ServerLoginPacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.unverified_username"));
/* 222 */               ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", name);
/*     */             } 
/* 224 */           } catch (AuthenticationUnavailableException ignored) {
/* 225 */             if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
/* 226 */               ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
/* 227 */               ServerLoginPacketListenerImpl.this.startClientVerification(UUIDUtil.createOfflineProfile(name));
/*     */             } else {
/* 229 */               ServerLoginPacketListenerImpl.this.disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
/* 230 */               ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         private InetAddress getAddress() {
/* 236 */           SocketAddress remoteAddress = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
/* 237 */           return (ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && remoteAddress instanceof InetSocketAddress) ? ((InetSocketAddress)remoteAddress).getAddress() : null;
/*     */         }
/*     */       };
/* 240 */     thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 241 */     thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket packet) { disconnect(ServerCommonPacketListenerImpl.DISCONNECT_UNEXPECTED_QUERY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket packet) {
/* 253 */     Validate.validState((this.state == State.PROTOCOL_SWITCHING), "Unexpected login acknowledgement packet", new Object[0]);
/* 254 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.CLIENTBOUND);
/* 255 */     CommonListenerCookie cookie = CommonListenerCookie.createInitial((GameProfile)Objects.requireNonNull(this.authenticatedProfile), this.transferred);
/* 256 */     ServerConfigurationPacketListenerImpl configPacketListener = new ServerConfigurationPacketListenerImpl(this.server, this.connection, cookie);
/* 257 */     this.connection.setupInboundProtocol(ConfigurationProtocols.SERVERBOUND, configPacketListener);
/* 258 */     configPacketListener.startConfiguration();
/* 259 */     this.state = State.ACCEPTED;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 264 */   public void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) { connectionDetails.setDetail("Login phase", () -> this.state.toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   public void handleCookieResponse(ServerboundCookieResponsePacket packet) { disconnect(ServerCommonPacketListenerImpl.DISCONNECT_UNEXPECTED_QUERY); }
/*     */   
/*     */   private enum State
/*     */   {
/* 273 */     HELLO,
/* 274 */     KEY,
/* 275 */     AUTHENTICATING,
/* 276 */     NEGOTIATING,
/* 277 */     VERIFYING,
/* 278 */     WAITING_FOR_DUPE_DISCONNECT,
/* 279 */     PROTOCOL_SWITCHING,
/* 280 */     ACCEPTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerLoginPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */