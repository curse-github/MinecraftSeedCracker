/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class DesertWellFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  20 */   private static final BlockStatePredicate IS_SAND = BlockStatePredicate.forBlock(Blocks.SAND);
/*     */   
/*  22 */   private final BlockState sand = Blocks.SAND.defaultBlockState();
/*  23 */   private final BlockState sandSlab = Blocks.SANDSTONE_SLAB.defaultBlockState();
/*  24 */   private final BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
/*  25 */   private final BlockState water = Blocks.WATER.defaultBlockState();
/*     */ 
/*     */   
/*  28 */   public DesertWellFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/*  33 */     WorldGenLevel level = context.level();
/*  34 */     BlockPos origin = context.origin();
/*  35 */     origin = origin.above();
/*     */     
/*  37 */     while (level.isEmptyBlock(origin) && origin.getY() > level.getMinY() + 2) {
/*  38 */       origin = origin.below();
/*     */     }
/*     */     
/*  41 */     if (!IS_SAND.test(level.getBlockState(origin))) {
/*  42 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  47 */     for (int ox = -2; ox <= 2; ox++) {
/*  48 */       for (int oz = -2; oz <= 2; oz++) {
/*  49 */         if (level.isEmptyBlock(origin.offset(ox, -1, oz)) && level.isEmptyBlock(origin.offset(ox, -2, oz))) {
/*  50 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  56 */     for (int oy = -2; oy <= 0; oy++) {
/*  57 */       for (int ox = -2; ox <= 2; ox++) {
/*  58 */         for (int oz = -2; oz <= 2; oz++) {
/*  59 */           level.setBlock(origin.offset(ox, oy, oz), this.sandstone, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  65 */     level.setBlock(origin, this.water, 2);
/*  66 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  67 */       level.setBlock(origin.relative(direction), this.water, 2);
/*     */     }
/*     */ 
/*     */     
/*  71 */     BlockPos sandCenter = origin.below();
/*  72 */     level.setBlock(sandCenter, this.sand, 2);
/*  73 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  74 */       level.setBlock(sandCenter.relative(direction), this.sand, 2);
/*     */     }
/*     */ 
/*     */     
/*  78 */     for (int ox = -2; ox <= 2; ox++) {
/*  79 */       for (int oz = -2; oz <= 2; oz++) {
/*  80 */         if (ox == -2 || ox == 2 || oz == -2 || oz == 2) {
/*  81 */           level.setBlock(origin.offset(ox, 1, oz), this.sandstone, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     level.setBlock(origin.offset(2, 1, 0), this.sandSlab, 2);
/*  87 */     level.setBlock(origin.offset(-2, 1, 0), this.sandSlab, 2);
/*  88 */     level.setBlock(origin.offset(0, 1, 2), this.sandSlab, 2);
/*  89 */     level.setBlock(origin.offset(0, 1, -2), this.sandSlab, 2);
/*     */ 
/*     */     
/*  92 */     for (int ox = -1; ox <= 1; ox++) {
/*  93 */       for (int oz = -1; oz <= 1; oz++) {
/*  94 */         if (ox == 0 && oz == 0) {
/*  95 */           level.setBlock(origin.offset(ox, 4, oz), this.sandstone, 2);
/*     */         } else {
/*  97 */           level.setBlock(origin.offset(ox, 4, oz), this.sandSlab, 2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 103 */     for (int oy = 1; oy <= 3; oy++) {
/* 104 */       level.setBlock(origin.offset(-1, oy, -1), this.sandstone, 2);
/* 105 */       level.setBlock(origin.offset(-1, oy, 1), this.sandstone, 2);
/* 106 */       level.setBlock(origin.offset(1, oy, -1), this.sandstone, 2);
/* 107 */       level.setBlock(origin.offset(1, oy, 1), this.sandstone, 2);
/*     */     } 
/*     */     
/* 110 */     BlockPos waterCenter = origin;
/* 111 */     List<BlockPos> waterPositions = List.of(waterCenter, waterCenter.east(), waterCenter.south(), waterCenter.west(), waterCenter.north());
/*     */     
/* 113 */     RandomSource random = context.random();
/* 114 */     placeSusSand(level, ((BlockPos)Util.getRandom(waterPositions, random)).below(1));
/* 115 */     placeSusSand(level, ((BlockPos)Util.getRandom(waterPositions, random)).below(2));
/*     */     
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   private static void placeSusSand(WorldGenLevel level, BlockPos pos) {
/* 121 */     level.setBlock(pos, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 3);
/* 122 */     level.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK).ifPresent(e -> e.setLootTable(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, pos.asLong()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DesertWellFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */