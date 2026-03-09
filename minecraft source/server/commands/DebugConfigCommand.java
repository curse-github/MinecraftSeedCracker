/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
/*     */ 
/*     */ 
/*     */ public class DebugConfigCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  34 */     dispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugconfig")
/*  36 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  37 */         .then(
/*  38 */           Commands.literal("config")
/*  39 */           .then(
/*  40 */             Commands.argument("target", EntityArgument.player())
/*  41 */             .executes(c -> config((CommandSourceStack)c.getSource(), EntityArgument.getPlayer(c, "target"))))))
/*     */ 
/*     */         
/*  44 */         .then(
/*  45 */           Commands.literal("unconfig")
/*  46 */           .then(
/*  47 */             Commands.argument("target", UuidArgument.uuid())
/*  48 */             .suggests((c, p) -> SharedSuggestionProvider.suggest(getUuidsInConfig(((CommandSourceStack)c.getSource()).getServer()), p))
/*  49 */             .executes(c -> unconfig((CommandSourceStack)c.getSource(), UuidArgument.getUuid(c, "target"))))))
/*     */ 
/*     */         
/*  52 */         .then(
/*  53 */           Commands.literal("dialog")
/*  54 */           .then(
/*  55 */             Commands.argument("target", UuidArgument.uuid())
/*  56 */             .suggests((c, p) -> SharedSuggestionProvider.suggest(getUuidsInConfig(((CommandSourceStack)c.getSource()).getServer()), p))
/*  57 */             .then(
/*  58 */               Commands.argument("dialog", ResourceOrIdArgument.dialog(context))
/*  59 */               .executes(c -> showDialog((CommandSourceStack)c.getSource(), UuidArgument.getUuid(c, "target"), ResourceOrIdArgument.getDialog(c, "dialog")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Iterable<String> getUuidsInConfig(MinecraftServer server) {
/*  67 */     Set<String> result = new HashSet<String>();
/*  68 */     for (Connection connection : server.getConnection().getConnections()) {
/*  69 */       PacketListener packetListener = connection.getPacketListener(); if (packetListener instanceof ServerConfigurationPacketListenerImpl) { ServerConfigurationPacketListenerImpl configListener = (ServerConfigurationPacketListenerImpl)packetListener;
/*  70 */         result.add(configListener.getOwner().id().toString()); }
/*     */     
/*     */     } 
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   private static int config(CommandSourceStack source, ServerPlayer target) {
/*  77 */     GameProfile gameProfile = target.getGameProfile();
/*  78 */     target.connection.switchToConfig();
/*  79 */     source.sendSuccess(() -> Component.literal("Switched player " + gameProfile.name() + "(" + String.valueOf(gameProfile.id()) + ") to config mode"), false);
/*  80 */     return 1;
/*     */   }
/*     */   
/*     */   private static ServerConfigurationPacketListenerImpl findConfigPlayer(MinecraftServer server, UUID target) {
/*  84 */     for (Connection connection : server.getConnection().getConnections()) {
/*  85 */       PacketListener packetListener = connection.getPacketListener(); if (packetListener instanceof ServerConfigurationPacketListenerImpl) { ServerConfigurationPacketListenerImpl configListener = (ServerConfigurationPacketListenerImpl)packetListener;
/*  86 */         if (configListener.getOwner().id().equals(target)) {
/*  87 */           return configListener;
/*     */         } }
/*     */     
/*     */     } 
/*     */     
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   private static int unconfig(CommandSourceStack source, UUID target) {
/*  96 */     ServerConfigurationPacketListenerImpl listener = findConfigPlayer(source.getServer(), target);
/*     */     
/*  98 */     if (listener != null) {
/*  99 */       listener.returnToWorld();
/* 100 */       return 1;
/*     */     } 
/* 102 */     source.sendFailure(Component.literal("Can't find player to unconfig"));
/* 103 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int showDialog(CommandSourceStack source, UUID target, Holder<Dialog> dialog) {
/* 108 */     ServerConfigurationPacketListenerImpl listener = findConfigPlayer(source.getServer(), target);
/*     */     
/* 110 */     if (listener != null) {
/* 111 */       listener.send(new ClientboundShowDialogPacket(dialog));
/* 112 */       return 1;
/*     */     } 
/* 114 */     source.sendFailure(Component.literal("Can't find player to talk to"));
/* 115 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugConfigCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */