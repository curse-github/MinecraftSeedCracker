/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class MangroveRootsBlock extends Block implements SimpleWaterloggedBlock {
/* 19 */   public static final MapCodec<MangroveRootsBlock> CODEC = simpleCodec(MangroveRootsBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<MangroveRootsBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected MangroveRootsBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) { return (neighborState.is(Blocks.MANGROVE_ROOTS) && direction.getAxis() == Direction.Axis.Y); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 42 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 43 */     return (BlockState)super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 48 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 49 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 52 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 57 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 58 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 61 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MangroveRootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */