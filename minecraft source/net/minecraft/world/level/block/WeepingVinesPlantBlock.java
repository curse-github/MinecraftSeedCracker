/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesPlantBlock extends GrowingPlantBodyBlock {
/*  8 */   public static final MapCodec<WeepingVinesPlantBlock> CODEC = simpleCodec(WeepingVinesPlantBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 12 */   public MapCodec<WeepingVinesPlantBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 15 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */ 
/*    */   
/* 18 */   public WeepingVinesPlantBlock(BlockBehaviour.Properties properties) { super(properties, Direction.DOWN, SHAPE, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected GrowingPlantHeadBlock getHeadBlock() { return (GrowingPlantHeadBlock)Blocks.WEEPING_VINES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeepingVinesPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */