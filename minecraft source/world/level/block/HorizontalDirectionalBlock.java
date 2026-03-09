/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ 
/*    */ public abstract class HorizontalDirectionalBlock extends Block {
/* 10 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */ 
/*    */   
/* 13 */   protected HorizontalDirectionalBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends HorizontalDirectionalBlock> codec();
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HorizontalDirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */