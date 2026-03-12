/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ 
/*    */ public abstract class DirectionalBlock extends Block {
/*  9 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*    */ 
/*    */   
/* 12 */   protected DirectionalBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */   
/*    */   protected abstract MapCodec<? extends DirectionalBlock> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */