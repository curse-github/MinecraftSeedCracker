/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class NetherrackBlock extends Block implements BonemealableBlock {
/* 13 */   public static final MapCodec<NetherrackBlock> CODEC = simpleCodec(NetherrackBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 17 */   public MapCodec<NetherrackBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public NetherrackBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 26 */     if (!level.getBlockState(pos.above()).propagatesSkylightDown()) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
/* 31 */       if (level.getBlockState(blockPos).is(BlockTags.NYLIUM)) {
/* 32 */         return true;
/*    */       }
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 45 */     boolean foundRed = false;
/* 46 */     boolean foundBlue = false;
/* 47 */     for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
/* 48 */       BlockState blockState = level.getBlockState(blockPos);
/* 49 */       if (blockState.is(Blocks.WARPED_NYLIUM)) {
/* 50 */         foundBlue = true;
/*    */       }
/*    */       
/* 53 */       if (blockState.is(Blocks.CRIMSON_NYLIUM)) {
/* 54 */         foundRed = true;
/*    */       }
/*    */       
/* 57 */       if (foundBlue && foundRed) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 62 */     if (foundBlue && foundRed) {
/* 63 */       level.setBlock(pos, random.nextBoolean() ? Blocks.WARPED_NYLIUM.defaultBlockState() : Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/* 64 */     } else if (foundBlue) {
/* 65 */       level.setBlock(pos, Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
/* 66 */     } else if (foundRed) {
/* 67 */       level.setBlock(pos, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public BonemealableBlock.Type getType() { return BonemealableBlock.Type.NEIGHBOR_SPREADER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\NetherrackBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */