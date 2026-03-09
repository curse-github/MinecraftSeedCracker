/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class UniformFloat
/*    */   extends FloatProvider {
/* 14 */   public static final MapCodec<UniformFloat> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 15 */         .fieldOf("min_inclusive").forGetter(()), Codec.FLOAT
/* 16 */         .fieldOf("max_exclusive").forGetter(()))
/* 17 */       .apply(i, UniformFloat::new)).validate(u -> {
/* 18 */         if (u.maxExclusive <= u.minInclusive) {
/* 19 */           return DataResult.error(());
/*    */         }
/* 21 */         return DataResult.success(u);
/*    */       });
/*    */   
/*    */   private final float minInclusive;
/*    */   private final float maxExclusive;
/*    */   
/*    */   private UniformFloat(float minInclusive, float maxExclusive) {
/* 28 */     this.minInclusive = minInclusive;
/* 29 */     this.maxExclusive = maxExclusive;
/*    */   }
/*    */   
/*    */   public static UniformFloat of(float minInclusive, float maxExclusive) {
/* 33 */     if (maxExclusive <= minInclusive) {
/* 34 */       throw new IllegalArgumentException("Max must exceed min");
/*    */     }
/* 36 */     return new UniformFloat(minInclusive, maxExclusive);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public float sample(RandomSource random) { return Mth.randomBetween(random, this.minInclusive, this.maxExclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public float getMinValue() { return this.minInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public float getMaxValue() { return this.maxExclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public FloatProviderType<?> getType() { return FloatProviderType.UNIFORM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public String toString() { return "[" + this.minInclusive + "-" + this.maxExclusive + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\UniformFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */