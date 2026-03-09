/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.RotatedBlockProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*    */ 
/*    */ public class PileFeatures
/*    */ {
/* 16 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_HAY = FeatureUtils.createKey("pile_hay");
/* 17 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_MELON = FeatureUtils.createKey("pile_melon");
/* 18 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_SNOW = FeatureUtils.createKey("pile_snow");
/* 19 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_ICE = FeatureUtils.createKey("pile_ice");
/* 20 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_PUMPKIN = FeatureUtils.createKey("pile_pumpkin");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 23 */     FeatureUtils.register(context, PILE_HAY, Feature.BLOCK_PILE, new BlockPileConfiguration(new RotatedBlockProvider(Blocks.HAY_BLOCK)));
/*    */ 
/*    */     
/* 26 */     FeatureUtils.register(context, PILE_MELON, Feature.BLOCK_PILE, new BlockPileConfiguration(
/* 27 */           BlockStateProvider.simple(Blocks.MELON)));
/*    */     
/* 29 */     FeatureUtils.register(context, PILE_SNOW, Feature.BLOCK_PILE, new BlockPileConfiguration(
/* 30 */           BlockStateProvider.simple(Blocks.SNOW)));
/*    */     
/* 32 */     FeatureUtils.register(context, PILE_ICE, Feature.BLOCK_PILE, new BlockPileConfiguration(new WeightedStateProvider(
/*    */             
/* 34 */             WeightedList.builder()
/* 35 */             .add(Blocks.BLUE_ICE.defaultBlockState(), 1)
/* 36 */             .add(Blocks.PACKED_ICE.defaultBlockState(), 5))));
/*    */ 
/*    */     
/* 39 */     FeatureUtils.register(context, PILE_PUMPKIN, Feature.BLOCK_PILE, new BlockPileConfiguration(new WeightedStateProvider(
/* 40 */             WeightedList.builder()
/* 41 */             .add(Blocks.PUMPKIN.defaultBlockState(), 19)
/* 42 */             .add(Blocks.JACK_O_LANTERN.defaultBlockState(), 1))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\features\PileFeatures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */