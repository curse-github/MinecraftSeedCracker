/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DataComponentType<T>
/*     */ {
/*  19 */   public static final Codec<DataComponentType<?>> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec());
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentType<?>> STREAM_CODEC = StreamCodec.recursive(c -> ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE));
/*     */   
/*  22 */   public static final Codec<DataComponentType<?>> PERSISTENT_CODEC = CODEC.validate(type -> type.isTransient() ? DataResult.error(()) : DataResult.success(type));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static final Codec<Map<DataComponentType<?>, Object>> VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, DataComponentType::codecOrThrow);
/*     */ 
/*     */   
/*  30 */   static <T> Builder<T> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Codec<T> codecOrThrow() {
/*  36 */     Codec<T> codec = codec();
/*  37 */     if (codec == null) {
/*  38 */       throw new IllegalStateException(String.valueOf(this) + " is not a persistent component");
/*     */     }
/*  40 */     return codec;
/*     */   }
/*     */ 
/*     */   
/*  44 */   default boolean isTransient() { return (codec() == null); }
/*     */   
/*     */   Codec<T> codec();
/*     */   
/*     */   boolean ignoreSwapAnimation();
/*     */   
/*     */   StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*     */   
/*     */   public static class Builder<T> extends Object { private Codec<T> codec;
/*     */     private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private boolean cacheEncoding;
/*     */     private boolean ignoreSwapAnimation;
/*     */     
/*     */     public Builder<T> persistent(Codec<T> codec) {
/*  58 */       this.codec = codec;
/*  59 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
/*  66 */       this.streamCodec = streamCodec;
/*  67 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> cacheEncoding() {
/*  71 */       this.cacheEncoding = true;
/*  72 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentType<T> build() {
/*  76 */       StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = (StreamCodec)Objects.requireNonNullElseGet(this.streamCodec, () -> 
/*     */           
/*  78 */           ByteBufCodecs.fromCodecWithRegistries((Codec)Objects.requireNonNull(this.codec, "Missing Codec for component")));
/*     */       
/*  80 */       Codec<T> cachingCodec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  81 */       return new SimpleType(cachingCodec, streamCodec, this.ignoreSwapAnimation);
/*     */     }
/*     */     
/*     */     public Builder<T> ignoreSwapAnimation() {
/*  85 */       this.ignoreSwapAnimation = true;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     private static class SimpleType<T>
/*     */       extends Object implements DataComponentType<T> {
/*     */       private final Codec<T> codec;
/*     */       private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */       private final boolean ignoreSwapAnimation;
/*     */       
/*     */       private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*  96 */         this.codec = codec;
/*  97 */         this.streamCodec = streamCodec;
/*  98 */         this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 103 */       public boolean ignoreSwapAnimation() { return this.ignoreSwapAnimation; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       public String toString() { return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); } } } private static class SimpleType<T> extends Object implements DataComponentType<T> { public String toString() { return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); }
/*     */     
/*     */     private final Codec<T> codec;
/*     */     private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private final boolean ignoreSwapAnimation;
/*     */     
/*     */     private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*     */       this.codec = codec;
/*     */       this.streamCodec = streamCodec;
/*     */       this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */     }
/*     */     
/*     */     public boolean ignoreSwapAnimation() { return this.ignoreSwapAnimation; }
/*     */     
/*     */     public Codec<T> codec() { return this.codec; }
/*     */     
/*     */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */