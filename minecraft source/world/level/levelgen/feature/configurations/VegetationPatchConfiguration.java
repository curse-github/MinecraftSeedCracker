/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function10;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class VegetationPatchConfiguration implements FeatureConfiguration {
/* 16 */   public static final Codec<VegetationPatchConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 17 */         TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(()), BlockStateProvider.CODEC
/* 18 */         .fieldOf("ground_state").forGetter(()), PlacedFeature.CODEC
/* 19 */         .fieldOf("vegetation_feature").forGetter(()), CaveSurface.CODEC
/* 20 */         .fieldOf("surface").forGetter(()), 
/* 21 */         IntProvider.codec(1, 128).fieldOf("depth").forGetter(()), 
/* 22 */         Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(()), 
/* 23 */         Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(()), 
/* 24 */         Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(()), IntProvider.CODEC
/* 25 */         .fieldOf("xz_radius").forGetter(()), 
/* 26 */         Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(()))
/* 27 */       .apply(i, VegetationPatchConfiguration::new));
/*    */   
/*    */   public final TagKey<Block> replaceable;
/*    */   
/*    */   public final BlockStateProvider groundState;
/*    */   public final Holder<PlacedFeature> vegetationFeature;
/*    */   public final CaveSurface surface;
/*    */   public final IntProvider depth;
/*    */   public final float extraBottomBlockChance;
/*    */   public final int verticalRange;
/*    */   public final float vegetationChance;
/*    */   public final IntProvider xzRadius;
/*    */   public final float extraEdgeColumnChance;
/*    */   
/*    */   public VegetationPatchConfiguration(TagKey<Block> replaceable, BlockStateProvider groundState, Holder<PlacedFeature> vegetationFeature, CaveSurface surface, IntProvider depth, float extraBottomBlockChance, int verticalRange, float vegetationChance, IntProvider xzRadius, float extraEdgeColumnChance) {
/* 42 */     this.replaceable = replaceable;
/* 43 */     this.groundState = groundState;
/* 44 */     this.vegetationFeature = vegetationFeature;
/* 45 */     this.surface = surface;
/* 46 */     this.depth = depth;
/* 47 */     this.extraBottomBlockChance = extraBottomBlockChance;
/* 48 */     this.verticalRange = verticalRange;
/* 49 */     this.vegetationChance = vegetationChance;
/* 50 */     this.xzRadius = xzRadius;
/* 51 */     this.extraEdgeColumnChance = extraEdgeColumnChance;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\VegetationPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */