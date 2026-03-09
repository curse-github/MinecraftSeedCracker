/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BiasedToBottomInt extends IntProvider {
/* 10 */   public static final MapCodec<BiasedToBottomInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 11 */         .fieldOf("min_inclusive").forGetter(()), Codec.INT
/* 12 */         .fieldOf("max_inclusive").forGetter(()))
/* 13 */       .apply(i, BiasedToBottomInt::new)).validate(u -> {
/* 14 */         if (u.maxInclusive < u.minInclusive) {
/* 15 */           return DataResult.error(());
/*    */         }
/* 17 */         return DataResult.success(u);
/*    */       });
/*    */   
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   private BiasedToBottomInt(int minInclusive, int maxInclusive) {
/* 24 */     this.minInclusive = minInclusive;
/* 25 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static BiasedToBottomInt of(int minInclusive, int maxInclusive) { return new BiasedToBottomInt(minInclusive, maxInclusive); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public int sample(RandomSource random) { return this.minInclusive + random.nextInt(random.nextInt(this.maxInclusive - this.minInclusive + 1) + 1); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public int getMinValue() { return this.minInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getMaxValue() { return this.maxInclusive; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public IntProviderType<?> getType() { return IntProviderType.BIASED_TO_BOTTOM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public String toString() { return "[" + this.minInclusive + "-" + this.maxInclusive + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\BiasedToBottomInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */