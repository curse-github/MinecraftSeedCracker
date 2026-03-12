/*    */ package net.minecraft.resources;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ResourceKey<T> extends Object {
/*    */   private static final class InternKey extends Record {
/*    */     private final Identifier registry;
/*    */     private final Identifier identifier;
/*    */     
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/ResourceKey$InternKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; }
/*    */     
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/ResourceKey$InternKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; }
/*    */     
/* 18 */     private InternKey(Identifier registry, Identifier identifier) { this.registry = registry; this.identifier = identifier; } public Identifier registry() { return this.registry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/ResourceKey$InternKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier identifier() { return this.identifier; }
/*    */   }
/* 20 */   private static final ConcurrentMap<InternKey, ResourceKey<?>> VALUES = (new MapMaker()).weakValues().makeMap();
/*    */   
/*    */   private final Identifier registryName;
/*    */   
/*    */   private final Identifier identifier;
/*    */   
/* 26 */   public static <T> Codec<ResourceKey<T>> codec(ResourceKey<? extends Registry<T>> registryName) { return Identifier.CODEC.xmap(name -> create(registryName, name), ResourceKey::identifier); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static <T> StreamCodec<ByteBuf, ResourceKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryName) { return Identifier.STREAM_CODEC.map(name -> create(registryName, name), ResourceKey::identifier); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static <T> ResourceKey<T> create(ResourceKey<? extends Registry<T>> registryName, Identifier location) { return create(registryName.identifier, location); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static <T> ResourceKey<Registry<T>> createRegistryKey(Identifier identifier) { return create(Registries.ROOT_REGISTRY_NAME, identifier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   private static <T> ResourceKey<T> create(Identifier registryName, Identifier identifier) { return (ResourceKey)VALUES.computeIfAbsent(new InternKey(registryName, identifier), k -> new ResourceKey(k.registry, k.identifier)); }
/*    */ 
/*    */   
/*    */   private ResourceKey(Identifier registryName, Identifier identifier) {
/* 50 */     this.registryName = registryName;
/* 51 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public String toString() { return "ResourceKey[" + String.valueOf(this.registryName) + " / " + String.valueOf(this.identifier) + "]"; }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean isFor(ResourceKey<? extends Registry<?>> registry) { return this.registryName.equals(registry.identifier()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public <E> Optional<ResourceKey<E>> cast(ResourceKey<? extends Registry<E>> registry) { return isFor(registry) ? Optional.of(this) : Optional.empty(); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public Identifier identifier() { return this.identifier; }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public Identifier registry() { return this.registryName; }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public ResourceKey<Registry<T>> registryKey() { return createRegistryKey(this.registryName); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\ResourceKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */