/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountConfiguration implements FeatureConfiguration {
/*  8 */   public static final Codec<CountConfiguration> CODEC = IntProvider.codec(0, 256).fieldOf("count")
/*  9 */     .xmap(CountConfiguration::new, CountConfiguration::count).codec();
/*    */   
/*    */   private final IntProvider count;
/*    */ 
/*    */   
/* 14 */   public CountConfiguration(int count) { this.count = ConstantInt.of(count); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public CountConfiguration(IntProvider count) { this.count = count; }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public IntProvider count() { return this.count; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\CountConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */