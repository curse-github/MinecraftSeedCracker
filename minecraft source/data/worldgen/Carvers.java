/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.valueproviders.ConstantFloat;
/*    */ import net.minecraft.util.valueproviders.TrapezoidFloat;
/*    */ import net.minecraft.util.valueproviders.UniformFloat;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
/*    */ import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class Carvers {
/* 22 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE = createKey("cave");
/* 23 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE_EXTRA_UNDERGROUND = createKey("cave_extra_underground");
/* 24 */   public static final ResourceKey<ConfiguredWorldCarver<?>> CANYON = createKey("canyon");
/* 25 */   public static final ResourceKey<ConfiguredWorldCarver<?>> NETHER_CAVE = createKey("nether_cave");
/*    */ 
/*    */   
/* 28 */   private static ResourceKey<ConfiguredWorldCarver<?>> createKey(String name) { return ResourceKey.create(Registries.CONFIGURED_CARVER, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
/* 32 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/*    */     
/* 34 */     context.register(CAVE, WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.15F, 
/*    */             
/* 36 */             UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)), 
/* 37 */             UniformFloat.of(0.1F, 0.9F), 
/* 38 */             VerticalAnchor.aboveBottom(8), 
/* 39 */             CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()), blocks
/* 40 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 41 */             UniformFloat.of(0.7F, 1.4F), 
/* 42 */             UniformFloat.of(0.8F, 1.3F), 
/* 43 */             UniformFloat.of(-1.0F, -0.4F))));
/*    */ 
/*    */     
/* 46 */     context.register(CAVE_EXTRA_UNDERGROUND, WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.07F, 
/*    */             
/* 48 */             UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(47)), 
/* 49 */             UniformFloat.of(0.1F, 0.9F), 
/* 50 */             VerticalAnchor.aboveBottom(8), 
/* 51 */             CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()), blocks
/* 52 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 53 */             UniformFloat.of(0.7F, 1.4F), 
/* 54 */             UniformFloat.of(0.8F, 1.3F), 
/* 55 */             UniformFloat.of(-1.0F, -0.4F))));
/*    */ 
/*    */     
/* 58 */     context.register(CANYON, WorldCarver.CANYON.configured(new CanyonCarverConfiguration(0.01F, 
/*    */             
/* 60 */             UniformHeight.of(VerticalAnchor.absolute(10), VerticalAnchor.absolute(67)), 
/* 61 */             ConstantFloat.of(3.0F), 
/* 62 */             VerticalAnchor.aboveBottom(8), 
/* 63 */             CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), blocks
/* 64 */             .getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES), 
/* 65 */             UniformFloat.of(-0.125F, 0.125F), new CanyonCarverConfiguration.CanyonShapeConfiguration(
/*    */               
/* 67 */               UniformFloat.of(0.75F, 1.0F), 
/* 68 */               TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, 
/*    */               
/* 70 */               UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     context.register(NETHER_CAVE, WorldCarver.NETHER_CAVE.configured(new CaveCarverConfiguration(0.2F, 
/*    */             
/* 78 */             UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.belowTop(1)), 
/* 79 */             ConstantFloat.of(0.5F), 
/* 80 */             VerticalAnchor.aboveBottom(10), blocks
/* 81 */             .getOrThrow(BlockTags.NETHER_CARVER_REPLACEABLES), 
/* 82 */             ConstantFloat.of(1.0F), 
/* 83 */             ConstantFloat.of(1.0F), 
/* 84 */             ConstantFloat.of(-0.7F))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\Carvers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */