/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ 
/*     */ class SimpleType<T>
/*     */   extends Object
/*     */   implements DataComponentType<T>
/*     */ {
/*     */   private final Codec<T> codec;
/*     */   private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
/*     */   private final boolean ignoreSwapAnimation;
/*     */   
/*     */   private SimpleType(Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean ignoreSwapAnimation) {
/*  96 */     this.codec = codec;
/*  97 */     this.streamCodec = streamCodec;
/*  98 */     this.ignoreSwapAnimation = ignoreSwapAnimation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean ignoreSwapAnimation() { return this.ignoreSwapAnimation; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public String toString() { return Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentType$Builder$SimpleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */