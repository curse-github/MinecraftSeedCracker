/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ 
/*    */ public class RotatedPillarBlock extends Block {
/* 12 */   public static final MapCodec<RotatedPillarBlock> CODEC = simpleCodec(RotatedPillarBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<? extends RotatedPillarBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 19 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*    */   
/*    */   public RotatedPillarBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/* 23 */     registerDefaultState((BlockState)defaultBlockState().setValue(AXIS, Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected BlockState rotate(BlockState state, Rotation rotation) { return rotatePillar(state, rotation); }
/*    */ 
/*    */   
/*    */   public static BlockState rotatePillar(BlockState state, Rotation rotation) {
/* 32 */     switch (rotation) {
/*    */       case COUNTERCLOCKWISE_90:
/*    */       case CLOCKWISE_90:
/* 35 */         switch ((Direction.Axis)state.getValue(AXIS)) {
/*    */           case COUNTERCLOCKWISE_90:
/* 37 */             return (BlockState)state.setValue(AXIS, Direction.Axis.Z);
/*    */           case CLOCKWISE_90:
/* 39 */             return (BlockState)state.setValue(AXIS, Direction.Axis.X);
/*    */         } 
/* 41 */         return state;
/*    */     } 
/*    */     
/* 44 */     return state;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AXIS }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */