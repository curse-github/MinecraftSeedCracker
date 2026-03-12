/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class RandomSpreadFoliagePlacer extends FoliagePlacer {
/* 13 */   public static final MapCodec<RandomSpreadFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(i
/* 14 */         .group(
/* 15 */           IntProvider.codec(1, 512).fieldOf("foliage_height").forGetter(()), 
/* 16 */           Codec.intRange(0, 256).fieldOf("leaf_placement_attempts").forGetter(())))
/*    */       
/* 18 */       .apply(i, RandomSpreadFoliagePlacer::new));
/*    */   
/*    */   private final IntProvider foliageHeight;
/*    */   private final int leafPlacementAttempts;
/*    */   
/*    */   public RandomSpreadFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider foliageHeight, int leafPlacementAttempts) {
/* 24 */     super(radius, offset);
/*    */     
/* 26 */     this.foliageHeight = foliageHeight;
/* 27 */     this.leafPlacementAttempts = leafPlacementAttempts;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.RANDOM_SPREAD_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 37 */     BlockPos origin = foliageAttachment.pos();
/* 38 */     BlockPos.MutableBlockPos pos = origin.mutable();
/*    */     
/* 40 */     for (int i = 0; i < this.leafPlacementAttempts; i++) {
/* 41 */       pos.setWithOffset(origin, random.nextInt(leafRadius) - random.nextInt(leafRadius), random.nextInt(foliageHeight) - random.nextInt(foliageHeight), random.nextInt(leafRadius) - random.nextInt(leafRadius));
/* 42 */       tryPlaceLeaf(level, foliageSetter, random, config, pos);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.foliageHeight.sample(random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\RandomSpreadFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */