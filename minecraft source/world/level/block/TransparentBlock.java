/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TransparentBlock extends HalfTransparentBlock {
/* 12 */   public static final MapCodec<TransparentBlock> CODEC = simpleCodec(TransparentBlock::new);
/*    */   
/* 14 */   protected TransparentBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected MapCodec<? extends TransparentBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 1.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected boolean propagatesSkylightDown(BlockState state) { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */