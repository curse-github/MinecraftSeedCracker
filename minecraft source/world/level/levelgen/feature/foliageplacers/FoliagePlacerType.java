/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FoliagePlacerType<P extends FoliagePlacer> extends Object {
/*  8 */   public static final FoliagePlacerType<BlobFoliagePlacer> BLOB_FOLIAGE_PLACER = register("blob_foliage_placer", BlobFoliagePlacer.CODEC);
/*  9 */   public static final FoliagePlacerType<SpruceFoliagePlacer> SPRUCE_FOLIAGE_PLACER = register("spruce_foliage_placer", SpruceFoliagePlacer.CODEC);
/* 10 */   public static final FoliagePlacerType<PineFoliagePlacer> PINE_FOLIAGE_PLACER = register("pine_foliage_placer", PineFoliagePlacer.CODEC);
/* 11 */   public static final FoliagePlacerType<AcaciaFoliagePlacer> ACACIA_FOLIAGE_PLACER = register("acacia_foliage_placer", AcaciaFoliagePlacer.CODEC);
/* 12 */   public static final FoliagePlacerType<BushFoliagePlacer> BUSH_FOLIAGE_PLACER = register("bush_foliage_placer", BushFoliagePlacer.CODEC);
/* 13 */   public static final FoliagePlacerType<FancyFoliagePlacer> FANCY_FOLIAGE_PLACER = register("fancy_foliage_placer", FancyFoliagePlacer.CODEC);
/* 14 */   public static final FoliagePlacerType<MegaJungleFoliagePlacer> MEGA_JUNGLE_FOLIAGE_PLACER = register("jungle_foliage_placer", MegaJungleFoliagePlacer.CODEC);
/* 15 */   public static final FoliagePlacerType<MegaPineFoliagePlacer> MEGA_PINE_FOLIAGE_PLACER = register("mega_pine_foliage_placer", MegaPineFoliagePlacer.CODEC);
/* 16 */   public static final FoliagePlacerType<DarkOakFoliagePlacer> DARK_OAK_FOLIAGE_PLACER = register("dark_oak_foliage_placer", DarkOakFoliagePlacer.CODEC);
/* 17 */   public static final FoliagePlacerType<RandomSpreadFoliagePlacer> RANDOM_SPREAD_FOLIAGE_PLACER = register("random_spread_foliage_placer", RandomSpreadFoliagePlacer.CODEC);
/* 18 */   public static final FoliagePlacerType<CherryFoliagePlacer> CHERRY_FOLIAGE_PLACER = register("cherry_foliage_placer", CherryFoliagePlacer.CODEC);
/*    */   private final MapCodec<P> codec;
/*    */   
/* 21 */   private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String name, MapCodec<P> codec) { return (FoliagePlacerType)Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, name, new FoliagePlacerType(codec)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private FoliagePlacerType(MapCodec<P> codec) { this.codec = codec; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public MapCodec<P> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */