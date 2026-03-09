/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BushBlock extends VegetationBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<BushBlock> CODEC = simpleCodec(BushBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<BushBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 13.0D);
/*    */ 
/*    */   
/* 25 */   protected BushBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, state); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { BonemealableBlock.findSpreadableNeighbourPos(level, pos, state).ifPresent(blockPos -> level.setBlockAndUpdate(blockPos, defaultBlockState())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BushBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */