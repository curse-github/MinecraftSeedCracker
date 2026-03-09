/*    */ package net.minecraft.world.entity.animal.cow;
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
/*    */ public class CowVariants {
/* 17 */   public static final ResourceKey<CowVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<CowVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<CowVariant> COLD = createKey(TemperatureVariants.COLD);
/* 20 */   public static final ResourceKey<CowVariant> DEFAULT = TEMPERATE;
/*    */ 
/*    */   
/* 23 */   private static ResourceKey<CowVariant> createKey(Identifier id) { return ResourceKey.create(Registries.COW_VARIANT, id); }
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<CowVariant> context) {
/* 27 */     register(context, TEMPERATE, CowVariant.ModelType.NORMAL, "temperate_cow", SpawnPrioritySelectors.fallback(0));
/* 28 */     register(context, WARM, CowVariant.ModelType.WARM, "warm_cow", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 29 */     register(context, COLD, CowVariant.ModelType.COLD, "cold_cow", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CowVariant> context, ResourceKey<CowVariant> name, CowVariant.ModelType modelType, String textureName, TagKey<Biome> spawnBiome) {
/* 33 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 34 */     register(context, name, modelType, textureName, SpawnPrioritySelectors.single(new BiomeCheck(named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CowVariant> context, ResourceKey<CowVariant> name, CowVariant.ModelType modelType, String textureName, SpawnPrioritySelectors selectors) {
/* 38 */     Identifier textureId = Identifier.withDefaultNamespace("entity/cow/" + textureName);
/* 39 */     context.register(name, new CowVariant(new ModelAndTexture(modelType, textureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\cow\CowVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */