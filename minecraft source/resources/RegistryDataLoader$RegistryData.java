/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
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
/*     */ public final class RegistryData<T>
/*     */   extends Record
/*     */ {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*     */   private final Codec<T> elementCodec;
/*     */   private final boolean requiredNonEmpty;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #97	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #97	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #97	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>; }
/*     */   
/*  97 */   public RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, boolean requiredNonEmpty) { this.key = key; this.elementCodec = elementCodec; this.requiredNonEmpty = requiredNonEmpty; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public Codec<T> elementCodec() { return this.elementCodec; } public boolean requiredNonEmpty() { return this.requiredNonEmpty; }
/*     */ 
/*     */   
/* 100 */   private RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) { this(key, elementCodec, false); }
/*     */ 
/*     */   
/*     */   private RegistryDataLoader.Loader<T> create(Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 104 */     MappedRegistry mappedRegistry = new MappedRegistry(this.key, lifecycle);
/* 105 */     return new RegistryDataLoader.Loader(this, mappedRegistry, loadingErrors);
/*     */   }
/*     */ 
/*     */   
/* 109 */   public void runWithArguments(BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) { output.accept(this.key, this.elementCodec); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryDataLoader$RegistryData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */