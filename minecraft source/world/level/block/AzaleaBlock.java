/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AzaleaBlock extends VegetationBlock implements BonemealableBlock {
/* 18 */   public static final MapCodec<AzaleaBlock> CODEC = simpleCodec(AzaleaBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<AzaleaBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Shapes.or(
/* 26 */       Block.column(16.0D, 8.0D, 16.0D), 
/* 27 */       Block.column(4.0D, 0.0D, 8.0D));
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected AzaleaBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (state.is(Blocks.CLAY) || super.mayPlaceOn(state, level, pos)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getFluidState(pos.above()).isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return (level.random.nextFloat() < 0.45D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { TreeGrower.AZALEA.growTree(level, level.getChunkSource().getGenerator(), pos, state, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AzaleaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */