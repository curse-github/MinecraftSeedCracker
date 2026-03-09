/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.StructureBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class StructureBlockEntity
/*     */   extends BlockEntity
/*     */   implements BoundingBoxRenderable {
/*     */   private static final int SCAN_CORNER_BLOCKS_RANGE = 5;
/*     */   public static final int MAX_OFFSET_PER_AXIS = 48;
/*     */   public static final int MAX_SIZE_PER_AXIS = 48;
/*     */   public static final String AUTHOR_TAG = "author";
/*     */   private static final String DEFAULT_AUTHOR = "";
/*     */   private static final String DEFAULT_METADATA = "";
/*  49 */   private static final BlockPos DEFAULT_POS = new BlockPos(0, 1, 0);
/*  50 */   private static final Vec3i DEFAULT_SIZE = Vec3i.ZERO;
/*  51 */   private static final Rotation DEFAULT_ROTATION = Rotation.NONE;
/*  52 */   private static final Mirror DEFAULT_MIRROR = Mirror.NONE;
/*     */   
/*     */   private static final boolean DEFAULT_IGNORE_ENTITIES = true;
/*     */   private static final boolean DEFAULT_STRICT = false;
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   private static final boolean DEFAULT_SHOW_AIR = false;
/*     */   private static final boolean DEFAULT_SHOW_BOUNDING_BOX = true;
/*     */   private static final float DEFAULT_INTEGRITY = 1.0F;
/*     */   private static final long DEFAULT_SEED = 0L;
/*     */   private Identifier structureName;
/*  62 */   private String author = "";
/*  63 */   private String metaData = "";
/*  64 */   private BlockPos structurePos = DEFAULT_POS;
/*  65 */   private Vec3i structureSize = DEFAULT_SIZE;
/*  66 */   private Mirror mirror = Mirror.NONE;
/*  67 */   private Rotation rotation = Rotation.NONE;
/*     */   private StructureMode mode;
/*     */   private boolean ignoreEntities = true;
/*     */   private boolean strict = false;
/*     */   private boolean powered = false;
/*     */   private boolean showAir = false;
/*     */   private boolean showBoundingBox = true;
/*  74 */   private float integrity = 1.0F;
/*  75 */   private long seed = 0L;
/*     */   
/*     */   public StructureBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  78 */     super(BlockEntityType.STRUCTURE_BLOCK, worldPosition, blockState);
/*  79 */     this.mode = (StructureMode)blockState.getValue(StructureBlock.MODE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  84 */     super.saveAdditional(output);
/*  85 */     output.putString("name", getStructureName());
/*  86 */     output.putString("author", this.author);
/*  87 */     output.putString("metadata", this.metaData);
/*  88 */     output.putInt("posX", this.structurePos.getX());
/*  89 */     output.putInt("posY", this.structurePos.getY());
/*  90 */     output.putInt("posZ", this.structurePos.getZ());
/*  91 */     output.putInt("sizeX", this.structureSize.getX());
/*  92 */     output.putInt("sizeY", this.structureSize.getY());
/*  93 */     output.putInt("sizeZ", this.structureSize.getZ());
/*  94 */     output.store("rotation", Rotation.LEGACY_CODEC, this.rotation);
/*  95 */     output.store("mirror", Mirror.LEGACY_CODEC, this.mirror);
/*  96 */     output.store("mode", StructureMode.LEGACY_CODEC, this.mode);
/*  97 */     output.putBoolean("ignoreEntities", this.ignoreEntities);
/*  98 */     output.putBoolean("strict", this.strict);
/*  99 */     output.putBoolean("powered", this.powered);
/* 100 */     output.putBoolean("showair", this.showAir);
/* 101 */     output.putBoolean("showboundingbox", this.showBoundingBox);
/* 102 */     output.putFloat("integrity", this.integrity);
/* 103 */     output.putLong("seed", this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 108 */     super.loadAdditional(input);
/* 109 */     setStructureName(input.getStringOr("name", ""));
/* 110 */     this.author = input.getStringOr("author", "");
/* 111 */     this.metaData = input.getStringOr("metadata", "");
/* 112 */     int xOffset = Mth.clamp(input.getIntOr("posX", DEFAULT_POS.getX()), -48, 48);
/* 113 */     int yOffset = Mth.clamp(input.getIntOr("posY", DEFAULT_POS.getY()), -48, 48);
/* 114 */     int zOffset = Mth.clamp(input.getIntOr("posZ", DEFAULT_POS.getZ()), -48, 48);
/* 115 */     this.structurePos = new BlockPos(xOffset, yOffset, zOffset);
/* 116 */     int width = Mth.clamp(input.getIntOr("sizeX", DEFAULT_SIZE.getX()), 0, 48);
/* 117 */     int height = Mth.clamp(input.getIntOr("sizeY", DEFAULT_SIZE.getY()), 0, 48);
/* 118 */     int depth = Mth.clamp(input.getIntOr("sizeZ", DEFAULT_SIZE.getZ()), 0, 48);
/* 119 */     this.structureSize = new Vec3i(width, height, depth);
/* 120 */     this.rotation = (Rotation)input.read("rotation", Rotation.LEGACY_CODEC).orElse(DEFAULT_ROTATION);
/* 121 */     this.mirror = (Mirror)input.read("mirror", Mirror.LEGACY_CODEC).orElse(DEFAULT_MIRROR);
/* 122 */     this.mode = (StructureMode)input.read("mode", StructureMode.LEGACY_CODEC).orElse(StructureMode.DATA);
/* 123 */     this.ignoreEntities = input.getBooleanOr("ignoreEntities", true);
/* 124 */     this.strict = input.getBooleanOr("strict", false);
/* 125 */     this.powered = input.getBooleanOr("powered", false);
/* 126 */     this.showAir = input.getBooleanOr("showair", false);
/* 127 */     this.showBoundingBox = input.getBooleanOr("showboundingbox", true);
/* 128 */     this.integrity = input.getFloatOr("integrity", 1.0F);
/* 129 */     this.seed = input.getLongOr("seed", 0L);
/* 130 */     updateBlockState();
/*     */   }
/*     */   
/*     */   private void updateBlockState() {
/* 134 */     if (this.level == null) {
/*     */       return;
/*     */     }
/* 137 */     BlockPos pos = getBlockPos();
/* 138 */     BlockState blockState = this.level.getBlockState(pos);
/* 139 */     if (blockState.is(Blocks.STRUCTURE_BLOCK)) {
/* 140 */       this.level.setBlock(pos, (BlockState)blockState.setValue(StructureBlock.MODE, this.mode), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */   
/*     */   public boolean usedBy(Player player) {
/* 155 */     if (!player.canUseGameMasterBlocks()) {
/* 156 */       return false;
/*     */     }
/* 158 */     if (player.level().isClientSide()) {
/* 159 */       player.openStructureBlock(this);
/*     */     }
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 165 */   public String getStructureName() { return (this.structureName == null) ? "" : this.structureName.toString(); }
/*     */ 
/*     */ 
/*     */   
/* 169 */   public boolean hasStructureName() { return (this.structureName != null); }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public void setStructureName(String structureName) { setStructureName(StringUtil.isNullOrEmpty(structureName) ? null : Identifier.tryParse(structureName)); }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public void setStructureName(Identifier structureName) { this.structureName = structureName; }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public void createdBy(LivingEntity creator) { this.author = creator.getPlainTextName(); }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public BlockPos getStructurePos() { return this.structurePos; }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public void setStructurePos(BlockPos structurePos) { this.structurePos = structurePos; }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public Vec3i getStructureSize() { return this.structureSize; }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public void setStructureSize(Vec3i structureSize) { this.structureSize = structureSize; }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public Mirror getMirror() { return this.mirror; }
/*     */ 
/*     */ 
/*     */   
/* 205 */   public void setMirror(Mirror mirror) { this.mirror = mirror; }
/*     */ 
/*     */ 
/*     */   
/* 209 */   public Rotation getRotation() { return this.rotation; }
/*     */ 
/*     */ 
/*     */   
/* 213 */   public void setRotation(Rotation rotation) { this.rotation = rotation; }
/*     */ 
/*     */ 
/*     */   
/* 217 */   public String getMetaData() { return this.metaData; }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public void setMetaData(String metaData) { this.metaData = metaData; }
/*     */ 
/*     */ 
/*     */   
/* 225 */   public StructureMode getMode() { return this.mode; }
/*     */ 
/*     */   
/*     */   public void setMode(StructureMode mode) {
/* 229 */     this.mode = mode;
/* 230 */     BlockState state = this.level.getBlockState(getBlockPos());
/* 231 */     if (state.is(Blocks.STRUCTURE_BLOCK)) {
/* 232 */       this.level.setBlock(getBlockPos(), (BlockState)state.setValue(StructureBlock.MODE, mode), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 237 */   public boolean isIgnoreEntities() { return this.ignoreEntities; }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public boolean isStrict() { return this.strict; }
/*     */ 
/*     */ 
/*     */   
/* 245 */   public void setIgnoreEntities(boolean ignoreEntities) { this.ignoreEntities = ignoreEntities; }
/*     */ 
/*     */ 
/*     */   
/* 249 */   public void setStrict(boolean strict) { this.strict = strict; }
/*     */ 
/*     */ 
/*     */   
/* 253 */   public float getIntegrity() { return this.integrity; }
/*     */ 
/*     */ 
/*     */   
/* 257 */   public void setIntegrity(float integrity) { this.integrity = integrity; }
/*     */ 
/*     */ 
/*     */   
/* 261 */   public long getSeed() { return this.seed; }
/*     */ 
/*     */ 
/*     */   
/* 265 */   public void setSeed(long seed) { this.seed = seed; }
/*     */ 
/*     */   
/*     */   public boolean detectSize() {
/* 269 */     if (this.mode != StructureMode.SAVE) {
/* 270 */       return false;
/*     */     }
/* 272 */     BlockPos pos = getBlockPos();
/* 273 */     int radius = 80;
/* 274 */     BlockPos corner1 = new BlockPos(pos.getX() - 80, this.level.getMinY(), pos.getZ() - 80);
/* 275 */     BlockPos corner2 = new BlockPos(pos.getX() + 80, this.level.getMaxY(), pos.getZ() + 80);
/*     */     
/* 277 */     Stream<BlockPos> relatedCorners = getRelatedCorners(corner1, corner2);
/*     */     
/* 279 */     return calculateEnclosingBoundingBox(pos, relatedCorners).filter(bb -> {
/* 280 */           int deltaX = bb.maxX() - bb.minX();
/* 281 */           int deltaY = bb.maxY() - bb.minY();
/* 282 */           int deltaZ = bb.maxZ() - bb.minZ();
/* 283 */           if (deltaX > 1 && deltaY > 1 && deltaZ > 1) {
/* 284 */             this.structurePos = new BlockPos(bb.minX() - pos.getX() + 1, bb.minY() - pos.getY() + 1, bb.minZ() - pos.getZ() + 1);
/* 285 */             this.structureSize = new Vec3i(deltaX - 1, deltaY - 1, deltaZ - 1);
/* 286 */             setChanged();
/* 287 */             BlockState state = this.level.getBlockState(pos);
/* 288 */             this.level.sendBlockUpdated(pos, state, state, 3);
/* 289 */             return true;
/*     */           } 
/* 291 */           return false;
/* 292 */         }).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Stream<BlockPos> getRelatedCorners(BlockPos corner1, BlockPos corner2) {
/* 298 */     Objects.requireNonNull(this.level); return BlockPos.betweenClosedStream(corner1, corner2).filter(pos -> this.level.getBlockState(pos).is(Blocks.STRUCTURE_BLOCK)).map(this.level::getBlockEntity)
/* 299 */       .filter(e -> e instanceof StructureBlockEntity)
/* 300 */       .map(e -> (StructureBlockEntity)e)
/* 301 */       .filter(input -> (input.mode == StructureMode.CORNER && Objects.equals(this.structureName, input.structureName)))
/* 302 */       .map(BlockEntity::getBlockPos);
/*     */   }
/*     */   
/*     */   private static Optional<BoundingBox> calculateEnclosingBoundingBox(BlockPos pos, Stream<BlockPos> relatedCorners) {
/* 306 */     Iterator<BlockPos> iterator = relatedCorners.iterator();
/* 307 */     if (!iterator.hasNext()) {
/* 308 */       return Optional.empty();
/*     */     }
/*     */     
/* 311 */     BlockPos firstCorner = (BlockPos)iterator.next();
/* 312 */     BoundingBox result = new BoundingBox(firstCorner);
/* 313 */     if (iterator.hasNext()) {
/* 314 */       Objects.requireNonNull(result); iterator.forEachRemaining(result::encapsulate);
/*     */     } else {
/*     */       
/* 317 */       result.encapsulate(pos);
/*     */     } 
/* 319 */     return Optional.of(result);
/*     */   }
/*     */   
/*     */   public boolean saveStructure() {
/* 323 */     if (this.mode != StructureMode.SAVE) {
/* 324 */       return false;
/*     */     }
/* 326 */     return saveStructure(true);
/*     */   }
/*     */   public boolean saveStructure(boolean saveToDisk) {
/*     */     ServerLevel serverLevel;
/* 330 */     if (this.structureName != null) { Level level = this.level; if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 331 */       else { return false; }  } else { return false; }
/*     */     
/* 333 */     BlockPos pos = getBlockPos().offset(this.structurePos);
/* 334 */     return saveStructure(serverLevel, this.structureName, pos, this.structureSize, this.ignoreEntities, this.author, saveToDisk, List.of());
/*     */   }
/*     */   public static boolean saveStructure(ServerLevel level, Identifier structureName, BlockPos pos, Vec3i structureSize, boolean ignoreEntities, String author, boolean saveToDisk, List<Block> ignoreBlocks) {
/*     */     StructureTemplate structureTemplate;
/* 338 */     StructureTemplateManager manager = level.getStructureManager();
/*     */     
/*     */     try {
/* 341 */       structureTemplate = manager.getOrCreate(structureName);
/* 342 */     } catch (IdentifierException e) {
/* 343 */       return false;
/*     */     } 
/*     */     
/* 346 */     structureTemplate.fillFromWorld(level, pos, structureSize, !ignoreEntities, Stream.concat(ignoreBlocks.stream(), Stream.of(Blocks.STRUCTURE_VOID)).toList());
/* 347 */     structureTemplate.setAuthor(author);
/* 348 */     if (saveToDisk) {
/*     */       try {
/* 350 */         return manager.save(structureName);
/* 351 */       } catch (IdentifierException e) {
/* 352 */         return false;
/*     */       } 
/*     */     }
/* 355 */     return true;
/*     */   }
/*     */   
/*     */   public static RandomSource createRandom(long seed) {
/* 359 */     if (seed == 0L) {
/* 360 */       return RandomSource.create(Util.getMillis());
/*     */     }
/* 362 */     return RandomSource.create(seed);
/*     */   }
/*     */   
/*     */   public boolean placeStructureIfSameSize(ServerLevel level) {
/* 366 */     if (this.mode != StructureMode.LOAD || this.structureName == null) {
/* 367 */       return false;
/*     */     }
/* 369 */     StructureTemplate template = (StructureTemplate)level.getStructureManager().get(this.structureName).orElse(null);
/* 370 */     if (template == null) {
/* 371 */       return false;
/*     */     }
/*     */     
/* 374 */     if (template.getSize().equals(this.structureSize)) {
/* 375 */       placeStructure(level, template);
/* 376 */       return true;
/*     */     } 
/* 378 */     loadStructureInfo(template);
/* 379 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean loadStructureInfo(ServerLevel level) {
/* 384 */     StructureTemplate template = getStructureTemplate(level);
/* 385 */     if (template == null) {
/* 386 */       return false;
/*     */     }
/* 388 */     loadStructureInfo(template);
/* 389 */     return true;
/*     */   }
/*     */   
/*     */   private void loadStructureInfo(StructureTemplate structureTemplate) {
/* 393 */     this.author = !StringUtil.isNullOrEmpty(structureTemplate.getAuthor()) ? structureTemplate.getAuthor() : "";
/* 394 */     this.structureSize = structureTemplate.getSize();
/* 395 */     setChanged();
/*     */   }
/*     */   
/*     */   public void placeStructure(ServerLevel level) {
/* 399 */     StructureTemplate template = getStructureTemplate(level);
/* 400 */     if (template != null) {
/* 401 */       placeStructure(level, template);
/*     */     }
/*     */   }
/*     */   
/*     */   private StructureTemplate getStructureTemplate(ServerLevel level) {
/* 406 */     if (this.structureName == null) {
/* 407 */       return null;
/*     */     }
/* 409 */     return (StructureTemplate)level.getStructureManager().get(this.structureName).orElse(null);
/*     */   }
/*     */   
/*     */   private void placeStructure(ServerLevel level, StructureTemplate template) {
/* 413 */     loadStructureInfo(template);
/*     */     
/* 415 */     StructurePlaceSettings placeSettings = (new StructurePlaceSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setKnownShape(this.strict);
/* 416 */     if (this.integrity < 1.0F) {
/* 417 */       placeSettings.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.seed));
/*     */     }
/* 419 */     BlockPos pos = getBlockPos().offset(this.structurePos);
/*     */ 
/*     */     
/* 422 */     if (SharedConstants.DEBUG_STRUCTURE_EDIT_MODE) {
/* 423 */       BlockPos.betweenClosed(pos, pos.offset(this.structureSize)).forEach(p -> level.setBlock(p, Blocks.STRUCTURE_VOID.defaultBlockState(), 2));
/*     */     }
/*     */     
/* 426 */     template.placeInWorld(level, pos, pos, placeSettings, createRandom(this.seed), 0x2 | (this.strict ? 816 : 0));
/*     */   }
/*     */   
/*     */   public void unloadStructure() {
/* 430 */     if (this.structureName == null) {
/*     */       return;
/*     */     }
/* 433 */     ServerLevel serverLevel = (ServerLevel)this.level;
/* 434 */     StructureTemplateManager manager = serverLevel.getStructureManager();
/* 435 */     manager.remove(this.structureName);
/*     */   }
/*     */   
/*     */   public boolean isStructureLoadable() {
/* 439 */     if (this.mode != StructureMode.LOAD || this.level.isClientSide() || this.structureName == null) {
/* 440 */       return false;
/*     */     }
/* 442 */     ServerLevel serverLevel = (ServerLevel)this.level;
/* 443 */     StructureTemplateManager manager = serverLevel.getStructureManager();
/*     */     try {
/* 445 */       return manager.get(this.structureName).isPresent();
/* 446 */     } catch (IdentifierException e) {
/* 447 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 452 */   public boolean isPowered() { return this.powered; }
/*     */ 
/*     */ 
/*     */   
/* 456 */   public void setPowered(boolean powered) { this.powered = powered; }
/*     */ 
/*     */ 
/*     */   
/* 460 */   public boolean getShowAir() { return this.showAir; }
/*     */ 
/*     */ 
/*     */   
/* 464 */   public void setShowAir(boolean showAir) { this.showAir = showAir; }
/*     */ 
/*     */ 
/*     */   
/* 468 */   public boolean getShowBoundingBox() { return this.showBoundingBox; }
/*     */ 
/*     */ 
/*     */   
/* 472 */   public void setShowBoundingBox(boolean showBoundingBox) { this.showBoundingBox = showBoundingBox; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBoxRenderable.Mode renderMode() {
/* 477 */     if (this.mode != StructureMode.SAVE && this.mode != StructureMode.LOAD) {
/* 478 */       return BoundingBoxRenderable.Mode.NONE;
/*     */     }
/* 480 */     if (this.mode == StructureMode.SAVE && this.showAir) {
/* 481 */       return BoundingBoxRenderable.Mode.BOX_AND_INVISIBLE_BLOCKS;
/*     */     }
/* 483 */     if (this.mode == StructureMode.SAVE || this.showBoundingBox) {
/* 484 */       return BoundingBoxRenderable.Mode.BOX;
/*     */     }
/* 486 */     return BoundingBoxRenderable.Mode.NONE;
/*     */   }
/*     */   
/*     */   public BoundingBoxRenderable.RenderableBox getRenderableBox() {
/*     */     int z1, z1, z1, x1, x1, x1, z0, z0, z0, x0, x0, x0, zDiff, zDiff, zDiff, xDiff, xDiff, xDiff;
/* 491 */     BlockPos pos = getStructurePos();
/* 492 */     Vec3i size = getStructureSize();
/*     */     
/* 494 */     int xOrigin = pos.getX();
/* 495 */     int zOrigin = pos.getZ();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 500 */     int y0 = pos.getY();
/*     */ 
/*     */ 
/*     */     
/* 504 */     int y1 = y0 + size.getY();
/*     */ 
/*     */     
/* 507 */     switch (this.mirror) {
/*     */       case CLOCKWISE_90:
/* 509 */         xDiff = size.getX();
/* 510 */         zDiff = -size.getZ();
/*     */         break;
/*     */       case CLOCKWISE_180:
/* 513 */         xDiff = -size.getX();
/* 514 */         zDiff = size.getZ();
/*     */         break;
/*     */       default:
/* 517 */         xDiff = size.getX();
/* 518 */         zDiff = size.getZ();
/*     */         break;
/*     */     } 
/*     */     
/* 522 */     switch (this.rotation)
/*     */     { case CLOCKWISE_90:
/* 524 */         x0 = (zDiff < 0) ? xOrigin : (xOrigin + 1);
/* 525 */         z0 = (xDiff < 0) ? (zOrigin + 1) : zOrigin;
/* 526 */         x1 = x0 - zDiff;
/* 527 */         z1 = z0 + xDiff;
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
/* 548 */         return BoundingBoxRenderable.RenderableBox.fromCorners(x0, y0, z0, x1, y1, z1);case CLOCKWISE_180: x0 = (xDiff < 0) ? xOrigin : (xOrigin + 1); z0 = (zDiff < 0) ? zOrigin : (zOrigin + 1); x1 = x0 - xDiff; z1 = z0 - zDiff; return BoundingBoxRenderable.RenderableBox.fromCorners(x0, y0, z0, x1, y1, z1);case COUNTERCLOCKWISE_90: x0 = (zDiff < 0) ? (xOrigin + 1) : xOrigin; z0 = (xDiff < 0) ? zOrigin : (zOrigin + 1); x1 = x0 + zDiff; z1 = z0 - xDiff; return BoundingBoxRenderable.RenderableBox.fromCorners(x0, y0, z0, x1, y1, z1); }  int x0 = (xDiff < 0) ? (xOrigin + 1) : xOrigin; int z0 = (zDiff < 0) ? (zOrigin + 1) : zOrigin; int x1 = x0 + xDiff; int z1 = z0 + zDiff; return BoundingBoxRenderable.RenderableBox.fromCorners(x0, y0, z0, x1, y1, z1);
/*     */   }
/*     */   
/*     */   public enum UpdateType {
/* 552 */     UPDATE_DATA,
/* 553 */     SAVE_AREA,
/* 554 */     LOAD_AREA,
/* 555 */     SCAN_AREA;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\StructureBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */