/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfos;
/*     */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.registries.VanillaRegistries;
/*     */ import net.minecraft.gametest.framework.TestCommand;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.commands.AdvancementCommands;
/*     */ import net.minecraft.server.commands.AttributeCommand;
/*     */ import net.minecraft.server.commands.BanIpCommands;
/*     */ import net.minecraft.server.commands.BanListCommands;
/*     */ import net.minecraft.server.commands.BanPlayerCommands;
/*     */ import net.minecraft.server.commands.BossBarCommands;
/*     */ import net.minecraft.server.commands.ChaseCommand;
/*     */ import net.minecraft.server.commands.ClearInventoryCommands;
/*     */ import net.minecraft.server.commands.CloneCommands;
/*     */ import net.minecraft.server.commands.DamageCommand;
/*     */ import net.minecraft.server.commands.DataPackCommand;
/*     */ import net.minecraft.server.commands.DeOpCommands;
/*     */ import net.minecraft.server.commands.DebugCommand;
/*     */ import net.minecraft.server.commands.DebugConfigCommand;
/*     */ import net.minecraft.server.commands.DebugMobSpawningCommand;
/*     */ import net.minecraft.server.commands.DebugPathCommand;
/*     */ import net.minecraft.server.commands.DefaultGameModeCommands;
/*     */ import net.minecraft.server.commands.DialogCommand;
/*     */ import net.minecraft.server.commands.DifficultyCommand;
/*     */ import net.minecraft.server.commands.EffectCommands;
/*     */ import net.minecraft.server.commands.EmoteCommands;
/*     */ import net.minecraft.server.commands.EnchantCommand;
/*     */ import net.minecraft.server.commands.ExecuteCommand;
/*     */ import net.minecraft.server.commands.ExperienceCommand;
/*     */ import net.minecraft.server.commands.FetchProfileCommand;
/*     */ import net.minecraft.server.commands.FillBiomeCommand;
/*     */ import net.minecraft.server.commands.FillCommand;
/*     */ import net.minecraft.server.commands.ForceLoadCommand;
/*     */ import net.minecraft.server.commands.FunctionCommand;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.server.commands.GameRuleCommand;
/*     */ import net.minecraft.server.commands.GiveCommand;
/*     */ import net.minecraft.server.commands.HelpCommand;
/*     */ import net.minecraft.server.commands.ItemCommands;
/*     */ import net.minecraft.server.commands.JfrCommand;
/*     */ import net.minecraft.server.commands.KickCommand;
/*     */ import net.minecraft.server.commands.KillCommand;
/*     */ import net.minecraft.server.commands.ListPlayersCommand;
/*     */ import net.minecraft.server.commands.LocateCommand;
/*     */ import net.minecraft.server.commands.LootCommand;
/*     */ import net.minecraft.server.commands.MsgCommand;
/*     */ import net.minecraft.server.commands.OpCommand;
/*     */ import net.minecraft.server.commands.PardonCommand;
/*     */ import net.minecraft.server.commands.PardonIpCommand;
/*     */ import net.minecraft.server.commands.ParticleCommand;
/*     */ import net.minecraft.server.commands.PerfCommand;
/*     */ import net.minecraft.server.commands.PlaceCommand;
/*     */ import net.minecraft.server.commands.PlaySoundCommand;
/*     */ import net.minecraft.server.commands.PublishCommand;
/*     */ import net.minecraft.server.commands.RaidCommand;
/*     */ import net.minecraft.server.commands.RandomCommand;
/*     */ import net.minecraft.server.commands.RecipeCommand;
/*     */ import net.minecraft.server.commands.ReloadCommand;
/*     */ import net.minecraft.server.commands.ReturnCommand;
/*     */ import net.minecraft.server.commands.RideCommand;
/*     */ import net.minecraft.server.commands.RotateCommand;
/*     */ import net.minecraft.server.commands.SaveAllCommand;
/*     */ import net.minecraft.server.commands.SaveOffCommand;
/*     */ import net.minecraft.server.commands.SaveOnCommand;
/*     */ import net.minecraft.server.commands.SayCommand;
/*     */ import net.minecraft.server.commands.ScheduleCommand;
/*     */ import net.minecraft.server.commands.ScoreboardCommand;
/*     */ import net.minecraft.server.commands.SeedCommand;
/*     */ import net.minecraft.server.commands.ServerPackCommand;
/*     */ import net.minecraft.server.commands.SetBlockCommand;
/*     */ import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
/*     */ import net.minecraft.server.commands.SetSpawnCommand;
/*     */ import net.minecraft.server.commands.SetWorldSpawnCommand;
/*     */ import net.minecraft.server.commands.SpawnArmorTrimsCommand;
/*     */ import net.minecraft.server.commands.SpectateCommand;
/*     */ import net.minecraft.server.commands.SpreadPlayersCommand;
/*     */ import net.minecraft.server.commands.StopCommand;
/*     */ import net.minecraft.server.commands.StopSoundCommand;
/*     */ import net.minecraft.server.commands.StopwatchCommand;
/*     */ import net.minecraft.server.commands.SummonCommand;
/*     */ import net.minecraft.server.commands.TagCommand;
/*     */ import net.minecraft.server.commands.TeamCommand;
/*     */ import net.minecraft.server.commands.TeamMsgCommand;
/*     */ import net.minecraft.server.commands.TeleportCommand;
/*     */ import net.minecraft.server.commands.TellRawCommand;
/*     */ import net.minecraft.server.commands.TickCommand;
/*     */ import net.minecraft.server.commands.TimeCommand;
/*     */ import net.minecraft.server.commands.TitleCommand;
/*     */ import net.minecraft.server.commands.TransferCommand;
/*     */ import net.minecraft.server.commands.TriggerCommand;
/*     */ import net.minecraft.server.commands.VersionCommand;
/*     */ import net.minecraft.server.commands.WardenSpawnTrackerCommand;
/*     */ import net.minecraft.server.commands.WaypointCommand;
/*     */ import net.minecraft.server.commands.WeatherCommand;
/*     */ import net.minecraft.server.commands.WhitelistCommand;
/*     */ import net.minecraft.server.commands.WorldBorderCommand;
/*     */ import net.minecraft.server.commands.data.DataCommands;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.PermissionCheck;
/*     */ import net.minecraft.server.permissions.PermissionProviderCheck;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Commands
/*     */ {
/*     */   public static final String COMMAND_PREFIX = "/";
/* 167 */   private static final ThreadLocal<ExecutionContext<CommandSourceStack>> CURRENT_EXECUTION_CONTEXT = new ThreadLocal();
/*     */   
/* 169 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/* 172 */   public static final PermissionCheck LEVEL_ALL = PermissionCheck.AlwaysPass.INSTANCE;
/*     */   
/* 174 */   public static final PermissionCheck LEVEL_MODERATORS = new PermissionCheck.Require(Permissions.COMMANDS_MODERATOR);
/*     */   
/* 176 */   public static final PermissionCheck LEVEL_GAMEMASTERS = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
/*     */   
/* 178 */   public static final PermissionCheck LEVEL_ADMINS = new PermissionCheck.Require(Permissions.COMMANDS_ADMIN);
/*     */   
/* 180 */   public static final PermissionCheck LEVEL_OWNERS = new PermissionCheck.Require(Permissions.COMMANDS_OWNER);
/*     */   
/* 182 */   private static final ClientboundCommandsPacket.NodeInspector<CommandSourceStack> COMMAND_NODE_INSPECTOR = new ClientboundCommandsPacket.NodeInspector<CommandSourceStack>() {
/* 183 */       private final CommandSourceStack noPermissionSource = Commands.createCompilationContext(PermissionSet.NO_PERMISSIONS);
/*     */ 
/*     */       
/*     */       public Identifier suggestionId(ArgumentCommandNode<CommandSourceStack, ?> node) {
/* 187 */         SuggestionProvider<CommandSourceStack> suggestionProvider = node.getCustomSuggestions();
/* 188 */         return (suggestionProvider != null) ? SuggestionProviders.getName(suggestionProvider) : null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 193 */       public boolean isExecutable(CommandNode<CommandSourceStack> node) { return (node.getCommand() != null); }
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean isRestricted(CommandNode<CommandSourceStack> node) {
/* 198 */         Predicate<CommandSourceStack> requirement = node.getRequirement();
/*     */ 
/*     */         
/* 201 */         return !requirement.test(this.noPermissionSource);
/*     */       }
/*     */     };
/*     */   private final CommandDispatcher<CommandSourceStack> dispatcher;
/*     */   
/*     */   public enum CommandSelection
/*     */   {
/* 208 */     ALL(true, true),
/* 209 */     DEDICATED(false, true),
/* 210 */     INTEGRATED(true, false);
/*     */     
/*     */     private final boolean includeIntegrated;
/*     */     
/*     */     private final boolean includeDedicated;
/*     */     
/*     */     CommandSelection(boolean includeIntegrated, boolean includeDedicated) {
/* 217 */       this.includeIntegrated = includeIntegrated;
/* 218 */       this.includeDedicated = includeDedicated;
/*     */     } }
/*     */   
/*     */   public Commands(CommandSelection commandSelection, CommandBuildContext context) {
/*     */     this.dispatcher = new CommandDispatcher();
/* 223 */     AdvancementCommands.register(this.dispatcher);
/* 224 */     AttributeCommand.register(this.dispatcher, context);
/* 225 */     ExecuteCommand.register(this.dispatcher, context);
/* 226 */     BossBarCommands.register(this.dispatcher, context);
/* 227 */     ClearInventoryCommands.register(this.dispatcher, context);
/* 228 */     CloneCommands.register(this.dispatcher, context);
/* 229 */     DamageCommand.register(this.dispatcher, context);
/* 230 */     DataCommands.register(this.dispatcher);
/* 231 */     DataPackCommand.register(this.dispatcher, context);
/* 232 */     DebugCommand.register(this.dispatcher);
/* 233 */     DefaultGameModeCommands.register(this.dispatcher);
/* 234 */     DialogCommand.register(this.dispatcher, context);
/* 235 */     DifficultyCommand.register(this.dispatcher);
/* 236 */     EffectCommands.register(this.dispatcher, context);
/* 237 */     EmoteCommands.register(this.dispatcher);
/* 238 */     EnchantCommand.register(this.dispatcher, context);
/* 239 */     ExperienceCommand.register(this.dispatcher);
/* 240 */     FillCommand.register(this.dispatcher, context);
/* 241 */     FillBiomeCommand.register(this.dispatcher, context);
/* 242 */     ForceLoadCommand.register(this.dispatcher);
/* 243 */     FunctionCommand.register(this.dispatcher);
/* 244 */     GameModeCommand.register(this.dispatcher);
/* 245 */     GameRuleCommand.register(this.dispatcher, context);
/* 246 */     GiveCommand.register(this.dispatcher, context);
/* 247 */     HelpCommand.register(this.dispatcher);
/* 248 */     ItemCommands.register(this.dispatcher, context);
/* 249 */     KickCommand.register(this.dispatcher);
/* 250 */     KillCommand.register(this.dispatcher);
/* 251 */     ListPlayersCommand.register(this.dispatcher);
/* 252 */     LocateCommand.register(this.dispatcher, context);
/* 253 */     LootCommand.register(this.dispatcher, context);
/* 254 */     MsgCommand.register(this.dispatcher);
/* 255 */     ParticleCommand.register(this.dispatcher, context);
/* 256 */     PlaceCommand.register(this.dispatcher);
/* 257 */     PlaySoundCommand.register(this.dispatcher);
/* 258 */     RandomCommand.register(this.dispatcher);
/* 259 */     ReloadCommand.register(this.dispatcher);
/* 260 */     RecipeCommand.register(this.dispatcher);
/* 261 */     FetchProfileCommand.register(this.dispatcher);
/* 262 */     ReturnCommand.register(this.dispatcher);
/* 263 */     RideCommand.register(this.dispatcher);
/* 264 */     RotateCommand.register(this.dispatcher);
/* 265 */     SayCommand.register(this.dispatcher);
/* 266 */     ScheduleCommand.register(this.dispatcher);
/* 267 */     ScoreboardCommand.register(this.dispatcher, context);
/* 268 */     SeedCommand.register(this.dispatcher, (commandSelection != CommandSelection.INTEGRATED));
/* 269 */     VersionCommand.register(this.dispatcher, (commandSelection != CommandSelection.INTEGRATED));
/* 270 */     SetBlockCommand.register(this.dispatcher, context);
/* 271 */     SetSpawnCommand.register(this.dispatcher);
/* 272 */     SetWorldSpawnCommand.register(this.dispatcher);
/* 273 */     SpectateCommand.register(this.dispatcher);
/* 274 */     SpreadPlayersCommand.register(this.dispatcher);
/* 275 */     StopSoundCommand.register(this.dispatcher);
/* 276 */     StopwatchCommand.register(this.dispatcher);
/* 277 */     SummonCommand.register(this.dispatcher, context);
/* 278 */     TagCommand.register(this.dispatcher);
/* 279 */     TeamCommand.register(this.dispatcher, context);
/* 280 */     TeamMsgCommand.register(this.dispatcher);
/* 281 */     TeleportCommand.register(this.dispatcher);
/* 282 */     TellRawCommand.register(this.dispatcher, context);
/* 283 */     TestCommand.register(this.dispatcher, context);
/* 284 */     TickCommand.register(this.dispatcher);
/* 285 */     TimeCommand.register(this.dispatcher);
/* 286 */     TitleCommand.register(this.dispatcher, context);
/* 287 */     TriggerCommand.register(this.dispatcher);
/* 288 */     WaypointCommand.register(this.dispatcher, context);
/* 289 */     WeatherCommand.register(this.dispatcher);
/* 290 */     WorldBorderCommand.register(this.dispatcher);
/*     */ 
/*     */     
/* 293 */     if (JvmProfiler.INSTANCE.isAvailable()) {
/* 294 */       JfrCommand.register(this.dispatcher);
/*     */     }
/*     */     
/* 297 */     if (SharedConstants.DEBUG_CHASE_COMMAND)
/*     */     {
/* 299 */       ChaseCommand.register(this.dispatcher);
/*     */     }
/*     */     
/* 302 */     if (SharedConstants.DEBUG_DEV_COMMANDS || SharedConstants.IS_RUNNING_IN_IDE) {
/*     */       
/* 304 */       RaidCommand.register(this.dispatcher, context);
/* 305 */       DebugPathCommand.register(this.dispatcher);
/* 306 */       DebugMobSpawningCommand.register(this.dispatcher);
/* 307 */       WardenSpawnTrackerCommand.register(this.dispatcher);
/* 308 */       SpawnArmorTrimsCommand.register(this.dispatcher);
/* 309 */       ServerPackCommand.register(this.dispatcher);
/* 310 */       if (commandSelection.includeDedicated) {
/* 311 */         DebugConfigCommand.register(this.dispatcher, context);
/*     */       }
/*     */     } 
/*     */     
/* 315 */     if (commandSelection.includeDedicated) {
/* 316 */       BanIpCommands.register(this.dispatcher);
/* 317 */       BanListCommands.register(this.dispatcher);
/* 318 */       BanPlayerCommands.register(this.dispatcher);
/* 319 */       DeOpCommands.register(this.dispatcher);
/* 320 */       OpCommand.register(this.dispatcher);
/* 321 */       PardonCommand.register(this.dispatcher);
/* 322 */       PardonIpCommand.register(this.dispatcher);
/* 323 */       PerfCommand.register(this.dispatcher);
/* 324 */       SaveAllCommand.register(this.dispatcher);
/* 325 */       SaveOffCommand.register(this.dispatcher);
/* 326 */       SaveOnCommand.register(this.dispatcher);
/* 327 */       SetPlayerIdleTimeoutCommand.register(this.dispatcher);
/* 328 */       StopCommand.register(this.dispatcher);
/* 329 */       TransferCommand.register(this.dispatcher);
/* 330 */       WhitelistCommand.register(this.dispatcher);
/*     */     } 
/*     */     
/* 333 */     if (commandSelection.includeIntegrated) {
/* 334 */       PublishCommand.register(this.dispatcher);
/*     */     }
/*     */     
/* 337 */     this.dispatcher.setConsumer(ExecutionCommandSource.resultConsumer());
/*     */   }
/*     */   
/*     */   public static <S> ParseResults<S> mapSource(ParseResults<S> parse, UnaryOperator<S> sourceOperator) {
/* 341 */     CommandContextBuilder<S> context = parse.getContext();
/* 342 */     CommandContextBuilder<S> source = context.withSource(sourceOperator.apply(context.getSource()));
/* 343 */     return new ParseResults(source, parse.getReader(), parse.getExceptions());
/*     */   }
/*     */   
/*     */   public void performPrefixedCommand(CommandSourceStack sender, String command) {
/* 347 */     command = trimOptionalPrefix(command);
/* 348 */     performCommand(this.dispatcher.parse(command, sender), command);
/*     */   }
/*     */ 
/*     */   
/* 352 */   public static String trimOptionalPrefix(String command) { return command.startsWith("/") ? command.substring(1) : command; }
/*     */ 
/*     */   
/*     */   public void performCommand(ParseResults<CommandSourceStack> command, String commandString) {
/* 356 */     CommandSourceStack sender = (CommandSourceStack)command.getContext().getSource();
/* 357 */     Profiler.get().push(() -> "/" + commandString);
/* 358 */     ContextChain<CommandSourceStack> commandChain = finishParsing(command, commandString, sender);
/*     */     
/*     */     try {
/* 361 */       if (commandChain != null) {
/* 362 */         executeCommandInContext(sender, executionContext -> ExecutionContext.queueInitialCommandExecution(executionContext, commandString, commandChain, sender, CommandResultCallback.EMPTY));
/*     */       }
/* 364 */     } catch (Exception e) {
/* 365 */       MutableComponent hover = Component.literal((e.getMessage() == null) ? e.getClass().getName() : e.getMessage());
/* 366 */       if (LOGGER.isDebugEnabled()) {
/* 367 */         LOGGER.error("Command exception: /{}", commandString, e);
/* 368 */         StackTraceElement[] stackTrace = e.getStackTrace();
/* 369 */         for (int i = 0; i < Math.min(stackTrace.length, 3); i++) {
/* 370 */           hover.append("\n\n")
/* 371 */             .append(stackTrace[i].getMethodName())
/* 372 */             .append("\n ")
/* 373 */             .append(stackTrace[i].getFileName())
/* 374 */             .append(":")
/* 375 */             .append(String.valueOf(stackTrace[i].getLineNumber()));
/*     */         }
/*     */       } 
/* 378 */       sender.sendFailure(Component.translatable("command.failed").withStyle(s -> s.withHoverEvent(new HoverEvent.ShowText(hover))));
/* 379 */       if (SharedConstants.DEBUG_VERBOSE_COMMAND_ERRORS || SharedConstants.IS_RUNNING_IN_IDE) {
/* 380 */         sender.sendFailure(Component.literal(Util.describeError(e)));
/* 381 */         LOGGER.error("'/{}' threw an exception", commandString, e);
/*     */       } 
/*     */     } finally {
/* 384 */       Profiler.get().pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static ContextChain<CommandSourceStack> finishParsing(ParseResults<CommandSourceStack> command, String commandString, CommandSourceStack sender) {
/*     */     try {
/* 391 */       validateParseResults(command);
/*     */       
/* 393 */       return (ContextChain)ContextChain.tryFlatten(command.getContext().build(commandString))
/* 394 */         .orElseThrow(() -> CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(command.getReader()));
/* 395 */     } catch (CommandSyntaxException e) {
/* 396 */       sender.sendFailure(ComponentUtils.fromMessage(e.getRawMessage()));
/*     */       
/* 398 */       if (e.getInput() != null && e.getCursor() >= 0) {
/* 399 */         int cursor = Math.min(e.getInput().length(), e.getCursor());
/* 400 */         MutableComponent context = Component.empty().withStyle(ChatFormatting.GRAY).withStyle(s -> 
/* 401 */             s.withClickEvent(new ClickEvent.SuggestCommand("/" + commandString)));
/*     */         
/* 403 */         if (cursor > 10) {
/* 404 */           context.append(CommonComponents.ELLIPSIS);
/*     */         }
/* 406 */         context.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
/* 407 */         if (cursor < e.getInput().length()) {
/* 408 */           MutableComponent mutableComponent = Component.literal(e.getInput().substring(cursor)).withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.UNDERLINE });
/* 409 */           context.append(mutableComponent);
/*     */         } 
/* 411 */         context.append(Component.translatable("command.context.here").withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }));
/* 412 */         sender.sendFailure(context);
/*     */       } 
/* 414 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void executeCommandInContext(CommandSourceStack context, Consumer<ExecutionContext<CommandSourceStack>> config) {
/* 419 */     ExecutionContext<CommandSourceStack> currentContext = (ExecutionContext)CURRENT_EXECUTION_CONTEXT.get();
/* 420 */     boolean isTopContext = (currentContext == null);
/*     */     
/* 422 */     if (isTopContext) {
/*     */       
/* 424 */       GameRules gameRules = context.getLevel().getGameRules();
/* 425 */       int chainLimit = Math.max(1, ((Integer)gameRules.get(GameRules.MAX_COMMAND_SEQUENCE_LENGTH)).intValue());
/* 426 */       int forkLimit = ((Integer)gameRules.get(GameRules.MAX_COMMAND_FORKS)).intValue(); 
/* 427 */       try { ExecutionContext<CommandSourceStack> executionContext = new ExecutionContext<CommandSourceStack>(chainLimit, forkLimit, Profiler.get()); 
/* 428 */         try { CURRENT_EXECUTION_CONTEXT.set(executionContext);
/* 429 */           config.accept(executionContext);
/* 430 */           executionContext.runCommandQueue();
/* 431 */           executionContext.close(); } catch (Throwable throwable) { try { executionContext.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  }
/* 432 */       finally { CURRENT_EXECUTION_CONTEXT.set(null); }
/*     */     
/*     */     } else {
/* 435 */       config.accept(currentContext);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendCommands(ServerPlayer player) {
/* 440 */     Map<CommandNode<CommandSourceStack>, CommandNode<CommandSourceStack>> playerCommands = new HashMap<CommandNode<CommandSourceStack>, CommandNode<CommandSourceStack>>();
/* 441 */     RootCommandNode<CommandSourceStack> root = new RootCommandNode<CommandSourceStack>();
/* 442 */     playerCommands.put(this.dispatcher.getRoot(), root);
/* 443 */     fillUsableCommands(this.dispatcher.getRoot(), root, player.createCommandSourceStack(), playerCommands);
/* 444 */     player.connection.send(new ClientboundCommandsPacket(root, COMMAND_NODE_INSPECTOR));
/*     */   }
/*     */   
/*     */   private static <S> void fillUsableCommands(CommandNode<S> source, CommandNode<S> target, S commandFilter, Map<CommandNode<S>, CommandNode<S>> converted) {
/* 448 */     for (CommandNode<S> child : source.getChildren()) {
/* 449 */       if (child.canUse(commandFilter)) {
/* 450 */         ArgumentBuilder<S, ?> builder = child.createBuilder();
/* 451 */         if (builder.getRedirect() != null) {
/* 452 */           builder.redirect((CommandNode)converted.get(builder.getRedirect()));
/*     */         }
/* 454 */         CommandNode<S> node = builder.build();
/* 455 */         converted.put(child, node);
/* 456 */         target.addChild(node);
/* 457 */         if (!child.getChildren().isEmpty()) {
/* 458 */           fillUsableCommands(child, node, commandFilter, converted);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 465 */   public static LiteralArgumentBuilder<CommandSourceStack> literal(String literal) { return LiteralArgumentBuilder.literal(literal); }
/*     */ 
/*     */ 
/*     */   
/* 469 */   public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) { return RequiredArgumentBuilder.argument(name, type); }
/*     */ 
/*     */   
/*     */   public static Predicate<String> createValidator(ParseFunction parser) {
/* 473 */     return value -> {
/*     */         try {
/* 475 */           parser.parse(new StringReader(value));
/* 476 */           return true;
/* 477 */         } catch (CommandSyntaxException ignored) {
/* 478 */           return false;
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 484 */   public CommandDispatcher<CommandSourceStack> getDispatcher() { return this.dispatcher; }
/*     */ 
/*     */   
/*     */   public static <S> void validateParseResults(ParseResults<S> command) throws CommandSyntaxException {
/* 488 */     CommandSyntaxException parseException = getParseException(command);
/* 489 */     if (parseException != null) {
/* 490 */       throw parseException;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <S> CommandSyntaxException getParseException(ParseResults<S> parse) {
/* 495 */     if (!parse.getReader().canRead())
/* 496 */       return null; 
/* 497 */     if (parse.getExceptions().size() == 1)
/* 498 */       return (CommandSyntaxException)parse.getExceptions().values().iterator().next(); 
/* 499 */     if (parse.getContext().getRange().isEmpty()) {
/* 500 */       return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
/*     */     }
/* 502 */     return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
/*     */   }
/*     */   
/*     */   public static CommandBuildContext createValidationContext(final HolderLookup.Provider registries) {
/* 506 */     return new CommandBuildContext()
/*     */       {
/*     */         public FeatureFlagSet enabledFeatures() {
/* 509 */           return FeatureFlags.REGISTRY.allFlags();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 514 */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() { return registries.listRegistryKeys(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 519 */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return registries.lookup(key).map(this::createLookup); }
/*     */ 
/*     */ 
/*     */         
/*     */         private <T> HolderLookup.RegistryLookup.Delegate<T> createLookup(final HolderLookup.RegistryLookup<T> original) {
/* 524 */           return new HolderLookup.RegistryLookup.Delegate<T>(this)
/*     */             {
/*     */               public HolderLookup.RegistryLookup<T> parent() {
/* 527 */                 return original;
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 532 */               public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.of(getOrThrow(id)); }
/*     */ 
/*     */ 
/*     */               
/*     */               public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 537 */                 Optional<HolderSet.Named<T>> tag = parent().get(id);
/* 538 */                 return (HolderSet.Named)tag.orElseGet(() -> HolderSet.emptyNamed(parent(), id));
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 546 */     context = createValidationContext(VanillaRegistries.createLookup());
/* 547 */     CommandDispatcher<CommandSourceStack> dispatcher = (new Commands(CommandSelection.ALL, context)).getDispatcher();
/* 548 */     RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();
/*     */     
/* 550 */     dispatcher.findAmbiguities((parent, child, sibling, ambiguities) -> 
/* 551 */         LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", new Object[] { dispatcher.getPath(child), dispatcher.getPath(sibling), ambiguities }));
/*     */ 
/*     */     
/* 554 */     Set<ArgumentType<?>> usedArgumentTypes = ArgumentUtils.findUsedArgumentTypes(root);
/* 555 */     Set<ArgumentType<?>> unregisteredTypes = (Set)usedArgumentTypes.stream().filter(arg -> !ArgumentTypeInfos.isClassRecognized(arg.getClass())).collect(Collectors.toSet());
/* 556 */     if (!unregisteredTypes.isEmpty()) {
/* 557 */       LOGGER.warn("Missing type registration for following arguments:\n {}", unregisteredTypes.stream().map(arg -> "\t" + String.valueOf(arg)).collect(Collectors.joining(",\n")));
/* 558 */       throw new IllegalStateException("Unregistered argument types");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 563 */   public static <T extends net.minecraft.server.permissions.PermissionSetSupplier> PermissionProviderCheck<T> hasPermission(PermissionCheck permission) { return new PermissionProviderCheck(permission); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 569 */   public static CommandSourceStack createCompilationContext(PermissionSet compilationPermissions) { return new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, compilationPermissions, "", CommonComponents.EMPTY, null, null); }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ParseFunction {
/*     */     void parse(StringReader param1StringReader) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\Commands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */