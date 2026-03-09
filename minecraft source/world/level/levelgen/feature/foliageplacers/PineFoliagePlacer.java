/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class PineFoliagePlacer extends FoliagePlacer {
/* 11 */   public static final MapCodec<PineFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(
/* 12 */         IntProvider.codec(0, 24).fieldOf("height").forGetter(()))
/* 13 */       .apply(i, PineFoliagePlacer::new));
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   public PineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
/* 18 */     super(radius, offset);
/* 19 */     this.height = height;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.PINE_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 29 */     int currentRadius = 0;
/*    */     
/* 31 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 32 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */       
/* 34 */       if (currentRadius >= 1 && yo == offset - foliageHeight + 1) {
/* 35 */         currentRadius--;
/* 36 */       } else if (currentRadius < leafRadius + foliageAttachment.radiusOffset()) {
/* 37 */         currentRadius++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int foliageRadius(RandomSource random, int trunkHeight) { return super.foliageRadius(random, trunkHeight) + random.nextInt(Math.max(trunkHeight + 1, 1)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.height.sample(random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) { return (dx == currentRadius && dz == currentRadius && currentRadius > 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\PineFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */