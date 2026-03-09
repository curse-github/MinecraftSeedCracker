/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RconThread
/*     */   extends GenericThread
/*     */ {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerSocket socket;
/*     */   private final String rconPassword;
/*  22 */   private final List<RconClient> clients = Lists.newArrayList();
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   private RconThread(ServerInterface serverInterface, ServerSocket socket, String rconPassword) {
/*  26 */     super("RCON Listener");
/*  27 */     this.serverInterface = serverInterface;
/*  28 */     this.socket = socket;
/*  29 */     this.rconPassword = rconPassword;
/*     */   }
/*     */ 
/*     */   
/*  33 */   private void clearClients() { this.clients.removeIf(client -> !client.isRunning()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  39 */       while (this.running) {
/*     */         
/*     */         try {
/*  42 */           Socket client = this.socket.accept();
/*  43 */           RconClient rconClient = new RconClient(this.serverInterface, this.rconPassword, client);
/*  44 */           rconClient.start();
/*  45 */           this.clients.add(rconClient);
/*     */ 
/*     */           
/*  48 */           clearClients();
/*  49 */         } catch (SocketTimeoutException ignored) {
/*     */           
/*  51 */           clearClients();
/*  52 */         } catch (IOException e) {
/*  53 */           if (this.running) {
/*  54 */             LOGGER.info("IO exception: ", e);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  59 */       closeSocket(this.socket);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RconThread create(ServerInterface serverInterface) {
/*  64 */     DedicatedServerProperties settings = serverInterface.getProperties();
/*     */     
/*  66 */     String serverIp = serverInterface.getServerIp();
/*  67 */     if (serverIp.isEmpty()) {
/*  68 */       serverIp = "0.0.0.0";
/*     */     }
/*     */     
/*  71 */     int port = settings.rconPort;
/*  72 */     if (0 >= port || 65535 < port) {
/*  73 */       LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", Integer.valueOf(port));
/*  74 */       return null;
/*     */     } 
/*     */     
/*  77 */     String password = settings.rconPassword;
/*  78 */     if (password.isEmpty()) {
/*  79 */       LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
/*  80 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/*  84 */       ServerSocket socket = new ServerSocket(port, 0, InetAddress.getByName(serverIp));
/*  85 */       socket.setSoTimeout(500);
/*     */       
/*  87 */       RconThread result = new RconThread(serverInterface, socket, password);
/*  88 */       if (!result.start()) {
/*  89 */         return null;
/*     */       }
/*  91 */       LOGGER.info("RCON running on {}:{}", serverIp, Integer.valueOf(port));
/*  92 */       return result;
/*  93 */     } catch (IOException e) {
/*  94 */       LOGGER.warn("Unable to initialise RCON on {}:{}", new Object[] { serverIp, Integer.valueOf(port), e });
/*     */ 
/*     */       
/*  97 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 102 */     this.running = false;
/* 103 */     closeSocket(this.socket);
/* 104 */     super.stop();
/*     */     
/* 106 */     for (RconClient rconClient : this.clients) {
/* 107 */       if (rconClient.isRunning()) {
/* 108 */         rconClient.stop();
/*     */       }
/*     */     } 
/* 111 */     this.clients.clear();
/*     */   }
/*     */   
/*     */   private void closeSocket(ServerSocket socket) {
/* 115 */     LOGGER.debug("closeSocket: {}", socket);
/*     */     
/*     */     try {
/* 118 */       socket.close();
/* 119 */     } catch (IOException e) {
/* 120 */       LOGGER.warn("Failed to close socket", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\thread\RconThread.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */