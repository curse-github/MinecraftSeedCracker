/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherSproutsBlock extends VegetationBlock {
/* 12 */   public static final MapCodec<NetherSproutsBlock> CODEC = simpleCodec(NetherSproutsBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public MapCodec<NetherSproutsBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 19 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 3.0D);
/*    */ 
/*    */   
/* 22 */   public NetherSproutsBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (state.is(BlockTags.NYLIUM) || state.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(state, level, pos)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\NetherSproutsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */