/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ServerCommonPacketListenerImpl implements ServerCommonPacketListener {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static final int LATENCY_CHECK_INTERVAL = 15000;
/*     */   private static final int CLOSED_LISTENER_TIMEOUT = 15000;
/*  37 */   private static final Component TIMEOUT_DISCONNECTION_MESSAGE = Component.translatable("disconnect.timeout");
/*  38 */   static final Component DISCONNECT_UNEXPECTED_QUERY = Component.translatable("multiplayer.disconnect.unexpected_query_response");
/*     */   
/*     */   protected final MinecraftServer server;
/*     */   
/*     */   protected final Connection connection;
/*     */   
/*     */   private final boolean transferred;
/*     */   private long keepAliveTime;
/*     */   
/*     */   public ServerCommonPacketListenerImpl(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
/*  48 */     this.closed = false;
/*     */     
/*  50 */     this.suspendFlushingOnServerThread = false;
/*     */ 
/*     */     
/*  53 */     this.server = server;
/*  54 */     this.connection = connection;
/*  55 */     this.keepAliveTime = Util.getMillis();
/*  56 */     this.latency = cookie.latency();
/*  57 */     this.transferred = cookie.transferred();
/*     */   }
/*     */   private boolean keepAlivePending; private long keepAliveChallenge; private long closedListenerTime; private boolean closed; private int latency;
/*     */   private void close() {
/*  61 */     if (!this.closed) {
/*  62 */       this.closedListenerTime = Util.getMillis();
/*  63 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/*  69 */     if (isSingleplayerOwner()) {
/*  70 */       LOGGER.info("Stopping singleplayer server as player logged out");
/*  71 */       this.server.halt(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPacketError(Packet packet, Exception e) throws ReportedException {
/*  77 */     super.onPacketError(packet, e);
/*  78 */     this.server.reportPacketHandlingException(e, packet.type());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKeepAlive(ServerboundKeepAlivePacket packet) {
/*  83 */     if (this.keepAlivePending && packet.getId() == this.keepAliveChallenge) {
/*  84 */       int time = (int)(Util.getMillis() - this.keepAliveTime);
/*  85 */       this.latency = (this.latency * 3 + time) / 4;
/*  86 */       this.keepAlivePending = false;
/*     */     }
/*  88 */     else if (!isSingleplayerOwner()) {
/*  89 */       disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePong(ServerboundPongPacket serverboundPongPacket) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleCustomClickAction(ServerboundCustomClickActionPacket packet) {
/* 104 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
/* 105 */     this.server.handleCustomClickAction(packet.id(), packet.payload());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
/* 110 */     PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
/* 111 */     if (packet.action() == ServerboundResourcePackPacket.Action.DECLINED && this.server.isResourcePackRequired()) {
/* 112 */       LOGGER.info("Disconnecting {} due to resource pack {} rejection", playerProfile().name(), packet.id());
/* 113 */       disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void handleCookieResponse(ServerboundCookieResponsePacket packet) { disconnect(DISCONNECT_UNEXPECTED_QUERY); }
/*     */ 
/*     */   
/*     */   protected void keepConnectionAlive() {
/* 123 */     Profiler.get().push("keepAlive");
/* 124 */     long now = Util.getMillis();
/* 125 */     if (!isSingleplayerOwner() && now - this.keepAliveTime >= 15000L) {
/* 126 */       if (this.keepAlivePending) {
/* 127 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/* 128 */       } else if (checkIfClosed(now)) {
/* 129 */         this.keepAlivePending = true;
/* 130 */         this.keepAliveTime = now;
/* 131 */         this.keepAliveChallenge = now;
/* 132 */         send(new ClientboundKeepAlivePacket(this.keepAliveChallenge));
/*     */       } 
/*     */     }
/* 135 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   private boolean checkIfClosed(long now) {
/* 139 */     if (this.closed) {
/* 140 */       if (now - this.closedListenerTime >= 15000L) {
/* 141 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/*     */       }
/* 143 */       return false;
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 149 */   public void suspendFlushing() { this.suspendFlushingOnServerThread = true; }
/*     */ 
/*     */   
/*     */   public void resumeFlushing() {
/* 153 */     this.suspendFlushingOnServerThread = false;
/* 154 */     this.connection.flushChannel();
/*     */   }
/*     */ 
/*     */   
/* 158 */   public void send(Packet<?> packet) { send(packet, null); }
/*     */ 
/*     */   
/*     */   public void send(Packet<?> packet, ChannelFutureListener listener) {
/* 162 */     if (packet.isTerminal()) {
/* 163 */       close();
/*     */     }
/* 165 */     boolean flush = (!this.suspendFlushingOnServerThread || !this.server.isSameThread());
/*     */     try {
/* 167 */       this.connection.send(packet, listener, flush);
/* 168 */     } catch (Throwable t) {
/* 169 */       CrashReport report = CrashReport.forThrowable(t, "Sending packet");
/* 170 */       CrashReportCategory category = report.addCategory("Packet being sent");
/*     */       
/* 172 */       category.setDetail("Packet class", () -> packet.getClass().getCanonicalName());
/*     */       
/* 174 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 179 */   public void disconnect(Component reason) { disconnect(new DisconnectionDetails(reason)); }
/*     */ 
/*     */   
/*     */   public void disconnect(DisconnectionDetails details) {
/* 183 */     this.connection.send(new ClientboundDisconnectPacket(details.reason()), PacketSendListener.thenRun(() -> this.connection.disconnect(details)));
/* 184 */     this.connection.setReadOnly();
/* 185 */     Objects.requireNonNull(this.connection); this.server.executeBlocking(this.connection::handleDisconnection);
/*     */   }
/*     */ 
/*     */   
/* 189 */   protected boolean isSingleplayerOwner() { return this.server.isSingleplayerOwner(new NameAndId(playerProfile())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 196 */   public GameProfile getOwner() { return playerProfile(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public int latency() { return this.latency; }
/*     */ 
/*     */   
/*     */   protected CommonListenerCookie createCookie(ClientInformation clientInformation) {
/* 207 */     return new CommonListenerCookie(
/* 208 */         playerProfile(), this.latency, clientInformation, this.transferred);
/*     */   }
/*     */   
/*     */   protected abstract GameProfile playerProfile();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerCommonPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */