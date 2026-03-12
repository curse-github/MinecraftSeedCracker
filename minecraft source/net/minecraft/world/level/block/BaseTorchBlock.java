/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class BaseTorchBlock extends Block {
/* 15 */   private static final VoxelShape SHAPE = Block.column(4.0D, 0.0D, 10.0D);
/*    */ 
/*    */   
/* 18 */   protected BaseTorchBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends BaseTorchBlock> codec();
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 31 */     if (directionToNeighbour == Direction.DOWN && !canSurvive(state, level, pos)) {
/* 32 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 34 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canSupportCenter(level, pos.below(), Direction.UP); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BaseTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */