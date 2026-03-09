/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class GeodeBlockSettings {
/*    */   public final BlockStateProvider fillingProvider;
/*    */   public final BlockStateProvider innerLayerProvider;
/*    */   public final BlockStateProvider alternateInnerLayerProvider;
/*    */   public final BlockStateProvider middleLayerProvider;
/*    */   public final BlockStateProvider outerLayerProvider;
/*    */   public final List<BlockState> innerPlacements;
/*    */   public final TagKey<Block> cannotReplace;
/*    */   public final TagKey<Block> invalidBlocks;
/* 24 */   public static final Codec<GeodeBlockSettings> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/* 25 */         .fieldOf("filling_provider").forGetter(()), BlockStateProvider.CODEC
/* 26 */         .fieldOf("inner_layer_provider").forGetter(()), BlockStateProvider.CODEC
/* 27 */         .fieldOf("alternate_inner_layer_provider").forGetter(()), BlockStateProvider.CODEC
/* 28 */         .fieldOf("middle_layer_provider").forGetter(()), BlockStateProvider.CODEC
/* 29 */         .fieldOf("outer_layer_provider").forGetter(()), 
/* 30 */         ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(()), 
/* 31 */         TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter(()), 
/* 32 */         TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(()))
/* 33 */       .apply(i, GeodeBlockSettings::new));
/*    */ 
/*    */   
/*    */   public GeodeBlockSettings(BlockStateProvider fillingProvider, BlockStateProvider innerLayerProvider, BlockStateProvider alternateInnerLayerProvider, BlockStateProvider middleLayerProvider, BlockStateProvider outerLayerProvider, List<BlockState> innerPlacements, TagKey<Block> cannotReplace, TagKey<Block> invalidBlocks) {
/* 37 */     this.fillingProvider = fillingProvider;
/* 38 */     this.innerLayerProvider = innerLayerProvider;
/* 39 */     this.alternateInnerLayerProvider = alternateInnerLayerProvider;
/* 40 */     this.middleLayerProvider = middleLayerProvider;
/* 41 */     this.outerLayerProvider = outerLayerProvider;
/* 42 */     this.innerPlacements = innerPlacements;
/* 43 */     this.cannotReplace = cannotReplace;
/* 44 */     this.invalidBlocks = invalidBlocks;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\GeodeBlockSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */