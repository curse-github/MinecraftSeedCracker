/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TitleCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  32 */     dispatcher.register(
/*  33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("title")
/*  34 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  35 */         .then((
/*  36 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/*  37 */           .then(
/*  38 */             Commands.literal("clear")
/*  39 */             .executes(c -> clearTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets")))))
/*     */           
/*  41 */           .then(
/*  42 */             Commands.literal("reset")
/*  43 */             .executes(c -> resetTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets")))))
/*     */           
/*  45 */           .then(
/*  46 */             Commands.literal("title")
/*  47 */             .then(
/*  48 */               Commands.argument("title", ComponentArgument.textComponent(context))
/*  49 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "title", net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket::new)))))
/*     */ 
/*     */           
/*  52 */           .then(
/*  53 */             Commands.literal("subtitle")
/*  54 */             .then(
/*  55 */               Commands.argument("title", ComponentArgument.textComponent(context))
/*  56 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "subtitle", net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket::new)))))
/*     */ 
/*     */           
/*  59 */           .then(
/*  60 */             Commands.literal("actionbar")
/*  61 */             .then(
/*  62 */               Commands.argument("title", ComponentArgument.textComponent(context))
/*  63 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "actionbar", net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket::new)))))
/*     */ 
/*     */           
/*  66 */           .then(
/*  67 */             Commands.literal("times")
/*  68 */             .then(
/*  69 */               Commands.argument("fadeIn", TimeArgument.time())
/*  70 */               .then(
/*  71 */                 Commands.argument("stay", TimeArgument.time())
/*  72 */                 .then(
/*  73 */                   Commands.argument("fadeOut", TimeArgument.time())
/*  74 */                   .executes(c -> setTimes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "fadeIn"), IntegerArgumentType.getInteger(c, "stay"), IntegerArgumentType.getInteger(c, "fadeOut")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int clearTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
/*  84 */     ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(false);
/*  85 */     for (ServerPlayer player : targets) {
/*  86 */       player.connection.send(packet);
/*     */     }
/*     */     
/*  89 */     if (targets.size() == 1) {
/*  90 */       source.sendSuccess(() -> Component.translatable("commands.title.cleared.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  92 */       source.sendSuccess(() -> Component.translatable("commands.title.cleared.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/*  95 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int resetTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
/*  99 */     ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(true);
/* 100 */     for (ServerPlayer player : targets) {
/* 101 */       player.connection.send(packet);
/*     */     }
/*     */     
/* 104 */     if (targets.size() == 1) {
/* 105 */       source.sendSuccess(() -> Component.translatable("commands.title.reset.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 107 */       source.sendSuccess(() -> Component.translatable("commands.title.reset.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/* 110 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int showTitle(CommandSourceStack source, Collection<ServerPlayer> targets, Component title, String type, Function<Component, Packet<?>> factory) throws CommandSyntaxException {
/* 114 */     for (ServerPlayer player : targets) {
/* 115 */       player.connection.send((Packet)factory.apply(ComponentUtils.updateForEntity(source, title, player, 0)));
/*     */     }
/*     */     
/* 118 */     if (targets.size() == 1) {
/* 119 */       source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 121 */       source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/* 124 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int setTimes(CommandSourceStack source, Collection<ServerPlayer> targets, int fadeIn, int stay, int fadeOut) {
/* 128 */     ClientboundSetTitlesAnimationPacket packet = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
/* 129 */     for (ServerPlayer player : targets) {
/* 130 */       player.connection.send(packet);
/*     */     }
/*     */     
/* 133 */     if (targets.size() == 1) {
/* 134 */       source.sendSuccess(() -> Component.translatable("commands.title.times.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 136 */       source.sendSuccess(() -> Component.translatable("commands.title.times.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/* 139 */     return targets.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TitleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */