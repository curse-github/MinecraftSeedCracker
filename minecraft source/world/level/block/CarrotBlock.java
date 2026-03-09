/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CarrotBlock extends CropBlock {
/* 13 */   public static final MapCodec<CarrotBlock> CODEC = simpleCodec(CarrotBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<CarrotBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 20 */   private static final VoxelShape[] SHAPES = Block.boxes(7, age -> Block.column(16.0D, 0.0D, (2 + age)));
/*    */ 
/*    */   
/* 23 */   public CarrotBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected ItemLike getBaseSeedId() { return Items.CARROT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[getAge(state)]; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CarrotBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */