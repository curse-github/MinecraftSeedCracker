/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class UnderwaterMagmaConfiguration implements FeatureConfiguration {
/*  7 */   public static final Codec<UnderwaterMagmaConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  8 */         Codec.intRange(0, 512).fieldOf("floor_search_range").forGetter(()), 
/*  9 */         Codec.intRange(0, 64).fieldOf("placement_radius_around_floor").forGetter(()), 
/* 10 */         Codec.floatRange(0.0F, 1.0F).fieldOf("placement_probability_per_valid_position").forGetter(()))
/* 11 */       .apply(i, UnderwaterMagmaConfiguration::new));
/*    */   
/*    */   public final int floorSearchRange;
/*    */   public final int placementRadiusAroundFloor;
/*    */   public final float placementProbabilityPerValidPosition;
/*    */   
/*    */   public UnderwaterMagmaConfiguration(int floorSearchRange, int placementRadiusAroundFloor, float placementProbabilityPerValidPosition) {
/* 18 */     this.floorSearchRange = floorSearchRange;
/* 19 */     this.placementRadiusAroundFloor = placementRadiusAroundFloor;
/* 20 */     this.placementProbabilityPerValidPosition = placementProbabilityPerValidPosition;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\UnderwaterMagmaConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */