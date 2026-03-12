/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleStateProvider extends BlockStateProvider {
/*  9 */   public static final MapCodec<SimpleStateProvider> CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, p -> p.state);
/*    */   
/*    */   private final BlockState state;
/*    */ 
/*    */   
/* 14 */   protected SimpleStateProvider(BlockState state) { this.state = state; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.SIMPLE_STATE_PROVIDER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public BlockState getState(RandomSource random, BlockPos pos) { return this.state; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\SimpleStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */