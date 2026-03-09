/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements BootstrapContext<T>
/*     */ {
/*     */   public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
/* 156 */     RegistrySetBuilder.RegisteredValue<?> previousValue = (RegistrySetBuilder.RegisteredValue)RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle));
/* 157 */     if (previousValue != null) {
/* 158 */       RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value)));
/*     */     }
/* 160 */     return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) { return (HolderGetter)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$BuildState$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */