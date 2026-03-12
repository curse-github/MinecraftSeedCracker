/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BambooSaplingBlock extends Block implements BonemealableBlock {
/* 21 */   public static final MapCodec<BambooSaplingBlock> CODEC = simpleCodec(BambooSaplingBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 25 */   public MapCodec<BambooSaplingBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 28 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 12.0D);
/*    */ 
/*    */   
/* 31 */   public BambooSaplingBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE.move(state.getOffset(pos)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 41 */     if (random.nextInt(3) == 0 && level.isEmptyBlock(pos.above()) && level.getRawBrightness(pos.above(), 0) >= 9) {
/* 42 */       growBamboo(level, pos);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 53 */     if (!state.canSurvive(level, pos)) {
/* 54 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 57 */     if (directionToNeighbour == Direction.UP && neighbourState.is(Blocks.BAMBOO)) {
/* 58 */       return Blocks.BAMBOO.defaultBlockState();
/*    */     }
/*    */     
/* 61 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Items.BAMBOO); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return level.getBlockState(pos.above()).isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { growBamboo(level, pos); }
/*    */ 
/*    */ 
/*    */   
/* 85 */   protected void growBamboo(Level level, BlockPos pos) { level.setBlock(pos.above(), (BlockState)Blocks.BAMBOO.defaultBlockState().setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BambooSaplingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */