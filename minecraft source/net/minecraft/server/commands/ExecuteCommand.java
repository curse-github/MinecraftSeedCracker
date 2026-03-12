/*      */ package net.minecraft.server.commands;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.brigadier.Command;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.Message;
/*      */ import com.mojang.brigadier.RedirectModifier;
/*      */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*      */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*      */ import com.mojang.brigadier.context.CommandContext;
/*      */ import com.mojang.brigadier.context.ContextChain;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*      */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*      */ import com.mojang.brigadier.tree.CommandNode;
/*      */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntPredicate;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*      */ import net.minecraft.commands.CommandBuildContext;
/*      */ import net.minecraft.commands.CommandResultCallback;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.ExecutionCommandSource;
/*      */ import net.minecraft.commands.FunctionInstantiationException;
/*      */ import net.minecraft.commands.arguments.DimensionArgument;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.commands.arguments.EntityArgument;
/*      */ import net.minecraft.commands.arguments.HeightmapTypeArgument;
/*      */ import net.minecraft.commands.arguments.IdentifierArgument;
/*      */ import net.minecraft.commands.arguments.NbtPathArgument;
/*      */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*      */ import net.minecraft.commands.arguments.RangeArgument;
/*      */ import net.minecraft.commands.arguments.ResourceArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*      */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*      */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*      */ import net.minecraft.commands.arguments.SlotsArgument;
/*      */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*      */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*      */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*      */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*      */ import net.minecraft.commands.execution.ChainModifiers;
/*      */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*      */ import net.minecraft.commands.execution.ExecutionControl;
/*      */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*      */ import net.minecraft.commands.execution.tasks.CallFunction;
/*      */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*      */ import net.minecraft.commands.execution.tasks.IsolatedCall;
/*      */ import net.minecraft.commands.functions.CommandFunction;
/*      */ import net.minecraft.commands.functions.InstantiatedFunction;
/*      */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.ByteTag;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.DoubleTag;
/*      */ import net.minecraft.nbt.FloatTag;
/*      */ import net.minecraft.nbt.IntTag;
/*      */ import net.minecraft.nbt.LongTag;
/*      */ import net.minecraft.nbt.ShortTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.bossevents.CustomBossEvent;
/*      */ import net.minecraft.server.commands.data.DataAccessor;
/*      */ import net.minecraft.server.commands.data.DataCommands;
/*      */ import net.minecraft.server.level.FullChunkStatus;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Stopwatch;
/*      */ import net.minecraft.world.Stopwatches;
/*      */ import net.minecraft.world.entity.Attackable;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.OwnableEntity;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.SlotProvider;
/*      */ import net.minecraft.world.entity.Targeting;
/*      */ import net.minecraft.world.entity.TraceableEntity;
/*      */ import net.minecraft.world.inventory.SlotRange;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExecuteCommand
/*      */ {
/*  165 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int MAX_TEST_AREA = 32768;
/*  168 */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.execute.blocks.toobig", new Object[] { max, count }));
/*      */   
/*  170 */   private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.execute.conditional.fail"));
/*  171 */   private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(count -> Component.translatableEscape("commands.execute.conditional.fail_count", new Object[] { count }));
/*      */   
/*      */   @VisibleForTesting
/*  174 */   public static final Dynamic2CommandExceptionType ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE = new Dynamic2CommandExceptionType((id, reason) -> Component.translatableEscape("commands.execute.function.instantiationFailure", new Object[] { id, reason }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  187 */     LiteralCommandNode<CommandSourceStack> execute = dispatcher.register((LiteralArgumentBuilder)Commands.literal("execute")
/*  188 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)));
/*      */     
/*  190 */     dispatcher.register(
/*  191 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute")
/*  192 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  193 */         .then(
/*  194 */           Commands.literal("run")
/*  195 */           .redirect(dispatcher.getRoot())))
/*      */         
/*  197 */         .then(
/*  198 */           addConditionals(execute, Commands.literal("if"), true, context)))
/*      */         
/*  200 */         .then(
/*  201 */           addConditionals(execute, Commands.literal("unless"), false, context)))
/*      */         
/*  203 */         .then(
/*  204 */           Commands.literal("as")
/*  205 */           .then(
/*  206 */             Commands.argument("targets", EntityArgument.entities())
/*  207 */             .fork(execute, c -> {
/*  208 */                 List<CommandSourceStack> result = Lists.newArrayList();
/*  209 */                 for (Entity entity : EntityArgument.getOptionalEntities(c, "targets")) {
/*  210 */                   result.add(((CommandSourceStack)c.getSource()).withEntity(entity));
/*      */                 }
/*  212 */                 return result;
/*      */ 
/*      */ 
/*      */               
/*  216 */               })))).then(
/*  217 */           Commands.literal("at")
/*  218 */           .then(
/*  219 */             Commands.argument("targets", EntityArgument.entities())
/*  220 */             .fork(execute, c -> {
/*  221 */                 List<CommandSourceStack> result = Lists.newArrayList();
/*  222 */                 for (Entity entity : EntityArgument.getOptionalEntities(c, "targets")) {
/*  223 */                   result.add(((CommandSourceStack)c.getSource()).withLevel((ServerLevel)entity.level()).withPosition(entity.position()).withRotation(entity.getRotationVector()));
/*      */                 }
/*  225 */                 return result;
/*      */ 
/*      */ 
/*      */               
/*  229 */               })))).then((
/*  230 */           (LiteralArgumentBuilder)Commands.literal("store")
/*  231 */           .then(wrapStores(execute, Commands.literal("result"), true)))
/*  232 */           .then(wrapStores(execute, Commands.literal("success"), false))))
/*      */         
/*  234 */         .then((
/*  235 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("positioned")
/*  236 */           .then(
/*  237 */             Commands.argument("pos", Vec3Argument.vec3())
/*  238 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).withPosition(Vec3Argument.getVec3(c, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET))))
/*      */           
/*  240 */           .then(
/*  241 */             Commands.literal("as")
/*  242 */             .then(
/*  243 */               Commands.argument("targets", EntityArgument.entities())
/*  244 */               .fork(execute, c -> {
/*  245 */                   List<CommandSourceStack> result = Lists.newArrayList();
/*  246 */                   for (Entity entity : EntityArgument.getOptionalEntities(c, "targets")) {
/*  247 */                     result.add(((CommandSourceStack)c.getSource()).withPosition(entity.position()));
/*      */                   }
/*  249 */                   return result;
/*      */ 
/*      */ 
/*      */                 
/*  253 */                 })))).then(
/*  254 */             Commands.literal("over")
/*  255 */             .then(
/*  256 */               Commands.argument("heightmap", HeightmapTypeArgument.heightmap())
/*  257 */               .redirect(execute, c -> {
/*      */ 
/*      */                   
/*  260 */                   Vec3 position = ((CommandSourceStack)c.getSource()).getPosition();
/*  261 */                   ServerLevel level = ((CommandSourceStack)c.getSource()).getLevel();
/*  262 */                   double x = position.x();
/*  263 */                   double z = position.z();
/*  264 */                   if (!level.hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))) {
/*  265 */                     throw BlockPosArgument.ERROR_NOT_LOADED.create();
/*      */                   }
/*  267 */                   int height = level.getHeight(HeightmapTypeArgument.getHeightmap(c, "heightmap"), Mth.floor(x), Mth.floor(z));
/*  268 */                   return ((CommandSourceStack)c.getSource()).withPosition(new Vec3(x, height, z));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  274 */                 }))))).then((
/*  275 */           (LiteralArgumentBuilder)Commands.literal("rotated")
/*  276 */           .then(
/*  277 */             Commands.argument("rot", RotationArgument.rotation())
/*  278 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).withRotation(RotationArgument.getRotation(c, "rot").getRotation((CommandSourceStack)c.getSource())))))
/*      */           
/*  280 */           .then(
/*  281 */             Commands.literal("as")
/*  282 */             .then(
/*  283 */               Commands.argument("targets", EntityArgument.entities())
/*  284 */               .fork(execute, c -> {
/*  285 */                   List<CommandSourceStack> result = Lists.newArrayList();
/*  286 */                   for (Entity entity : EntityArgument.getOptionalEntities(c, "targets")) {
/*  287 */                     result.add(((CommandSourceStack)c.getSource()).withRotation(entity.getRotationVector()));
/*      */                   }
/*  289 */                   return result;
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  294 */                 }))))).then((
/*  295 */           (LiteralArgumentBuilder)Commands.literal("facing")
/*  296 */           .then(
/*  297 */             Commands.literal("entity")
/*  298 */             .then(
/*  299 */               Commands.argument("targets", EntityArgument.entities())
/*  300 */               .then(
/*  301 */                 Commands.argument("anchor", EntityAnchorArgument.anchor())
/*  302 */                 .fork(execute, c -> {
/*  303 */                     List<CommandSourceStack> result = Lists.newArrayList();
/*  304 */                     EntityAnchorArgument.Anchor anchor = EntityAnchorArgument.getAnchor(c, "anchor");
/*  305 */                     for (Entity entity : EntityArgument.getOptionalEntities(c, "targets")) {
/*  306 */                       result.add(((CommandSourceStack)c.getSource()).facing(entity, anchor));
/*      */                     }
/*  308 */                     return result;
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  313 */                   }))))).then(
/*  314 */             Commands.argument("pos", Vec3Argument.vec3())
/*  315 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).facing(Vec3Argument.getVec3(c, "pos"))))))
/*      */ 
/*      */         
/*  318 */         .then(
/*  319 */           Commands.literal("align")
/*  320 */           .then(
/*  321 */             Commands.argument("axes", SwizzleArgument.swizzle())
/*  322 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).withPosition(((CommandSourceStack)c.getSource()).getPosition().align(SwizzleArgument.getSwizzle(c, "axes")))))))
/*      */ 
/*      */         
/*  325 */         .then(
/*  326 */           Commands.literal("anchored")
/*  327 */           .then(
/*  328 */             Commands.argument("anchor", EntityAnchorArgument.anchor())
/*  329 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).withAnchor(EntityAnchorArgument.getAnchor(c, "anchor"))))))
/*      */ 
/*      */         
/*  332 */         .then(
/*  333 */           Commands.literal("in")
/*  334 */           .then(
/*  335 */             Commands.argument("dimension", DimensionArgument.dimension())
/*  336 */             .redirect(execute, c -> ((CommandSourceStack)c.getSource()).withLevel(DimensionArgument.getDimension(c, "dimension"))))))
/*      */ 
/*      */         
/*  339 */         .then(
/*  340 */           Commands.literal("summon")
/*  341 */           .then(
/*  342 */             Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
/*  343 */             .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/*  344 */             .redirect(execute, c -> spawnEntityAndRedirect((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"))))))
/*      */ 
/*      */         
/*  347 */         .then(
/*  348 */           createRelationOperations(execute, Commands.literal("on"))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(LiteralCommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> literal, boolean storeResult) {
/*  354 */     literal.then(
/*  355 */         Commands.literal("score")
/*  356 */         .then(
/*  357 */           Commands.argument("targets", ScoreHolderArgument.scoreHolders())
/*  358 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  359 */           .then(
/*  360 */             Commands.argument("objective", ObjectiveArgument.objective())
/*  361 */             .redirect(execute, c -> storeValue((CommandSourceStack)c.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(c, "targets"), ObjectiveArgument.getObjective(c, "objective"), storeResult)))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  366 */     literal.then(
/*  367 */         Commands.literal("bossbar")
/*  368 */         .then((
/*  369 */           (RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/*  370 */           .suggests(BossBarCommands.SUGGEST_BOSS_BAR)
/*  371 */           .then(
/*  372 */             Commands.literal("value")
/*  373 */             .redirect(execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), true, storeResult))))
/*      */           
/*  375 */           .then(
/*  376 */             Commands.literal("max")
/*  377 */             .redirect(execute, c -> storeValue((CommandSourceStack)c.getSource(), BossBarCommands.getBossBar(c), false, storeResult)))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  382 */     for (Iterator iterator = DataCommands.TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataCommands.DataProvider provider = (DataCommands.DataProvider)iterator.next();
/*  383 */       provider.wrap(literal, p -> p
/*  384 */           .then((
/*  385 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", NbtPathArgument.nbtPath())
/*  386 */             .then(
/*  387 */               Commands.literal("int")
/*  388 */               .then(
/*  389 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  390 */                 .redirect(execute, ()))))
/*      */ 
/*      */ 
/*      */             
/*  394 */             .then(
/*  395 */               Commands.literal("float")
/*  396 */               .then(
/*  397 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  398 */                 .redirect(execute, ()))))
/*      */ 
/*      */ 
/*      */             
/*  402 */             .then(
/*  403 */               Commands.literal("short")
/*  404 */               .then(
/*  405 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  406 */                 .redirect(execute, ()))))
/*      */ 
/*      */ 
/*      */             
/*  410 */             .then(
/*  411 */               Commands.literal("long")
/*  412 */               .then(
/*  413 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  414 */                 .redirect(execute, ()))))
/*      */ 
/*      */ 
/*      */             
/*  418 */             .then(
/*  419 */               Commands.literal("double")
/*  420 */               .then(
/*  421 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  422 */                 .redirect(execute, ()))))
/*      */ 
/*      */ 
/*      */             
/*  426 */             .then(
/*  427 */               Commands.literal("byte")
/*  428 */               .then(
/*  429 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  430 */                 .redirect(execute, ()))))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  437 */     return literal;
/*      */   }
/*      */   
/*      */   private static CommandSourceStack storeValue(CommandSourceStack source, Collection<ScoreHolder> names, Objective objective, boolean storeResult) {
/*  441 */     ServerScoreboard serverScoreboard = source.getServer().getScoreboard();
/*      */     
/*  443 */     return source.withCallback((success, result) -> {
/*  444 */           for (ScoreHolder name : names) {
/*  445 */             ScoreAccess score = scoreboard.getOrCreatePlayerScore(name, objective);
/*  446 */             int value = storeResult ? result : (success ? 1 : 0);
/*  447 */             score.set(value);
/*      */           } 
/*      */         }CommandResultCallback::chain);
/*      */   }
/*      */ 
/*      */   
/*  453 */   private static CommandSourceStack storeValue(CommandSourceStack source, CustomBossEvent event, boolean storeIntoValue, boolean storeResult) { return source.withCallback((success, result) -> {
/*  454 */           int value = storeResult ? result : (success ? 1 : 0);
/*  455 */           if (storeIntoValue) {
/*  456 */             event.setValue(value);
/*      */           } else {
/*  458 */             event.setMax(value);
/*      */           } 
/*      */         }CommandResultCallback::chain); }
/*      */ 
/*      */ 
/*      */   
/*  464 */   private static CommandSourceStack storeData(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path, IntFunction<Tag> constructor, boolean storeResult) { return source.withCallback((success, result) -> {
/*      */           try {
/*  466 */             CompoundTag data = accessor.getData();
/*  467 */             int value = storeResult ? result : (success ? 1 : 0);
/*  468 */             path.set(data, (Tag)constructor.apply(value));
/*  469 */             accessor.setData(data);
/*  470 */           } catch (CommandSyntaxException commandSyntaxException) {}
/*      */         }CommandResultCallback::chain); }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isChunkLoaded(ServerLevel level, BlockPos pos) {
/*  476 */     ChunkPos chunkPos = new ChunkPos(pos);
/*      */     
/*  478 */     LevelChunk chunk = level.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
/*  479 */     if (chunk != null) {
/*  480 */       return (chunk.getFullStatus() == FullChunkStatus.ENTITY_TICKING && level.areEntitiesLoaded(chunkPos.toLong()));
/*      */     }
/*  482 */     return false;
/*      */   }
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> parent, boolean expected, CommandBuildContext context) {
/*  486 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)parent
/*  487 */       .then(
/*  488 */         Commands.literal("block")
/*  489 */         .then(
/*  490 */           Commands.argument("pos", BlockPosArgument.blockPos())
/*  491 */           .then(
/*  492 */             addConditional(execute, Commands.argument("block", BlockPredicateArgument.blockPredicate(context)), expected, c -> BlockPredicateArgument.getBlockPredicate(c, "block").test(new BlockInWorld(((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), true)))))))
/*      */ 
/*      */ 
/*      */       
/*  496 */       .then(
/*  497 */         Commands.literal("biome")
/*  498 */         .then(
/*  499 */           Commands.argument("pos", BlockPosArgument.blockPos())
/*  500 */           .then(
/*  501 */             addConditional(execute, Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME)), expected, c -> ResourceOrTagArgument.getResourceOrTag(c, "biome", Registries.BIOME).test(((CommandSourceStack)c.getSource()).getLevel().getBiome(BlockPosArgument.getLoadedBlockPos(c, "pos"))))))))
/*      */ 
/*      */ 
/*      */       
/*  505 */       .then(
/*  506 */         Commands.literal("loaded")
/*  507 */         .then(
/*  508 */           addConditional(execute, Commands.argument("pos", BlockPosArgument.blockPos()), expected, c -> isChunkLoaded(((CommandSourceStack)c.getSource()).getLevel(), BlockPosArgument.getBlockPos(c, "pos"))))))
/*      */ 
/*      */       
/*  511 */       .then(
/*  512 */         Commands.literal("dimension")
/*  513 */         .then(
/*  514 */           addConditional(execute, Commands.argument("dimension", DimensionArgument.dimension()), expected, c -> 
/*  515 */             (DimensionArgument.getDimension(c, "dimension") == ((CommandSourceStack)c.getSource()).getLevel())))))
/*      */ 
/*      */ 
/*      */       
/*  519 */       .then(
/*  520 */         Commands.literal("score")
/*  521 */         .then(
/*  522 */           Commands.argument("target", ScoreHolderArgument.scoreHolder())
/*  523 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  524 */           .then((
/*  525 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", ObjectiveArgument.objective())
/*  526 */             .then(
/*  527 */               Commands.literal("=")
/*  528 */               .then(
/*  529 */                 Commands.argument("source", ScoreHolderArgument.scoreHolder())
/*  530 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  531 */                 .then(
/*  532 */                   addConditional(execute, Commands.argument("sourceObjective", ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  536 */             .then(
/*  537 */               Commands.literal("<")
/*  538 */               .then(
/*  539 */                 Commands.argument("source", ScoreHolderArgument.scoreHolder())
/*  540 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  541 */                 .then(
/*  542 */                   addConditional(execute, Commands.argument("sourceObjective", ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  546 */             .then(
/*  547 */               Commands.literal("<=")
/*  548 */               .then(
/*  549 */                 Commands.argument("source", ScoreHolderArgument.scoreHolder())
/*  550 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  551 */                 .then(
/*  552 */                   addConditional(execute, Commands.argument("sourceObjective", ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  556 */             .then(
/*  557 */               Commands.literal(">")
/*  558 */               .then(
/*  559 */                 Commands.argument("source", ScoreHolderArgument.scoreHolder())
/*  560 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  561 */                 .then(
/*  562 */                   addConditional(execute, Commands.argument("sourceObjective", ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  566 */             .then(
/*  567 */               Commands.literal(">=")
/*  568 */               .then(
/*  569 */                 Commands.argument("source", ScoreHolderArgument.scoreHolder())
/*  570 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  571 */                 .then(
/*  572 */                   addConditional(execute, Commands.argument("sourceObjective", ObjectiveArgument.objective()), expected, c -> checkScore(c, ()))))))
/*      */ 
/*      */ 
/*      */             
/*  576 */             .then(
/*  577 */               Commands.literal("matches")
/*  578 */               .then(
/*  579 */                 addConditional(execute, Commands.argument("range", RangeArgument.intRange()), expected, c -> checkScore(c, RangeArgument.Ints.getRange(c, "range")))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  585 */       .then(
/*  586 */         Commands.literal("blocks")
/*  587 */         .then(
/*  588 */           Commands.argument("start", BlockPosArgument.blockPos())
/*  589 */           .then(
/*  590 */             Commands.argument("end", BlockPosArgument.blockPos())
/*  591 */             .then((
/*  592 */               (RequiredArgumentBuilder)Commands.argument("destination", BlockPosArgument.blockPos())
/*  593 */               .then(
/*  594 */                 addIfBlocksConditional(execute, Commands.literal("all"), expected, false)))
/*      */               
/*  596 */               .then(
/*  597 */                 addIfBlocksConditional(execute, Commands.literal("masked"), expected, true)))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       .then(
/*  604 */         Commands.literal("entity")
/*  605 */         .then((
/*  606 */           (RequiredArgumentBuilder)Commands.argument("entities", EntityArgument.entities())
/*  607 */           .fork(execute, c -> expect(c, expected, !EntityArgument.getOptionalEntities(c, "entities").isEmpty())))
/*  608 */           .executes(createNumericConditionalHandler(expected, c -> EntityArgument.getOptionalEntities(c, "entities").size())))))
/*      */ 
/*      */ 
/*      */       
/*  612 */       .then(
/*  613 */         Commands.literal("predicate")
/*  614 */         .then(
/*  615 */           addConditional(execute, Commands.argument("predicate", ResourceOrIdArgument.lootPredicate(context)), expected, c -> checkCustomPredicate((CommandSourceStack)c.getSource(), ResourceOrIdArgument.getLootPredicate(c, "predicate"))))))
/*      */ 
/*      */       
/*  618 */       .then(
/*  619 */         Commands.literal("function")
/*  620 */         .then(
/*  621 */           Commands.argument("name", FunctionArgument.functions())
/*  622 */           .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  623 */           .fork(execute, new ExecuteIfFunctionCustomModifier(expected)))))
/*      */ 
/*      */       
/*  626 */       .then((
/*  627 */         (LiteralArgumentBuilder)Commands.literal("items")
/*  628 */         .then(
/*  629 */           Commands.literal("entity")
/*  630 */           .then(
/*  631 */             Commands.argument("entities", EntityArgument.entities())
/*  632 */             .then(
/*  633 */               Commands.argument("slots", SlotsArgument.slots())
/*  634 */               .then((
/*  635 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", ItemPredicateArgument.itemPredicate(context))
/*  636 */                 .fork(execute, c -> expect(c, expected, (countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  637 */                 .executes(createNumericConditionalHandler(expected, c -> countItems(EntityArgument.getEntities(c, "entities"), SlotsArgument.getSlots(c, "slots"), ItemPredicateArgument.getItemPredicate(c, "item_predicate")))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  642 */         .then(
/*  643 */           Commands.literal("block")
/*  644 */           .then(
/*  645 */             Commands.argument("pos", BlockPosArgument.blockPos())
/*  646 */             .then(
/*  647 */               Commands.argument("slots", SlotsArgument.slots())
/*  648 */               .then((
/*  649 */                 (RequiredArgumentBuilder)Commands.argument("item_predicate", ItemPredicateArgument.itemPredicate(context))
/*  650 */                 .fork(execute, c -> expect(c, expected, (countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), ItemPredicateArgument.getItemPredicate(c, "item_predicate")) > 0))))
/*  651 */                 .executes(createNumericConditionalHandler(expected, c -> countItems((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotsArgument.getSlots(c, "slots"), ItemPredicateArgument.getItemPredicate(c, "item_predicate"))))))))))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  657 */       .then(
/*  658 */         Commands.literal("stopwatch")
/*  659 */         .then(
/*  660 */           Commands.argument("id", IdentifierArgument.id())
/*  661 */           .suggests(StopwatchCommand.SUGGEST_STOPWATCHES)
/*  662 */           .then(
/*  663 */             addConditional(execute, Commands.argument("range", RangeArgument.floatRange()), expected, c -> checkStopwatch(c, RangeArgument.Floats.getRange(c, "range"))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     for (DataCommands.DataProvider provider : DataCommands.SOURCE_PROVIDERS) {
/*  670 */       parent
/*  671 */         .then(provider
/*  672 */           .wrap(Commands.literal("data"), p -> p
/*  673 */             .then((
/*  674 */               (RequiredArgumentBuilder)Commands.argument("path", NbtPathArgument.nbtPath())
/*  675 */               .fork(execute, ()))
/*  676 */               .executes(createNumericConditionalHandler(expected, ())))));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  682 */     return parent;
/*      */   }
/*      */   
/*      */   private static int countItems(Iterable<? extends SlotProvider> sources, SlotRange slotRange, Predicate<ItemStack> predicate) {
/*  686 */     int count = 0;
/*  687 */     for (SlotProvider slotProvider : sources) {
/*  688 */       IntList slots = slotRange.slots();
/*  689 */       for (int i = 0; i < slots.size(); i++) {
/*  690 */         int slotId = slots.getInt(i);
/*  691 */         SlotAccess slot = slotProvider.getSlot(slotId);
/*  692 */         if (slot != null) {
/*  693 */           ItemStack contents = slot.get();
/*  694 */           if (predicate.test(contents)) {
/*  695 */             count += contents.getCount();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  700 */     return count;
/*      */   }
/*      */   
/*      */   private static int countItems(CommandSourceStack source, BlockPos pos, SlotRange slotRange, Predicate<ItemStack> predicate) throws CommandSyntaxException {
/*  704 */     int count = 0;
/*  705 */     Container container = ItemCommands.getContainer(source, pos, ItemCommands.ERROR_SOURCE_NOT_A_CONTAINER);
/*  706 */     int containerSize = container.getContainerSize();
/*      */     
/*  708 */     IntList slots = slotRange.slots();
/*  709 */     for (int i = 0; i < slots.size(); i++) {
/*  710 */       int slotId = slots.getInt(i);
/*  711 */       if (slotId >= 0 && slotId < containerSize) {
/*      */ 
/*      */         
/*  714 */         ItemStack contents = container.getItem(slotId);
/*  715 */         if (predicate.test(contents)) {
/*  716 */           count += contents.getCount();
/*      */         }
/*      */       } 
/*      */     } 
/*  720 */     return count;
/*      */   }
/*      */   
/*      */   private static Command<CommandSourceStack> createNumericConditionalHandler(boolean expected, CommandNumericPredicate condition) {
/*  724 */     if (expected) {
/*  725 */       return c -> {
/*  726 */           int count = condition.test(c);
/*  727 */           if (count > 0) {
/*  728 */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*  729 */             return count;
/*      */           } 
/*  731 */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         };
/*      */     }
/*      */     
/*  735 */     return c -> {
/*  736 */         int count = condition.test(c);
/*  737 */         if (count == 0) {
/*  738 */           ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*  739 */           return 1;
/*      */         } 
/*  741 */         throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(count));
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  748 */   private static int checkMatchingData(DataAccessor accessor, NbtPathArgument.NbtPath path) throws CommandSyntaxException { return path.countMatching(accessor.getData()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, IntBiPredicate operation) throws CommandSyntaxException {
/*  757 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  758 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*  759 */     ScoreHolder source = ScoreHolderArgument.getName(context, "source");
/*  760 */     Objective sourceObjective = ObjectiveArgument.getObjective(context, "sourceObjective");
/*      */     
/*  762 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*      */     
/*  764 */     ReadOnlyScoreInfo a = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*  765 */     ReadOnlyScoreInfo b = serverScoreboard.getPlayerScoreInfo(source, sourceObjective);
/*      */     
/*  767 */     if (a == null || b == null) {
/*  768 */       return false;
/*      */     }
/*      */     
/*  771 */     return operation.test(a.value(), b.value());
/*      */   }
/*      */   
/*      */   private static boolean checkScore(CommandContext<CommandSourceStack> context, MinMaxBounds.Ints range) throws CommandSyntaxException {
/*  775 */     ScoreHolder target = ScoreHolderArgument.getName(context, "target");
/*  776 */     Objective targetObjective = ObjectiveArgument.getObjective(context, "targetObjective");
/*      */     
/*  778 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)context.getSource()).getServer().getScoreboard();
/*      */     
/*  780 */     ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(target, targetObjective);
/*      */     
/*  782 */     if (scoreInfo == null) {
/*  783 */       return false;
/*      */     }
/*      */     
/*  786 */     return range.matches(scoreInfo.value());
/*      */   }
/*      */   
/*      */   private static boolean checkStopwatch(CommandContext<CommandSourceStack> context, MinMaxBounds.Doubles range) throws CommandSyntaxException {
/*  790 */     Identifier id = IdentifierArgument.getId(context, "id");
/*  791 */     Stopwatches stopwatches = ((CommandSourceStack)context.getSource()).getServer().getStopwatches();
/*  792 */     Stopwatch stopwatch = stopwatches.get(id);
/*  793 */     if (stopwatch == null) {
/*  794 */       throw StopwatchCommand.ERROR_DOES_NOT_EXIST.create(id);
/*      */     }
/*  796 */     long currentTime = Stopwatches.currentTime();
/*  797 */     double elapsedSeconds = stopwatch.elapsedSeconds(currentTime);
/*  798 */     return range.matches(elapsedSeconds);
/*      */   }
/*      */   
/*      */   private static boolean checkCustomPredicate(CommandSourceStack source, Holder<LootItemCondition> predicate) {
/*  802 */     ServerLevel level = source.getLevel();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  807 */     LootParams lootParams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, source.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.COMMAND);
/*  808 */     LootContext context = (new LootContext.Builder(lootParams)).create(Optional.empty());
/*  809 */     context.pushVisitedElement(LootContext.createVisitedEntry((LootItemCondition)predicate.value()));
/*  810 */     return ((LootItemCondition)predicate.value()).test(context);
/*      */   }
/*      */   
/*      */   private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> context, boolean expected, boolean result) {
/*  814 */     if (result == expected) {
/*  815 */       return Collections.singleton((CommandSourceStack)context.getSource());
/*      */     }
/*  817 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */   
/*      */   private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, CommandPredicate predicate) {
/*  822 */     return argument
/*  823 */       .fork(root, c -> expect(c, expected, predicate.test(c)))
/*  824 */       .executes(c -> {
/*  825 */           if (expected == predicate.test(c)) {
/*  826 */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*  827 */             return 1;
/*      */           } 
/*  829 */           throw ERROR_CONDITIONAL_FAILED.create();
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  835 */   private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> argument, boolean expected, boolean skipAir) { return argument
/*  836 */       .fork(root, c -> expect(c, expected, checkRegions(c, skipAir).isPresent()))
/*  837 */       .executes(expected ? (c -> checkIfRegions(c, skipAir)) : (c -> checkUnlessRegions(c, skipAir))); }
/*      */ 
/*      */   
/*      */   private static int checkIfRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  841 */     OptionalInt count = checkRegions(context, skipAir);
/*  842 */     if (count.isPresent()) {
/*  843 */       ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", new Object[] { Integer.valueOf(count.getAsInt()) }), false);
/*  844 */       return count.getAsInt();
/*      */     } 
/*  846 */     throw ERROR_CONDITIONAL_FAILED.create();
/*      */   }
/*      */ 
/*      */   
/*      */   private static int checkUnlessRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException {
/*  851 */     OptionalInt count = checkRegions(context, skipAir);
/*  852 */     if (count.isPresent()) {
/*  853 */       throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(count.getAsInt()));
/*      */     }
/*  855 */     ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
/*  856 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  861 */   private static OptionalInt checkRegions(CommandContext<CommandSourceStack> context, boolean skipAir) throws CommandSyntaxException { return checkRegions(((CommandSourceStack)context.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(context, "start"), BlockPosArgument.getLoadedBlockPos(context, "end"), BlockPosArgument.getLoadedBlockPos(context, "destination"), skipAir); }
/*      */ 
/*      */   
/*      */   private static OptionalInt checkRegions(ServerLevel level, BlockPos startPos, BlockPos endPos, BlockPos destPos, boolean skipAir) throws CommandSyntaxException {
/*  865 */     BoundingBox from = BoundingBox.fromCorners(startPos, endPos);
/*  866 */     BoundingBox destination = BoundingBox.fromCorners(destPos, destPos.offset(from.getLength()));
/*  867 */     BlockPos offset = new BlockPos(destination.minX() - from.minX(), destination.minY() - from.minY(), destination.minZ() - from.minZ());
/*  868 */     int area = from.getXSpan() * from.getYSpan() * from.getZSpan();
/*      */     
/*  870 */     if (area > 32768) {
/*  871 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(32768), Integer.valueOf(area));
/*      */     }
/*      */     
/*  874 */     int count = 0;
/*  875 */     RegistryAccess registryAccess = level.registryAccess();
/*  876 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/*  877 */     try { for (int z = from.minZ(); z <= from.maxZ(); z++)
/*  878 */       { for (int y = from.minY(); y <= from.maxY(); y++)
/*  879 */         { for (int x = from.minX(); x <= from.maxX(); x++)
/*  880 */           { BlockPos sourcePos = new BlockPos(x, y, z);
/*  881 */             BlockPos destinationPos = sourcePos.offset(offset);
/*      */             
/*  883 */             BlockState sourceBlock = level.getBlockState(sourcePos);
/*  884 */             if (!skipAir || !sourceBlock.is(Blocks.AIR))
/*      */             
/*      */             { 
/*      */               
/*  888 */               if (sourceBlock != level.getBlockState(destinationPos))
/*  889 */               { OptionalInt optionalInt = OptionalInt.empty();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  921 */                 reporter.close(); return optionalInt; }  BlockEntity sourceBlockEntity = level.getBlockEntity(sourcePos); BlockEntity destinationBlockEntity = level.getBlockEntity(destinationPos); if (sourceBlockEntity != null) { if (destinationBlockEntity == null) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  if (destinationBlockEntity.getType() != sourceBlockEntity.getType()) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  if (!sourceBlockEntity.components().equals(destinationBlockEntity.components())) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  TagValueOutput sourceOutput = TagValueOutput.createWithContext(reporter.forChild(sourceBlockEntity.problemPath()), registryAccess); sourceBlockEntity.saveCustomOnly(sourceOutput); CompoundTag sourceTag = sourceOutput.buildResult(); TagValueOutput destinationOutput = TagValueOutput.createWithContext(reporter.forChild(destinationBlockEntity.problemPath()), registryAccess); destinationBlockEntity.saveCustomOnly(destinationOutput); CompoundTag destinationTag = destinationOutput.buildResult(); if (!sourceTag.equals(destinationTag)) { OptionalInt optionalInt = OptionalInt.empty(); reporter.close(); return optionalInt; }  }  count++; }  }  }  }  reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/*  923 */      return OptionalInt.of(count);
/*      */   }
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToOneEntityRelation(Function<Entity, Optional<Entity>> unpacker) {
/*  927 */     return context -> {
/*  928 */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*  929 */         Entity entity = source.getEntity();
/*  930 */         if (entity == null) {
/*  931 */           return List.of();
/*      */         }
/*  933 */         return (Collection)((Optional)unpacker.apply(entity)).filter(()).map(()).orElse(List.of());
/*      */       };
/*      */   }
/*      */   
/*      */   private static RedirectModifier<CommandSourceStack> expandOneToManyEntityRelation(Function<Entity, Stream<Entity>> unpacker) {
/*  938 */     return context -> {
/*  939 */         CommandSourceStack source = (CommandSourceStack)context.getSource();
/*  940 */         Entity entity = source.getEntity();
/*  941 */         if (entity == null) {
/*  942 */           return List.of();
/*      */         }
/*  944 */         Objects.requireNonNull(source); return ((Stream)unpacker.apply(entity)).filter(()).map(source::withEntity).toList();
/*      */       };
/*      */   }
/*      */   
/*      */   private static LiteralArgumentBuilder<CommandSourceStack> createRelationOperations(CommandNode<CommandSourceStack> execute, LiteralArgumentBuilder<CommandSourceStack> on) {
/*  949 */     return (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)on
/*  950 */       .then(
/*  951 */         Commands.literal("owner")
/*  952 */         .fork(execute, expandOneToOneEntityRelation(e -> { OwnableEntity ownableEntity = (OwnableEntity)e; return (e instanceof OwnableEntity) ? Optional.ofNullable(ownableEntity.getOwner()) : Optional.empty();
/*      */             
/*  954 */             })))).then(
/*  955 */         Commands.literal("leasher")
/*  956 */         .fork(execute, expandOneToOneEntityRelation(e -> { Leashable leashable = (Leashable)e; return (e instanceof Leashable) ? Optional.ofNullable(leashable.getLeashHolder()) : Optional.empty();
/*      */             
/*  958 */             })))).then(
/*  959 */         Commands.literal("target")
/*  960 */         .fork(execute, expandOneToOneEntityRelation(e -> { Targeting targeting = (Targeting)e; return (e instanceof Targeting) ? Optional.ofNullable(targeting.getTarget()) : Optional.empty();
/*      */             
/*  962 */             })))).then(
/*  963 */         Commands.literal("attacker")
/*  964 */         .fork(execute, expandOneToOneEntityRelation(e -> { Attackable attackable = (Attackable)e; return (e instanceof Attackable) ? Optional.ofNullable(attackable.getLastAttacker()) : Optional.empty();
/*      */             
/*  966 */             })))).then(
/*  967 */         Commands.literal("vehicle")
/*  968 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getVehicle())))))
/*      */       
/*  970 */       .then(
/*  971 */         Commands.literal("controller")
/*  972 */         .fork(execute, expandOneToOneEntityRelation(e -> Optional.ofNullable(e.getControllingPassenger())))))
/*      */       
/*  974 */       .then(
/*  975 */         Commands.literal("origin")
/*  976 */         .fork(execute, expandOneToOneEntityRelation(e -> { TraceableEntity traceable = (TraceableEntity)e; return (e instanceof TraceableEntity) ? Optional.ofNullable(traceable.getOwner()) : Optional.empty();
/*      */             
/*  978 */             })))).then(
/*  979 */         Commands.literal("passengers")
/*  980 */         .fork(execute, expandOneToManyEntityRelation(e -> e.getPassengers().stream())));
/*      */   }
/*      */ 
/*      */   
/*      */   private static CommandSourceStack spawnEntityAndRedirect(CommandSourceStack source, Holder.Reference<EntityType<?>> type) throws CommandSyntaxException {
/*  985 */     Entity entity = SummonCommand.createEntity(source, type, source.getPosition(), new CompoundTag(), true);
/*  986 */     return source.withEntity(entity);
/*      */   } @FunctionalInterface
/*      */   private static interface CommandPredicate {
/*      */     boolean test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException; } @FunctionalInterface
/*      */   private static interface CommandNumericPredicate {
/*      */     int test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException; }
/*      */   private static class ExecuteIfFunctionCustomModifier extends Object implements CustomModifierExecutor.ModifierAdapter<CommandSourceStack> { private final IntPredicate check;
/*  993 */     private ExecuteIfFunctionCustomModifier(boolean check) { this.check = check ? (value -> (value != 0)) : (value -> (value == 0)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  998 */     public void apply(CommandSourceStack originalSource, List<CommandSourceStack> currentSources, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) { ExecuteCommand.scheduleFunctionConditionsAndTest(originalSource, currentSources, FunctionCommand::modifySenderForExecution, this.check, currentStep, null, output, c -> 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1006 */           FunctionArgument.getFunctions(c, "name"), modifiers); } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends ExecutionCommandSource<T>> void scheduleFunctionConditionsAndTest(T originalSource, List<T> currentSources, Function<T, T> functionContextModifier, IntPredicate check, ContextChain<T> currentStep, CompoundTag parameters, ExecutionControl<T> output, InCommandFunction<CommandContext<T>, Collection<CommandFunction<T>>> functionGetter, ChainModifiers modifiers) {
/*      */     Collection<CommandFunction<T>> functionsToRun;
/* 1013 */     List<T> filteredSources = new ArrayList<T>(currentSources.size());
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1018 */       functionsToRun = (Collection)functionGetter.apply(currentStep.getTopContext().copyFor(originalSource));
/* 1019 */     } catch (CommandSyntaxException e) {
/* 1020 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */       
/*      */       return;
/*      */     } 
/* 1024 */     int functionCount = functionsToRun.size();
/* 1025 */     if (functionCount == 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1030 */     List<InstantiatedFunction<T>> instantiatedFunctions = new ArrayList<InstantiatedFunction<T>>(functionCount);
/*      */     
/*      */     try {
/* 1033 */       for (CommandFunction<T> function : functionsToRun) {
/*      */         try {
/* 1035 */           instantiatedFunctions.add(function.instantiate(parameters, originalSource.dispatcher()));
/* 1036 */         } catch (FunctionInstantiationException e) {
/* 1037 */           throw ERROR_FUNCTION_CONDITION_INSTANTATION_FAILURE.create(function.id(), e.messageComponent());
/*      */         } 
/*      */       } 
/* 1040 */     } catch (CommandSyntaxException e) {
/* 1041 */       originalSource.handleError(e, modifiers.isForked(), output.tracer());
/*      */     } 
/*      */     
/* 1044 */     for (Iterator iterator = currentSources.iterator(); iterator.hasNext(); ) { T source = (T)(ExecutionCommandSource)iterator.next();
/* 1045 */       T newFunctionContext = (T)(ExecutionCommandSource)functionContextModifier.apply(source.clearCallbacks());
/* 1046 */       CommandResultCallback functionCallback = (success, result) -> {
/* 1047 */           if (check.test(result)) {
/* 1048 */             filteredSources.add(source);
/*      */           }
/*      */         };
/*      */       
/* 1052 */       output.queueNext(new IsolatedCall(o -> {
/* 1053 */               for (InstantiatedFunction<T> function : instantiatedFunctions) {
/* 1054 */                 o.queueNext((new CallFunction(function, o.currentFrame().returnValueConsumer(), true)).bind(newFunctionContext));
/*      */               }
/* 1056 */               o.queueNext(FallthroughTask.instance());
/*      */             }functionCallback)); }
/*      */ 
/*      */     
/* 1060 */     ContextChain<T> nextStage = currentStep.nextStage();
/* 1061 */     String input = currentStep.getTopContext().getInput();
/* 1062 */     output.queueNext(new BuildContexts.Continuation(input, nextStage, modifiers, originalSource, filteredSources));
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface IntBiPredicate {
/*      */     boolean test(int param1Int1, int param1Int2);
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ExecuteCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */