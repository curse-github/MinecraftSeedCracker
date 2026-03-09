/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
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
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class HangingRootsBlock extends Block implements SimpleWaterloggedBlock {
/* 22 */   public static final MapCodec<HangingRootsBlock> CODEC = simpleCodec(HangingRootsBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<HangingRootsBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 29 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 10.0D, 16.0D);
/*    */   
/*    */   protected HangingRootsBlock(BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED }); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 45 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 46 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 48 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 53 */     BlockState state = super.getStateForPlacement(context);
/* 54 */     if (state != null) {
/* 55 */       FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 56 */       return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 63 */     BlockPos attachedToPos = pos.above();
/* 64 */     BlockState attachedToState = level.getBlockState(attachedToPos);
/* 65 */     return attachedToState.isFaceSturdy(level, attachedToPos, Direction.DOWN);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 75 */     if (directionToNeighbour == Direction.UP && !canSurvive(state, level, pos)) {
/* 76 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 78 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 79 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/* 81 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HangingRootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */