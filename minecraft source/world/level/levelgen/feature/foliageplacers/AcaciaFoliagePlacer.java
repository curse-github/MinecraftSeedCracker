/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class AcaciaFoliagePlacer extends FoliagePlacer {
/* 12 */   public static final MapCodec<AcaciaFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).apply(i, AcaciaFoliagePlacer::new));
/*    */ 
/*    */   
/* 15 */   public AcaciaFoliagePlacer(IntProvider radius, IntProvider offset) { super(radius, offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.ACACIA_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 25 */     boolean doubleTrunk = foliageAttachment.doubleTrunk();
/* 26 */     BlockPos foliagePos = foliageAttachment.pos().above(offset);
/*    */     
/* 28 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, leafRadius + foliageAttachment.radiusOffset(), -1 - foliageHeight, doubleTrunk);
/* 29 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, leafRadius - 1, -foliageHeight, doubleTrunk);
/* 30 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, leafRadius + foliageAttachment.radiusOffset() - 1, 0, doubleTrunk);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return 0; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 40 */     if (y == 0)
/*    */     {
/* 42 */       return ((dx > 1 || dz > 1) && dx != 0 && dz != 0);
/*    */     }
/* 44 */     return (dx == currentRadius && dz == currentRadius && currentRadius > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\AcaciaFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */