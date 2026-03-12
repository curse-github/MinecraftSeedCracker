/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CaveCarverConfiguration extends CarverConfiguration {
/* 12 */   public static final Codec<CaveCarverConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(CarverConfiguration.CODEC
/* 13 */         .forGetter(()), FloatProvider.CODEC
/* 14 */         .fieldOf("horizontal_radius_multiplier").forGetter(()), FloatProvider.CODEC
/* 15 */         .fieldOf("vertical_radius_multiplier").forGetter(()), 
/* 16 */         FloatProvider.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(()))
/* 17 */       .apply(i, CaveCarverConfiguration::new));
/*    */   
/*    */   public final FloatProvider horizontalRadiusMultiplier;
/*    */   
/*    */   public final FloatProvider verticalRadiusMultiplier;
/*    */   
/*    */   final FloatProvider floorLevel;
/*    */   
/*    */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 26 */     super(probability, y, yScale, lavaLevel, debugSettings, replaceable);
/* 27 */     this.horizontalRadiusMultiplier = horizontalRadiusMultiplier;
/* 28 */     this.verticalRadiusMultiplier = verticalRadiusMultiplier;
/* 29 */     this.floorLevel = floorLevel;
/*    */   }
/*    */ 
/*    */   
/* 33 */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) { this(probability, y, yScale, lavaLevel, CarverDebugSettings.DEFAULT, replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public CaveCarverConfiguration(CarverConfiguration carver, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) { this(carver.probability, carver.y, carver.yScale, carver.lavaLevel, carver.debugSettings, carver.replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CaveCarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */