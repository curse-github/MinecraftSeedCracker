/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CanyonShapeConfiguration
/*    */ {
/* 14 */   public static final Codec<CanyonShapeConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(FloatProvider.CODEC
/* 15 */         .fieldOf("distance_factor").forGetter(()), FloatProvider.CODEC
/* 16 */         .fieldOf("thickness").forGetter(()), ExtraCodecs.POSITIVE_INT
/* 17 */         .fieldOf("width_smoothness").forGetter(()), FloatProvider.CODEC
/* 18 */         .fieldOf("horizontal_radius_factor").forGetter(()), Codec.FLOAT
/* 19 */         .fieldOf("vertical_radius_default_factor").forGetter(()), Codec.FLOAT
/* 20 */         .fieldOf("vertical_radius_center_factor").forGetter(()))
/* 21 */       .apply(i, CanyonShapeConfiguration::new));
/*    */   
/*    */   public final FloatProvider distanceFactor;
/*    */   public final FloatProvider thickness;
/*    */   public final int widthSmoothness;
/*    */   public final FloatProvider horizontalRadiusFactor;
/*    */   public final float verticalRadiusDefaultFactor;
/*    */   public final float verticalRadiusCenterFactor;
/*    */   
/*    */   public CanyonShapeConfiguration(FloatProvider distanceFactor, FloatProvider thickness, int widthSmoothness, FloatProvider horizontalRadiusFactor, float verticalRadiusDefaultFactor, float verticalRadiusCenterFactor) {
/* 31 */     this.widthSmoothness = widthSmoothness;
/* 32 */     this.horizontalRadiusFactor = horizontalRadiusFactor;
/* 33 */     this.verticalRadiusDefaultFactor = verticalRadiusDefaultFactor;
/* 34 */     this.verticalRadiusCenterFactor = verticalRadiusCenterFactor;
/* 35 */     this.distanceFactor = distanceFactor;
/* 36 */     this.thickness = thickness;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CanyonCarverConfiguration$CanyonShapeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */