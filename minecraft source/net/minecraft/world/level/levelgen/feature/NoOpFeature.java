/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class NoOpFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/*  8 */   public NoOpFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\NoOpFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */