/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DripstoneThickness;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PointedDripstoneBlock
/*     */   extends Block implements SimpleWaterloggedBlock, Fallable {
/*  49 */   public static final MapCodec<PointedDripstoneBlock> CODEC = simpleCodec(PointedDripstoneBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  53 */   public MapCodec<PointedDripstoneBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  56 */   public static final EnumProperty<Direction> TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
/*  57 */   public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
/*  58 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   private static final int MAX_SEARCH_LENGTH_WHEN_CHECKING_DRIP_TYPE = 11;
/*     */   
/*     */   private static final int DELAY_BEFORE_FALLING = 2;
/*     */   
/*     */   private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK = 0.02F;
/*     */   
/*     */   private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK_IF_UNDER_LIQUID_SOURCE = 0.12F;
/*     */   
/*     */   private static final int MAX_SEARCH_LENGTH_BETWEEN_STALACTITE_TIP_AND_CAULDRON = 11;
/*     */   
/*     */   private static final float WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.17578125F;
/*     */   
/*     */   private static final float LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.05859375F;
/*     */   
/*     */   private static final double MIN_TRIDENT_VELOCITY_TO_BREAK_DRIPSTONE = 0.6D;
/*     */   
/*     */   private static final float STALACTITE_DAMAGE_PER_FALL_DISTANCE_AND_SIZE = 1.0F;
/*     */   
/*     */   private static final int STALACTITE_MAX_DAMAGE = 40;
/*     */   
/*     */   private static final int MAX_STALACTITE_HEIGHT_FOR_DAMAGE_CALCULATION = 6;
/*     */   private static final float STALAGMITE_FALL_DISTANCE_OFFSET = 2.5F;
/*     */   private static final int STALAGMITE_FALL_DAMAGE_MODIFIER = 2;
/*     */   private static final float AVERAGE_DAYS_PER_GROWTH = 5.0F;
/*     */   private static final float GROWTH_PROBABILITY_PER_RANDOM_TICK = 0.011377778F;
/*     */   private static final int MAX_GROWTH_LENGTH = 7;
/*     */   private static final int MAX_STALAGMITE_SEARCH_RANGE_WHEN_GROWING = 10;
/*  87 */   private static final VoxelShape SHAPE_TIP_MERGE = Block.column(6.0D, 0.0D, 16.0D);
/*  88 */   private static final VoxelShape SHAPE_TIP_UP = Block.column(6.0D, 0.0D, 11.0D);
/*  89 */   private static final VoxelShape SHAPE_TIP_DOWN = Block.column(6.0D, 5.0D, 16.0D);
/*  90 */   private static final VoxelShape SHAPE_FRUSTUM = Block.column(8.0D, 0.0D, 16.0D);
/*  91 */   private static final VoxelShape SHAPE_MIDDLE = Block.column(10.0D, 0.0D, 16.0D);
/*  92 */   private static final VoxelShape SHAPE_BASE = Block.column(12.0D, 0.0D, 16.0D);
/*     */   
/*  94 */   private static final double STALACTITE_DRIP_START_PIXEL = SHAPE_TIP_DOWN.min(Direction.Axis.Y);
/*     */ 
/*     */   
/*  97 */   private static final float MAX_HORIZONTAL_OFFSET = (float)SHAPE_BASE.min(Direction.Axis.X);
/*     */   
/*  99 */   private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.column(4.0D, 0.0D, 16.0D);
/*     */   
/*     */   public PointedDripstoneBlock(BlockBehaviour.Properties properties) {
/* 102 */     super(properties);
/* 103 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/* 104 */         .setValue(TIP_DIRECTION, Direction.UP))
/* 105 */         .setValue(THICKNESS, DripstoneThickness.TIP))
/* 106 */         .setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { TIP_DIRECTION, THICKNESS, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return isValidPointedDripstonePlacement(level, pos, (Direction)state.getValue(TIP_DIRECTION)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 128 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 129 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 132 */     if (directionToNeighbour != Direction.UP && directionToNeighbour != Direction.DOWN) {
/* 133 */       return state;
/*     */     }
/*     */     
/* 136 */     Direction tipDirection = (Direction)state.getValue(TIP_DIRECTION);
/* 137 */     if (tipDirection == Direction.DOWN && ticks.getBlockTicks().hasScheduledTick(pos, this))
/*     */     {
/* 139 */       return state;
/*     */     }
/*     */     
/* 142 */     if (directionToNeighbour == tipDirection.getOpposite() && !canSurvive(state, level, pos)) {
/* 143 */       if (tipDirection == Direction.DOWN) {
/*     */         
/* 145 */         ticks.scheduleTick(pos, this, 2);
/*     */       } else {
/*     */         
/* 148 */         ticks.scheduleTick(pos, this, 1);
/*     */       } 
/* 150 */       return state;
/*     */     } 
/*     */     
/* 153 */     boolean mergeOpposingTips = (state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE);
/* 154 */     DripstoneThickness newThickness = calculateDripstoneThickness(level, pos, tipDirection, mergeOpposingTips);
/*     */     
/* 156 */     return (BlockState)state.setValue(THICKNESS, newThickness);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 161 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/* 164 */     BlockPos blockPos = blockHit.getBlockPos();
/* 165 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (projectile.mayInteract(serverLevel, blockPos) && projectile.mayBreak(serverLevel) && projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident && projectile.getDeltaMovement().length() > 0.6D) {
/* 166 */         level.destroyBlock(blockPos, true);
/*     */       } }
/*     */   
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
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 184 */     if (state.getValue(TIP_DIRECTION) == Direction.UP && state.getValue(THICKNESS) == DripstoneThickness.TIP) {
/* 185 */       entity.causeFallDamage(fallDistance + 2.5D, 2.0F, level.damageSources().stalagmite());
/*     */     } else {
/* 187 */       super.fallOn(level, state, pos, entity, fallDistance);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 193 */     if (!canDrip(state)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 198 */     float randomValue = random.nextFloat();
/* 199 */     if (randomValue > 0.12F) {
/*     */       return;
/*     */     }
/*     */     
/* 203 */     getFluidAboveStalactite(level, pos, state)
/*     */       
/* 205 */       .filter(fluidAbove -> (randomValue < 0.02F || canFillCauldron(fluidAbove.fluid)))
/* 206 */       .ifPresent(fluidAbove -> spawnDripParticle(level, pos, state, fluidAbove.fluid, fluidAbove.pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 211 */     if (isStalagmite(state) && !canSurvive(state, level, pos)) {
/* 212 */       level.destroyBlock(pos, true);
/*     */     } else {
/* 214 */       spawnFallingStalactite(state, level, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 220 */     maybeTransferFluid(state, level, pos, random.nextFloat());
/*     */     
/* 222 */     if (random.nextFloat() < 0.011377778F && isStalactiteStartPos(state, level, pos))
/* 223 */       growStalactiteOrStalagmiteIfPossible(state, level, pos, random); 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randomValue) {
/*     */     float transferProbability;
/* 229 */     if (randomValue > 0.17578125F && randomValue > 0.05859375F) {
/*     */       return;
/*     */     }
/*     */     
/* 233 */     if (!isStalactiteStartPos(state, level, pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 237 */     Optional<FluidInfo> fluidInfo = getFluidAboveStalactite(level, pos, state);
/* 238 */     if (fluidInfo.isEmpty()) {
/*     */       return;
/*     */     }
/* 241 */     Fluid fluid = ((FluidInfo)fluidInfo.get()).fluid;
/*     */ 
/*     */     
/* 244 */     if (fluid == Fluids.WATER) {
/* 245 */       transferProbability = 0.17578125F;
/* 246 */     } else if (fluid == Fluids.LAVA) {
/* 247 */       transferProbability = 0.05859375F;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 251 */     if (randomValue >= transferProbability) {
/*     */       return;
/*     */     }
/*     */     
/* 255 */     BlockPos stalactiteTipPos = findTip(state, level, pos, 11, false);
/* 256 */     if (stalactiteTipPos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 260 */     if (((FluidInfo)fluidInfo.get()).sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
/* 261 */       BlockState newState = Blocks.CLAY.defaultBlockState();
/* 262 */       level.setBlockAndUpdate(((FluidInfo)fluidInfo.get()).pos, newState);
/* 263 */       Block.pushEntitiesUp(((FluidInfo)fluidInfo.get()).sourceState, newState, level, ((FluidInfo)fluidInfo.get()).pos);
/* 264 */       level.gameEvent(GameEvent.BLOCK_CHANGE, ((FluidInfo)fluidInfo.get()).pos, GameEvent.Context.of(newState));
/* 265 */       level.levelEvent(1504, stalactiteTipPos, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 269 */     BlockPos cauldronPos = findFillableCauldronBelowStalactiteTip(level, stalactiteTipPos, fluid);
/* 270 */     if (cauldronPos == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 275 */     level.levelEvent(1504, stalactiteTipPos, 0);
/*     */ 
/*     */     
/* 278 */     int fallDistance = stalactiteTipPos.getY() - cauldronPos.getY();
/* 279 */     int delay = 50 + fallDistance;
/* 280 */     BlockState cauldronState = level.getBlockState(cauldronPos);
/* 281 */     level.scheduleTick(cauldronPos, cauldronState.getBlock(), delay);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 286 */     Level level1 = context.getLevel();
/* 287 */     BlockPos pos = context.getClickedPos();
/* 288 */     Direction defaultTipDirection = context.getNearestLookingVerticalDirection().getOpposite();
/* 289 */     Direction tipDirection = calculateTipDirection(level1, pos, defaultTipDirection);
/* 290 */     if (tipDirection == null) {
/* 291 */       return null;
/*     */     }
/*     */     
/* 294 */     boolean mergeOpposingTips = !context.isSecondaryUseActive();
/* 295 */     DripstoneThickness thickness = calculateDripstoneThickness(level1, pos, tipDirection, mergeOpposingTips);
/*     */     
/* 297 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/* 298 */       .setValue(TIP_DIRECTION, tipDirection))
/* 299 */       .setValue(THICKNESS, thickness))
/* 300 */       .setValue(WATERLOGGED, Boolean.valueOf((level1.getFluidState(pos).getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 305 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 306 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 308 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 313 */     switch ((DripstoneThickness)state.getValue(THICKNESS)) { default: throw new MatchException(null, null);
/*     */       
/*     */       case TIP_MERGE:
/*     */       
/*     */       case TIP:
/* 318 */         shape = (state.getValue(TIP_DIRECTION) == Direction.DOWN) ? SHAPE_TIP_DOWN : SHAPE_TIP_UP;
/*     */         
/* 320 */         return shape.move(state.getOffset(pos));case FRUSTUM: case MIDDLE: case BASE: break; }  VoxelShape shape = SHAPE_BASE; return shape.move(state.getOffset(pos));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 325 */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 330 */   protected float getMaxHorizontalOffset() { return MAX_HORIZONTAL_OFFSET; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
/* 335 */     if (!entity.isSilent()) {
/* 336 */       level.levelEvent(1045, pos, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 342 */   public DamageSource getFallDamageSource(Entity entity) { return entity.damageSources().fallingStalactite(entity); }
/*     */ 
/*     */   
/*     */   private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
/* 346 */     BlockPos.MutableBlockPos fallPos = pos.mutable();
/* 347 */     BlockState fallState = state;
/*     */     
/* 349 */     while (isStalactite(fallState)) {
/* 350 */       FallingBlockEntity entity = FallingBlockEntity.fall(level, fallPos, fallState);
/* 351 */       if (isTip(fallState, true)) {
/*     */ 
/*     */         
/* 354 */         int size = Math.max(1 + pos.getY() - fallPos.getY(), 6);
/* 355 */         float damagePerFallDistance = 1.0F * size;
/*     */         
/* 357 */         entity.setHurtsEntities(damagePerFallDistance, 40);
/*     */         break;
/*     */       } 
/* 360 */       fallPos.move(Direction.DOWN);
/* 361 */       fallState = level.getBlockState(fallPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void growStalactiteOrStalagmiteIfPossible(BlockState stalactiteStartState, ServerLevel level, BlockPos stalactiteStartPos, RandomSource random) {
/* 367 */     BlockState rootState = level.getBlockState(stalactiteStartPos.above(1));
/* 368 */     BlockState stateAbove = level.getBlockState(stalactiteStartPos.above(2));
/*     */     
/* 370 */     if (!canGrow(rootState, stateAbove)) {
/*     */       return;
/*     */     }
/*     */     
/* 374 */     BlockPos stalactiteTipPos = findTip(stalactiteStartState, level, stalactiteStartPos, 7, false);
/* 375 */     if (stalactiteTipPos == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 380 */     BlockState stalactiteTipState = level.getBlockState(stalactiteTipPos);
/* 381 */     if (!canDrip(stalactiteTipState) || !canTipGrow(stalactiteTipState, level, stalactiteTipPos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 386 */     if (random.nextBoolean()) {
/* 387 */       grow(level, stalactiteTipPos, Direction.DOWN);
/*     */     } else {
/* 389 */       growStalagmiteBelow(level, stalactiteTipPos);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void growStalagmiteBelow(ServerLevel level, BlockPos posAboveStalagmite) {
/* 397 */     BlockPos.MutableBlockPos pos = posAboveStalagmite.mutable();
/* 398 */     for (int i = 0; i < 10; i++) {
/* 399 */       pos.move(Direction.DOWN);
/* 400 */       BlockState state = level.getBlockState(pos);
/* 401 */       if (!state.getFluidState().isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 405 */       if (isUnmergedTipWithDirection(state, Direction.UP) && canTipGrow(state, level, pos)) {
/*     */         
/* 407 */         grow(level, pos, Direction.UP);
/*     */         return;
/*     */       } 
/* 410 */       if (isValidPointedDripstonePlacement(level, pos, Direction.UP) && !level.isWaterAt(pos.below())) {
/*     */         
/* 412 */         grow(level, pos.below(), Direction.UP);
/*     */         return;
/*     */       } 
/* 415 */       if (!canDripThrough(level, pos, state)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void grow(ServerLevel level, BlockPos growFromPos, Direction growToDirection) {
/* 422 */     BlockPos targetPos = growFromPos.relative(growToDirection);
/* 423 */     BlockState existingStateAtTargetPos = level.getBlockState(targetPos);
/* 424 */     if (isUnmergedTipWithDirection(existingStateAtTargetPos, growToDirection.getOpposite())) {
/* 425 */       createMergedTips(existingStateAtTargetPos, level, targetPos);
/* 426 */     } else if (existingStateAtTargetPos.isAir() || existingStateAtTargetPos.is(Blocks.WATER)) {
/* 427 */       createDripstone(level, targetPos, growToDirection, DripstoneThickness.TIP);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createDripstone(LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
/* 435 */     BlockState state = (BlockState)((BlockState)((BlockState)Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(TIP_DIRECTION, direction)).setValue(THICKNESS, thickness)).setValue(WATERLOGGED, Boolean.valueOf((level.getFluidState(pos).getType() == Fluids.WATER)));
/* 436 */     level.setBlock(pos, state, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createMergedTips(BlockState tipState, LevelAccessor level, BlockPos tipPos) {
/*     */     BlockPos stalagmitePos, stalactitePos;
/* 445 */     if (tipState.getValue(TIP_DIRECTION) == Direction.UP) {
/* 446 */       stalagmitePos = tipPos;
/* 447 */       stalactitePos = tipPos.above();
/*     */     } else {
/* 449 */       stalactitePos = tipPos;
/* 450 */       stalagmitePos = tipPos.below();
/*     */     } 
/*     */     
/* 453 */     createDripstone(level, stalactitePos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
/* 454 */     createDripstone(level, stalagmitePos, Direction.UP, DripstoneThickness.TIP_MERGE);
/*     */   }
/*     */ 
/*     */   
/* 458 */   public static void spawnDripParticle(Level level, BlockPos stalactiteTipPos, BlockState stalactiteTipState) { getFluidAboveStalactite(level, stalactiteTipPos, stalactiteTipState).ifPresent(fluidAbove -> spawnDripParticle(level, stalactiteTipPos, stalactiteTipState, fluidAbove.fluid, fluidAbove.pos)); }
/*     */ 
/*     */   
/*     */   private static void spawnDripParticle(Level level, BlockPos stalactiteTipPos, BlockState stalactiteTipState, Fluid fluidAbove, BlockPos posAbove) {
/* 462 */     Vec3 offset = stalactiteTipState.getOffset(stalactiteTipPos);
/* 463 */     double PIXEL_SIZE = 0.0625D;
/* 464 */     double x = stalactiteTipPos.getX() + 0.5D + offset.x;
/* 465 */     double y = stalactiteTipPos.getY() + STALACTITE_DRIP_START_PIXEL - 0.0625D;
/* 466 */     double z = stalactiteTipPos.getZ() + 0.5D + offset.z;
/*     */     
/* 468 */     ParticleOptions dripParticle = getDripParticle(level, fluidAbove, posAbove);
/* 469 */     level.addParticle(dripParticle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private static BlockPos findTip(BlockState dripstoneState, LevelAccessor level, BlockPos dripstonePos, int maxSearchLength, boolean includeMergedTip) {
/* 473 */     if (isTip(dripstoneState, includeMergedTip)) {
/* 474 */       return dripstonePos;
/*     */     }
/* 476 */     Direction searchDirection = (Direction)dripstoneState.getValue(TIP_DIRECTION);
/* 477 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> (state.is(Blocks.POINTED_DRIPSTONE) && state.getValue(TIP_DIRECTION) == searchDirection);
/* 478 */     return (BlockPos)findBlockVertical(level, dripstonePos, searchDirection.getAxisDirection(), pathPredicate, dripstone -> isTip(dripstone, includeMergedTip), maxSearchLength).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction defaultTipDirection) {
/*     */     Direction tipDirection;
/* 486 */     if (isValidPointedDripstonePlacement(level, pos, defaultTipDirection)) {
/* 487 */       tipDirection = defaultTipDirection;
/* 488 */     } else if (isValidPointedDripstonePlacement(level, pos, defaultTipDirection.getOpposite())) {
/* 489 */       tipDirection = defaultTipDirection.getOpposite();
/*     */     } else {
/* 491 */       return null;
/*     */     } 
/* 493 */     return tipDirection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DripstoneThickness calculateDripstoneThickness(LevelReader level, BlockPos pos, Direction tipDirection, boolean mergeOpposingTips) {
/* 500 */     Direction baseDirection = tipDirection.getOpposite();
/* 501 */     BlockState inFrontState = level.getBlockState(pos.relative(tipDirection));
/*     */     
/* 503 */     if (isPointedDripstoneWithDirection(inFrontState, baseDirection)) {
/*     */       
/* 505 */       if (mergeOpposingTips || inFrontState.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE) {
/* 506 */         return DripstoneThickness.TIP_MERGE;
/*     */       }
/* 508 */       return DripstoneThickness.TIP;
/*     */     } 
/*     */ 
/*     */     
/* 512 */     if (!isPointedDripstoneWithDirection(inFrontState, tipDirection)) {
/* 513 */       return DripstoneThickness.TIP;
/*     */     }
/*     */ 
/*     */     
/* 517 */     DripstoneThickness inFrontThickness = (DripstoneThickness)inFrontState.getValue(THICKNESS);
/* 518 */     if (inFrontThickness == DripstoneThickness.TIP || inFrontThickness == DripstoneThickness.TIP_MERGE) {
/* 519 */       return DripstoneThickness.FRUSTUM;
/*     */     }
/*     */     
/* 522 */     BlockState behindState = level.getBlockState(pos.relative(baseDirection));
/* 523 */     if (!isPointedDripstoneWithDirection(behindState, tipDirection)) {
/* 524 */       return DripstoneThickness.BASE;
/*     */     }
/* 526 */     return DripstoneThickness.MIDDLE;
/*     */   }
/*     */ 
/*     */   
/* 530 */   public static boolean canDrip(BlockState state) { return (isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !((Boolean)state.getValue(WATERLOGGED)).booleanValue()); }
/*     */ 
/*     */   
/*     */   private static boolean canTipGrow(BlockState tipState, ServerLevel level, BlockPos tipPos) {
/* 534 */     Direction growDirection = (Direction)tipState.getValue(TIP_DIRECTION);
/* 535 */     BlockPos growPos = tipPos.relative(growDirection);
/* 536 */     BlockState stateAtGrowPos = level.getBlockState(growPos);
/*     */     
/* 538 */     if (!stateAtGrowPos.getFluidState().isEmpty()) {
/* 539 */       return false;
/*     */     }
/* 541 */     if (stateAtGrowPos.isAir()) {
/* 542 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 546 */     return isUnmergedTipWithDirection(stateAtGrowPos, growDirection.getOpposite());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState dripStoneState, int maxSearchLength) {
/* 554 */     Direction tipDirection = (Direction)dripStoneState.getValue(TIP_DIRECTION);
/* 555 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pathPos, state) -> (state.is(Blocks.POINTED_DRIPSTONE) && state.getValue(TIP_DIRECTION) == tipDirection);
/* 556 */     return findBlockVertical(level, pos, tipDirection.getOpposite().getAxisDirection(), pathPredicate, state -> !state.is(Blocks.POINTED_DRIPSTONE), maxSearchLength);
/*     */   }
/*     */   
/*     */   private static boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction tipDirection) {
/* 560 */     BlockPos behindPos = pos.relative(tipDirection.getOpposite());
/* 561 */     BlockState behindState = level.getBlockState(behindPos);
/*     */     
/* 563 */     return (behindState.isFaceSturdy(level, behindPos, tipDirection) || isPointedDripstoneWithDirection(behindState, tipDirection));
/*     */   }
/*     */   
/*     */   private static boolean isTip(BlockState state, boolean includeMergedTip) {
/* 567 */     if (!state.is(Blocks.POINTED_DRIPSTONE)) {
/* 568 */       return false;
/*     */     }
/* 570 */     DripstoneThickness thickness = (DripstoneThickness)state.getValue(THICKNESS);
/* 571 */     return (thickness == DripstoneThickness.TIP || (includeMergedTip && thickness == DripstoneThickness.TIP_MERGE));
/*     */   }
/*     */ 
/*     */   
/* 575 */   private static boolean isUnmergedTipWithDirection(BlockState state, Direction tipDirection) { return (isTip(state, false) && state.getValue(TIP_DIRECTION) == tipDirection); }
/*     */ 
/*     */ 
/*     */   
/* 579 */   private static boolean isStalactite(BlockState state) { return isPointedDripstoneWithDirection(state, Direction.DOWN); }
/*     */ 
/*     */ 
/*     */   
/* 583 */   private static boolean isStalagmite(BlockState state) { return isPointedDripstoneWithDirection(state, Direction.UP); }
/*     */ 
/*     */ 
/*     */   
/* 587 */   private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) { return (isStalactite(state) && !level.getBlockState(pos.above()).is(Blocks.POINTED_DRIPSTONE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 592 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 596 */   private static boolean isPointedDripstoneWithDirection(BlockState blockState, Direction tipDirection) { return (blockState.is(Blocks.POINTED_DRIPSTONE) && blockState.getValue(TIP_DIRECTION) == tipDirection); }
/*     */ 
/*     */   
/*     */   private static BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos stalactiteTipPos, Fluid fluid) {
/* 600 */     Predicate<BlockState> cauldronPredicate = state -> (state.getBlock() instanceof AbstractCauldronBlock && ((AbstractCauldronBlock)state.getBlock()).canReceiveStalactiteDrip(fluid));
/* 601 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> canDripThrough(level, pos, state);
/* 602 */     return (BlockPos)findBlockVertical(level, stalactiteTipPos, Direction.DOWN.getAxisDirection(), pathPredicate, cauldronPredicate, 11).orElse(null);
/*     */   }
/*     */   
/*     */   public static BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos cauldronPos) {
/* 606 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> canDripThrough(level, pos, state);
/* 607 */     return (BlockPos)findBlockVertical(level, cauldronPos, Direction.UP.getAxisDirection(), pathPredicate, PointedDripstoneBlock::canDrip, 11).orElse(null);
/*     */   }
/*     */   
/*     */   public static Fluid getCauldronFillFluidType(ServerLevel level, BlockPos stalactitePos) {
/* 611 */     return (Fluid)getFluidAboveStalactite(level, stalactitePos, level.getBlockState(stalactitePos))
/* 612 */       .map(fluidSource -> fluidSource.fluid)
/* 613 */       .filter(PointedDripstoneBlock::canFillCauldron)
/* 614 */       .orElse(Fluids.EMPTY);
/*     */   }
/*     */   
/*     */   private static Optional<FluidInfo> getFluidAboveStalactite(Level level, BlockPos stalactitePos, BlockState stalactiteState) {
/* 618 */     if (!isStalactite(stalactiteState)) {
/* 619 */       return Optional.empty();
/*     */     }
/*     */     
/* 622 */     return findRootBlock(level, stalactitePos, stalactiteState, 11).map(rootPos -> {
/* 623 */           Fluid fluid; BlockPos abovePos = rootPos.above();
/* 624 */           BlockState aboveState = level.getBlockState(abovePos);
/*     */           
/* 626 */           if (aboveState.is(Blocks.MUD) && !((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, abovePos)).booleanValue()) {
/* 627 */             fluid = Fluids.WATER;
/*     */           } else {
/* 629 */             fluid = level.getFluidState(abovePos).getType();
/*     */           } 
/* 631 */           return new FluidInfo(abovePos, fluid, aboveState);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 636 */   private static boolean canFillCauldron(Fluid fluidAbove) { return (fluidAbove == Fluids.LAVA || fluidAbove == Fluids.WATER); }
/*     */ 
/*     */ 
/*     */   
/* 640 */   private static boolean canGrow(BlockState rootState, BlockState aboveState) { return (rootState.is(Blocks.DRIPSTONE_BLOCK) && aboveState.is(Blocks.WATER) && aboveState.getFluidState().isSource()); }
/*     */ 
/*     */   
/*     */   private static ParticleOptions getDripParticle(Level level, Fluid fluidAbove, BlockPos posAbove) {
/* 644 */     if (fluidAbove.isSame(Fluids.EMPTY)) {
/* 645 */       return (ParticleOptions)level.environmentAttributes().getValue(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, posAbove);
/*     */     }
/* 647 */     return fluidAbove.is(FluidTags.LAVA) ? ParticleTypes.DRIPPING_DRIPSTONE_LAVA : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findBlockVertical(LevelAccessor level, BlockPos pos, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> pathPredicate, Predicate<BlockState> targetPredicate, int maxSteps) {
/* 651 */     Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
/* 652 */     BlockPos.MutableBlockPos mutablePos = pos.mutable();
/*     */     
/* 654 */     for (int i = 1; i < maxSteps; i++) {
/* 655 */       mutablePos.move(direction);
/* 656 */       BlockState state = level.getBlockState(mutablePos);
/* 657 */       if (targetPredicate.test(state)) {
/* 658 */         return Optional.of(mutablePos.immutable());
/*     */       }
/* 660 */       if (level.isOutsideBuildHeight(mutablePos.getY()) || !pathPredicate.test(mutablePos, state)) {
/* 661 */         return Optional.empty();
/*     */       }
/*     */     } 
/* 664 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
/* 673 */     if (state.isAir()) {
/* 674 */       return true;
/*     */     }
/* 676 */     if (state.isSolidRender()) {
/* 677 */       return false;
/*     */     }
/* 679 */     if (!state.getFluidState().isEmpty()) {
/* 680 */       return false;
/*     */     }
/* 682 */     VoxelShape collisionShape = state.getCollisionShape(level, pos);
/* 683 */     return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, collisionShape, BooleanOp.AND);
/*     */   }
/*     */   static final class FluidInfo extends Record { private final BlockPos pos; private final Fluid fluid; private final BlockState sourceState;
/* 686 */     FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) { this.pos = pos; this.fluid = fluid; this.sourceState = sourceState; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #686	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #686	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #686	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/* 686 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public Fluid fluid() { return this.fluid; } public BlockState sourceState() { return this.sourceState; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PointedDripstoneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */