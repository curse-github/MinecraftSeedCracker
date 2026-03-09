/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BlockStateProvider {
/* 11 */   public static final Codec<BlockStateProvider> CODEC = BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.byNameCodec().dispatch(BlockStateProvider::type, BlockStateProviderType::codec);
/*    */ 
/*    */   
/* 14 */   public static SimpleStateProvider simple(BlockState state) { return new SimpleStateProvider(state); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static SimpleStateProvider simple(Block block) { return new SimpleStateProvider(block.defaultBlockState()); }
/*    */   
/*    */   protected abstract BlockStateProviderType<?> type();
/*    */   
/*    */   public abstract BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */