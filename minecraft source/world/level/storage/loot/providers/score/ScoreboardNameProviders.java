/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ScoreboardNameProviders {
/* 11 */   private static final Codec<ScoreboardNameProvider> TYPED_CODEC = BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.byNameCodec().dispatch(ScoreboardNameProvider::getType, LootScoreProviderType::codec);
/*    */   
/* 13 */   public static final Codec<ScoreboardNameProvider> CODEC = Codec.lazyInitialized(() -> 
/* 14 */       Codec.either(ContextScoreboardNameProvider.INLINE_CODEC, TYPED_CODEC).xmap(Either::unwrap, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final LootScoreProviderType FIXED = register("fixed", FixedScoreboardNameProvider.CODEC);
/* 21 */   public static final LootScoreProviderType CONTEXT = register("context", ContextScoreboardNameProvider.CODEC);
/*    */ 
/*    */   
/* 24 */   private static LootScoreProviderType register(String name, MapCodec<? extends ScoreboardNameProvider> codec) { return (LootScoreProviderType)Registry.register(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE, Identifier.withDefaultNamespace(name), new LootScoreProviderType(codec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\score\ScoreboardNameProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */