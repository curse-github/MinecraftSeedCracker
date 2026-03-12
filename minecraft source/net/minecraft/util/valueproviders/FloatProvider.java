/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public abstract class FloatProvider implements SampledFloat {
/*  9 */   private static final Codec<Either<Float, FloatProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(Codec.FLOAT, BuiltInRegistries.FLOAT_PROVIDER_TYPE
/*    */       
/* 11 */       .byNameCodec().dispatch(FloatProvider::getType, FloatProviderType::codec));
/*    */   
/* 13 */   public static final Codec<FloatProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> 
/* 14 */       (FloatProvider)either.map(ConstantFloat::of, ()), f -> 
/* 15 */       (f.getType() == FloatProviderType.CONSTANT) ? Either.left(Float.valueOf(((ConstantFloat)f).getValue())) : Either.right(f));
/*    */ 
/*    */   
/*    */   public static Codec<FloatProvider> codec(float minValue, float maxValue) {
/* 19 */     return CODEC.validate(value -> {
/* 20 */           if (value.getMinValue() < minValue) {
/* 21 */             return DataResult.error(());
/*    */           }
/* 23 */           if (value.getMaxValue() > maxValue) {
/* 24 */             return DataResult.error(());
/*    */           }
/* 26 */           return DataResult.success(value);
/*    */         });
/*    */   }
/*    */   
/*    */   public abstract float getMinValue();
/*    */   
/*    */   public abstract float getMaxValue();
/*    */   
/*    */   public abstract FloatProviderType<?> getType();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\FloatProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */