/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.GameProfileArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.players.UserWhiteList;
/*     */ import net.minecraft.server.players.UserWhiteListEntry;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class WhitelistCommand
/*     */ {
/*  24 */   private static final SimpleCommandExceptionType ERROR_ALREADY_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.alreadyOn"));
/*  25 */   private static final SimpleCommandExceptionType ERROR_ALREADY_DISABLED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.alreadyOff"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_ALREADY_WHITELISTED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.add.failed"));
/*  27 */   private static final SimpleCommandExceptionType ERROR_NOT_WHITELISTED = new SimpleCommandExceptionType(Component.translatable("commands.whitelist.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  30 */     dispatcher.register(
/*  31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("whitelist")
/*  32 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  33 */         .then(
/*  34 */           Commands.literal("on")
/*  35 */           .executes(c -> enableWhitelist((CommandSourceStack)c.getSource()))))
/*     */         
/*  37 */         .then(
/*  38 */           Commands.literal("off")
/*  39 */           .executes(c -> disableWhitelist((CommandSourceStack)c.getSource()))))
/*     */         
/*  41 */         .then(
/*  42 */           Commands.literal("list")
/*  43 */           .executes(c -> showList((CommandSourceStack)c.getSource()))))
/*     */         
/*  45 */         .then(
/*  46 */           Commands.literal("add")
/*  47 */           .then(
/*  48 */             Commands.argument("targets", GameProfileArgument.gameProfile())
/*  49 */             .suggests((c, p) -> {
/*  50 */                 PlayerList list = ((CommandSourceStack)c.getSource()).getServer().getPlayerList();
/*  51 */                 return SharedSuggestionProvider.suggest(list.getPlayers().stream().map(Player::nameAndId).filter(()).map(NameAndId::name), p);
/*     */               
/*  53 */               }).executes(c -> addPlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets"))))))
/*     */ 
/*     */         
/*  56 */         .then(
/*  57 */           Commands.literal("remove")
/*  58 */           .then(
/*  59 */             Commands.argument("targets", GameProfileArgument.gameProfile())
/*  60 */             .suggests((c, p) -> SharedSuggestionProvider.suggest(((CommandSourceStack)c.getSource()).getServer().getPlayerList().getWhiteListNames(), p))
/*  61 */             .executes(c -> removePlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets"))))))
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("reload")
/*  66 */           .executes(c -> reload((CommandSourceStack)c.getSource()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int reload(CommandSourceStack source) {
/*  72 */     source.getServer().getPlayerList().reloadWhiteList();
/*  73 */     source.sendSuccess(() -> Component.translatable("commands.whitelist.reloaded"), true);
/*  74 */     source.getServer().kickUnlistedPlayers();
/*  75 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addPlayers(CommandSourceStack source, Collection<NameAndId> targets) throws CommandSyntaxException {
/*  79 */     UserWhiteList list = source.getServer().getPlayerList().getWhiteList();
/*  80 */     int success = 0;
/*     */     
/*  82 */     for (NameAndId target : targets) {
/*  83 */       if (!list.isWhiteListed(target)) {
/*  84 */         UserWhiteListEntry entry = new UserWhiteListEntry(target);
/*  85 */         list.add(entry);
/*  86 */         source.sendSuccess(() -> Component.translatable("commands.whitelist.add.success", new Object[] { Component.literal(target.name()) }), true);
/*  87 */         success++;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     if (success == 0) {
/*  92 */       throw ERROR_ALREADY_WHITELISTED.create();
/*     */     }
/*     */     
/*  95 */     return success;
/*     */   }
/*     */   
/*     */   private static int removePlayers(CommandSourceStack source, Collection<NameAndId> targets) throws CommandSyntaxException {
/*  99 */     UserWhiteList list = source.getServer().getPlayerList().getWhiteList();
/* 100 */     int success = 0;
/*     */     
/* 102 */     for (NameAndId target : targets) {
/* 103 */       if (list.isWhiteListed(target)) {
/* 104 */         UserWhiteListEntry entry = new UserWhiteListEntry(target);
/* 105 */         list.remove(entry);
/* 106 */         source.sendSuccess(() -> Component.translatable("commands.whitelist.remove.success", new Object[] { Component.literal(target.name()) }), true);
/* 107 */         success++;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (success == 0) {
/* 112 */       throw ERROR_NOT_WHITELISTED.create();
/*     */     }
/*     */     
/* 115 */     source.getServer().kickUnlistedPlayers();
/* 116 */     return success;
/*     */   }
/*     */   
/*     */   private static int enableWhitelist(CommandSourceStack source) {
/* 120 */     if (source.getServer().isUsingWhitelist()) {
/* 121 */       throw ERROR_ALREADY_ENABLED.create();
/*     */     }
/* 123 */     source.getServer().setUsingWhitelist(true);
/* 124 */     source.sendSuccess(() -> Component.translatable("commands.whitelist.enabled"), true);
/* 125 */     source.getServer().kickUnlistedPlayers();
/* 126 */     return 1;
/*     */   }
/*     */   
/*     */   private static int disableWhitelist(CommandSourceStack source) {
/* 130 */     if (!source.getServer().isUsingWhitelist()) {
/* 131 */       throw ERROR_ALREADY_DISABLED.create();
/*     */     }
/* 133 */     source.getServer().setUsingWhitelist(false);
/* 134 */     source.sendSuccess(() -> Component.translatable("commands.whitelist.disabled"), true);
/* 135 */     return 1;
/*     */   }
/*     */   
/*     */   private static int showList(CommandSourceStack source) {
/* 139 */     String[] list = source.getServer().getPlayerList().getWhiteListNames();
/* 140 */     if (list.length == 0) {
/* 141 */       source.sendSuccess(() -> Component.translatable("commands.whitelist.none"), false);
/*     */     } else {
/* 143 */       source.sendSuccess(() -> Component.translatable("commands.whitelist.list", new Object[] { Integer.valueOf(list.length), String.join(", ", list) }), false);
/*     */     } 
/* 145 */     return list.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\WhitelistCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */