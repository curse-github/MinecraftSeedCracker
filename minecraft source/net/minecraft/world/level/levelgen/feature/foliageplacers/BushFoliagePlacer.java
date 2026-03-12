/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BushFoliagePlacer extends BlobFoliagePlacer {
/* 11 */   public static final MapCodec<BushFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> blobParts(i).apply(i, BushFoliagePlacer::new));
/*    */ 
/*    */   
/* 14 */   public BushFoliagePlacer(IntProvider radius, IntProvider offset, int height) { super(radius, offset, height); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.BUSH_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 24 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 25 */       int currentRadius = leafRadius + foliageAttachment.radiusOffset() - 1 - yo;
/* 26 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) { return (dx == currentRadius && dz == currentRadius && random.nextInt(2) == 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BushFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */