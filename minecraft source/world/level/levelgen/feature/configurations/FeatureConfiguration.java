/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public interface FeatureConfiguration
/*    */ {
/*  8 */   public static final NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;
/*    */ 
/*    */   
/* 11 */   default Stream<ConfiguredFeature<?, ?>> getFeatures() { return Stream.empty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\FeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */