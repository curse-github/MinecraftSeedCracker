/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ public interface SegmentableBlock
/*    */ {
/*    */   public static final int MIN_SEGMENT = 1;
/*    */   public static final int MAX_SEGMENT = 4;
/* 19 */   public static final IntegerProperty AMOUNT = BlockStateProperties.SEGMENT_AMOUNT;
/*    */   
/*    */   default Function<BlockState, VoxelShape> getShapeCalculator(EnumProperty<Direction> facing, IntegerProperty amount) {
/* 22 */     Map<Direction, VoxelShape> shapes = Shapes.rotateHorizontal(Block.box(0.0D, 0.0D, 0.0D, 8.0D, getShapeHeight(), 8.0D));
/* 23 */     return state -> {
/* 24 */         VoxelShape shape = Shapes.empty();
/*    */         
/* 26 */         Direction direction = (Direction)state.getValue(facing);
/* 27 */         int count = ((Integer)state.getValue(amount)).intValue();
/* 28 */         for (int i = 0; i < count; i++) {
/* 29 */           shape = Shapes.or(shape, (VoxelShape)shapes.get(direction));
/* 30 */           direction = direction.getCounterClockWise();
/*    */         } 
/*    */         
/* 33 */         return shape.singleEncompassing();
/*    */       };
/*    */   }
/*    */ 
/*    */   
/* 38 */   default IntegerProperty getSegmentAmountProperty() { return AMOUNT; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   default double getShapeHeight() { return 1.0D; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   default boolean canBeReplaced(BlockState state, BlockPlaceContext context, IntegerProperty segment) { return (!context.isSecondaryUseActive() && context.getItemInHand().is(state.getBlock().asItem()) && ((Integer)state.getValue(segment)).intValue() < 4); }
/*    */ 
/*    */ 
/*    */   
/*    */   default BlockState getStateForPlacement(BlockPlaceContext context, Block block, IntegerProperty segment, EnumProperty<Direction> facing) {
/* 51 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/* 52 */     if (state.is(block)) {
/* 53 */       return (BlockState)state.setValue(segment, Integer.valueOf(Math.min(4, ((Integer)state.getValue(segment)).intValue() + 1)));
/*    */     }
/* 55 */     return (BlockState)block.defaultBlockState().setValue(facing, context.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SegmentableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */