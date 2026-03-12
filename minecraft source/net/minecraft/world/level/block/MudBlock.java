/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class MudBlock extends Block {
/* 13 */   public static final MapCodec<MudBlock> CODEC = simpleCodec(MudBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<MudBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 20 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 14.0D);
/*    */ 
/*    */   
/* 23 */   public MudBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.block(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.block(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 0.2F; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MudBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */