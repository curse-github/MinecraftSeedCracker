/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface FloatProviderType<P extends FloatProvider> {
/*  8 */   public static final FloatProviderType<ConstantFloat> CONSTANT = register("constant", ConstantFloat.CODEC);
/*  9 */   public static final FloatProviderType<UniformFloat> UNIFORM = register("uniform", UniformFloat.CODEC);
/* 10 */   public static final FloatProviderType<ClampedNormalFloat> CLAMPED_NORMAL = register("clamped_normal", ClampedNormalFloat.CODEC);
/* 11 */   public static final FloatProviderType<TrapezoidFloat> TRAPEZOID = register("trapezoid", TrapezoidFloat.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */ 
/*    */   
/* 17 */   static <P extends FloatProvider> FloatProviderType<P> register(String id, MapCodec<P> codec) { return (FloatProviderType)Registry.register(BuiltInRegistries.FLOAT_PROVIDER_TYPE, id, () -> codec); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\FloatProviderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */