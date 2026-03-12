/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Climate;
/*    */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class BiomeParametersDumpReport implements DataProvider {
/* 29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path topPath;
/*    */   
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/* 34 */   private static final MapCodec<ResourceKey<Biome>> ENTRY_CODEC = ResourceKey.codec(Registries.BIOME).fieldOf("biome");
/*    */   
/* 36 */   private static final Codec<Climate.ParameterList<ResourceKey<Biome>>> CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes").codec();
/*    */   
/*    */   public BiomeParametersDumpReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 39 */     this.topPath = output.getOutputFolder(PackOutput.Target.REPORTS).resolve("biome_parameters");
/* 40 */     this.registries = registries;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 45 */     return this.registries.thenCompose(registryAccess -> {
/* 46 */           RegistryOps registryOps1 = registryAccess.createSerializationContext(JsonOps.INSTANCE);
/* 47 */           List<CompletableFuture<?>> result = new ArrayList<CompletableFuture<?>>();
/* 48 */           MultiNoiseBiomeSourceParameterList.knownPresets().forEach(());
/*    */ 
/*    */           
/* 51 */           return CompletableFuture.allOf((CompletableFuture[])result.toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   private static <E> CompletableFuture<?> dumpValue(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, Encoder<E> codec, E value) {
/* 56 */     Optional<JsonElement> result = codec.encodeStart(ops, value).resultOrPartial(e -> LOGGER.error("Couldn't serialize element {}: {}", path, e));
/* 57 */     if (result.isPresent()) {
/* 58 */       return DataProvider.saveStable(cache, (JsonElement)result.get(), path);
/*    */     }
/* 60 */     return CompletableFuture.completedFuture(null);
/*    */   }
/*    */   
/*    */   private Path createPath(Identifier element) {
/* 64 */     return this.topPath.resolve(element.getNamespace()).resolve(element.getPath() + ".json");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public final String getName() { return "Biome Parameters"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\info\BiomeParametersDumpReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */