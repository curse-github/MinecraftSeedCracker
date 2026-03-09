/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.BlockUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBodyBlock
/*    */   extends GrowingPlantBlock implements BonemealableBlock {
/* 23 */   protected GrowingPlantBodyBlock(BlockBehaviour.Properties properties, Direction growthDirection, VoxelShape shape, boolean scheduleFluidTicks) { super(properties, growthDirection, shape, scheduleFluidTicks); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends GrowingPlantBodyBlock> codec();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected BlockState updateHeadAfterConvertedFromBody(BlockState bodyState, BlockState headState) { return headState; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 38 */     if (directionToNeighbour == this.growthDirection.getOpposite() && !state.canSurvive(level, pos)) {
/* 39 */       ticks.scheduleTick(pos, this, 1);
/*    */     }
/*    */     
/* 42 */     GrowingPlantHeadBlock headBlock = getHeadBlock();
/* 43 */     if (directionToNeighbour == this.growthDirection && 
/* 44 */       !neighbourState.is(this) && !neighbourState.is(headBlock))
/*    */     {
/* 46 */       return updateHeadAfterConvertedFromBody(state, headBlock.getStateForPlacement(random));
/*    */     }
/*    */ 
/*    */     
/* 50 */     if (this.scheduleFluidTicks) {
/* 51 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(getHeadBlock()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 64 */     Optional<BlockPos> headPos = getHeadPos(level, pos, state.getBlock());
/* 65 */     return (headPos.isPresent() && getHeadBlock().canGrowInto(level.getBlockState(((BlockPos)headPos.get()).relative(this.growthDirection))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 75 */     Optional<BlockPos> headPos = getHeadPos(level, pos, state.getBlock());
/*    */     
/* 77 */     if (headPos.isPresent()) {
/* 78 */       BlockState forwardState = level.getBlockState((BlockPos)headPos.get());
/* 79 */       ((GrowingPlantHeadBlock)forwardState.getBlock()).performBonemeal(level, random, (BlockPos)headPos.get(), forwardState);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 84 */   private Optional<BlockPos> getHeadPos(BlockGetter level, BlockPos pos, Block bodyBlock) { return BlockUtil.getTopConnectedBlock(level, pos, bodyBlock, this.growthDirection, getHeadBlock()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 89 */     boolean result = super.canBeReplaced(state, context);
/* 90 */     if (result && context.getItemInHand().is(getHeadBlock().asItem())) {
/* 91 */       return false;
/*    */     }
/* 93 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 98 */   protected Block getBodyBlock() { return this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantBodyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */