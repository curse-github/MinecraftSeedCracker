/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class CherryFoliagePlacer extends FoliagePlacer {
/* 13 */   public static final MapCodec<CherryFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(i.group(
/* 14 */           IntProvider.codec(4, 16).fieldOf("height").forGetter(()), 
/* 15 */           Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter(()), 
/* 16 */           Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter(()), 
/* 17 */           Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(()), 
/* 18 */           Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(())))
/* 19 */       .apply(i, CherryFoliagePlacer::new));
/*    */   
/*    */   private final IntProvider height;
/*    */   private final float wideBottomLayerHoleChance;
/*    */   private final float cornerHoleChance;
/*    */   private final float hangingLeavesChance;
/*    */   private final float hangingLeavesExtensionChance;
/*    */   
/*    */   public CherryFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height, float wideBottomLayerHoleChance, float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
/* 28 */     super(radius, offset);
/* 29 */     this.height = height;
/* 30 */     this.wideBottomLayerHoleChance = wideBottomLayerHoleChance;
/* 31 */     this.cornerHoleChance = cornerHoleChance;
/* 32 */     this.hangingLeavesChance = hangingLeavesChance;
/* 33 */     this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.CHERRY_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 64 */     boolean doubleTrunk = foliageAttachment.doubleTrunk();
/* 65 */     BlockPos foliagePos = foliageAttachment.pos().above(offset);
/*    */     
/* 67 */     int currentRadius = leafRadius + foliageAttachment.radiusOffset() - 1;
/*    */     
/* 69 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius - 2, foliageHeight - 3, doubleTrunk);
/* 70 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius - 1, foliageHeight - 4, doubleTrunk);
/*    */     
/* 72 */     for (int y = foliageHeight - 5; y >= 0; y--) {
/* 73 */       placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius, y, doubleTrunk);
/*    */     }
/*    */     
/* 76 */     placeLeavesRowWithHangingLeavesBelow(level, foliageSetter, random, config, foliagePos, currentRadius, -1, doubleTrunk, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/* 77 */     placeLeavesRowWithHangingLeavesBelow(level, foliageSetter, random, config, foliagePos, currentRadius - 1, -2, doubleTrunk, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.height.sample(random); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 87 */     if (y == -1 && (dx == currentRadius || dz == currentRadius) && random.nextFloat() < this.wideBottomLayerHoleChance) {
/* 88 */       return true;
/*    */     }
/*    */     
/* 91 */     boolean corner = (dx == currentRadius && dz == currentRadius);
/* 92 */     boolean wideLayer = (currentRadius > 2);
/*    */     
/* 94 */     if (wideLayer)
/*    */     {
/* 96 */       return (corner || (dx + dz > currentRadius * 2 - 2 && random.nextFloat() < this.cornerHoleChance));
/*    */     }
/* 98 */     return (corner && random.nextFloat() < this.cornerHoleChance);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\CherryFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */