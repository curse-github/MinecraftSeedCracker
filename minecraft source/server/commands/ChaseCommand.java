/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.chase.ChaseClient;
/*     */ import net.minecraft.server.chase.ChaseServer;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChaseCommand
/*     */ {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_CONNECT_HOST = "localhost";
/*     */   
/*     */   private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
/*     */   
/*     */   private static final int DEFAULT_PORT = 10000;
/*     */   
/*     */   private static final int BROADCAST_INTERVAL_MS = 100;
/*  46 */   public static BiMap<String, ResourceKey<Level>> DIMENSION_NAMES = ImmutableBiMap.of("o", Level.OVERWORLD, "n", Level.NETHER, "e", Level.END);
/*     */ 
/*     */   
/*     */   private static ChaseServer chaseServer;
/*     */ 
/*     */   
/*     */   private static ChaseClient chaseClient;
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  56 */     dispatcher.register(
/*  57 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("chase")
/*  58 */         .then((
/*  59 */           (LiteralArgumentBuilder)Commands.literal("follow")
/*  60 */           .then(((RequiredArgumentBuilder)Commands.argument("host", StringArgumentType.string())
/*  61 */             .executes(c -> follow((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "host"), 10000)))
/*  62 */             .then(Commands.argument("port", IntegerArgumentType.integer(1, 65535))
/*  63 */               .executes(c -> follow((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "host"), IntegerArgumentType.getInteger(c, "port"))))))
/*     */           
/*  65 */           .executes(c -> follow((CommandSourceStack)c.getSource(), "localhost", 10000))))
/*     */         
/*  67 */         .then((
/*  68 */           (LiteralArgumentBuilder)Commands.literal("lead")
/*  69 */           .then(((RequiredArgumentBuilder)Commands.argument("bind_address", StringArgumentType.string())
/*  70 */             .executes(c -> lead((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "bind_address"), 10000)))
/*  71 */             .then(Commands.argument("port", IntegerArgumentType.integer(1024, 65535))
/*  72 */               .executes(c -> lead((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "bind_address"), IntegerArgumentType.getInteger(c, "port"))))))
/*     */           
/*  74 */           .executes(c -> lead((CommandSourceStack)c.getSource(), "0.0.0.0", 10000))))
/*     */         
/*  76 */         .then(
/*  77 */           Commands.literal("stop")
/*  78 */           .executes(c -> stop((CommandSourceStack)c.getSource()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int stop(CommandSourceStack source) {
/*  84 */     if (chaseClient != null) {
/*  85 */       chaseClient.stop();
/*  86 */       source.sendSuccess(() -> Component.literal("You have now stopped chasing"), false);
/*  87 */       chaseClient = null;
/*     */     } 
/*  89 */     if (chaseServer != null) {
/*  90 */       chaseServer.stop();
/*  91 */       source.sendSuccess(() -> Component.literal("You are no longer being chased"), false);
/*  92 */       chaseServer = null;
/*     */     } 
/*  94 */     return 0;
/*     */   }
/*     */   
/*     */   private static boolean alreadyRunning(CommandSourceStack source) {
/*  98 */     if (chaseServer != null) {
/*  99 */       source.sendFailure(Component.literal("Chase server is already running. Stop it using /chase stop"));
/* 100 */       return true;
/*     */     } 
/* 102 */     if (chaseClient != null) {
/* 103 */       source.sendFailure(Component.literal("You are already chasing someone. Stop it using /chase stop"));
/* 104 */       return true;
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   private static int lead(CommandSourceStack source, String serverBindAddress, int port) {
/* 110 */     if (alreadyRunning(source)) {
/* 111 */       return 0;
/*     */     }
/*     */     
/* 114 */     chaseServer = new ChaseServer(serverBindAddress, port, source.getServer().getPlayerList(), 100);
/*     */     try {
/* 116 */       chaseServer.start();
/* 117 */       source.sendSuccess(() -> Component.literal("Chase server is now running on port " + port + ". Clients can follow you using /chase follow <ip> <port>"), false);
/* 118 */     } catch (IOException e) {
/* 119 */       LOGGER.error("Failed to start chase server", e);
/* 120 */       source.sendFailure(Component.literal("Failed to start chase server on port " + port));
/* 121 */       chaseServer = null;
/*     */     } 
/* 123 */     return 0;
/*     */   }
/*     */   
/*     */   private static int follow(CommandSourceStack source, String host, int port) {
/* 127 */     if (alreadyRunning(source)) {
/* 128 */       return 0;
/*     */     }
/*     */     
/* 131 */     chaseClient = new ChaseClient(host, port, source.getServer());
/* 132 */     chaseClient.start();
/* 133 */     source.sendSuccess(() -> Component.literal("You are now chasing " + host + ":" + port + ". If that server does '/chase lead' then you will automatically go to the same position. Use '/chase stop' to stop chasing."), false);
/* 134 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ChaseCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */