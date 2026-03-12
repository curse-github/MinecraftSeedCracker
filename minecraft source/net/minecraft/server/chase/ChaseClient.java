/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringReader;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ 
/*     */ public class ChaseClient
/*     */ {
/*  40 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int RECONNECT_INTERVAL_SECONDS = 5;
/*     */   
/*     */   private final String serverHost;
/*     */   
/*     */   private final int serverPort;
/*     */   
/*     */   private final MinecraftServer server;
/*     */   private Socket socket;
/*     */   private Thread thread;
/*     */   
/*     */   public ChaseClient(String serverHost, int serverPort, MinecraftServer server) {
/*  53 */     this.serverHost = serverHost;
/*  54 */     this.serverPort = serverPort;
/*  55 */     this.server = server;
/*     */   }
/*     */   
/*     */   public void start() {
/*  59 */     if (this.thread != null && this.thread.isAlive()) {
/*  60 */       LOGGER.warn("Remote control client was asked to start, but it is already running. Will ignore.");
/*     */     }
/*  62 */     this.wantsToRun = true;
/*  63 */     this.thread = new Thread(this::run, "chase-client");
/*  64 */     this.thread.setDaemon(true);
/*  65 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  69 */     this.wantsToRun = false;
/*     */     
/*  71 */     IOUtils.closeQuietly(this.socket);
/*  72 */     this.socket = null;
/*  73 */     this.thread = null;
/*     */   }
/*     */   
/*     */   public void run() {
/*  77 */     String serverAddress = this.serverHost + ":" + this.serverHost;
/*  78 */     while (this.wantsToRun) {
/*     */       try {
/*  80 */         LOGGER.info("Connecting to remote control server {}", serverAddress);
/*  81 */         this.socket = new Socket(this.serverHost, this.serverPort);
/*  82 */         LOGGER.info("Connected to remote control server! Will continuously execute the command broadcasted by that server.");
/*     */         
/*  84 */         try { BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.US_ASCII)); 
/*  85 */           try { while (this.wantsToRun) {
/*  86 */               String message = input.readLine();
/*  87 */               if (message == null) {
/*     */                 
/*  89 */                 LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5));
/*     */                 break;
/*     */               } 
/*  92 */               handleMessage(message);
/*     */             } 
/*  94 */             input.close(); } catch (Throwable throwable) { try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException err)
/*  95 */         { LOGGER.warn("Lost connection to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5)); }
/*     */       
/*  97 */       } catch (IOException e) {
/*  98 */         LOGGER.warn("Failed to connect to remote control server {}. Will retry in {}s.", serverAddress, Integer.valueOf(5));
/*     */       } 
/*     */       
/* 101 */       if (this.wantsToRun) {
/*     */         try {
/* 103 */           Thread.sleep(5000L);
/* 104 */         } catch (InterruptedException interruptedException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleMessage(String message) {
/*     */     
/* 111 */     try { Scanner scanner = new Scanner(new StringReader(message)); 
/* 112 */       try { scanner.useLocale(Locale.ROOT);
/* 113 */         String head = scanner.next();
/* 114 */         if ("t".equals(head)) {
/* 115 */           handleTeleport(scanner);
/*     */         } else {
/* 117 */           LOGGER.warn("Unknown message type '{}'", head);
/*     */         } 
/* 119 */         scanner.close(); } catch (Throwable throwable) { try { scanner.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (NoSuchElementException e)
/*     */     
/* 121 */     { LOGGER.warn("Could not parse message '{}', ignoring", message); }
/*     */   
/*     */   }
/*     */   
/*     */   private void handleTeleport(Scanner scanner) {
/* 126 */     parseTarget(scanner).ifPresent(target -> 
/* 127 */         executeCommand(String.format(Locale.ROOT, "execute in %s run tp @s %.3f %.3f %.3f %.3f %.3f", new Object[] { target.level
/* 128 */               .identifier(), Double.valueOf(target.pos.x), Double.valueOf(target.pos.y), Double.valueOf(target.pos.z), Float.valueOf(target.rot.y), Float.valueOf(target.rot.x) })));
/*     */   }
/*     */   static final class TeleportTarget extends Record { private final ResourceKey<Level> level; private final Vec3 pos; private final Vec2 rot;
/* 131 */     TeleportTarget(ResourceKey<Level> level, Vec3 pos, Vec2 rot) { this.level = level; this.pos = pos; this.rot = rot; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #131	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #131	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #131	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/chase/ChaseClient$TeleportTarget;
/* 131 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<Level> level() { return this.level; } public Vec3 pos() { return this.pos; } public Vec2 rot() { return this.rot; } }
/*     */   
/*     */   private Optional<TeleportTarget> parseTarget(Scanner scanner) {
/* 134 */     ResourceKey<Level> levelType = (ResourceKey)ChaseCommand.DIMENSION_NAMES.get(scanner.next());
/* 135 */     if (levelType == null) {
/* 136 */       return Optional.empty();
/*     */     }
/*     */     
/* 139 */     float x = scanner.nextFloat();
/* 140 */     float y = scanner.nextFloat();
/* 141 */     float z = scanner.nextFloat();
/* 142 */     float yRot = scanner.nextFloat();
/* 143 */     float xRot = scanner.nextFloat();
/* 144 */     return Optional.of(new TeleportTarget(levelType, new Vec3(x, y, z), new Vec2(xRot, yRot)));
/*     */   }
/*     */   
/*     */   private void executeCommand(String command) {
/* 148 */     this.server.execute(() -> {
/* 149 */           List<ServerPlayer> players = this.server.getPlayerList().getPlayers();
/* 150 */           if (players.isEmpty()) {
/*     */             return;
/*     */           }
/* 153 */           ServerPlayer player = (ServerPlayer)players.get(0);
/* 154 */           ServerLevel level = this.server.overworld();
/* 155 */           CommandSourceStack commandSourceStack = new CommandSourceStack(player.commandSource(), Vec3.atLowerCornerOf(level.getRespawnData().pos()), Vec2.ZERO, level, LevelBasedPermissionSet.OWNER, "", CommonComponents.EMPTY, this.server, player);
/* 156 */           Commands commands = this.server.getCommands();
/* 157 */           commands.performPrefixedCommand(commandSourceStack, command);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\chase\ChaseClient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */