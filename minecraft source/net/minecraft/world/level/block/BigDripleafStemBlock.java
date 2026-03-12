/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ public class BigDripleafStemBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  30 */   public static final MapCodec<BigDripleafStemBlock> CODEC = simpleCodec(BigDripleafStemBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  34 */   public MapCodec<BigDripleafStemBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  37 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  39 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 16.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*     */   
/*     */   protected BigDripleafStemBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(false))).setValue(FACING, Direction.NORTH));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED, FACING }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  58 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  59 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/*  62 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  67 */     BlockPos belowPos = pos.below();
/*  68 */     BlockState belowState = level.getBlockState(belowPos);
/*  69 */     BlockState aboveState = level.getBlockState(pos.above());
/*  70 */     return ((belowState.is(this) || belowState.is(BlockTags.BIG_DRIPLEAF_PLACEABLE)) && (aboveState
/*  71 */       .is(this) || aboveState.is(Blocks.BIG_DRIPLEAF)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean place(LevelAccessor level, BlockPos pos, FluidState fluidState, Direction facing) {
/*  77 */     BlockState newState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF_STEM.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType(Fluids.WATER)))).setValue(FACING, facing);
/*  78 */     return level.setBlock(pos, newState, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  83 */     if ((directionToNeighbour == Direction.DOWN || directionToNeighbour == Direction.UP) && !state.canSurvive(level, pos)) {
/*  84 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*  86 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  87 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  89 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  94 */     if (!state.canSurvive(level, pos)) {
/*  95 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 101 */     Optional<BlockPos> headPos = BlockUtil.getTopConnectedBlock(level, pos, state.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 102 */     if (headPos.isEmpty()) {
/* 103 */       return false;
/*     */     }
/* 105 */     BlockPos abovePos = ((BlockPos)headPos.get()).above();
/* 106 */     BlockState aboveState = level.getBlockState(abovePos);
/* 107 */     return BigDripleafBlock.canPlaceAt(level, abovePos, aboveState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 117 */     Optional<BlockPos> forwardPos = BlockUtil.getTopConnectedBlock(level, pos, state.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 118 */     if (forwardPos.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     BlockPos headPos = (BlockPos)forwardPos.get();
/* 123 */     BlockPos placeHeadPos = headPos.above();
/* 124 */     Direction facing = (Direction)state.getValue(FACING);
/*     */     
/* 126 */     place(level, headPos, level.getFluidState(headPos), facing);
/* 127 */     BigDripleafBlock.place(level, placeHeadPos, level.getFluidState(placeHeadPos), facing);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Blocks.BIG_DRIPLEAF); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BigDripleafStemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */