/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ 
/*    */ public abstract class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {
/* 17 */   public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
/*    */ 
/*    */   
/* 20 */   protected FaceAttachedHorizontalDirectionalBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec();
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canAttach(level, pos, getConnectedDirection(state).getOpposite()); }
/*    */ 
/*    */   
/*    */   public static boolean canAttach(LevelReader level, BlockPos pos, Direction direction) {
/* 32 */     BlockPos relative = pos.relative(direction);
/* 33 */     return level.getBlockState(relative).isFaceSturdy(level, relative, direction.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 38 */     for (Direction direction : context.getNearestLookingDirections()) {
/*    */       BlockState state;
/* 40 */       if (direction.getAxis() == Direction.Axis.Y) {
/* 41 */         state = (BlockState)((BlockState)defaultBlockState().setValue(FACE, (direction == Direction.UP) ? AttachFace.CEILING : AttachFace.FLOOR)).setValue(FACING, context.getHorizontalDirection());
/*    */       } else {
/* 43 */         state = (BlockState)((BlockState)defaultBlockState().setValue(FACE, AttachFace.WALL)).setValue(FACING, direction.getOpposite());
/*    */       } 
/*    */       
/* 46 */       if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
/* 47 */         return state;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if (getConnectedDirection(state).getOpposite() == directionToNeighbour && !state.canSurvive(level, pos)) {
/* 57 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 59 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected static Direction getConnectedDirection(BlockState state) {
/* 63 */     switch ((AttachFace)state.getValue(FACE)) {
/*    */       case CEILING:
/* 65 */         return Direction.DOWN;
/*    */       case FLOOR:
/* 67 */         return Direction.UP;
/*    */     } 
/* 69 */     return (Direction)state.getValue(FACING);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FaceAttachedHorizontalDirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */