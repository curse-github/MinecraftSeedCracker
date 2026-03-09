/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class TwoLayersFeatureSize
/*    */   extends FeatureSize {
/* 12 */   public static final MapCodec<TwoLayersFeatureSize> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 13 */         Codec.intRange(0, 81).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), 
/* 14 */         Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), 
/* 15 */         Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), 
/* 16 */         minClippedHeightCodec())
/* 17 */       .apply(i, TwoLayersFeatureSize::new));
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int upperSize;
/*    */   
/* 25 */   public TwoLayersFeatureSize(int limit, int lowerSize, int upperSize) { this(limit, lowerSize, upperSize, OptionalInt.empty()); }
/*    */ 
/*    */   
/*    */   public TwoLayersFeatureSize(int limit, int lowerSize, int upperSize, OptionalInt minClippedHeight) {
/* 29 */     super(minClippedHeight);
/* 30 */     this.limit = limit;
/* 31 */     this.lowerSize = lowerSize;
/* 32 */     this.upperSize = upperSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected FeatureSizeType<?> type() { return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getSizeAtHeight(int treeHeight, int yo) { return (yo < this.limit) ? this.lowerSize : this.upperSize; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\TwoLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */