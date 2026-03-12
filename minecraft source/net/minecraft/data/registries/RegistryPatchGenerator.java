/*    */ package net.minecraft.data.registries;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.Cloner;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RegistryPatchGenerator {
/*    */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla, RegistrySetBuilder packBuilder) {
/* 19 */     return vanilla.thenApply(parent -> {
/* 20 */           RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */           
/* 22 */           Cloner.Factory cloner = new Cloner.Factory();
/* 23 */           RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(());
/*    */           
/* 25 */           RegistrySetBuilder.PatchedRegistries newRegistries = packBuilder.buildPatch(staticRegistries, parent, cloner);
/*    */           
/* 27 */           HolderLookup.Provider fullPatchedRegistry = newRegistries.full();
/* 28 */           Optional<? extends HolderLookup.RegistryLookup<Biome>> biomes = fullPatchedRegistry.lookup(Registries.BIOME);
/* 29 */           Optional<? extends HolderLookup.RegistryLookup<PlacedFeature>> features = fullPatchedRegistry.lookup(Registries.PLACED_FEATURE);
/*    */           
/* 31 */           if (biomes.isPresent() || features.isPresent()) {
/* 32 */             VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter)DataFixUtils.orElseGet(features, ()), (HolderLookup)DataFixUtils.orElseGet(biomes, ()));
/*    */           }
/* 34 */           return newRegistries;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\registries\RegistryPatchGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */