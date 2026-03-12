/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.gametest.framework.FailedTestTracker;
/*     */ import net.minecraft.gametest.framework.GameTestInfo;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.GameTestRunner;
/*     */ import net.minecraft.gametest.framework.GameTestTicker;
/*     */ import net.minecraft.gametest.framework.RetryOptions;
/*     */ import net.minecraft.gametest.framework.StructureUtils;
/*     */ import net.minecraft.gametest.framework.TestCommand;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class TestInstanceBlockEntity extends BlockEntity implements BoundingBoxRenderable, BeaconBeamOwner {
/*  59 */   private static final Component INVALID_TEST_NAME = Component.translatable("test_instance_block.invalid_test");
/*  60 */   private static final List<BeaconBeamOwner.Section> BEAM_CLEARED = List.of();
/*  61 */   private static final List<BeaconBeamOwner.Section> BEAM_RUNNING = List.of(new BeaconBeamOwner.Section(ARGB.color(128, 128, 128)));
/*  62 */   private static final List<BeaconBeamOwner.Section> BEAM_SUCCESS = List.of(new BeaconBeamOwner.Section(ARGB.color(0, 255, 0)));
/*  63 */   private static final List<BeaconBeamOwner.Section> BEAM_REQUIRED_FAILED = List.of(new BeaconBeamOwner.Section(ARGB.color(255, 0, 0)));
/*  64 */   private static final List<BeaconBeamOwner.Section> BEAM_OPTIONAL_FAILED = List.of(new BeaconBeamOwner.Section(ARGB.color(255, 128, 0)));
/*     */   
/*     */   public enum Status implements StringRepresentable {
/*  67 */     CLEARED("cleared", 0),
/*  68 */     RUNNING("running", 1),
/*  69 */     FINISHED("finished", 2); private static final IntFunction<Status> ID_MAP; public static final Codec<Status> CODEC; public static final StreamCodec<ByteBuf, Status> STREAM_CODEC; private final String id; private final int index;
/*     */     static  {
/*  71 */       ID_MAP = ByIdMap.continuous(s -> s.index, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  72 */       CODEC = StringRepresentable.fromEnum(Status::values);
/*  73 */       STREAM_CODEC = ByteBufCodecs.idMapper(Status::byIndex, s -> s.index);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Status(String id, int index) {
/*  79 */       this.id = id;
/*  80 */       this.index = index;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     public String getSerializedName() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/*  89 */     public static Status byIndex(int index) { return (Status)ID_MAP.apply(index); } }
/*     */   public static final class Data extends Record { private final Optional<ResourceKey<GameTestInstance>> test; private final Vec3i size; private final Rotation rotation; private final boolean ignoreEntities; private final TestInstanceBlockEntity.Status status;
/*     */     private final Optional<Component> errorMessage;
/*     */     
/*  93 */     public Data(Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities, TestInstanceBlockEntity.Status status, Optional<Component> errorMessage) { this.test = test; this.size = size; this.rotation = rotation; this.ignoreEntities = ignoreEntities; this.status = status; this.errorMessage = errorMessage; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*  93 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ResourceKey<GameTestInstance>> test() { return this.test; } public Vec3i size() { return this.size; } public Rotation rotation() { return this.rotation; } public boolean ignoreEntities() { return this.ignoreEntities; } public TestInstanceBlockEntity.Status status() { return this.status; } public Optional<Component> errorMessage() { return this.errorMessage; }
/*  94 */     public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  95 */           ResourceKey.codec(Registries.TEST_INSTANCE).optionalFieldOf("test").forGetter(Data::test), Vec3i.CODEC
/*  96 */           .fieldOf("size").forGetter(Data::size), Rotation.CODEC
/*  97 */           .fieldOf("rotation").forGetter(Data::rotation), Codec.BOOL
/*  98 */           .fieldOf("ignore_entities").forGetter(Data::ignoreEntities), TestInstanceBlockEntity.Status.CODEC
/*  99 */           .fieldOf("status").forGetter(Data::status), ComponentSerialization.CODEC
/* 100 */           .optionalFieldOf("error_message").forGetter(Data::errorMessage))
/* 101 */         .apply(i, Data::new));
/* 102 */     public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
/* 103 */         ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.TEST_INSTANCE)), Data::test, Vec3i.STREAM_CODEC, Data::size, Rotation.STREAM_CODEC, Data::rotation, ByteBufCodecs.BOOL, Data::ignoreEntities, TestInstanceBlockEntity.Status.STREAM_CODEC, Data::status, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 108 */         ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), Data::errorMessage, Data::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     public Data withSize(Vec3i size) { return new Data(this.test, size, this.rotation, this.ignoreEntities, this.status, this.errorMessage); }
/*     */ 
/*     */ 
/*     */     
/* 117 */     public Data withStatus(TestInstanceBlockEntity.Status status) { return new Data(this.test, this.size, this.rotation, this.ignoreEntities, status, Optional.empty()); }
/*     */ 
/*     */ 
/*     */     
/* 121 */     public Data withError(Component error) { return new Data(this.test, this.size, this.rotation, this.ignoreEntities, TestInstanceBlockEntity.Status.FINISHED, Optional.of(error)); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static final Vec3i STRUCTURE_OFFSET = new Vec3i(0, 1, 1);
/*     */   
/*     */   private Data data;
/* 129 */   private final List<ErrorMarker> errorMarkers = new ArrayList();
/*     */   
/*     */   public TestInstanceBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 132 */     super(BlockEntityType.TEST_INSTANCE_BLOCK, worldPosition, blockState);
/* 133 */     this.data = new Data(Optional.empty(), Vec3i.ZERO, Rotation.NONE, false, Status.CLEARED, Optional.empty());
/*     */   }
/*     */   
/*     */   public void set(Data data) {
/* 137 */     this.data = data;
/* 138 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/* 142 */   public static Optional<Vec3i> getStructureSize(ServerLevel level, ResourceKey<GameTestInstance> testKey) { return getStructureTemplate(level, testKey).map(StructureTemplate::getSize); }
/*     */ 
/*     */   
/*     */   public BoundingBox getStructureBoundingBox() {
/* 146 */     BlockPos corner1 = getStructurePos();
/* 147 */     BlockPos corner2 = corner1.offset(getTransformedSize()).offset(-1, -1, -1);
/*     */     
/* 149 */     return BoundingBox.fromCorners(corner1, corner2);
/*     */   }
/*     */ 
/*     */   
/* 153 */   public AABB getStructureBounds() { return AABB.of(getStructureBoundingBox()); }
/*     */ 
/*     */ 
/*     */   
/* 157 */   private static Optional<StructureTemplate> getStructureTemplate(ServerLevel level, ResourceKey<GameTestInstance> testKey) { return level.registryAccess().get(testKey)
/* 158 */       .map(test -> ((GameTestInstance)test.value()).structure())
/* 159 */       .flatMap(template -> level.getStructureManager().get(template)); }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public Optional<ResourceKey<GameTestInstance>> test() { return this.data.test(); }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public Component getTestName() { return (Component)test().map(key -> Component.literal(key.identifier().toString())).orElse(INVALID_TEST_NAME); }
/*     */ 
/*     */ 
/*     */   
/* 171 */   private Optional<Holder.Reference<GameTestInstance>> getTestHolder() { Objects.requireNonNull(this.level.registryAccess()); return test().flatMap(this.level.registryAccess()::get); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public boolean ignoreEntities() { return this.data.ignoreEntities(); }
/*     */ 
/*     */ 
/*     */   
/* 179 */   public Vec3i getSize() { return this.data.size(); }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public Rotation getRotation() { return ((Rotation)getTestHolder()
/* 184 */       .map(Holder::value)
/* 185 */       .map(GameTestInstance::rotation)
/* 186 */       .orElse(Rotation.NONE))
/* 187 */       .getRotated(this.data.rotation()); }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public Optional<Component> errorMessage() { return this.data.errorMessage(); }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public void setErrorMessage(Component errorMessage) { set(this.data.withError(errorMessage)); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public void setSuccess() { set(this.data.withStatus(Status.FINISHED)); }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public void setRunning() { set(this.data.withStatus(Status.RUNNING)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 208 */     super.setChanged();
/* 209 */     if (this.level instanceof ServerLevel) {
/* 210 */       this.level.sendBlockUpdated(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState(), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 216 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 226 */     input.read("data", Data.CODEC).ifPresent(this::set);
/*     */     
/* 228 */     this.errorMarkers.clear();
/* 229 */     this.errorMarkers.addAll((Collection)input.read("errors", ErrorMarker.LIST_CODEC).orElse(List.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 234 */     output.store("data", Data.CODEC, this.data);
/* 235 */     if (!this.errorMarkers.isEmpty()) {
/* 236 */       output.store("errors", ErrorMarker.LIST_CODEC, this.errorMarkers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public BoundingBoxRenderable.Mode renderMode() { return BoundingBoxRenderable.Mode.BOX; }
/*     */ 
/*     */ 
/*     */   
/* 246 */   public BlockPos getStructurePos() { return getStructurePos(getBlockPos()); }
/*     */ 
/*     */ 
/*     */   
/* 250 */   public static BlockPos getStructurePos(BlockPos blockPos) { return blockPos.offset(STRUCTURE_OFFSET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public BoundingBoxRenderable.RenderableBox getRenderableBox() { return new BoundingBoxRenderable.RenderableBox(new BlockPos(STRUCTURE_OFFSET), getTransformedSize()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BeaconBeamOwner.Section> getBeamSections() {
/* 260 */     switch (this.data.status().ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*     */ 
/*     */       
/* 263 */       errorMessage().isEmpty() ? 
/* 264 */       BEAM_SUCCESS : (
/* 265 */       ((Boolean)getTestHolder().map(Holder::value).map(GameTestInstance::required).orElse(Boolean.valueOf(true))).booleanValue() ? 
/* 266 */       BEAM_REQUIRED_FAILED : 
/* 267 */       BEAM_OPTIONAL_FAILED);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3i getTransformedSize() {
/* 272 */     Vec3i size = getSize();
/* 273 */     Rotation rotation = getRotation();
/* 274 */     boolean axesSwitched = (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90);
/* 275 */     int xSize = axesSwitched ? size.getZ() : size.getX();
/* 276 */     int zSize = axesSwitched ? size.getX() : size.getZ();
/* 277 */     return new Vec3i(xSize, size.getY(), zSize);
/*     */   }
/*     */   
/*     */   public void resetTest(Consumer<Component> feedbackOutput) {
/* 281 */     removeBarriers();
/* 282 */     clearErrorMarkers();
/* 283 */     boolean placed = placeStructure();
/* 284 */     if (placed) {
/* 285 */       feedbackOutput.accept(Component.translatable("test_instance_block.reset_success", new Object[] { getTestName() }).withStyle(ChatFormatting.GREEN));
/*     */     }
/* 287 */     set(this.data.withStatus(Status.CLEARED));
/*     */   }
/*     */   
/*     */   public Optional<Identifier> saveTest(Consumer<Component> feedbackOutput) {
/* 291 */     Optional<Identifier> identifier, test = getTestHolder();
/*     */     
/* 293 */     if (test.isPresent()) {
/* 294 */       identifier = Optional.of(((GameTestInstance)((Holder.Reference)test.get()).value()).structure());
/*     */     } else {
/*     */       
/* 297 */       identifier = test().map(ResourceKey::identifier);
/*     */     } 
/* 299 */     if (identifier.isEmpty()) {
/* 300 */       BlockPos pos = getBlockPos();
/* 301 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.unable_to_save", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/* 302 */       return identifier;
/*     */     } 
/* 304 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 305 */       StructureBlockEntity.saveStructure(serverLevel, (Identifier)identifier.get(), getStructurePos(), getSize(), ignoreEntities(), "", true, List.of(Blocks.AIR)); }
/*     */     
/* 307 */     return identifier;
/*     */   }
/*     */   public boolean exportTest(Consumer<Component> feedbackOutput) {
/*     */     ServerLevel serverLevel;
/* 311 */     Optional<Identifier> saved = saveTest(feedbackOutput);
/* 312 */     if (!saved.isEmpty()) { Level level = this.level; if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 313 */       else { return false; }  } else { return false; }
/*     */     
/* 315 */     return export(serverLevel, (Identifier)saved.get(), feedbackOutput);
/*     */   }
/*     */   
/*     */   public static boolean export(ServerLevel level, Identifier structureId, Consumer<Component> feedbackOutput) {
/* 319 */     Path outputDir = StructureUtils.testStructuresDir;
/*     */     
/* 321 */     Path inputFile = level.getStructureManager().createAndValidatePathToGeneratedStructure(structureId, ".nbt");
/* 322 */     Path outputFile = NbtToSnbt.convertStructure(CachedOutput.NO_CACHE, inputFile, structureId.getPath(), outputDir.resolve(structureId.getNamespace()).resolve("structure"));
/* 323 */     if (outputFile == null) {
/*     */       
/* 325 */       feedbackOutput.accept(Component.literal("Failed to export " + String.valueOf(inputFile)).withStyle(ChatFormatting.RED));
/* 326 */       return true;
/*     */     } 
/*     */     
/*     */     try {
/* 330 */       FileUtil.createDirectoriesSafe(outputFile.getParent());
/* 331 */     } catch (IOException e) {
/* 332 */       feedbackOutput.accept(Component.literal("Could not create folder " + String.valueOf(outputFile.getParent())).withStyle(ChatFormatting.RED));
/* 333 */       return true;
/*     */     } 
/*     */     
/* 336 */     feedbackOutput.accept(Component.literal("Exported " + String.valueOf(structureId) + " to " + String.valueOf(outputFile.toAbsolutePath())));
/* 337 */     return false;
/*     */   }
/*     */   public void runTest(Consumer<Component> feedbackOutput) {
/*     */     ServerLevel serverLevel;
/* 341 */     Level level = this.level; if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/* 344 */      Optional<Holder.Reference<GameTestInstance>> test = getTestHolder();
/* 345 */     BlockPos pos = getBlockPos();
/* 346 */     if (test.isEmpty()) {
/* 347 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.no_test", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/*     */       
/*     */       return;
/*     */     } 
/* 351 */     if (!placeStructure()) {
/* 352 */       feedbackOutput.accept(Component.translatable("test_instance_block.error.no_test_structure", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()) }).withStyle(ChatFormatting.RED));
/*     */       
/*     */       return;
/*     */     } 
/* 356 */     clearErrorMarkers();
/* 357 */     GameTestTicker.SINGLETON.clear();
/* 358 */     FailedTestTracker.forgetFailedTests();
/* 359 */     feedbackOutput.accept(Component.translatable("test_instance_block.starting", new Object[] { ((Holder.Reference)test.get()).getRegisteredName() }));
/*     */     
/* 361 */     GameTestInfo gameTestInfo = new GameTestInfo((Holder.Reference)test.get(), this.data.rotation(), serverLevel, RetryOptions.noRetries());
/* 362 */     gameTestInfo.setTestBlockPos(pos);
/*     */     
/* 364 */     GameTestRunner runner = GameTestRunner.Builder.fromInfo(List.of(gameTestInfo), serverLevel).build();
/* 365 */     TestCommand.trackAndStartRunner(serverLevel.getServer().createCommandSourceStack(), runner);
/*     */   }
/*     */   
/*     */   public boolean placeStructure() {
/* 369 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 370 */       Optional<StructureTemplate> template = this.data.test().flatMap(test -> getStructureTemplate(serverLevel, test));
/* 371 */       if (template.isPresent()) {
/* 372 */         placeStructure(serverLevel, (StructureTemplate)template.get());
/* 373 */         return true;
/*     */       }  }
/*     */     
/* 376 */     return false;
/*     */   }
/*     */   
/*     */   private void placeStructure(ServerLevel level, StructureTemplate template) {
/* 380 */     StructurePlaceSettings placeSettings = (new StructurePlaceSettings()).setRotation(getRotation()).setIgnoreEntities(this.data.ignoreEntities()).setKnownShape(true);
/* 381 */     BlockPos pos = getStartCorner();
/*     */     
/* 383 */     forceLoadChunks();
/* 384 */     StructureUtils.clearSpaceForStructure(getStructureBoundingBox(), level);
/* 385 */     removeEntities();
/* 386 */     template.placeInWorld(level, pos, pos, placeSettings, level.getRandom(), 818);
/*     */   }
/*     */ 
/*     */   
/* 390 */   private void removeEntities() { this.level.getEntities(null, getStructureBounds()).stream().filter(entity -> !(entity instanceof net.minecraft.world.entity.player.Player)).forEach(Entity::discard); }
/*     */ 
/*     */   
/*     */   private void forceLoadChunks() {
/* 394 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 395 */       getStructureBoundingBox().intersectingChunks().forEach(pos -> serverLevel.setChunkForced(pos.x, pos.z, true)); }
/*     */   
/*     */   }
/*     */   
/*     */   public BlockPos getStartCorner() {
/* 400 */     Vec3i structureSize = getSize();
/* 401 */     Rotation rotation = getRotation();
/* 402 */     BlockPos northWestCorner = getStructurePos();
/* 403 */     switch (rotation) { default: throw new MatchException(null, null);case NONE: case CLOCKWISE_90: case CLOCKWISE_180: case COUNTERCLOCKWISE_90: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 407 */       northWestCorner.offset(0, 0, structureSize.getX() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encaseStructure() {
/* 412 */     processStructureBoundary(blockPos -> {
/*     */           
/* 414 */           if (!this.level.getBlockState(blockPos).is(Blocks.TEST_INSTANCE_BLOCK)) {
/* 415 */             this.level.setBlockAndUpdate(blockPos, Blocks.BARRIER.defaultBlockState());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBarriers() {
/* 422 */     processStructureBoundary(blockPos -> {
/*     */           
/* 424 */           if (this.level.getBlockState(blockPos).is(Blocks.BARRIER)) {
/* 425 */             this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void processStructureBoundary(Consumer<BlockPos> action) {
/* 432 */     AABB bounds = getStructureBounds();
/* 433 */     boolean hasCeiling = !((Boolean)getTestHolder().map(h -> Boolean.valueOf(((GameTestInstance)h.value()).skyAccess())).orElse(Boolean.valueOf(false))).booleanValue();
/*     */     
/* 435 */     BlockPos low = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).offset(-1, -1, -1);
/* 436 */     BlockPos high = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
/* 437 */     BlockPos.betweenClosedStream(low, high).forEach(blockPos -> {
/*     */           
/* 439 */           boolean isNonCeilingEdge = (blockPos.getX() == low.getX() || blockPos.getX() == high.getX() || blockPos.getZ() == low.getZ() || blockPos.getZ() == high.getZ() || blockPos.getY() == low.getY());
/* 440 */           boolean isCeiling = (blockPos.getY() == high.getY());
/* 441 */           if (isNonCeilingEdge || (isCeiling && hasCeiling)) {
/* 442 */             action.accept(blockPos);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void markError(BlockPos pos, Component text) {
/* 449 */     this.errorMarkers.add(new ErrorMarker(pos, text));
/* 450 */     setChanged();
/*     */   }
/*     */   
/*     */   public void clearErrorMarkers() {
/* 454 */     if (!this.errorMarkers.isEmpty()) {
/* 455 */       this.errorMarkers.clear();
/* 456 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 461 */   public List<ErrorMarker> getErrorMarkers() { return this.errorMarkers; }
/*     */   public static final class ErrorMarker extends Record { private final BlockPos pos; private final Component text;
/*     */     
/* 464 */     public ErrorMarker(BlockPos pos, Component text) { this.pos = pos; this.text = text; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$ErrorMarker;
/* 464 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public Component text() { return this.text; }
/*     */ 
/*     */ 
/*     */     
/* 468 */     public static final Codec<ErrorMarker> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/* 469 */           .fieldOf("pos").forGetter(ErrorMarker::pos), ComponentSerialization.CODEC
/* 470 */           .fieldOf("text").forGetter(ErrorMarker::text))
/* 471 */         .apply(i, ErrorMarker::new));
/*     */     
/* 473 */     public static final Codec<List<ErrorMarker>> LIST_CODEC = CODEC.listOf(); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */