/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class BaseCoralPlantTypeBlock extends Block implements SimpleWaterloggedBlock {
/* 23 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 4.0D);
/*    */   
/*    */   protected BaseCoralPlantTypeBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(properties);
/* 29 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends BaseCoralPlantTypeBlock> codec();
/*    */   
/*    */   protected void tryScheduleDieTick(BlockState state, BlockGetter level, ScheduledTickAccess ticks, RandomSource random, BlockPos pos) {
/* 36 */     if (!scanForWater(state, level, pos)) {
/* 37 */       ticks.scheduleTick(pos, this, 60 + random.nextInt(40));
/*    */     }
/*    */   }
/*    */   
/*    */   protected static boolean scanForWater(BlockState state, BlockGetter level, BlockPos blockPos) {
/* 42 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 43 */       return true;
/*    */     }
/*    */     
/* 46 */     for (Direction direction : Direction.values()) {
/* 47 */       if (level.getFluidState(blockPos.relative(direction)).is(FluidTags.WATER)) {
/* 48 */         return true;
/*    */       }
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 56 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/*    */     
/* 58 */     return (BlockState)defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf((fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 68 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 69 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 72 */     if (directionToNeighbour == Direction.DOWN && !canSurvive(state, level, pos)) {
/* 73 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 75 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 80 */     BlockPos below = pos.below();
/* 81 */     return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 86 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED }); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 91 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 92 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 95 */     return super.getFluidState(state);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BaseCoralPlantTypeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */