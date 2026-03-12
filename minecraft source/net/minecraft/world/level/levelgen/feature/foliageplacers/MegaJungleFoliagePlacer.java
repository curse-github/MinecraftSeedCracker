/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaJungleFoliagePlacer extends FoliagePlacer {
/* 13 */   public static final MapCodec<MegaJungleFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(
/* 14 */         Codec.intRange(0, 16).fieldOf("height").forGetter(()))
/* 15 */       .apply(i, MegaJungleFoliagePlacer::new));
/*    */   
/*    */   protected final int height;
/*    */   
/*    */   public MegaJungleFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
/* 20 */     super(radius, offset);
/* 21 */     this.height = height;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 32 */     int leafHeight = foliageAttachment.doubleTrunk() ? foliageHeight : (1 + random.nextInt(2));
/*    */     
/* 34 */     for (int yo = offset; yo >= offset - leafHeight; yo--) {
/* 35 */       int currentRadius = leafRadius + foliageAttachment.radiusOffset() + 1 - yo;
/* 36 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.height; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 47 */     if (dx + dz >= 7) {
/* 48 */       return true;
/*    */     }
/* 50 */     return (dx * dx + dz * dz > currentRadius * currentRadius);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaJungleFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */