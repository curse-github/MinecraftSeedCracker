/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class TriggerCommand {
/*  31 */   private static final SimpleCommandExceptionType ERROR_NOT_PRIMED = new SimpleCommandExceptionType(Component.translatable("commands.trigger.failed.unprimed"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType(Component.translatable("commands.trigger.failed.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  35 */     dispatcher.register(
/*  36 */         (LiteralArgumentBuilder)Commands.literal("trigger")
/*  37 */         .then((
/*  38 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", ObjectiveArgument.objective())
/*  39 */           .suggests((c, p) -> suggestObjectives((CommandSourceStack)c.getSource(), p))
/*  40 */           .executes(c -> simpleTrigger((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(c, "objective"))))
/*  41 */           .then(
/*  42 */             Commands.literal("add")
/*  43 */             .then(
/*  44 */               Commands.argument("value", IntegerArgumentType.integer())
/*  45 */               .executes(c -> addValue((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(c, "objective"), IntegerArgumentType.getInteger(c, "value"))))))
/*     */ 
/*     */           
/*  48 */           .then(
/*  49 */             Commands.literal("set")
/*  50 */             .then(
/*  51 */               Commands.argument("value", IntegerArgumentType.integer())
/*  52 */               .executes(c -> setValue((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(c, "objective"), IntegerArgumentType.getInteger(c, "value")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Suggestions> suggestObjectives(CommandSourceStack source, SuggestionsBuilder builder) {
/*  60 */     Entity entity1 = source.getEntity();
/*  61 */     List<String> result = Lists.newArrayList();
/*     */     
/*  63 */     if (entity1 != null) {
/*  64 */       ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */       
/*  66 */       for (Objective objective : serverScoreboard.getObjectives()) {
/*  67 */         if (objective.getCriteria() == ObjectiveCriteria.TRIGGER) {
/*  68 */           ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(entity1, objective);
/*  69 */           if (scoreInfo != null && !scoreInfo.isLocked()) {
/*  70 */             result.add(objective.getName());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  76 */     return SharedSuggestionProvider.suggest(result, builder);
/*     */   }
/*     */   
/*     */   private static int addValue(CommandSourceStack source, ServerPlayer player, Objective objective, int amount) throws CommandSyntaxException {
/*  80 */     ScoreAccess score = getScore(source.getServer().getScoreboard(), player, objective);
/*  81 */     int newValue = score.add(amount);
/*  82 */     source.sendSuccess(() -> Component.translatable("commands.trigger.add.success", new Object[] { objective.getFormattedDisplayName(), Integer.valueOf(amount) }), true);
/*  83 */     return newValue;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack source, ServerPlayer player, Objective objective, int amount) throws CommandSyntaxException {
/*  87 */     ScoreAccess score = getScore(source.getServer().getScoreboard(), player, objective);
/*  88 */     score.set(amount);
/*  89 */     source.sendSuccess(() -> Component.translatable("commands.trigger.set.success", new Object[] { objective.getFormattedDisplayName(), Integer.valueOf(amount) }), true);
/*  90 */     return amount;
/*     */   }
/*     */   
/*     */   private static int simpleTrigger(CommandSourceStack source, ServerPlayer player, Objective objective) throws CommandSyntaxException {
/*  94 */     ScoreAccess score = getScore(source.getServer().getScoreboard(), player, objective);
/*  95 */     int newValue = score.add(1);
/*  96 */     source.sendSuccess(() -> Component.translatable("commands.trigger.simple.success", new Object[] { objective.getFormattedDisplayName() }), true);
/*  97 */     return newValue;
/*     */   }
/*     */   
/*     */   private static ScoreAccess getScore(Scoreboard scoreboard, ScoreHolder scoreHolder, Objective objective) throws CommandSyntaxException {
/* 101 */     if (objective.getCriteria() != ObjectiveCriteria.TRIGGER) {
/* 102 */       throw ERROR_INVALID_OBJECTIVE.create();
/*     */     }
/*     */     
/* 105 */     ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo(scoreHolder, objective);
/*     */     
/* 107 */     if (scoreInfo == null || scoreInfo.isLocked()) {
/* 108 */       throw ERROR_NOT_PRIMED.create();
/*     */     }
/*     */     
/* 111 */     ScoreAccess score = scoreboard.getOrCreatePlayerScore(scoreHolder, objective);
/* 112 */     score.lock();
/* 113 */     return score;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TriggerCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */