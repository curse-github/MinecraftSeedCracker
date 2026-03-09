/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegistrySetBuilder
/*     */ {
/*     */   private static class LazyHolder<T>
/*     */     extends Holder.Reference<T>
/*     */   {
/*     */     private Supplier<T> supplier;
/*     */     
/*  35 */     protected LazyHolder(HolderOwner<T> owner, ResourceKey<T> key) { super(Holder.Reference.Type.STAND_ALONE, owner, key, null); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void bindValue(T value) {
/*  40 */       super.bindValue(value);
/*  41 */       this.supplier = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public T value() {
/*  46 */       if (this.supplier != null) {
/*  47 */         bindValue(this.supplier.get());
/*     */       }
/*  49 */       return (T)super.value();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class EmptyTagLookup<T>
/*     */     extends Object implements HolderGetter<T> {
/*     */     protected final HolderOwner<T> owner;
/*     */     
/*  57 */     protected EmptyTagLookup(HolderOwner<T> owner) { this.owner = owner; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     public Optional<HolderSet.Named<T>> get(TagKey<T> id) { return Optional.of(HolderSet.emptyNamed(this.owner, id)); }
/*     */   }
/*     */   
/*     */   private static abstract class EmptyTagRegistryLookup<T>
/*     */     extends EmptyTagLookup<T>
/*     */     implements HolderLookup.RegistryLookup<T> {
/*  68 */     protected EmptyTagRegistryLookup(HolderOwner<T> owner) { super(owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     public Stream<HolderSet.Named<T>> listTags() { throw new UnsupportedOperationException("Tags are not available in datagen"); }
/*     */   }
/*     */   
/*     */   private static class EmptyTagLookupWrapper<T>
/*     */     extends EmptyTagRegistryLookup<T> implements HolderLookup.RegistryLookup.Delegate<T> {
/*     */     private final HolderLookup.RegistryLookup<T> parent;
/*     */     
/*     */     private EmptyTagLookupWrapper(HolderOwner<T> owner, HolderLookup.RegistryLookup<T> parent) {
/*  81 */       super(owner);
/*  82 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  87 */     public HolderLookup.RegistryLookup<T> parent() { return this.parent; }
/*     */   }
/*     */   
/*     */   private static class UniversalOwner
/*     */     extends Object
/*     */     implements HolderOwner<Object>
/*     */   {
/*  94 */     public <T> HolderOwner<T> cast() { return this; }
/*     */   }
/*     */   
/*     */   private static class UniversalLookup
/*     */     extends EmptyTagLookup<Object> {
/*  99 */     private final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap();
/*     */ 
/*     */     
/* 102 */     public UniversalLookup(HolderOwner<Object> owner) { super(owner); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) { return Optional.of(getOrCreate(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     private <T> Holder.Reference<T> getOrCreate(ResourceKey<T> id) { return (Holder.Reference)this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this.owner, k)); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> HolderGetter<T> wrapContextLookup(final HolderLookup.RegistryLookup<T> original) {
/* 117 */     return new EmptyTagLookup<T>(original)
/*     */       {
/*     */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 120 */           return original.get(id); }
/*     */       };
/*     */   }
/*     */   private static final class RegisteredValue<T> extends Record { private final T value; private final Lifecycle lifecycle;
/*     */     
/* 125 */     private RegisteredValue(T value, Lifecycle lifecycle) { this.value = value; this.lifecycle = lifecycle; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 125 */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegisteredValue<TT;>; } public T value() { return (T)this.value; } public Lifecycle lifecycle() { return this.lifecycle; } }
/*     */   private static final class BuildState extends Record { private final RegistrySetBuilder.UniversalOwner owner; private final RegistrySetBuilder.UniversalLookup lookup; private final Map<Identifier, HolderGetter<?>> registries; private final Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues; private final List<RuntimeException> errors;
/* 127 */     private BuildState(RegistrySetBuilder.UniversalOwner owner, RegistrySetBuilder.UniversalLookup lookup, Map<Identifier, HolderGetter<?>> registries, Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues, List<RuntimeException> errors) { this.owner = owner; this.lookup = lookup; this.registries = registries; this.registeredValues = registeredValues; this.errors = errors; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState;
/* 127 */       //   0	8	1	o	Ljava/lang/Object; } public RegistrySetBuilder.UniversalOwner owner() { return this.owner; } public RegistrySetBuilder.UniversalLookup lookup() { return this.lookup; } public Map<Identifier, HolderGetter<?>> registries() { return this.registries; } public Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues() { return this.registeredValues; } public List<RuntimeException> errors() { return this.errors; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static BuildState create(RegistryAccess context, Stream<ResourceKey<? extends Registry<?>>> newRegistries) {
/* 135 */       RegistrySetBuilder.UniversalOwner owner = new RegistrySetBuilder.UniversalOwner();
/* 136 */       List<RuntimeException> errors = new ArrayList<RuntimeException>();
/* 137 */       RegistrySetBuilder.UniversalLookup lookup = new RegistrySetBuilder.UniversalLookup(owner);
/*     */       
/* 139 */       ImmutableMap.Builder<Identifier, HolderGetter<?>> registries = ImmutableMap.builder();
/* 140 */       context.registries().forEach(contextRegistry -> registries.put(contextRegistry.key().identifier(), RegistrySetBuilder.wrapContextLookup(contextRegistry.value())));
/* 141 */       newRegistries.forEach(newRegistry -> registries.put(newRegistry.identifier(), lookup));
/*     */       
/* 143 */       return new BuildState(owner, lookup, registries
/*     */ 
/*     */           
/* 146 */           .build(), new HashMap(), errors);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> BootstrapContext<T> bootstrapContext() {
/* 153 */       return new BootstrapContext<T>()
/*     */         {
/*     */           public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
/* 156 */             RegistrySetBuilder.RegisteredValue<?> previousValue = (RegistrySetBuilder.RegisteredValue)RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle));
/* 157 */             if (previousValue != null) {
/* 158 */               RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value)));
/*     */             }
/* 160 */             return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 166 */           public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) { return (HolderGetter)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup); }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void reportUnclaimedRegisteredValues() {
/* 172 */       this.registeredValues.forEach((key, registeredValue) -> 
/* 173 */           this.errors.add(new IllegalStateException("Orpaned value " + String.valueOf(registeredValue.value) + " for key " + String.valueOf(key))));
/*     */     }
/*     */ 
/*     */     
/*     */     public void reportNotCollectedHolders() {
/* 178 */       for (ResourceKey<Object> key : this.lookup.holders.keySet()) {
/* 179 */         this.errors.add(new IllegalStateException("Unreferenced key: " + String.valueOf(key)));
/*     */       }
/*     */     }
/*     */     
/*     */     public void throwOnError() {
/* 184 */       if (!this.errors.isEmpty()) {
/* 185 */         IllegalStateException result = new IllegalStateException("Errors during registry creation");
/* 186 */         for (RuntimeException error : this.errors) {
/* 187 */           result.addSuppressed(error);
/*     */         }
/* 189 */         throw result;
/*     */       }  } } class null extends Object implements BootstrapContext<T> {
/*     */     public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) { RegistrySetBuilder.RegisteredValue<?> previousValue = (RegistrySetBuilder.RegisteredValue)RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle)); if (previousValue != null)
/*     */         RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value))); 
/*     */       return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key); } public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) { return (HolderGetter)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup); }
/* 194 */   } private static final class ValueAndHolder<T> extends Record { private final RegistrySetBuilder.RegisteredValue<T> value; private ValueAndHolder(RegistrySetBuilder.RegisteredValue<T> value, Optional<Holder.Reference<T>> holder) { this.value = value; this.holder = holder; } private final Optional<Holder.Reference<T>> holder; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #194	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #194	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #194	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 194 */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>; } public RegistrySetBuilder.RegisteredValue<T> value() { return this.value; } public Optional<Holder.Reference<T>> holder() { return this.holder; } }
/*     */   private static final class RegistryStub<T> extends Record { private final ResourceKey<? extends Registry<T>> key; private final Lifecycle lifecycle; private final RegistrySetBuilder.RegistryBootstrap<T> bootstrap;
/* 196 */     private RegistryStub(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) { this.key = key; this.lifecycle = lifecycle; this.bootstrap = bootstrap; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #196	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 196 */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public Lifecycle lifecycle() { return this.lifecycle; } public RegistrySetBuilder.RegistryBootstrap<T> bootstrap() { return this.bootstrap; }
/*     */     
/* 198 */     private void apply(RegistrySetBuilder.BuildState state) { this.bootstrap.run(state.bootstrapContext()); }
/*     */ 
/*     */     
/*     */     public RegistrySetBuilder.RegistryContents<T> collectRegisteredValues(RegistrySetBuilder.BuildState state) {
/* 202 */       Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> result = new HashMap<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>>();
/*     */       
/* 204 */       Iterator<Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>>> iterator = state.registeredValues.entrySet().iterator();
/* 205 */       while (iterator.hasNext()) {
/* 206 */         Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> entry = (Map.Entry)iterator.next();
/* 207 */         ResourceKey<?> key = (ResourceKey)entry.getKey();
/* 208 */         if (key.isFor(this.key)) {
/* 209 */           ResourceKey<T> castKey = key;
/* 210 */           RegistrySetBuilder.RegisteredValue<T> value = (RegistrySetBuilder.RegisteredValue)entry.getValue();
/* 211 */           Holder.Reference<T> holder = (Holder.Reference)state.lookup.holders.remove(key);
/* 212 */           result.put(castKey, new RegistrySetBuilder.ValueAndHolder(value, Optional.ofNullable(holder)));
/*     */           
/* 214 */           iterator.remove();
/*     */         } 
/*     */       } 
/* 217 */       return new RegistrySetBuilder.RegistryContents(this.key, this.lifecycle, result);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static <T> HolderLookup.RegistryLookup<T> lookupFromMap(final ResourceKey<? extends Registry<? extends T>> key, final Lifecycle lifecycle, HolderOwner<T> owner, final Map<ResourceKey<T>, Holder.Reference<T>> entries) {
/* 222 */     return new EmptyTagRegistryLookup<T>(owner)
/*     */       {
/*     */         public ResourceKey<? extends Registry<? extends T>> key() {
/* 225 */           return key;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 230 */         public Lifecycle registryLifecycle() { return lifecycle; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 235 */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return Optional.ofNullable((Holder.Reference)entries.get(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 240 */         public Stream<Holder.Reference<T>> listElements() { return entries.values().stream(); }
/*     */       };
/*     */   }
/*     */   private static final class RegistryContents<T> extends Record { private final ResourceKey<? extends Registry<? extends T>> key; private final Lifecycle lifecycle; private final Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values;
/*     */     
/* 245 */     private RegistryContents(ResourceKey<? extends Registry<? extends T>> key, Lifecycle lifecycle, Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values) { this.key = key; this.lifecycle = lifecycle; this.values = values; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #245	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #245	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #245	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 245 */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>; } public ResourceKey<? extends Registry<? extends T>> key() { return this.key; } public Lifecycle lifecycle() { return this.lifecycle; } public Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values() { return this.values; }
/*     */     public HolderLookup.RegistryLookup<T> buildAsLookup(RegistrySetBuilder.UniversalOwner owner) {
/* 247 */       Map<ResourceKey<T>, Holder.Reference<T>> entries = (Map)this.values.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> {
/*     */ 
/*     */               
/* 250 */               RegistrySetBuilder.ValueAndHolder<T> entry = (RegistrySetBuilder.ValueAndHolder)e.getValue();
/* 251 */               Holder.Reference<T> holder = (Holder.Reference)entry.holder().orElseGet(());
/* 252 */               holder.bindValue(entry.value().value());
/* 253 */               return holder;
/*     */             }));
/*     */ 
/*     */       
/* 257 */       return RegistrySetBuilder.lookupFromMap(this.key, this.lifecycle, owner.cast(), entries);
/*     */     } }
/*     */ 
/*     */   
/* 261 */   private final List<RegistryStub<?>> entries = new ArrayList();
/*     */   
/*     */   public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, RegistryBootstrap<T> bootstrap) {
/* 264 */     this.entries.add(new RegistryStub(key, lifecycle, bootstrap));
/* 265 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 269 */   public <T> RegistrySetBuilder add(ResourceKey<? extends Registry<T>> key, RegistryBootstrap<T> bootstrap) { return add(key, Lifecycle.stable(), bootstrap); }
/*     */ 
/*     */   
/*     */   private BuildState createState(RegistryAccess context) {
/* 273 */     BuildState state = BuildState.create(context, this.entries.stream().map(RegistryStub::key));
/* 274 */     this.entries.forEach(e -> e.apply(state));
/* 275 */     return state;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static HolderLookup.Provider buildProviderWithContext(UniversalOwner owner, RegistryAccess context, Stream<HolderLookup.RegistryLookup<?>> newRegistries) {
/* 291 */     final Map<ResourceKey<? extends Registry<?>>, Entry<?>> lookups = new HashMap<ResourceKey<? extends Registry<?>>, Entry<?>>();
/* 292 */     context.registries().forEach(contextRegistry -> lookups.put(contextRegistry.key(), Entry.createForContextRegistry(contextRegistry.value())));
/* 293 */     newRegistries.forEach(newRegistry -> lookups.put(newRegistry.key(), Entry.createForNewRegistry(owner, newRegistry)));
/*     */     
/* 295 */     return new HolderLookup.Provider()
/*     */       {
/*     */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 298 */           return lookups.keySet().stream();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 303 */         private <T> Optional<RegistrySetBuilder.Entry<T>> getEntry(ResourceKey<? extends Registry<? extends T>> key) { return Optional.ofNullable((RegistrySetBuilder.Entry)lookups.get(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 308 */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) { return getEntry(key).map(RegistrySetBuilder.Entry::lookup); }
/*     */ 
/*     */ 
/*     */         
/*     */         public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/* 313 */           return RegistryOps.create(parent, new RegistryOps.RegistryInfoLookup()
/*     */               {
/*     */                 public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/* 316 */                   return RegistrySetBuilder.null.this.getEntry(registryKey).map(RegistrySetBuilder.Entry::opsInfo);
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider build(RegistryAccess context) {
/* 324 */     BuildState state = createState(context);
/*     */     
/* 326 */     Stream<HolderLookup.RegistryLookup<?>> newRegistries = this.entries.stream().map(stub -> stub.collectRegisteredValues(state).buildAsLookup(state.owner));
/* 327 */     HolderLookup.Provider result = buildProviderWithContext(state.owner, context, newRegistries);
/*     */     
/* 329 */     state.reportNotCollectedHolders();
/* 330 */     state.reportUnclaimedRegisteredValues();
/* 331 */     state.throwOnError();
/*     */     
/* 333 */     return result;
/*     */   }
/*     */   
/*     */   public static final class PatchedRegistries extends Record {
/*     */     private final HolderLookup.Provider full;
/*     */     private final HolderLookup.Provider patches;
/*     */     
/* 340 */     public PatchedRegistries(HolderLookup.Provider full, HolderLookup.Provider patches) { this.full = full; this.patches = patches; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$PatchedRegistries;
/* 340 */       //   0	8	1	o	Ljava/lang/Object; } public HolderLookup.Provider full() { return this.full; } public HolderLookup.Provider patches() { return this.patches; }
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
/*     */   private HolderLookup.Provider createLazyFullPatchedRegistries(RegistryAccess context, HolderLookup.Provider fallbackProvider, Cloner.Factory clonerFactory, Map<ResourceKey<? extends Registry<?>>, RegistryContents<?>> newRegistries, HolderLookup.Provider patchOnlyRegistries) {
/* 353 */     UniversalOwner fullPatchedOwner = new UniversalOwner();
/* 354 */     MutableObject<HolderLookup.Provider> resultReference = new MutableObject<HolderLookup.Provider>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     List<HolderLookup.RegistryLookup<?>> lazyFullRegistries = (List)newRegistries.keySet().stream().map(registryKey -> createLazyFullPatchedRegistries(fullPatchedOwner, clonerFactory, registryKey, patchOnlyRegistries, fallbackProvider, resultReference)).collect(Collectors.toUnmodifiableList());
/*     */     
/* 361 */     HolderLookup.Provider result = buildProviderWithContext(fullPatchedOwner, context, lazyFullRegistries.stream());
/* 362 */     resultReference.setValue(result);
/* 363 */     return result;
/*     */   }
/*     */   
/*     */   private <T> HolderLookup.RegistryLookup<T> createLazyFullPatchedRegistries(HolderOwner<T> owner, Cloner.Factory clonerFactory, ResourceKey<? extends Registry<? extends T>> registryKey, HolderLookup.Provider patchProvider, HolderLookup.Provider fallbackProvider, MutableObject<HolderLookup.Provider> targetProvider) {
/* 367 */     Cloner<T> cloner = clonerFactory.cloner(registryKey);
/* 368 */     if (cloner == null) {
/* 369 */       throw new NullPointerException("No cloner for " + String.valueOf(registryKey.identifier()));
/*     */     }
/*     */     
/* 372 */     Map<ResourceKey<T>, Holder.Reference<T>> entries = new HashMap<ResourceKey<T>, Holder.Reference<T>>();
/*     */     
/* 374 */     HolderLookup.RegistryLookup<T> patchContents = patchProvider.lookupOrThrow(registryKey);
/* 375 */     patchContents.listElements().forEach(elementHolder -> {
/* 376 */           ResourceKey<T> elementKey = elementHolder.key();
/*     */           
/* 378 */           LazyHolder<T> holder = new LazyHolder<T>(owner, elementKey);
/* 379 */           holder.supplier = (());
/* 380 */           entries.put(elementKey, holder);
/*     */         });
/*     */     
/* 383 */     HolderLookup.RegistryLookup<T> fallbackContents = fallbackProvider.lookupOrThrow(registryKey);
/* 384 */     fallbackContents.listElements().forEach(elementHolder -> {
/* 385 */           ResourceKey<T> elementKey = elementHolder.key();
/*     */           
/* 387 */           entries.computeIfAbsent(elementKey, ());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     Lifecycle lifecycle = patchContents.registryLifecycle().add(fallbackContents.registryLifecycle());
/*     */     
/* 396 */     return lookupFromMap(registryKey, lifecycle, owner, entries);
/*     */   }
/*     */   
/*     */   public PatchedRegistries buildPatch(RegistryAccess context, HolderLookup.Provider fallbackProvider, Cloner.Factory clonerFactory) {
/* 400 */     BuildState state = createState(context);
/*     */     
/* 402 */     Map<ResourceKey<? extends Registry<?>>, RegistryContents<?>> newRegistries = new HashMap<ResourceKey<? extends Registry<?>>, RegistryContents<?>>();
/*     */ 
/*     */     
/* 405 */     this.entries.stream()
/* 406 */       .map(stub -> stub.collectRegisteredValues(state))
/* 407 */       .forEach(e -> newRegistries.put(e.key, e));
/*     */ 
/*     */     
/* 410 */     Set<ResourceKey<? extends Registry<?>>> contextRegistries = (Set)context.listRegistryKeys().collect(Collectors.toUnmodifiableSet());
/* 411 */     fallbackProvider
/* 412 */       .listRegistryKeys()
/* 413 */       .filter(k -> !contextRegistries.contains(k))
/* 414 */       .forEach(resourceKey -> newRegistries.putIfAbsent(resourceKey, new RegistryContents(resourceKey, Lifecycle.stable(), Map.of())));
/*     */     
/* 416 */     Stream<HolderLookup.RegistryLookup<?>> dynamicRegistries = newRegistries.values().stream().map(registryContents -> registryContents.buildAsLookup(state.owner));
/* 417 */     HolderLookup.Provider patchOnlyRegistries = buildProviderWithContext(state.owner, context, dynamicRegistries);
/*     */     
/* 419 */     state.reportUnclaimedRegisteredValues();
/* 420 */     state.throwOnError();
/*     */     
/* 422 */     HolderLookup.Provider fullPatchedRegistries = createLazyFullPatchedRegistries(context, fallbackProvider, clonerFactory, newRegistries, patchOnlyRegistries);
/*     */     
/* 424 */     return new PatchedRegistries(fullPatchedRegistries, patchOnlyRegistries);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RegistryBootstrap<T> {
/*     */     void run(BootstrapContext<T> param1BootstrapContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */