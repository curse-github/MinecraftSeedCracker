/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlags;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.redstone.RedstoneWireEvaluator;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedStoneWireBlock
/*     */   extends Block {
/*  45 */   public static final MapCodec<RedStoneWireBlock> CODEC = simpleCodec(RedStoneWireBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  49 */   public MapCodec<RedStoneWireBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  52 */   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
/*  53 */   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
/*  54 */   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
/*  55 */   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
/*  56 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  58 */   public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final int[] COLORS = (int[])Util.make(new int[16], list -> {
/*  66 */         for (int i = 0; i <= 15; i++) {
/*  67 */           float power = i / 15.0F;
/*  68 */           float red = power * 0.6F + ((power > 0.0F) ? 0.4F : 0.3F);
/*  69 */           float green = Mth.clamp(power * power * 0.7F - 0.5F, 0.0F, 1.0F);
/*  70 */           float blue = Mth.clamp(power * power * 0.6F - 0.7F, 0.0F, 1.0F);
/*  71 */           list[i] = ARGB.colorFromFloat(1.0F, red, green, blue);
/*     */         } 
/*     */       });
/*     */   
/*     */   private static final float PARTICLE_DENSITY = 0.2F;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final BlockState crossState;
/*  80 */   private final RedstoneWireEvaluator evaluator = new DefaultRedstoneWireEvaluator(this);
/*     */   private boolean shouldSignal = true;
/*     */   
/*     */   public RedStoneWireBlock(BlockBehaviour.Properties properties) {
/*  84 */     super(properties);
/*  85 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(NORTH, RedstoneSide.NONE)).setValue(EAST, RedstoneSide.NONE)).setValue(SOUTH, RedstoneSide.NONE)).setValue(WEST, RedstoneSide.NONE)).setValue(POWER, Integer.valueOf(0)));
/*     */     
/*  87 */     this.shapes = makeShapes();
/*     */     
/*  89 */     this.crossState = (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue(NORTH, RedstoneSide.SIDE)).setValue(EAST, RedstoneSide.SIDE)).setValue(SOUTH, RedstoneSide.SIDE)).setValue(WEST, RedstoneSide.SIDE);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  93 */     int height = 1;
/*  94 */     int width = 10;
/*     */     
/*  96 */     VoxelShape dot = Block.column(10.0D, 0.0D, 1.0D);
/*  97 */     Map<Direction, VoxelShape> floor = Shapes.rotateHorizontal(Block.boxZ(10.0D, 0.0D, 1.0D, 0.0D, 8.0D));
/*  98 */     Map<Direction, VoxelShape> up = Shapes.rotateHorizontal(Block.boxZ(10.0D, 16.0D, 0.0D, 1.0D));
/*     */     
/* 100 */     return getShapeForEachState(state -> {
/* 101 */           VoxelShape shape = dot;
/*     */           
/* 103 */           for (Map.Entry<Direction, EnumProperty<RedstoneSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/* 104 */             switch ((RedstoneSide)state.getValue((Property)entry.getValue())) { default: throw new MatchException(null, null);
/*     */               case LEFT_RIGHT: 
/*     */               case FRONT_BACK: 
/* 107 */               case null: break; }  shape = shape;
/*     */           } 
/*     */           
/* 110 */           return shape;
/*     */         }new Property[] { POWER });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return getConnectionState(context.getLevel(), this.crossState, context.getClickedPos()); }
/*     */ 
/*     */   
/*     */   private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
/* 125 */     boolean wasDot = isDot(state);
/* 126 */     state = getMissingConnections(level, (BlockState)defaultBlockState().setValue(POWER, (Integer)state.getValue(POWER)), pos);
/*     */ 
/*     */     
/* 129 */     if (wasDot && isDot(state)) {
/* 130 */       return state;
/*     */     }
/*     */     
/* 133 */     boolean north = ((RedstoneSide)state.getValue(NORTH)).isConnected();
/* 134 */     boolean south = ((RedstoneSide)state.getValue(SOUTH)).isConnected();
/* 135 */     boolean east = ((RedstoneSide)state.getValue(EAST)).isConnected();
/* 136 */     boolean west = ((RedstoneSide)state.getValue(WEST)).isConnected();
/* 137 */     boolean northSouthEmpty = (!north && !south);
/* 138 */     boolean eastWestEmpty = (!east && !west);
/*     */     
/* 140 */     if (!west && northSouthEmpty) {
/* 141 */       state = (BlockState)state.setValue(WEST, RedstoneSide.SIDE);
/*     */     }
/* 143 */     if (!east && northSouthEmpty) {
/* 144 */       state = (BlockState)state.setValue(EAST, RedstoneSide.SIDE);
/*     */     }
/* 146 */     if (!north && eastWestEmpty) {
/* 147 */       state = (BlockState)state.setValue(NORTH, RedstoneSide.SIDE);
/*     */     }
/* 149 */     if (!south && eastWestEmpty) {
/* 150 */       state = (BlockState)state.setValue(SOUTH, RedstoneSide.SIDE);
/*     */     }
/* 152 */     return state;
/*     */   }
/*     */   
/*     */   private BlockState getMissingConnections(BlockGetter level, BlockState state, BlockPos pos) {
/* 156 */     boolean canConnectUp = !level.getBlockState(pos.above()).isRedstoneConductor(level, pos);
/* 157 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 158 */       if (!((RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected()) {
/* 159 */         RedstoneSide sideConnection = getConnectingSide(level, pos, direction, canConnectUp);
/* 160 */         state = (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), sideConnection);
/*     */       } 
/*     */     } 
/* 163 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 168 */     if (directionToNeighbour == Direction.DOWN) {
/* 169 */       if (!canSurviveOn(level, neighbourPos, neighbourState)) {
/* 170 */         return Blocks.AIR.defaultBlockState();
/*     */       }
/* 172 */       return state;
/*     */     } 
/* 174 */     if (directionToNeighbour == Direction.UP) {
/* 175 */       return getConnectionState(level, state, pos);
/*     */     }
/*     */     
/* 178 */     RedstoneSide sideConnection = getConnectingSide(level, pos, directionToNeighbour);
/* 179 */     if (sideConnection.isConnected() == ((RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour))).isConnected() && !isCross(state)) {
/* 180 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), sideConnection);
/*     */     }
/* 182 */     return getConnectionState(level, (BlockState)((BlockState)this.crossState.setValue(POWER, (Integer)state.getValue(POWER))).setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), sideConnection), pos);
/*     */   }
/*     */ 
/*     */   
/* 186 */   private static boolean isCross(BlockState state) { return (((RedstoneSide)state.getValue(NORTH)).isConnected() && ((RedstoneSide)state.getValue(SOUTH)).isConnected() && ((RedstoneSide)state.getValue(EAST)).isConnected() && ((RedstoneSide)state.getValue(WEST)).isConnected()); }
/*     */ 
/*     */ 
/*     */   
/* 190 */   private static boolean isDot(BlockState state) { return (!((RedstoneSide)state.getValue(NORTH)).isConnected() && !((RedstoneSide)state.getValue(SOUTH)).isConnected() && !((RedstoneSide)state.getValue(EAST)).isConnected() && !((RedstoneSide)state.getValue(WEST)).isConnected()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, @UpdateFlags int updateFlags, int updateLimit) {
/* 195 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 196 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 197 */       RedstoneSide value = (RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(direction));
/* 198 */       if (value != RedstoneSide.NONE && !level.getBlockState(blockPos.setWithOffset(pos, direction)).is(this)) {
/* 199 */         blockPos.move(Direction.DOWN);
/* 200 */         BlockState blockStateDown = level.getBlockState(blockPos);
/* 201 */         if (blockStateDown.is(this)) {
/* 202 */           BlockPos neighborPos = blockPos.relative(direction.getOpposite());
/* 203 */           level.neighborShapeChanged(direction.getOpposite(), blockPos, neighborPos, level.getBlockState(neighborPos), updateFlags, updateLimit);
/*     */         } 
/*     */         
/* 206 */         blockPos.setWithOffset(pos, direction).move(Direction.UP);
/* 207 */         BlockState blockStateUp = level.getBlockState(blockPos);
/* 208 */         if (blockStateUp.is(this)) {
/* 209 */           BlockPos neighborPos = blockPos.relative(direction.getOpposite());
/* 210 */           level.neighborShapeChanged(direction.getOpposite(), blockPos, neighborPos, level.getBlockState(neighborPos), updateFlags, updateLimit);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 217 */   private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction direction) { return getConnectingSide(level, pos, direction, !level.getBlockState(pos.above()).isRedstoneConductor(level, pos)); }
/*     */ 
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction direction, boolean canConnectUp) {
/* 221 */     BlockPos relativePos = pos.relative(direction);
/* 222 */     BlockState relativeState = level.getBlockState(relativePos);
/* 223 */     if (canConnectUp) {
/*     */       
/* 225 */       boolean isPlaceableAbove = (relativeState.getBlock() instanceof TrapDoorBlock || canSurviveOn(level, relativePos, relativeState));
/* 226 */       if (isPlaceableAbove && shouldConnectTo(level.getBlockState(relativePos.above()))) {
/*     */ 
/*     */         
/* 229 */         if (relativeState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
/* 230 */           return RedstoneSide.UP;
/*     */         }
/* 232 */         return RedstoneSide.SIDE;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 237 */     if (shouldConnectTo(relativeState, direction) || (!relativeState.isRedstoneConductor(level, relativePos) && shouldConnectTo(level.getBlockState(relativePos.below())))) {
/* 238 */       return RedstoneSide.SIDE;
/*     */     }
/* 240 */     return RedstoneSide.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 245 */     BlockPos below = pos.below();
/* 246 */     BlockState belowState = level.getBlockState(below);
/* 247 */     return canSurviveOn(level, below, belowState);
/*     */   }
/*     */ 
/*     */   
/* 251 */   private boolean canSurviveOn(BlockGetter level, BlockPos relativePos, BlockState relativeState) { return (relativeState.isFaceSturdy(level, relativePos, Direction.UP) || relativeState.is(Blocks.HOPPER)); }
/*     */ 
/*     */   
/*     */   private void updatePowerStrength(Level level, BlockPos pos, BlockState state, Orientation orientation, boolean shapeUpdateWiresAroundInitialPosition) {
/* 255 */     if (useExperimentalEvaluator(level)) {
/* 256 */       (new ExperimentalRedstoneWireEvaluator(this)).updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
/*     */     } else {
/* 258 */       this.evaluator.updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockSignal(Level level, BlockPos pos) {
/* 266 */     this.shouldSignal = false;
/* 267 */     int blockSignal = level.getBestNeighborSignal(pos);
/* 268 */     this.shouldSignal = true;
/* 269 */     return blockSignal;
/*     */   }
/*     */   
/*     */   private void checkCornerChangeAt(Level level, BlockPos pos) {
/* 273 */     if (!level.getBlockState(pos).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/* 277 */     level.updateNeighborsAt(pos, this);
/* 278 */     for (Direction direction : Direction.values()) {
/* 279 */       level.updateNeighborsAt(pos.relative(direction), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 285 */     if (oldState.is(state.getBlock()) || level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 289 */     updatePowerStrength(level, pos, state, null, true);
/*     */     
/* 291 */     for (Direction direction : Direction.Plane.VERTICAL) {
/* 292 */       level.updateNeighborsAt(pos.relative(direction), this);
/*     */     }
/*     */     
/* 295 */     updateNeighborsOfNeighboringWires(level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 300 */     if (movedByPiston) {
/*     */       return;
/*     */     }
/* 303 */     for (Direction direction : Direction.values()) {
/* 304 */       level.updateNeighborsAt(pos.relative(direction), this);
/*     */     }
/* 306 */     updatePowerStrength(level, pos, state, null, false);
/*     */     
/* 308 */     updateNeighborsOfNeighboringWires(level, pos);
/*     */   }
/*     */   
/*     */   private void updateNeighborsOfNeighboringWires(Level level, BlockPos pos) {
/* 312 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 313 */       checkCornerChangeAt(level, pos.relative(direction));
/*     */     }
/*     */     
/* 316 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 317 */       BlockPos target = pos.relative(direction);
/*     */       
/* 319 */       if (level.getBlockState(target).isRedstoneConductor(level, target)) {
/* 320 */         checkCornerChangeAt(level, target.above()); continue;
/*     */       } 
/* 322 */       checkCornerChangeAt(level, target.below());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 329 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 334 */     if (block == this && useExperimentalEvaluator(level)) {
/*     */       return;
/*     */     }
/*     */     
/* 338 */     if (state.canSurvive(level, pos)) {
/* 339 */       updatePowerStrength(level, pos, state, orientation, false);
/*     */     } else {
/* 341 */       dropResources(state, level, pos);
/* 342 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 347 */   private static boolean useExperimentalEvaluator(Level level) { return level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 352 */     if (!this.shouldSignal) {
/* 353 */       return 0;
/*     */     }
/* 355 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 360 */     if (!this.shouldSignal || direction == Direction.DOWN) {
/* 361 */       return 0;
/*     */     }
/* 363 */     int power = ((Integer)state.getValue(POWER)).intValue();
/* 364 */     if (power == 0) {
/* 365 */       return 0;
/*     */     }
/*     */     
/* 368 */     if (direction == Direction.UP || ((RedstoneSide)getConnectionState(level, state, pos).getValue((Property)PROPERTY_BY_DIRECTION.get(direction.getOpposite()))).isConnected()) {
/* 369 */       return power;
/*     */     }
/* 371 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 376 */   protected static boolean shouldConnectTo(BlockState blockState) { return shouldConnectTo(blockState, null); }
/*     */ 
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState blockState, Direction direction) {
/* 380 */     if (blockState.is(Blocks.REDSTONE_WIRE)) {
/* 381 */       return true;
/*     */     }
/*     */     
/* 384 */     if (blockState.is(Blocks.REPEATER)) {
/* 385 */       Direction repeaterDirection = (Direction)blockState.getValue(RepeaterBlock.FACING);
/* 386 */       return (repeaterDirection == direction || repeaterDirection.getOpposite() == direction);
/*     */     } 
/*     */     
/* 389 */     if (blockState.is(Blocks.OBSERVER)) {
/* 390 */       return (direction == blockState.getValue(ObserverBlock.FACING));
/*     */     }
/*     */     
/* 393 */     return (blockState.isSignalSource() && direction != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 398 */   protected boolean isSignalSource(BlockState state) { return this.shouldSignal; }
/*     */ 
/*     */ 
/*     */   
/* 402 */   public static int getColorForPower(int power) { return COLORS[power]; }
/*     */ 
/*     */   
/*     */   private static void spawnParticlesAlongLine(Level level, RandomSource random, BlockPos pos, int color, Direction side, Direction along, float from, float to) {
/* 406 */     float span = to - from;
/* 407 */     if (random.nextFloat() >= 0.2F * span) {
/*     */       return;
/*     */     }
/* 410 */     float sideOfBlock = 0.4375F;
/* 411 */     float positionOnLine = from + span * random.nextFloat();
/* 412 */     double x = 0.5D + (0.4375F * side.getStepX()) + (positionOnLine * along.getStepX());
/* 413 */     double y = 0.5D + (0.4375F * side.getStepY()) + (positionOnLine * along.getStepY());
/* 414 */     double z = 0.5D + (0.4375F * side.getStepZ()) + (positionOnLine * along.getStepZ());
/* 415 */     level.addParticle(new DustParticleOptions(color, 1.0F), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 420 */     int power = ((Integer)state.getValue(POWER)).intValue();
/* 421 */     if (power == 0) {
/*     */       return;
/*     */     }
/* 424 */     for (Direction horizontal : Direction.Plane.HORIZONTAL) {
/* 425 */       RedstoneSide connection = (RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(horizontal));
/* 426 */       switch (connection) {
/*     */         case LEFT_RIGHT:
/* 428 */           spawnParticlesAlongLine(level, random, pos, COLORS[power], horizontal, Direction.UP, -0.5F, 0.5F);
/*     */         
/*     */         case FRONT_BACK:
/* 431 */           spawnParticlesAlongLine(level, random, pos, COLORS[power], Direction.DOWN, horizontal, 0.0F, 0.5F);
/*     */           continue;
/*     */       } 
/*     */       
/* 435 */       spawnParticlesAlongLine(level, random, pos, COLORS[power], Direction.DOWN, horizontal, 0.0F, 0.3F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 442 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/* 444 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.getValue(SOUTH))).setValue(EAST, (RedstoneSide)state.getValue(WEST))).setValue(SOUTH, (RedstoneSide)state.getValue(NORTH))).setValue(WEST, (RedstoneSide)state.getValue(EAST));
/*     */       case FRONT_BACK:
/* 446 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.getValue(EAST))).setValue(EAST, (RedstoneSide)state.getValue(SOUTH))).setValue(SOUTH, (RedstoneSide)state.getValue(WEST))).setValue(WEST, (RedstoneSide)state.getValue(NORTH));
/*     */       case null:
/* 448 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.getValue(WEST))).setValue(EAST, (RedstoneSide)state.getValue(NORTH))).setValue(SOUTH, (RedstoneSide)state.getValue(EAST))).setValue(WEST, (RedstoneSide)state.getValue(SOUTH));
/*     */     } 
/* 450 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 456 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 458 */         return (BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.getValue(SOUTH))).setValue(SOUTH, (RedstoneSide)state.getValue(NORTH));
/*     */       case FRONT_BACK:
/* 460 */         return (BlockState)((BlockState)state.setValue(EAST, (RedstoneSide)state.getValue(WEST))).setValue(WEST, (RedstoneSide)state.getValue(EAST));
/*     */     } 
/*     */ 
/*     */     
/* 464 */     return super.mirror(state, mirror);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 469 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { NORTH, EAST, SOUTH, WEST, POWER }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 474 */     if (!(player.getAbilities()).mayBuild) {
/* 475 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 478 */     if (isCross(state) || isDot(state)) {
/* 479 */       BlockState newState = isCross(state) ? defaultBlockState() : this.crossState;
/* 480 */       newState = (BlockState)newState.setValue(POWER, (Integer)state.getValue(POWER));
/* 481 */       newState = getConnectionState(level, newState, pos);
/* 482 */       if (newState != state) {
/* 483 */         level.setBlock(pos, newState, 3);
/*     */         
/* 485 */         updatesOnShapeChange(level, pos, state, newState);
/* 486 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 489 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private void updatesOnShapeChange(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
/* 493 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, null, Direction.UP);
/* 494 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 495 */       BlockPos relativePos = pos.relative(direction);
/* 496 */       if (((RedstoneSide)oldState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() != ((RedstoneSide)newState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() && level.getBlockState(relativePos).isRedstoneConductor(level, relativePos))
/* 497 */         level.updateNeighborsAtExceptFromFacing(relativePos, newState.getBlock(), direction.getOpposite(), ExperimentalRedstoneUtils.withFront(orientation, direction)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RedStoneWireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */