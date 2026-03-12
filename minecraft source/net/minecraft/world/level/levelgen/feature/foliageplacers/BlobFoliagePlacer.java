/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BlobFoliagePlacer extends FoliagePlacer {
/* 13 */   public static final MapCodec<BlobFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> blobParts(i).apply(i, BlobFoliagePlacer::new)); protected final int height;
/*    */   
/*    */   protected static <P extends BlobFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer> blobParts(RecordCodecBuilder.Instance<P> instance) {
/* 16 */     return foliagePlacerParts(instance).and(
/* 17 */         Codec.intRange(0, 16).fieldOf("height").forGetter(p -> Integer.valueOf(p.height)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlobFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
/* 24 */     super(radius, offset);
/* 25 */     this.height = height;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.BLOB_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 35 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 36 */       int currentRadius = Math.max(leafRadius + foliageAttachment.radiusOffset() - 1 - yo / 2, 0);
/* 37 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.height; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) { return (dx == currentRadius && dz == currentRadius && (random.nextInt(2) == 0 || y == 0)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BlobFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */