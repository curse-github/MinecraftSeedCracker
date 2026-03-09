/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExperienceCommand
/*     */ {
/*  29 */   private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType(Component.translatable("commands.experience.set.points.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  32 */     LiteralCommandNode<CommandSourceStack> command = dispatcher.register(
/*  33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("experience")
/*  34 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  35 */         .then(
/*  36 */           Commands.literal("add")
/*  37 */           .then(
/*  38 */             Commands.argument("target", EntityArgument.players())
/*  39 */             .then((
/*  40 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", IntegerArgumentType.integer())
/*  41 */               .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS)))
/*  42 */               .then(
/*  43 */                 Commands.literal("points")
/*  44 */                 .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS))))
/*     */               
/*  46 */               .then(
/*  47 */                 Commands.literal("levels")
/*  48 */                 .executes(c -> addExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  53 */         .then(
/*  54 */           Commands.literal("set")
/*  55 */           .then(
/*  56 */             Commands.argument("target", EntityArgument.players())
/*  57 */             .then((
/*  58 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", IntegerArgumentType.integer(0))
/*  59 */               .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS)))
/*  60 */               .then(
/*  61 */                 Commands.literal("points")
/*  62 */                 .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.POINTS))))
/*     */               
/*  64 */               .then(
/*  65 */                 Commands.literal("levels")
/*  66 */                 .executes(c -> setExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "target"), IntegerArgumentType.getInteger(c, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         .then(
/*  72 */           Commands.literal("query")
/*  73 */           .then((
/*  74 */             (RequiredArgumentBuilder)Commands.argument("target", EntityArgument.player())
/*  75 */             .then(
/*  76 */               Commands.literal("points")
/*  77 */               .executes(c -> queryExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayer(c, "target"), Type.POINTS))))
/*     */             
/*  79 */             .then(
/*  80 */               Commands.literal("levels")
/*  81 */               .executes(c -> queryExperience((CommandSourceStack)c.getSource(), EntityArgument.getPlayer(c, "target"), Type.LEVELS))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     dispatcher.register(
/*  88 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("xp")
/*  89 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  90 */         .redirect(command));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int queryExperience(CommandSourceStack source, ServerPlayer target, Type type) {
/*  95 */     int result = type.query.applyAsInt(target);
/*  96 */     source.sendSuccess(() -> Component.translatable("commands.experience.query." + type.name, new Object[] { target.getDisplayName(), Integer.valueOf(result) }), false);
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   private static int addExperience(CommandSourceStack source, Collection<? extends ServerPlayer> players, int amount, Type type) {
/* 101 */     for (ServerPlayer player : players) {
/* 102 */       type.add.accept(player, Integer.valueOf(amount));
/*     */     }
/*     */     
/* 105 */     if (players.size() == 1) {
/* 106 */       source.sendSuccess(() -> Component.translatable("commands.experience.add." + type.name + ".success.single", new Object[] { Integer.valueOf(amount), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 108 */       source.sendSuccess(() -> Component.translatable("commands.experience.add." + type.name + ".success.multiple", new Object[] { Integer.valueOf(amount), Integer.valueOf(players.size()) }), true);
/*     */     } 
/*     */     
/* 111 */     return players.size();
/*     */   }
/*     */   
/*     */   private static int setExperience(CommandSourceStack source, Collection<? extends ServerPlayer> players, int amount, Type type) {
/* 115 */     int success = 0;
/*     */     
/* 117 */     for (ServerPlayer player : players) {
/* 118 */       if (type.set.test(player, Integer.valueOf(amount))) {
/* 119 */         success++;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     if (success == 0) {
/* 124 */       throw ERROR_SET_POINTS_INVALID.create();
/*     */     }
/*     */     
/* 127 */     if (players.size() == 1) {
/* 128 */       source.sendSuccess(() -> Component.translatable("commands.experience.set." + type.name + ".success.single", new Object[] { Integer.valueOf(amount), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 130 */       source.sendSuccess(() -> Component.translatable("commands.experience.set." + type.name + ".success.multiple", new Object[] { Integer.valueOf(amount), Integer.valueOf(players.size()) }), true);
/*     */     } 
/*     */     
/* 133 */     return players.size();
/*     */   }
/*     */   
/*     */   private enum Type {
/* 137 */     POINTS("points", Player::giveExperiencePoints, (p, a) -> {
/* 138 */         if (a.intValue() >= p.getXpNeededForNextLevel()) {
/* 139 */           return false;
/*     */         }
/* 141 */         p.setExperiencePoints(a.intValue());
/* 142 */         return true;
/* 143 */       }p -> Mth.floor(p.experienceProgress * p.getXpNeededForNextLevel())),
/* 144 */     LEVELS("levels", ServerPlayer::giveExperienceLevels, (p, a) -> {
/* 145 */         p.setExperienceLevels(a.intValue());
/* 146 */         return true;
/* 147 */       }p -> p.experienceLevel);
/*     */     
/*     */     public final BiConsumer<ServerPlayer, Integer> add;
/*     */     public final BiPredicate<ServerPlayer, Integer> set;
/*     */     public final String name;
/*     */     private final ToIntFunction<ServerPlayer> query;
/*     */     
/*     */     Type(String name, BiConsumer<ServerPlayer, Integer> add, BiPredicate<ServerPlayer, Integer> set, ToIntFunction<ServerPlayer> query) {
/* 155 */       this.add = add;
/* 156 */       this.name = name;
/* 157 */       this.set = set;
/* 158 */       this.query = query;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ExperienceCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */