/*     */ package net.minecraft.resources;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ public class RegistryOps<T> extends DelegatingOps<T> {
/*     */   private final RegistryInfoLookup lookupProvider;
/*     */   
/*     */   public static final class RegistryInfo<T> extends Record {
/*     */     private final HolderOwner<T> owner;
/*     */     private final HolderGetter<T> getter;
/*     */     private final Lifecycle elementsLifecycle;
/*     */     
/*  20 */     public RegistryInfo(HolderOwner<T> owner, HolderGetter<T> getter, Lifecycle elementsLifecycle) { this.owner = owner; this.getter = getter; this.elementsLifecycle = elementsLifecycle; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  20 */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public HolderOwner<T> owner() { return this.owner; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  20 */       //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public HolderGetter<T> getter() { return this.getter; } public Lifecycle elementsLifecycle() { return this.elementsLifecycle; }
/*     */     
/*  22 */     public static <T> RegistryInfo<T> fromRegistryLookup(HolderLookup.RegistryLookup<T> registry) { return new RegistryInfo(registry, registry, registry.registryLifecycle()); }
/*     */   }
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
/*  34 */   public static <T> RegistryOps<T> create(DynamicOps<T> parent, HolderLookup.Provider lookupProvider) { return create(parent, new HolderLookupAdapter(lookupProvider)); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static <T> RegistryOps<T> create(DynamicOps<T> parent, RegistryInfoLookup lookupProvider) { return new RegistryOps(parent, lookupProvider); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static <T> Dynamic<T> injectRegistryContext(Dynamic<T> dynamic, HolderLookup.Provider lookupProvider) { return new Dynamic(lookupProvider.createSerializationContext(dynamic.getOps()), dynamic.getValue()); }
/*     */ 
/*     */   
/*     */   private RegistryOps(DynamicOps<T> parent, RegistryInfoLookup lookupProvider) {
/*  46 */     super(parent);
/*  47 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> RegistryOps<U> withParent(DynamicOps<U> parent) {
/*  52 */     if (parent == this.delegate) {
/*  53 */       return this;
/*     */     }
/*  55 */     return new RegistryOps(parent, this.lookupProvider);
/*     */   }
/*     */ 
/*     */   
/*  59 */   public <E> Optional<HolderOwner<E>> owner(ResourceKey<? extends Registry<? extends E>> registryKey) { return this.lookupProvider.lookup(registryKey).map(RegistryInfo::owner); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public <E> Optional<HolderGetter<E>> getter(ResourceKey<? extends Registry<? extends E>> registryKey) { return this.lookupProvider.lookup(registryKey).map(RegistryInfo::getter); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     if (this == obj) {
/*  69 */       return true;
/*     */     }
/*  71 */     if (obj == null || getClass() != obj.getClass()) {
/*  72 */       return false;
/*     */     }
/*  74 */     RegistryOps<?> ops = (RegistryOps)obj;
/*  75 */     return (this.delegate.equals(ops.delegate) && this.lookupProvider.equals(ops.lookupProvider));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int hashCode() { return this.delegate.hashCode() * 31 + this.lookupProvider.hashCode(); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static <E, O> RecordCodecBuilder<O, HolderGetter<E>> retrieveGetter(ResourceKey<? extends Registry<? extends E>> registryKey) { return ExtraCodecs.retrieveContext(ops -> {
/*  85 */           if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/*  86 */             return (DataResult)registryOps.lookupProvider.lookup(registryKey)
/*  87 */               .map(())
/*  88 */               .orElseGet(()); }
/*     */           
/*  90 */           return DataResult.error(());
/*  91 */         }).forGetter(e -> null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E, O> RecordCodecBuilder<O, Holder.Reference<E>> retrieveElement(ResourceKey<E> key) {
/*  99 */     ResourceKey<? extends Registry<E>> registryKey = ResourceKey.createRegistryKey(key.registry());
/* 100 */     return ExtraCodecs.retrieveContext(ops -> {
/* 101 */           if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/* 102 */             return (DataResult)registryOps.lookupProvider.lookup(registryKey)
/* 103 */               .flatMap(())
/* 104 */               .map(DataResult::success)
/* 105 */               .orElseGet(()); }
/*     */           
/* 107 */           return DataResult.error(());
/* 108 */         }).forGetter(e -> null);
/*     */   } public static interface RegistryInfoLookup {
/*     */     <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey); }
/*     */   private static final class HolderLookupAdapter implements RegistryInfoLookup { private final HolderLookup.Provider lookupProvider; private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups;
/*     */     public HolderLookupAdapter(HolderLookup.Provider lookupProvider) {
/* 113 */       this.lookups = new ConcurrentHashMap();
/*     */ 
/*     */       
/* 116 */       this.lookupProvider = lookupProvider;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) { return (Optional)this.lookups.computeIfAbsent(registryKey, this::createLookup); }
/*     */ 
/*     */ 
/*     */     
/* 126 */     private Optional<RegistryOps.RegistryInfo<Object>> createLookup(ResourceKey<? extends Registry<?>> key) { return this.lookupProvider.lookup(key).map(RegistryOps.RegistryInfo::fromRegistryLookup); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 131 */       if (this == obj) {
/* 132 */         return true;
/*     */       }
/* 134 */       if (obj instanceof HolderLookupAdapter) { HolderLookupAdapter adapter = (HolderLookupAdapter)obj; if (this.lookupProvider.equals(adapter.lookupProvider)); }  return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 139 */     public int hashCode() { return this.lookupProvider.hashCode(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */