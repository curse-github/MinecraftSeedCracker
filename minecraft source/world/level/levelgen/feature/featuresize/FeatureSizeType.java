/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FeatureSizeType<P extends FeatureSize> extends Object {
/*  8 */   public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
/*  9 */   public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
/*    */   private final MapCodec<P> codec;
/*    */   
/* 12 */   private static <P extends FeatureSize> FeatureSizeType<P> register(String name, MapCodec<P> codec) { return (FeatureSizeType)Registry.register(BuiltInRegistries.FEATURE_SIZE_TYPE, name, new FeatureSizeType(codec)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   private FeatureSizeType(MapCodec<P> codec) { this.codec = codec; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<P> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSizeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */