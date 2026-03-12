/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BaseCoralFanBlock extends BaseCoralPlantTypeBlock {
/* 11 */   public static final MapCodec<BaseCoralFanBlock> CODEC = simpleCodec(BaseCoralFanBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 15 */   public MapCodec<? extends BaseCoralFanBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 18 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 4.0D);
/*    */ 
/*    */   
/* 21 */   protected BaseCoralFanBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BaseCoralFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */