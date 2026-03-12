/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class BuildState
/*     */   extends Record
/*     */ {
/*     */   private final RegistrySetBuilder.UniversalOwner owner;
/*     */   private final RegistrySetBuilder.UniversalLookup lookup;
/*     */   private final Map<Identifier, HolderGetter<?>> registries;
/*     */   private final Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues;
/*     */   private final List<RuntimeException> errors;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 127 */   private BuildState(RegistrySetBuilder.UniversalOwner owner, RegistrySetBuilder.UniversalLookup lookup, Map<Identifier, HolderGetter<?>> registries, Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues, List<RuntimeException> errors) { this.owner = owner; this.lookup = lookup; this.registries = registries; this.registeredValues = registeredValues; this.errors = errors; } public RegistrySetBuilder.UniversalOwner owner() { return this.owner; } public RegistrySetBuilder.UniversalLookup lookup() { return this.lookup; } public Map<Identifier, HolderGetter<?>> registries() { return this.registries; } public Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues() { return this.registeredValues; } public List<RuntimeException> errors() { return this.errors; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BuildState create(RegistryAccess context, Stream<ResourceKey<? extends Registry<?>>> newRegistries) {
/* 135 */     RegistrySetBuilder.UniversalOwner owner = new RegistrySetBuilder.UniversalOwner();
/* 136 */     List<RuntimeException> errors = new ArrayList<RuntimeException>();
/* 137 */     RegistrySetBuilder.UniversalLookup lookup = new RegistrySetBuilder.UniversalLookup(owner);
/*     */     
/* 139 */     ImmutableMap.Builder<Identifier, HolderGetter<?>> registries = ImmutableMap.builder();
/* 140 */     context.registries().forEach(contextRegistry -> registries.put(contextRegistry.key().identifier(), RegistrySetBuilder.wrapContextLookup(contextRegistry.value())));
/* 141 */     newRegistries.forEach(newRegistry -> registries.put(newRegistry.identifier(), lookup));
/*     */     
/* 143 */     return new BuildState(owner, lookup, registries
/*     */ 
/*     */         
/* 146 */         .build(), new HashMap(), errors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> BootstrapContext<T> bootstrapContext() {
/* 153 */     return new BootstrapContext<T>()
/*     */       {
/*     */         public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
/* 156 */           RegistrySetBuilder.RegisteredValue<?> previousValue = (RegistrySetBuilder.RegisteredValue)RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle));
/* 157 */           if (previousValue != null) {
/* 158 */             RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value)));
/*     */           }
/* 160 */           return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 166 */         public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) { return (HolderGetter)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportUnclaimedRegisteredValues() {
/* 172 */     this.registeredValues.forEach((key, registeredValue) -> 
/* 173 */         this.errors.add(new IllegalStateException("Orpaned value " + String.valueOf(registeredValue.value) + " for key " + String.valueOf(key))));
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportNotCollectedHolders() {
/* 178 */     for (ResourceKey<Object> key : this.lookup.holders.keySet()) {
/* 179 */       this.errors.add(new IllegalStateException("Unreferenced key: " + String.valueOf(key)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void throwOnError() {
/* 184 */     if (!this.errors.isEmpty()) {
/* 185 */       IllegalStateException result = new IllegalStateException("Errors during registry creation");
/* 186 */       for (RuntimeException error : this.errors) {
/* 187 */         result.addSuppressed(error);
/*     */       }
/* 189 */       throw result;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$BuildState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */