/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<T>
/*     */   extends Object
/*     */ {
/*     */   private Codec<T> codec;
/*     */   private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   private boolean cacheEncoding;
/*     */   private boolean ignoreSwapAnimation;
/*     */   
/*     */   public Builder<T> persistent(Codec<T> codec) {
/*  58 */     this.codec = codec;
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
/*  66 */     this.streamCodec = streamCodec;
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> cacheEncoding() {
/*  71 */     this.cacheEncoding = true;
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentType<T> build() {
/*  76 */     StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = (StreamCodec)Objects.requireNonNullElseGet(this.streamCodec, () -> 
/*     */         
/*  78 */         ByteBufCodecs.fromCodecWithRegistries((Codec)Objects.requireNonNull(this.codec, "Missing Codec for component")));
/*     */     
/*  80 */     Codec<T> cachingCodec = (this.cacheEncoding && this.codec != null) ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
/*  81 */     return new SimpleType(cachingCodec, streamCodec, this.ignoreSwapAnimation);
/*     */   }
/*     */   
/*     */   public Builder<T> ignoreSwapAnimation() {
/*  85 */     this.ignoreSwapAnimation = true;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   private static class SimpleType<T>
/*     */     extends Object implements DataComponentType<T> {
/*     */     private final Codec<T> codec;
/*     */     private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */     private final boolean ignoreSwapAnimation;
/*     */     
/*     */     private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*  96 */       this.codec = codec;
/*  97 */       this.streamCodec = streamCodec;
/*  98 */       this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     public boolean ignoreSwapAnimation() { return this.ignoreSwapAnimation; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     public String toString() { return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentType$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */