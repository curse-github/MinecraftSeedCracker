/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ChainBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
/* 25 */   public static final MapCodec<ChainBlock> CODEC = simpleCodec(ChainBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 29 */   public MapCodec<? extends ChainBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 32 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 34 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateAllAxis(Block.cube(3.0D, 3.0D, 16.0D));
/*    */   
/*    */   public ChainBlock(BlockBehaviour.Properties properties) {
/* 37 */     super(properties);
/* 38 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(false))).setValue(AXIS, Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(AXIS)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 48 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 49 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 50 */     return (BlockState)super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 55 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 56 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/* 58 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED }).add(new Property[] { AXIS }); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 68 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 69 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 71 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ChainBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */