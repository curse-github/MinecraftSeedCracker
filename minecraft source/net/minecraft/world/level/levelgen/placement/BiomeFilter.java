/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BiomeFilter
/*    */   extends PlacementFilter
/*    */ {
/* 14 */   private static final BiomeFilter INSTANCE = new BiomeFilter();
/*    */   
/* 16 */   public static MapCodec<BiomeFilter> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static BiomeFilter biome() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 27 */     PlacedFeature feature = (PlacedFeature)context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
/* 28 */     Holder<Biome> biome = context.getLevel().getBiome(origin);
/* 29 */     return context.generator().getBiomeGenerationSettings(biome).hasFeature(feature);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public PlacementModifierType<?> type() { return PlacementModifierType.BIOME_FILTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\BiomeFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */