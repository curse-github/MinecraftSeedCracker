/*    */ package net.minecraft.data.registries;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistriesDatapackGenerator implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public RegistriesDatapackGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 24 */     this.registries = registries;
/* 25 */     this.output = output;
/*    */   }
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 30 */     return this.registries.thenCompose(access -> {
/* 31 */           RegistryOps registryOps1 = access.createSerializationContext(JsonOps.INSTANCE);
/* 32 */           return CompletableFuture.allOf((CompletableFuture[])RegistryDataLoader.WORLDGEN_REGISTRIES.stream()
/* 33 */               .flatMap(())
/* 34 */               .toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cache, HolderLookup.Provider registries, DynamicOps<JsonElement> writeOps, RegistryDataLoader.RegistryData<T> v) {
/* 39 */     ResourceKey<? extends Registry<T>> registryKey = v.key();
/* 40 */     return registries.lookup(registryKey).map(registry -> {
/* 41 */           PackOutput.PathProvider pathProvider = this.output.createRegistryElementsPathProvider(registryKey);
/*    */           
/* 43 */           return CompletableFuture.allOf((CompletableFuture[])registry.listElements()
/* 44 */               .map(())
/* 45 */               .toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   private static <E> CompletableFuture<?> dumpValue(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, Encoder<E> codec, E value) {
/* 50 */     return (CompletableFuture)codec.encodeStart(ops, value).mapOrElse(result -> 
/* 51 */         DataProvider.saveStable(cache, result, path), error -> 
/* 52 */         CompletableFuture.failedFuture(new IllegalStateException("Couldn't generate file '" + String.valueOf(path) + "': " + error.message())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public final String getName() { return "Registries"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\registries\RegistriesDatapackGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */