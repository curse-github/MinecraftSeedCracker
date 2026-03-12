/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class GameTestInfo
/*     */ {
/*     */   private final Holder.Reference<GameTestInstance> test;
/*     */   private BlockPos testBlockPos;
/*     */   private final ServerLevel level;
/*     */   private final Collection<GameTestListener> listeners;
/*     */   private final int timeoutTicks;
/*     */   private final Collection<GameTestSequence> sequences;
/*     */   private final Object2LongMap<Runnable> runAtTickTimeMap;
/*     */   private boolean placedStructure;
/*     */   
/*     */   public GameTestInfo(Holder.Reference<GameTestInstance> test, Rotation extraRotation, ServerLevel level, RetryOptions retryOptions) {
/*  42 */     this.listeners = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*  46 */     this.sequences = Lists.newCopyOnWriteArrayList();
/*  47 */     this.runAtTickTimeMap = new Object2LongOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.timer = Stopwatch.createUnstarted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.test = test;
/*  64 */     this.level = level;
/*  65 */     this.retryOptions = retryOptions;
/*  66 */     this.timeoutTicks = ((GameTestInstance)test.value()).maxTicks();
/*  67 */     this.extraRotation = extraRotation;
/*     */   }
/*     */   private boolean chunksLoaded; private int tickCount; private boolean started; private final RetryOptions retryOptions; private final Stopwatch timer; private boolean done; private final Rotation extraRotation; private GameTestException error; private TestInstanceBlockEntity testInstanceBlockEntity;
/*     */   
/*  71 */   public void setTestBlockPos(BlockPos testBlockPos) { this.testBlockPos = testBlockPos; }
/*     */ 
/*     */   
/*     */   public GameTestInfo startExecution(int tickDelay) {
/*  75 */     this.tickCount = -(((GameTestInstance)this.test.value()).setupTicks() + tickDelay + 1);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public void placeStructure() {
/*  80 */     if (this.placedStructure) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     TestInstanceBlockEntity test = getTestInstanceBlockEntity();
/*  85 */     if (!test.placeStructure()) {
/*  86 */       fail(Component.translatable("test.error.structure.failure", new Object[] { test.getTestName().getString() }));
/*     */     }
/*     */     
/*  89 */     this.placedStructure = true;
/*     */     
/*  91 */     test.encaseStructure();
/*  92 */     BoundingBox boundingBox = test.getStructureBoundingBox();
/*  93 */     this.level.getBlockTicks().clearArea(boundingBox);
/*  94 */     this.level.clearBlockEvents(boundingBox);
/*  95 */     this.listeners.forEach(listener -> listener.testStructureLoaded(this));
/*     */   }
/*     */   
/*     */   public void tick(GameTestRunner runner) {
/*  99 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     if (!this.placedStructure) {
/* 104 */       fail(Component.translatable("test.error.ticking_without_structure"));
/*     */     }
/*     */     
/* 107 */     if (this.testInstanceBlockEntity == null) {
/* 108 */       fail(Component.translatable("test.error.missing_block_entity"));
/*     */     }
/*     */     
/* 111 */     if (this.error != null) {
/* 112 */       finish();
/*     */     }
/*     */ 
/*     */     
/* 116 */     Objects.requireNonNull(this.level); if (!this.chunksLoaded && !this.testInstanceBlockEntity.getStructureBoundingBox().intersectingChunks().allMatch(this.level::areEntitiesActuallyLoadedAndTicking)) {
/*     */       return;
/*     */     }
/* 119 */     this.chunksLoaded = true;
/*     */ 
/*     */     
/* 122 */     tickInternal();
/*     */     
/* 124 */     if (isDone()) {
/* 125 */       if (this.error != null) {
/* 126 */         this.listeners.forEach(listener -> listener.testFailed(this, runner));
/*     */       } else {
/* 128 */         this.listeners.forEach(listener -> listener.testPassed(this, runner));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void tickInternal() {
/* 134 */     this.tickCount++;
/* 135 */     if (this.tickCount < 0) {
/*     */       return;
/*     */     }
/* 138 */     if (!this.started) {
/* 139 */       startTest();
/*     */     }
/* 141 */     ObjectIterator<Object2LongMap.Entry<Runnable>> it = this.runAtTickTimeMap.object2LongEntrySet().iterator();
/* 142 */     while (it.hasNext()) {
/* 143 */       Object2LongMap.Entry<Runnable> entry = (Object2LongMap.Entry)it.next();
/* 144 */       if (entry.getLongValue() <= this.tickCount) {
/*     */         try {
/* 146 */           ((Runnable)entry.getKey()).run();
/* 147 */         } catch (GameTestException error) {
/* 148 */           fail(error);
/* 149 */         } catch (Exception exception) {
/* 150 */           fail(new UnknownGameTestException(exception));
/*     */         } 
/* 152 */         it.remove();
/*     */       } 
/*     */     } 
/* 155 */     if (this.tickCount > this.timeoutTicks) {
/*     */       
/* 157 */       if (this.sequences.isEmpty()) {
/* 158 */         fail(new GameTestTimeoutException(Component.translatable("test.error.timeout.no_result", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) })));
/*     */       } else {
/* 160 */         this.sequences.forEach(ticker -> ticker.tickAndFailIfNotComplete(this.tickCount));
/* 161 */         if (this.error == null) {
/* 162 */           fail(new GameTestTimeoutException(Component.translatable("test.error.timeout.no_sequences_finished", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) })));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 167 */       this.sequences.forEach(ticker -> ticker.tickAndContinue(this.tickCount));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startTest() {
/* 172 */     if (this.started) {
/*     */       return;
/*     */     }
/* 175 */     this.started = true;
/* 176 */     this.timer.start();
/* 177 */     getTestInstanceBlockEntity().setRunning();
/*     */     try {
/* 179 */       ((GameTestInstance)this.test.value()).run(new GameTestHelper(this));
/* 180 */     } catch (GameTestException e) {
/* 181 */       fail(e);
/* 182 */     } catch (Exception e) {
/* 183 */       fail(new UnknownGameTestException(e));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 188 */   public void setRunAtTickTime(long time, Runnable assertAtTickTime) { this.runAtTickTimeMap.put(assertAtTickTime, time); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public Identifier id() { return this.test.key().identifier(); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public BlockPos getTestBlockPos() { return this.testBlockPos; }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public BlockPos getTestOrigin() { return this.testInstanceBlockEntity.getStartCorner(); }
/*     */ 
/*     */   
/*     */   public AABB getStructureBounds() {
/* 204 */     TestInstanceBlockEntity blockEntity = getTestInstanceBlockEntity();
/* 205 */     return blockEntity.getStructureBounds();
/*     */   }
/*     */   
/*     */   public TestInstanceBlockEntity getTestInstanceBlockEntity() {
/* 209 */     if (this.testInstanceBlockEntity == null) {
/* 210 */       if (this.testBlockPos == null) {
/* 211 */         throw new IllegalStateException("This GameTestInfo has no position");
/*     */       }
/*     */       
/* 214 */       BlockEntity blockEntity1 = this.level.getBlockEntity(this.testBlockPos); if (blockEntity1 instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity blockEntity = (TestInstanceBlockEntity)blockEntity1;
/* 215 */         this.testInstanceBlockEntity = blockEntity; }
/*     */       
/* 217 */       if (this.testInstanceBlockEntity == null) {
/* 218 */         throw new IllegalStateException("Could not find a test instance block entity at the given coordinate " + String.valueOf(this.testBlockPos));
/*     */       }
/*     */     } 
/*     */     
/* 222 */     return this.testInstanceBlockEntity;
/*     */   }
/*     */ 
/*     */   
/* 226 */   public ServerLevel getLevel() { return this.level; }
/*     */ 
/*     */ 
/*     */   
/* 230 */   public boolean hasSucceeded() { return (this.done && this.error == null); }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public boolean hasFailed() { return (this.error != null); }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public boolean hasStarted() { return this.started; }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public boolean isDone() { return this.done; }
/*     */ 
/*     */ 
/*     */   
/* 246 */   public long getRunTime() { return this.timer.elapsed(TimeUnit.MILLISECONDS); }
/*     */ 
/*     */   
/*     */   private void finish() {
/* 250 */     if (!this.done) {
/* 251 */       this.done = true;
/* 252 */       if (this.timer.isRunning()) {
/* 253 */         this.timer.stop();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void succeed() {
/* 260 */     if (this.error == null) {
/* 261 */       finish();
/* 262 */       AABB bounds = getStructureBounds();
/* 263 */       List<Entity> entities = getLevel().getEntitiesOfClass(Entity.class, bounds.inflate(1.0D), mob -> !(mob instanceof net.minecraft.world.entity.player.Player));
/* 264 */       entities.forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 269 */   public void fail(Component message) { fail(new GameTestAssertException(message, this.tickCount)); }
/*     */ 
/*     */ 
/*     */   
/* 273 */   public void fail(GameTestException error) { this.error = error; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   public GameTestException getError() { return this.error; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   public String toString() { return id().toString(); }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public void addListener(GameTestListener listener) { this.listeners.add(listener); }
/*     */ 
/*     */   
/*     */   public GameTestInfo prepareTestStructure() {
/* 293 */     TestInstanceBlockEntity testInstanceBlock = createTestInstanceBlock((BlockPos)Objects.requireNonNull(this.testBlockPos), this.extraRotation, this.level);
/* 294 */     if (testInstanceBlock != null) {
/* 295 */       this.testInstanceBlockEntity = testInstanceBlock;
/* 296 */       placeStructure();
/* 297 */       return this;
/*     */     } 
/* 299 */     return null;
/*     */   }
/*     */   
/*     */   private TestInstanceBlockEntity createTestInstanceBlock(BlockPos testPos, Rotation rotation, ServerLevel level) {
/* 303 */     level.setBlockAndUpdate(testPos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/*     */     
/* 305 */     BlockEntity blockEntity1 = level.getBlockEntity(testPos); if (blockEntity1 instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity blockEntity = (TestInstanceBlockEntity)blockEntity1;
/* 306 */       ResourceKey<GameTestInstance> test = getTestHolder().key();
/* 307 */       Vec3i size = (Vec3i)TestInstanceBlockEntity.getStructureSize(level, test).orElse(new Vec3i(1, 1, 1));
/* 308 */       blockEntity.set(new TestInstanceBlockEntity.Data(Optional.of(test), size, rotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/* 309 */       return blockEntity; }
/*     */     
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 315 */   int getTick() { return this.tickCount; }
/*     */ 
/*     */   
/*     */   GameTestSequence createSequence() {
/* 319 */     GameTestSequence sequence = new GameTestSequence(this);
/* 320 */     this.sequences.add(sequence);
/* 321 */     return sequence;
/*     */   }
/*     */ 
/*     */   
/* 325 */   public boolean isRequired() { return ((GameTestInstance)this.test.value()).required(); }
/*     */ 
/*     */ 
/*     */   
/* 329 */   public boolean isOptional() { return !((GameTestInstance)this.test.value()).required(); }
/*     */ 
/*     */ 
/*     */   
/* 333 */   public Identifier getStructure() { return ((GameTestInstance)this.test.value()).structure(); }
/*     */ 
/*     */ 
/*     */   
/* 337 */   public Rotation getRotation() { return ((GameTestInstance)this.test.value()).info().rotation().getRotated(this.extraRotation); }
/*     */ 
/*     */ 
/*     */   
/* 341 */   public GameTestInstance getTest() { return (GameTestInstance)this.test.value(); }
/*     */ 
/*     */ 
/*     */   
/* 345 */   public Holder.Reference<GameTestInstance> getTestHolder() { return this.test; }
/*     */ 
/*     */ 
/*     */   
/* 349 */   public int getTimeoutTicks() { return this.timeoutTicks; }
/*     */ 
/*     */ 
/*     */   
/* 353 */   public boolean isFlaky() { return (((GameTestInstance)this.test.value()).maxAttempts() > 1); }
/*     */ 
/*     */ 
/*     */   
/* 357 */   public int maxAttempts() { return ((GameTestInstance)this.test.value()).maxAttempts(); }
/*     */ 
/*     */ 
/*     */   
/* 361 */   public int requiredSuccesses() { return ((GameTestInstance)this.test.value()).requiredSuccesses(); }
/*     */ 
/*     */ 
/*     */   
/* 365 */   public RetryOptions retryOptions() { return this.retryOptions; }
/*     */ 
/*     */ 
/*     */   
/* 369 */   public Stream<GameTestListener> getListeners() { return this.listeners.stream(); }
/*     */ 
/*     */   
/*     */   public GameTestInfo copyReset() {
/* 373 */     GameTestInfo i = new GameTestInfo(this.test, this.extraRotation, this.level, retryOptions());
/* 374 */     if (this.testBlockPos != null) {
/* 375 */       i.setTestBlockPos(this.testBlockPos);
/*     */     }
/* 377 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */