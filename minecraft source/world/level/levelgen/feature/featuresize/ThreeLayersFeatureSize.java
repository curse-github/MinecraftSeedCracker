/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class ThreeLayersFeatureSize extends FeatureSize {
/* 11 */   public static final MapCodec<ThreeLayersFeatureSize> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 12 */         Codec.intRange(0, 80).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), 
/* 13 */         Codec.intRange(0, 80).fieldOf("upper_limit").orElse(Integer.valueOf(1)).forGetter(()), 
/* 14 */         Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), 
/* 15 */         Codec.intRange(0, 16).fieldOf("middle_size").orElse(Integer.valueOf(1)).forGetter(()), 
/* 16 */         Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), 
/* 17 */         minClippedHeightCodec())
/* 18 */       .apply(i, ThreeLayersFeatureSize::new));
/*    */   
/*    */   private final int limit;
/*    */   private final int upperLimit;
/*    */   private final int lowerSize;
/*    */   private final int middleSize;
/*    */   private final int upperSize;
/*    */   
/*    */   public ThreeLayersFeatureSize(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, OptionalInt minClippedHeight) {
/* 27 */     super(minClippedHeight);
/* 28 */     this.limit = limit;
/* 29 */     this.upperLimit = upperLimit;
/* 30 */     this.lowerSize = lowerSize;
/* 31 */     this.middleSize = middleSize;
/* 32 */     this.upperSize = upperSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected FeatureSizeType<?> type() { return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int treeHeight, int yo) {
/* 42 */     if (yo < this.limit) {
/* 43 */       return this.lowerSize;
/*    */     }
/* 45 */     if (yo >= treeHeight - this.upperLimit) {
/* 46 */       return this.upperSize;
/*    */     }
/* 48 */     return this.middleSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\ThreeLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */