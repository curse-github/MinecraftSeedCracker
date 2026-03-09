/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WallSide;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallBlock extends Block implements SimpleWaterloggedBlock {
/*  33 */   public static final MapCodec<WallBlock> CODEC = simpleCodec(WallBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<WallBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/*  41 */   public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
/*  42 */   public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
/*  43 */   public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
/*  44 */   public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
/*  45 */   public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> collisionShapes;
/*  56 */   private static final VoxelShape TEST_SHAPE_POST = Block.column(2.0D, 0.0D, 16.0D);
/*  57 */   private static final Map<Direction, VoxelShape> TEST_SHAPES_WALL = Shapes.rotateHorizontal(Block.boxZ(2.0D, 16.0D, 0.0D, 9.0D));
/*     */   
/*     */   public WallBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(UP, Boolean.valueOf(true))).setValue(NORTH, WallSide.NONE)).setValue(EAST, WallSide.NONE)).setValue(SOUTH, WallSide.NONE)).setValue(WEST, WallSide.NONE)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  63 */     this.shapes = makeShapes(16.0F, 14.0F);
/*  64 */     this.collisionShapes = makeShapes(24.0F, 24.0F);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes(float postHeight, float wallTop) {
/*  68 */     VoxelShape post = Block.column(8.0D, 0.0D, postHeight);
/*  69 */     int width = 6;
/*  70 */     Map<Direction, VoxelShape> low = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, wallTop, 0.0D, 11.0D));
/*  71 */     Map<Direction, VoxelShape> tall = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, postHeight, 0.0D, 11.0D));
/*     */     
/*  73 */     return getShapeForEachState(state -> {
/*  74 */           VoxelShape shape = ((Boolean)state.getValue(UP)).booleanValue() ? post : Shapes.empty();
/*     */           
/*  76 */           for (Map.Entry<Direction, EnumProperty<WallSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*  77 */             switch ((WallSide)state.getValue((Property)entry.getValue())) { default: throw new MatchException(null, null);case NONE: case LOW: case TALL: break; }  shape = Shapes.or(shape, 
/*     */ 
/*     */                 
/*  80 */                 (VoxelShape)tall.get(entry.getKey()));
/*     */           } 
/*     */           
/*  83 */           return shape;
/*     */         }new Property[] { WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.collisionShapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */   
/*     */   private boolean connectsTo(BlockState state, boolean faceSolid, Direction direction) {
/* 103 */     Block block = state.getBlock();
/*     */     
/* 105 */     boolean connectedFenceGate = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction));
/* 106 */     return (state.is(BlockTags.WALLS) || (!isExceptionForConnection(state) && faceSolid) || block instanceof IronBarsBlock || connectedFenceGate);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 111 */     Level level1 = context.getLevel();
/* 112 */     BlockPos pos = context.getClickedPos();
/* 113 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 115 */     BlockPos northPos = pos.north();
/* 116 */     BlockPos eastPos = pos.east();
/* 117 */     BlockPos southPos = pos.south();
/* 118 */     BlockPos westPos = pos.west();
/* 119 */     BlockPos topPos = pos.above();
/*     */     
/* 121 */     BlockState northState = level1.getBlockState(northPos);
/* 122 */     BlockState eastState = level1.getBlockState(eastPos);
/* 123 */     BlockState southState = level1.getBlockState(southPos);
/* 124 */     BlockState westState = level1.getBlockState(westPos);
/* 125 */     BlockState topState = level1.getBlockState(topPos);
/*     */     
/* 127 */     boolean north = connectsTo(northState, northState.isFaceSturdy(level1, northPos, Direction.SOUTH), Direction.SOUTH);
/* 128 */     boolean east = connectsTo(eastState, eastState.isFaceSturdy(level1, eastPos, Direction.WEST), Direction.WEST);
/* 129 */     boolean south = connectsTo(southState, southState.isFaceSturdy(level1, southPos, Direction.NORTH), Direction.NORTH);
/* 130 */     boolean west = connectsTo(westState, westState.isFaceSturdy(level1, westPos, Direction.EAST), Direction.EAST);
/*     */     
/* 132 */     BlockState state = (BlockState)defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/* 133 */     return updateShape(level1, state, topPos, topState, north, east, south, west);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 138 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 139 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 142 */     if (directionToNeighbour == Direction.DOWN) {
/* 143 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     }
/*     */     
/* 146 */     if (directionToNeighbour == Direction.UP) {
/* 147 */       return topUpdate(level, state, neighbourPos, neighbourState);
/*     */     }
/*     */     
/* 150 */     return sideUpdate(level, pos, state, neighbourPos, neighbourState, directionToNeighbour);
/*     */   }
/*     */ 
/*     */   
/* 154 */   private static boolean isConnected(BlockState state, Property<WallSide> northWall) { return (state.getValue(northWall) != WallSide.NONE); }
/*     */ 
/*     */ 
/*     */   
/* 158 */   private static boolean isCovered(VoxelShape aboveShape, VoxelShape testShape) { return !Shapes.joinIsNotEmpty(testShape, aboveShape, BooleanOp.ONLY_FIRST); }
/*     */ 
/*     */   
/*     */   private BlockState topUpdate(LevelReader level, BlockState state, BlockPos topPos, BlockState topNeighbour) {
/* 162 */     boolean north = isConnected(state, NORTH);
/* 163 */     boolean east = isConnected(state, EAST);
/* 164 */     boolean south = isConnected(state, SOUTH);
/* 165 */     boolean west = isConnected(state, WEST);
/*     */     
/* 167 */     return updateShape(level, state, topPos, topNeighbour, north, east, south, west);
/*     */   }
/*     */   
/*     */   private BlockState sideUpdate(LevelReader level, BlockPos pos, BlockState state, BlockPos neighbourPos, BlockState neighbour, Direction direction) {
/* 171 */     Direction opposite = direction.getOpposite();
/* 172 */     boolean isNorthConnected = (direction == Direction.NORTH) ? connectsTo(neighbour, neighbour.isFaceSturdy(level, neighbourPos, opposite), opposite) : isConnected(state, NORTH);
/* 173 */     boolean isEastConnected = (direction == Direction.EAST) ? connectsTo(neighbour, neighbour.isFaceSturdy(level, neighbourPos, opposite), opposite) : isConnected(state, EAST);
/* 174 */     boolean isSouthConnected = (direction == Direction.SOUTH) ? connectsTo(neighbour, neighbour.isFaceSturdy(level, neighbourPos, opposite), opposite) : isConnected(state, SOUTH);
/* 175 */     boolean isWestConnected = (direction == Direction.WEST) ? connectsTo(neighbour, neighbour.isFaceSturdy(level, neighbourPos, opposite), opposite) : isConnected(state, WEST);
/*     */     
/* 177 */     BlockPos above = pos.above();
/* 178 */     BlockState aboveState = level.getBlockState(above);
/* 179 */     return updateShape(level, state, above, aboveState, isNorthConnected, isEastConnected, isSouthConnected, isWestConnected);
/*     */   }
/*     */   
/*     */   private BlockState updateShape(LevelReader level, BlockState state, BlockPos topPos, BlockState topNeighbour, boolean north, boolean east, boolean south, boolean west) {
/* 183 */     VoxelShape aboveShape = topNeighbour.getCollisionShape(level, topPos).getFaceShape(Direction.DOWN);
/* 184 */     BlockState sidesUpdatedState = updateSides(state, north, east, south, west, aboveShape);
/*     */     
/* 186 */     return (BlockState)sidesUpdatedState.setValue(UP, Boolean.valueOf(shouldRaisePost(sidesUpdatedState, topNeighbour, aboveShape)));
/*     */   }
/*     */   
/*     */   private boolean shouldRaisePost(BlockState state, BlockState topNeighbour, VoxelShape aboveShape) {
/* 190 */     boolean topNeighbourHasPost = (topNeighbour.getBlock() instanceof WallBlock && ((Boolean)topNeighbour.getValue(UP)).booleanValue());
/* 191 */     if (topNeighbourHasPost) {
/* 192 */       return true;
/*     */     }
/*     */     
/* 195 */     WallSide northWall = (WallSide)state.getValue(NORTH);
/* 196 */     WallSide southWall = (WallSide)state.getValue(SOUTH);
/* 197 */     WallSide eastWall = (WallSide)state.getValue(EAST);
/* 198 */     WallSide westWall = (WallSide)state.getValue(WEST);
/*     */     
/* 200 */     boolean southNone = (southWall == WallSide.NONE);
/* 201 */     boolean westNone = (westWall == WallSide.NONE);
/* 202 */     boolean eastNone = (eastWall == WallSide.NONE);
/* 203 */     boolean northNone = (northWall == WallSide.NONE);
/*     */     
/* 205 */     boolean hasCorner = ((northNone && southNone && westNone && eastNone) || northNone != southNone || westNone != eastNone);
/*     */ 
/*     */     
/* 208 */     if (hasCorner) {
/* 209 */       return true;
/*     */     }
/*     */     
/* 212 */     boolean hasHighWall = ((northWall == WallSide.TALL && southWall == WallSide.TALL) || (eastWall == WallSide.TALL && westWall == WallSide.TALL));
/*     */     
/* 214 */     if (hasHighWall) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     return (topNeighbour.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(aboveShape, TEST_SHAPE_POST));
/*     */   }
/*     */   
/*     */   private BlockState updateSides(BlockState state, boolean northConnection, boolean eastConnection, boolean southConnection, boolean westConnection, VoxelShape aboveShape) {
/* 222 */     return (BlockState)((BlockState)((BlockState)((BlockState)state
/* 223 */       .setValue(NORTH, makeWallState(northConnection, aboveShape, (VoxelShape)TEST_SHAPES_WALL.get(Direction.NORTH))))
/* 224 */       .setValue(EAST, makeWallState(eastConnection, aboveShape, (VoxelShape)TEST_SHAPES_WALL.get(Direction.EAST))))
/* 225 */       .setValue(SOUTH, makeWallState(southConnection, aboveShape, (VoxelShape)TEST_SHAPES_WALL.get(Direction.SOUTH))))
/* 226 */       .setValue(WEST, makeWallState(westConnection, aboveShape, (VoxelShape)TEST_SHAPES_WALL.get(Direction.WEST)));
/*     */   }
/*     */   
/*     */   private WallSide makeWallState(boolean connectsToSide, VoxelShape aboveShape, VoxelShape testShape) {
/* 230 */     if (connectsToSide) {
/* 231 */       if (isCovered(aboveShape, testShape)) {
/* 232 */         return WallSide.TALL;
/*     */       }
/* 234 */       return WallSide.LOW;
/*     */     } 
/*     */     
/* 237 */     return WallSide.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 243 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 244 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 246 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 251 */   protected boolean propagatesSkylightDown(BlockState state) { return !((Boolean)state.getValue(WATERLOGGED)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { UP, NORTH, EAST, WEST, SOUTH, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 261 */     switch (rotation) {
/*     */       case NONE:
/* 263 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(SOUTH))).setValue(EAST, (WallSide)state.getValue(WEST))).setValue(SOUTH, (WallSide)state.getValue(NORTH))).setValue(WEST, (WallSide)state.getValue(EAST));
/*     */       case LOW:
/* 265 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(EAST))).setValue(EAST, (WallSide)state.getValue(SOUTH))).setValue(SOUTH, (WallSide)state.getValue(WEST))).setValue(WEST, (WallSide)state.getValue(NORTH));
/*     */       case TALL:
/* 267 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(WEST))).setValue(EAST, (WallSide)state.getValue(NORTH))).setValue(SOUTH, (WallSide)state.getValue(EAST))).setValue(WEST, (WallSide)state.getValue(SOUTH));
/*     */     } 
/* 269 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 275 */     switch (mirror) {
/*     */       case NONE:
/* 277 */         return (BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(SOUTH))).setValue(SOUTH, (WallSide)state.getValue(NORTH));
/*     */       case LOW:
/* 279 */         return (BlockState)((BlockState)state.setValue(EAST, (WallSide)state.getValue(WEST))).setValue(WEST, (WallSide)state.getValue(EAST));
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return super.mirror(state, mirror);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */