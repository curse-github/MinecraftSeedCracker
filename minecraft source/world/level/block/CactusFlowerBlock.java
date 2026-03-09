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
/*    */ public class CactusFlowerBlock extends VegetationBlock {
/* 12 */   public static final MapCodec<CactusFlowerBlock> CODEC = simpleCodec(CactusFlowerBlock::new);
/* 13 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 12.0D);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<? extends CactusFlowerBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public CactusFlowerBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 31 */     BlockState blockBelow = level.getBlockState(pos);
/* 32 */     return (blockBelow.is(Blocks.CACTUS) || blockBelow.is(Blocks.FARMLAND) || blockBelow.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CactusFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */