/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BuddingAmethystBlock extends AmethystBlock {
/* 13 */   public static final MapCodec<BuddingAmethystBlock> CODEC = simpleCodec(BuddingAmethystBlock::new);
/*    */   
/*    */   public static final int GROWTH_CHANCE = 5;
/*    */   
/* 17 */   public MapCodec<BuddingAmethystBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */ 
/*    */   
/* 25 */   public BuddingAmethystBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 30 */     if (random.nextInt(5) != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     Direction growDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
/* 35 */     BlockPos growPos = pos.relative(growDirection);
/* 36 */     BlockState relativeState = level.getBlockState(growPos);
/* 37 */     Block nextStage = null;
/* 38 */     if (canClusterGrowAtState(relativeState)) {
/* 39 */       nextStage = Blocks.SMALL_AMETHYST_BUD;
/* 40 */     } else if (relativeState.is(Blocks.SMALL_AMETHYST_BUD) && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
/* 41 */       nextStage = Blocks.MEDIUM_AMETHYST_BUD;
/* 42 */     } else if (relativeState.is(Blocks.MEDIUM_AMETHYST_BUD) && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
/* 43 */       nextStage = Blocks.LARGE_AMETHYST_BUD;
/* 44 */     } else if (relativeState.is(Blocks.LARGE_AMETHYST_BUD) && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
/* 45 */       nextStage = Blocks.AMETHYST_CLUSTER;
/*    */     } 
/*    */     
/* 48 */     if (nextStage != null) {
/*    */ 
/*    */       
/* 51 */       BlockState targetState = (BlockState)((BlockState)nextStage.defaultBlockState().setValue(AmethystClusterBlock.FACING, growDirection)).setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf((relativeState.getFluidState().getType() == Fluids.WATER)));
/* 52 */       level.setBlockAndUpdate(growPos, targetState);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 57 */   public static boolean canClusterGrowAtState(BlockState state) { return (state.isAir() || (state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BuddingAmethystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */