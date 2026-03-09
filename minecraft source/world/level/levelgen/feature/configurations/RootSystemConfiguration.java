/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function13;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RootSystemConfiguration implements FeatureConfiguration {
/* 14 */   public static final Codec<RootSystemConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(PlacedFeature.CODEC
/* 15 */         .fieldOf("feature").forGetter(()), 
/* 16 */         Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(()), 
/* 17 */         Codec.intRange(1, 64).fieldOf("root_radius").forGetter(()), 
/* 18 */         TagKey.hashedCodec(Registries.BLOCK).fieldOf("root_replaceable").forGetter(()), BlockStateProvider.CODEC
/* 19 */         .fieldOf("root_state_provider").forGetter(()), 
/* 20 */         Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(()), 
/* 21 */         Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(()), 
/* 22 */         Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(()), 
/* 23 */         Codec.intRange(1, 16).fieldOf("hanging_roots_vertical_span").forGetter(()), BlockStateProvider.CODEC
/* 24 */         .fieldOf("hanging_root_state_provider").forGetter(()), 
/* 25 */         Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(()), 
/* 26 */         Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(()), BlockPredicate.CODEC
/* 27 */         .fieldOf("allowed_tree_position").forGetter(()))
/* 28 */       .apply(i, RootSystemConfiguration::new));
/*    */   
/*    */   public final Holder<PlacedFeature> treeFeature;
/*    */   public final int requiredVerticalSpaceForTree;
/*    */   public final int rootRadius;
/*    */   public final TagKey<Block> rootReplaceable;
/*    */   public final BlockStateProvider rootStateProvider;
/*    */   public final int rootPlacementAttempts;
/*    */   public final int rootColumnMaxHeight;
/*    */   public final int hangingRootRadius;
/*    */   public final int hangingRootsVerticalSpan;
/*    */   public final BlockStateProvider hangingRootStateProvider;
/*    */   public final int hangingRootPlacementAttempts;
/*    */   public final int allowedVerticalWaterForTree;
/*    */   public final BlockPredicate allowedTreePosition;
/*    */   
/*    */   public RootSystemConfiguration(Holder<PlacedFeature> treeFeature, int requiredVerticalSpaceForTree, int rootRadius, TagKey<Block> rootReplaceable, BlockStateProvider rootStateProvider, int rootPlacementAttempts, int rootColumnMaxHeight, int hangingRootRadius, int hangingRootsVerticalSpan, BlockStateProvider hangingRootStateProvider, int hangingRootPlacementAttempts, int allowedVerticalWaterForTree, BlockPredicate allowedTreePosition) {
/* 45 */     this.treeFeature = treeFeature;
/* 46 */     this.requiredVerticalSpaceForTree = requiredVerticalSpaceForTree;
/* 47 */     this.rootRadius = rootRadius;
/* 48 */     this.rootReplaceable = rootReplaceable;
/* 49 */     this.rootStateProvider = rootStateProvider;
/* 50 */     this.rootPlacementAttempts = rootPlacementAttempts;
/* 51 */     this.rootColumnMaxHeight = rootColumnMaxHeight;
/* 52 */     this.hangingRootRadius = hangingRootRadius;
/* 53 */     this.hangingRootsVerticalSpan = hangingRootsVerticalSpan;
/* 54 */     this.hangingRootStateProvider = hangingRootStateProvider;
/* 55 */     this.hangingRootPlacementAttempts = hangingRootPlacementAttempts;
/* 56 */     this.allowedVerticalWaterForTree = allowedVerticalWaterForTree;
/* 57 */     this.allowedTreePosition = allowedTreePosition;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RootSystemConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */