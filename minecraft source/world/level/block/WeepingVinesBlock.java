/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesBlock extends GrowingPlantHeadBlock {
/* 10 */   public static final MapCodec<WeepingVinesBlock> CODEC = simpleCodec(WeepingVinesBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 14 */   public MapCodec<WeepingVinesBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 17 */   private static final VoxelShape SHAPE = Block.column(8.0D, 9.0D, 16.0D);
/*    */ 
/*    */   
/* 20 */   public WeepingVinesBlock(BlockBehaviour.Properties properties) { super(properties, Direction.DOWN, SHAPE, false, 0.1D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) { return NetherVines.getBlocksToGrowWhenBonemealed(random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected Block getBodyBlock() { return Blocks.WEEPING_VINES_PLANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected boolean canGrowInto(BlockState state) { return NetherVines.isValidGrowthState(state); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeepingVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */