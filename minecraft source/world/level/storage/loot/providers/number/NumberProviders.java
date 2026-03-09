/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class NumberProviders {
/* 11 */   private static final Codec<NumberProvider> TYPED_CODEC = BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.byNameCodec().dispatch(NumberProvider::getType, LootNumberProviderType::codec);
/*    */   
/* 13 */   public static final Codec<NumberProvider> CODEC = Codec.lazyInitialized(() -> {
/*    */         
/* 15 */         typedCodecWithFallback = Codec.withAlternative(TYPED_CODEC, UniformGenerator.CODEC.codec());
/* 16 */         return Codec.either(ConstantValue.INLINE_CODEC, typedCodecWithFallback).xmap(Either::unwrap, ());
/*    */       });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final LootNumberProviderType CONSTANT = register("constant", ConstantValue.CODEC);
/* 23 */   public static final LootNumberProviderType UNIFORM = register("uniform", UniformGenerator.CODEC);
/* 24 */   public static final LootNumberProviderType BINOMIAL = register("binomial", BinomialDistributionGenerator.CODEC);
/* 25 */   public static final LootNumberProviderType SCORE = register("score", ScoreboardValue.CODEC);
/* 26 */   public static final LootNumberProviderType STORAGE = register("storage", StorageValue.CODEC);
/* 27 */   public static final LootNumberProviderType ENCHANTMENT_LEVEL = register("enchantment_level", EnchantmentLevelProvider.CODEC);
/*    */ 
/*    */   
/* 30 */   private static LootNumberProviderType register(String name, MapCodec<? extends NumberProvider> codec) { return (LootNumberProviderType)Registry.register(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, Identifier.withDefaultNamespace(name), new LootNumberProviderType(codec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */