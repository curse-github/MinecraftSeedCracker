/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LeafLitterBlock extends VegetationBlock implements SegmentableBlock {
/* 19 */   public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
/*    */   
/* 21 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   private final Function<BlockState, VoxelShape> shapes;
/*    */   
/*    */   public LeafLitterBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/* 27 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(getSegmentAmountProperty(), Integer.valueOf(1)));
/* 28 */     this.shapes = makeShapes();
/*    */   }
/*    */ 
/*    */   
/* 32 */   private Function<BlockState, VoxelShape> makeShapes() { return getShapeForEachState(getShapeCalculator(FACING, getSegmentAmountProperty())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected MapCodec<LeafLitterBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 52 */     if (canBeReplaced(state, context, getSegmentAmountProperty())) {
/* 53 */       return true;
/*    */     }
/* 55 */     return super.canBeReplaced(state, context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 60 */     BlockPos belowPos = pos.below();
/* 61 */     return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return getStateForPlacement(context, this, getSegmentAmountProperty(), FACING); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, getSegmentAmountProperty() }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LeafLitterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */