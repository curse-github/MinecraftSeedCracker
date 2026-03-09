/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class FancyFoliagePlacer extends BlobFoliagePlacer {
/* 12 */   public static final MapCodec<FancyFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> blobParts(i).apply(i, FancyFoliagePlacer::new));
/*    */ 
/*    */   
/* 15 */   public FancyFoliagePlacer(IntProvider radius, IntProvider offset, int height) { super(radius, offset, height); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.FANCY_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 25 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 26 */       int currentRadius = leafRadius + ((yo == offset || yo == offset - foliageHeight) ? 0 : 1);
/* 27 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) { return (Mth.square(dx + 0.5F) + Mth.square(dz + 0.5F) > (currentRadius * currentRadius)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FancyFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */