/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ 
/*    */ public class RailBlock extends BaseRailBlock {
/* 14 */   public static final MapCodec<RailBlock> CODEC = simpleCodec(RailBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 18 */   public MapCodec<RailBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 21 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;
/*    */   
/*    */   protected RailBlock(BlockBehaviour.Properties properties) {
/* 24 */     super(false, properties);
/* 25 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(SHAPE, RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
/* 30 */     if (block.defaultBlockState().isSignalSource() && (
/* 31 */       new RailState(level, pos, state)).countPotentialConnections() == 3) {
/* 32 */       updateDir(level, pos, state, false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Property<RailShape> getShapeProperty() { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 44 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 45 */     RailShape newShape = rotate(currentShape, rotation);
/* 46 */     return (BlockState)state.setValue(SHAPE, newShape);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 51 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 52 */     RailShape newShape = mirror(currentShape, mirror);
/* 53 */     return (BlockState)state.setValue(SHAPE, newShape);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { SHAPE, WATERLOGGED }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */