/*     */ package net.minecraft.gametest.framework;
/*     */ 
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
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.commands.arguments.ResourceSelectorArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameTestHighlightPosPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.commands.InCommandFunction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TestCommand
/*     */ {
/*     */   public static final int TEST_NEARBY_SEARCH_RADIUS = 15;
/*     */   public static final int TEST_FULL_SEARCH_RADIUS = 250;
/*     */   public static final int VERIFY_TEST_GRID_AXIS_SIZE = 10;
/*     */   public static final int VERIFY_TEST_BATCH_SIZE = 100;
/*     */   private static final int DEFAULT_CLEAR_RADIUS = 250;
/*     */   private static final int MAX_CLEAR_RADIUS = 1024;
/*     */   private static final int TEST_POS_Z_OFFSET_FROM_PLAYER = 3;
/*     */   private static final int DEFAULT_X_SIZE = 5;
/*     */   private static final int DEFAULT_Y_SIZE = 5;
/*     */   private static final int DEFAULT_Z_SIZE = 5;
/*  81 */   private static final SimpleCommandExceptionType CLEAR_NO_TESTS = new SimpleCommandExceptionType(Component.translatable("commands.test.clear.error.no_tests"));
/*  82 */   private static final SimpleCommandExceptionType RESET_NO_TESTS = new SimpleCommandExceptionType(Component.translatable("commands.test.reset.error.no_tests"));
/*  83 */   private static final SimpleCommandExceptionType TEST_INSTANCE_COULD_NOT_BE_FOUND = new SimpleCommandExceptionType(Component.translatable("commands.test.error.test_instance_not_found"));
/*  84 */   private static final SimpleCommandExceptionType NO_STRUCTURES_TO_EXPORT = new SimpleCommandExceptionType(Component.literal("Could not find any structures to export"));
/*  85 */   private static final SimpleCommandExceptionType NO_TEST_INSTANCES = new SimpleCommandExceptionType(Component.translatable("commands.test.error.no_test_instances"));
/*  86 */   private static final Dynamic3CommandExceptionType NO_TEST_CONTAINING = new Dynamic3CommandExceptionType((x, y, z) -> Component.translatableEscape("commands.test.error.no_test_containing_pos", new Object[] { x, y, z }));
/*  87 */   private static final DynamicCommandExceptionType TOO_LARGE = new DynamicCommandExceptionType(size -> Component.translatableEscape("commands.test.error.too_large", new Object[] { size }));
/*     */   
/*     */   private static int reset(TestFinder finder) throws CommandSyntaxException {
/*  90 */     stopTests();
/*  91 */     int count = toGameTestInfos(finder.source(), RetryOptions.noRetries(), finder).map(info -> Integer.valueOf(resetGameTestInfo(finder.source(), info))).toList().size();
/*  92 */     if (count == 0) {
/*  93 */       throw CLEAR_NO_TESTS.create();
/*     */     }
/*  95 */     finder.source().sendSuccess(() -> Component.translatable("commands.test.reset.success", new Object[] { Integer.valueOf(count) }), true);
/*     */     
/*  97 */     return count;
/*     */   }
/*     */   
/*     */   private static int clear(TestFinder finder) throws CommandSyntaxException {
/* 101 */     stopTests();
/* 102 */     CommandSourceStack source = finder.source();
/* 103 */     ServerLevel level = source.getLevel();
/*     */ 
/*     */     
/* 106 */     List<TestInstanceBlockEntity> tests = finder.findTestPos().flatMap(pos -> level.getBlockEntity(pos, BlockEntityType.TEST_INSTANCE_BLOCK).stream()).toList();
/* 107 */     for (TestInstanceBlockEntity testInstanceBlockEntity : tests) {
/* 108 */       StructureUtils.clearSpaceForStructure(testInstanceBlockEntity.getStructureBoundingBox(), level);
/* 109 */       testInstanceBlockEntity.removeBarriers();
/* 110 */       level.destroyBlock(testInstanceBlockEntity.getBlockPos(), false);
/*     */     } 
/* 112 */     if (tests.isEmpty()) {
/* 113 */       throw CLEAR_NO_TESTS.create();
/*     */     }
/* 115 */     source.sendSuccess(() -> Component.translatable("commands.test.clear.success", new Object[] { Integer.valueOf(tests.size()) }), true);
/*     */     
/* 117 */     return tests.size();
/*     */   }
/*     */   
/*     */   private static int export(TestFinder finder) throws CommandSyntaxException {
/* 121 */     CommandSourceStack source = finder.source();
/* 122 */     ServerLevel level = source.getLevel();
/*     */     
/* 124 */     int count = 0;
/* 125 */     boolean allGood = true;
/* 126 */     for (Iterator<BlockPos> iterator = finder.findTestPos().iterator(); iterator.hasNext(); ) {
/* 127 */       BlockPos pos = (BlockPos)iterator.next();
/* 128 */       BlockEntity blockEntity1 = level.getBlockEntity(pos); if (blockEntity1 instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity blockEntity = (TestInstanceBlockEntity)blockEntity1;
/* 129 */         Objects.requireNonNull(source); if (!blockEntity.exportTest(source::sendSystemMessage)) {
/* 130 */           allGood = false;
/*     */         }
/* 132 */         count++; continue; }
/*     */       
/* 134 */       throw TEST_INSTANCE_COULD_NOT_BE_FOUND.create();
/*     */     } 
/*     */     
/* 137 */     if (count == 0) {
/* 138 */       throw NO_STRUCTURES_TO_EXPORT.create();
/*     */     }
/* 140 */     String message = "Exported " + count + " structures";
/* 141 */     finder.source().sendSuccess(() -> Component.literal(message), true);
/*     */ 
/*     */     
/* 144 */     return allGood ? 0 : 1;
/*     */   }
/*     */   
/*     */   private static int verify(TestFinder finder) throws CommandSyntaxException {
/* 148 */     stopTests();
/* 149 */     CommandSourceStack source = finder.source();
/* 150 */     ServerLevel level = source.getLevel();
/* 151 */     BlockPos testPos = createTestPositionAround(source);
/*     */ 
/*     */ 
/*     */     
/* 155 */     Collection<GameTestInfo> infos = Stream.concat(toGameTestInfos(source, RetryOptions.noRetries(), finder), toGameTestInfo(source, RetryOptions.noRetries(), finder, 0)).toList();
/*     */     
/* 157 */     FailedTestTracker.forgetFailedTests();
/*     */     
/* 159 */     Collection<GameTestBatch> batches = new ArrayList<GameTestBatch>();
/* 160 */     for (GameTestInfo info : infos) {
/* 161 */       for (Rotation rotation : Rotation.values()) {
/* 162 */         Collection<GameTestInfo> transformedInfos = new ArrayList<GameTestInfo>();
/* 163 */         for (int i = 0; i < 100; i++) {
/* 164 */           GameTestInfo copyInfo = new GameTestInfo(info.getTestHolder(), rotation, level, new RetryOptions(1, true));
/* 165 */           copyInfo.setTestBlockPos(info.getTestBlockPos());
/* 166 */           transformedInfos.add(copyInfo);
/*     */         } 
/* 168 */         GameTestBatch batch = GameTestBatchFactory.toGameTestBatch(transformedInfos, info.getTest().batch(), rotation.ordinal());
/* 169 */         batches.add(batch);
/*     */       } 
/*     */     } 
/* 172 */     StructureGridSpawner spawner = new StructureGridSpawner(testPos, 10, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     GameTestRunner runner = GameTestRunner.Builder.fromBatches(batches, level).batcher(GameTestBatchFactory.fromGameTestInfo(100)).newStructureSpawner(spawner).existingStructureSpawner(spawner).haltOnError().clearBetweenBatches().build();
/* 180 */     return trackAndStartRunner(source, runner);
/*     */   }
/*     */   
/*     */   private static int run(TestFinder finder, RetryOptions retryOptions, int extraRotationSteps, int testsPerRow) {
/* 184 */     stopTests();
/* 185 */     CommandSourceStack source = finder.source();
/* 186 */     ServerLevel level = source.getLevel();
/*     */     
/* 188 */     BlockPos testPos = createTestPositionAround(source);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     Collection<GameTestInfo> infos = Stream.concat(toGameTestInfos(source, retryOptions, finder), toGameTestInfo(source, retryOptions, finder, extraRotationSteps)).toList();
/*     */     
/* 195 */     if (infos.isEmpty()) {
/* 196 */       source.sendSuccess(() -> Component.translatable("commands.test.no_tests"), false);
/* 197 */       return 0;
/*     */     } 
/*     */     
/* 200 */     FailedTestTracker.forgetFailedTests();
/* 201 */     source.sendSuccess(() -> Component.translatable("commands.test.run.running", new Object[] { Integer.valueOf(infos.size()) }), false);
/*     */ 
/*     */ 
/*     */     
/* 205 */     GameTestRunner runner = GameTestRunner.Builder.fromInfo(infos, level).newStructureSpawner(new StructureGridSpawner(testPos, testsPerRow, false)).build();
/* 206 */     return trackAndStartRunner(source, runner);
/*     */   }
/*     */   
/*     */   private static int locate(TestFinder finder) throws CommandSyntaxException {
/* 210 */     finder.source().sendSystemMessage(Component.translatable("commands.test.locate.started"));
/*     */     
/* 212 */     MutableInt structuresFound = new MutableInt(0);
/* 213 */     BlockPos sourcePos = BlockPos.containing(finder.source().getPosition());
/* 214 */     finder.findTestPos().forEach(structurePos -> {
/* 215 */           TestInstanceBlockEntity testBlock; BlockEntity patt0$temp = finder.source().getLevel().getBlockEntity(structurePos); if (patt0$temp instanceof TestInstanceBlockEntity) { testBlock = (TestInstanceBlockEntity)patt0$temp; }
/*     */           else
/*     */           { return; }
/*     */           
/* 219 */           Direction facingDirection = testBlock.getRotation().rotate(Direction.NORTH);
/* 220 */           BlockPos telportPosition = testBlock.getBlockPos().relative(facingDirection, 2);
/* 221 */           int teleportYRot = (int)facingDirection.getOpposite().toYRot();
/* 222 */           String tpCommand = String.format(Locale.ROOT, "/tp @s %d %d %d %d 0", new Object[] { Integer.valueOf(telportPosition.getX()), Integer.valueOf(telportPosition.getY()), Integer.valueOf(telportPosition.getZ()), Integer.valueOf(teleportYRot) });
/*     */           
/* 224 */           int dx = sourcePos.getX() - structurePos.getX();
/* 225 */           int dz = sourcePos.getZ() - structurePos.getZ();
/* 226 */           int distance = Mth.floor(Mth.sqrt((dx * dx + dz * dz)));
/*     */           
/* 228 */           MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", new Object[] { Integer.valueOf(structurePos.getX()), Integer.valueOf(structurePos.getY()), Integer.valueOf(structurePos.getZ()) })).withStyle(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 234 */           finder.source().sendSuccess((), false);
/* 235 */           structuresFound.increment();
/*     */         });
/*     */     
/* 238 */     int structures = structuresFound.intValue();
/* 239 */     if (structures == 0) {
/* 240 */       throw NO_TEST_INSTANCES.create();
/*     */     }
/*     */     
/* 243 */     finder.source().sendSuccess(() -> Component.translatable("commands.test.locate.done", new Object[] { Integer.valueOf(structures) }), true);
/* 244 */     return structures;
/*     */   }
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> runWithRetryOptions(ArgumentBuilder<CommandSourceStack, ?> runArgument, InCommandFunction<CommandContext<CommandSourceStack>, TestFinder> finder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> then) {
/* 248 */     return runArgument
/* 249 */       .executes(c -> run((TestFinder)finder.apply(c), RetryOptions.noRetries(), 0, 8))
/* 250 */       .then(((RequiredArgumentBuilder)Commands.argument("numberOfTimes", IntegerArgumentType.integer(0))
/* 251 */         .executes(c -> run((TestFinder)finder.apply(c), new RetryOptions(IntegerArgumentType.getInteger(c, "numberOfTimes"), false), 0, 8)))
/* 252 */         .then((ArgumentBuilder)then
/* 253 */           .apply(Commands.argument("untilFailed", BoolArgumentType.bool())
/* 254 */             .executes(c -> run((TestFinder)finder.apply(c), new RetryOptions(IntegerArgumentType.getInteger(c, "numberOfTimes"), BoolArgumentType.getBool(c, "untilFailed")), 0, 8)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   private static ArgumentBuilder<CommandSourceStack, ?> runWithRetryOptions(ArgumentBuilder<CommandSourceStack, ?> runArgument, InCommandFunction<CommandContext<CommandSourceStack>, TestFinder> finder) { return runWithRetryOptions(runArgument, finder, a -> a); }
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> runWithRetryOptionsAndBuildInfo(ArgumentBuilder<CommandSourceStack, ?> runArgument, InCommandFunction<CommandContext<CommandSourceStack>, TestFinder> finder) {
/* 264 */     return runWithRetryOptions(runArgument, finder, then -> 
/* 265 */         then.then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", IntegerArgumentType.integer())
/* 266 */           .executes(()))
/* 267 */           .then(Commands.argument("testsPerRow", IntegerArgumentType.integer())
/* 268 */             .executes(()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 275 */     ArgumentBuilder<CommandSourceStack, ?> runFailedWithRequiredTestsFlag = runWithRetryOptionsAndBuildInfo(Commands.argument("onlyRequiredTests", BoolArgumentType.bool()), c -> TestFinder.builder().failedTests(c, BoolArgumentType.getBool(c, "onlyRequiredTests")));
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
/*     */     
/* 295 */     Objects.requireNonNull(TestFinder.builder());
/* 296 */     Objects.requireNonNull(TestFinder.builder());
/* 297 */     Objects.requireNonNull(TestFinder.builder());
/* 298 */     Objects.requireNonNull(TestFinder.builder());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     LiteralArgumentBuilder<CommandSourceStack> testCommand = (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("test").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.literal("run").then(runWithRetryOptionsAndBuildInfo(Commands.argument("tests", ResourceSelectorArgument.resourceSelector(context, Registries.TEST_INSTANCE)), c -> TestFinder.builder().byResourceSelection(c, ResourceSelectorArgument.getSelectedResources(c, "tests")))))).then(Commands.literal("runmultiple").then(((RequiredArgumentBuilder)Commands.argument("tests", ResourceSelectorArgument.resourceSelector(context, Registries.TEST_INSTANCE)).executes(c -> run(TestFinder.builder().byResourceSelection(c, ResourceSelectorArgument.getSelectedResources(c, "tests")), RetryOptions.noRetries(), 0, 8))).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(c -> run(TestFinder.builder().createMultipleCopies(IntegerArgumentType.getInteger(c, "amount")).byResourceSelection(c, ResourceSelectorArgument.getSelectedResources(c, "tests")), RetryOptions.noRetries(), 0, 8)))))).then(runWithRetryOptions(Commands.literal("runthese"), TestFinder.builder()::allNearby))).then(runWithRetryOptions(Commands.literal("runclosest"), TestFinder.builder()::nearest))).then(runWithRetryOptions(Commands.literal("runthat"), TestFinder.builder()::lookedAt))).then(runWithRetryOptionsAndBuildInfo(Commands.literal("runfailed").then(runFailedWithRequiredTestsFlag), TestFinder.builder()::failedTests))).then(Commands.literal("verify").then(Commands.argument("tests", ResourceSelectorArgument.resourceSelector(context, Registries.TEST_INSTANCE)).executes(c -> verify(TestFinder.builder().byResourceSelection(c, ResourceSelectorArgument.getSelectedResources(c, "tests"))))))).then(Commands.literal("locate").then(Commands.argument("tests", ResourceSelectorArgument.resourceSelector(context, Registries.TEST_INSTANCE)).executes(c -> locate(TestFinder.builder().byResourceSelection(c, ResourceSelectorArgument.getSelectedResources(c, "tests"))))))).then(Commands.literal("resetclosest").executes(c -> reset(TestFinder.builder().nearest(c))))).then(Commands.literal("resetthese").executes(c -> reset(TestFinder.builder().allNearby(c))))).then(Commands.literal("resetthat").executes(c -> reset(TestFinder.builder().lookedAt(c))))).then(Commands.literal("clearthat").executes(c -> clear(TestFinder.builder().lookedAt(c))))).then(Commands.literal("clearthese").executes(c -> clear(TestFinder.builder().allNearby(c))))).then(((LiteralArgumentBuilder)Commands.literal("clearall").executes(c -> clear(TestFinder.builder().radius(c, 250)))).then(Commands.argument("radius", IntegerArgumentType.integer()).executes(c -> clear(TestFinder.builder().radius(c, Mth.clamp(IntegerArgumentType.getInteger(c, "radius"), 0, 1024))))))).then(Commands.literal("stop").executes(c -> stopTests()))).then(((LiteralArgumentBuilder)Commands.literal("pos").executes(c -> showPos((CommandSourceStack)c.getSource(), "pos"))).then(Commands.argument("var", StringArgumentType.word()).executes(c -> showPos((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "var")))))).then(
/* 327 */         Commands.literal("create")
/* 328 */         .then((
/* 329 */           (RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/* 330 */           .suggests(TestCommand::suggestTestFunction)
/* 331 */           .executes(c -> createNewStructure((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), 5, 5, 5)))
/* 332 */           .then((
/* 333 */             (RequiredArgumentBuilder)Commands.argument("width", IntegerArgumentType.integer())
/* 334 */             .executes(c -> createNewStructure((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), IntegerArgumentType.getInteger(c, "width"), IntegerArgumentType.getInteger(c, "width"), IntegerArgumentType.getInteger(c, "width"))))
/* 335 */             .then(
/* 336 */               Commands.argument("height", IntegerArgumentType.integer())
/* 337 */               .then(
/* 338 */                 Commands.argument("depth", IntegerArgumentType.integer())
/* 339 */                 .executes(c -> createNewStructure((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), IntegerArgumentType.getInteger(c, "width"), IntegerArgumentType.getInteger(c, "height"), IntegerArgumentType.getInteger(c, "depth"))))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 353 */       testCommand = (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)testCommand.then(Commands.literal("export").then(Commands.argument("test", ResourceArgument.resource(context, Registries.TEST_INSTANCE)).executes(c -> exportTestStructure((CommandSourceStack)c.getSource(), ResourceArgument.getResource(c, "test", Registries.TEST_INSTANCE)))))).then(Commands.literal("exportclosest").executes(c -> export(TestFinder.builder().nearest(c))))).then(Commands.literal("exportthese").executes(c -> export(TestFinder.builder().allNearby(c))))).then(Commands.literal("exportthat").executes(c -> export(TestFinder.builder().lookedAt(c))));
/*     */     }
/* 355 */     dispatcher.register(testCommand);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Suggestions> suggestTestFunction(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
/* 359 */     Stream<String> testNamesStream = ((CommandSourceStack)context.getSource()).registryAccess().lookupOrThrow(Registries.TEST_FUNCTION).listElements().map(Holder::getRegisteredName);
/* 360 */     return SharedSuggestionProvider.suggest(testNamesStream, builder);
/*     */   }
/*     */   
/*     */   private static int resetGameTestInfo(CommandSourceStack source, GameTestInfo testInfo) {
/* 364 */     TestInstanceBlockEntity blockEntity = testInfo.getTestInstanceBlockEntity();
/* 365 */     Objects.requireNonNull(source); blockEntity.resetTest(source::sendSystemMessage);
/* 366 */     return 1;
/*     */   }
/*     */   
/*     */   private static Stream<GameTestInfo> toGameTestInfos(CommandSourceStack source, RetryOptions retryOptions, TestPosFinder finder) {
/* 370 */     return finder.findTestPos()
/* 371 */       .map(pos -> createGameTestInfo(pos, source, retryOptions))
/* 372 */       .flatMap(Optional::stream);
/*     */   }
/*     */ 
/*     */   
/* 376 */   private static Stream<GameTestInfo> toGameTestInfo(CommandSourceStack source, RetryOptions retryOptions, TestInstanceFinder finder, int rotationSteps) { return finder.findTests()
/* 377 */       .filter(test -> verifyStructureExists(source, ((GameTestInstance)test.value()).structure()))
/* 378 */       .map(test -> new GameTestInfo(test, StructureUtils.getRotationForRotationSteps(rotationSteps), source.getLevel(), retryOptions)); }
/*     */   
/*     */   private static Optional<GameTestInfo> createGameTestInfo(BlockPos testBlockPos, CommandSourceStack source, RetryOptions retryOptions) {
/*     */     TestInstanceBlockEntity blockEntity;
/* 382 */     ServerLevel level = source.getLevel();
/* 383 */     BlockEntity blockEntity1 = level.getBlockEntity(testBlockPos); if (blockEntity1 instanceof TestInstanceBlockEntity) { blockEntity = (TestInstanceBlockEntity)blockEntity1; }
/* 384 */     else { source.sendFailure(Component.translatable("commands.test.error.test_instance_not_found.position", new Object[] { Integer.valueOf(testBlockPos.getX()), Integer.valueOf(testBlockPos.getY()), Integer.valueOf(testBlockPos.getZ()) }));
/* 385 */       return Optional.empty(); }
/*     */ 
/*     */ 
/*     */     
/* 389 */     Objects.requireNonNull(source.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE)); Optional<Holder.Reference<GameTestInstance>> maybeTest = blockEntity.test().flatMap(source.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE)::get);
/* 390 */     if (maybeTest.isEmpty()) {
/* 391 */       source.sendFailure(Component.translatable("commands.test.error.non_existant_test", new Object[] { blockEntity.getTestName() }));
/* 392 */       return Optional.empty();
/*     */     } 
/*     */     
/* 395 */     Holder.Reference<GameTestInstance> test = (Holder.Reference)maybeTest.get();
/* 396 */     GameTestInfo testInfo = new GameTestInfo(test, blockEntity.getRotation(), level, retryOptions);
/* 397 */     testInfo.setTestBlockPos(testBlockPos);
/*     */     
/* 399 */     if (!verifyStructureExists(source, testInfo.getStructure())) {
/* 400 */       return Optional.empty();
/*     */     }
/*     */     
/* 403 */     return Optional.of(testInfo);
/*     */   }
/*     */   
/*     */   private static int createNewStructure(CommandSourceStack source, Identifier id, int xSize, int ySize, int zSize) throws CommandSyntaxException {
/* 407 */     if (xSize > 48 || ySize > 48 || zSize > 48) {
/* 408 */       throw TOO_LARGE.create(Integer.valueOf(48));
/*     */     }
/*     */     
/* 411 */     ServerLevel level = source.getLevel();
/* 412 */     BlockPos testPos = createTestPositionAround(source);
/*     */     
/* 414 */     TestInstanceBlockEntity test = StructureUtils.createNewEmptyTest(id, testPos, new Vec3i(xSize, ySize, zSize), Rotation.NONE, level);
/*     */     
/* 416 */     BlockPos low = test.getStructurePos();
/* 417 */     BlockPos high = low.offset(xSize - 1, 0, zSize - 1);
/* 418 */     BlockPos.betweenClosedStream(low, high).forEach(blockPos -> 
/* 419 */         level.setBlockAndUpdate(blockPos, Blocks.BEDROCK.defaultBlockState()));
/*     */ 
/*     */     
/* 422 */     source.sendSuccess(() -> Component.translatable("commands.test.create.success", new Object[] { test.getTestName() }), true);
/* 423 */     return 1;
/*     */   }
/*     */   private static int showPos(CommandSourceStack source, String varName) throws CommandSyntaxException {
/*     */     TestInstanceBlockEntity testBlockEntity;
/* 427 */     ServerPlayer player = source.getPlayerOrException();
/* 428 */     BlockHitResult pick = (BlockHitResult)player.pick(10.0D, 1.0F, false);
/*     */     
/* 430 */     BlockPos targetPosAbsolute = pick.getBlockPos();
/* 431 */     ServerLevel level = source.getLevel();
/*     */     
/* 433 */     Optional<BlockPos> testBlockPos = StructureUtils.findTestContainingPos(targetPosAbsolute, 15, level);
/* 434 */     if (testBlockPos.isEmpty())
/*     */     {
/* 436 */       testBlockPos = StructureUtils.findTestContainingPos(targetPosAbsolute, 250, level);
/*     */     }
/*     */     
/* 439 */     if (testBlockPos.isEmpty()) {
/* 440 */       throw NO_TEST_CONTAINING.create(Integer.valueOf(targetPosAbsolute.getX()), Integer.valueOf(targetPosAbsolute.getY()), Integer.valueOf(targetPosAbsolute.getZ()));
/*     */     }
/* 442 */     BlockEntity blockEntity = level.getBlockEntity((BlockPos)testBlockPos.get()); if (blockEntity instanceof TestInstanceBlockEntity) { testBlockEntity = (TestInstanceBlockEntity)blockEntity; }
/* 443 */     else { throw TEST_INSTANCE_COULD_NOT_BE_FOUND.create(); }
/*     */ 
/*     */     
/* 446 */     BlockPos testOrigin = testBlockEntity.getStructurePos();
/* 447 */     BlockPos targetPosRelative = targetPosAbsolute.subtract(testOrigin);
/* 448 */     String targetPosDescription = "" + targetPosRelative.getX() + ", " + targetPosRelative.getX() + ", " + targetPosRelative.getY();
/* 449 */     String testName = testBlockEntity.getTestName().getString();
/*     */ 
/*     */     
/* 452 */     MutableComponent mutableComponent = Component.translatable("commands.test.coordinates", new Object[] { Integer.valueOf(targetPosRelative.getX()), Integer.valueOf(targetPosRelative.getY()), Integer.valueOf(targetPosRelative.getZ()) }).setStyle(Style.EMPTY
/* 453 */         .withBold(Boolean.valueOf(true))
/* 454 */         .withColor(ChatFormatting.GREEN)
/* 455 */         .withHoverEvent(new HoverEvent.ShowText(Component.translatable("commands.test.coordinates.copy")))
/* 456 */         .withClickEvent(new ClickEvent.CopyToClipboard("final BlockPos " + varName + " = new BlockPos(" + targetPosDescription + ");")));
/*     */     
/* 458 */     source.sendSuccess(() -> Component.translatable("commands.test.relative_position", new Object[] { testName, coords }), false);
/*     */     
/* 460 */     player.connection.send(new ClientboundGameTestHighlightPosPacket(targetPosAbsolute, targetPosRelative));
/*     */     
/* 462 */     return 1;
/*     */   }
/*     */   
/*     */   private static int stopTests() {
/* 466 */     GameTestTicker.SINGLETON.clear();
/* 467 */     return 1;
/*     */   }
/*     */   
/*     */   public static int trackAndStartRunner(CommandSourceStack source, GameTestRunner runner) {
/* 471 */     runner.addListener(new TestBatchSummaryDisplayer(source));
/* 472 */     MultipleTestTracker tracker = new MultipleTestTracker(runner.getTestInfos());
/* 473 */     tracker.addListener(new TestSummaryDisplayer(source, tracker));
/* 474 */     tracker.addFailureListener(testInfo -> FailedTestTracker.rememberFailedTest(testInfo.getTestHolder()));
/* 475 */     runner.start();
/*     */     
/* 477 */     return 1;
/*     */   }
/*     */   
/*     */   private static int exportTestStructure(CommandSourceStack source, Holder<GameTestInstance> test) {
/* 481 */     Objects.requireNonNull(source); if (!TestInstanceBlockEntity.export(source.getLevel(), ((GameTestInstance)test.value()).structure(), source::sendSystemMessage)) {
/* 482 */       return 0;
/*     */     }
/* 484 */     return 1;
/*     */   }
/*     */   
/*     */   private static boolean verifyStructureExists(CommandSourceStack source, Identifier structure) {
/* 488 */     if (source.getLevel().getStructureManager().get(structure).isEmpty()) {
/* 489 */       source.sendFailure(Component.translatable("commands.test.error.structure_not_found", new Object[] { Component.translationArg(structure) }));
/* 490 */       return false;
/*     */     } 
/* 492 */     return true;
/*     */   }
/*     */   
/*     */   private static BlockPos createTestPositionAround(CommandSourceStack source) {
/* 496 */     BlockPos playerPos = BlockPos.containing(source.getPosition());
/* 497 */     int surfaceY = source.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, playerPos).getY();
/* 498 */     return new BlockPos(playerPos.getX(), surfaceY, playerPos.getZ() + 3);
/*     */   }
/*     */   public static final class TestSummaryDisplayer extends Record implements GameTestListener { private final CommandSourceStack source; private final MultipleTestTracker tracker;
/* 501 */     public TestSummaryDisplayer(CommandSourceStack source, MultipleTestTracker tracker) { this.source = source; this.tracker = tracker; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 501 */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer; } public CommandSourceStack source() { return this.source; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #501	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;
/* 501 */       //   0	8	1	o	Ljava/lang/Object; } public MultipleTestTracker tracker() { return this.tracker; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */ 
/*     */     
/* 508 */     public void testPassed(GameTestInfo testInfo, GameTestRunner runner) { showTestSummaryIfAllDone(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 513 */     public void testFailed(GameTestInfo testInfo, GameTestRunner runner) { showTestSummaryIfAllDone(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) { this.tracker.addTestToTrack(copy); }
/*     */ 
/*     */     
/*     */     private void showTestSummaryIfAllDone() {
/* 522 */       if (this.tracker.isDone()) {
/* 523 */         this.source.sendSuccess(() -> Component.translatable("commands.test.summary", new Object[] { Integer.valueOf(this.tracker.getTotalCount()) }).withStyle(ChatFormatting.WHITE), true);
/* 524 */         if (this.tracker.hasFailedRequired()) {
/* 525 */           this.source.sendFailure(Component.translatable("commands.test.summary.failed", new Object[] { Integer.valueOf(this.tracker.getFailedRequiredCount()) }));
/*     */         } else {
/* 527 */           this.source.sendSuccess(() -> Component.translatable("commands.test.summary.all_required_passed").withStyle(ChatFormatting.GREEN), true);
/*     */         } 
/* 529 */         if (this.tracker.hasFailedOptional())
/* 530 */           this.source.sendSystemMessage(Component.translatable("commands.test.summary.optional_failed", new Object[] { Integer.valueOf(this.tracker.getFailedOptionalCount()) })); 
/*     */       } 
/*     */     } }
/*     */   
/*     */   private static final class TestBatchSummaryDisplayer extends Record implements GameTestBatchListener { private final CommandSourceStack source;
/*     */     
/* 536 */     private TestBatchSummaryDisplayer(CommandSourceStack source) { this.source = source; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #536	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #536	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #536	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;
/* 536 */       //   0	8	1	o	Ljava/lang/Object; } public CommandSourceStack source() { return this.source; }
/*     */ 
/*     */     
/* 539 */     public void testBatchStarting(GameTestBatch batch) { this.source.sendSuccess(() -> Component.translatable("commands.test.batch.starting", new Object[] { batch.environment().getRegisteredName(), Integer.valueOf(batch.index()) }), true); }
/*     */     
/*     */     public void testBatchFinished(GameTestBatch batch) {} }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */