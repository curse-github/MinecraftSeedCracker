/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class FlowingFluid
/*     */   extends Fluid
/*     */ {
/*  35 */   public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
/*  36 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_FLOWING;
/*     */   private static final int CACHE_SIZE = 200;
/*     */   
/*  39 */   private static final ThreadLocal<Object2ByteLinkedOpenHashMap<BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
/*  40 */         map = new Object2ByteLinkedOpenHashMap<BlockStatePairKey>(200)
/*     */           {
/*     */             protected void rehash(int newN) {}
/*     */           };
/*     */         
/*  45 */         map.defaultReturnValue(127);
/*  46 */         return map;
/*     */       });
/*     */   
/*  49 */   private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) { builder.add(new Property[] { FALLING }); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getFlow(BlockGetter level, BlockPos pos, FluidState fluidState) {
/*  58 */     double flowX = 0.0D;
/*  59 */     double flowZ = 0.0D;
/*     */     
/*  61 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  62 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  63 */       blockPos.setWithOffset(pos, direction);
/*  64 */       FluidState neighbourFluid = level.getFluidState(blockPos);
/*  65 */       if (!affectsFlow(neighbourFluid)) {
/*     */         continue;
/*     */       }
/*  68 */       float neighborHeight = neighbourFluid.getOwnHeight();
/*  69 */       float distance = 0.0F;
/*  70 */       if (neighborHeight == 0.0F) {
/*  71 */         if (!level.getBlockState(blockPos).blocksMotion()) {
/*  72 */           BlockPos neighborPos = blockPos.below();
/*  73 */           FluidState belowNeighborState = level.getFluidState(neighborPos);
/*  74 */           if (affectsFlow(belowNeighborState)) {
/*  75 */             neighborHeight = belowNeighborState.getOwnHeight();
/*  76 */             if (neighborHeight > 0.0F) {
/*  77 */               distance = fluidState.getOwnHeight() - neighborHeight - 0.8888889F;
/*     */             }
/*     */           } 
/*     */         } 
/*  81 */       } else if (neighborHeight > 0.0F) {
/*  82 */         distance = fluidState.getOwnHeight() - neighborHeight;
/*     */       } 
/*     */       
/*  85 */       if (distance != 0.0F) {
/*  86 */         flowX += (direction.getStepX() * distance);
/*  87 */         flowZ += (direction.getStepZ() * distance);
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     Vec3 flow = new Vec3(flowX, 0.0D, flowZ);
/*  92 */     if (((Boolean)fluidState.getValue(FALLING)).booleanValue()) {
/*  93 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  94 */         blockPos.setWithOffset(pos, direction);
/*  95 */         if (isSolidFace(level, blockPos, direction) || isSolidFace(level, blockPos.above(), direction)) {
/*  96 */           flow = flow.normalize().add(0.0D, -6.0D, 0.0D);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 102 */     return flow.normalize();
/*     */   }
/*     */ 
/*     */   
/* 106 */   private boolean affectsFlow(FluidState neighbourFluid) { return (neighbourFluid.isEmpty() || neighbourFluid.getType().isSame(this)); }
/*     */ 
/*     */   
/*     */   protected boolean isSolidFace(BlockGetter level, BlockPos pos, Direction direction) {
/* 110 */     BlockState state = level.getBlockState(pos);
/* 111 */     FluidState fluidState = level.getFluidState(pos);
/* 112 */     if (fluidState.getType().isSame(this)) {
/* 113 */       return false;
/*     */     }
/* 115 */     if (direction == Direction.UP) {
/* 116 */       return true;
/*     */     }
/* 118 */     if (state.getBlock() instanceof net.minecraft.world.level.block.IceBlock) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     return state.isFaceSturdy(level, pos, direction);
/*     */   }
/*     */   
/*     */   protected void spread(ServerLevel level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 126 */     if (fluidState.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     BlockPos belowPos = pos.below();
/* 131 */     BlockState belowState = level.getBlockState(belowPos);
/* 132 */     FluidState belowFluid = belowState.getFluidState();
/*     */     
/* 134 */     if (canMaybePassThrough(level, pos, state, Direction.DOWN, belowPos, belowState, belowFluid)) {
/* 135 */       FluidState newBelowFluid = getNewLiquid(level, belowPos, belowState);
/* 136 */       Fluid newBelowFluidType = newBelowFluid.getType();
/* 137 */       if (belowFluid.canBeReplacedWith(level, belowPos, newBelowFluidType, Direction.DOWN) && canHoldSpecificFluid(level, belowPos, belowState, newBelowFluidType)) {
/* 138 */         spreadTo(level, belowPos, belowState, Direction.DOWN, newBelowFluid);
/*     */         
/* 140 */         if (sourceNeighborCount(level, pos) >= 3) {
/* 141 */           spreadToSides(level, pos, fluidState, state);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 147 */     if (fluidState.isSource() || !isWaterHole(level, pos, state, belowPos, belowState)) {
/* 148 */       spreadToSides(level, pos, fluidState, state);
/*     */     }
/*     */   }
/*     */   
/*     */   private void spreadToSides(ServerLevel level, BlockPos pos, FluidState fluidState, BlockState state) {
/* 153 */     int neighbor = fluidState.getAmount() - getDropOff(level);
/* 154 */     if (((Boolean)fluidState.getValue(FALLING)).booleanValue()) {
/* 155 */       neighbor = 7;
/*     */     }
/* 157 */     if (neighbor <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     Map<Direction, FluidState> spreads = getSpread(level, pos, state);
/* 162 */     for (Map.Entry<Direction, FluidState> entry : spreads.entrySet()) {
/* 163 */       Direction spread = (Direction)entry.getKey();
/* 164 */       FluidState newNeighborFluid = (FluidState)entry.getValue();
/* 165 */       BlockPos neighborPos = pos.relative(spread);
/* 166 */       spreadTo(level, neighborPos, level.getBlockState(neighborPos), spread, newNeighborFluid);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected FluidState getNewLiquid(ServerLevel level, BlockPos pos, BlockState state) {
/* 171 */     int highestNeighbor = 0;
/* 172 */     int neighbourSources = 0;
/*     */     
/* 174 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */     
/* 176 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 177 */       BlockPos.MutableBlockPos mutableBlockPos1 = mutablePos.setWithOffset(pos, direction);
/* 178 */       BlockState blockState = level.getBlockState(mutableBlockPos1);
/* 179 */       FluidState fluidState = blockState.getFluidState();
/* 180 */       if (!fluidState.getType().isSame(this)) {
/*     */         continue;
/*     */       }
/* 183 */       if (canPassThroughWall(direction, level, pos, state, mutableBlockPos1, blockState)) {
/* 184 */         if (fluidState.isSource()) {
/* 185 */           neighbourSources++;
/*     */         }
/* 187 */         highestNeighbor = Math.max(highestNeighbor, fluidState.getAmount());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 192 */     if (neighbourSources >= 2 && canConvertToSource(level)) {
/* 193 */       BlockState belowState = level.getBlockState(mutablePos.setWithOffset(pos, Direction.DOWN));
/* 194 */       FluidState belowFluid = belowState.getFluidState();
/* 195 */       if (belowState.isSolid() || isSourceBlockOfThisType(belowFluid)) {
/* 196 */         return getSource(false);
/*     */       }
/*     */     } 
/*     */     
/* 200 */     BlockPos.MutableBlockPos mutableBlockPos = mutablePos.setWithOffset(pos, Direction.UP);
/* 201 */     BlockState aboveState = level.getBlockState(mutableBlockPos);
/* 202 */     FluidState aboveFluid = aboveState.getFluidState();
/*     */     
/* 204 */     if (!aboveFluid.isEmpty() && aboveFluid.getType().isSame(this) && canPassThroughWall(Direction.UP, level, pos, state, mutableBlockPos, aboveState)) {
/* 205 */       return getFlowing(8, true);
/*     */     }
/*     */     
/* 208 */     int amount = highestNeighbor - getDropOff(level);
/* 209 */     if (amount <= 0) {
/* 210 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/* 212 */     return getFlowing(amount, false);
/*     */   }
/*     */   private static final class BlockStatePairKey extends Record { private final BlockState first;
/* 215 */     private BlockStatePairKey(BlockState first, BlockState second, Direction direction) { this.first = first; this.second = second; this.direction = direction; } private final BlockState second; private final Direction direction; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/material/FlowingFluid$BlockStatePairKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 215 */       //   0	7	0	this	Lnet/minecraft/world/level/material/FlowingFluid$BlockStatePairKey; } public BlockState first() { return this.first; } public BlockState second() { return this.second; } public Direction direction() { return this.direction; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     public boolean equals(Object o) { if (o instanceof BlockStatePairKey) { BlockStatePairKey that = (BlockStatePairKey)o; if (this.first == that.first && this.second == that.second && this.direction == that.direction); }  return false; }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 228 */       result = System.identityHashCode(this.first);
/* 229 */       result = 31 * result + System.identityHashCode(this.second);
/* 230 */       return 31 * result + this.direction.hashCode();
/*     */     } }
/*     */   
/*     */   private static boolean canPassThroughWall(Direction direction, BlockGetter level, BlockPos sourcePos, BlockState sourceState, BlockPos targetPos, BlockState targetState) {
/*     */     BlockStatePairKey key;
/*     */     Object2ByteLinkedOpenHashMap<BlockStatePairKey> cache;
/* 236 */     if (SharedConstants.DEBUG_DISABLE_LIQUID_SPREADING || (SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD && targetPos.getZ() < 0)) {
/* 237 */       return false;
/*     */     }
/*     */     
/* 240 */     VoxelShape targetShape = targetState.getCollisionShape(level, targetPos);
/* 241 */     if (targetShape == Shapes.block()) {
/* 242 */       return false;
/*     */     }
/*     */     
/* 245 */     VoxelShape sourceShape = sourceState.getCollisionShape(level, sourcePos);
/* 246 */     if (sourceShape == Shapes.block()) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (sourceShape == Shapes.empty() && targetShape == Shapes.empty()) {
/* 251 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 255 */     if (sourceState.getBlock().hasDynamicShape() || targetState.getBlock().hasDynamicShape()) {
/* 256 */       cache = null;
/*     */     } else {
/* 258 */       cache = (Object2ByteLinkedOpenHashMap)OCCLUSION_CACHE.get();
/*     */     } 
/*     */ 
/*     */     
/* 262 */     if (cache != null) {
/* 263 */       key = new BlockStatePairKey(sourceState, targetState, direction);
/* 264 */       byte cached = cache.getAndMoveToFirst(key);
/* 265 */       if (cached != Byte.MAX_VALUE) {
/* 266 */         return (cached != 0);
/*     */       }
/*     */     } else {
/* 269 */       key = null;
/*     */     } 
/*     */     
/* 272 */     boolean result = !Shapes.mergedFaceOccludes(sourceShape, targetShape, direction);
/*     */     
/* 274 */     if (cache != null) {
/* 275 */       if (cache.size() == 200) {
/* 276 */         cache.removeLastByte();
/*     */       }
/* 278 */       cache.putAndMoveToFirst(key, (byte)(result ? 1 : 0));
/*     */     } 
/* 280 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 286 */   public FluidState getFlowing(int amount, boolean falling) { return (FluidState)((FluidState)getFlowing().defaultFluidState().setValue(LEVEL, Integer.valueOf(amount))).setValue(FALLING, Boolean.valueOf(falling)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 292 */   public FluidState getSource(boolean falling) { return (FluidState)getSource().defaultFluidState().setValue(FALLING, Boolean.valueOf(falling)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState target) {
/* 298 */     Block block = state.getBlock(); if (block instanceof LiquidBlockContainer) { LiquidBlockContainer container = (LiquidBlockContainer)block;
/* 299 */       container.placeLiquid(level, pos, state, target); }
/*     */     else
/* 301 */     { if (!state.isAir()) {
/* 302 */         beforeDestroyingBlock(level, pos, state);
/*     */       }
/* 304 */       level.setBlock(pos, target.createLegacyBlock(), 3); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSlopeDistance(LevelReader level, BlockPos pos, int pass, Direction from, BlockState state, SpreadContext context) {
/* 311 */     int lowest = 1000;
/*     */     
/* 313 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 314 */       if (direction == from) {
/*     */         continue;
/*     */       }
/*     */       
/* 318 */       BlockPos testPos = pos.relative(direction);
/*     */       
/* 320 */       BlockState testState = context.getBlockState(testPos);
/* 321 */       FluidState testFluidState = testState.getFluidState();
/*     */ 
/*     */       
/* 324 */       if (canPassThrough(level, getFlowing(), pos, state, direction, testPos, testState, testFluidState)) {
/* 325 */         if (context.isHole(testPos)) {
/* 326 */           return pass;
/*     */         }
/* 328 */         if (pass < getSlopeFindDistance(level)) {
/* 329 */           int v = getSlopeDistance(level, testPos, pass + 1, direction.getOpposite(), testState, context);
/* 330 */           if (v < lowest) {
/* 331 */             lowest = v;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 336 */     return lowest;
/*     */   }
/*     */   
/*     */   private boolean isWaterHole(BlockGetter level, BlockPos topPos, BlockState topState, BlockPos bottomPos, BlockState bottomState) {
/* 340 */     if (!canPassThroughWall(Direction.DOWN, level, topPos, topState, bottomPos, bottomState)) {
/* 341 */       return false;
/*     */     }
/*     */     
/* 344 */     if (bottomState.getFluidState().getType().isSame(this)) {
/* 345 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 349 */     return canHoldFluid(level, bottomPos, bottomState, getFlowing());
/*     */   }
/*     */   
/*     */   private boolean canPassThrough(BlockGetter level, Fluid fluid, BlockPos sourcePos, BlockState sourceState, Direction direction, BlockPos testPos, BlockState testState, FluidState testFluidState) {
/* 353 */     return (canMaybePassThrough(level, sourcePos, sourceState, direction, testPos, testState, testFluidState) && 
/* 354 */       canHoldSpecificFluid(level, testPos, testState, fluid));
/*     */   }
/*     */   
/*     */   private boolean canMaybePassThrough(BlockGetter level, BlockPos sourcePos, BlockState sourceState, Direction direction, BlockPos testPos, BlockState testState, FluidState testFluidState) {
/* 358 */     return (!isSourceBlockOfThisType(testFluidState) && 
/* 359 */       canHoldAnyFluid(testState) && 
/* 360 */       canPassThroughWall(direction, level, sourcePos, sourceState, testPos, testState));
/*     */   }
/*     */ 
/*     */   
/* 364 */   private boolean isSourceBlockOfThisType(FluidState state) { return (state.getType().isSame(this) && state.isSource()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int sourceNeighborCount(LevelReader level, BlockPos pos) {
/* 370 */     int count = 0;
/* 371 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 372 */       BlockPos testPos = pos.relative(direction);
/* 373 */       FluidState testFluidState = level.getFluidState(testPos);
/*     */       
/* 375 */       if (isSourceBlockOfThisType(testFluidState)) {
/* 376 */         count++;
/*     */       }
/*     */     } 
/*     */     
/* 380 */     return count;
/*     */   }
/*     */   protected class SpreadContext { private final BlockGetter level; private final BlockPos origin;
/*     */     private final Short2ObjectMap<BlockState> stateCache;
/*     */     private final Short2BooleanMap holeCache;
/*     */     
/*     */     private SpreadContext(BlockGetter level, BlockPos origin) {
/* 387 */       this.stateCache = new Short2ObjectOpenHashMap();
/* 388 */       this.holeCache = new Short2BooleanOpenHashMap();
/*     */ 
/*     */       
/* 391 */       this.level = level;
/* 392 */       this.origin = origin;
/*     */     }
/*     */ 
/*     */     
/* 396 */     public BlockState getBlockState(BlockPos pos) { return getBlockState(pos, getCacheKey(pos)); }
/*     */ 
/*     */ 
/*     */     
/* 400 */     private BlockState getBlockState(BlockPos pos, short key) { return (BlockState)this.stateCache.computeIfAbsent(key, k -> this.level.getBlockState(pos)); }
/*     */ 
/*     */     
/*     */     public boolean isHole(BlockPos pos) {
/* 404 */       return this.holeCache.computeIfAbsent(getCacheKey(pos), key -> {
/* 405 */             BlockState state = getBlockState(pos, key);
/* 406 */             BlockPos below = pos.below();
/* 407 */             BlockState belowState = this.level.getBlockState(below);
/* 408 */             return FlowingFluid.this.isWaterHole(this.level, pos, state, below, belowState);
/*     */           });
/*     */     }
/*     */     
/*     */     private short getCacheKey(BlockPos pos) {
/* 413 */       int relativeX = pos.getX() - this.origin.getX();
/* 414 */       int relativeZ = pos.getZ() - this.origin.getZ();
/* 415 */       return (short)((relativeX + 128 & 0xFF) << 8 | relativeZ + 128 & 0xFF);
/*     */     } }
/*     */ 
/*     */   
/*     */   protected Map<Direction, FluidState> getSpread(ServerLevel level, BlockPos pos, BlockState state) {
/* 420 */     int lowest = 1000;
/* 421 */     Map<Direction, FluidState> result = Maps.newEnumMap(Direction.class);
/*     */     
/* 423 */     SpreadContext context = null;
/*     */     
/* 425 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 426 */       int distance; BlockPos testPos = pos.relative(direction);
/*     */       
/* 428 */       BlockState testState = level.getBlockState(testPos);
/* 429 */       FluidState testFluidState = testState.getFluidState();
/* 430 */       if (!canMaybePassThrough(level, pos, state, direction, testPos, testState, testFluidState)) {
/*     */         continue;
/*     */       }
/*     */       
/* 434 */       FluidState newFluid = getNewLiquid(level, testPos, testState);
/* 435 */       if (!canHoldSpecificFluid(level, testPos, testState, newFluid.getType())) {
/*     */         continue;
/*     */       }
/*     */       
/* 439 */       if (context == null) {
/* 440 */         context = new SpreadContext(level, pos);
/*     */       }
/*     */ 
/*     */       
/* 444 */       if (context.isHole(testPos)) {
/* 445 */         distance = 0;
/*     */       } else {
/* 447 */         distance = getSlopeDistance(level, testPos, 1, direction.getOpposite(), testState, context);
/*     */       } 
/*     */       
/* 450 */       if (distance < lowest) {
/* 451 */         result.clear();
/*     */       }
/*     */       
/* 454 */       if (distance <= lowest) {
/* 455 */         if (testFluidState.canBeReplacedWith(level, testPos, newFluid.getType(), direction)) {
/* 456 */           result.put(direction, newFluid);
/*     */         }
/* 458 */         lowest = distance;
/*     */       } 
/*     */     } 
/*     */     
/* 462 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean canHoldAnyFluid(BlockState state) {
/* 466 */     Block block = state.getBlock();
/* 467 */     if (block instanceof LiquidBlockContainer) {
/* 468 */       return true;
/*     */     }
/*     */     
/* 471 */     if (state.blocksMotion()) {
/* 472 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 476 */     return (!(block instanceof net.minecraft.world.level.block.DoorBlock) && 
/* 477 */       !state.is(BlockTags.SIGNS) && 
/* 478 */       !state.is(Blocks.LADDER) && 
/* 479 */       !state.is(Blocks.SUGAR_CANE) && 
/* 480 */       !state.is(Blocks.BUBBLE_COLUMN) && 
/* 481 */       !state.is(Blocks.NETHER_PORTAL) && 
/* 482 */       !state.is(Blocks.END_PORTAL) && 
/* 483 */       !state.is(Blocks.END_GATEWAY) && 
/* 484 */       !state.is(Blocks.STRUCTURE_VOID));
/*     */   }
/*     */ 
/*     */   
/* 488 */   private static boolean canHoldFluid(BlockGetter level, BlockPos pos, BlockState state, Fluid newFluid) { return (canHoldAnyFluid(state) && canHoldSpecificFluid(level, pos, state, newFluid)); }
/*     */ 
/*     */   
/*     */   private static boolean canHoldSpecificFluid(BlockGetter level, BlockPos pos, BlockState state, Fluid newFluid) {
/* 492 */     Block block = state.getBlock();
/* 493 */     if (block instanceof LiquidBlockContainer) { LiquidBlockContainer container = (LiquidBlockContainer)block;
/* 494 */       return container.canPlaceLiquid(null, level, pos, state, newFluid); }
/*     */     
/* 496 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 502 */   protected int getSpreadDelay(Level level, BlockPos pos, FluidState oldFluidState, FluidState newFluidState) { return getTickDelay(level); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState) {
/* 507 */     if (!fluidState.isSource()) {
/* 508 */       FluidState newFluidState = getNewLiquid(level, pos, level.getBlockState(pos));
/* 509 */       int tickDelay = getSpreadDelay(level, pos, fluidState, newFluidState);
/*     */       
/* 511 */       if (newFluidState.isEmpty()) {
/* 512 */         fluidState = newFluidState;
/* 513 */         blockState = Blocks.AIR.defaultBlockState();
/* 514 */         level.setBlock(pos, blockState, 3);
/* 515 */       } else if (newFluidState != fluidState) {
/* 516 */         fluidState = newFluidState;
/* 517 */         blockState = fluidState.createLegacyBlock();
/* 518 */         level.setBlock(pos, blockState, 3);
/* 519 */         level.scheduleTick(pos, fluidState.getType(), tickDelay);
/*     */       } 
/*     */     } 
/*     */     
/* 523 */     spread(level, pos, blockState, fluidState);
/*     */   }
/*     */   
/*     */   protected static int getLegacyLevel(FluidState fluidState) {
/* 527 */     if (fluidState.isSource()) {
/* 528 */       return 0;
/*     */     }
/* 530 */     return 8 - Math.min(fluidState.getAmount(), 8) + (((Boolean)fluidState.getValue(FALLING)).booleanValue() ? 8 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 535 */   private static boolean hasSameAbove(FluidState fluidState, BlockGetter level, BlockPos pos) { return fluidState.getType().isSame(level.getFluidState(pos.above()).getType()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeight(FluidState fluidState, BlockGetter level, BlockPos pos) {
/* 540 */     if (hasSameAbove(fluidState, level, pos)) {
/* 541 */       return 1.0F;
/*     */     }
/* 543 */     return fluidState.getOwnHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 548 */   public float getOwnHeight(FluidState fluidState) { return fluidState.getAmount() / 9.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
/* 556 */     if (state.getAmount() == 9 && hasSameAbove(state, level, pos)) {
/* 557 */       return Shapes.block();
/*     */     }
/*     */     
/* 560 */     return (VoxelShape)this.shapes.computeIfAbsent(state, fluidState -> Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, fluidState.getHeight(level, pos), 1.0D));
/*     */   }
/*     */   
/*     */   public abstract Fluid getFlowing();
/*     */   
/*     */   public abstract Fluid getSource();
/*     */   
/*     */   protected abstract boolean canConvertToSource(ServerLevel paramServerLevel);
/*     */   
/*     */   protected abstract void beforeDestroyingBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState);
/*     */   
/*     */   protected abstract int getSlopeFindDistance(LevelReader paramLevelReader);
/*     */   
/*     */   protected abstract int getDropOff(LevelReader paramLevelReader);
/*     */   
/*     */   public abstract int getAmount(FluidState paramFluidState);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\FlowingFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */