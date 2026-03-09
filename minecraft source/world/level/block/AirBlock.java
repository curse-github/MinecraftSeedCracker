/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AirBlock extends Block {
/* 12 */   public static final MapCodec<AirBlock> CODEC = simpleCodec(AirBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<AirBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public AirBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AirBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */