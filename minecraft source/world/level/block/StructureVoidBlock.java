/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class StructureVoidBlock
/*    */   extends Block
/*    */ {
/* 15 */   public static final MapCodec<StructureVoidBlock> CODEC = simpleCodec(StructureVoidBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<StructureVoidBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.cube(6.0D);
/*    */ 
/*    */   
/* 25 */   protected StructureVoidBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 1.0F; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\StructureVoidBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */