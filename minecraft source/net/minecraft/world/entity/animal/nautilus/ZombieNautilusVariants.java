/*    */ package net.minecraft.world.entity.animal.nautilus;
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
/*    */ public class ZombieNautilusVariants {
/* 17 */   public static final ResourceKey<ZombieNautilusVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<ZombieNautilusVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<ZombieNautilusVariant> DEFAULT = TEMPERATE;
/*    */ 
/*    */   
/* 22 */   private static ResourceKey<ZombieNautilusVariant> createKey(Identifier id) { return ResourceKey.create(Registries.ZOMBIE_NAUTILUS_VARIANT, id); }
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ZombieNautilusVariant> context) {
/* 26 */     register(context, TEMPERATE, ZombieNautilusVariant.ModelType.NORMAL, "zombie_nautilus", SpawnPrioritySelectors.fallback(0));
/* 27 */     register(context, WARM, ZombieNautilusVariant.ModelType.WARM, "zombie_nautilus_coral", BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ZombieNautilusVariant> context, ResourceKey<ZombieNautilusVariant> name, ZombieNautilusVariant.ModelType modelType, String textureName, TagKey<Biome> spawnBiome) {
/* 31 */     HolderSet.Named named = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
/* 32 */     register(context, name, modelType, textureName, SpawnPrioritySelectors.single(new BiomeCheck(named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ZombieNautilusVariant> context, ResourceKey<ZombieNautilusVariant> name, ZombieNautilusVariant.ModelType modelType, String textureName, SpawnPrioritySelectors selectors) {
/* 36 */     Identifier textureId = Identifier.withDefaultNamespace("entity/nautilus/" + textureName);
/* 37 */     context.register(name, new ZombieNautilusVariant(new ModelAndTexture(modelType, textureId), selectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */