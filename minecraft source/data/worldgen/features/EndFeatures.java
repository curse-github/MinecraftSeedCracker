/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*    */ 
/*    */ public class EndFeatures {
/* 13 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_PLATFORM = FeatureUtils.createKey("end_platform");
/* 14 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_SPIKE = FeatureUtils.createKey("end_spike");
/* 15 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_RETURN = FeatureUtils.createKey("end_gateway_return");
/* 16 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_GATEWAY_DELAYED = FeatureUtils.createKey("end_gateway_delayed");
/* 17 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHORUS_PLANT = FeatureUtils.createKey("chorus_plant");
/* 18 */   public static final ResourceKey<ConfiguredFeature<?, ?>> END_ISLAND = FeatureUtils.createKey("end_island");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 21 */     FeatureUtils.register(context, END_PLATFORM, Feature.END_PLATFORM);
/* 22 */     FeatureUtils.register(context, END_SPIKE, Feature.END_SPIKE, new SpikeConfiguration(false, 
/*    */           
/* 24 */           ImmutableList.of(), null));
/*    */ 
/*    */     
/* 27 */     FeatureUtils.register(context, END_GATEWAY_RETURN, Feature.END_GATEWAY, 
/* 28 */         EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true));
/*    */     
/* 30 */     FeatureUtils.register(context, END_GATEWAY_DELAYED, Feature.END_GATEWAY, EndGatewayConfiguration.delayedExitSearch());
/* 31 */     FeatureUtils.register(context, CHORUS_PLANT, Feature.CHORUS_PLANT);
/* 32 */     FeatureUtils.register(context, END_ISLAND, Feature.END_ISLAND);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\features\EndFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */