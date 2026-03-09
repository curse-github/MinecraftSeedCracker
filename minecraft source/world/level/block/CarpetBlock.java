/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CarpetBlock extends Block {
/* 15 */   public static final MapCodec<CarpetBlock> CODEC = simpleCodec(CarpetBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<? extends CarpetBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.0D);
/*    */ 
/*    */   
/* 25 */   public CarpetBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 35 */     if (!state.canSurvive(level, pos)) {
/* 36 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 39 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return !level.isEmptyBlock(pos.below()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CarpetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */