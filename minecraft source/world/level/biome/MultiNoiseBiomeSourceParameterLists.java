/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class MultiNoiseBiomeSourceParameterLists {
/* 10 */   public static final ResourceKey<MultiNoiseBiomeSourceParameterList> NETHER = register("nether");
/* 11 */   public static final ResourceKey<MultiNoiseBiomeSourceParameterList> OVERWORLD = register("overworld");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
/* 14 */     HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
/* 15 */     context.register(NETHER, new MultiNoiseBiomeSourceParameterList(MultiNoiseBiomeSourceParameterList.Preset.NETHER, biomes));
/* 16 */     context.register(OVERWORLD, new MultiNoiseBiomeSourceParameterList(MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD, biomes));
/*    */   }
/*    */ 
/*    */   
/* 20 */   private static ResourceKey<MultiNoiseBiomeSourceParameterList> register(String name) { return ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterLists.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */