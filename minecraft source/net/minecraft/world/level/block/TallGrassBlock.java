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
/*    */ public class TallGrassBlock extends VegetationBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(TallGrassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<TallGrassBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */ 
/*    */   
/* 25 */   protected TallGrassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return (getGrownBlock(state).defaultBlockState().canSurvive(level, pos) && level.isEmptyBlock(pos.above())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { DoublePlantBlock.placeAt(level, getGrownBlock(state).defaultBlockState(), pos, 2); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   private static DoublePlantBlock getGrownBlock(BlockState state) { return (DoublePlantBlock)(state.is(Blocks.FERN) ? Blocks.LARGE_FERN : Blocks.TALL_GRASS); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TallGrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */