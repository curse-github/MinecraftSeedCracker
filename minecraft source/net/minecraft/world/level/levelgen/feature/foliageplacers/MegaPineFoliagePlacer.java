/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaPineFoliagePlacer extends FoliagePlacer {
/* 14 */   public static final MapCodec<MegaPineFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(
/* 15 */         IntProvider.codec(0, 24).fieldOf("crown_height").forGetter(()))
/* 16 */       .apply(i, MegaPineFoliagePlacer::new));
/*    */   
/*    */   private final IntProvider crownHeight;
/*    */   
/*    */   public MegaPineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider crownHeight) {
/* 21 */     super(radius, offset);
/* 22 */     this.crownHeight = crownHeight;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected FoliagePlacerType<?> type() { return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedReader level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 32 */     BlockPos foliagePos = foliageAttachment.pos();
/*    */     
/* 34 */     int prevRadius = 0;
/* 35 */     for (int yy = foliagePos.getY() - foliageHeight + offset; yy <= foliagePos.getY() + offset; yy++) {
/* 36 */       int jaggedRadius, yo = foliagePos.getY() - yy;
/* 37 */       int smoothRadius = leafRadius + foliageAttachment.radiusOffset() + Mth.floor(yo / foliageHeight * 3.5F);
/*    */       
/* 39 */       if (yo > 0 && smoothRadius == prevRadius && (yy & true) == 0) {
/* 40 */         jaggedRadius = smoothRadius + 1;
/*    */       } else {
/* 42 */         jaggedRadius = smoothRadius;
/*    */       } 
/*    */       
/* 45 */       placeLeavesRow(level, foliageSetter, random, config, new BlockPos(foliagePos.getX(), yy, foliagePos.getZ()), jaggedRadius, 0, foliageAttachment.doubleTrunk());
/* 46 */       prevRadius = smoothRadius;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) { return this.crownHeight.sample(random); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 57 */     if (dx + dz >= 7) {
/* 58 */       return true;
/*    */     }
/* 60 */     return (dx * dx + dz * dz > currentRadius * currentRadius);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaPineFoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */