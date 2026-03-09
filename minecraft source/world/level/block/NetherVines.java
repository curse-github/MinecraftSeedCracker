/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class NetherVines
/*    */ {
/*    */   private static final double BONEMEAL_GROW_PROBABILITY_DECREASE_RATE = 0.826D;
/*    */   public static final double GROW_PER_TICK_PROBABILITY = 0.1D;
/*    */   
/* 11 */   public static boolean isValidGrowthState(BlockState state) { return state.isAir(); }
/*    */ 
/*    */   
/*    */   public static int getBlocksToGrowWhenBonemealed(RandomSource random) {
/* 15 */     double growProbabilty = 1.0D;
/* 16 */     int count = 0;
/* 17 */     while (random.nextDouble() < growProbabilty) {
/* 18 */       growProbabilty *= 0.826D;
/* 19 */       count++;
/*    */     } 
/* 21 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\NetherVines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */