/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
/*     */ import net.minecraft.commands.arguments.OperationArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.ScoreboardSlotArgument;
/*     */ import net.minecraft.commands.arguments.StyleArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.numbers.BlankFormat;
/*     */ import net.minecraft.network.chat.numbers.FixedFormat;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.StyledFormat;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScoreboardCommand
/*     */ {
/*  68 */   private static final SimpleCommandExceptionType ERROR_OBJECTIVE_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.add.duplicate"));
/*  69 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_EMPTY = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.display.alreadyEmpty"));
/*  70 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_SET = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.objectives.display.alreadySet"));
/*  71 */   private static final SimpleCommandExceptionType ERROR_TRIGGER_ALREADY_ENABLED = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.players.enable.failed"));
/*  72 */   private static final SimpleCommandExceptionType ERROR_NOT_TRIGGER = new SimpleCommandExceptionType(Component.translatable("commands.scoreboard.players.enable.invalid"));
/*  73 */   private static final Dynamic2CommandExceptionType ERROR_NO_VALUE = new Dynamic2CommandExceptionType((objective, target) -> Component.translatableEscape("commands.scoreboard.players.get.null", new Object[] { objective, target }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  76 */     dispatcher.register(
/*  77 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("scoreboard")
/*  78 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  79 */         .then((
/*  80 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("objectives")
/*  81 */           .then(
/*  82 */             Commands.literal("list")
/*  83 */             .executes(c -> listObjectives((CommandSourceStack)c.getSource()))))
/*     */           
/*  85 */           .then(
/*  86 */             Commands.literal("add")
/*  87 */             .then(
/*  88 */               Commands.argument("objective", StringArgumentType.word())
/*  89 */               .then((
/*  90 */                 (RequiredArgumentBuilder)Commands.argument("criteria", ObjectiveCriteriaArgument.criteria())
/*  91 */                 .executes(c -> addObjective((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "objective"), ObjectiveCriteriaArgument.getCriteria(c, "criteria"), Component.literal(StringArgumentType.getString(c, "objective")))))
/*  92 */                 .then(
/*  93 */                   Commands.argument("displayName", ComponentArgument.textComponent(context))
/*  94 */                   .executes(c -> addObjective((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "objective"), ObjectiveCriteriaArgument.getCriteria(c, "criteria"), ComponentArgument.getResolvedComponent(c, "displayName"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  99 */           .then(
/* 100 */             Commands.literal("modify")
/* 101 */             .then((
/* 102 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", ObjectiveArgument.objective())
/* 103 */               .then(
/* 104 */                 Commands.literal("displayname")
/* 105 */                 .then(
/* 106 */                   Commands.argument("displayName", ComponentArgument.textComponent(context))
/* 107 */                   .executes(c -> setDisplayName((CommandSourceStack)c.getSource(), ObjectiveArgument.getObjective(c, "objective"), ComponentArgument.getResolvedComponent(c, "displayName"))))))
/*     */               
/* 109 */               .then(createRenderTypeModify()))
/* 110 */               .then(
/* 111 */                 Commands.literal("displayautoupdate")
/* 112 */                 .then(
/* 113 */                   Commands.argument("value", BoolArgumentType.bool())
/* 114 */                   .executes(c -> setDisplayAutoUpdate((CommandSourceStack)c.getSource(), ObjectiveArgument.getObjective(c, "objective"), BoolArgumentType.getBool(c, "value"))))))
/*     */ 
/*     */               
/* 117 */               .then(
/* 118 */                 addNumberFormats(context, Commands.literal("numberformat"), (c, numberFormat) -> setObjectiveFormat((CommandSourceStack)c.getSource(), ObjectiveArgument.getObjective(c, "objective"), numberFormat))))))
/*     */ 
/*     */ 
/*     */           
/* 122 */           .then(
/* 123 */             Commands.literal("remove")
/* 124 */             .then(
/* 125 */               Commands.argument("objective", ObjectiveArgument.objective())
/* 126 */               .executes(c -> removeObjective((CommandSourceStack)c.getSource(), ObjectiveArgument.getObjective(c, "objective"))))))
/*     */ 
/*     */           
/* 129 */           .then(
/* 130 */             Commands.literal("setdisplay")
/* 131 */             .then((
/* 132 */               (RequiredArgumentBuilder)Commands.argument("slot", ScoreboardSlotArgument.displaySlot())
/* 133 */               .executes(c -> clearDisplaySlot((CommandSourceStack)c.getSource(), ScoreboardSlotArgument.getDisplaySlot(c, "slot"))))
/* 134 */               .then(
/* 135 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 136 */                 .executes(c -> setDisplaySlot((CommandSourceStack)c.getSource(), ScoreboardSlotArgument.getDisplaySlot(c, "slot"), ObjectiveArgument.getObjective(c, "objective"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 141 */         .then((
/* 142 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("players")
/* 143 */           .then((
/* 144 */             (LiteralArgumentBuilder)Commands.literal("list")
/* 145 */             .executes(c -> listTrackedPlayers((CommandSourceStack)c.getSource())))
/* 146 */             .then(
/* 147 */               Commands.argument("target", ScoreHolderArgument.scoreHolder())
/* 148 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 149 */               .executes(c -> listTrackedPlayerScores((CommandSourceStack)c.getSource(), ScoreHolderArgument.getName(c, "target"))))))
/*     */ 
/*     */           
/* 152 */           .then(
/* 153 */             Commands.literal("set")
/* 154 */             .then(
/* 155 */               Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 156 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 157 */               .then(
/* 158 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 159 */                 .then(
/* 160 */                   Commands.argument("score", IntegerArgumentType.integer())
/* 161 */                   .executes(c -> setScore((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getWritableObjective(c, "objective"), IntegerArgumentType.getInteger(c, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 166 */           .then(
/* 167 */             Commands.literal("get")
/* 168 */             .then(
/* 169 */               Commands.argument("target", ScoreHolderArgument.scoreHolder())
/* 170 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 171 */               .then(
/* 172 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 173 */                 .executes(c -> getScore((CommandSourceStack)c.getSource(), ScoreHolderArgument.getName(c, "target"), ObjectiveArgument.getObjective(c, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 177 */           .then(
/* 178 */             Commands.literal("add")
/* 179 */             .then(
/* 180 */               Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 181 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 182 */               .then(
/* 183 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 184 */                 .then(
/* 185 */                   Commands.argument("score", IntegerArgumentType.integer(0))
/* 186 */                   .executes(c -> addScore((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getWritableObjective(c, "objective"), IntegerArgumentType.getInteger(c, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 191 */           .then(
/* 192 */             Commands.literal("remove")
/* 193 */             .then(
/* 194 */               Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 195 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 196 */               .then(
/* 197 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 198 */                 .then(
/* 199 */                   Commands.argument("score", IntegerArgumentType.integer(0))
/* 200 */                   .executes(c -> removeScore((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getWritableObjective(c, "objective"), IntegerArgumentType.getInteger(c, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 205 */           .then(
/* 206 */             Commands.literal("reset")
/* 207 */             .then((
/* 208 */               (RequiredArgumentBuilder)Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 209 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 210 */               .executes(c -> resetScores((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"))))
/* 211 */               .then(
/* 212 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 213 */                 .executes(c -> resetScore((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 217 */           .then(
/* 218 */             Commands.literal("enable")
/* 219 */             .then(
/* 220 */               Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 221 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 222 */               .then(
/* 223 */                 Commands.argument("objective", ObjectiveArgument.objective())
/* 224 */                 .suggests((c, p) -> suggestTriggers((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), p))
/* 225 */                 .executes(c -> enableTrigger((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 229 */           .then((
/* 230 */             (LiteralArgumentBuilder)Commands.literal("display")
/* 231 */             .then(
/* 232 */               Commands.literal("name")
/* 233 */               .then(
/* 234 */                 Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 235 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 236 */                 .then((
/* 237 */                   (RequiredArgumentBuilder)Commands.argument("objective", ObjectiveArgument.objective())
/* 238 */                   .then(
/* 239 */                     Commands.argument("name", ComponentArgument.textComponent(context))
/* 240 */                     .executes(c -> setScoreDisplay((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), ComponentArgument.getResolvedComponent(c, "name")))))
/*     */                   
/* 242 */                   .executes(c -> setScoreDisplay((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), null))))))
/*     */ 
/*     */ 
/*     */             
/* 246 */             .then(
/* 247 */               Commands.literal("numberformat")
/* 248 */               .then(
/* 249 */                 Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 250 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 251 */                 .then(
/* 252 */                   addNumberFormats(context, Commands.argument("objective", ObjectiveArgument.objective()), (c, format) -> 
/* 253 */                     setScoreNumberFormat((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), format)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 259 */           .then(
/* 260 */             Commands.literal("operation")
/* 261 */             .then(
/* 262 */               Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/* 263 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 264 */               .then(
/* 265 */                 Commands.argument("targetObjective", ObjectiveArgument.objective())
/* 266 */                 .then(
/* 267 */                   Commands.argument("operation", OperationArgument.operation())
/* 268 */                   .then(
/* 269 */                     Commands.argument("source", ScoreHolderArgument.scoreHolders())
/* 270 */                     .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 271 */                     .then(
/* 272 */                       Commands.argument("sourceObjective", ObjectiveArgument.objective())
/* 273 */                       .executes(c -> performOperation((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getWritableObjective(c, "targetObjective"), OperationArgument.getOperation(c, "operation"), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "source"), ObjectiveArgument.getObjective(c, "sourceObjective")))))))))));
/*     */   }
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
/*     */ 
/*     */   
/* 290 */   private static ArgumentBuilder<CommandSourceStack, ?> addNumberFormats(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> top, NumberFormatCommandExecutor callback) { return top
/* 291 */       .then(
/* 292 */         Commands.literal("blank")
/* 293 */         .executes(c -> callback.run(c, BlankFormat.INSTANCE)))
/*     */       
/* 295 */       .then(
/* 296 */         Commands.literal("fixed")
/* 297 */         .then(
/* 298 */           Commands.argument("contents", ComponentArgument.textComponent(context))
/* 299 */           .executes(c -> {
/* 300 */               Component contents = ComponentArgument.getResolvedComponent(c, "contents");
/* 301 */               return callback.run(c, new FixedFormat(contents));
/*     */ 
/*     */ 
/*     */             
/* 305 */             }))).then(
/* 306 */         Commands.literal("styled")
/* 307 */         .then(
/* 308 */           Commands.argument("style", StyleArgument.style(context))
/* 309 */           .executes(c -> {
/* 310 */               Style style = StyleArgument.getStyle(c, "style");
/* 311 */               return callback.run(c, new StyledFormat(style));
/*     */ 
/*     */ 
/*     */             
/* 315 */             }))).executes(c -> callback.run(c, null)); }
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> createRenderTypeModify() {
/* 319 */     result = Commands.literal("rendertype");
/*     */     
/* 321 */     for (ObjectiveCriteria.RenderType renderType : ObjectiveCriteria.RenderType.values()) {
/* 322 */       result.then(Commands.literal(renderType.getId())
/* 323 */           .executes(c -> setRenderType((CommandSourceStack)c.getSource(), ObjectiveArgument.getObjective(c, "objective"), renderType)));
/*     */     }
/*     */     
/* 326 */     return result;
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestTriggers(CommandSourceStack source, Collection<ScoreHolder> targets, SuggestionsBuilder builder) {
/* 330 */     List<String> result = Lists.newArrayList();
/* 331 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 333 */     for (Objective objective : serverScoreboard.getObjectives()) {
/* 334 */       if (objective.getCriteria() == ObjectiveCriteria.TRIGGER) {
/* 335 */         boolean available = false;
/* 336 */         for (ScoreHolder name : targets) {
/* 337 */           ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(name, objective);
/*     */           
/* 339 */           if (scoreInfo == null || scoreInfo.isLocked()) {
/* 340 */             available = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 344 */         if (available) {
/* 345 */           result.add(objective.getName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 350 */     return SharedSuggestionProvider.suggest(result, builder);
/*     */   }
/*     */   
/*     */   private static int getScore(CommandSourceStack source, ScoreHolder target, Objective objective) throws CommandSyntaxException {
/* 354 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 356 */     ReadOnlyScoreInfo score = serverScoreboard.getPlayerScoreInfo(target, objective);
/* 357 */     if (score == null) {
/* 358 */       throw ERROR_NO_VALUE.create(objective.getName(), target.getFeedbackDisplayName());
/*     */     }
/*     */     
/* 361 */     source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.get.success", new Object[] { target.getFeedbackDisplayName(), Integer.valueOf(score.value()), objective.getFormattedDisplayName() }), false);
/*     */     
/* 363 */     return score.value();
/*     */   }
/*     */ 
/*     */   
/* 367 */   private static Component getFirstTargetName(Collection<ScoreHolder> names) { return ((ScoreHolder)names.iterator().next()).getFeedbackDisplayName(); }
/*     */ 
/*     */   
/*     */   private static int performOperation(CommandSourceStack source, Collection<ScoreHolder> targets, Objective targetObjective, OperationArgument.Operation operation, Collection<ScoreHolder> sources, Objective sourceObjective) throws CommandSyntaxException {
/* 371 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 372 */     int result = 0;
/*     */     
/* 374 */     for (ScoreHolder target : targets) {
/* 375 */       ScoreAccess score = serverScoreboard.getOrCreatePlayerScore(target, targetObjective);
/* 376 */       for (ScoreHolder from : sources) {
/* 377 */         ScoreAccess sourceScore = serverScoreboard.getOrCreatePlayerScore(from, sourceObjective);
/* 378 */         operation.apply(score, sourceScore);
/*     */       } 
/* 380 */       result += score.get();
/*     */     } 
/*     */     
/* 383 */     if (targets.size() == 1) {
/* 384 */       int finalResult = result;
/* 385 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.single", new Object[] { targetObjective.getFormattedDisplayName(), getFirstTargetName(targets), Integer.valueOf(finalResult) }), true);
/*     */     } else {
/* 387 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.multiple", new Object[] { targetObjective.getFormattedDisplayName(), Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*     */     
/* 390 */     return result;
/*     */   }
/*     */   
/*     */   private static int enableTrigger(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective) throws CommandSyntaxException {
/* 394 */     if (objective.getCriteria() != ObjectiveCriteria.TRIGGER) {
/* 395 */       throw ERROR_NOT_TRIGGER.create();
/*     */     }
/* 397 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 399 */     int count = 0;
/*     */     
/* 401 */     for (ScoreHolder name : names) {
/* 402 */       ScoreAccess score = serverScoreboard.getOrCreatePlayerScore(name, objective);
/* 403 */       if (score.locked()) {
/* 404 */         score.unlock();
/* 405 */         count++;
/*     */       } 
/*     */     } 
/*     */     
/* 409 */     if (count == 0) {
/* 410 */       throw ERROR_TRIGGER_ALREADY_ENABLED.create();
/*     */     }
/*     */     
/* 413 */     if (names.size() == 1) {
/* 414 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.single", new Object[] { objective.getFormattedDisplayName(), getFirstTargetName(names) }), true);
/*     */     } else {
/* 416 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.multiple", new Object[] { objective.getFormattedDisplayName(), Integer.valueOf(names.size()) }), true);
/*     */     } 
/*     */     
/* 419 */     return count;
/*     */   }
/*     */   
/*     */   private static int resetScores(CommandSourceStack source, Collection<ScoreHolder> names) {
/* 423 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 425 */     for (ScoreHolder name : names) {
/* 426 */       serverScoreboard.resetAllPlayerScores(name);
/*     */     }
/*     */     
/* 429 */     if (names.size() == 1) {
/* 430 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.single", new Object[] { getFirstTargetName(names) }), true);
/*     */     } else {
/* 432 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.multiple", new Object[] { Integer.valueOf(names.size()) }), true);
/*     */     } 
/*     */     
/* 435 */     return names.size();
/*     */   }
/*     */   
/*     */   private static int resetScore(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective) throws CommandSyntaxException {
/* 439 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 441 */     for (ScoreHolder name : names) {
/* 442 */       serverScoreboard.resetSinglePlayerScore(name, objective);
/*     */     }
/*     */     
/* 445 */     if (names.size() == 1) {
/* 446 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.single", new Object[] { objective.getFormattedDisplayName(), getFirstTargetName(names) }), true);
/*     */     } else {
/* 448 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.multiple", new Object[] { objective.getFormattedDisplayName(), Integer.valueOf(names.size()) }), true);
/*     */     } 
/*     */     
/* 451 */     return names.size();
/*     */   }
/*     */   
/*     */   private static int setScore(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, int value) {
/* 455 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 457 */     for (ScoreHolder name : names) {
/* 458 */       serverScoreboard.getOrCreatePlayerScore(name, objective).set(value);
/*     */     }
/*     */     
/* 461 */     if (names.size() == 1) {
/* 462 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.single", new Object[] { objective.getFormattedDisplayName(), getFirstTargetName(names), Integer.valueOf(value) }), true);
/*     */     } else {
/* 464 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.multiple", new Object[] { objective.getFormattedDisplayName(), Integer.valueOf(names.size()), Integer.valueOf(value) }), true);
/*     */     } 
/*     */     
/* 467 */     return value * names.size();
/*     */   }
/*     */   
/*     */   private static int setScoreDisplay(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, Component display) {
/* 471 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 473 */     for (ScoreHolder name : names) {
/* 474 */       serverScoreboard.getOrCreatePlayerScore(name, objective).display(display);
/*     */     }
/*     */     
/* 477 */     if (display == null) {
/* 478 */       if (names.size() == 1) {
/* 479 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.clear.success.single", new Object[] { getFirstTargetName(names), objective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 481 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.clear.success.multiple", new Object[] { Integer.valueOf(names.size()), objective.getFormattedDisplayName() }), true);
/*     */       }
/*     */     
/* 484 */     } else if (names.size() == 1) {
/* 485 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.set.success.single", new Object[] { display, getFirstTargetName(names), objective.getFormattedDisplayName() }), true);
/*     */     } else {
/* 487 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.set.success.multiple", new Object[] { display, Integer.valueOf(names.size()), objective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */ 
/*     */     
/* 491 */     return names.size();
/*     */   }
/*     */   
/*     */   private static int setScoreNumberFormat(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, NumberFormat numberFormat) {
/* 495 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 497 */     for (ScoreHolder name : names) {
/* 498 */       serverScoreboard.getOrCreatePlayerScore(name, objective).numberFormatOverride(numberFormat);
/*     */     }
/*     */     
/* 501 */     if (numberFormat == null) {
/* 502 */       if (names.size() == 1) {
/* 503 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.clear.success.single", new Object[] { getFirstTargetName(names), objective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 505 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.clear.success.multiple", new Object[] { Integer.valueOf(names.size()), objective.getFormattedDisplayName() }), true);
/*     */       }
/*     */     
/* 508 */     } else if (names.size() == 1) {
/* 509 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.set.success.single", new Object[] { getFirstTargetName(names), objective.getFormattedDisplayName() }), true);
/*     */     } else {
/* 511 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.set.success.multiple", new Object[] { Integer.valueOf(names.size()), objective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */ 
/*     */     
/* 515 */     return names.size();
/*     */   }
/*     */   
/*     */   private static int addScore(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, int value) {
/* 519 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 520 */     int result = 0;
/*     */     
/* 522 */     for (ScoreHolder name : names) {
/* 523 */       ScoreAccess score = serverScoreboard.getOrCreatePlayerScore(name, objective);
/* 524 */       score.set(score.get() + value);
/* 525 */       result += score.get();
/*     */     } 
/*     */     
/* 528 */     if (names.size() == 1) {
/* 529 */       int finalResult = result;
/* 530 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.single", new Object[] { Integer.valueOf(value), objective.getFormattedDisplayName(), getFirstTargetName(names), Integer.valueOf(finalResult) }), true);
/*     */     } else {
/* 532 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.multiple", new Object[] { Integer.valueOf(value), objective.getFormattedDisplayName(), Integer.valueOf(names.size()) }), true);
/*     */     } 
/*     */     
/* 535 */     return result;
/*     */   }
/*     */   
/*     */   private static int removeScore(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, int value) {
/* 539 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 540 */     int result = 0;
/*     */     
/* 542 */     for (ScoreHolder name : names) {
/* 543 */       ScoreAccess score = serverScoreboard.getOrCreatePlayerScore(name, objective);
/* 544 */       score.set(score.get() - value);
/* 545 */       result += score.get();
/*     */     } 
/*     */     
/* 548 */     if (names.size() == 1) {
/* 549 */       int finalResult = result;
/* 550 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.single", new Object[] { Integer.valueOf(value), objective.getFormattedDisplayName(), getFirstTargetName(names), Integer.valueOf(finalResult) }), true);
/*     */     } else {
/* 552 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.multiple", new Object[] { Integer.valueOf(value), objective.getFormattedDisplayName(), Integer.valueOf(names.size()) }), true);
/*     */     } 
/*     */     
/* 555 */     return result;
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayers(CommandSourceStack source) {
/* 559 */     Collection<ScoreHolder> entities = source.getServer().getScoreboard().getTrackedPlayers();
/*     */     
/* 561 */     if (entities.isEmpty()) {
/* 562 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.empty"), false);
/*     */     } else {
/* 564 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.success", new Object[] { Integer.valueOf(entities.size()), ComponentUtils.formatList(entities, ScoreHolder::getFeedbackDisplayName) }), false);
/*     */     } 
/*     */     
/* 567 */     return entities.size();
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayerScores(CommandSourceStack source, ScoreHolder entity) {
/* 571 */     Object2IntMap<Objective> scores = source.getServer().getScoreboard().listPlayerScores(entity);
/*     */     
/* 573 */     if (scores.isEmpty()) {
/* 574 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.empty", new Object[] { entity.getFeedbackDisplayName() }), false);
/*     */     } else {
/* 576 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.success", new Object[] { entity.getFeedbackDisplayName(), Integer.valueOf(scores.size()) }), false);
/*     */       
/* 578 */       Object2IntMaps.fastForEach(scores, entry -> 
/* 579 */           source.sendSuccess((), false));
/*     */     } 
/*     */ 
/*     */     
/* 583 */     return scores.size();
/*     */   }
/*     */   
/*     */   private static int clearDisplaySlot(CommandSourceStack source, DisplaySlot slot) throws CommandSyntaxException {
/* 587 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 589 */     if (serverScoreboard.getDisplayObjective(slot) == null) {
/* 590 */       throw ERROR_DISPLAY_SLOT_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 593 */     serverScoreboard.setDisplayObjective(slot, null);
/* 594 */     source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.cleared", new Object[] { slot.getSerializedName() }), true);
/*     */     
/* 596 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplaySlot(CommandSourceStack source, DisplaySlot slot, Objective objective) throws CommandSyntaxException {
/* 600 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 602 */     if (serverScoreboard.getDisplayObjective(slot) == objective) {
/* 603 */       throw ERROR_DISPLAY_SLOT_ALREADY_SET.create();
/*     */     }
/*     */     
/* 606 */     serverScoreboard.setDisplayObjective(slot, objective);
/* 607 */     source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.set", new Object[] { slot.getSerializedName(), objective.getDisplayName() }), true);
/*     */     
/* 609 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack source, Objective objective, Component displayName) {
/* 613 */     if (!objective.getDisplayName().equals(displayName)) {
/* 614 */       objective.setDisplayName(displayName);
/* 615 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayname", new Object[] { objective.getName(), objective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 618 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayAutoUpdate(CommandSourceStack source, Objective objective, boolean displayAutoUpdate) {
/* 622 */     if (objective.displayAutoUpdate() != displayAutoUpdate) {
/* 623 */       objective.setDisplayAutoUpdate(displayAutoUpdate);
/* 624 */       if (displayAutoUpdate) {
/* 625 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayAutoUpdate.enable", new Object[] { objective.getName(), objective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 627 */         source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayAutoUpdate.disable", new Object[] { objective.getName(), objective.getFormattedDisplayName() }), true);
/*     */       } 
/*     */     } 
/*     */     
/* 631 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setObjectiveFormat(CommandSourceStack source, Objective objective, NumberFormat numberFormat) {
/* 635 */     objective.setNumberFormat(numberFormat);
/* 636 */     if (numberFormat != null) {
/* 637 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.objectiveFormat.set", new Object[] { objective.getName() }), true);
/*     */     } else {
/* 639 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.objectiveFormat.clear", new Object[] { objective.getName() }), true);
/*     */     } 
/* 641 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setRenderType(CommandSourceStack source, Objective objective, ObjectiveCriteria.RenderType renderType) {
/* 645 */     if (objective.getRenderType() != renderType) {
/* 646 */       objective.setRenderType(renderType);
/* 647 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.rendertype", new Object[] { objective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 650 */     return 0;
/*     */   }
/*     */   
/*     */   private static int removeObjective(CommandSourceStack source, Objective objective) {
/* 654 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/* 655 */     serverScoreboard.removeObjective(objective);
/* 656 */     source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.remove.success", new Object[] { objective.getFormattedDisplayName() }), true);
/* 657 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int addObjective(CommandSourceStack source, String name, ObjectiveCriteria criteria, Component displayName) throws CommandSyntaxException {
/* 661 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*     */     
/* 663 */     if (serverScoreboard.getObjective(name) != null) {
/* 664 */       throw ERROR_OBJECTIVE_ALREADY_EXISTS.create();
/*     */     }
/*     */     
/* 667 */     serverScoreboard.addObjective(name, criteria, displayName, criteria.getDefaultRenderType(), false, null);
/* 668 */     Objective objective = serverScoreboard.getObjective(name);
/*     */     
/* 670 */     source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.add.success", new Object[] { objective.getFormattedDisplayName() }), true);
/*     */     
/* 672 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int listObjectives(CommandSourceStack source) {
/* 676 */     Collection<Objective> objectives = source.getServer().getScoreboard().getObjectives();
/*     */     
/* 678 */     if (objectives.isEmpty()) {
/* 679 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.empty"), false);
/*     */     } else {
/* 681 */       source.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.success", new Object[] { Integer.valueOf(objectives.size()), ComponentUtils.formatList(objectives, Objective::getFormattedDisplayName) }), false);
/*     */     } 
/*     */     
/* 684 */     return objectives.size();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface NumberFormatCommandExecutor {
/*     */     int run(CommandContext<CommandSourceStack> param1CommandContext, NumberFormat param1NumberFormat) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ScoreboardCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */