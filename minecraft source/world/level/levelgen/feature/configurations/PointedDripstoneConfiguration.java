/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class PointedDripstoneConfiguration implements FeatureConfiguration {
/*  7 */   public static final Codec<PointedDripstoneConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  8 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_dripstone").orElse(Float.valueOf(0.2F)).forGetter(()), 
/*  9 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(Float.valueOf(0.7F)).forGetter(()), 
/* 10 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(Float.valueOf(0.5F)).forGetter(()), 
/* 11 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(Float.valueOf(0.5F)).forGetter(()))
/*    */       
/* 13 */       .apply(i, PointedDripstoneConfiguration::new));
/*    */   
/*    */   public final float chanceOfTallerDripstone;
/*    */   
/*    */   public final float chanceOfDirectionalSpread;
/*    */   
/*    */   public final float chanceOfSpreadRadius2;
/*    */   
/*    */   public final float chanceOfSpreadRadius3;
/*    */   
/*    */   public PointedDripstoneConfiguration(float chanceOfTallerDripstone, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
/* 24 */     this.chanceOfTallerDripstone = chanceOfTallerDripstone;
/* 25 */     this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
/* 26 */     this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
/* 27 */     this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\PointedDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */