/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SoulFireBlock extends BaseFireBlock {
/* 13 */   public static final MapCodec<SoulFireBlock> CODEC = simpleCodec(SoulFireBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<SoulFireBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public SoulFireBlock(BlockBehaviour.Properties properties) { super(properties, 2.0F); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 26 */     if (canSurvive(state, level, pos)) {
/* 27 */       return defaultBlockState();
/*    */     }
/*    */     
/* 30 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canSurviveOnBlock(level.getBlockState(pos.below())); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static boolean canSurviveOnBlock(BlockState state) { return state.is(BlockTags.SOUL_FIRE_BASE_BLOCKS); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected boolean canBurn(BlockState state) { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SoulFireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */