/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ 
/*    */ public abstract class FeatureSize
/*    */ {
/* 12 */   public static final Codec<FeatureSize> CODEC = BuiltInRegistries.FEATURE_SIZE_TYPE.byNameCodec().dispatch(FeatureSize::type, FeatureSizeType::codec);
/*    */   protected static final int MAX_WIDTH = 16;
/*    */   protected final OptionalInt minClippedHeight;
/*    */   
/* 16 */   protected static <S extends FeatureSize> RecordCodecBuilder<S, OptionalInt> minClippedHeightCodec() { return Codec.intRange(0, 80).optionalFieldOf("min_clipped_height")
/* 17 */       .xmap(o -> (OptionalInt)o.map(OptionalInt::of).orElse(OptionalInt.empty()), o -> o.isPresent() ? Optional.of(Integer.valueOf(o.getAsInt())) : Optional.empty()).forGetter(f -> f.minClippedHeight); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public FeatureSize(OptionalInt minClippedHeight) { this.minClippedHeight = minClippedHeight; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public OptionalInt minClippedHeight() { return this.minClippedHeight; }
/*    */   
/*    */   protected abstract FeatureSizeType<?> type();
/*    */   
/*    */   public abstract int getSizeAtHeight(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */