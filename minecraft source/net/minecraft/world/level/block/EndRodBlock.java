/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ 
/*    */ public class EndRodBlock extends RodBlock {
/* 14 */   public static final MapCodec<EndRodBlock> CODEC = simpleCodec(EndRodBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 18 */   public MapCodec<EndRodBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   protected EndRodBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/* 23 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 29 */     Direction clickedFace = context.getClickedFace();
/*    */     
/* 31 */     BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(clickedFace.getOpposite()));
/* 32 */     if (blockState.is(this) && blockState.getValue(FACING) == clickedFace) {
/* 33 */       return (BlockState)defaultBlockState().setValue(FACING, clickedFace.getOpposite());
/*    */     }
/*    */     
/* 36 */     return (BlockState)defaultBlockState().setValue(FACING, clickedFace);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 41 */     Direction direction = (Direction)state.getValue(FACING);
/* 42 */     double x = pos.getX() + 0.55D - (random.nextFloat() * 0.1F);
/* 43 */     double y = pos.getY() + 0.55D - (random.nextFloat() * 0.1F);
/* 44 */     double z = pos.getZ() + 0.55D - (random.nextFloat() * 0.1F);
/* 45 */     double r = (0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F);
/*    */     
/* 47 */     if (random.nextInt(5) == 0) {
/* 48 */       level.addParticle(ParticleTypes.END_ROD, x + direction.getStepX() * r, y + direction.getStepY() * r, z + direction.getStepZ() * r, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EndRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */