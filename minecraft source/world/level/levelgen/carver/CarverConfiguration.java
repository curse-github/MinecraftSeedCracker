/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CarverConfiguration extends ProbabilityFeatureConfiguration {
/* 16 */   public static final MapCodec<CarverConfiguration> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 17 */         Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), HeightProvider.CODEC
/* 18 */         .fieldOf("y").forGetter(()), FloatProvider.CODEC
/* 19 */         .fieldOf("yScale").forGetter(()), VerticalAnchor.CODEC
/* 20 */         .fieldOf("lava_level").forGetter(()), CarverDebugSettings.CODEC
/* 21 */         .optionalFieldOf("debug_settings", CarverDebugSettings.DEFAULT).forGetter(()), 
/* 22 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(()))
/* 23 */       .apply(i, CarverConfiguration::new));
/*    */   
/*    */   public final HeightProvider y;
/*    */   
/*    */   public final FloatProvider yScale;
/*    */   public final VerticalAnchor lavaLevel;
/*    */   public final CarverDebugSettings debugSettings;
/*    */   public final HolderSet<Block> replaceable;
/*    */   
/*    */   public CarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable) {
/* 33 */     super(probability);
/* 34 */     this.y = y;
/* 35 */     this.yScale = yScale;
/* 36 */     this.lavaLevel = lavaLevel;
/* 37 */     this.debugSettings = debugSettings;
/* 38 */     this.replaceable = replaceable;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */