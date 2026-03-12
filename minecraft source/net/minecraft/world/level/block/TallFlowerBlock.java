/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TallFlowerBlock extends DoublePlantBlock implements BonemealableBlock {
/* 13 */   public static final MapCodec<TallFlowerBlock> CODEC = simpleCodec(TallFlowerBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<TallFlowerBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public TallFlowerBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { popResource(level, pos, new ItemStack(this)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TallFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */