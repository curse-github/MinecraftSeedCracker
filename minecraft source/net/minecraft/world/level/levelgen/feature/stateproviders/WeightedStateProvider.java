/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeightedStateProvider extends BlockStateProvider {
/* 11 */   public static final MapCodec<WeightedStateProvider> CODEC = WeightedList.nonEmptyCodec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, p -> p.weightedList).fieldOf("entries");
/*    */   private final WeightedList<BlockState> weightedList;
/*    */   
/*    */   private static DataResult<WeightedStateProvider> create(WeightedList<BlockState> weightedList) {
/* 15 */     if (weightedList.isEmpty()) {
/* 16 */       return DataResult.error(() -> "WeightedStateProvider with no states");
/*    */     }
/* 18 */     return DataResult.success(new WeightedStateProvider(weightedList));
/*    */   }
/*    */ 
/*    */   
/* 22 */   public WeightedStateProvider(WeightedList<BlockState> weightedList) { this.weightedList = weightedList; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public WeightedStateProvider(WeightedList.Builder<BlockState> weightedList) { this(weightedList.build()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.WEIGHTED_STATE_PROVIDER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public BlockState getState(RandomSource random, BlockPos pos) { return (BlockState)this.weightedList.getRandomOrThrow(random); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\WeightedStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */