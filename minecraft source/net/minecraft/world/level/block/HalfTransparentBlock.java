/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HalfTransparentBlock extends Block {
/*  8 */   public static final MapCodec<HalfTransparentBlock> CODEC = simpleCodec(HalfTransparentBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 12 */   protected MapCodec<? extends HalfTransparentBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected HalfTransparentBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/* 21 */     if (neighborState.is(this)) {
/* 22 */       return true;
/*    */     }
/* 24 */     return super.skipRendering(state, neighborState, direction);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HalfTransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */