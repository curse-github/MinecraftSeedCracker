/*    */ package net.minecraft.core;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ public class RegistrySynchronization {
/* 23 */   private static final Set<ResourceKey<? extends Registry<?>>> NETWORKABLE_REGISTRIES = (Set)RegistryDataLoader.SYNCHRONIZED_REGISTRIES.stream().map(RegistryDataLoader.RegistryData::key).collect(Collectors.toUnmodifiableSet());
/*    */ 
/*    */   
/* 26 */   public static void packRegistries(DynamicOps<Tag> ops, RegistryAccess registries, Set<KnownPack> clientKnownPacks, BiConsumer<ResourceKey<? extends Registry<?>>, List<PackedRegistryEntry>> output) { RegistryDataLoader.SYNCHRONIZED_REGISTRIES.forEach(registryEntry -> packRegistry(ops, registryEntry, registries, clientKnownPacks, output)); }
/*    */ 
/*    */   
/*    */   private static <T> void packRegistry(DynamicOps<Tag> ops, RegistryDataLoader.RegistryData<T> registryData, RegistryAccess registries, Set<KnownPack> clientKnownPacks, BiConsumer<ResourceKey<? extends Registry<?>>, List<PackedRegistryEntry>> output) {
/* 30 */     registries.lookup(registryData.key()).ifPresent(registry -> {
/* 31 */           List<PackedRegistryEntry> packedElements = new ArrayList<PackedRegistryEntry>(registry.size());
/*    */           
/* 33 */           registry.listElements().forEach(());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 44 */           output.accept(registry.key(), packedElements);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 49 */   private static Stream<RegistryAccess.RegistryEntry<?>> ownedNetworkableRegistries(RegistryAccess access) { return access.registries().filter(e -> isNetworkable(e.key())); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static Stream<RegistryAccess.RegistryEntry<?>> networkedRegistries(LayeredRegistryAccess<RegistryLayer> registries) { return ownedNetworkableRegistries(registries.getAccessFrom(RegistryLayer.WORLDGEN)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Stream<RegistryAccess.RegistryEntry<?>> networkSafeRegistries(LayeredRegistryAccess<RegistryLayer> registries) {
/* 61 */     Stream<RegistryAccess.RegistryEntry<?>> staticRegistries = registries.getLayer(RegistryLayer.STATIC).registries();
/* 62 */     Stream<RegistryAccess.RegistryEntry<?>> networkedRegistries = networkedRegistries(registries);
/* 63 */     return Stream.concat(networkedRegistries, staticRegistries);
/*    */   }
/*    */   public static final class PackedRegistryEntry extends Record { private final Identifier id; private final Optional<Tag> data;
/* 66 */     public PackedRegistryEntry(Identifier id, Optional<Tag> data) { this.id = id; this.data = data; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 66 */       //   0	7	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/RegistrySynchronization$PackedRegistryEntry;
/* 66 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Tag> data() { return this.data; }
/* 67 */     public static final StreamCodec<ByteBuf, PackedRegistryEntry> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, PackedRegistryEntry::id, ByteBufCodecs.TAG
/*    */         
/* 69 */         .apply(ByteBufCodecs::optional), PackedRegistryEntry::data, PackedRegistryEntry::new); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public static boolean isNetworkable(ResourceKey<? extends Registry<?>> key) { return NETWORKABLE_REGISTRIES.contains(key); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySynchronization.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */