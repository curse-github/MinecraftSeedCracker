/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class UniformInt extends IntProvider {
/* 11 */   public static final MapCodec<UniformInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 12 */         .fieldOf("min_inclusive").forGetter(()), Codec.INT
/* 13 */         .fieldOf("max_inclusive").forGetter(()))
/* 14 */       .apply(i, UniformInt::new)).validate(u -> {
/* 15 */         if (u.maxInclusive < u.minInclusive) {
/* 16 */           return DataResult.error(());
/*    */         }
/* 18 */         return DataResult.success(u);
/*    */       });
/*    */   
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   private UniformInt(int minInclusive, int maxInclusive) {
/* 25 */     this.minInclusive = minInclusive;
/* 26 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/* 30 */   public static UniformInt of(int minInclusive, int maxInclusive) { return new UniformInt(minInclusive, maxInclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public int sample(RandomSource random) { return Mth.randomBetweenInclusive(random, this.minInclusive, this.maxInclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getMinValue() { return this.minInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int getMaxValue() { return this.maxInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public IntProviderType<?> getType() { return IntProviderType.UNIFORM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String toString() { return "[" + this.minInclusive + "-" + this.maxInclusive + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\UniformInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */