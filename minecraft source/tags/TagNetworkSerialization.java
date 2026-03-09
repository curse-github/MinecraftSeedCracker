/*    */ package net.minecraft.tags;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ 
/*    */ public class TagNetworkSerialization {
/*    */   public static Map<ResourceKey<? extends Registry<?>>, NetworkPayload> serializeTagsToNetwork(LayeredRegistryAccess<RegistryLayer> registries) {
/* 23 */     return (Map)RegistrySynchronization.networkSafeRegistries(registries)
/* 24 */       .map(e -> Pair.of(e.key(), serializeToNetwork(e.value())))
/* 25 */       .filter(e -> !((NetworkPayload)e.getSecond()).isEmpty())
/* 26 */       .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
/*    */   }
/*    */   
/*    */   private static <T> NetworkPayload serializeToNetwork(Registry<T> registry) {
/* 30 */     Map<Identifier, IntList> result = new HashMap<Identifier, IntList>();
/* 31 */     registry.getTags().forEach(tag -> {
/* 32 */           IntArrayList intArrayList = new IntArrayList(tag.size());
/* 33 */           for (Holder<T> holder : tag) {
/* 34 */             if (holder.kind() != Holder.Kind.REFERENCE) {
/* 35 */               throw new IllegalStateException("Can't serialize unregistered value " + String.valueOf(holder));
/*    */             }
/* 37 */             intArrayList.add(registry.getId(holder.value()));
/*    */           } 
/* 39 */           result.put(tag.key().location(), intArrayList);
/*    */         });
/* 41 */     return new NetworkPayload(result);
/*    */   }
/*    */   
/*    */   private static <T> TagLoader.LoadResult<T> deserializeTagsFromNetwork(Registry<T> registry, NetworkPayload payload) {
/* 45 */     ResourceKey<? extends Registry<T>> registryKey = registry.key();
/* 46 */     Map<TagKey<T>, List<Holder<T>>> tags = new HashMap<TagKey<T>, List<Holder<T>>>();
/*    */     
/* 48 */     payload.tags.forEach((key, ids) -> {
/* 49 */           TagKey<T> tagKey = TagKey.create(registryKey, key);
/* 50 */           Objects.requireNonNull(registry); List<Holder<T>> values = (List)ids.intStream().mapToObj(registry::get).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/* 51 */           tags.put(tagKey, values);
/*    */         });
/*    */     
/* 54 */     return new TagLoader.LoadResult(registryKey, tags);
/*    */   }
/*    */   
/*    */   public static final class NetworkPayload {
/* 58 */     public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */     
/*    */     private final Map<Identifier, IntList> tags;
/*    */ 
/*    */     
/* 63 */     NetworkPayload(Map<Identifier, IntList> tags) { this.tags = tags; }
/*    */ 
/*    */ 
/*    */     
/* 67 */     public void write(FriendlyByteBuf buf) { buf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList); }
/*    */ 
/*    */ 
/*    */     
/* 71 */     public static NetworkPayload read(FriendlyByteBuf buf) { return new NetworkPayload(buf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList)); }
/*    */ 
/*    */ 
/*    */     
/* 75 */     public boolean isEmpty() { return this.tags.isEmpty(); }
/*    */ 
/*    */ 
/*    */     
/* 79 */     public int size() { return this.tags.size(); }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public <T> TagLoader.LoadResult<T> resolve(Registry<T> registry) { return TagNetworkSerialization.deserializeTagsFromNetwork(registry, this); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\TagNetworkSerialization.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */