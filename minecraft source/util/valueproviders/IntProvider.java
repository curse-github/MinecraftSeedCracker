/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class IntProvider {
/* 10 */   private static final Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(Codec.INT, BuiltInRegistries.INT_PROVIDER_TYPE
/*    */       
/* 12 */       .byNameCodec().dispatch(IntProvider::getType, IntProviderType::codec));
/*    */   
/* 14 */   public static final Codec<IntProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> 
/* 15 */       (IntProvider)either.map(ConstantInt::of, ()), f -> 
/* 16 */       (f.getType() == IntProviderType.CONSTANT) ? Either.left(Integer.valueOf(((ConstantInt)f).getValue())) : Either.right(f));
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static Codec<IntProvider> codec(int minValue, int maxValue) { return validateCodec(minValue, maxValue, CODEC); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static <T extends IntProvider> Codec<T> validateCodec(int minValue, int maxValue, Codec<T> codec) { return codec.validate(value -> validate(minValue, maxValue, value)); }
/*    */ 
/*    */   
/*    */   private static <T extends IntProvider> DataResult<T> validate(int minValue, int maxValue, T value) {
/* 28 */     if (value.getMinValue() < minValue) {
/* 29 */       return DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
/*    */     }
/* 31 */     if (value.getMaxValue() > maxValue) {
/* 32 */       return DataResult.error(() -> "Value provider too high: " + maxValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
/*    */     }
/* 34 */     return DataResult.success(value);
/*    */   }
/*    */   
/* 37 */   public static final Codec<IntProvider> NON_NEGATIVE_CODEC = codec(0, 2147483647);
/* 38 */   public static final Codec<IntProvider> POSITIVE_CODEC = codec(1, 2147483647);
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource);
/*    */   
/*    */   public abstract int getMinValue();
/*    */   
/*    */   public abstract int getMaxValue();
/*    */   
/*    */   public abstract IntProviderType<?> getType();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\IntProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */