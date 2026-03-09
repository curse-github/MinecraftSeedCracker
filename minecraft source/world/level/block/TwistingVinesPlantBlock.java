/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVinesPlantBlock extends GrowingPlantBodyBlock {
/*  8 */   public static final MapCodec<TwistingVinesPlantBlock> CODEC = simpleCodec(TwistingVinesPlantBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 12 */   public MapCodec<TwistingVinesPlantBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 15 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */ 
/*    */   
/* 18 */   public TwistingVinesPlantBlock(BlockBehaviour.Properties properties) { super(properties, Direction.UP, SHAPE, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected GrowingPlantHeadBlock getHeadBlock() { return (GrowingPlantHeadBlock)Blocks.TWISTING_VINES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TwistingVinesPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */