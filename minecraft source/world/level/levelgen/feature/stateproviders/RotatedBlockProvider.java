/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RotatedBlockProvider extends BlockStateProvider {
/* 12 */   public static final MapCodec<RotatedBlockProvider> CODEC = BlockState.CODEC.fieldOf("state")
/* 13 */     .xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState)
/* 14 */     .xmap(RotatedBlockProvider::new, p -> p.block);
/*    */   
/*    */   private final Block block;
/*    */ 
/*    */   
/* 19 */   public RotatedBlockProvider(Block block) { this.block = block; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.ROTATED_BLOCK_PROVIDER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, BlockPos pos) {
/* 29 */     Direction.Axis randomAxis = Direction.Axis.getRandom(random);
/* 30 */     return (BlockState)this.block.defaultBlockState().trySetValue(RotatedPillarBlock.AXIS, randomAxis);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RotatedBlockProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */