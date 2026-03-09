/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class BlockStateProviderType<P extends BlockStateProvider> extends Object {
/*  8 */   public static final BlockStateProviderType<SimpleStateProvider> SIMPLE_STATE_PROVIDER = register("simple_state_provider", SimpleStateProvider.CODEC);
/*  9 */   public static final BlockStateProviderType<WeightedStateProvider> WEIGHTED_STATE_PROVIDER = register("weighted_state_provider", WeightedStateProvider.CODEC);
/* 10 */   public static final BlockStateProviderType<NoiseThresholdProvider> NOISE_THRESHOLD_PROVIDER = register("noise_threshold_provider", NoiseThresholdProvider.CODEC);
/* 11 */   public static final BlockStateProviderType<NoiseProvider> NOISE_PROVIDER = register("noise_provider", NoiseProvider.CODEC);
/* 12 */   public static final BlockStateProviderType<DualNoiseProvider> DUAL_NOISE_PROVIDER = register("dual_noise_provider", DualNoiseProvider.CODEC);
/* 13 */   public static final BlockStateProviderType<RotatedBlockProvider> ROTATED_BLOCK_PROVIDER = register("rotated_block_provider", RotatedBlockProvider.CODEC);
/* 14 */   public static final BlockStateProviderType<RandomizedIntStateProvider> RANDOMIZED_INT_STATE_PROVIDER = register("randomized_int_state_provider", RandomizedIntStateProvider.CODEC);
/*    */   private final MapCodec<P> codec;
/*    */   
/* 17 */   private static <P extends BlockStateProvider> BlockStateProviderType<P> register(String name, MapCodec<P> codec) { return (BlockStateProviderType)Registry.register(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, name, new BlockStateProviderType(codec)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private BlockStateProviderType(MapCodec<P> codec) { this.codec = codec; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public MapCodec<P> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */