/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeLayerSettings {
/*  7 */   private static final Codec<Double> LAYER_RANGE = Codec.doubleRange(0.01D, 50.0D);
/*  8 */   public static final Codec<GeodeLayerSettings> CODEC = RecordCodecBuilder.create(i -> i.group(LAYER_RANGE
/*  9 */         .fieldOf("filling").orElse(Double.valueOf(1.7D)).forGetter(()), LAYER_RANGE
/* 10 */         .fieldOf("inner_layer").orElse(Double.valueOf(2.2D)).forGetter(()), LAYER_RANGE
/* 11 */         .fieldOf("middle_layer").orElse(Double.valueOf(3.2D)).forGetter(()), LAYER_RANGE
/* 12 */         .fieldOf("outer_layer").orElse(Double.valueOf(4.2D)).forGetter(()))
/* 13 */       .apply(i, GeodeLayerSettings::new));
/*    */   
/*    */   public final double filling;
/*    */   public final double innerLayer;
/*    */   public final double middleLayer;
/*    */   public final double outerLayer;
/*    */   
/*    */   public GeodeLayerSettings(double filling, double innerLayer, double middleLayer, double outerLayer) {
/* 21 */     this.filling = filling;
/* 22 */     this.innerLayer = innerLayer;
/* 23 */     this.middleLayer = middleLayer;
/* 24 */     this.outerLayer = outerLayer;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\GeodeLayerSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */