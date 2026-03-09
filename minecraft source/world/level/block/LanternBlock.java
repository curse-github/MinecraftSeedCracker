/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LanternBlock extends Block implements SimpleWaterloggedBlock {
/*  24 */   public static final MapCodec<LanternBlock> CODEC = simpleCodec(LanternBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  28 */   public MapCodec<? extends LanternBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  31 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*  32 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  34 */   private static final VoxelShape SHAPE_STANDING = Shapes.or(
/*  35 */       Block.column(4.0D, 7.0D, 9.0D), 
/*  36 */       Block.column(6.0D, 0.0D, 7.0D));
/*     */   
/*  38 */   private static final VoxelShape SHAPE_HANGING = SHAPE_STANDING.move(0.0D, 0.0625D, 0.0D).optimize();
/*     */   
/*     */   public LanternBlock(BlockBehaviour.Properties properties) {
/*  41 */     super(properties);
/*  42 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HANGING, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  47 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/*  49 */     for (Direction direction : context.getNearestLookingDirections()) {
/*     */       
/*  51 */       if (direction.getAxis() == Direction.Axis.Y) {
/*  52 */         BlockState state = (BlockState)defaultBlockState().setValue(HANGING, Boolean.valueOf((direction == Direction.UP)));
/*     */         
/*  54 */         if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
/*  55 */           return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return ((Boolean)state.getValue(HANGING)).booleanValue() ? SHAPE_HANGING : SHAPE_STANDING; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { HANGING, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  75 */     Direction direction = getConnectedDirection(state).getOpposite();
/*  76 */     return Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite());
/*     */   }
/*     */ 
/*     */   
/*  80 */   protected static Direction getConnectedDirection(BlockState state) { return ((Boolean)state.getValue(HANGING)).booleanValue() ? Direction.DOWN : Direction.UP; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  85 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  86 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  88 */     if (getConnectedDirection(state).getOpposite() == directionToNeighbour && !state.canSurvive(level, pos)) {
/*  89 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  91 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  96 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  97 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  99 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LanternBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */