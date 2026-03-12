/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ 
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.animal.TemperatureVariants;
/*    */ import net.minecraft.world.entity.variant.BiomeCheck;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class PigVariants {
/* 17 */   public static final ResourceKey<PigVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<PigVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<PigVariant> COLD = createKey(TemperatureVariants.COLD);
/* 20 */   public static final ResourceKey<PigVariant> DEFAULT = TEMPERATE;
/*    */ 
/*    */   
/* 23 */   private static ResourceKey<PigVariant> createKey(Identifier id) { return ResourceKey.create(Registries.PIG_VARIANT, id); }
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PigVariant> context) {
/* 27 */     register(context, TEMPERATE, PigVariant.ModelType.NORMAL, "temperate_pig", SpawnPrioritySelectors.fallback(0));
/* 28 */     register(context, WARM, PigVariant.ModelType.NORMAL, "warm_pig", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 29 */     register(context, COLD, PigVariant.ModelType.COLD, "cold_pig", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> context, ResourceKey<PigVariant> name, PigVariant.ModelType modelType, String textureName, TagKey<Biome> spawnBiome) {
/* 33 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 34 */     register(context, name, modelType, textureName, SpawnPrioritySelectors.single(new BiomeCheck(named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> context, ResourceKey<PigVariant> name, PigVariant.ModelType modelType, String textureName, SpawnPrioritySelectors selectors) {
/* 38 */     Identifier textureId = Identifier.withDefaultNamespace("entity/pig/" + textureName);
/* 39 */     context.register(name, new PigVariant(new ModelAndTexture(modelType, textureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\pig\PigVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */