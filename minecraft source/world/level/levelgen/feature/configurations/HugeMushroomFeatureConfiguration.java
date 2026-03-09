/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
/*  8 */   public static final Codec<HugeMushroomFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockStateProvider.CODEC
/*  9 */         .fieldOf("cap_provider").forGetter(()), BlockStateProvider.CODEC
/* 10 */         .fieldOf("stem_provider").forGetter(()), Codec.INT
/* 11 */         .fieldOf("foliage_radius").orElse(Integer.valueOf(2)).forGetter(()))
/* 12 */       .apply(i, HugeMushroomFeatureConfiguration::new));
/*    */   
/*    */   public final BlockStateProvider capProvider;
/*    */   public final BlockStateProvider stemProvider;
/*    */   public final int foliageRadius;
/*    */   
/*    */   public HugeMushroomFeatureConfiguration(BlockStateProvider capProvider, BlockStateProvider stemProvider, int foliageRadius) {
/* 19 */     this.capProvider = capProvider;
/* 20 */     this.stemProvider = stemProvider;
/* 21 */     this.foliageRadius = foliageRadius;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\HugeMushroomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */