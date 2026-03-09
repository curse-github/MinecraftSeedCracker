/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
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
/*     */ final class Loader<T>
/*     */   extends Record
/*     */ {
/*     */   private final RegistryDataLoader.RegistryData<T> data;
/*     */   private final WritableRegistry<T> registry;
/*     */   private final Map<ResourceKey<?>, Exception> loadingErrors;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$Loader;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$Loader;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$Loader;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$Loader<TT;>; }
/*     */   
/* 118 */   private Loader(RegistryDataLoader.RegistryData<T> data, WritableRegistry<T> registry, Map<ResourceKey<?>, Exception> loadingErrors) { this.data = data; this.registry = registry; this.loadingErrors = loadingErrors; } public RegistryDataLoader.RegistryData<T> data() { return this.data; } public WritableRegistry<T> registry() { return this.registry; } public Map<ResourceKey<?>, Exception> loadingErrors() { return this.loadingErrors; }
/*     */ 
/*     */   
/* 121 */   public void loadFromResources(ResourceManager resourceManager, RegistryOps.RegistryInfoLookup context) { RegistryDataLoader.loadContentsFromManager(resourceManager, context, this.registry, this.data.elementCodec, this.loadingErrors); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public void loadFromNetwork(Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entries, ResourceProvider knownDataSource, RegistryOps.RegistryInfoLookup context) { RegistryDataLoader.loadContentsFromNetwork(entries, knownDataSource, context, this.registry, this.data.elementCodec, this.loadingErrors); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryDataLoader$Loader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */