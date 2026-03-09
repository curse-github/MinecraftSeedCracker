/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public final class VillagerType {
/* 21 */   public static final ResourceKey<VillagerType> DESERT = createKey("desert");
/* 22 */   public static final ResourceKey<VillagerType> JUNGLE = createKey("jungle");
/* 23 */   public static final ResourceKey<VillagerType> PLAINS = createKey("plains");
/* 24 */   public static final ResourceKey<VillagerType> SAVANNA = createKey("savanna");
/* 25 */   public static final ResourceKey<VillagerType> SNOW = createKey("snow");
/* 26 */   public static final ResourceKey<VillagerType> SWAMP = createKey("swamp");
/* 27 */   public static final ResourceKey<VillagerType> TAIGA = createKey("taiga");
/*    */ 
/*    */   
/* 30 */   private static ResourceKey<VillagerType> createKey(String name) { return ResourceKey.create(Registries.VILLAGER_TYPE, Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */   
/* 33 */   public static final Codec<Holder<VillagerType>> CODEC = RegistryFixedCodec.create(Registries.VILLAGER_TYPE);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<VillagerType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.VILLAGER_TYPE);
/*    */ 
/*    */   
/* 37 */   private static VillagerType register(Registry<VillagerType> registry, ResourceKey<VillagerType> name) { return (VillagerType)Registry.register(registry, name, new VillagerType()); }
/*    */ 
/*    */   
/*    */   public static VillagerType bootstrap(Registry<VillagerType> registry) {
/* 41 */     register(registry, DESERT);
/* 42 */     register(registry, JUNGLE);
/* 43 */     register(registry, PLAINS);
/* 44 */     register(registry, SAVANNA);
/* 45 */     register(registry, SNOW);
/* 46 */     register(registry, SWAMP);
/* 47 */     return register(registry, TAIGA);
/*    */   }
/*    */   
/* 50 */   private static final Map<ResourceKey<Biome>, ResourceKey<VillagerType>> BY_BIOME = (Map)Util.make(Maps.newHashMap(), map -> {
/*    */ 
/*    */         
/* 53 */         map.put(Biomes.BADLANDS, DESERT);
/* 54 */         map.put(Biomes.DESERT, DESERT);
/* 55 */         map.put(Biomes.ERODED_BADLANDS, DESERT);
/* 56 */         map.put(Biomes.WOODED_BADLANDS, DESERT);
/*    */         
/* 58 */         map.put(Biomes.BAMBOO_JUNGLE, JUNGLE);
/* 59 */         map.put(Biomes.JUNGLE, JUNGLE);
/* 60 */         map.put(Biomes.SPARSE_JUNGLE, JUNGLE);
/*    */         
/* 62 */         map.put(Biomes.SAVANNA_PLATEAU, SAVANNA);
/* 63 */         map.put(Biomes.SAVANNA, SAVANNA);
/* 64 */         map.put(Biomes.WINDSWEPT_SAVANNA, SAVANNA);
/*    */         
/* 66 */         map.put(Biomes.DEEP_FROZEN_OCEAN, SNOW);
/* 67 */         map.put(Biomes.FROZEN_OCEAN, SNOW);
/* 68 */         map.put(Biomes.FROZEN_RIVER, SNOW);
/* 69 */         map.put(Biomes.ICE_SPIKES, SNOW);
/* 70 */         map.put(Biomes.SNOWY_BEACH, SNOW);
/* 71 */         map.put(Biomes.SNOWY_TAIGA, SNOW);
/* 72 */         map.put(Biomes.SNOWY_PLAINS, SNOW);
/* 73 */         map.put(Biomes.GROVE, SNOW);
/* 74 */         map.put(Biomes.SNOWY_SLOPES, SNOW);
/* 75 */         map.put(Biomes.FROZEN_PEAKS, SNOW);
/* 76 */         map.put(Biomes.JAGGED_PEAKS, SNOW);
/*    */         
/* 78 */         map.put(Biomes.SWAMP, SWAMP);
/* 79 */         map.put(Biomes.MANGROVE_SWAMP, SWAMP);
/*    */         
/* 81 */         map.put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, TAIGA);
/* 82 */         map.put(Biomes.OLD_GROWTH_PINE_TAIGA, TAIGA);
/* 83 */         map.put(Biomes.WINDSWEPT_GRAVELLY_HILLS, TAIGA);
/* 84 */         map.put(Biomes.WINDSWEPT_HILLS, TAIGA);
/* 85 */         map.put(Biomes.TAIGA, TAIGA);
/* 86 */         map.put(Biomes.WINDSWEPT_FOREST, TAIGA);
/*    */       });
/*    */ 
/*    */   
/* 90 */   public static ResourceKey<VillagerType> byBiome(Holder<Biome> biome) { Objects.requireNonNull(BY_BIOME); return (ResourceKey)biome.unwrapKey().map(BY_BIOME::get).orElse(PLAINS); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\villager\VillagerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */