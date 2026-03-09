/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class TrunkPlacerType<P extends TrunkPlacer> extends Object {
/*  8 */   public static final TrunkPlacerType<StraightTrunkPlacer> STRAIGHT_TRUNK_PLACER = register("straight_trunk_placer", StraightTrunkPlacer.CODEC);
/*  9 */   public static final TrunkPlacerType<ForkingTrunkPlacer> FORKING_TRUNK_PLACER = register("forking_trunk_placer", ForkingTrunkPlacer.CODEC);
/* 10 */   public static final TrunkPlacerType<GiantTrunkPlacer> GIANT_TRUNK_PLACER = register("giant_trunk_placer", GiantTrunkPlacer.CODEC);
/* 11 */   public static final TrunkPlacerType<MegaJungleTrunkPlacer> MEGA_JUNGLE_TRUNK_PLACER = register("mega_jungle_trunk_placer", MegaJungleTrunkPlacer.CODEC);
/* 12 */   public static final TrunkPlacerType<DarkOakTrunkPlacer> DARK_OAK_TRUNK_PLACER = register("dark_oak_trunk_placer", DarkOakTrunkPlacer.CODEC);
/* 13 */   public static final TrunkPlacerType<FancyTrunkPlacer> FANCY_TRUNK_PLACER = register("fancy_trunk_placer", FancyTrunkPlacer.CODEC);
/* 14 */   public static final TrunkPlacerType<BendingTrunkPlacer> BENDING_TRUNK_PLACER = register("bending_trunk_placer", BendingTrunkPlacer.CODEC);
/* 15 */   public static final TrunkPlacerType<UpwardsBranchingTrunkPlacer> UPWARDS_BRANCHING_TRUNK_PLACER = register("upwards_branching_trunk_placer", UpwardsBranchingTrunkPlacer.CODEC);
/* 16 */   public static final TrunkPlacerType<CherryTrunkPlacer> CHERRY_TRUNK_PLACER = register("cherry_trunk_placer", CherryTrunkPlacer.CODEC);
/*    */   private final MapCodec<P> codec;
/*    */   
/* 19 */   private static <P extends TrunkPlacer> TrunkPlacerType<P> register(String name, MapCodec<P> codec) { return (TrunkPlacerType)Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, name, new TrunkPlacerType(codec)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   private TrunkPlacerType(MapCodec<P> codec) { this.codec = codec; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public MapCodec<P> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacerType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */