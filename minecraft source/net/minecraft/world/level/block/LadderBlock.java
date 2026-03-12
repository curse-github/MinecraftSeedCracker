/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LadderBlock extends Block implements SimpleWaterloggedBlock {
/*  26 */   public static final MapCodec<LadderBlock> CODEC = simpleCodec(LadderBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  30 */   public MapCodec<LadderBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  33 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  34 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  36 */   public static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   protected LadderBlock(BlockBehaviour.Properties properties) {
/*  39 */     super(properties);
/*  40 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */   
/*     */   private boolean canAttachTo(BlockGetter level, BlockPos pos, Direction direction) {
/*  49 */     BlockState blockState = level.getBlockState(pos);
/*  50 */     return blockState.isFaceSturdy(level, pos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  55 */     Direction direction = (Direction)state.getValue(FACING);
/*  56 */     return canAttachTo(level, pos.relative(direction.getOpposite()), direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  61 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  62 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  64 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  65 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  68 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  74 */     if (!context.replacingClickedOnBlock()) {
/*  75 */       BlockState state = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
/*  76 */       if (state.is(this) && state.getValue(FACING) == context.getClickedFace()) {
/*  77 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*  81 */     BlockState state = defaultBlockState();
/*     */     
/*  83 */     Level level1 = context.getLevel();
/*  84 */     BlockPos pos = context.getClickedPos();
/*  85 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/*  87 */     for (Direction direction : context.getNearestLookingDirections()) {
/*  88 */       if (direction.getAxis().isHorizontal()) {
/*  89 */         state = (BlockState)state.setValue(FACING, direction.getOpposite());
/*  90 */         if (state.canSurvive(level1, pos)) {
/*  91 */           return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 116 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 117 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 119 */     return super.getFluidState(state);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LadderBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */