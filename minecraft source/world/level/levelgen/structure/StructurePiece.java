/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
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
/*     */ 
/*     */ public abstract class StructurePiece
/*     */ {
/*  66 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   protected BoundingBox boundingBox;
/*     */   private Direction orientation;
/*     */   private Mirror mirror;
/*     */   private Rotation rotation;
/*     */   protected int genDepth;
/*     */   private final StructurePieceType type;
/*     */   
/*     */   protected StructurePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/*  75 */     this.type = type;
/*  76 */     this.genDepth = genDepth;
/*  77 */     this.boundingBox = boundingBox;
/*     */   }
/*     */   
/*     */   public StructurePiece(StructurePieceType type, CompoundTag tag) {
/*  81 */     this(type, tag
/*     */         
/*  83 */         .getIntOr("GD", 0), (BoundingBox)tag
/*  84 */         .read("BB", BoundingBox.CODEC).orElseThrow());
/*     */     
/*  86 */     int orientation = tag.getIntOr("O", 0);
/*  87 */     setOrientation((orientation == -1) ? null : Direction.from2DDataValue(orientation));
/*     */   }
/*     */   
/*     */   protected static BoundingBox makeBoundingBox(int x, int y, int z, Direction direction, int width, int height, int depth) {
/*  91 */     if (direction.getAxis() == Direction.Axis.Z) {
/*  92 */       return new BoundingBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1);
/*     */     }
/*  94 */     return new BoundingBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected static Direction getRandomHorizontalDirection(RandomSource random) { return Direction.Plane.HORIZONTAL.getRandomDirection(random); }
/*     */ 
/*     */   
/*     */   public final CompoundTag createTag(StructurePieceSerializationContext context) {
/* 103 */     CompoundTag tag = new CompoundTag();
/*     */     
/* 105 */     tag.putString("id", BuiltInRegistries.STRUCTURE_PIECE.getKey(getType()).toString());
/* 106 */     tag.store("BB", BoundingBox.CODEC, this.boundingBox);
/* 107 */     Direction orientation = getOrientation();
/* 108 */     tag.putInt("O", (orientation == null) ? -1 : orientation.get2DDataValue());
/* 109 */     tag.putInt("GD", this.genDepth);
/*     */     
/* 111 */     addAdditionalSaveData(context, tag);
/*     */     
/* 113 */     return tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public BoundingBox getBoundingBox() { return this.boundingBox; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public int getGenDepth() { return this.genDepth; }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public void setGenDepth(int genDepth) { this.genDepth = genDepth; }
/*     */ 
/*     */   
/*     */   public boolean isCloseToChunk(ChunkPos pos, int distance) {
/* 136 */     int cx = pos.getMinBlockX();
/* 137 */     int cz = pos.getMinBlockZ();
/*     */     
/* 139 */     return this.boundingBox.intersects(cx - distance, cz - distance, cx + 15 + distance, cz + 15 + distance);
/*     */   }
/*     */ 
/*     */   
/* 143 */   public BlockPos getLocatorPosition() { return new BlockPos(this.boundingBox.getCenter()); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   protected BlockPos.MutableBlockPos getWorldPos(int x, int y, int z) { return new BlockPos.MutableBlockPos(getWorldX(x, z), getWorldY(y), getWorldZ(x, z)); }
/*     */ 
/*     */   
/*     */   protected int getWorldX(int x, int z) {
/* 151 */     Direction orientation = getOrientation();
/* 152 */     if (orientation == null) {
/* 153 */       return x;
/*     */     }
/*     */     
/* 156 */     switch (orientation) {
/*     */       case NORTH:
/*     */       case SOUTH:
/* 159 */         return this.boundingBox.minX() + x;
/*     */       case WEST:
/* 161 */         return this.boundingBox.maxX() - z;
/*     */       case EAST:
/* 163 */         return this.boundingBox.minX() + z;
/*     */     } 
/* 165 */     return x;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getWorldY(int y) {
/* 170 */     if (getOrientation() == null) {
/* 171 */       return y;
/*     */     }
/* 173 */     return y + this.boundingBox.minY();
/*     */   }
/*     */   
/*     */   protected int getWorldZ(int x, int z) {
/* 177 */     Direction orientation = getOrientation();
/* 178 */     if (orientation == null) {
/* 179 */       return z;
/*     */     }
/*     */     
/* 182 */     switch (orientation) {
/*     */       case NORTH:
/* 184 */         return this.boundingBox.maxZ() - z;
/*     */       case SOUTH:
/* 186 */         return this.boundingBox.minZ() + z;
/*     */       case WEST:
/*     */       case EAST:
/* 189 */         return this.boundingBox.minZ() + x;
/*     */     } 
/* 191 */     return z;
/*     */   }
/*     */ 
/*     */   
/* 195 */   private static final Set<Block> SHAPE_CHECK_BLOCKS = ImmutableSet.builder()
/*     */     
/* 197 */     .add(Blocks.NETHER_BRICK_FENCE)
/* 198 */     .add(Blocks.TORCH)
/* 199 */     .add(Blocks.WALL_TORCH)
/* 200 */     .add(Blocks.OAK_FENCE)
/* 201 */     .add(Blocks.SPRUCE_FENCE)
/* 202 */     .add(Blocks.DARK_OAK_FENCE)
/* 203 */     .add(Blocks.PALE_OAK_FENCE)
/* 204 */     .add(Blocks.ACACIA_FENCE)
/* 205 */     .add(Blocks.BIRCH_FENCE)
/* 206 */     .add(Blocks.JUNGLE_FENCE)
/* 207 */     .add(Blocks.LADDER)
/* 208 */     .add(Blocks.IRON_BARS)
/* 209 */     .build();
/*     */   
/*     */   protected void placeBlock(WorldGenLevel level, BlockState blockState, int x, int y, int z, BoundingBox chunkBB) {
/* 212 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/*     */     
/* 214 */     if (!chunkBB.isInside(mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     if (!canBeReplaced(level, x, y, z, chunkBB)) {
/*     */       return;
/*     */     }
/*     */     
/* 222 */     if (this.mirror != Mirror.NONE) {
/* 223 */       blockState = blockState.mirror(this.mirror);
/*     */     }
/* 225 */     if (this.rotation != Rotation.NONE) {
/* 226 */       blockState = blockState.rotate(this.rotation);
/*     */     }
/*     */     
/* 229 */     level.setBlock(mutableBlockPos, blockState, 2);
/* 230 */     FluidState fluidState = level.getFluidState(mutableBlockPos);
/* 231 */     if (!fluidState.isEmpty()) {
/* 232 */       level.scheduleTick(mutableBlockPos, fluidState.getType(), 0);
/*     */     }
/* 234 */     if (SHAPE_CHECK_BLOCKS.contains(blockState.getBlock())) {
/* 235 */       level.getChunk(mutableBlockPos).markPosForPostprocessing(mutableBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 240 */   protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox chunkBB) { return true; }
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
/*     */   protected BlockState getBlock(BlockGetter level, int x, int y, int z, BoundingBox chunkBB) {
/* 256 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 257 */     if (!chunkBB.isInside(mutableBlockPos)) {
/* 258 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 261 */     return level.getBlockState(mutableBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInterior(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/* 266 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y + 1, z);
/*     */     
/* 268 */     if (!chunkBB.isInside(mutableBlockPos)) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     return (mutableBlockPos.getY() < level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutableBlockPos.getX(), mutableBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   protected void generateAirBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1) {
/* 276 */     for (int y = y0; y <= y1; y++) {
/* 277 */       for (int x = x0; x <= x1; x++) {
/* 278 */         for (int z = z0; z <= z1; z++) {
/* 279 */           placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, chunkBB);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState edgeBlock, BlockState fillBlock, boolean skipAir) {
/* 286 */     for (int y = y0; y <= y1; y++) {
/* 287 */       for (int x = x0; x <= x1; x++) {
/* 288 */         for (int z = z0; z <= z1; z++) {
/* 289 */           if (!skipAir || !getBlock(level, x, y, z, chunkBB).isAir())
/*     */           {
/*     */             
/* 292 */             if (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1) {
/* 293 */               placeBlock(level, edgeBlock, x, y, z, chunkBB);
/*     */             } else {
/* 295 */               placeBlock(level, fillBlock, x, y, z, chunkBB);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/* 303 */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, BoundingBox boxBB, BlockState edgeBlock, BlockState fillBlock, boolean skipAir) { generateBox(level, chunkBB, boxBB.minX(), boxBB.minY(), boxBB.minZ(), boxBB.maxX(), boxBB.maxY(), boxBB.maxZ(), edgeBlock, fillBlock, skipAir); }
/*     */ 
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, boolean skipAir, RandomSource random, BlockSelector selector) {
/* 307 */     for (int y = y0; y <= y1; y++) {
/* 308 */       for (int x = x0; x <= x1; x++) {
/* 309 */         for (int z = z0; z <= z1; z++) {
/* 310 */           if (!skipAir || !getBlock(level, x, y, z, chunkBB).isAir()) {
/*     */ 
/*     */             
/* 313 */             selector.next(random, x, y, z, (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1));
/* 314 */             placeBlock(level, selector.getNext(), x, y, z, chunkBB);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/* 321 */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, BoundingBox boxBB, boolean skipAir, RandomSource random, BlockSelector selector) { generateBox(level, chunkBB, boxBB.minX(), boxBB.minY(), boxBB.minZ(), boxBB.maxX(), boxBB.maxY(), boxBB.maxZ(), skipAir, random, selector); }
/*     */ 
/*     */   
/*     */   protected void generateMaybeBox(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x0, int y0, int z0, int x1, int y1, int z1, BlockState edgeBlock, BlockState fillBlock, boolean skipAir, boolean hasToBeInside) {
/* 325 */     for (int y = y0; y <= y1; y++) {
/* 326 */       for (int x = x0; x <= x1; x++) {
/* 327 */         for (int z = z0; z <= z1; z++) {
/* 328 */           if (random.nextFloat() <= probability)
/*     */           {
/*     */             
/* 331 */             if (!skipAir || !getBlock(level, x, y, z, chunkBB).isAir())
/*     */             {
/*     */               
/* 334 */               if (!hasToBeInside || isInterior(level, x, y, z, chunkBB))
/*     */               {
/*     */                 
/* 337 */                 if (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1) {
/* 338 */                   placeBlock(level, edgeBlock, x, y, z, chunkBB);
/*     */                 } else {
/* 340 */                   placeBlock(level, fillBlock, x, y, z, chunkBB);
/*     */                 }  }  }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void maybeGenerateBlock(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x, int y, int z, BlockState blockState) {
/* 348 */     if (random.nextFloat() < probability) {
/* 349 */       placeBlock(level, blockState, x, y, z, chunkBB);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void generateUpperHalfSphere(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState fillBlock, boolean skipAir) {
/* 354 */     float diagX = (x1 - x0 + 1);
/* 355 */     float diagY = (y1 - y0 + 1);
/* 356 */     float diagZ = (z1 - z0 + 1);
/*     */     
/* 358 */     float cx = x0 + diagX / 2.0F;
/* 359 */     float cz = z0 + diagZ / 2.0F;
/*     */     
/* 361 */     for (int y = y0; y <= y1; y++) {
/* 362 */       float normalizedYDistance = (y - y0) / diagY;
/*     */       
/* 364 */       for (int x = x0; x <= x1; x++) {
/* 365 */         float normalizedXDistance = (x - cx) / diagX * 0.5F;
/*     */         
/* 367 */         for (int z = z0; z <= z1; z++) {
/* 368 */           float normalizedZDistance = (z - cz) / diagZ * 0.5F;
/*     */           
/* 370 */           if (!skipAir || !getBlock(level, x, y, z, chunkBB).isAir()) {
/*     */ 
/*     */ 
/*     */             
/* 374 */             float dist = normalizedXDistance * normalizedXDistance + normalizedYDistance * normalizedYDistance + normalizedZDistance * normalizedZDistance;
/*     */             
/* 376 */             if (dist <= 1.05F)
/* 377 */               placeBlock(level, fillBlock, x, y, z, chunkBB); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void fillColumnDown(WorldGenLevel level, BlockState blockState, int x, int startY, int z, BoundingBox chunkBB) {
/* 385 */     BlockPos.MutableBlockPos pos = getWorldPos(x, startY, z);
/* 386 */     if (!chunkBB.isInside(pos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 391 */     while (isReplaceableByStructures(level.getBlockState(pos)) && pos.getY() > level.getMinY() + 1) {
/* 392 */       level.setBlock(pos, blockState, 2);
/* 393 */       pos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 398 */   protected boolean isReplaceableByStructures(BlockState state) { return (state.isAir() || state.liquid() || state.is(Blocks.GLOW_LICHEN) || state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS)); }
/*     */ 
/*     */ 
/*     */   
/* 402 */   protected boolean createChest(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) { return createChest(level, chunkBB, random, getWorldPos(x, y, z), lootTable, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BlockState reorient(BlockGetter level, BlockPos blockPos, BlockState blockState) {
/* 407 */     Direction solidNeighbor = null;
/* 408 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 409 */       BlockPos relativePos = blockPos.relative(direction);
/* 410 */       BlockState state = level.getBlockState(relativePos);
/* 411 */       if (state.is(Blocks.CHEST)) {
/* 412 */         return blockState;
/*     */       }
/* 414 */       if (state.isSolidRender()) {
/* 415 */         if (solidNeighbor == null) {
/* 416 */           solidNeighbor = direction; continue;
/*     */         } 
/* 418 */         solidNeighbor = null;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 423 */     if (solidNeighbor != null) {
/* 424 */       return (BlockState)blockState.setValue(HorizontalDirectionalBlock.FACING, solidNeighbor.getOpposite());
/*     */     }
/*     */ 
/*     */     
/* 428 */     Direction lockDir = (Direction)blockState.getValue(HorizontalDirectionalBlock.FACING);
/* 429 */     BlockPos relativePos = blockPos.relative(lockDir);
/* 430 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 431 */       lockDir = lockDir.getOpposite();
/* 432 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/* 434 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 435 */       lockDir = lockDir.getClockWise();
/* 436 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/* 438 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 439 */       lockDir = lockDir.getOpposite();
/* 440 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/*     */     
/* 443 */     return (BlockState)blockState.setValue(HorizontalDirectionalBlock.FACING, lockDir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean createChest(ServerLevelAccessor level, BoundingBox chunkBB, RandomSource random, BlockPos pos, ResourceKey<LootTable> lootTable, BlockState blockState) {
/* 450 */     if (!chunkBB.isInside(pos) || level.getBlockState(pos).is(Blocks.CHEST)) {
/* 451 */       return false;
/*     */     }
/*     */     
/* 454 */     if (blockState == null) {
/* 455 */       blockState = reorient(level, pos, Blocks.CHEST.defaultBlockState());
/*     */     }
/* 457 */     level.setBlock(pos, blockState, 2);
/*     */     
/* 459 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 460 */     if (blockEntity instanceof ChestBlockEntity) {
/* 461 */       ((ChestBlockEntity)blockEntity).setLootTable(lootTable, random.nextLong());
/*     */     }
/* 463 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean createDispenser(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, Direction facing, ResourceKey<LootTable> lootTable) {
/* 467 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/*     */     
/* 469 */     if (chunkBB.isInside(mutableBlockPos) && 
/* 470 */       !level.getBlockState(mutableBlockPos).is(Blocks.DISPENSER)) {
/* 471 */       placeBlock(level, (BlockState)Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, facing), x, y, z, chunkBB);
/*     */       
/* 473 */       BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
/* 474 */       if (blockEntity instanceof DispenserBlockEntity) {
/* 475 */         ((DispenserBlockEntity)blockEntity).setLootTable(lootTable, random.nextLong());
/*     */       }
/* 477 */       return true;
/*     */     } 
/*     */     
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 484 */   public void move(int dx, int dy, int dz) { this.boundingBox.move(dx, dy, dz); }
/*     */ 
/*     */ 
/*     */   
/* 488 */   public static BoundingBox createBoundingBox(Stream<StructurePiece> pieces) { Objects.requireNonNull(pieces.map(StructurePiece::getBoundingBox)); return (BoundingBox)BoundingBox.encapsulatingBoxes(pieces.map(StructurePiece::getBoundingBox)::iterator).orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox without pieces")); }
/*     */ 
/*     */   
/*     */   public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BoundingBox box) {
/* 492 */     for (StructurePiece piece : pieces) {
/* 493 */       if (piece.getBoundingBox().intersects(box)) {
/* 494 */         return piece;
/*     */       }
/*     */     } 
/* 497 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 501 */   public Direction getOrientation() { return this.orientation; }
/*     */ 
/*     */   
/*     */   public void setOrientation(Direction orientation) {
/* 505 */     this.orientation = orientation;
/* 506 */     if (orientation == null) {
/* 507 */       this.rotation = Rotation.NONE;
/* 508 */       this.mirror = Mirror.NONE;
/*     */     } else {
/* 510 */       switch (orientation) {
/*     */         case SOUTH:
/* 512 */           this.mirror = Mirror.LEFT_RIGHT;
/* 513 */           this.rotation = Rotation.NONE;
/*     */           return;
/*     */         case WEST:
/* 516 */           this.mirror = Mirror.LEFT_RIGHT;
/* 517 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */         case EAST:
/* 520 */           this.mirror = Mirror.NONE;
/* 521 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */       } 
/* 524 */       this.mirror = Mirror.NONE;
/* 525 */       this.rotation = Rotation.NONE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 532 */   public Rotation getRotation() { return this.rotation; }
/*     */ 
/*     */ 
/*     */   
/* 536 */   public Mirror getMirror() { return this.mirror; }
/*     */   
/*     */   protected abstract void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag);
/*     */   
/* 540 */   public StructurePieceType getType() { return this.type; }
/*     */   
/*     */   public abstract void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos);
/*     */   
/* 544 */   public static abstract class BlockSelector { protected BlockState next = Blocks.AIR.defaultBlockState();
/*     */ 
/*     */     
/*     */     public abstract void next(RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
/*     */     
/* 549 */     public BlockState getNext() { return this.next; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */