/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ 
/*    */ public class GlazedTerracottaBlock extends HorizontalDirectionalBlock {
/*  9 */   public static final MapCodec<GlazedTerracottaBlock> CODEC = simpleCodec(GlazedTerracottaBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 13 */   public MapCodec<GlazedTerracottaBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public GlazedTerracottaBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GlazedTerracottaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */