/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class SpongeBlock extends Block {
/* 17 */   public static final MapCodec<SpongeBlock> CODEC = simpleCodec(SpongeBlock::new);
/*    */   public static final int MAX_DEPTH = 6;
/*    */   public static final int MAX_COUNT = 64;
/*    */   
/* 21 */   public MapCodec<SpongeBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private static final Direction[] ALL_DIRECTIONS = Direction.values();
/*    */ 
/*    */   
/* 30 */   protected SpongeBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 35 */     if (oldState.is(state.getBlock())) {
/*    */       return;
/*    */     }
/* 38 */     tryAbsorbWater(level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 43 */     tryAbsorbWater(level, pos);
/* 44 */     super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
/*    */   }
/*    */   
/*    */   protected void tryAbsorbWater(Level level, BlockPos pos) {
/* 48 */     if (removeWaterBreadthFirstSearch(level, pos)) {
/*    */       
/* 50 */       level.setBlock(pos, Blocks.WET_SPONGE.defaultBlockState(), 2);
/* 51 */       level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   private boolean removeWaterBreadthFirstSearch(Level level, BlockPos startPos) { return (BlockPos.breadthFirstTraversal(startPos, 6, 65, (pos, consumer) -> {
/* 59 */           for (Direction direction : ALL_DIRECTIONS) {
/* 60 */             consumer.accept(pos.relative(direction));
/*    */           }
/*    */         }pos -> {
/*    */           
/* 64 */           if (pos.equals(startPos)) {
/* 65 */             return BlockPos.TraversalNodeStatus.ACCEPT;
/*    */           }
/*    */           
/* 68 */           BlockState state = level.getBlockState(pos);
/* 69 */           FluidState fluidState = level.getFluidState(pos);
/* 70 */           if (!fluidState.is(FluidTags.WATER)) {
/* 71 */             return BlockPos.TraversalNodeStatus.SKIP;
/*    */           }
/* 73 */           Block patt0$temp = state.getBlock(); if (patt0$temp instanceof BucketPickup) { BucketPickup bucketPickup = (BucketPickup)patt0$temp; if (!bucketPickup.pickupBlock(null, level, pos, state).isEmpty())
/* 74 */               return BlockPos.TraversalNodeStatus.ACCEPT;  }
/*    */           
/* 76 */           if (state.getBlock() instanceof LiquidBlock) {
/* 77 */             level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/* 78 */           } else if (state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT) || state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS)) {
/*    */             
/* 80 */             BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/* 81 */             dropResources(state, level, pos, blockEntity);
/* 82 */             level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*    */           } else {
/* 84 */             return BlockPos.TraversalNodeStatus.SKIP;
/*    */           } 
/* 86 */           return BlockPos.TraversalNodeStatus.ACCEPT;
/*    */         }) > 1); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SpongeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */