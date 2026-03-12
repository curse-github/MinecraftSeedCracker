/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.rcon.PktUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RconClient
/*     */   extends GenericThread {
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final int SERVERDATA_AUTH = 3;
/*     */   private static final int SERVERDATA_EXECCOMMAND = 2;
/*     */   private static final int SERVERDATA_RESPONSE_VALUE = 0;
/*     */   private static final int SERVERDATA_AUTH_RESPONSE = 2;
/*     */   private static final int SERVERDATA_AUTH_FAILURE = -1;
/*     */   private boolean authed;
/*     */   private final Socket client;
/*  25 */   private final byte[] buf = new byte[1460];
/*     */   private final String rconPassword;
/*     */   private final ServerInterface serverInterface;
/*     */   
/*     */   RconClient(ServerInterface serverInterface, String rconPassword, Socket socket) {
/*  30 */     super("RCON Client " + String.valueOf(socket.getInetAddress()));
/*  31 */     this.serverInterface = serverInterface;
/*  32 */     this.client = socket;
/*     */     
/*     */     try {
/*  35 */       this.client.setSoTimeout(0);
/*  36 */     } catch (Exception ignored) {
/*  37 */       this.running = false;
/*     */     } 
/*     */     
/*  40 */     this.rconPassword = rconPassword;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     
/*  46 */     try { while (this.running) {
/*  47 */         String password; BufferedInputStream inputStream = new BufferedInputStream(this.client.getInputStream());
/*  48 */         int read = inputStream.read(this.buf, 0, 1460);
/*     */         
/*  50 */         if (10 > read) {
/*     */           return;
/*     */         }
/*     */         
/*  54 */         int offset = 0;
/*  55 */         int pktsize = PktUtils.intFromByteArray(this.buf, 0, read);
/*  56 */         if (pktsize != read - 4) {
/*     */           return;
/*     */         }
/*     */         
/*  60 */         offset += 4;
/*  61 */         int requestid = PktUtils.intFromByteArray(this.buf, offset, read);
/*  62 */         offset += 4;
/*     */         
/*  64 */         int cmd = PktUtils.intFromByteArray(this.buf, offset);
/*  65 */         offset += 4;
/*  66 */         switch (cmd) {
/*     */           case 3:
/*  68 */             password = PktUtils.stringFromByteArray(this.buf, offset, read);
/*  69 */             offset += password.length();
/*  70 */             if (!password.isEmpty() && password.equals(this.rconPassword)) {
/*  71 */               this.authed = true;
/*  72 */               send(requestid, 2, ""); continue;
/*     */             } 
/*  74 */             this.authed = false;
/*  75 */             sendAuthFailure();
/*     */             continue;
/*     */           
/*     */           case 2:
/*  79 */             if (this.authed) {
/*  80 */               String command = PktUtils.stringFromByteArray(this.buf, offset, read);
/*     */               try {
/*  82 */                 sendCmdResponse(requestid, this.serverInterface.runCommand(command)); continue;
/*  83 */               } catch (Exception e) {
/*  84 */                 sendCmdResponse(requestid, "Error executing: " + command + " (" + e.getMessage() + ")"); continue;
/*     */               } 
/*     */             } 
/*  87 */             sendAuthFailure();
/*     */             continue;
/*     */         } 
/*     */         
/*  91 */         sendCmdResponse(requestid, String.format(Locale.ROOT, "Unknown request %s", new Object[] { Integer.toHexString(cmd) }));
/*     */       }
/*     */        }
/*  94 */     catch (IOException iOException) {  }
/*  95 */     catch (Exception e)
/*  96 */     { LOGGER.error("Exception whilst parsing RCON input", e); }
/*     */     finally
/*  98 */     { closeSocket();
/*  99 */       LOGGER.info("Thread {} shutting down", this.name);
/* 100 */       this.running = false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void send(int requestid, int cmd, String str) throws IOException {
/* 107 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1248);
/* 108 */     DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
/* 109 */     byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
/* 110 */     dataOutputStream.writeInt(Integer.reverseBytes(bytes.length + 10));
/* 111 */     dataOutputStream.writeInt(Integer.reverseBytes(requestid));
/* 112 */     dataOutputStream.writeInt(Integer.reverseBytes(cmd));
/* 113 */     dataOutputStream.write(bytes);
/* 114 */     dataOutputStream.write(0);
/* 115 */     dataOutputStream.write(0);
/* 116 */     this.client.getOutputStream().write(outputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   
/* 120 */   private void sendAuthFailure() { send(-1, 2, ""); }
/*     */ 
/*     */   
/*     */   private void sendCmdResponse(int requestid, String response) throws IOException {
/* 124 */     int len = response.length();
/*     */     
/*     */     do {
/* 127 */       int dataLen = (4096 <= len) ? 4096 : len;
/* 128 */       send(requestid, 0, response.substring(0, dataLen));
/* 129 */       response = response.substring(dataLen);
/* 130 */       len = response.length();
/* 131 */     } while (0 != len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 139 */     this.running = false;
/* 140 */     closeSocket();
/* 141 */     super.stop();
/*     */   }
/*     */   
/*     */   private void closeSocket() {
/*     */     try {
/* 146 */       this.client.close();
/* 147 */     } catch (IOException e) {
/* 148 */       LOGGER.warn("Failed to close socket", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\rcon\thread\RconClient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */