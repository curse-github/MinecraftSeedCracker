/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RootedDirtBlock extends Block implements BonemealableBlock {
/* 12 */   public static final MapCodec<RootedDirtBlock> CODEC = simpleCodec(RootedDirtBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<RootedDirtBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public RootedDirtBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.below()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.setBlockAndUpdate(pos.below(), Blocks.HANGING_ROOTS.defaultBlockState()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public BlockPos getParticlePos(BlockPos blockPos) { return blockPos.below(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RootedDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */