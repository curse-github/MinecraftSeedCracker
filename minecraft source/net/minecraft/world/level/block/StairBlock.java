/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StairBlock extends Block implements SimpleWaterloggedBlock {
/*  30 */   public static final MapCodec<StairBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockState.CODEC
/*  31 */         .fieldOf("base_state").forGetter(()), 
/*  32 */         propertiesCodec())
/*  33 */       .apply(i, StairBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<? extends StairBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  41 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  42 */   public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
/*  43 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_OUTER = Shapes.or(
/*  46 */       Block.column(16.0D, 0.0D, 8.0D), 
/*  47 */       Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D));
/*     */   
/*  49 */   private static final VoxelShape SHAPE_STRAIGHT = Shapes.or(SHAPE_OUTER, Shapes.rotate(SHAPE_OUTER, OctahedralGroup.BLOCK_ROT_Y_90));
/*  50 */   private static final VoxelShape SHAPE_INNER = Shapes.or(SHAPE_STRAIGHT, Shapes.rotate(SHAPE_STRAIGHT, OctahedralGroup.BLOCK_ROT_Y_90));
/*     */   
/*  52 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER);
/*  53 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT);
/*  54 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_INNER = Shapes.rotateHorizontal(SHAPE_INNER);
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER, OctahedralGroup.INVERT_Y);
/*  57 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT, OctahedralGroup.INVERT_Y);
/*  58 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_INNER = Shapes.rotateHorizontal(SHAPE_INNER, OctahedralGroup.INVERT_Y);
/*     */   
/*     */   private final Block base;
/*     */   protected final BlockState baseState;
/*     */   
/*     */   protected StairBlock(BlockState baseState, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(HALF, Half.BOTTOM)).setValue(SHAPE, StairsShape.STRAIGHT)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*  66 */     this.base = baseState.getBlock();
/*  67 */     this.baseState = baseState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  77 */     boolean isBottom = (state.getValue(HALF) == Half.BOTTOM);
/*  78 */     Direction facing = (Direction)state.getValue(FACING);
/*     */     
/*  80 */     switch ((StairsShape)state.getValue(SHAPE)) { default: throw new MatchException(null, null);
/*  81 */       case LEFT_RIGHT: if (isBottom);
/*  82 */       case null: case null: if (isBottom);
/*  83 */       case FRONT_BACK: case null: if (isBottom); break; }  switch ((StairsShape)state
/*  84 */       .getValue(SHAPE)) { default: throw new MatchException(null, null);case LEFT_RIGHT: case FRONT_BACK: case null: case null: case null: break; }  return (VoxelShape)SHAPE_TOP_OUTER.get(
/*     */ 
/*     */         
/*  87 */         facing.getClockWise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public float getExplosionResistance() { return this.base.getExplosionResistance(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  98 */     Direction clickedFace = context.getClickedFace();
/*  99 */     BlockPos pos = context.getClickedPos();
/* 100 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     BlockState state = (BlockState)((BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection())).setValue(HALF, (clickedFace == Direction.DOWN || (clickedFace != Direction.UP && (context.getClickLocation()).y - pos.getY() > 0.5D)) ? Half.TOP : Half.BOTTOM)).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */     
/* 107 */     return (BlockState)state.setValue(SHAPE, getStairsShape(state, context.getLevel(), pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 112 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 113 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 115 */     if (directionToNeighbour.getAxis().isHorizontal()) {
/* 116 */       return (BlockState)state.setValue(SHAPE, getStairsShape(state, level, pos));
/*     */     }
/* 118 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static StairsShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 122 */     Direction facing = (Direction)state.getValue(FACING);
/* 123 */     BlockState behindState = level.getBlockState(pos.relative(facing));
/* 124 */     if (isStairs(behindState) && state.getValue(HALF) == behindState.getValue(HALF)) {
/* 125 */       Direction behindFacing = (Direction)behindState.getValue(FACING);
/* 126 */       if (behindFacing.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && canTakeShape(state, level, pos, behindFacing.getOpposite())) {
/* 127 */         if (behindFacing == facing.getCounterClockWise()) {
/* 128 */           return StairsShape.OUTER_LEFT;
/*     */         }
/* 130 */         return StairsShape.OUTER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     BlockState frontState = level.getBlockState(pos.relative(facing.getOpposite()));
/* 135 */     if (isStairs(frontState) && state.getValue(HALF) == frontState.getValue(HALF)) {
/* 136 */       Direction frontFacing = (Direction)frontState.getValue(FACING);
/* 137 */       if (frontFacing.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && canTakeShape(state, level, pos, frontFacing)) {
/* 138 */         if (frontFacing == facing.getCounterClockWise()) {
/* 139 */           return StairsShape.INNER_LEFT;
/*     */         }
/* 141 */         return StairsShape.INNER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     return StairsShape.STRAIGHT;
/*     */   }
/*     */   
/*     */   private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction neighbour) {
/* 149 */     BlockState neighborState = level.getBlockState(pos.relative(neighbour));
/* 150 */     return (!isStairs(neighborState) || neighborState.getValue(FACING) != state.getValue(FACING) || neighborState.getValue(HALF) != state.getValue(HALF));
/*     */   }
/*     */ 
/*     */   
/* 154 */   public static boolean isStairs(BlockState state) { return state.getBlock() instanceof StairBlock; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 164 */     Direction direction = (Direction)state.getValue(FACING);
/* 165 */     StairsShape shape = (StairsShape)state.getValue(SHAPE);
/* 166 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 168 */         if (direction.getAxis() == Direction.Axis.Z) {
/* 169 */           switch (shape) {
/*     */             case null:
/* 171 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
/*     */             case null:
/* 173 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
/*     */             case FRONT_BACK:
/* 175 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 177 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
/*     */           } 
/* 179 */           return state.rotate(Rotation.CLOCKWISE_180);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 184 */         if (direction.getAxis() == Direction.Axis.X) {
/* 185 */           switch (shape) {
/*     */             case null:
/* 187 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
/*     */             case null:
/* 189 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
/*     */             case FRONT_BACK:
/* 191 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 193 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
/*     */             case LEFT_RIGHT:
/* 195 */               return state.rotate(Rotation.CLOCKWISE_180);
/*     */           } 
/*     */         
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 202 */     return super.mirror(state, mirror);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, HALF, SHAPE, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 212 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 213 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 215 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 220 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StairBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */