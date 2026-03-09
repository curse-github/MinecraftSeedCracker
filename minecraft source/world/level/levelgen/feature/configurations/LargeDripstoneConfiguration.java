/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function9;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class LargeDripstoneConfiguration implements FeatureConfiguration {
/*  9 */   public static final Codec<LargeDripstoneConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 10 */         Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(Integer.valueOf(30)).forGetter(()), 
/*    */         
/* 12 */         IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(()), 
/* 13 */         FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(()), 
/* 14 */         Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(()), 
/*    */         
/* 16 */         FloatProvider.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(()), 
/* 17 */         FloatProvider.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(()), 
/*    */         
/* 19 */         FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(()), 
/*    */         
/* 21 */         Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(()), 
/* 22 */         Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(()))
/* 23 */       .apply(i, LargeDripstoneConfiguration::new));
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */   
/*    */   public final IntProvider columnRadius;
/*    */   
/*    */   public final FloatProvider heightScale;
/*    */   
/*    */   public final float maxColumnRadiusToCaveHeightRatio;
/*    */   
/*    */   public final FloatProvider stalactiteBluntness;
/*    */   
/*    */   public final FloatProvider stalagmiteBluntness;
/*    */   
/*    */   public final FloatProvider windSpeed;
/*    */   
/*    */   public final int minRadiusForWind;
/*    */   
/*    */   public final float minBluntnessForWind;
/*    */   
/*    */   public LargeDripstoneConfiguration(int floorToCeilingSearchRange, IntProvider columnRadius, FloatProvider heightScale, float maxColumnRadiusToCaveHeightRatio, FloatProvider stalactiteBluntness, FloatProvider stalagmiteBluntness, FloatProvider windSpeed, int minRadiusForWind, float minBluntnessForWind) {
/* 44 */     this.floorToCeilingSearchRange = floorToCeilingSearchRange;
/* 45 */     this.columnRadius = columnRadius;
/* 46 */     this.heightScale = heightScale;
/* 47 */     this.maxColumnRadiusToCaveHeightRatio = maxColumnRadiusToCaveHeightRatio;
/* 48 */     this.stalactiteBluntness = stalactiteBluntness;
/* 49 */     this.stalagmiteBluntness = stalagmiteBluntness;
/* 50 */     this.windSpeed = windSpeed;
/* 51 */     this.minRadiusForWind = minRadiusForWind;
/* 52 */     this.minBluntnessForWind = minBluntnessForWind;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\LargeDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */