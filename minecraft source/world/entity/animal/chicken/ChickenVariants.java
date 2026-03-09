/*    */ package net.minecraft.world.entity.animal.chicken;
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
/*    */ public class ChickenVariants {
/* 17 */   public static final ResourceKey<ChickenVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<ChickenVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<ChickenVariant> COLD = createKey(TemperatureVariants.COLD);
/* 20 */   public static final ResourceKey<ChickenVariant> DEFAULT = TEMPERATE;
/*    */ 
/*    */   
/* 23 */   private static ResourceKey<ChickenVariant> createKey(Identifier id) { return ResourceKey.create(Registries.CHICKEN_VARIANT, id); }
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ChickenVariant> context) {
/* 27 */     register(context, TEMPERATE, ChickenVariant.ModelType.NORMAL, "temperate_chicken", SpawnPrioritySelectors.fallback(0));
/* 28 */     register(context, WARM, ChickenVariant.ModelType.NORMAL, "warm_chicken", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 29 */     register(context, COLD, ChickenVariant.ModelType.COLD, "cold_chicken", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> context, ResourceKey<ChickenVariant> name, ChickenVariant.ModelType modelType, String textureName, TagKey<Biome> spawnBiome) {
/* 33 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 34 */     register(context, name, modelType, textureName, SpawnPrioritySelectors.single(new BiomeCheck(named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> context, ResourceKey<ChickenVariant> name, ChickenVariant.ModelType modelType, String textureName, SpawnPrioritySelectors selectors) {
/* 38 */     Identifier textureId = Identifier.withDefaultNamespace("entity/chicken/" + textureName);
/* 39 */     context.register(name, new ChickenVariant(new ModelAndTexture(modelType, textureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\chicken\ChickenVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */