/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallDryGrassBlock extends DryVegetationBlock implements BonemealableBlock {
/* 16 */   public static final MapCodec<TallDryGrassBlock> CODEC = simpleCodec(TallDryGrassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 20 */   public MapCodec<TallDryGrassBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 23 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */ 
/*    */   
/* 26 */   protected TallDryGrassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) { AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(level, pos, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, Blocks.SHORT_DRY_GRASS.defaultBlockState()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { BonemealableBlock.findSpreadableNeighbourPos(level, pos, Blocks.SHORT_DRY_GRASS.defaultBlockState()).ifPresent(blockPos -> level.setBlockAndUpdate(blockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TallDryGrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */