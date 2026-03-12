/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedInt extends IntProvider {
/* 11 */   public static final MapCodec<ClampedInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(IntProvider.CODEC
/* 12 */         .fieldOf("source").forGetter(()), Codec.INT
/* 13 */         .fieldOf("min_inclusive").forGetter(()), Codec.INT
/* 14 */         .fieldOf("max_inclusive").forGetter(()))
/* 15 */       .apply(i, ClampedInt::new)).validate(u -> {
/* 16 */         if (u.maxInclusive < u.minInclusive) {
/* 17 */           return DataResult.error(());
/*    */         }
/* 19 */         return DataResult.success(u);
/*    */       });
/*    */   
/*    */   private final IntProvider source;
/*    */   
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/* 27 */   public static ClampedInt of(IntProvider source, int minInclusive, int maxInclusive) { return new ClampedInt(source, minInclusive, maxInclusive); }
/*    */ 
/*    */   
/*    */   public ClampedInt(IntProvider source, int minInclusive, int maxInclusive) {
/* 31 */     this.source = source;
/* 32 */     this.minInclusive = minInclusive;
/* 33 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int sample(RandomSource random) { return Mth.clamp(this.source.sample(random), this.minInclusive, this.maxInclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int getMinValue() { return Math.max(this.minInclusive, this.source.getMinValue()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getMaxValue() { return Math.min(this.maxInclusive, this.source.getMaxValue()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public IntProviderType<?> getType() { return IntProviderType.CLAMPED; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\ClampedInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */