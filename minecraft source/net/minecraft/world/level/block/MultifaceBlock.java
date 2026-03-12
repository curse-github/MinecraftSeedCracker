/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultifaceBlock
/*     */   extends Block
/*     */   implements SimpleWaterloggedBlock
/*     */ {
/*  39 */   public static final MapCodec<MultifaceBlock> CODEC = simpleCodec(MultifaceBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected MapCodec<? extends MultifaceBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  46 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  48 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  50 */   protected static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final boolean canRotate;
/*     */   private final boolean canMirrorX;
/*     */   private final boolean canMirrorZ;
/*     */   
/*     */   public MultifaceBlock(BlockBehaviour.Properties properties) {
/*  59 */     super(properties);
/*  60 */     registerDefaultState(getDefaultMultifaceState(this.stateDefinition));
/*  61 */     this.shapes = makeShapes();
/*     */     
/*  63 */     this.canRotate = Direction.Plane.HORIZONTAL.stream().allMatch(this::isFaceSupported);
/*  64 */     this.canMirrorX = (Direction.Plane.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::isFaceSupported).count() % 2L == 0L);
/*  65 */     this.canMirrorZ = (Direction.Plane.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::isFaceSupported).count() % 2L == 0L);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  69 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  71 */     return getShapeForEachState(state -> {
/*  72 */           VoxelShape shape = Shapes.empty();
/*  73 */           for (Direction direction : DIRECTIONS) {
/*  74 */             if (hasFace(state, direction)) {
/*  75 */               shape = Shapes.or(shape, (VoxelShape)shapes.get(direction));
/*     */             }
/*     */           } 
/*  78 */           return shape.isEmpty() ? Shapes.block() : shape;
/*     */         }new Property[] { WATERLOGGED });
/*     */   }
/*     */   
/*     */   public static Set<Direction> availableFaces(BlockState state) {
/*  83 */     if (!(state.getBlock() instanceof MultifaceBlock)) {
/*  84 */       return Set.of();
/*     */     }
/*  86 */     Set<Direction> faces = EnumSet.noneOf(Direction.class);
/*  87 */     for (Direction direction : Direction.values()) {
/*  88 */       if (hasFace(state, direction)) {
/*  89 */         faces.add(direction);
/*     */       }
/*     */     } 
/*  92 */     return faces;
/*     */   }
/*     */   
/*     */   public static Set<Direction> unpack(byte data) {
/*  96 */     Set<Direction> presentDirections = EnumSet.noneOf(Direction.class);
/*  97 */     for (Direction direction : Direction.values()) {
/*  98 */       if ((data & (byte)(1 << direction.ordinal())) > 0) {
/*  99 */         presentDirections.add(direction);
/*     */       }
/*     */     } 
/* 102 */     return presentDirections;
/*     */   }
/*     */   
/*     */   public static byte pack(Collection<Direction> directions) {
/* 106 */     byte code = 0;
/* 107 */     for (Direction direction : directions) {
/* 108 */       code = (byte)(code | 1 << direction.ordinal());
/*     */     }
/* 110 */     return code;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   protected boolean isFaceSupported(Direction faceDirection) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 120 */     for (Direction direction : DIRECTIONS) {
/* 121 */       if (isFaceSupported(direction)) {
/* 122 */         builder.add(new Property[] { getFaceProperty(direction) });
/*     */       }
/*     */     } 
/* 125 */     builder.add(new Property[] { WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 133 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 134 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 137 */     if (!hasAnyFace(state)) {
/* 138 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 141 */     if (!hasFace(state, directionToNeighbour) || canAttachTo(level, directionToNeighbour, neighbourPos, neighbourState)) {
/* 142 */       return state;
/*     */     }
/* 144 */     return removeFace(state, getFaceProperty(directionToNeighbour));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 149 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 150 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 152 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 157 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 162 */     boolean hasAtLeastOneFace = false;
/* 163 */     for (Direction directionToNeighbour : DIRECTIONS) {
/* 164 */       if (hasFace(state, directionToNeighbour)) {
/*     */ 
/*     */         
/* 167 */         if (!canAttachTo(level, pos, directionToNeighbour)) {
/* 168 */           return false;
/*     */         }
/* 170 */         hasAtLeastOneFace = true;
/*     */       } 
/* 172 */     }  return hasAtLeastOneFace;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 177 */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) { return (!context.getItemInHand().is(asItem()) || hasAnyVacantFace(state)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 182 */     Level level = context.getLevel();
/* 183 */     BlockPos placePos = context.getClickedPos();
/* 184 */     BlockState oldState = level.getBlockState(placePos);
/* 185 */     return (BlockState)Arrays.stream(context.getNearestLookingDirections())
/* 186 */       .map(direction -> getStateForPlacement(oldState, level, placePos, direction))
/* 187 */       .filter(Objects::nonNull)
/* 188 */       .findFirst()
/* 189 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public boolean isValidStateForPlacement(BlockGetter level, BlockState oldState, BlockPos placementPos, Direction placementDirection) {
/* 193 */     if (!isFaceSupported(placementDirection) || (oldState.is(this) && hasFace(oldState, placementDirection))) {
/* 194 */       return false;
/*     */     }
/* 196 */     BlockPos neighbourPos = placementPos.relative(placementDirection);
/* 197 */     return canAttachTo(level, placementDirection, neighbourPos, level.getBlockState(neighbourPos));
/*     */   }
/*     */   public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) {
/*     */     BlockState newState;
/* 201 */     if (!isValidStateForPlacement(level, oldState, placementPos, placementDirection)) {
/* 202 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 206 */     if (oldState.is(this)) {
/*     */       
/* 208 */       newState = oldState;
/* 209 */     } else if (oldState.getFluidState().isSourceOfType(Fluids.WATER)) {
/* 210 */       newState = (BlockState)defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */     } else {
/* 212 */       newState = defaultBlockState();
/*     */     } 
/*     */     
/* 215 */     return (BlockState)newState.setValue(getFaceProperty(placementDirection), Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 220 */     if (!this.canRotate) {
/* 221 */       return state;
/*     */     }
/*     */     
/* 224 */     Objects.requireNonNull(rotation); return mapDirections(state, rotation::rotate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 229 */     if (mirror == Mirror.FRONT_BACK && !this.canMirrorX) {
/* 230 */       return state;
/*     */     }
/* 232 */     if (mirror == Mirror.LEFT_RIGHT && !this.canMirrorZ) {
/* 233 */       return state;
/*     */     }
/*     */     
/* 236 */     Objects.requireNonNull(mirror); return mapDirections(state, mirror::mirror);
/*     */   }
/*     */   
/*     */   private BlockState mapDirections(BlockState state, Function<Direction, Direction> mapping) {
/* 240 */     BlockState newState = state;
/* 241 */     for (Direction direction : DIRECTIONS) {
/* 242 */       if (isFaceSupported(direction)) {
/* 243 */         newState = (BlockState)newState.setValue(getFaceProperty((Direction)mapping.apply(direction)), (Boolean)state.getValue(getFaceProperty(direction)));
/*     */       }
/*     */     } 
/* 246 */     return newState;
/*     */   }
/*     */   
/*     */   public static boolean hasFace(BlockState state, Direction faceDirection) {
/* 250 */     BooleanProperty property = getFaceProperty(faceDirection);
/* 251 */     return ((Boolean)state.getValueOrElse(property, Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, BlockPos pos, Direction directionTowardsNeighbour) {
/* 255 */     BlockPos neighbourPos = pos.relative(directionTowardsNeighbour);
/* 256 */     BlockState blockState = level.getBlockState(neighbourPos);
/* 257 */     return canAttachTo(level, directionTowardsNeighbour, neighbourPos, blockState);
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, Direction directionTowardsNeighbour, BlockPos neighbourPos, BlockState neighbourState) {
/* 261 */     return (Block.isFaceFull(neighbourState.getBlockSupportShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()) || 
/* 262 */       Block.isFaceFull(neighbourState.getCollisionShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()));
/*     */   }
/*     */   
/*     */   private static BlockState removeFace(BlockState state, BooleanProperty property) {
/* 266 */     BlockState newState = (BlockState)state.setValue(property, Boolean.valueOf(false));
/* 267 */     if (hasAnyFace(newState)) {
/* 268 */       return newState;
/*     */     }
/*     */     
/* 271 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/* 275 */   public static BooleanProperty getFaceProperty(Direction faceDirection) { return (BooleanProperty)PROPERTY_BY_DIRECTION.get(faceDirection); }
/*     */ 
/*     */   
/*     */   private static BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> stateDefinition) {
/* 279 */     BlockState state = (BlockState)((BlockState)stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(false));
/* 280 */     for (BooleanProperty faceProperty : PROPERTY_BY_DIRECTION.values()) {
/* 281 */       state = (BlockState)state.trySetValue(faceProperty, Boolean.valueOf(false));
/*     */     }
/* 283 */     return state;
/*     */   }
/*     */   
/*     */   protected static boolean hasAnyFace(BlockState state) {
/* 287 */     for (Direction direction : DIRECTIONS) {
/* 288 */       if (hasFace(state, direction)) {
/* 289 */         return true;
/*     */       }
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean hasAnyVacantFace(BlockState state) {
/* 296 */     for (Direction direction : DIRECTIONS) {
/* 297 */       if (!hasFace(state, direction)) {
/* 298 */         return true;
/*     */       }
/*     */     } 
/* 301 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */