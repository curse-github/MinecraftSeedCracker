/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.NetworkDataOutputStream;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class QueryThreadGs4
/*     */   extends GenericThread
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String GAME_TYPE = "SMP";
/*     */   private static final String GAME_ID = "MINECRAFT";
/*     */   private static final long CHALLENGE_CHECK_INTERVAL = 30000L;
/*     */   private static final long RESPONSE_CACHE_TIME = 5000L;
/*     */   private long lastChallengeCheck;
/*     */   private final int port;
/*     */   private final int serverPort;
/*     */   private final int maxPlayers;
/*     */   private final String serverName;
/*     */   private final String worldName;
/*     */   private DatagramSocket socket;
/*  39 */   private final byte[] buffer = new byte[1460];
/*     */   private String hostIp;
/*     */   private String serverIp;
/*     */   private final Map<SocketAddress, RequestChallenge> validChallenges;
/*     */   private final NetworkDataOutputStream rulesResponse;
/*     */   private long lastRulesResponse;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private QueryThreadGs4(ServerInterface serverInterface, int port) {
/*  48 */     super("Query Listener");
/*  49 */     this.serverInterface = serverInterface;
/*     */     
/*  51 */     this.port = port;
/*  52 */     this.serverIp = serverInterface.getServerIp();
/*  53 */     this.serverPort = serverInterface.getServerPort();
/*  54 */     this.serverName = serverInterface.getServerName();
/*  55 */     this.maxPlayers = serverInterface.getMaxPlayers();
/*  56 */     this.worldName = serverInterface.getLevelIdName();
/*     */ 
/*     */     
/*  59 */     this.lastRulesResponse = 0L;
/*     */     
/*  61 */     this.hostIp = "0.0.0.0";
/*     */ 
/*     */     
/*  64 */     if (this.serverIp.isEmpty() || this.hostIp.equals(this.serverIp)) {
/*     */       
/*  66 */       this.serverIp = "0.0.0.0";
/*     */       try {
/*  68 */         InetAddress addr = InetAddress.getLocalHost();
/*  69 */         this.hostIp = addr.getHostAddress();
/*  70 */       } catch (UnknownHostException e) {
/*  71 */         LOGGER.warn("Unable to determine local host IP, please set server-ip in server.properties", e);
/*     */       } 
/*     */     } else {
/*  74 */       this.hostIp = this.serverIp;
/*     */     } 
/*     */ 
/*     */     
/*  78 */     this.rulesResponse = new NetworkDataOutputStream(1460);
/*  79 */     this.validChallenges = Maps.newHashMap();
/*     */   }
/*     */   
/*     */   public static QueryThreadGs4 create(ServerInterface serverInterface) {
/*  83 */     int port = (serverInterface.getProperties()).queryPort;
/*  84 */     if (0 >= port || 65535 < port) {
/*  85 */       LOGGER.warn("Invalid query port {} found in server.properties (queries disabled)", Integer.valueOf(port));
/*  86 */       return null;
/*     */     } 
/*     */     
/*  89 */     QueryThreadGs4 result = new QueryThreadGs4(serverInterface, port);
/*  90 */     if (!result.start()) {
/*  91 */       return null;
/*     */     }
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  97 */   private void sendTo(byte[] data, DatagramPacket src) throws IOException { this.socket.send(new DatagramPacket(data, data.length, src.getSocketAddress())); }
/*     */   
/*     */   private boolean processPacket(DatagramPacket packet) throws IOException {
/*     */     NetworkDataOutputStream dos;
/* 101 */     byte[] buf = packet.getData();
/* 102 */     int len = packet.getLength();
/* 103 */     SocketAddress socketAddress = packet.getSocketAddress();
/* 104 */     LOGGER.debug("Packet len {} [{}]", Integer.valueOf(len), socketAddress);
/* 105 */     if (3 > len || -2 != buf[0] || -3 != buf[1]) {
/*     */       
/* 107 */       LOGGER.debug("Invalid packet [{}]", socketAddress);
/* 108 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     LOGGER.debug("Packet '{}' [{}]", PktUtils.toHexString(buf[2]), socketAddress);
/* 113 */     switch (buf[2]) {
/*     */       
/*     */       case 9:
/* 116 */         sendChallenge(packet);
/* 117 */         LOGGER.debug("Challenge [{}]", socketAddress);
/* 118 */         return true;
/*     */ 
/*     */       
/*     */       case 0:
/* 122 */         if (!validChallenge(packet).booleanValue()) {
/* 123 */           LOGGER.debug("Invalid challenge [{}]", socketAddress);
/* 124 */           return false;
/*     */         } 
/*     */         
/* 127 */         if (15 == len) {
/*     */           
/* 129 */           sendTo(buildRuleResponse(packet), packet);
/* 130 */           LOGGER.debug("Rules [{}]", socketAddress);
/*     */           break;
/*     */         } 
/* 133 */         dos = new NetworkDataOutputStream(1460);
/* 134 */         dos.write(0);
/* 135 */         dos.writeBytes(getIdentBytes(packet.getSocketAddress()));
/* 136 */         dos.writeString(this.serverName);
/* 137 */         dos.writeString("SMP");
/* 138 */         dos.writeString(this.worldName);
/* 139 */         dos.writeString(Integer.toString(this.serverInterface.getPlayerCount()));
/* 140 */         dos.writeString(Integer.toString(this.maxPlayers));
/* 141 */         dos.writeShort((short)this.serverPort);
/* 142 */         dos.writeString(this.hostIp);
/*     */         
/* 144 */         sendTo(dos.toByteArray(), packet);
/* 145 */         LOGGER.debug("Status [{}]", socketAddress);
/*     */         break;
/*     */     } 
/*     */     
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   private byte[] buildRuleResponse(DatagramPacket packet) throws IOException {
/* 153 */     long now = Util.getMillis();
/* 154 */     if (now < this.lastRulesResponse + 5000L) {
/*     */       
/* 156 */       byte[] data = this.rulesResponse.toByteArray();
/* 157 */       byte[] ident = getIdentBytes(packet.getSocketAddress());
/* 158 */       data[1] = ident[0];
/* 159 */       data[2] = ident[1];
/* 160 */       data[3] = ident[2];
/* 161 */       data[4] = ident[3];
/*     */       
/* 163 */       return data;
/*     */     } 
/*     */     
/* 166 */     this.lastRulesResponse = now;
/*     */     
/* 168 */     this.rulesResponse.reset();
/* 169 */     this.rulesResponse.write(0);
/* 170 */     this.rulesResponse.writeBytes(getIdentBytes(packet.getSocketAddress()));
/* 171 */     this.rulesResponse.writeString("splitnum");
/* 172 */     this.rulesResponse.write(128);
/* 173 */     this.rulesResponse.write(0);
/*     */ 
/*     */     
/* 176 */     this.rulesResponse.writeString("hostname");
/* 177 */     this.rulesResponse.writeString(this.serverName);
/* 178 */     this.rulesResponse.writeString("gametype");
/* 179 */     this.rulesResponse.writeString("SMP");
/* 180 */     this.rulesResponse.writeString("game_id");
/* 181 */     this.rulesResponse.writeString("MINECRAFT");
/* 182 */     this.rulesResponse.writeString("version");
/* 183 */     this.rulesResponse.writeString(this.serverInterface.getServerVersion());
/* 184 */     this.rulesResponse.writeString("plugins");
/* 185 */     this.rulesResponse.writeString(this.serverInterface.getPluginNames());
/* 186 */     this.rulesResponse.writeString("map");
/* 187 */     this.rulesResponse.writeString(this.worldName);
/* 188 */     this.rulesResponse.writeString("numplayers");
/* 189 */     this.rulesResponse.writeString("" + this.serverInterface.getPlayerCount());
/* 190 */     this.rulesResponse.writeString("maxplayers");
/* 191 */     this.rulesResponse.writeString("" + this.maxPlayers);
/* 192 */     this.rulesResponse.writeString("hostport");
/* 193 */     this.rulesResponse.writeString("" + this.serverPort);
/* 194 */     this.rulesResponse.writeString("hostip");
/* 195 */     this.rulesResponse.writeString(this.hostIp);
/* 196 */     this.rulesResponse.write(0);
/* 197 */     this.rulesResponse.write(1);
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.rulesResponse.writeString("player_");
/* 202 */     this.rulesResponse.write(0);
/*     */     
/* 204 */     String[] players = this.serverInterface.getPlayerNames();
/* 205 */     for (String player : players) {
/* 206 */       this.rulesResponse.writeString(player);
/*     */     }
/* 208 */     this.rulesResponse.write(0);
/*     */     
/* 210 */     return this.rulesResponse.toByteArray();
/*     */   }
/*     */ 
/*     */   
/* 214 */   private byte[] getIdentBytes(SocketAddress src) { return ((RequestChallenge)this.validChallenges.get(src)).getIdentBytes(); }
/*     */ 
/*     */   
/*     */   private Boolean validChallenge(DatagramPacket src) {
/* 218 */     SocketAddress sockAddr = src.getSocketAddress();
/* 219 */     if (!this.validChallenges.containsKey(sockAddr))
/*     */     {
/* 221 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 224 */     byte[] data = src.getData();
/* 225 */     return Boolean.valueOf((((RequestChallenge)this.validChallenges.get(sockAddr)).getChallenge() == PktUtils.intFromNetworkByteArray(data, 7, src.getLength())));
/*     */   }
/*     */   
/*     */   private void sendChallenge(DatagramPacket src) throws IOException {
/* 229 */     RequestChallenge challenge = new RequestChallenge(src);
/* 230 */     this.validChallenges.put(src.getSocketAddress(), challenge);
/*     */     
/* 232 */     sendTo(challenge.getChallengeBytes(), src);
/*     */   }
/*     */   
/*     */   private void pruneChallenges() {
/* 236 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     long now = Util.getMillis();
/* 241 */     if (now < this.lastChallengeCheck + 30000L) {
/*     */       return;
/*     */     }
/* 244 */     this.lastChallengeCheck = now;
/*     */     
/* 246 */     this.validChallenges.values().removeIf(challenge -> challenge.before(now).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 251 */     LOGGER.info("Query running on {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 252 */     this.lastChallengeCheck = Util.getMillis();
/* 253 */     DatagramPacket request = new DatagramPacket(this.buffer, this.buffer.length);
/*     */     
/*     */     try {
/* 256 */       while (this.running) {
/*     */         try {
/* 258 */           this.socket.receive(request);
/*     */ 
/*     */           
/* 261 */           pruneChallenges();
/*     */ 
/*     */           
/* 264 */           processPacket(request);
/* 265 */         } catch (SocketTimeoutException ignored) {
/*     */           
/* 267 */           pruneChallenges();
/* 268 */         } catch (PortUnreachableException portUnreachableException) {
/*     */         
/* 270 */         } catch (IOException e) {
/*     */           
/* 272 */           recoverSocketError(e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 276 */       LOGGER.debug("closeSocket: {}:{}", this.serverIp, Integer.valueOf(this.port));
/* 277 */       this.socket.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean start() {
/* 283 */     if (this.running) {
/* 284 */       return true;
/*     */     }
/*     */     
/* 287 */     if (!initSocket()) {
/* 288 */       return false;
/*     */     }
/*     */     
/* 291 */     return super.start();
/*     */   }
/*     */   
/*     */   private void recoverSocketError(Exception e) {
/* 295 */     if (!this.running) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 300 */     LOGGER.warn("Unexpected exception", e);
/*     */ 
/*     */     
/* 303 */     if (!initSocket()) {
/* 304 */       LOGGER.error("Failed to recover from exception, shutting down!");
/* 305 */       this.running = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean initSocket() {
/*     */     try {
/* 311 */       this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.serverIp));
/* 312 */       this.socket.setSoTimeout(500);
/* 313 */       return true;
/* 314 */     } catch (Exception e) {
/* 315 */       LOGGER.warn("Unable to initialise query system on {}:{}", new Object[] { this.serverIp, Integer.valueOf(this.port), e });
/*     */       
/* 317 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RequestChallenge
/*     */   {
/* 328 */     private final long time = (new Date()).getTime(); private final int challenge; private final byte[] identBytes; public RequestChallenge(DatagramPacket src) throws IOException {
/* 329 */       byte[] buf = src.getData();
/* 330 */       this.identBytes = new byte[4];
/* 331 */       this.identBytes[0] = buf[3];
/* 332 */       this.identBytes[1] = buf[4];
/* 333 */       this.identBytes[2] = buf[5];
/* 334 */       this.identBytes[3] = buf[6];
/* 335 */       this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
/* 336 */       this.challenge = RandomSource.create().nextInt(16777216);
/* 337 */       this.challengeBytes = String.format(Locale.ROOT, "\t%s%d\000", new Object[] { this.ident, Integer.valueOf(this.challenge) }).getBytes(StandardCharsets.UTF_8);
/*     */     }
/*     */     private final byte[] challengeBytes; private final String ident;
/*     */     
/* 341 */     public Boolean before(long time) { return Boolean.valueOf((this.time < time)); }
/*     */ 
/*     */ 
/*     */     
/* 345 */     public int getChallenge() { return this.challenge; }
/*     */ 
/*     */ 
/*     */     
/* 349 */     public byte[] getChallengeBytes() { return this.challengeBytes; }
/*     */ 
/*     */ 
/*     */     
/* 353 */     public byte[] getIdentBytes() { return this.identBytes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     public String getIdent() { return this.ident; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\thread\QueryThreadGs4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */