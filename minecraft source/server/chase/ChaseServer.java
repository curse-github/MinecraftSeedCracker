/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.ClosedByInterruptException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChaseServer
/*     */ {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger(); private final String serverBindAddress;
/*     */   private final int serverPort;
/*     */   private final PlayerList playerList;
/*     */   private final int broadcastIntervalMs;
/*     */   private ServerSocket serverSocket;
/*     */   private final CopyOnWriteArrayList<Socket> clientSockets;
/*     */   
/*     */   public ChaseServer(String serverBindAddress, int serverPort, PlayerList playerList, int broadcastIntervalMs) {
/*  39 */     this.clientSockets = new CopyOnWriteArrayList();
/*     */ 
/*     */     
/*  42 */     this.serverBindAddress = serverBindAddress;
/*  43 */     this.serverPort = serverPort;
/*  44 */     this.playerList = playerList;
/*  45 */     this.broadcastIntervalMs = broadcastIntervalMs;
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/*  49 */     if (this.serverSocket != null && !this.serverSocket.isClosed()) {
/*  50 */       LOGGER.warn("Remote control server was asked to start, but it is already running. Will ignore.");
/*     */       return;
/*     */     } 
/*  53 */     this.wantsToRun = true;
/*  54 */     this.serverSocket = new ServerSocket(this.serverPort, 50, InetAddress.getByName(this.serverBindAddress));
/*  55 */     Thread acceptor = new Thread(this::runAcceptor, "chase-server-acceptor");
/*  56 */     acceptor.setDaemon(true);
/*  57 */     acceptor.start();
/*     */     
/*  59 */     Thread sender = new Thread(this::runSender, "chase-server-sender");
/*  60 */     sender.setDaemon(true);
/*  61 */     sender.start();
/*     */   }
/*     */   
/*     */   private void runSender() throws IOException {
/*  65 */     PlayerPosition oldPlayerPosition = null;
/*  66 */     while (this.wantsToRun) {
/*  67 */       if (!this.clientSockets.isEmpty()) {
/*  68 */         PlayerPosition playerPosition = getPlayerPosition();
/*  69 */         if (playerPosition != null && !playerPosition.equals(oldPlayerPosition)) {
/*  70 */           oldPlayerPosition = playerPosition;
/*  71 */           byte[] messageBytes = playerPosition.format().getBytes(StandardCharsets.US_ASCII);
/*  72 */           for (Socket clientSocket : this.clientSockets) {
/*  73 */             if (clientSocket.isClosed()) {
/*     */               continue;
/*     */             }
/*  76 */             Util.ioPool().execute(() -> {
/*     */                   try {
/*  78 */                     OutputStream output = clientSocket.getOutputStream();
/*  79 */                     output.write(messageBytes);
/*  80 */                     output.flush();
/*  81 */                   } catch (IOException e) {
/*  82 */                     LOGGER.info("Remote control client socket got an IO exception and will be closed", e);
/*  83 */                     IOUtils.closeQuietly(clientSocket);
/*     */                   } 
/*     */                 });
/*     */           } 
/*     */         } 
/*  88 */         List<Socket> closed = (List)this.clientSockets.stream().filter(Socket::isClosed).collect(Collectors.toList());
/*  89 */         this.clientSockets.removeAll(closed);
/*     */       } 
/*  91 */       if (this.wantsToRun) {
/*     */         try {
/*  93 */           Thread.sleep(this.broadcastIntervalMs);
/*  94 */         } catch (InterruptedException interruptedException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() throws IOException {
/* 101 */     this.wantsToRun = false;
/*     */     
/* 103 */     IOUtils.closeQuietly(this.serverSocket);
/* 104 */     this.serverSocket = null;
/*     */   }
/*     */   
/*     */   private void runAcceptor() throws IOException {
/*     */     try {
/* 109 */       while (this.wantsToRun) {
/* 110 */         if (this.serverSocket != null) {
/* 111 */           LOGGER.info("Remote control server is listening for connections on port {}", Integer.valueOf(this.serverPort));
/* 112 */           Socket clientSocket = this.serverSocket.accept();
/* 113 */           LOGGER.info("Remote control server received client connection on port {}", Integer.valueOf(clientSocket.getPort()));
/* 114 */           this.clientSockets.add(clientSocket);
/*     */         } 
/*     */       } 
/* 117 */     } catch (ClosedByInterruptException e) {
/* 118 */       if (this.wantsToRun) {
/* 119 */         LOGGER.info("Remote control server closed by interrupt");
/*     */       }
/* 121 */     } catch (IOException e) {
/* 122 */       if (this.wantsToRun) {
/* 123 */         LOGGER.error("Remote control server closed because of an IO exception", e);
/*     */       }
/*     */     } finally {
/* 126 */       IOUtils.closeQuietly(this.serverSocket);
/*     */     } 
/* 128 */     LOGGER.info("Remote control server is now stopped");
/* 129 */     this.wantsToRun = false;
/*     */   }
/*     */   
/*     */   private PlayerPosition getPlayerPosition() {
/* 133 */     List<ServerPlayer> players = this.playerList.getPlayers();
/* 134 */     if (players.isEmpty()) {
/* 135 */       return null;
/*     */     }
/* 137 */     ServerPlayer player = (ServerPlayer)players.get(0);
/* 138 */     String dimensionName = (String)ChaseCommand.DIMENSION_NAMES.inverse().get(player.level().dimension());
/* 139 */     if (dimensionName == null) {
/* 140 */       return null;
/*     */     }
/* 142 */     return new PlayerPosition(dimensionName, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
/*     */   }
/*     */   private static final class PlayerPosition extends Record { private final String dimensionName; private final double x; private final double y;
/* 145 */     private PlayerPosition(String dimensionName, double x, double y, double z, float yRot, float xRot) { this.dimensionName = dimensionName; this.x = x; this.y = y; this.z = z; this.yRot = yRot; this.xRot = xRot; } private final double z; private final float yRot; private final float xRot; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/* 145 */       //   0	8	1	o	Ljava/lang/Object; } public String dimensionName() { return this.dimensionName; } public double x() { return this.x; } public double y() { return this.y; } public double z() { return this.z; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; }
/*     */     
/* 147 */     private String format() { return String.format(Locale.ROOT, "t %s %.2f %.2f %.2f %.2f %.2f\n", new Object[] { this.dimensionName, Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), Float.valueOf(this.yRot), Float.valueOf(this.xRot) }); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\chase\ChaseServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */