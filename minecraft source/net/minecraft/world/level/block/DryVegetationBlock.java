/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DryVegetationBlock extends VegetationBlock {
/* 15 */   public static final MapCodec<DryVegetationBlock> CODEC = simpleCodec(DryVegetationBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 19 */   public MapCodec<? extends DryVegetationBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */ 
/*    */   
/* 25 */   protected DryVegetationBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return state.is(BlockTags.DRY_VEGETATION_MAY_PLACE_ON); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) { AmbientDesertBlockSoundsPlayer.playAmbientDeadBushSounds(level, pos, random); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DryVegetationBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */