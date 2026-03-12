/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ public interface EnchantmentValueEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentValueEffect> bootstrap(Registry<MapCodec<? extends EnchantmentValueEffect>> registry) {
/* 14 */     Registry.register(registry, "add", AddValue.CODEC);
/* 15 */     Registry.register(registry, "all_of", AllOf.ValueEffects.CODEC);
/* 16 */     Registry.register(registry, "multiply", MultiplyValue.CODEC);
/* 17 */     Registry.register(registry, "remove_binomial", RemoveBinomial.CODEC);
/* 18 */     Registry.register(registry, "exponential", ScaleExponentially.CODEC);
/* 19 */     return (MapCodec)Registry.register(registry, "set", SetValue.CODEC);
/*    */   }
/* 21 */   public static final Codec<EnchantmentValueEffect> CODEC = BuiltInRegistries.ENCHANTMENT_VALUE_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentValueEffect::codec, Function.identity());
/*    */   
/*    */   float process(int paramInt, RandomSource paramRandomSource, float paramFloat);
/*    */   
/*    */   MapCodec<? extends EnchantmentValueEffect> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentValueEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */