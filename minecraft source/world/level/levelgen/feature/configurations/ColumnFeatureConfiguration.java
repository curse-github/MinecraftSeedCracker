/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class ColumnFeatureConfiguration implements FeatureConfiguration {
/*  8 */   public static final Codec<ColumnFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  9 */         IntProvider.codec(0, 3).fieldOf("reach").forGetter(()), 
/* 10 */         IntProvider.codec(1, 10).fieldOf("height").forGetter(()))
/* 11 */       .apply(i, ColumnFeatureConfiguration::new));
/*    */   
/*    */   private final IntProvider reach;
/*    */   private final IntProvider height;
/*    */   
/*    */   public ColumnFeatureConfiguration(IntProvider reach, IntProvider height) {
/* 17 */     this.reach = reach;
/* 18 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/* 22 */   public IntProvider reach() { return this.reach; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public IntProvider height() { return this.height; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ColumnFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */