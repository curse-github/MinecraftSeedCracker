/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SeagrassBlock extends VegetationBlock implements BonemealableBlock, LiquidBlockContainer {
/* 26 */   public static final MapCodec<SeagrassBlock> CODEC = simpleCodec(SeagrassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<SeagrassBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */ 
/*    */   
/* 36 */   protected SeagrassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (state.isFaceSturdy(level, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 51 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/*    */     
/* 53 */     if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
/* 54 */       return super.getStateForPlacement(context);
/*    */     }
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 61 */     BlockState result = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/* 62 */     if (!result.isAir()) {
/* 63 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/* 65 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.above()).is(Blocks.WATER); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 80 */   protected FluidState getFluidState(BlockState state) { return Fluids.WATER.getSource(false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 85 */     BlockState lowerState = Blocks.TALL_SEAGRASS.defaultBlockState();
/* 86 */     BlockState upperState = (BlockState)lowerState.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
/* 87 */     BlockPos above = pos.above();
/* 88 */     level.setBlock(pos, lowerState, 2);
/* 89 */     level.setBlock(above, upperState, 2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SeagrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */