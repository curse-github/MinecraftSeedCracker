/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class NbtProviders {
/* 11 */   private static final Codec<NbtProvider> TYPED_CODEC = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.byNameCodec().dispatch(NbtProvider::getType, LootNbtProviderType::codec);
/*    */   
/* 13 */   public static final Codec<NbtProvider> CODEC = Codec.lazyInitialized(() -> 
/* 14 */       Codec.either(ContextNbtProvider.INLINE_CODEC, TYPED_CODEC).xmap(Either::unwrap, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final LootNbtProviderType STORAGE = register("storage", StorageNbtProvider.CODEC);
/* 21 */   public static final LootNbtProviderType CONTEXT = register("context", ContextNbtProvider.MAP_CODEC);
/*    */ 
/*    */   
/* 24 */   private static LootNbtProviderType register(String name, MapCodec<? extends NbtProvider> codec) { return (LootNbtProviderType)Registry.register(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, Identifier.withDefaultNamespace(name), new LootNbtProviderType(codec)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\nbt\NbtProviders.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */